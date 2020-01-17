package app;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;

public class AddNewCompany implements Initializable {

	// ------------------- add company
	@FXML
	TextField CoName;
	@FXML
	ComboBox<String> CoLocation;
	@FXML
	CheckBox Involoved;

	@FXML
	VBox Skills;

	ObservableList<CheckBox> ObCheckBox = FXCollections.observableArrayList();

	public void initialize(URL location, ResourceBundle resources) {
		CoLocation.getItems().addAll("Palestine", "Israel", "both");
		try {
			SkillsView();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void SkillsView() throws SQLException {

		ResultSet resultSet = Main.stmt.executeQuery(" SELECT * FROM `skills` WHERE 1");

		while (resultSet.next()) {

			CheckBox c = new CheckBox(resultSet.getString("Name"));
			c.setId(resultSet.getString("ID"));
			Skills.getChildren().add(c);

			EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {

				public void handle(ActionEvent e) {
					if (c.isSelected())
						ObCheckBox.add(c);

					else
						ObCheckBox.remove(c);
				}

			};

			// set event to checkbox
			c.setOnAction(event);
		}
		//

	}

	String skillsLine = "";

	public void AddCompany() throws SQLException {
		int lastID;
		if (!CoName.getText().equals("") & CoLocation.getValue() != null) {

			Main.stmt.executeUpdate(
					"INSERT INTO `companies`( `Name`, `Location`, `Involved`) VALUES ('" + CoName.getText() + "','"
							+ CoLocation.getValue() + "','" + Involoved(Involoved.isSelected()) + "')");

			ResultSet resultSet = Main.stmt.executeQuery(" SELECT ID FROM companies ORDER BY ID DESC LIMIT 1");
			resultSet.next();
			lastID = Integer.parseInt(resultSet.getString("ID"));

			ObCheckBox.forEach((node) -> {
				skillsLine += node.getText() + " ,";

				try {
					//
					Main.stmt.executeUpdate("INSERT INTO `companies_skills`( `company_id`, `skill_ID`) VALUES ('"
							+ lastID + "','" + node.getId() + "')");

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			});



			Controller.CompaniesOBList.add((new Company(lastID, CoName.getText(), CoLocation.getValue(),
					Involoved(Involoved.isSelected()), skillsLine)));

			Controller.addCompanyStage.close();
			CoName.clear();
		}

	}

	String Involoved(boolean a) {
		if (a == true)
			return "yes";
		else
			return "no";
	}

}

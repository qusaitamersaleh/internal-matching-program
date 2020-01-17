package app;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class EditCompany implements Initializable {

	@FXML
	TextField CoName;
	@FXML
	ComboBox<String> CoLocation;
	@FXML
	CheckBox Involoved;

	@FXML
	VBox Skills;

	Company company = Controller.EditCompany;
	ObservableList<CheckBox> SkillsTOadd = FXCollections.observableArrayList();
	ObservableList<CheckBox> SkillsToDelete = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		CoName.setText(company.getName());

		CoLocation.getItems().addAll("Palestine", "Israel", "both");
		CoLocation.setValue(company.getLocation());

		if (company.getInvolved().equals("yes"))
			Involoved.setSelected(true);
		else
			Involoved.setSelected(false);

		ResultSet resultSet = null;
		try {
			resultSet = Main.stmt.executeQuery(" SELECT * FROM `skills` WHERE 1");

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			while (resultSet.next()) {

				CheckBox c = new CheckBox(resultSet.getString("Name"));
				c.setId(resultSet.getString("ID"));

				if (contains(company.getSkills(), resultSet.getString("Name")))
					c.setSelected(true);

				else
					c.setSelected(false);

				Skills.getChildren().add(c);

				EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {

					public void handle(ActionEvent e) {
						if (c.isSelected()) {
							SkillsTOadd.add(c); // wehn unselected is selected
							for (int i = 0; i < SkillsToDelete.size(); i++) {
								if (SkillsToDelete.get(i).getId() == c.getId()) {
									SkillsToDelete.remove(i);
									break;
								}
							}
						}

						else {
							SkillsToDelete.add(c); // when unselected
							for (int i = 0; i < SkillsTOadd.size(); i++) {
								if (SkillsTOadd.get(i).getId() == c.getId()) {
									SkillsTOadd.remove(i);
									break;
								}
							}
							// SkillsTOadd.remove(c);

						}

					}

				};

				// set event to checkbox
				c.setOnAction(event);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	String updatedSkills = "";

	public void SaveEdit() throws SQLException {

		if (!CoName.getText().equals("") & CoLocation.getValue() != null) {

			Main.stmt.executeUpdate(
					"UPDATE `companies` SET `Name`='" + CoName.getText() + "',`Location`='" + CoLocation.getValue()
							+ "',`Involved`='" + Involoved(Involoved.isSelected()) + "' WHERE ID=" + company.getID());

			SkillsTOadd.forEach((node) -> {
				updatedSkills += node + ", ";
				try {

					Main.stmt.executeUpdate("INSERT INTO `companies_skills`( `company_id`, `skill_ID`) VALUES ('"
							+ company.getID() + "','" + node.getId() + "')");

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

			SkillsToDelete.forEach((node) -> {
				try {
					Main.stmt.executeUpdate("DELETE FROM `companies_skills` WHERE company_id='" + company.getID()
							+ "' AND skill_ID='" + node.getId() + "'");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

		}

		Controller.editCompanyStage.close();
	}

	String Involoved(boolean a) {
		if (a == true)
			return "yes";
		else
			return "no";
	}

	boolean contains(String sentece, String word) {
		ArrayList<String> S = new ArrayList<>();
		String t = "";
		for (int i = 0; i < sentece.length(); i++) {

			if ((sentece.charAt(i) == ',' & sentece.charAt(i + 1) == ' ')) {
				S.add(t);
				// m.m(t);
				t = "";
				i++;
			} else
				t += sentece.charAt(i);
		}

		if (S.contains(word))
			return true;
		else
			return false;
	}

}

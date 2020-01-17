package app;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class AddNewParticipant implements Initializable {

	@FXML
	TextField participantName;
	@FXML
	TextField participantCycle;
	@FXML
	CheckBox Matched;

	@FXML
	VBox Skills;

	ObservableList<CheckBox> ObCheckBox = FXCollections.observableArrayList();

	public void initialize(URL location, ResourceBundle resources) {

		try {
			SkillsView();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	String skillsLine = "";

	public void addNewParticipant() throws SQLException {
		int lastID;
		if (!participantCycle.getText().equals("") & !participantName.getText().equals("")) {

			Main.stmt.executeUpdate("INSERT INTO `participants`( `Name`, `CycleN`, `Matched`) VALUES ('"
					+ participantName.getText() + "','" + Integer.parseInt(participantCycle.getText()) + "','"
					+ Matched(Matched.isSelected()) + "')");

			ResultSet resultSet = Main.stmt.executeQuery(" SELECT ID FROM participants ORDER BY ID DESC LIMIT 1");
			resultSet.next();
			lastID = Integer.parseInt(resultSet.getString("ID"));

			ObCheckBox.forEach((node) -> {
				skillsLine += node.getText() + " ,";

				try {

					Main.stmt2
							.executeUpdate("INSERT INTO `participants_skills`( `participant_ID`, `skill_ID`) VALUES ('"
									+ lastID + "','" + node.getId() + "')");

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			});

			Controller.ParticipantsOBList.add(new Participant(lastID, Integer.parseInt(participantCycle.getText()),
					participantName.getText(), Matched(Matched.isSelected()), skillsLine));

			Controller.addParticipantStage.close();
			participantName.clear();
		}

	}

	String Matched(boolean a) {
		if (a == true)
			return "yes";
		else
			return "no";
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
			c.setOnAction(event);

		}
	}
}

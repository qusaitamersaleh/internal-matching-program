package app;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class MatchingParticipant implements Initializable {

	@FXML
	Text Cycle = new Text();
	@FXML
	Text ParicipantName = new Text();
	@FXML
	TableView<Company> CompaniesToMatch = new TableView<Company>();
	@FXML
	TableColumn<Company, String> Name = new TableColumn<>("Name");
	@FXML
	TableColumn<Company, String> location = new TableColumn<>("location");
	@FXML
	TableColumn<Company, String> Involved = new TableColumn<>("Involved");
	@FXML
	TableColumn<Company, String> MatchedSkills = new TableColumn<>("MatchedSkills");

	ObservableList<Company> MatchedCoOBlist = FXCollections.observableArrayList();

	public void setMatched() {

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		ParicipantName.setText(Controller.getParticipantMatched.getName());
		Cycle.setText("" + Controller.getParticipantMatched.getCycle());
		try {
			showCompanies();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void showCompanies() throws SQLException {

		ResultSet res = Main.stmt.executeQuery(
				"SELECT Distinct companies.ID, Name ,Location, Involved FROM `companies`  join companies_skills on companies_skills.company_id = companies.ID inner join participants_skills on participants_skills.skill_ID = companies_skills.skill_ID WHERE participants_skills.participant_ID ="
						+ Controller.getParticipantMatched.getID());
		int id;

		String SkillsString = "";
		while (res.next()) {
			id = Integer.parseInt(res.getString("ID"));

			ResultSet Skills = Main.stmt2.executeQuery(
					" SELECT skills.Name from  skills join participants_skills on participants_skills.skill_ID =  skills.ID join companies_skills on companies_skills.skill_ID = skills.ID where companies_skills.company_id ="+id+" AND participants_skills.participant_ID="+Controller.getParticipantMatched.getID());
			SkillsString = "";
			while (Skills.next()) {

				SkillsString += Skills.getString("Name") + ", ";

			}

			MatchedCoOBlist.add(new Company(id, res.getString("Name"), res.getString("Location"),
					res.getString("Involved"), SkillsString));

		}

		// System.out.println(OBlist.get(0).getName());

		this.Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
		this.location.setCellValueFactory(new PropertyValueFactory<>("Location"));
		this.Involved.setCellValueFactory(new PropertyValueFactory<>("Involved"));
		this.MatchedSkills.setCellValueFactory(new PropertyValueFactory<>("Skills"));
		CompaniesToMatch.setItems(MatchedCoOBlist);

	}
}

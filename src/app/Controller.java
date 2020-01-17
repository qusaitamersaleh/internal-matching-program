package app;

import java.awt.Button;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import jxl.write.Number;

public class Controller implements Initializable {

	@FXML
	TextField newSkill;

	@FXML
	ListView<String> Skills;

	@FXML
	ComboBox<String> viewSkillsToDelete;

	// ---------------- --- --- Company section
	@FXML
	TableView<Company> CompaniesTable = new TableView<Company>();
	@FXML
	TableColumn<Company, String> CompanyCoName = new TableColumn<>("name");
	@FXML
	TableColumn<Company, String> CompanyLocation = new TableColumn<>("location");
	@FXML
	TableColumn<Company, String> CompanyInvolved = new TableColumn<>("Involved");
	@FXML
	TableColumn<Company, String> CompanySkills = new TableColumn<>("Skills");
	@FXML
	TableColumn<Company, Integer> CoID = new TableColumn<>("ID");

	static ObservableList<Company> CompaniesOBList = FXCollections.observableArrayList();
	static ObservableList<Participant> ParticipantsOBList = FXCollections.observableArrayList();
	// -------------------------------

	static Company EditCompany;
	static Stage addCompanyStage = new Stage();
	static Stage editCompanyStage = new Stage();

	// ----------------------Participants
	static Participant EditParticipant;
	static Stage addParticipantStage = new Stage();
	static Stage EditParticipantStage = new Stage();
	@FXML
	TableView<Participant> participantsTable = new TableView<Participant>();
	@FXML
	TableColumn<Participant, String> ParticipantName = new TableColumn<>("Name");
	@FXML
	TableColumn<Participant, Integer> ParticipantCycle = new TableColumn<>("Cycle");
	@FXML
	TableColumn<Participant, String> ParticipantMatched = new TableColumn<>("Matched");
	@FXML
	TableColumn<Participant, String> ParticipantSkills = new TableColumn<>("Skills");
	@FXML
	TableColumn<Participant, Integer> PartipantID = new TableColumn<>("ID");
	// -------------- matching elements
	static Participant getParticipantMatched;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		try {
			getSkills();
			GetCompanies();
			GetParticipants();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addCompanyRow() {
		this.CompaniesTable.setItems(CompaniesOBList);
	}

	public void refresh() {
		try {

			GetCompanies();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getSkills() throws SQLException {

		viewSkillsToDelete.getItems().clear();
		Skills.getItems().clear();
		ResultSet resultSet = Main.stmt.executeQuery(" SELECT  `Name` FROM `skills` WHERE 1");
		// ObservableList<Skills> S;

		while (resultSet.next()) {

			Skills.getItems().add(resultSet.getString(1));
			// viewSkillsToDelete.add(new Skills(resultSet.getInt(1),
			// resultSet.getString(2)));
			viewSkillsToDelete.getItems().add(resultSet.getString(1));
			// viewSkillsToDelete.getItems().add((S));

		}

	}

	public void addSkill() throws SQLException {
		if (newSkill.getText().equals(""))
			return;
		Main.stmt.executeUpdate("INSERT INTO `skills`(`Name`) VALUES ('" + newSkill.getText() + "')");
		newSkill.clear();
		getSkills();
	}

	public void deleteSkill() throws SQLException {

		// I know this is wrong because I'm not useing the ID
		Main.stmt.execute("DELETE FROM `skills` WHERE `Name` ='" + viewSkillsToDelete.getValue() + "'");
		getSkills();

	}
	// -------------------------

	public void GetCompanies() throws SQLException {
		CompaniesOBList.clear();
		ResultSet res = Main.stmt.executeQuery("SELECT `ID`, `Name`, `Location`, `Involved` FROM `companies` WHERE 1");
		int id;

		while (res.next()) {
			id = Integer.parseInt(res.getString("ID"));

			ResultSet Skills = Main.stmt2.executeQuery(
					"SELECT skills.Name from  skills inner join companies_skills on companies_skills.skill_ID = skills.ID where companies_skills.company_id ="
							+ id);
			String SkillsString = "";
			while (Skills.next()) {

				SkillsString += Skills.getString("Name") + ", ";

			}
			CompaniesOBList.add(new Company(id, res.getString("Name"), res.getString("Location"),
					res.getString("Involved"), SkillsString));

		}

		// System.out.println(OBlist.get(0).getName());
		this.CoID.setCellValueFactory(new PropertyValueFactory<>("ID"));
		this.CompanyCoName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		this.CompanyLocation.setCellValueFactory(new PropertyValueFactory<>("Location"));
		this.CompanyInvolved.setCellValueFactory(new PropertyValueFactory<>("Involved"));
		this.CompanySkills.setCellValueFactory(new PropertyValueFactory<>("Skills"));
		CompaniesTable.setItems(CompaniesOBList);

	}

	public void newCompanyToTable(int id, String name, String location, String Involved) throws SQLException {
		ResultSet Skills = Main.stmt2.executeQuery(
				"SELECT skills.Name from  skills inner join companies_skills on companies_skills.skill_ID = skills.ID where companies_skills.company_id ="
						+ id);
		String SkillsString = "";
		while (Skills.next()) {
			SkillsString += Skills.getString("Name") + ", ";

		}

		CompaniesOBList.add(new Company(id, name, location, Involved, SkillsString));

		CompaniesTable.setItems(CompaniesOBList);

	}

	public void editCompany() {

		EditCompany = CompaniesTable.getSelectionModel().getSelectedItem();

		Parent c;
		try {
			c = FXMLLoader.load(getClass().getResource("/app/EditCompany.fxml"));
			editCompanyStage.setTitle("Edit Company");
			editCompanyStage.setScene(new Scene(c));
			editCompanyStage.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}

	}

	public void DeleteCompany() throws SQLException {

		Company selectedItem = CompaniesTable.getSelectionModel().getSelectedItem();
		CompaniesTable.getItems().remove(selectedItem);
		Main.stmt.executeUpdate("DELETE FROM `companies` WHERE ID =" + selectedItem.getID());

	}

	String Involoved(boolean a) {
		if (a = true)
			return "yes";
		else
			return "no";
	}

	public void NewCompanyPage() throws IOException {

		Parent B = FXMLLoader.load(getClass().getResource("/app/NewCompany.fxml"));
		addCompanyStage.setTitle("Add new Company");
		addCompanyStage.setScene(new Scene(B));
		// primaryStage.setFullScreen(true);
		addCompanyStage.show();

	}

	// participants ----------------------------------------------------------
	public void newParticipant() throws IOException {
		Parent p = FXMLLoader.load(getClass().getResource("/app/NewParticipant.fxml"));
		addParticipantStage.setTitle("Add new Participant");
		addParticipantStage.setScene(new Scene(p));
		// primaryStage.setFullScreen(true);
		addParticipantStage.show();

	}

	private void GetParticipants() throws SQLException {
		ParticipantsOBList.clear();

		ResultSet res = Main.stmt.executeQuery("SELECT * FROM `participants` WHERE 1");
		int id;
		String SkillsString = "";
		while (res.next()) {

			id = Integer.parseInt(res.getString("ID"));

			ResultSet Skills = Main.stmt2.executeQuery(
					"SELECT skills.Name from  skills inner join participants_skills on participants_skills.skill_ID = skills.ID where participants_skills.participant_ID ="
							+ id);

			while (Skills.next()) {

				SkillsString += Skills.getString("Name") + ", ";

			}
			Participant a = new Participant(id, Integer.parseInt(res.getString("CycleN")), res.getString("name"),
					res.getString("Matched"), SkillsString);

			ParticipantsOBList.add(a);

			SkillsString = "";
		}

		this.PartipantID.setCellValueFactory(new PropertyValueFactory<>("ID"));
		this.ParticipantName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		this.ParticipantCycle.setCellValueFactory(new PropertyValueFactory<>("Cycle"));
		this.ParticipantMatched.setCellValueFactory(new PropertyValueFactory<>("Matched"));
		this.ParticipantSkills.setCellValueFactory(new PropertyValueFactory<>("Skills"));

		participantsTable.setItems(ParticipantsOBList);
	}

	public void editParticipant() throws IOException {
		if (participantsTable.getSelectionModel().getSelectedItem() != null) {
			EditParticipant = participantsTable.getSelectionModel().getSelectedItem();

			Parent c;

			c = FXMLLoader.load(getClass().getResource("/app/EditParticipant.fxml"));
			EditParticipantStage.setTitle("Edit Participant");
			EditParticipantStage.setScene(new Scene(c));
			EditParticipantStage.show();

		}
	}

	public void refreshParticipantsTable() throws SQLException {
		GetParticipants();

	}

	public void DeleteParticipant() throws SQLException {
		Participant selectedItem = participantsTable.getSelectionModel().getSelectedItem();

		participantsTable.getItems().remove(selectedItem);
		Main.stmt.executeUpdate("DELETE FROM `participants` WHERE ID =" + selectedItem.getID());

	}

	String Matched(boolean a) {
		if (a = true)
			return "yes";
		else
			return "no";
	}

	public void MatchParticipant() throws SQLException, IOException {
		if (participantsTable.getSelectionModel().getSelectedItem() != null) {
			getParticipantMatched = participantsTable.getSelectionModel().getSelectedItem();
			Parent d;

			d = FXMLLoader.load(getClass().getResource("/app/MatchParticipant.fxml"));
			editCompanyStage.setTitle("Match Participant");
			editCompanyStage.setScene(new Scene(d));
			editCompanyStage.show();

		}

	}

	// -------------------- Exportations
	@FXML
	TextField SelectedPath;
	@FXML
	AnchorPane BackUpAncor;
	@FXML
	Button ExportParticipants, ExportCompanies;

	public void HandelBouttonAction() {

		final DirectoryChooser chooser = new DirectoryChooser();
		// chooser.setTitle("JavaFX Projects");
		Stage a = (Stage) BackUpAncor.getScene().getWindow();

		File file = chooser.showDialog(a);

		if (file != null) {
			SelectedPath.setText(file.getAbsolutePath());

		}

	}

	public void ExportParticipants() throws IOException, RowsExceededException, WriteException, SQLException {
		// 1. Create an Excel file
		WritableWorkbook myFirstWbook = null;
		LocalDate date = LocalDate.now();

		myFirstWbook = Workbook.createWorkbook(
				new File(SelectedPath.getText() + "\\Participants " + date.getMonth() + "-" + date.getYear() + ".xls"));

		// create an Excel sheet
		WritableSheet excelSheet = myFirstWbook.createSheet("Sheet 1", 0);

		// add something into the Excel sheet
		Label label = new Label(0, 0, "Name");
		excelSheet.addCell(label);
		label = new Label(1, 0, "Cycle");
		excelSheet.addCell(label);
		label = new Label(2, 0, "Matched");
		excelSheet.addCell(label);
		label = new Label(3, 0, "Skills");
		excelSheet.addCell(label);

		Number number;

		ResultSet res = Main.stmt.executeQuery("SELECT * FROM `participants` WHERE 1");
		int id;
		String SkillsString = "";
		int con = 1;
		while (res.next()) {

			id = Integer.parseInt(res.getString("ID"));

			ResultSet Skills = Main.stmt2.executeQuery(
					"SELECT skills.Name from  skills inner join participants_skills on participants_skills.skill_ID = skills.ID where participants_skills.participant_ID ="
							+ id);

			while (Skills.next()) {

				SkillsString += Skills.getString("Name") + ", ";

			}
			label = new Label(0, con, res.getString("name"));
			excelSheet.addCell(label);
			label = new Label(1, con, res.getString("CycleN"));
			excelSheet.addCell(label);
			label = new Label(2, con, res.getString("Matched"));
			excelSheet.addCell(label);
			label = new Label(3, con, SkillsString);
			excelSheet.addCell(label);
			SkillsString = "";
			con++;
		}

		// number = new Number(0, 2, 2);
		// excelSheet.addCell(number);

		myFirstWbook.write();

		if (myFirstWbook != null) {
			try {
				myFirstWbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}

	}

	public void ExportCompanies() throws IOException, RowsExceededException, WriteException, SQLException {

		// 1. Create an Excel file
		WritableWorkbook myFirstWbook = null;
		LocalDate date = LocalDate.now();

		myFirstWbook = Workbook.createWorkbook(
				new File(SelectedPath.getText() + "\\Companies-" + date.getMonth() + "-" + date.getYear() + ".xls"));

		// create an Excel sheet
		WritableSheet excelSheet = myFirstWbook.createSheet("Sheet 1", 0);

		// add something into the Excel sheet
		Label label = new Label(0, 0, "Name");
		excelSheet.addCell(label);
		label = new Label(1, 0, "Location");
		excelSheet.addCell(label);
		label = new Label(2, 0, "Involved");
		excelSheet.addCell(label);
		label = new Label(3, 0, "Required  Skills");
		excelSheet.addCell(label);

		Number number;
		ResultSet res = Main.stmt.executeQuery("SELECT `ID`, `Name`, `Location`, `Involved` FROM `companies` WHERE 1");
		int id;
		String SkillsString = "";
		int con = 1;
		while (res.next()) {
			id = Integer.parseInt(res.getString("ID"));
			ResultSet Skills = Main.stmt2.executeQuery(
					"SELECT skills.Name from  skills inner join companies_skills on companies_skills.skill_ID = skills.ID where companies_skills.company_id ="
							+ id);
			while (Skills.next()) {

				SkillsString += Skills.getString("Name") + ", ";
			}
			label = new Label(0, con, res.getString("name"));
			excelSheet.addCell(label);
			label = new Label(1, con, res.getString("Location"));
			excelSheet.addCell(label);
			label = new Label(2, con, res.getString("Involved"));
			excelSheet.addCell(label);
			label = new Label(3, con, SkillsString);
			excelSheet.addCell(label);
			SkillsString = "";
			con++;
		}

		// number = new Number(0, 2, 2);
		// excelSheet.addCell(number);

		myFirstWbook.write();

		if (myFirstWbook != null) {
			try {
				myFirstWbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}

	}

}

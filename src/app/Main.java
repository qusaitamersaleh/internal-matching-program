package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	static Statement stmt;
	static Statement stmt2;
	public static Stage primaryStage;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		launch(args);


		String[] values = new String[2];
		
		values[1]="aaa";
		
		
		for (int i = 0; i <= 0; i++){
	    	 
	    	
	    }
		  if (values[1].length()%2!=0){
			  m.m("asd");
            
          }
		
		
	}
	
    
    	
 
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		try {
			this.primaryStage = primaryStage;
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/pip", "root", "");
			Main.stmt = con.createStatement();
			Main.stmt2 = con.createStatement();
			// con.close();
			createDatabase();
			Parent root = FXMLLoader.load(getClass().getResource("/app/Home.fxml"));
			this.primaryStage.setTitle("PIP Matching Program v1");
			this.primaryStage.setScene(new Scene(root));
			// primaryStage.setFullScreen(true);
			this.primaryStage.show();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void createDatabase() {
		try {

			int created = stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS pip");
			if (created == 1) {
				createTables();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createTables() throws SQLException {

		String companies = "create table IF NOT EXISTS companies (`ID` int(11) NOT NULL AUTO_INCREMENT, `Name` text NOT NULL,`Location` text NOT NULL,`Involved` tinytext NOT NULL, PRIMARY KEY (`ID`))";
		stmt2.execute(companies);
		String co_skills = "CREATE TABLE `companies_skills` ( `ID` int(11) NOT NULL AUTO_INCREMENT, `company_id` int(11) NOT NULL, `skill_ID` int(11) NOT NULL, PRIMARY KEY (`ID`), KEY `company_ID` (`company_id`), KEY ` skill_ID` (`skill_ID`), CONSTRAINT ` skill_ID` FOREIGN KEY (`skill_ID`) REFERENCES `skills` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT `company_ID` FOREIGN KEY (`company_id`) REFERENCES `companies` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=latin1";
		stmt2.execute(co_skills);

		String participants = " CREATE TABLE `participants` ( `ID` int(11) NOT NULL AUTO_INCREMENT, `Name` text NOT NULL, `CycleN` int(11) NOT NULL, `Matched` text NOT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1";
		stmt2.execute(participants);
		String participants_skills = "CREATE TABLE `participants_skills` ( `ID` int(11) NOT NULL AUTO_INCREMENT, `skill_ID` int(11) NOT NULL, `participant_ID` int(11) NOT NULL, PRIMARY KEY (`ID`), KEY `participant_ID` (`participant_ID`), KEY `skill_ID` (`skill_ID`), CONSTRAINT `participant_ID` FOREIGN KEY (`participant_ID`) REFERENCES `participants` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT `skill_ID` FOREIGN KEY (`skill_ID`) REFERENCES `skills` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 ";
		stmt2.execute(participants_skills);

		String skills = "CREATE TABLE `skills` ( `ID` int(11) NOT NULL AUTO_INCREMENT, `Name` tinytext CHARACTER SET armscii8 COLLATE armscii8_bin NOT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1";
		stmt2.execute(skills);

	}

}
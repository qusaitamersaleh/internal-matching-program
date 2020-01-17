package app;

public class Participant {

	String Name, Matched,Skills;
	int Cycle,ID;

	public Participant(int ID, int cycleNumber,String name, String matched,  String skills) {
		super();
		this.ID=ID;
		Name = name;
		Matched = matched;
		this.Cycle = cycleNumber;
		this.Skills= skills;
	}


	public String getSkills() {
		return Skills;
	}


	public void setSkills(String skills) {
		Skills = skills;
	}


	public int getID() {
		return ID;
	}


	public void setID(int iD) {
		ID = iD;
	}


	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getMatched() {
		return Matched;
	}
	public void setMatched(String matched) {
		Matched = matched;
	}
	public int getCycle() {
		return Cycle;
	}
	public void setCycle(int cycleNumber) {
		this.Cycle = cycleNumber;
	}



}

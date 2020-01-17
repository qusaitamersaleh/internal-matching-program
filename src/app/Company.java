package app;



public class Company {

	private String Name ,Location,Involved;
	private int ID;
	private String  Skills;


	public Company(int ID ,String name, String location,String involved , String  Skills) {
		super();
		this.ID=ID;
		this.Name = name ;
		this.Location =location;
		this.Involved = involved;
		this.Skills = Skills;
	}


	public void EditCompany( String name, String location,String involved , String  Skills) {

		this.Name = name ;
		this.Location =location;
		this.Involved = involved;
		this.Skills = Skills;
	}

	public String getName() {
		return Name;
	}


	public void setName(String name) {
		Name = name;
	}


	public String getLocation() {
		return Location;
	}


	public void setLocation(String location) {
		Location = location;
	}


	public String getInvolved() {
		return Involved;
	}


	public void setInvolved(String involved) {
		this.Involved = involved;
	}


	public int getID() {
		return ID;
	}


	public void setID(int iD) {
		ID = iD;
	}


	public String  getSkills() {
		return Skills;
	}


	public void setSkills(String  skills) {
		this.Skills = skills;
	}








}

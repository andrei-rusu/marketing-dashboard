package application.model;

public class PersonRecord {

	Double id;
	int gender;
	int ageGroup;
	int income;

	public PersonRecord(Double id1, int gender2, int ageGr, int inc){
		this.id = id1;
		this.gender = gender2;
		this.ageGroup = ageGr;
		this.income = inc;
	}

	public Double getId() {
		return id;
	}

	public int getGender() {
		return gender;
	}

	public int getAgeGroup() {
		return ageGroup;
	}

	public int getIncome() {
		return income;
	}

	 @Override
	    public boolean equals(Object o) {

	        if (o == this) return true;
	        if (o instanceof PersonRecord) {
	        	PersonRecord p = (PersonRecord) o;
	        	return p.id == this.id;
	        } else if(o instanceof ImpressionRecord){
	        	ImpressionRecord i = (ImpressionRecord) o;
	        	return i.id == this.id;
	        }

	        return false;
	    }

}

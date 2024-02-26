package application.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PersonWrapper {


	ArrayList<PersonRecord> people;

	public PersonWrapper(ArrayList<PersonRecord> pl){
		people = new ArrayList<PersonRecord>(pl);
	}

	public ArrayList<PersonRecord> getPersons() {
		return people;
	}

	public int filterGender(int gender) {

		List<PersonRecord> result = people.stream()
			.filter(rec -> rec.gender == gender)
			.collect(Collectors.toList());

		return result.size();
	}

	public int filterAge(int ageGroup) {

		List<PersonRecord> result = people.stream()
			.filter(rec -> rec.ageGroup == ageGroup)
			.collect(Collectors.toList());

		return result.size();
	}

	public int filterIncome(int incomeGroup) {

		List<PersonRecord> result = people.stream()
			.filter(rec -> rec.income == incomeGroup)
			.collect(Collectors.toList());

		return result.size();
	}

}

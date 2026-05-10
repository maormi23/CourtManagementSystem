package model;

import java.util.ArrayList;
import java.util.Date;

import enums.Gender;
import enums.Specialization;
import enums.Status;
import exceptions.FutureDateException;
import exceptions.InvalidBirthDateException;

public class Judge extends Lawyer {
	// fields
	private int experienceYear;
	private ArrayList <Case> casesPresided = new ArrayList<>();
	
	// full
	public Judge(int id, String firstName, String lastName, Date birthdate, String address, String email, String phone,
			Gender gender, ArrayList<Case> casesHandled, Specialization specialization, int licenseNumber,
			double salary, Department department, int experienceYear, ArrayList<Case> casesPresided)
					throws InvalidBirthDateException, FutureDateException {
		super(id, firstName, lastName, birthdate, address, email, phone, gender, casesHandled, specialization,
				licenseNumber, salary, department);
		this.experienceYear = experienceYear;
		this.casesPresided = casesPresided;
	}
	
	// without arraylist
	public Judge(int id, String firstName, String lastName, Date birthdate,
            String address, String phone, String email, Gender gender,
            Specialization specialization, int licenseNumber, double salary,
            int experienceYear) throws InvalidBirthDateException, FutureDateException {
			super(id, firstName, lastName, birthdate, address, phone, email, gender,
					specialization, licenseNumber, salary);
			this.experienceYear = experienceYear;
			this.casesPresided = new ArrayList<>(); 
}

	// Getters
	public int getExperienceYear() {
		return experienceYear;
	}

	public ArrayList<Case> getCasesPresided() {
		return casesPresided;
	}

	// Setters
	public void setExperienceYear(int experienceYear) {
		this.experienceYear = experienceYear;
	}

	public void setCasesPresided(ArrayList<Case> casesPresided) {
		this.casesPresided = casesPresided;
	}

	@Override
	public String toString() {
	    return "Judge [id =" + id + ", FirstName=" + firstName + ", LastName=" + lastName +
	           ", BirthDate=" + birthdate + ", Address=" + address +
	           ", PhoneNumber=" + phone + ", Email=" + email + ", Gender=" + gender +
	           ", Specialization=" + specialization + ", licenseNumber= " + licenseNumber + 
	           ", salary=" + salary + ", ExperienceYear=" + experienceYear + "]";
	}

	@Override
    public boolean closeCase(Case Case) {
        if (Case != null && casesHandled.contains(Case)) {
        	Case.setCaseStatus(Status.finished);
            casesHandled.remove(Case);
        	casesPresided.add(Case);
            return true;
        }
        return false; 
    }
}

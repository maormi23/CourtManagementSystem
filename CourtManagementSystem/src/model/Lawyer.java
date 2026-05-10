package model;

import java.util.ArrayList;
import java.util.Date;

import enums.Gender;
import enums.Specialization;
import enums.Status;
import exceptions.FutureDateException;
import exceptions.InvalidBirthDateException;
import exceptions.NegativeSalaryException;
import exceptions.ObjectAlreadyExistsException;
import exceptions.ObjectDoesNotExistException;

public class Lawyer extends Person {
	// fields
	protected ArrayList<Case> casesHandled = new ArrayList<>();
	protected Specialization specialization;
	protected int licenseNumber;
	protected double salary;
	protected Department department;
	
	//full
	public Lawyer(int id, String firstName, String lastName, Date birthdate, String address, String email, String phone,
			Gender gender, ArrayList<Case> casesHandled, Specialization specialization, int licenseNumber,
			double salary, Department department) throws InvalidBirthDateException, FutureDateException {
		super(id, firstName, lastName, birthdate, address, email, phone, gender);
		this.casesHandled = casesHandled;
		this.specialization = specialization;
		this.licenseNumber = licenseNumber;
		this.salary = salary;
		this.department = department;
	}
	
	// without department && arraylist
	public Lawyer(int id, String firstName, String lastName, Date birthdate,
	              String address, String phone, String email, Gender gender,
	              Specialization specialization, int licenseNumber, double salary) throws InvalidBirthDateException, FutureDateException {
	    super(id, firstName, lastName, birthdate, address, phone, email, gender);
	    this.casesHandled = new ArrayList<>();   
	    this.specialization = specialization;
	    this.licenseNumber = licenseNumber;
	    this.salary = salary;
	    this.department = null;        
	}


	// Getters
	public ArrayList<Case> getCasesHandled() {
		return casesHandled;
	}

	public Specialization getSpecialization() {
		return specialization;
	}

	public int getLicenseNumber() {
		return licenseNumber;
	}

	public double getSalary() {
		return salary;
	}

	public Department getDepartment() {
		return department;
	}

	// Setters
	public void setCasesHandled(ArrayList<Case> casesHandled) {
		this.casesHandled = casesHandled;
	}

	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}

	public void setLicenseNumber(int licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public void setSalary(double salary) throws NegativeSalaryException {
	    if (salary < 0) {
	        throw new NegativeSalaryException(salary);
	    }
	    this.salary = salary;
	}


	public void setDepartment(Department department) {
		this.department = department;
	}

	@Override
	public String toString() {
	    return "Lawyer [id =" + id + ", FirstName=" + firstName + ", LastName=" + lastName +
	           ", BirthDate=" + birthdate + ", Address=" + address + 
	           ", PhoneNumber=" + phone + ", Email=" + email + ", Gender=" + gender +
	           ", Specialization=" + specialization + ", licenseNumber= " + licenseNumber + ", salary=" + salary + "]";
	}

	public void addCase(Case c) throws ObjectAlreadyExistsException {
	    if (c == null) {
	        throw new NullPointerException("Case object is null.");
	    }
	    if (casesHandled.contains(c)) {
	        throw new ObjectAlreadyExistsException("Case with code " + c.getCode());
	    }
	    casesHandled.add(c);
	}

	
	public void removeCase(Case c) throws ObjectDoesNotExistException {
	    if (c == null) {
	        throw new NullPointerException("Case is null.");
	    }
	    if (!casesHandled.contains(c)) {
	        throw new ObjectDoesNotExistException("Case with code " + c.getCode());
	    }
	    casesHandled.remove(c);
	}

    
	public boolean closeCase(Case Case) {
		if (Case != null && casesHandled.contains(Case)) {
			Case.setCaseStatus(Status.finished);
			return true;
			} 
		return false;
		}

    
	public Case getRequiredCase() {
	    Case res = null;
	    for (Case c : casesHandled) {
	        if (c != null && c.getLawyersList() != null) {
	            boolean hasSpec = c.getLawyersList().stream()
	                    .anyMatch(l -> l != null && l.getSpecialization() == this.specialization);
	            if (hasSpec) {
	                if (res == null || 
	                    (c.getAccused() != null && res.getAccused() != null &&
	                     c.getAccused().getCases().size() > res.getAccused().getCases().size())) {
	                    res = c;
	                }
	            }
	        }
	    }
	    return res;
	}

}

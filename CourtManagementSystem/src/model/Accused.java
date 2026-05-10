package model;

import java.util.Date;
import java.util.HashSet;

import enums.Gender;
import enums.Status;
import exceptions.FutureDateException;
import exceptions.InvalidBirthDateException;
import exceptions.ObjectAlreadyExistsException;
import exceptions.ObjectDoesNotExistException;

public class Accused extends Person {
	//fields
	private String job;
	private HashSet <Case> cases = new HashSet<>();
	
	public Accused(int id, String firstName, String lastName, Date birthdate, String address, String email,
			String phone, Gender gender, String job, HashSet<Case> cases) throws InvalidBirthDateException, FutureDateException {
		super(id, firstName, lastName, birthdate, address, email, phone, gender);
		setJob(job);
		this.cases = cases;
	}
	
	// without hashset
	public Accused(int id, String firstName, String lastName, Date birthdate, String address, String email,
			String phone, Gender gender, String job) throws InvalidBirthDateException, FutureDateException {
		super(id, firstName, lastName, birthdate, address, email, phone, gender);
		setJob(job);
	}

	// Getters
	public String getJob() {
		return job;
	}

	public HashSet<Case> getCases() {
		return cases;
	}
	// Setters
	public void setJob(String job) {
	    if (job == null) {
	        this.job = null;
	        return;
	    }
	    job = job.replace("\"", "");
	    job = job.trim();
	    this.job = job;
	}

	@Override
	public String toString() {
	    return "Accused [id =" + id + ", FirstName=" + firstName + ", LastName=" + lastName +
	           ", BirthDate=" + birthdate + ", Address=" + address +
	           ", PhoneNumber=" + phone + ", Email=" + email + ", Gender=" + gender +
	           " ,job=" + job + "]";
	}

	public void setCases(HashSet<Case> cases) {
		this.cases = cases;
	}
	
	public void addCase(Case c) throws ObjectAlreadyExistsException {
	    if (c == null) {
	        throw new NullPointerException("Case object is null.");
	    }
	    if (cases.contains(c)) {
	        throw new ObjectAlreadyExistsException("Case with Code " + c.getCode());
	    }
	    cases.add(c);
	}

	
	public void removeCase(Case c) throws ObjectDoesNotExistException {
	    if (c == null) {
	        throw new NullPointerException("Case object is null.");
	    }
	    if (!cases.contains(c)) {
	        throw new ObjectDoesNotExistException("Case with Code " + c.getCode());
	    }
	    cases.remove(c);
	}

    
    public boolean closeCase(Case Case) {
        if (Case != null && cases.contains(Case)) {
        	Case.setCaseStatus(Status.finished);
            return true;
        }
        return false; 
    }
    
    public Case getRequiredCase() {
        Case res = null;
        for (Case c : cases) {
            if (res == null || c.getOpenDate().before(res.getOpenDate())) res = c;
        }
        return res; 
    }
}

package model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;

import enums.Specialization;
import enums.Status;
import exceptions.ObjectAlreadyExistsException;
import exceptions.ObjectDoesNotExistException;

public class Case implements Serializable {
	// fields
    private static int nextCode = 0;
	private String code;
	private Accused accused;
	private Date openDate;
	private Status caseStatus;
	private Specialization caseType;
	private HashSet <Lawyer>lawyersList = new HashSet<>();
	private Verdict verdict;
	
	// without hashset
	public Case(Accused accused, Date openDate, Status caseStatus, Specialization caseType,Verdict verdict) {
		super();
		this.code = String.valueOf(nextCode++);
		this.accused = accused;
		this.openDate = openDate;
		this.caseStatus = caseStatus;
		this.caseType = caseType;
		this.verdict = verdict;
	}
	
	// full
	public Case(String code, Accused accused, Date openDate, Status caseStatus, Specialization caseType,
			HashSet<Lawyer> lawyersList, Verdict verdict) {
		super();
		this.code = code;
		this.accused = accused;
		this.openDate = openDate;
		this.caseStatus = caseStatus;
		this.caseType = caseType;
		this.lawyersList = lawyersList;
		this.verdict = verdict;
	}

	public String getCode() {
		return code;
	}
	public Accused getAccused() {
		return accused;
	}
	public Date getOpenDate() {
		return openDate;
	}
	public Status getCaseStatus() {
		return caseStatus;
	}
	public Specialization getCaseType() {
		return caseType;
	}
	public HashSet<Lawyer> getLawyersList() {
		return lawyersList;
	}
	public Verdict getVerdict() {
		return verdict;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setAccused(Accused accused) {
		this.accused = accused;
	}
	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}
	public void setCaseStatus(Status caseStatus) {
		this.caseStatus = caseStatus;
	}
	public void setCaseType(Specialization caseType) {
		this.caseType = caseType;
	}
	public void setCases(HashSet<Lawyer> lawyersList) {
		this.lawyersList = lawyersList;
	}
	public void setVerdict(Verdict verdict) {
		this.verdict = verdict;
	}
	
	@Override
	public String toString() {
	    String base = "code=" + code;
	    
	    base += ", accused=";
	    if (accused != null) {
	        base += accused.toString();
	    } else {
	        base += "null";
	    }

	    base += ", openedDate=" + openDate;
	    base += ", caseStatus=" + caseStatus;
	    base += ", caseType=" + caseType;

	    base += ", lawyers=";
	    if (lawyersList != null && !lawyersList.isEmpty()) {
	        base += "[";
	        for (Lawyer l : lawyersList) {
	            base += l.getFirstName() + " " + l.getLastName() + " (id=" + l.getId() + "), ";
	        }
	        base = base.substring(0, base.length() - 2); 
	        base += "]";
	    } else {
	        base += "none";
	    }

	    if (verdict != null && caseStatus == Status.finished) {
	        base += ", verdict=" + verdict.toString();
	    }

	    return "Case [" + base + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(code);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Case other = (Case) obj;
		return code == other.code;
	}
	
	public void addLawyer(Lawyer lawyer) throws ObjectAlreadyExistsException {
	    if (lawyer == null) {
	        throw new NullPointerException("Lawyer is null.");
	    }
	    if (lawyersList.contains(lawyer)) {
	        throw new ObjectAlreadyExistsException("Lawyer with id " + lawyer.getId() + " already exists in this department.");
	    }
	    lawyersList.add(lawyer);
	}

	public void removeLawyer(Lawyer lawyer) throws ObjectDoesNotExistException {
	    if (lawyer == null) {
	        throw new NullPointerException("Lawyer is null.");
	    }
	    if (!lawyersList.contains(lawyer)) {
	        throw new ObjectDoesNotExistException("Lawyer with id " + lawyer.getId() + " does not exist in this department.");
	    }
	    lawyersList.remove(lawyer);
	}

	
	
}

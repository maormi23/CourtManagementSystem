package model;

import java.io.Serializable;
import java.util.HashSet;

import enums.Specialization;
import exceptions.ObjectAlreadyExistsException;
import exceptions.ObjectDoesNotExistException;

public class Department implements Comparable<Department>, Serializable{
	// fields
	private int number;
	private String name;
	private Judge manager;
	private String building;
	private Specialization specialization;
	private HashSet <Lawyer>lawyersList = new HashSet<>();
	
	public Department(int numberPk, String name, Judge manager, String building, Specialization specialization,
			HashSet<Lawyer> lawyersList) {
		super();
		this.number = numberPk;
		this.name = name;
		this.manager = manager;
		this.building = building;
		this.specialization = specialization;
		this.lawyersList = lawyersList;
	}
	
	// without arraylist
	public Department(int number, String name, Judge manager, String building, Specialization specialization) {
		super();
		this.number = number;
		this.name = name;
		this.manager = manager;
		this.building = building;
		this.specialization = specialization;
	}


	// Getters
	public int getNumber() {
		return number;
	}

	public String getName() {
		return name;
	}

	public Judge getManager() {
		return manager;
	}

	public String getBuilding() {
		return building;
	}

	public Specialization getSpecialization() {
		return specialization;
	}

	public HashSet<Lawyer> getLawyersList() {
		return lawyersList;
	}
	
	// Setters
	public void setNumber(int number) {
		this.number = number;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setManager(Judge manager) {
		this.manager = manager;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}

	public void setLawyersList(HashSet<Lawyer> lawyersList) {
		this.lawyersList = lawyersList;
	}
	
	// Add\Remove
	public void addLawyer(Lawyer lawyer) throws ObjectAlreadyExistsException {
	    if (lawyer == null) {
	        throw new NullPointerException("Lawyer is null.");
	    }
	    if (lawyersList.contains(lawyer)) {
	        throw new ObjectAlreadyExistsException("Lawyer with id " + lawyer.getId());
	    }
	    lawyersList.add(lawyer);
	}

	public void removeLawyer(Lawyer lawyer) throws ObjectDoesNotExistException {
	    if (lawyer == null) {
	        throw new NullPointerException("Lawyer is null.");
	    }
	    if (!lawyersList.contains(lawyer)) {
	        throw new ObjectDoesNotExistException("Lawyer with id " + lawyer.getId());
	    }
	    lawyersList.remove(lawyer);
	}

	public void addJudge(Judge judge) throws ObjectAlreadyExistsException {
	    if (judge == null) {
	        throw new NullPointerException("Judge is null.");
	    }
	    if (lawyersList.contains(judge)) {
	        throw new ObjectAlreadyExistsException("Judge with id " + judge.getId());
	    }
	    lawyersList.add(judge);
	}

	public void removeJudge(Judge judge) throws ObjectDoesNotExistException {
	    if (judge == null) {
	        throw new NullPointerException("Judge is null.");
	    }
	    if (!lawyersList.contains(judge)) {
	        throw new ObjectDoesNotExistException("Judge with id " + judge.getId());
	    }
	    lawyersList.remove(judge);
	}

	
	@Override
	public String toString() {
	    return "number=" + number + ", name=" + name + ", manager=" + manager +
	           ", building=" + building + ", specialization=" + specialization;
	}
	
	@Override
	public int compareTo(Department other) {
	    return Integer.compare(this.number, other.number); 
	}

}

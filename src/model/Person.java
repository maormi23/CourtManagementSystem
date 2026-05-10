package model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import enums.Gender;
import exceptions.FutureDateException;
import exceptions.InvalidBirthDateException;

public abstract class Person implements Comparable<Person>, Serializable {
	// fields
	protected int id;
	protected String firstName;
	protected String lastName;
	protected Date birthdate;
	protected String address;
	protected String phone;
	protected String email;
	protected Gender gender;
	
	public Person(int id, String firstName, String lastName, Date birthdate,
            String address, String phone, String email, Gender gender) throws InvalidBirthDateException, FutureDateException {
			this.id = id;
			this.firstName = firstName;
			this.lastName = lastName;
			setBirthdate(birthdate);
			this.address = address;
			this.phone = phone;
			this.email = email;
			this.gender = gender;
}

	// Getters
	public int getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public String getAddress() {
		return address;
	}
	public String getPhone() {
		return phone;
	}
	public String getEmail() {
		return email;
	}

	public Gender getGender() {
		return gender;
	}

	// Setters
	public void setId(int id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setBirthdate(Date birthdate) throws InvalidBirthDateException, FutureDateException {
	    if (birthdate == null) {
	        throw new InvalidBirthDateException(); 
	    }

	    Date today = new Date();
	    if (birthdate.after(today)) {
	        throw new FutureDateException(); 
	    }

	    this.birthdate = birthdate; 
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
	    return "id =" + id + ", FirstName=" + firstName + ", LastName=" + lastName + 
	           ", BirthDate=" + birthdate + ", Address=" + address + 
	           ", PhoneNumber=" + phone + ", Email=" + email + ", Gender=" + gender;
	}


	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		return id == other.id;
	}	
	
    @Override
    public int compareTo(Person other) {
        return Integer.compare(this.id, other.id);
    }
}

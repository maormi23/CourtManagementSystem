package model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import control.Court;

public class Verdict implements Serializable {
    private static int nextID = 0;
	private int verdictID;
	private String verdictSummary;
	private Date issusedDate;
	private Judge judge;
	private Case Case;
	  
	public Verdict(String verdictSummary, Date issusedDate, Judge judge, Case Case) {
	    super();
	    if (nextID == 0) {
	        Court court = Court.getInstance(); 
	        for (Verdict v : court.getAllVerdicts().values()) {
	            if (v != null && v.getVerdictID() >= nextID) {
	                nextID = v.getVerdictID() + 1;
	            }
	        }
	    }
	    
	    this.verdictID = nextID++;
	    this.verdictSummary = verdictSummary;
	    this.issusedDate = issusedDate;
	    this.judge = judge;
	    this.Case = Case;
	}

	public int getVerdictID() {
		return verdictID;
	}

	public String getVerdictSummary() {
		return verdictSummary;
	}

	public Date getIssusedDate() {
		return issusedDate;
	}

	public Judge getJudge() {
		return judge;
	}
	
	public Case getCase() {
		return Case;
	}


	public void setVerdictID(int verdictID) {
		this.verdictID = verdictID;
	}

	public void setVerdictSummary(String verdictSummary) {
		this.verdictSummary = verdictSummary;
	}

	public void setIssusedDate(Date issusedDate) {
		this.issusedDate = issusedDate;
	}

	public void setJudge(Judge judge) {
		this.judge = judge;
	}
	
	public void setCase(Case c) {
		this.Case = c;
	}

	@Override
	public String toString() {
	    String str = "verdictID=" + verdictID + ", caseCode=";
	    if (Case != null) str += Case.getCode(); else str += "null";
	    str += ", verdictSummary=" + verdictSummary;
	    str += ", issusedDate=" + issusedDate;
	    str += ", judge=";
	    if (judge != null) str += judge.firstName; else str += "null";
	    return str;
	}

	@Override
	public int hashCode() {
		return Objects.hash(verdictID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Verdict other = (Verdict) obj;
		return verdictID == other.verdictID;
	}
}

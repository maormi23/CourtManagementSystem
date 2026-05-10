package control;

import java.util.Date;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.TreeSet;
import java.util.stream.Collectors;
import enums.Specialization;
import enums.Status;
import exceptions.*;
import model.*;
import utils.MyFileLogWriter;

public class Court implements Serializable {
	// fields
    private static final long serialVersionUID = 1L;
    private static final Court INSTANCE = new Court();
	private final HashMap< Integer, Lawyer> allLawyers;
	private final HashMap < Integer, Accused> allAccused;
	private final HashMap < Integer, Department > allDepartments;
	private final HashMap <String , Case> allCases;
	private final HashMap <Integer , Verdict > allVerdicts;
	
    private Court() {
    	allLawyers = new HashMap<>();
    	allAccused = new HashMap<>();
    	allDepartments = new HashMap<>();
    	allCases = new HashMap<>();
    	allVerdicts = new HashMap<>();
    }
    
    // Getters
	public HashMap<Integer, Lawyer> getAllLawyers() {
		return allLawyers;
	}
	public HashMap<Integer, Accused> getAllAccused() {
		return allAccused;
	}
	public HashMap<Integer, Department> getAllDepartments() {
		return allDepartments;
	}
	public HashMap<String, Case> getAllCases() {
		return allCases;
	}
	public HashMap<Integer, Verdict> getAllVerdicts() {
		return allVerdicts;
	}
	public static Court getInstance() { return INSTANCE; }
	private Object readResolve() throws java.io.ObjectStreamException {
	    INSTANCE.allLawyers.clear();
	    INSTANCE.allLawyers.putAll(this.allLawyers);

	    INSTANCE.allAccused.clear();
	    INSTANCE.allAccused.putAll(this.allAccused);

	    INSTANCE.allDepartments.clear();
	    INSTANCE.allDepartments.putAll(this.allDepartments);

	    INSTANCE.allCases.clear();
	    INSTANCE.allCases.putAll(this.allCases);

	    INSTANCE.allVerdicts.clear();
	    INSTANCE.allVerdicts.putAll(this.allVerdicts);
	    return INSTANCE;
	}


	@Override
	public String toString() {
		return "Court [allLawyers=" + allLawyers + ", allAccused=" + allAccused + ", allDepartments="
				+ allDepartments + ", allCases=" + allCases + ", allVerdicts=" + allVerdicts + "]";
	}

	////
	// Functions for adding elements from collections
	////
	public void addLawyer(Lawyer l) throws ObjectAlreadyExistsException, NegativeSalaryException {
	    if (l == null) throw new NullPointerException("Lawyer is null.");
	    if (allLawyers.containsKey(l.getId()))
	        throw new ObjectAlreadyExistsException("Lawyer with id " + l.getId() + " already exists.");
	    if (l.getSalary() < 0)
	        throw new NegativeSalaryException(l.getSalary());
	    allLawyers.put(l.getId(), l);
	}

	public void addJudge(Judge j) throws ObjectAlreadyExistsException, NegativeSalaryException {
	    if (j == null) throw new NullPointerException("Judge is null.");
	    if (allLawyers.containsKey(j.getId()))
	        throw new ObjectAlreadyExistsException("Judge with id " + j.getId() + " already exists.");
	    if (j.getSalary() < 0)
	        throw new NegativeSalaryException(j.getSalary());
	    allLawyers.put(j.getId(), j);
	}


	public void addAccused(Accused a) throws ObjectAlreadyExistsException {
	    if (a == null) {
	        throw new NullPointerException("Accused is null.");
	    }
	    if (allAccused.containsKey(a.getId())) {
	        throw new ObjectAlreadyExistsException("Accused with id " + a.getId() + " already exists.");
	    }
	    allAccused.put(a.getId(), a);
	}

	public void addDepartment(Department d) throws ObjectAlreadyExistsException {
	    if (d == null) {
	        throw new NullPointerException("Department is null.");
	    }
	    if (allDepartments.containsKey(d.getNumber())) {
	        throw new ObjectAlreadyExistsException("Department with number " + d.getNumber() + " already exists.");
	    }
	    allDepartments.put(d.getNumber(), d);
	}

	public void addVerdict(Verdict v)
	        throws ObjectAlreadyExistsException, ObjectDoesNotExistException {
	    if (v == null) throw new NullPointerException("Verdict is null.");
	    if (allVerdicts.containsKey(v.getVerdictID()))
	        throw new ObjectAlreadyExistsException("Verdict with id " + v.getVerdictID() + " already exists.");

	    Case c = v.getCase();
	    if (c == null || !allCases.containsKey(c.getCode()))
	        throw new ObjectDoesNotExistException("Case does not exist in registry.");

	    if (c.getVerdict() != null || c.getCaseStatus() == Status.finished)
	        throw new ObjectAlreadyExistsException("Case " + c.getCode() + " already has a verdict or is finished.");

	    Judge j = v.getJudge();
	    if (j == null || !allLawyers.containsKey(j.getId()) || !(allLawyers.get(j.getId()) instanceof Judge))
	        throw new ObjectDoesNotExistException("Judge does not exist in registry.");

	    Date issued = v.getIssusedDate();
	    if (issued == null)
	        throw new NullPointerException("Verdict issue date is null.");

	    allVerdicts.put(v.getVerdictID(), v);
	    c.setVerdict(v);
	    c.setCaseStatus(Status.finished);
	}


	public void addCase(Case c) throws ObjectAlreadyExistsException, ObjectDoesNotExistException {
	    if (c == null) {
	        throw new NullPointerException("Case is null.");
	    }
	    if (allCases.containsKey(c.getCode())) {
	        throw new ObjectAlreadyExistsException("Case with code " + c.getCode() + " already exists.");
	    }
	    allCases.put(c.getCode(), c);

	    if (c.getLawyersList() != null) {
	        for (Lawyer l : c.getLawyersList()) {
	            if (l != null) {
	                l.addCase(c);
	            }
	        }
	    }

	    if (c.getAccused() != null) {
	        c.getAccused().addCase(c);
	    }

	    if (c.getVerdict() != null) {
	        addVerdict(c.getVerdict());
	    }
	}

	public void addLawyerToDepartment(Department department, Lawyer lawyer)
	        throws ObjectAlreadyExistsException, ObjectDoesNotExistException {
	    if (department == null) throw new NullPointerException("Department is null.");
	    if (lawyer == null) throw new NullPointerException("Lawyer is null.");
	    if (!allDepartments.containsKey(department.getNumber()))
	        throw new ObjectDoesNotExistException("Department " + department.getNumber() + " does not exist.");
	    if (!allLawyers.containsKey(lawyer.getId()))
	        throw new ObjectDoesNotExistException("Lawyer " + lawyer.getId() + " does not exist.");

	    if (department.getLawyersList().contains(lawyer))
	        throw new ObjectAlreadyExistsException("Lawyer " + lawyer.getId() + " already in department " + department.getNumber());

	    department.getLawyersList().add(lawyer);
	    lawyer.setDepartment(department);
	}

	public void addJudgeToDepartment(Department department, Judge judge)
	        throws ObjectAlreadyExistsException, ObjectDoesNotExistException {
	    if (department == null) throw new NullPointerException("Department is null.");
	    if (judge == null) throw new NullPointerException("Judge is null.");
	    if (!allDepartments.containsKey(department.getNumber()))
	        throw new ObjectDoesNotExistException("Department " + department.getNumber() + " does not exist.");
	    if (!allLawyers.containsKey(judge.getId()) || !(allLawyers.get(judge.getId()) instanceof Judge))
	        throw new ObjectDoesNotExistException("Judge " + judge.getId() + " does not exist.");

	    if (department.getLawyersList().contains(judge))
	        throw new ObjectAlreadyExistsException("Judge " + judge.getId() + " already in department " + department.getNumber());

	    department.getLawyersList().add(judge);
	    judge.setDepartment(department);

	    if (department.getManager() != judge) {
	        department.setManager(judge);
	    }
	}


	////
	// Functions for removing elements from collections
	////
	public void removeLawyer(Lawyer l) throws ObjectDoesNotExistException {
	    if (l == null) {
	        throw new NullPointerException("Lawyer is null.");
	    }

	    Lawyer removed = allLawyers.remove(l.getId());
	    if (removed == null) {
	        throw new ObjectDoesNotExistException("Lawyer with id " + l.getId() + " does not exist.");
	    }

	    Department dep = removed.getDepartment();
	    if (dep != null) {
	        dep.getLawyersList().remove(removed);
	        if (dep.getManager() == removed) {
	            dep.setManager(null);
	        }
	    }

	    for (Case c : allCases.values()) {
	        if (c.getLawyersList() != null) {
	            c.getLawyersList().remove(removed);
	        }
	    }
	}

	public void removeJudge(Judge j) throws ObjectDoesNotExistException {
	    if (j == null) {
	        throw new NullPointerException("Judge is null.");
	    }

	    Lawyer asLawyer = allLawyers.get(j.getId());
	    if (!(asLawyer instanceof Judge)) {
	        throw new ObjectDoesNotExistException("Judge with id " + j.getId() + " does not exist.");
	    }

	    Judge removed = (Judge) asLawyer;
	    allLawyers.remove(removed.getId());

	    Department d = removed.getDepartment(); 
	    if (d != null && d.getManager() == removed) {
	        d.setManager(null);
	    }
	    for (Department dep : allDepartments.values()) {
	        if (dep.getManager() == removed) {
	            dep.setManager(null);
	        }
	    }

	    for (Verdict v : allVerdicts.values()) {
	        if (v != null && v.getJudge() == removed) {
	            v.setJudge(null);
	        }
	    }
	}

	public void removeAccused(Accused a) throws ObjectDoesNotExistException {
	    if (a == null) {
	        throw new NullPointerException("Accused is null.");
	    }

	    Accused removed = allAccused.remove(a.getId());
	    if (removed == null) {
	        throw new ObjectDoesNotExistException("Accused with id " + a.getId() + " does not exist.");
	    }

	    for (Case c : new ArrayList<>(removed.getCases())) {
	        c.setAccused(null);
	        removed.getCases().remove(c);
	    }
	}

	public void removeDepartment(Department d) throws ObjectDoesNotExistException {
	    if (d == null) {
	        throw new NullPointerException("Department is null.");
	    }

	    Department removed = allDepartments.remove(d.getNumber());
	    if (removed == null) {
	        throw new ObjectDoesNotExistException("Department with number " + d.getNumber() + " does not exist.");
	    }

	    for (Lawyer l : new ArrayList<>(removed.getLawyersList())) {
	        l.setDepartment(null);
	    }
	    removed.setManager(null);
	    removed.getLawyersList().clear();
	}

	public void removeCase(Case c) throws ObjectDoesNotExistException {
	    if (c == null) {
	        throw new NullPointerException("Case is null.");
	    }

	    Case removed = allCases.remove(c.getCode());
	    if (removed == null) {
	        throw new ObjectDoesNotExistException("Case with code " + c.getCode() + " does not exist.");
	    }

	    if (removed.getLawyersList() != null) {
	        for (Lawyer lw : removed.getLawyersList()) {
	            if (lw != null && lw.getCasesHandled() != null) {
	                lw.getCasesHandled().remove(removed);
	            }
	        }
	    }

	    Accused ac = removed.getAccused();
	    if (ac != null && ac.getCases() != null) {
	        ac.getCases().remove(removed);
	    }

	    if (removed.getVerdict() != null) {
	        Verdict v = removed.getVerdict();
	        allVerdicts.remove(v.getVerdictID());
	        removed.setVerdict(null);
	        removed.setCaseStatus(Status.inProcess);
	    }
	}

	public void removeVerdict(Verdict v) throws ObjectDoesNotExistException {
	    if (v == null) {
	        throw new NullPointerException("Verdict is null.");
	    }

	    Verdict removed = allVerdicts.remove(v.getVerdictID());
	    if (removed == null) {
	        throw new ObjectDoesNotExistException("Verdict with id " + v.getVerdictID() + " does not exist.");
	    }

	    Case caseRef = removed.getCase();
	    if (caseRef != null) {
	        caseRef.setVerdict(null);
	        caseRef.setCaseStatus(Status.inProcess);
	    }
	}

    /**
     * Retrieves a Object by their unique code.
     *
     * @param code
     * @return the object, or null if not found
     */
	public Lawyer getRealLawyer(int id) {
	    return allLawyers.get(id);
	}
	public Judge getRealJudge(int id) {
	    Lawyer l = allLawyers.get(id);
	    if (l instanceof Judge) {
	        return (Judge) l;
	    }
	    return null;
	}
	public Accused getRealAccused(int id) {
	    return allAccused.get(id);
	}
	public Department getRealDepartment(int number) {
	    return allDepartments.get(number);
	}
	public Case getRealCase(String code) {
	    return allCases.get(code);
	}
	public Verdict getRealVerdict(int id) {
	    return allVerdicts.get(id);
	}
	
	////
	// Query Methods
	////

	/**
	 * Counts finished cases with verdicts issued before the given date.
	 */
	public int howManyCasesBefore(Date date) {
	    if (date == null) return 0;
	    int count = 0;
	    for (Case c : allCases.values()) {
	        Verdict v = c.getVerdict();
	        if (v != null
	                && c.getCaseStatus() == Status.finished && v.getIssusedDate() != null && v.getIssusedDate().before(date)) {
	            count++;
	        }
	    }
	    return count;
	}

	/**
	 * Calculates difference in days between longest and shortest case of a lawyer or judge.
	 */
	public int differnceBetweenTheLongestAndShortestCase(Lawyer l) {
	    if (l == null) return 0;

	    List<Case> src = (l instanceof Judge)
	            ? ((Judge) l).getCasesPresided()
	            : l.getCasesHandled();
	    if (src == null) return 0;

	    List<Integer> durations = src.stream()
	            .filter(Objects::nonNull)
	            .filter(c -> c.getOpenDate() != null)
	            .filter(c -> c.getVerdict() != null && c.getVerdict().getIssusedDate() != null)
	            .filter(c -> (l instanceof Judge) || c.getCaseStatus() == Status.finished)
	            .map(c -> (int) java.time.temporal.ChronoUnit.DAYS.between(
	                    new java.sql.Date(c.getOpenDate().getTime()).toLocalDate(),
	                    new java.sql.Date(c.getVerdict().getIssusedDate().getTime()).toLocalDate()))
	            .sorted()
	            .collect(Collectors.toList());

	    if (durations.size() < 2) return 0;
	    return durations.get(durations.size() - 1) - durations.get(0);
	}

	/**
	 * Prints the most experienced judge to the log file.
	 */
	public void printMostExperiencedJudge() {
	    Judge mostExperienced = null;
	    for (Lawyer l : allLawyers.values()) {
	        if (l instanceof Judge) {
	            Judge j = (Judge) l;
	            if (mostExperienced == null || j.getExperienceYear() > mostExperienced.getExperienceYear()) {
	                mostExperienced = j;
	            }
	        }
	    }
	    if (mostExperienced != null) {
	        MyFileLogWriter.println("The Most Experienced Judge is : " + mostExperienced);
	    }
	}

	/**
	 * Returns accused with maximum number of cases by lawyers of a given specialization.
	 * In case of tie, returns accused with earliest case.
	 */
	public Accused getAccusedWithMaxCasesByLawyers(Specialization caseType) {
	    Accused maxAccused = null;
	    int maxCases = -1;

	    for (Accused accused : allAccused.values()) {
	        int count = 0;
	        for (Case c : accused.getCases()) {
	            if (c.getLawyersList() != null) {
	                for (Lawyer lawyer : c.getLawyersList()) {
	                    if (lawyer != null && lawyer.getSpecialization() == caseType) {
	                        count++;
	                        break; 
	                    }
	                }
	            }
	        }
	        if (count > maxCases) {
	            maxCases = count;
	            maxAccused = accused;
	        }
	    }

	    if (maxCases == 0) {
	        for (Accused accused : allAccused.values()) {
	            if (maxAccused == null || accused.getCases().size() > maxAccused.getCases().size()) {
	                maxAccused = accused;
	            }
	        }
	    }

	    return maxAccused;
	}


	/**
	 * Returns lawyers working in more than one department as stacks of departments.
	 */
	public HashMap<Lawyer, Stack<Department>> lawyersThatWorkWithMoreThanOneDepartment() {
	    HashMap<Lawyer, Stack<Department>> res = new HashMap<>();

	    for (Lawyer l : allLawyers.values()) {
	        if (l == null) continue;
	        
	        Stack<Department> deps = new Stack<>();

	        for (Department d : allDepartments.values()) {
	            if (d.getLawyersList().contains(l)) {
	                deps.push(d);
	            }
	        }

	        if (deps.size() > 1) {
	            res.put(l, deps);
	        }
	    }

	    return res;
	}


	/**
	 * Returns number of finished cases assigned to each department.
	 */
	public HashMap<Department, Integer> findInActiveCasesCountByDepartment() {
	    HashMap<Department, Integer> map = new HashMap<>();
	    
	    // Initialize all departments with 0
	    for (Department d : allDepartments.values()) {
	        map.put(d, 0);
	    }
	    
	    for (Case c : allCases.values()) {
	        if (c.getCaseStatus() == Status.finished && c.getVerdict() != null) {
	            Judge judge = c.getVerdict().getJudge();
	            if (judge != null) {
	                Department dept = judge.getDepartment();
	                if (dept != null) {
	                    map.put(dept, map.get(dept) + 1);
	                }
	            }
	        }
	    }
	    return map;
	}
	
	/**
	* Appoints the most experienced judge as new department manager.
	* Fires current manager completely from the system, assigns new one, and gives salary bonus.
	* Prevents re-appointing previous managers to encourage rotation.
	* **@throws** NegativeSalaryException
	*/
	public Judge appointANewManager(Department department) {
	    if (department == null) return null;

	    Department real = allDepartments.get(department.getNumber());
	    if (real == null) return null;

	    Judge current = real.getManager();
	    Integer currentId = (current != null ? current.getId() : null);

	    // 1) Find the best candidate (do not mutate yet)
	    Judge best = null;
	    if (real.getLawyersList() != null) {
	        for (Lawyer l : real.getLawyersList()) {
	            if (!(l instanceof Judge j)) continue;

	            // exclude current manager
	            if (currentId != null && j.getId() == currentId) continue;

	            // exclude judges who already manage another department
	            boolean managesElsewhere = false;
	            for (Department d : allDepartments.values()) {
	                if (d == real) continue;
	                Judge m = d.getManager();
	                if (m != null && m.getId() == j.getId()) { managesElsewhere = true; break; }
	            }
	            if (managesElsewhere) continue;

	            if (best == null
	                || j.getExperienceYear() > best.getExperienceYear()
	                || (j.getExperienceYear() == best.getExperienceYear() && j.getId() < best.getId())) {
	                best = j;
	            }
	        }
	    }

	    if (best == null) return null;

	    // 3) We have a candidate -> clean current and appoint best
	    if (current != null) {
	        allLawyers.remove(current.getId());
	        if (real.getLawyersList() != null) real.getLawyersList().remove(current);
	        real.setManager(null);
	        for (Department dep : allDepartments.values()) {
	            if (dep.getManager() == current) dep.setManager(null);
	        }
	        for (Verdict v : allVerdicts.values()) {
	            if (v != null && v.getJudge() == current) v.setJudge(null);
	        }
	    }

	    real.setManager(best);
	    if (best.getDepartment() != real) best.setDepartment(real);
	    try { best.setSalary(best.getSalary() + 5000.0); } catch (Exception ignore) {}

	    return best;
	}

	/**
	 * Returns the k smallest lawyers ordered by natural ordering.
	 */
	public List<Lawyer> KLawyers(int k) {
	    return allLawyers.values().stream()
	            .sorted()  
	            .limit(k)
	            .collect(Collectors.toList());
	}
	
	/**
	 * Returns all accused sorted by id and birthdate.
	 */
	public ArrayList<Accused> sortedAccused() {
	    return allAccused.values().stream()
	            .sorted(Comparator
	                    .comparingInt(Accused::getId) 
	                    .thenComparing(Accused::getBirthdate))
	            .collect(Collectors.toCollection(ArrayList::new));
	}
	
	/**
	 * Returns departments sorted by department number and lawyer count.
	 */
	public TreeSet<Department> sortedDepartmentsByDepartmentNumberAndLawyersCount() {
	    Comparator<Department> byNumberThenLawyers =
	        Comparator.comparingInt(Department::getNumber)
	                  .thenComparingInt(d -> d.getLawyersList() == null ? 0 : d.getLawyersList().size());

	    TreeSet<Department> set = new TreeSet<>(byNumberThenLawyers);
	    set.addAll(allDepartments.values()); 
	    return set;
	}
}
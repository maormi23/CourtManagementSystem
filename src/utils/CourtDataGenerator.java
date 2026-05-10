package utils;

import control.Court;
import enums.*;
import model.*;

import java.util.Date;

public final class CourtDataGenerator {
    private CourtDataGenerator() {}

    /**
     * Populate the singleton Court with demo data.
     * Safe to call multiple times: it seeds only if empty.
     */
    public static Court seed() {
        Court court = Court.getInstance();
        if (!court.getAllLawyers().isEmpty()
                || !court.getAllAccused().isEmpty()
                || !court.getAllDepartments().isEmpty()
                || !court.getAllCases().isEmpty()
                || !court.getAllVerdicts().isEmpty()) {
            return court; // already seeded
        }
        

        try {
            // ===== Lawyers =====
            Lawyer l0 = new Lawyer(200, "Avi",  "Levi",  new Date(90, 5, 12), "Tel Aviv",   "0501234567", "avi@mail.com",  Gender.M, Specialization.criminal, 101, 15000);
            Lawyer l1 = new Lawyer(201, "Dana", "Cohen", new Date(92, 3,  3), "Haifa",      "0502345678", "dana@mail.com", Gender.F, Specialization.criminal, 102, 15500);
            Lawyer l2 = new Lawyer(202, "Itai", "Mor",   new Date(89,10, 20), "Jerusalem",  "0503456789", "itai@mail.com", Gender.M, Specialization.criminal, 103, 16000);
            Lawyer l3 = new Lawyer(203, "Noa",  "Bar",   new Date(93, 8, 14), "Beer Sheva", "0504567890", "noa@mail.com",  Gender.F, Specialization.criminal, 104, 16200);
            Lawyer l4 = new Lawyer(204, "Yair", "Tal",   new Date(91, 1,  8), "Herzliya",   "0505678901", "yair@mail.com", Gender.M, Specialization.criminal, 105, 15800);
            court.addLawyer(l0); court.addLawyer(l1); court.addLawyer(l2); court.addLawyer(l3); court.addLawyer(l4);

            // ===== Judges =====
            Judge j0 = new Judge(100, "Moshe", "Katz",   new Date(85, 2, 20), "Jerusalem",  "0501111111", "moshe@mail.com", Gender.M, Specialization.criminal, 201, 20000, 15);
            Judge j1 = new Judge(101, "Rina",  "Oz",     new Date(82, 7,  5), "Tel Aviv",   "0501111112", "rina@mail.com",  Gender.F, Specialization.criminal, 202, 20500, 18);
            Judge j2 = new Judge(102, "Ofer",  "Halevi", new Date(83, 9, 13), "Haifa",      "0501111113", "ofer@mail.com",  Gender.M, Specialization.criminal, 203, 21000, 20);
            Judge j3 = new Judge(103, "Gal",   "Amir",   new Date(81, 4, 27), "Ashdod",     "0501111114", "gal@mail.com",   Gender.F, Specialization.criminal, 204, 19800, 12);
            Judge j4 = new Judge(104, "Ziv",   "Shariv", new Date(84,12,  1), "Beer Sheva", "0501111115", "ziv@mail.com",   Gender.M, Specialization.criminal, 205, 20300, 16);
            court.addJudge(j0); court.addJudge(j1); court.addJudge(j2); court.addJudge(j3); court.addJudge(j4);

            // ===== Accused =====
            Accused a0 = new Accused(300, "Itzik", "Ronen",  new Date(95, 1,  5), "Beer Sheva", "0502222222", "itzik@mail.com", Gender.M, "Teacher");
            Accused a1 = new Accused(301, "Lior",  "Peretz", new Date(96, 6, 15), "Haifa",      "0502222223", "lior@mail.com",  Gender.M, "Driver");
            Accused a2 = new Accused(302, "Sarit", "Ben-Ami",new Date(97,11, 30), "Tel Aviv",   "0502222224", "sarit@mail.com", Gender.F, "Nurse");
            Accused a3 = new Accused(303, "Nadav", "Harel",  new Date(94, 3, 21), "Jerusalem",  "0502222225", "nadav@mail.com", Gender.M, "Student");
            Accused a4 = new Accused(304, "Maya",  "Gabay",  new Date(98, 9,  9), "Ashkelon",   "0502222226", "maya@mail.com",  Gender.F, "Engineer");
            court.addAccused(a0); court.addAccused(a1); court.addAccused(a2); court.addAccused(a3); court.addAccused(a4);

            // ===== Departments (created with an initial manager, then wired through Court helpers) =====
            Department dA = new Department(1, "Criminal Dept A", j0, "Building A", Specialization.criminal);
            Department dB = new Department(2, "Criminal Dept B", j1, "Building B", Specialization.criminal);
            Department dC = new Department(3, "Criminal Dept C", j2, "Building C", Specialization.criminal);
            Department dD = new Department(4, "Criminal Dept D", j3, "Building D", Specialization.criminal);
            Department dE = new Department(5, "Criminal Dept E", j4, "Building E", Specialization.criminal);
            court.addDepartment(dA); court.addDepartment(dB); court.addDepartment(dC); court.addDepartment(dD); court.addDepartment(dE);

            // Ensure bidirectional links via Court API (adds to lists, sets manager/department properly)
            court.addJudgeToDepartment(dA, j0);
            court.addJudgeToDepartment(dB, j1);
            court.addJudgeToDepartment(dC, j2);
            court.addJudgeToDepartment(dD, j3);
            court.addJudgeToDepartment(dE, j4);

            court.addLawyerToDepartment(dA, l0);
            court.addLawyerToDepartment(dA, l2);
            court.addLawyerToDepartment(dB, l1);
            court.addLawyerToDepartment(dC, l2);
            court.addLawyerToDepartment(dD, l3);
            court.addLawyerToDepartment(dE, l4);

            // ===== Cases (open dates use java.util.Date: year=YYYY-1900, month=0-based) =====
            Case c0 = new Case(a0, new Date(125, 0, 10), Status.inProcess, Specialization.criminal, null);
            Case c1 = new Case(a1, new Date(125, 1, 11), Status.inProcess, Specialization.criminal, null);
            Case c2 = new Case(a2, new Date(125, 2, 12), Status.inProcess, Specialization.criminal, null);
            Case c3 = new Case(a3, new Date(125, 3, 13), Status.inProcess, Specialization.criminal, null);
            Case c4 = new Case(a4, new Date(125, 4, 14), Status.inProcess, Specialization.criminal, null);

            // Assign lawyers to cases (before adding cases to Court to keep both sides linked by Court.addCase)
            c0.getLawyersList().add(l0); c0.getLawyersList().add(l2);
            c1.getLawyersList().add(l1);
            c2.getLawyersList().add(l2);
            c3.getLawyersList().add(l3);
            c4.getLawyersList().add(l4);

            court.addCase(c0); court.addCase(c1); court.addCase(c2); court.addCase(c3); court.addCase(c4);

            // ===== Verdicts (Court.addVerdict will set the verdict on the case and mark it finished) =====
            Verdict v0 = new Verdict("Guilty of theft",     new Date(125, 5,  1), j0, c0);
            Verdict v1 = new Verdict("Not guilty - alibi",  new Date(125, 2, 20), j0, c1);
            Verdict v2 = new Verdict("Guilty - plea deal",  new Date(125, 5,  5), j2, c2);
            Verdict v3 = new Verdict("Case dismissed",      new Date(125, 5,  7), j3, c3);
            Verdict v4 = new Verdict("Guilty - assault",    new Date(125, 5, 10), j4, c4);
            court.addVerdict(v0); court.addVerdict(v1); court.addVerdict(v2); court.addVerdict(v3); court.addVerdict(v4);
            j0.getCasesPresided().add(c0);
            j0.getCasesPresided().add(c1);
            j2.getCasesPresided().add(c2);
            j3.getCasesPresided().add(c3);
            j4.getCasesPresided().add(c4);
            
         Judge j5 = new Judge(105, "Tamar", "Rosenberg", new Date(80, 11, 25), "Tel Aviv", "0501111116", "tamar@mail.com", Gender.F, Specialization.criminal, 206, 22000, 22);
         Judge j6 = new Judge(106, "David", "Goldstein", new Date(86, 5, 18), "Jerusalem", "0501111117", "david@mail.com", Gender.M, Specialization.criminal, 207, 19500, 10);

         court.addJudge(j5); 
         court.addJudge(j6);

         court.addJudgeToDepartment(dA, j5);
         court.addJudgeToDepartment(dA, j6);

            // ===== Extra in-process case for Judge Moshe (no verdict) =====
            Case c5 = new Case(a0, new Date(125, 6, 15), Status.inProcess, Specialization.criminal, null);
            c5.getLawyersList().add(l0);
            court.addCase(c5);
            // Link for reporting (no Court method for this, so we update the judge list directly)
            j0.getCasesPresided().add(c5);
            
        } catch (Exception e) {
            throw new RuntimeException("Seeding failed", e);
        }

        return court;
    }
}

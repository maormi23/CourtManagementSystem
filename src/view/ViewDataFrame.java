package view;

import control.Court;
import model.*;
import enums.Specialization;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;

public class ViewDataFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private Court court;
    private JTable table;
    private JComboBox<String> selector;

    public ViewDataFrame(Court court) {
        this.court = court;
        setTitle("View Data");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top bar with dropdown
        JPanel top = new JPanel();
        selector = new JComboBox<>(new String[]{
                "Lawyers", "Judges", "Accused", "Departments", "Cases", "Verdicts"
        });
        top.add(new JLabel("Select entity:"));
        top.add(selector);

        add(top, BorderLayout.NORTH);

        // Table
        table = new JTable();
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // On change
        selector.addActionListener(e -> loadTable());

        loadTable(); // initial
    }

    private void loadTable() {
        String choice = (String) selector.getSelectedItem();
        if (choice == null) return;

        switch (choice) {
            case "Lawyers" -> showLawyers();
            case "Judges" -> showJudges();
            case "Accused" -> showAccused();
            case "Departments" -> showDepartments();
            case "Cases" -> showCases();
            case "Verdicts" -> showVerdicts();
        }
    }

    private void showLawyers() {
        String[] cols = {"ID", "First Name", "Last Name", "Specialization", "Salary"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (Lawyer l : court.getAllLawyers().values()) {
            if (l instanceof Judge) continue; // skip judges
            model.addRow(new Object[]{
                    l.getId(), l.getFirstName(), l.getLastName(),
                    safeSpec(l.getSpecialization()), l.getSalary()
            });
        }
        table.setModel(model);
    }

    private void showJudges() {
        String[] cols = {"ID", "First Name", "Last Name", "Specialization", "Experience"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (Lawyer l : court.getAllLawyers().values()) {
            if (l instanceof Judge j) {
                model.addRow(new Object[]{
                        j.getId(), j.getFirstName(), j.getLastName(),
                        safeSpec(j.getSpecialization()), j.getExperienceYear()
                });
            }
        }
        table.setModel(model);
    }

    private void showAccused() {
        String[] cols = {"ID", "First Name", "Last Name", "Job", "#Cases"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        java.util.List<Accused> list = new java.util.ArrayList<>(court.getAllAccused().values());
        list.sort(java.util.Comparator.comparingInt(Accused::getId)); 

        for (Accused a : list) {
            model.addRow(new Object[]{
                a.getId(),
                a.getFirstName(),
                a.getLastName(),
                a.getJob(),
                (a.getCases() == null ? 0 : a.getCases().size())
            });
        }

        table.setModel(model); 
    }


    private void showDepartments() {
        String[] cols = {"Number", "Name", "Specialization", "Manager", "#Lawyers"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (Department d : court.getAllDepartments().values()) {
            String manager = d.getManager() == null ? "" : d.getManager().getFirstName();
            model.addRow(new Object[]{
                    d.getNumber(), d.getName(),
                    safeSpec(d.getSpecialization()),
                    manager, d.getLawyersList().size()
            });
        }
        table.setModel(model);
    }

    private void showCases() {
        String[] cols = {"Code", "Accused", "Status", "Type", "#Lawyers", "VerdictID"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (Case c : court.getAllCases().values()) {
            model.addRow(new Object[]{
                    c.getCode(),
                    c.getAccused() == null ? "" : c.getAccused().getFirstName(),
                    String.valueOf(c.getCaseStatus()),
                    safeSpec(c.getCaseType()),
                    c.getLawyersList() == null ? 0 : c.getLawyersList().size(),
                    c.getVerdict() == null ? "" : c.getVerdict().getVerdictID()
            });
        }
        table.setModel(model);
    }

    private void showVerdicts() {
        String[] cols = {"Verdict ID", "Judge", "Case Code", "Summary"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (Verdict v : court.getAllVerdicts().values()) {
            model.addRow(new Object[]{
                    v.getVerdictID(),
                    v.getJudge() == null ? "" : v.getJudge().getFirstName(),
                    v.getCase() == null ? "" : v.getCase().getCode(),
                    v.getVerdictSummary()
            });
        }
        table.setModel(model);
    }

    private String safeSpec(Specialization s) {
        return s == null ? "" : s.name();
    }
}
package view;

import control.Court;
import exceptions.ObjectAlreadyExistsException;
import exceptions.ObjectDoesNotExistException;
import model.Department;
import model.Judge;
import model.Lawyer;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class AddLawyerToDepartmentFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final Court court;
    private final JComboBox<DepartmentItem> deptBox = new JComboBox<>();
    private final JComboBox<LawyerItem> lawyerBox = new JComboBox<>();

    public AddLawyerToDepartmentFrame(Court court) {
        this.court = court;

        setTitle("Add Lawyer to Department");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(560, 240);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel title = new JLabel("Add Lawyer to Department", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(25, 25, 112));
        title.setBounds(40, 15, 480, 30);
        add(title);

        JLabel lDept = new JLabel("Department:");
        lDept.setBounds(40, 70, 120, 25);
        add(lDept);
        deptBox.setBounds(160, 70, 360, 28);
        add(deptBox);

        JLabel lLawyer = new JLabel("Lawyer:");
        lLawyer.setBounds(40, 105, 120, 25);
        add(lLawyer);
        lawyerBox.setBounds(160, 105, 360, 28);
        add(lawyerBox);

        JButton addBtn = new JButton("Add");
        JButton cancel = new JButton("Cancel");
        addBtn.setBounds(360, 150, 75, 32);
        cancel.setBounds(445, 150, 75, 32);
        add(addBtn);
        add(cancel);

        cancel.addActionListener(e -> dispose());
        addBtn.addActionListener(e -> onAdd());

        loadDepartments();
        loadLawyers();
    }

    private void loadDepartments() {
        deptBox.removeAllItems();
        try {
            for (Map.Entry<Integer, Department> e : court.getAllDepartments().entrySet()) {
                Department d = e.getValue();
                if (d != null) deptBox.addItem(new DepartmentItem(d));
            }
        } catch (Throwable t) {
            JOptionPane.showMessageDialog(this, "Cannot load departments: " + t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadLawyers() {
        lawyerBox.removeAllItems();
        try {
            for (Map.Entry<Integer, Lawyer> e : court.getAllLawyers().entrySet()) {
                Lawyer l = e.getValue();
                if (l != null && !(l instanceof Judge)) { 
                    lawyerBox.addItem(new LawyerItem(l));
                }
            }
        } catch (Throwable t) {
            JOptionPane.showMessageDialog(this, "Cannot load lawyers: " + t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAdd() {
        DepartmentItem dItem = (DepartmentItem) deptBox.getSelectedItem();
        LawyerItem lItem = (LawyerItem) lawyerBox.getSelectedItem();
        if (dItem == null || dItem.ref == null || lItem == null || lItem.ref == null) {
            JOptionPane.showMessageDialog(this, "Select department and lawyer.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            court.addLawyerToDepartment(dItem.ref, lItem.ref);
            JOptionPane.showMessageDialog(this, "Lawyer added to department.");
            dispose();
        } catch (ObjectAlreadyExistsException | ObjectDoesNotExistException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class DepartmentItem {
        final Department ref;
        final String label;
        DepartmentItem(Department d) {
            this.ref = d;
            this.label = "Dept " + d.getNumber() + " - " + d.getName();
        }
        @Override public String toString() { return label; }
    }

    private static class LawyerItem {
        final Lawyer ref;
        final String label;
        LawyerItem(Lawyer l) {
            this.ref = l;
            this.label = l.getFirstName() + " " + l.getLastName() + " (ID " + l.getId() + ")";
        }
        @Override public String toString() { return label; }
    }
}

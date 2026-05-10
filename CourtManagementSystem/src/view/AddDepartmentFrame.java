package view;

import control.Court;
import enums.Specialization;
import exceptions.ObjectAlreadyExistsException;
import model.Judge;
import model.Lawyer;
import model.Department;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class AddDepartmentFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final Court court;
    private final JTextField numField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField buildingField = new JTextField();
    private final JComboBox<Specialization> specBox = new JComboBox<>(Specialization.values());
    private final JComboBox<JudgeItem> managerBox = new JComboBox<>();

    public AddDepartmentFrame(Court court) {
        this.court = court;

        setTitle("Add Department");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 320);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel title = new JLabel("Add Department", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(25, 25, 112));
        title.setBounds(40, 15, 440, 30);
        add(title);

        JLabel lNum = new JLabel("Department Number:");
        lNum.setBounds(40, 65, 150, 25);
        add(lNum);
        numField.setBounds(200, 65, 280, 28);
        add(numField);

        JLabel lName = new JLabel("Name:");
        lName.setBounds(40, 100, 150, 25);
        add(lName);
        nameField.setBounds(200, 100, 280, 28);
        add(nameField);

        JLabel lBuilding = new JLabel("Building:");
        lBuilding.setBounds(40, 135, 150, 25);
        add(lBuilding);
        buildingField.setBounds(200, 135, 280, 28);
        add(buildingField);

        JLabel lSpec = new JLabel("Specialization:");
        lSpec.setBounds(40, 170, 150, 25);
        add(lSpec);
        specBox.setBounds(200, 170, 280, 28);
        add(specBox);

        JLabel lMgr = new JLabel("Manager (Judge):");
        lMgr.setBounds(40, 205, 150, 25);
        add(lMgr);
        managerBox.setBounds(200, 205, 280, 28);
        add(managerBox);

        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");
        save.setBounds(310, 245, 85, 32);
        cancel.setBounds(395, 245, 85, 32);
        add(save);
        add(cancel);

        cancel.addActionListener(e -> dispose());
        save.addActionListener(e -> onSave());
        loadJudges();
    }

    private void loadJudges() {
        managerBox.removeAllItems();
        try {
            managerBox.addItem(new JudgeItem(null, "-- No Manager --"));
            for (Map.Entry<Integer, Lawyer> e : court.getAllLawyers().entrySet()) {
                Lawyer l = e.getValue();
                if (l instanceof Judge) {
                    managerBox.addItem(new JudgeItem((Judge) l));
                }
            }
        } catch (Throwable t) {
            JOptionPane.showMessageDialog(this, "Cannot load judges: " + t.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSave() {
        String numStr = numField.getText().trim();
        String name   = nameField.getText().trim();
        String building = buildingField.getText().trim();
        Specialization spec = (Specialization) specBox.getSelectedItem();
        JudgeItem mgrItem = (JudgeItem) managerBox.getSelectedItem();
        Judge manager = (mgrItem == null) ? null : mgrItem.ref;

        if (numStr.isEmpty() || name.isEmpty() || building.isEmpty() || spec == null) {
            JOptionPane.showMessageDialog(this, "All fields except manager are required.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int number = Integer.parseInt(numStr);
            Department d = new Department(number, name, manager, building, spec);
            court.addDepartment(d);
            JOptionPane.showMessageDialog(this, "Department added successfully.");
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Department number must be an integer.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ObjectAlreadyExistsException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class JudgeItem {
        final Judge ref;
        final String label;
        JudgeItem(Judge j) {
            this.ref = j;
            this.label = j.getFirstName() + " " + j.getLastName() + " (ID " + j.getId() + ")";
        }
        JudgeItem(Judge j, String label) { this.ref = j; this.label = label; }
        @Override public String toString() { return label; }
    }
}

package view;

import javax.swing.*;
import java.awt.*;

import control.Court;
import model.Department;
import model.Judge;

public class AppointDeptManagerFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    public AppointDeptManagerFrame(Court courtSystem) {
        setTitle("Appoint Department Manager");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 280);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        JLabel title = new JLabel("Appoint Department Manager", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(25, 25, 112));
        title.setBounds(60, 20, 400, 30);
        add(title);

        JLabel depLabel = createStyledLabel("Department:");
        depLabel.setBounds(60, 90, 120, 25);
        add(depLabel);

        JComboBox<Department> depCombo = new JComboBox<>();
        courtSystem.getAllDepartments().values().forEach(depCombo::addItem);
        depCombo.setBounds(180, 90, 280, 30);
        depCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                String txt = (value instanceof Department d)
                        ? d.getNumber() + " - " + d.getName() + " (" + d.getBuilding() + ")"
                        : String.valueOf(value);
                return super.getListCellRendererComponent(list, txt, index, isSelected, cellHasFocus);
            }
        });
        add(depCombo);

        JButton appointBtn = createStyledButton("APPOINT", new Color(0, 123, 255));
        appointBtn.setBounds(320, 170, 140, 40);
        add(appointBtn);

        JButton cancelBtn = createStyledButton("CANCEL", new Color(108, 117, 125));
        cancelBtn.setBounds(180, 170, 120, 40);
        cancelBtn.addActionListener(e -> dispose());
        add(cancelBtn);

        appointBtn.addActionListener(e -> {
            if (depCombo.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this, "No departments found.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Department dept = (Department) depCombo.getSelectedItem();
            try {
                Judge newMgr = courtSystem.appointANewManager(dept);
                if (newMgr == null) {
                    JOptionPane.showMessageDialog(this, "No suitable judge found.", "Result", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "New manager appointed:\n" +
                            newMgr.getFirstName() + " " + newMgr.getLastName() +
                            " (ID " + newMgr.getId() + ")\nDepartment: " + dept.getName(),
                            "Result", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(60, 60, 60));
        return label;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
}
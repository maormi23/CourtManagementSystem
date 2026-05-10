package view;

import control.Court;
import exceptions.ObjectDoesNotExistException;
import model.Department;
import model.Judge;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class RemoveDepartmentFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final Court court;
    private final JComboBox<Department> deptCombo = new JComboBox<>();

    public RemoveDepartmentFrame(Court court) {
        this.court = court;

        setTitle("Remove Department");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(480, 220);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel title = new JLabel("Remove Department", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(139, 0, 0));
        title.setBounds(40, 15, 400, 30);
        add(title);

        JLabel lChoose = new JLabel("Choose Department:");
        lChoose.setBounds(40, 70, 140, 25);
        add(lChoose);

        // combo
        deptCombo.setBounds(190, 68, 250, 30);
        deptCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Department) {
                    Department d = (Department) value;
                    String name = "";
                    try { name = d.getName(); } catch (Throwable ignored) {}

                    String mgr = "";
                    try {
                        Judge j = d.getManager();
                        if (j != null) {
                            String fn = "";
                            String ln = "";
                            try { fn = j.getFirstName(); } catch (Throwable ignored) {}
                            try { ln = j.getLastName(); }  catch (Throwable ignored) {}
                            mgr = (fn + " " + ln).trim();
                        }
                    } catch (Throwable ignored) {}

                    int num = -1;
                    try { num = d.getNumber(); } catch (Throwable ignored) {}

                    String text = "#" + num + "  " + (name != null ? name : "") + (mgr.isEmpty() ? "" : "  (Manager: " + mgr + ")");
                    lbl.setText(text.trim());
                }
                return lbl;
            }
        });
        add(deptCombo);

        JButton remove = new JButton("Remove");
        JButton cancel = new JButton("Cancel");
        remove.setBounds(270, 120, 85, 32);
        cancel.setBounds(355, 120, 85, 32);
        add(remove);
        add(cancel);

        cancel.addActionListener(e -> dispose());
        remove.addActionListener(e -> onRemove());

        // load departments
        reloadDepartments();
    }

    private void reloadDepartments() {
        deptCombo.removeAllItems();
        try {
            Map<Integer, Department> map = court.getAllDepartments();
            if (map == null || map.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No departments found.", "Info", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                return;
            }
            List<Department> list = new ArrayList<>(map.values());
            // sort by department number (fallback by name if needed)
            list.sort((a, b) -> {
                int na = safeNumber(a), nb = safeNumber(b);
                if (na != nb) return Integer.compare(na, nb);
                String sa = safeName(a), sb = safeName(b);
                return sa.compareToIgnoreCase(sb);
            });
            for (Department d : list) {
                deptCombo.addItem(d);
            }
            if (deptCombo.getItemCount() > 0) {
                deptCombo.setSelectedIndex(0);
            }
        } catch (Throwable t) {
            JOptionPane.showMessageDialog(this, "Failed to load departments: " + t.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void onRemove() {
        Department selected = (Department) deptCombo.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please choose a department.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String name = safeName(selected);
        int number = safeNumber(selected);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete department #" + number + (name.isEmpty() ? "" : " (" + name + ")") + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            court.removeDepartment(selected);
            JOptionPane.showMessageDialog(this, "Department removed successfully.");

            // remove from combo model
            deptCombo.removeItem(selected);

            // if nothing left, close the window
            if (deptCombo.getItemCount() == 0) {
                dispose();
            }
        } catch (ObjectDoesNotExistException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // helpers to safely access fields (in case getters throw or are missing)
    private static int safeNumber(Department d) {
        try { return d.getNumber(); } catch (Throwable ignored) { return -1; }
    }
    private static String safeName(Department d) {
        try {
            String s = d.getName();
            return (s == null) ? "" : s;
        } catch (Throwable ignored) { return ""; }
    }
}

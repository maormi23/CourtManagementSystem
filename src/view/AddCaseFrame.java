package view;

import control.Court;
import enums.Specialization;
import enums.Status;
import exceptions.ObjectAlreadyExistsException;
import exceptions.ObjectDoesNotExistException;
import model.Accused;
import model.Case;
import model.Lawyer;
import model.Judge;

import javax.swing.*;
import java.awt.*;
import java.awt.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Map.Entry;

public class AddCaseFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final Court court;

    private final JComboBox<AccusedItem> accusedBox = new JComboBox<>();
    private final JSpinner openDateSpinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
    private final JComboBox<Status> statusBox = new JComboBox<>(Status.values());
    private final JComboBox<Specialization> specBox = new JComboBox<>(Specialization.values());
    private final JList<LawyerItem> lawyersList = new JList<>();

    public AddCaseFrame(Court court) {
        this.court = court;

        setTitle("Add Case");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(620, 460);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel title = new JLabel("Add Case", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(25, 25, 112));
        title.setBounds(40, 15, 540, 30);
        add(title);

        int y = 65;

        JLabel lAccused = new JLabel("Accused:");
        lAccused.setBounds(40, y, 140, 25);
        add(lAccused);
        accusedBox.setBounds(180, y, 400, 28);
        add(accusedBox);
        y += 35;

        JLabel lDate = new JLabel("Open Date:");
        lDate.setBounds(40, y, 140, 25);
        add(lDate);
        JSpinner.DateEditor ed = new JSpinner.DateEditor(openDateSpinner, "yyyy-MM-dd");
        openDateSpinner.setEditor(ed);
        openDateSpinner.setBounds(180, y, 180, 28);
        add(openDateSpinner);
        y += 35;

        JLabel lStatus = new JLabel("Status:");
        lStatus.setBounds(40, y, 140, 25);
        add(lStatus);
        statusBox.setBounds(180, y, 180, 28);
        try { statusBox.setSelectedItem(Status.inProcess); } catch (Throwable ignore) {}
        add(statusBox);
        y += 35;

        JLabel lSpec = new JLabel("Case Type:");
        lSpec.setBounds(40, y, 140, 25);
        add(lSpec);
        specBox.setBounds(180, y, 180, 28);
        add(specBox);
        y += 35;

        JLabel lLawyers = new JLabel("Lawyers (multi-select):");
        lLawyers.setBounds(40, y, 200, 25);
        add(lLawyers);
        JScrollPane sp = new JScrollPane(lawyersList);
        sp.setBounds(180, y, 400, 180);
        add(sp);
        lawyersList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");
        save.setBounds(380, 380, 90, 32);
        cancel.setBounds(470, 380, 90, 32);
        add(save);
        add(cancel);

        cancel.addActionListener(e -> dispose());
        save.addActionListener(e -> onSave());

        loadAccused();
        loadLawyers();
    }

    private void loadAccused() {
        accusedBox.removeAllItems();
        try {
            for (Entry<Integer, Accused> e : court.getAllAccused().entrySet()) {
                Accused a = e.getValue();
                if (a != null) accusedBox.addItem(new AccusedItem(a));
            }
        } catch (Throwable t) {
            JOptionPane.showMessageDialog(this, "Cannot load accused: " + t.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadLawyers() {
        DefaultListModel<LawyerItem> model = new DefaultListModel<>();
        try {
            for (Entry<Integer, Lawyer> e : court.getAllLawyers().entrySet()) {
                Lawyer l = e.getValue();
                if (l != null && !(l instanceof Judge)) { // exclude judges
                    model.addElement(new LawyerItem(l));
                }
            }
        } catch (Throwable t) {
            JOptionPane.showMessageDialog(this, "Cannot load lawyers: " + t.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        lawyersList.setModel(model);
    }

    private void onSave() {
        AccusedItem accItem = (AccusedItem) accusedBox.getSelectedItem();
        if (accItem == null || accItem.ref == null) {
            JOptionPane.showMessageDialog(this, "Select an accused.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date openDate = (Date) openDateSpinner.getValue();
        Status status = (Status) statusBox.getSelectedItem();
        Specialization spec = (Specialization) specBox.getSelectedItem();

        // Collect selected lawyers robustly (no streams, no getSelectedValuesList)
        java.util.List<model.Lawyer> chosen = new java.util.ArrayList<>();
        ListModel<LawyerItem> model = lawyersList.getModel();
        int[] indices = lawyersList.getSelectedIndices();
        for (int idx : indices) {
            LawyerItem it = model.getElementAt(idx);
            if (it != null && it.ref != null) chosen.add(it.ref);
        }

        try {
            // Create the case with auto-generated String code and without verdict
            Case c = new Case(accItem.ref, openDate, status, spec, null);

            // Attach lawyers if the list exists
            try {
                if (c.getLawyersList() != null && !chosen.isEmpty()) {
                    c.getLawyersList().addAll(chosen);
                }
            } catch (Throwable ignore) {}

            court.addCase(c);
            JOptionPane.showMessageDialog(this, "Case added successfully.");
            dispose();
        } catch (exceptions.ObjectAlreadyExistsException | exceptions.ObjectDoesNotExistException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private static class AccusedItem {
        final Accused ref;
        final String label;
        AccusedItem(Accused a) {
            this.ref = a;
            this.label = a.getFirstName() + " " + a.getLastName() + " (ID " + a.getId() + ")";
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

package view;

import control.Court;
import exceptions.ObjectDoesNotExistException;
import model.Case;
import model.Accused;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class RemoveCaseFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final Court court;
    private final JComboBox<CaseItem> caseBox = new JComboBox<>();

    public RemoveCaseFrame(Court court) {
        this.court = court;

        setTitle("Remove Case");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 220);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel title = new JLabel("Remove Case", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(139, 0, 0));
        title.setBounds(40, 15, 440, 30);
        add(title);

        JLabel lCase = new JLabel("Select Case:");
        lCase.setBounds(40, 70, 120, 25);
        add(lCase);

        caseBox.setBounds(160, 70, 320, 28);
        add(caseBox);

        JButton remove = new JButton("Remove");
        JButton cancel = new JButton("Cancel");
        remove.setBounds(300, 120, 90, 32);
        cancel.setBounds(390, 120, 90, 32);
        add(remove);
        add(cancel);

        cancel.addActionListener(e -> dispose());
        remove.addActionListener(e -> onRemove());

        loadCases();
    }

    private void loadCases() {
        caseBox.removeAllItems();
        try {
            for (Map.Entry<String, Case> e : court.getAllCases().entrySet()) {
                Case c = e.getValue();
                if (c != null) {
                    caseBox.addItem(new CaseItem(c));
                }
            }
        } catch (Throwable t) {
            JOptionPane.showMessageDialog(this, "Cannot load cases: " + t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onRemove() {
        CaseItem item = (CaseItem) caseBox.getSelectedItem();
        if (item == null || item.ref == null) {
            JOptionPane.showMessageDialog(this, "Select a case to remove.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            court.removeCase(item.ref);
            JOptionPane.showMessageDialog(this, "Case removed successfully.");
            dispose();
        } catch (ObjectDoesNotExistException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            loadCases();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class CaseItem {
        final Case ref;
        final String label;

        CaseItem(Case c) {
            this.ref = c;
            String accused = "";
            try {
                Accused a = c.getAccused();
                accused = (a == null) ? "" : (a.getFirstName() + " " + a.getLastName() + " (ID " + a.getId() + ")");
            } catch (Throwable ignore) {}
            this.label = "Case " + c.getCode() + (accused.isEmpty() ? "" : (" - " + accused));
        }

        @Override public String toString() { return label; }
    }
}

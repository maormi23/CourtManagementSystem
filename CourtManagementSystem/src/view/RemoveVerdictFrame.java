package view;

import control.Court;
import exceptions.ObjectDoesNotExistException;
import model.Case;
import model.Verdict;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class RemoveVerdictFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final Court court;
    private final JComboBox<CaseItem> caseBox = new JComboBox<>();

    public RemoveVerdictFrame(Court court) {
        this.court = court;

        setTitle("Remove Verdict");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 220);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel title = new JLabel("Remove Verdict", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(139, 0, 0));
        title.setBounds(40, 15, 440, 30);
        add(title);

        JLabel lCase = new JLabel("Select Case (has verdict):");
        lCase.setBounds(40, 70, 200, 25);
        add(lCase);

        caseBox.setBounds(240, 70, 240, 28);
        add(caseBox);

        JButton remove = new JButton("Remove");
        JButton cancel = new JButton("Cancel");
        remove.setBounds(300, 120, 90, 32);
        cancel.setBounds(390, 120, 90, 32);
        add(remove);
        add(cancel);

        cancel.addActionListener(e -> dispose());
        remove.addActionListener(e -> onRemove());

        loadCasesWithVerdict();
    }

    private void loadCasesWithVerdict() {
        caseBox.removeAllItems();
        try {
            for (Map.Entry<String, Case> e : court.getAllCases().entrySet()) {
                Case c = e.getValue();
                if (c != null && c.getVerdict() != null) {
                    caseBox.addItem(new CaseItem(c));
                }
            }
        } catch (Throwable t) {
            JOptionPane.showMessageDialog(this, "Cannot load cases: " + t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onRemove() {
        CaseItem item = (CaseItem) caseBox.getSelectedItem();
        if (item == null || item.ref == null || item.ref.getVerdict() == null) {
            JOptionPane.showMessageDialog(this, "Select a case with a verdict.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Verdict v = item.ref.getVerdict();
            court.removeVerdict(v);
            JOptionPane.showMessageDialog(this, "Verdict removed successfully.");
            dispose();
        } catch (ObjectDoesNotExistException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            loadCasesWithVerdict();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class CaseItem {
        final Case ref;
        final String label;

        CaseItem(Case c) {
            this.ref = c;
            this.label = "Case " + c.getCode();
        }

        @Override public String toString() { return label; }
    }
}

package view;

import control.Court;
import enums.Status;
import exceptions.ObjectAlreadyExistsException;
import exceptions.ObjectDoesNotExistException;
import model.Case;
import model.Judge;
import model.Verdict;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class AddVerdictFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final Court court;
    private final Judge judge;

    private final JComboBox<CaseDisplay> caseBox = new JComboBox<>();
    private final JTextArea summaryArea = new JTextArea(6, 40);

    public AddVerdictFrame(Court court, Judge judge) {
        this.court = court;
        this.judge = judge;

        setTitle("Add Verdict - Judge " + judge.getFirstName() + " " + judge.getLastName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("Select Case:"));
        caseBox.setPrototypeDisplayValue(new CaseDisplay("XXXXXXXXXXXXXXXXXXXXXXXXX"));
        row1.add(caseBox);

        JPanel row2 = new JPanel(new BorderLayout());
        row2.add(new JLabel("Verdict Summary:"), BorderLayout.NORTH);
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);
        row2.add(new JScrollPane(summaryArea), BorderLayout.CENTER);

        form.add(row1);
        form.add(Box.createVerticalStrut(10));
        form.add(row2);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");
        buttons.add(save);
        buttons.add(cancel);

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        cancel.addActionListener(e -> dispose());
        save.addActionListener(e -> onSave());

        loadEligibleCases();
    }

    private void loadEligibleCases() {
        caseBox.removeAllItems();

        List<Case> presided = (judge.getCasesPresided() == null)
                ? java.util.Collections.emptyList()
                : judge.getCasesPresided();

        for (Case c : court.getAllCases().values()) {
            if (c == null) continue;
            boolean open = c.getCaseStatus() == Status.inProcess && c.getVerdict() == null;
            boolean related = presided.contains(c);
            if (open && related) {
                caseBox.addItem(new CaseDisplay(c));
            }
        }
    }

    private void onSave() {
        CaseDisplay selected = (CaseDisplay) caseBox.getSelectedItem();
        if (selected == null || selected.caseRef == null) {
            JOptionPane.showMessageDialog(this, "Please select a case.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String summary = summaryArea.getText().trim();
        if (summary.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter verdict summary.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Case c = selected.caseRef;

        try {
            Verdict v = new Verdict(summary, new Date(), judge, c);
            court.addVerdict(v);
            JOptionPane.showMessageDialog(this, "Verdict added successfully.");
            dispose();
        } catch (ObjectAlreadyExistsException | ObjectDoesNotExistException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            loadEligibleCases();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class CaseDisplay {
        final Case caseRef;
        final String label;

        CaseDisplay(Case c) {
            this.caseRef = c;
            if (c == null) {
                this.label = "";
            } else {
                String accused = (c.getAccused() == null) ? "" :
                        c.getAccused().getFirstName() + " " + c.getAccused().getLastName() + " (ID " + c.getAccused().getId() + ")";
                this.label = "Case " + c.getCode() + "  -  " + accused + "  [" + c.getCaseStatus() + "]";
            }
        }

        CaseDisplay(String label) {
            this.caseRef = null;
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
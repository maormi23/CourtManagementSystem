package view;

import control.Court;
import enums.Specialization;
import enums.Status;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CasesFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private final Court court;
    private final JTable table = new JTable();
    private Judge currentJudge;

    // Build for Lawyer view (no "#Lawyers" column)
    public CasesFrame(Court court, Lawyer lawyer) {
        this.court = court;
        this.currentJudge = null;
        setTitle("Cases of Lawyer " + lawyer.getFirstName() + " " + lawyer.getLastName());
        initUI();
        loadForLawyer(lawyer);
    }

    // Build for Judge view (no "#Lawyers" column), with management actions
    public CasesFrame(Court court, Judge judge) {
        this.court = court;
        this.currentJudge = judge;
        setTitle("Cases of Judge " + judge.getFirstName() + " " + judge.getLastName());
        initUI();
        loadForJudge(judge);
        addManagementFeatures();
    }

    // Basic frame + table + close button
    private void initUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        bottomPanel.add(closeBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Judge-only: enable Add Verdict / Cancel for inProcess cases
    private void addManagementFeatures() {
        if (currentJudge == null) return;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addVerdictBtn = new JButton("Add Verdict");
        JButton cancelCaseBtn = new JButton("Cancel Case");
        JLabel instructionLabel = new JLabel("Select a case in the table, then:");

        addVerdictBtn.addActionListener(e -> handleFinishCase());
        cancelCaseBtn.addActionListener(e -> handleCancelCase());

        addVerdictBtn.setBackground(new Color(34, 139, 34));
        addVerdictBtn.setForeground(Color.WHITE);
        cancelCaseBtn.setBackground(new Color(220, 20, 60));
        cancelCaseBtn.setForeground(Color.WHITE);

        buttonPanel.add(instructionLabel);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(addVerdictBtn);
        buttonPanel.add(cancelCaseBtn);
        add(buttonPanel, BorderLayout.NORTH);

        table.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = table.getSelectedRow() >= 0;
            boolean isInProcess = false;
            if (hasSelection) {
                String status = (String) table.getValueAt(table.getSelectedRow(), 2); // "Status" column
                isInProcess = "inProcess".equals(status);
            }
            addVerdictBtn.setEnabled(hasSelection && isInProcess);
            cancelCaseBtn.setEnabled(hasSelection && isInProcess);
        });

        addVerdictBtn.setEnabled(false);
        cancelCaseBtn.setEnabled(false);
    }

    // Open AddVerdict flow for selected inProcess case
    private void handleFinishCase() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a case first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String caseCode = (String) table.getValueAt(row, 0);
        String status = (String) table.getValueAt(row, 2);
        if (!"inProcess".equals(status)) {
            JOptionPane.showMessageDialog(this, "Can only add verdict to cases that are 'inProcess'.", "Invalid Status", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Case targetCase = findCaseByCode(caseCode);
        if (targetCase != null) {
            new AddVerdictFrame(court, currentJudge).setVisible(true);
            dispose();
        }
    }

    // Cancel selected inProcess case (confirm + refresh)
    private void handleCancelCase() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a case first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String caseCode = (String) table.getValueAt(row, 0);
        String status = (String) table.getValueAt(row, 2);
        if (!"inProcess".equals(status)) {
            JOptionPane.showMessageDialog(this, "Can only cancel cases that are 'inProcess'.", "Invalid Status", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Case targetCase = findCaseByCode(caseCode);
        if (targetCase != null) {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel case " + caseCode + "?",
                "Confirm Cancel",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                targetCase.setCaseStatus(Status.canceled);
                JOptionPane.showMessageDialog(this, "Case " + caseCode + " has been canceled.");
                loadForJudge(currentJudge);
            }
        }
    }

    // Lookup by case code
    private Case findCaseByCode(String code) {
        for (Case c : court.getAllCases().values()) {
            if (c != null && code.equals(c.getCode())) {
                return c;
            }
        }
        return null;
    }

    // Table data for Lawyer view (removed "#Lawyers" column)
    private void loadForLawyer(Lawyer lawyer) {
        String[] cols = {"Code", "Accused", "Status", "Type", "Lawyers", "VerdictID"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Case c : court.getAllCases().values()) {
            if (c.getLawyersList() != null && c.getLawyersList().contains(lawyer)) {
                String accused = (c.getAccused() == null) ? "" :
                        c.getAccused().getFirstName() + " " + c.getAccused().getLastName() +
                        " (ID " + c.getAccused().getId() + ")";

                String lawyers = getLawyersNames(c);
                Object verdictId = (c.getVerdict() == null) ? "" : c.getVerdict().getVerdictID();

                model.addRow(new Object[]{
                    c.getCode(),
                    accused,
                    String.valueOf(c.getCaseStatus()),
                    spec(c.getCaseType()),
                    lawyers,
                    verdictId
                });
            }
        }
        table.setModel(model);
    }

    // Table data for Judge view (removed "#Lawyers" column)
    private void loadForJudge(Judge judge) {
        String[] cols = {"Code", "Accused", "Status", "Type", "Lawyers", "VerdictID"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Case c : court.getAllCases().values()) {
            boolean showCase = false;
            if (c.getVerdict() != null && c.getVerdict().getJudge() != null
                    && c.getVerdict().getJudge().equals(judge)) {
                showCase = true;
            }
            if (judge.getCasesPresided() != null && judge.getCasesPresided().contains(c)) {
                showCase = true;
            }
            if (showCase) {
                String accused = (c.getAccused() == null) ? "" :
                        c.getAccused().getFirstName() + " " + c.getAccused().getLastName() +
                        " (ID " + c.getAccused().getId() + ")";

                String lawyers = getLawyersNames(c);
                Object verdictId = (c.getVerdict() == null) ? "" : c.getVerdict().getVerdictID();

                model.addRow(new Object[]{
                    c.getCode(),
                    accused,
                    String.valueOf(c.getCaseStatus()),
                    spec(c.getCaseType()),
                    lawyers,
                    verdictId
                });
            }
        }
        table.setModel(model);
    }

    // Join lawyer names for display
    private String getLawyersNames(Case c) {
        if (c.getLawyersList() == null || c.getLawyersList().isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (Lawyer l : c.getLawyersList()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(l.getFirstName()).append(" ").append(l.getLastName());
        }
        return sb.toString();
    }

    // Safe enum-to-string
    private String spec(Specialization s) {
        return (s == null) ? "" : s.name();
    }
}

package view;

import control.Court;
import model.Judge;
import model.Lawyer;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class SelectJudgeForVerdictFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final Court court;
    private final JComboBox<JudgeItem> judgeBox = new JComboBox<>();

    public SelectJudgeForVerdictFrame(Court court) {
        this.court = court;

        setTitle("Add Verdict - Choose Judge");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 200);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel title = new JLabel("Add Verdict - Choose Judge", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(25, 25, 112));
        title.setBounds(40, 15, 440, 30);
        add(title);

        JLabel lJudge = new JLabel("Select Judge:");
        lJudge.setBounds(40, 70, 120, 25);
        add(lJudge);

        judgeBox.setBounds(160, 70, 320, 28);
        add(judgeBox);

        JButton next = new JButton("Next");
        JButton cancel = new JButton("Cancel");
        next.setBounds(300, 120, 90, 32);
        cancel.setBounds(390, 120, 90, 32);
        add(next);
        add(cancel);

        cancel.addActionListener(e -> dispose());
        next.addActionListener(e -> onNext());

        loadJudges();
    }

    private void loadJudges() {
        judgeBox.removeAllItems();
        try {
            for (Map.Entry<Integer, Lawyer> e : court.getAllLawyers().entrySet()) {
                Lawyer l = e.getValue();
                if (l instanceof Judge) {
                    judgeBox.addItem(new JudgeItem((Judge) l));
                }
            }
        } catch (Throwable t) {
            JOptionPane.showMessageDialog(this, "Cannot load judges: " + t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onNext() {
        JudgeItem item = (JudgeItem) judgeBox.getSelectedItem();
        if (item == null || item.ref == null) {
            JOptionPane.showMessageDialog(this, "Select a judge.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        new AddVerdictFrame(court, item.ref).setVisible(true);
        dispose();
    }

    private static class JudgeItem {
        final Judge ref;
        final String label;

        JudgeItem(Judge j) {
            this.ref = j;
            this.label = j.getFirstName() + " " + j.getLastName() + " (ID " + j.getId() + ")";
        }

        @Override public String toString() { return label; }
    }
}

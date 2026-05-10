package view;

import javax.swing.*;
import java.awt.*;

import control.Court;
import model.Judge;

public class RemoveJudgeFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final Court courtSystem;
    private final JComboBox<Judge> judgeCombo;

    public RemoveJudgeFrame(Court courtSystem) {
        this.courtSystem = courtSystem;

        setTitle("Remove Judge");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 260);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        JLabel title = new JLabel("Remove Judge", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(25, 25, 112));
        title.setBounds(60, 20, 400, 30);
        add(title);

        JLabel lbl = createStyledLabel("Select Judge:");
        lbl.setBounds(60, 90, 120, 25);
        add(lbl);

        judgeCombo = new JComboBox<>();
        for (var l : courtSystem.getAllLawyers().values()) {
            if (l instanceof Judge j) {
                judgeCombo.addItem(j);
            }
        }
        judgeCombo.setBounds(180, 88, 280, 30);

        judgeCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                String txt;
                if (value instanceof Judge j) {
                    txt = j.getFirstName() + " " + j.getLastName();
                } else {
                    txt = String.valueOf(value);
                }
                return super.getListCellRendererComponent(list, txt, index, isSelected, cellHasFocus);
            }
        });

        add(judgeCombo);

        JButton removeBtn = createStyledButton("REMOVE", new Color(220, 53, 69));
        removeBtn.setBounds(320, 150, 140, 40);
        add(removeBtn);

        JButton cancelBtn = createStyledButton("CANCEL", new Color(108, 117, 125));
        cancelBtn.setBounds(180, 150, 120, 40);
        cancelBtn.addActionListener(e -> dispose());
        add(cancelBtn);

        if (judgeCombo.getItemCount() == 0) {
            removeBtn.setEnabled(false);
            JOptionPane.showMessageDialog(this, "No judges found.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }

        removeBtn.addActionListener(e -> onRemove());
    }

    private void onRemove() {
        Judge selected = (Judge) judgeCombo.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a judge.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            courtSystem.removeJudge(selected);
            JOptionPane.showMessageDialog(this, "Judge removed successfully.");

            judgeCombo.removeItem(selected);
            if (judgeCombo.getItemCount() == 0) {
                dispose();
            }

        } catch (exceptions.ObjectDoesNotExistException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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
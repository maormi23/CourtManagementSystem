package view;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import control.Court;

public class HowManyCasesBeforeFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final Court courtSystem;
    private final JTextField dateField;

    public HowManyCasesBeforeFrame(Court courtSystem) {
        this.courtSystem = courtSystem;

        setTitle("Cases Finished Before Date");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 220);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        JLabel title = new JLabel("Cases Finished Before Date", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(25, 25, 112));
        title.setBounds(60, 16, 400, 28);
        add(title);

        JLabel lbl = new JLabel("Date (yyyy-MM-dd):");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(60, 60, 60));
        lbl.setBounds(60, 70, 160, 24);
        add(lbl);

        dateField = new JTextField();
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        dateField.setBounds(220, 68, 240, 30);
        add(dateField);

        JButton calcBtn = createButton("CALCULATE", new Color(0, 123, 255));
        calcBtn.setBounds(320, 120, 140, 40);
        add(calcBtn);

        JButton cancelBtn = createButton("CANCEL", new Color(108, 117, 125));
        cancelBtn.setBounds(180, 120, 120, 40);
        cancelBtn.addActionListener(e -> dispose());
        add(cancelBtn);

        calcBtn.addActionListener(e -> calculate());
    }

    private void calculate() {
        String input = dateField.getText().trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a date.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            Date date = sdf.parse(input);
            int count = courtSystem.howManyCasesBefore(date);
            JOptionPane.showMessageDialog(this,
                    "Number of cases finished before " + input + ": " + count,
                    "Result", JOptionPane.INFORMATION_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
}
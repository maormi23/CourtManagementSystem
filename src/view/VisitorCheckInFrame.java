package view;

import control.Court;
import model.Department;
import model.Judge;
import model.Lawyer;

import javax.swing.*;
import java.awt.*;

public class VisitorCheckInFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final Court court;

    private JTextField nameField;
    private JRadioButton rbDepartment, rbJudge;
    private JComboBox<Object> targetCombo;
    private JButton btnCheckIn, btnBack;

    // layout
    private static final int CARD_X = 200, CARD_Y = 130, CARD_W = 420, CARD_H = 260;

    public VisitorCheckInFrame(Court court) {
        this.court = court;

        setTitle("Visitor Check-In");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // background
        JComponent root;
        try {
            JLabel bg = new JLabel(new ImageIcon(getClass().getResource("/background.jpg")));
            bg.setLayout(null);
            root = bg;
        } catch (Exception e) {
            JPanel bg = new JPanel(null);
            bg.setBackground(new Color(235, 240, 248));
            root = bg;
        }
        setContentPane(root);

        // card
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255,255,255,230));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),30,30);
                g2.setColor(new Color(200,200,200,150));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1,1,getWidth()-2,getHeight()-2,30,30);
            }
        };
        card.setOpaque(false);
        card.setLayout(null);
        card.setBounds(CARD_X, CARD_Y, CARD_W, CARD_H);
        root.add(card);

        // header
        JLabel title = new JLabel("Visitor Check-In", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(25,25,112));
        title.setBounds(50, 16, CARD_W - 100, 28);
        card.add(title);

        // name
        JLabel lblName = new JLabel("Your Name:");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblName.setBounds(32, 62, 100, 22);
        card.add(lblName);

        nameField = new JTextField();
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameField.setBounds(132, 60, 256, 28);
        card.add(nameField);

        // target type
        rbDepartment = new JRadioButton("Department", true);
        rbJudge = new JRadioButton("Judge");
        ButtonGroup group = new ButtonGroup();
        group.add(rbDepartment);
        group.add(rbJudge);
        rbDepartment.setOpaque(false);
        rbJudge.setOpaque(false);
        rbDepartment.setBounds(32, 98, 120, 24);
        rbJudge.setBounds(160, 98, 80, 24);
        card.add(rbDepartment);
        card.add(rbJudge);

        // target list
        targetCombo = new JComboBox<>();
        targetCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        targetCombo.setBounds(32, 128, 356, 30);
        card.add(targetCombo);

        // buttons
        btnCheckIn = makeButton("Check In", new Color(70,130,180), Color.WHITE);
        btnBack    = makeButton("Back",     new Color(220,20,60),  Color.WHITE);
        btnCheckIn.setBounds(232, 176, 100, 38);
        btnBack.setBounds(120, 176, 100, 38);
        card.add(btnCheckIn);
        card.add(btnBack);

        // data
        rbDepartment.addActionListener(e -> refreshTargets());
        rbJudge.addActionListener(e -> refreshTargets());
        refreshTargets(); // initial

        // actions
        btnCheckIn.addActionListener(e -> onCheckIn());
        btnBack.addActionListener(e -> dispose());
    }

    // fill combo based on selection
    private void refreshTargets() {
        targetCombo.removeAllItems();

        if (rbDepartment.isSelected()) {
            for (Department d : court.getAllDepartments().values()) {
                targetCombo.addItem(d);
            }
            targetCombo.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                              boolean isSelected, boolean cellHasFocus) {
                    String text = (value instanceof Department dep)
                            ? dep.getNumber() + " - " + dep.getName() + " (" + dep.getBuilding() + ")"
                            : String.valueOf(value);
                    return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
                }
            });
        } else {
            for (Lawyer lw : court.getAllLawyers().values()) {
                if (lw instanceof Judge j) {
                    targetCombo.addItem(j);
                }
            }
            targetCombo.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                              boolean isSelected, boolean cellHasFocus) {
                    String text = (value instanceof Judge j)
                            ? j.getFirstName() + " " + j.getLastName() + " (ID " + j.getId() + ")"
                            : String.valueOf(value);
                    return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
                }
            });
        }
    }

    // simple flow
    private void onCheckIn() {
        String name = nameField.getText().trim();
        Object target = targetCombo.getSelectedItem();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (target == null) {
            JOptionPane.showMessageDialog(this, "Please select a target.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String msg;
        if (target instanceof Department d) {
            msg = "Your check-in was sent to Department: " + d.getName() + ".";
        } else if (target instanceof Judge j) {
            msg = "Your check-in was sent to Judge: " + j.getFirstName() + " " + j.getLastName() + ".";
        } else {
            msg = "Your check-in was sent.";
        }

        JOptionPane.showMessageDialog(this,
                "Thank you, " + name + ".\n" + msg,
                "Checked In",
                JOptionPane.INFORMATION_MESSAGE);

        nameField.setText("");
        if (targetCombo.getItemCount() > 0) targetCombo.setSelectedIndex(0);
    }

    // small button factory
    private JButton makeButton(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        b.addChangeListener(e -> {
            ButtonModel m = b.getModel();
            b.setBackground(m.isRollover() ? adjust(bg, 1.08f) : bg);
        });
        return b;
    }

    // color helper
    private Color adjust(Color c, float factor) {
        int r = Math.min(255, Math.round(c.getRed() * factor));
        int g = Math.min(255, Math.round(c.getGreen() * factor));
        int b = Math.min(255, Math.round(c.getBlue() * factor));
        return new Color(r, g, b);
    }
}
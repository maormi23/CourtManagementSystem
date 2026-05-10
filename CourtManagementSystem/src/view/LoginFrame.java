package view;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.*;

import control.Court;
import model.Judge;
import model.Lawyer;

public class LoginFrame extends BaseMenu {
    private static final long serialVersionUID = 1L;

    private static final String ADMIN_ID = "123456789";

    private JTextField idField;
    private JButton loginButton;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignore) {}

        Court courtSystem;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("Court.ser"))) {
            courtSystem = (Court) in.readObject();
            System.out.println("Court loaded from Court.ser");
        } catch (Exception e) {
            System.out.println("Court.ser not found. Seeding from CourtDataGenerator...");
            courtSystem = utils.CourtDataGenerator.seed(); 

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Court.ser"))) {
                out.writeObject(courtSystem);
                System.out.println("Seeded Court.ser created.");
            } catch (Exception ex) {
                System.out.println("Failed to write Court.ser: " + ex.getMessage());
            }
        }

        Court finalCourtSystem = courtSystem;
        SwingUtilities.invokeLater(() -> new LoginFrame(finalCourtSystem).setVisible(true));
    }

    public LoginFrame(Court courtSystem) {
        super(courtSystem, "Login - Court Management System");

        // layout
        placeCard(200, 140, 400, 260);

        // title
        JLabel title = new JLabel("Court System", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(25, 25, 112));
        title.setBounds(50, 18, 300, 36);
        card.add(title);

        // portal
        JButton publicPortal = new JButton("Public Portal");
        publicPortal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        publicPortal.setBackground(new Color(235, 245, 252));
        publicPortal.setForeground(new Color(30, 90, 150));
        publicPortal.setFocusPainted(false);
        publicPortal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240)),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));
        publicPortal.setBounds(130, 54, 140, 30);
        publicPortal.addActionListener(e -> new VisitorMenu(courtSystem).setVisible(true));
        card.add(publicPortal);

        // id input
        JLabel lblId = new JLabel("Enter ID:");
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblId.setForeground(new Color(60, 60, 60));
        lblId.setBounds(50, 86, 160, 22);
        card.add(lblId);

        idField = new JTextField();
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        idField.setBounds(50, 110, 300, 32);
        card.add(idField);

        // login button
        Font btnFont = new Font("Segoe UI", Font.BOLD, 16);
        loginButton = makeButton("LOGIN", btnFont, new Color(70,130,180), Color.WHITE);
        loginButton.setBounds(150, 158, 100, 40);
        card.add(loginButton);

        wireActions();
    }

    // hide menu bar on this frame
    @Override
    protected boolean enableMenuBar() { return false; }

    private void wireActions() {
        loginButton.addActionListener(e -> {
            String inputId = idField.getText().trim();

            if (inputId.isEmpty()) {
                error("Please enter ID.");
                return;
            }

            if (ADMIN_ID.equals(inputId)) {
                openAdmin();
                return;
            }

            final Integer numericId;
            try {
                numericId = Integer.parseInt(inputId);
            } catch (NumberFormatException ex) {
                error("ID must be numeric for Judges/Lawyers.");
                return;
            }

            Judge judge = courtSystem.getRealJudge(numericId);
            Lawyer lawyer = courtSystem.getRealLawyer(numericId);

            if (judge == null && lawyer == null) {
                error("Login failed. No user found for this ID.");
                return;
            }

            if (judge != null && lawyer == null) {
                openJudge(judge);
                return;
            }
            if (lawyer != null && judge == null) {
                openLawyer(lawyer);
                return;
            }

            Object choice = JOptionPane.showInputDialog(
                    this,
                    "Multiple roles found for this ID. Choose role:",
                    "Select Role",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Judge", "Lawyer"},
                    "Judge"
            );
            if (choice == null) return;
            if ("Judge".equals(choice)) openJudge(judge);
            else openLawyer(lawyer);
        });
    }

    private void openAdmin() {
        new AdminMenu(courtSystem).setVisible(true);
        dispose();
    }

    private void openJudge(Judge judge) {
        new JudgeMenu(courtSystem, judge).setVisible(true);
        dispose();
    }

    private void openLawyer(Lawyer lawyer) {
        new LawyerMenu(courtSystem, lawyer).setVisible(true);
        dispose();
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
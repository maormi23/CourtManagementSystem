package view;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

import control.Court;
import enums.*;
import exceptions.ObjectAlreadyExistsException;
import exceptions.NegativeSalaryException;
import model.Judge;

public class AddJudgeFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    public AddJudgeFrame(Court courtSystem) {
        setTitle("Add New Judge");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        JLabel title = new JLabel("Add New Judge", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(25, 25, 112));
        title.setBounds(100, 20, 400, 30);
        add(title);

        JLabel idLabel = createStyledLabel("ID:");
        idLabel.setBounds(100, 80, 150, 25);
        add(idLabel);
        JTextField idText = createStyledTextField();
        idText.setBounds(270, 80, 200, 30);
        add(idText);

        JLabel firstNameLabel = createStyledLabel("First Name:");
        firstNameLabel.setBounds(100, 120, 150, 25);
        add(firstNameLabel);
        JTextField firstNameText = createStyledTextField();
        firstNameText.setBounds(270, 120, 200, 30);
        add(firstNameText);

        JLabel lastNameLabel = createStyledLabel("Last Name:");
        lastNameLabel.setBounds(100, 160, 150, 25);
        add(lastNameLabel);
        JTextField lastNameText = createStyledTextField();
        lastNameText.setBounds(270, 160, 200, 30);
        add(lastNameText);

        JLabel phoneLabel = createStyledLabel("Phone:");
        phoneLabel.setBounds(100, 200, 150, 25);
        add(phoneLabel);
        JTextField phoneText = createStyledTextField();
        phoneText.setBounds(270, 200, 200, 30);
        add(phoneText);

        JLabel emailLabel = createStyledLabel("Email:");
        emailLabel.setBounds(100, 240, 150, 25);
        add(emailLabel);
        JTextField emailText = createStyledTextField();
        emailText.setBounds(270, 240, 200, 30);
        add(emailText);

        JLabel specializationLabel = createStyledLabel("Specialization:");
        specializationLabel.setBounds(100, 280, 150, 25);
        add(specializationLabel);
        JComboBox<Specialization> specializationBox = new JComboBox<>(Specialization.values());
        specializationBox.setBounds(270, 280, 200, 30);
        add(specializationBox);

        JLabel licenseLabel = createStyledLabel("License Number:");
        licenseLabel.setBounds(100, 320, 150, 25);
        add(licenseLabel);
        JTextField licenseText = createStyledTextField();
        licenseText.setBounds(270, 320, 200, 30);
        add(licenseText);

        JLabel salaryLabel = createStyledLabel("Salary:");
        salaryLabel.setBounds(100, 360, 150, 25);
        add(salaryLabel);
        JTextField salaryText = createStyledTextField();
        salaryText.setBounds(270, 360, 200, 30);
        add(salaryText);

        JLabel expLabel = createStyledLabel("Years of Experience:");
        expLabel.setBounds(100, 400, 170, 25);
        add(expLabel);
        JTextField expText = createStyledTextField();
        expText.setBounds(270, 400, 200, 30);
        add(expText);

        JButton saveButton = new JButton("SAVE");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        saveButton.setBackground(new Color(40, 167, 69));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBounds(370, 460, 100, 40);
        add(saveButton);

        JButton cancelButton = new JButton("CANCEL");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cancelButton.setBackground(new Color(220, 53, 69));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBounds(130, 460, 100, 40);
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);

        saveButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idText.getText().trim());
                String firstName = firstNameText.getText().trim();
                String lastName = lastNameText.getText().trim();
                String phone = phoneText.getText().trim();
                String email = emailText.getText().trim();
                Specialization specialization = (Specialization) specializationBox.getSelectedItem();
                int license = Integer.parseInt(licenseText.getText().trim());
                double salary = Double.parseDouble(salaryText.getText().trim());
                int years = Integer.parseInt(expText.getText().trim());

                if (firstName.isEmpty() || lastName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "First/Last name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Judge newJudge = new Judge(
                        id, firstName, lastName, new Date(),
                        "N/A", phone, email, Gender.M,
                        specialization, license, salary, years
                );

                courtSystem.addJudge(newJudge);

                JOptionPane.showMessageDialog(this, "Judge added successfully!");
                dispose();

            } catch (NegativeSalaryException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID, License, Salary and Experience must be numeric.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ObjectAlreadyExistsException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(60, 60, 60));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }
}
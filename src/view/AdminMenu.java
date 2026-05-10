package view;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import control.Court;

public class AdminMenu extends BaseMenu {
    private static final long serialVersionUID = 1L;

    public AdminMenu(Court courtSystem) {
        super(courtSystem, "Admin Menu");

        // card height reduced to fit in 800x600 frame
        placeCard(170, 30, 480, 540);

        // title
        JLabel titleLabel = new JLabel("Admin Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(25, 25, 112));

        // buttons
        Font btnFont = new Font("Segoe UI", Font.BOLD, 16);
        Color primary = new Color(70, 130, 180);
        Color action  = new Color(46, 139, 87);
        Color text    = Color.WHITE;

        JButton btnAddJudge         = makeButton("Add Judge",             btnFont, primary, text);
        JButton btnAddLawyer        = makeButton("Add Lawyer",            btnFont, primary, text);
        JButton btnRemoveJudge      = makeButton("Remove Judge",          btnFont, primary, text);
        JButton btnRemoveLawyer     = makeButton("Remove Lawyer",         btnFont, primary, text);
        JButton btnAppointManager   = makeButton("Appoint A New Manager", btnFont, primary, text);
        JButton btnViewData         = makeButton("View Data",             btnFont, primary, text);

        JButton btnAddDepartment    = makeButton("Add Department",        btnFont, primary, text);
        JButton btnRemoveDepartment = makeButton("Remove Department",     btnFont, primary, text);

        // NEW: add joins to department
        JButton btnAddLawyerToDept  = makeButton("Add Lawyer to Department", btnFont, primary, text);
        JButton btnAddJudgeToDept   = makeButton("Add Judge to Department",  btnFont, primary, text);

        JButton btnAddCase          = makeButton("Add Case",              btnFont, primary, text);
        JButton btnRemoveCase       = makeButton("Remove Case",           btnFont, primary, text);

        JButton btnAddVerdict       = makeButton("Add Verdict",           btnFont, primary, text);
        JButton btnRemoveVerdict    = makeButton("Remove Verdict",        btnFont, primary, text);

        JButton btnSaveCourt        = makeButton("Save Court",            btnFont, action,  text);
        JButton btnLogout           = makeLogoutButton(btnFont);

        // vertical list panel (auto layout, scrollable)
        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // helper to unify button sizing/alignment
        java.util.function.Consumer<JButton> addBtn = b -> {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            Dimension d = new Dimension(300, 40);
            b.setMaximumSize(d);
            b.setPreferredSize(d);
            list.add(b);
            list.add(Box.createVerticalStrut(12));
        };

        // assemble
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        list.add(titleLabel);
        list.add(Box.createVerticalStrut(12));

        addBtn.accept(btnAddJudge);
        addBtn.accept(btnAddLawyer);
        addBtn.accept(btnRemoveJudge);
        addBtn.accept(btnRemoveLawyer);
        addBtn.accept(btnAppointManager);
        addBtn.accept(btnViewData);

        addBtn.accept(btnAddDepartment);
        addBtn.accept(btnRemoveDepartment);

        // NEW block for department membership
        addBtn.accept(btnAddLawyerToDept);
        addBtn.accept(btnAddJudgeToDept);

        addBtn.accept(btnAddCase);
        addBtn.accept(btnRemoveCase);

        addBtn.accept(btnAddVerdict);
        addBtn.accept(btnRemoveVerdict);

        addBtn.accept(btnSaveCourt);
        addBtn.accept(btnLogout);

        // scroll pane inside the card
        JScrollPane sp = new JScrollPane(list,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setBorder(null);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        sp.setBounds(0, 0, 480, 540);

        card.setLayout(null);
        card.add(sp);

        // actions
        btnAddJudge.addActionListener(e -> new AddJudgeFrame(courtSystem).setVisible(true));
        btnAddLawyer.addActionListener(e -> new AddLawyerFrame(courtSystem).setVisible(true));
        btnRemoveJudge.addActionListener(e -> new RemoveJudgeFrame(courtSystem).setVisible(true));
        btnRemoveLawyer.addActionListener(e -> new RemoveLawyerFrame(courtSystem).setVisible(true));
        btnAppointManager.addActionListener(e -> new AppointDeptManagerFrame(courtSystem).setVisible(true));
        btnViewData.addActionListener(e -> new ViewDataFrame(courtSystem).setVisible(true));

        btnAddDepartment.addActionListener(e -> new AddDepartmentFrame(courtSystem).setVisible(true));
        btnRemoveDepartment.addActionListener(e -> new RemoveDepartmentFrame(courtSystem).setVisible(true));

        // NEW actions
        btnAddLawyerToDept.addActionListener(e -> new AddLawyerToDepartmentFrame(courtSystem).setVisible(true));
        btnAddJudgeToDept.addActionListener(e -> new AddJudgeToDepartmentFrame(courtSystem).setVisible(true));

        btnAddCase.addActionListener(e -> new AddCaseFrame(courtSystem).setVisible(true));
        btnRemoveCase.addActionListener(e -> new RemoveCaseFrame(courtSystem).setVisible(true));
        btnAddVerdict.addActionListener(e -> new SelectJudgeForVerdictFrame(courtSystem).setVisible(true));
        btnRemoveVerdict.addActionListener(e -> new RemoveVerdictFrame(courtSystem).setVisible(true));

        btnSaveCourt.addActionListener(e -> saveCourt());
    }

    private void saveCourt() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Court.ser"))) {
            out.writeObject(courtSystem);
            JOptionPane.showMessageDialog(this, "Saved successfully to Court.ser");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Save failed: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void populateMainMenu(JMenu menu) {
        menu.add(mItem("Save Court", this::saveCourt));
    }

    @Override
    protected void populateQueriesMenu(JMenu q) {
        q.add(mItem("Appoint A New Manager",
                () -> new AppointDeptManagerFrame(courtSystem).setVisible(true)
        ));
    }

    @Override
    protected void populateInputMenu(JMenu inputMenu) {
        inputMenu.add(mItem("Add Judge", () -> new AddJudgeFrame(courtSystem).setVisible(true)));
        inputMenu.add(mItem("Add Lawyer", () -> new AddLawyerFrame(courtSystem).setVisible(true)));
        inputMenu.add(mItem("Remove Judge", () -> new RemoveJudgeFrame(courtSystem).setVisible(true)));
        inputMenu.add(mItem("Remove Lawyer", () -> new RemoveLawyerFrame(courtSystem).setVisible(true)));

        inputMenu.addSeparator();
        inputMenu.add(mItem("Add Department", () -> new AddDepartmentFrame(courtSystem).setVisible(true)));
        inputMenu.add(mItem("Remove Department", () -> new RemoveDepartmentFrame(courtSystem).setVisible(true)));

        // NEW items
        inputMenu.addSeparator();
        inputMenu.add(mItem("Add Lawyer to Department", () -> new AddLawyerToDepartmentFrame(courtSystem).setVisible(true)));
        inputMenu.add(mItem("Add Judge to Department",  () -> new AddJudgeToDepartmentFrame(courtSystem).setVisible(true)));

        inputMenu.addSeparator();
        inputMenu.add(mItem("Add Case", () -> new AddCaseFrame(courtSystem).setVisible(true)));
        inputMenu.add(mItem("Remove Case", () -> new RemoveCaseFrame(courtSystem).setVisible(true)));

        inputMenu.addSeparator();
        inputMenu.add(mItem("Add Verdict",
                () -> new SelectJudgeForVerdictFrame(courtSystem).setVisible(true)
        ));
        inputMenu.add(mItem("Remove Verdict", () -> new RemoveVerdictFrame(courtSystem).setVisible(true)));
    }

    @Override
    protected void populateViewMenu(JMenu viewMenu) {
        viewMenu.add(mItem("View Data", () -> new ViewDataFrame(courtSystem).setVisible(true)));
    }
}

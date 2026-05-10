package view;

import javax.swing.*;
import java.awt.*;

import control.Court;
import model.Judge;

public class JudgeMenu extends BaseMenu {
    private static final long serialVersionUID = 1L;

    private final Judge judge;

    public JudgeMenu(Court courtSystem, Judge judge) {
        super(courtSystem, "Judge Menu - " + judge.getFirstName() + " " + judge.getLastName());
        this.judge = judge;

        // layout
        placeCard(200, 70, 420, 520);

        // header
        JLabel title = new JLabel("Judge Menu", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(25, 25, 112));
        title.setBounds(60, 20, 300, 36);
        card.add(title);

        JLabel subtitle = new JLabel("Welcome, " + judge.getFirstName(), SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(80, 80, 95));
        subtitle.setBounds(60, 56, 300, 22);
        card.add(subtitle);

        // buttons
        Font btnFont = new Font("Segoe UI", Font.BOLD, 16);
        Color primary = new Color(70, 130, 180);
        Color text    = Color.WHITE;

        JButton btnMyCases    = makeButton("View/Manage My Cases",                 btnFont, primary, text);
        JButton btnAddVerdict = makeButton("Add Verdict",                btnFont, primary, text);
        JButton btnHowMany    = makeButton("Cases Finished Before Date",    btnFont, primary, text);
        JButton btnByDept     = makeButton("Finished Cases by Department",  btnFont, primary, text);
        JButton btnSpread     = makeButton("Diff between The Longest&Shortest Case",       btnFont, primary, text);
        JButton btnLogout     = makeLogoutButton(btnFont);

        int btnW = 280, btnH = 42;
        int left = (420 - btnW) / 2;
        int top  = 94;
        int gap  = 14;

        btnMyCases.setBounds(left, top, btnW, btnH);    top += btnH + gap;
        btnAddVerdict.setBounds(left, top, btnW, btnH); top += btnH + gap;
        btnHowMany.setBounds(left, top, btnW, btnH);    top += btnH + gap;
        btnByDept.setBounds(left, top, btnW, btnH);     top += btnH + gap;
        btnSpread.setBounds(left, top, btnW, btnH);     top += btnH + gap;
        btnLogout.setBounds(left, top, btnW, btnH);

        card.add(btnMyCases);
        card.add(btnAddVerdict);
        card.add(btnHowMany);
        card.add(btnByDept);
        card.add(btnSpread);
        card.add(btnLogout);

        // actions
        btnMyCases.addActionListener(e -> new CasesFrame(courtSystem, judge).setVisible(true));
        btnAddVerdict.addActionListener(e -> new AddVerdictFrame(courtSystem, judge).setVisible(true));
        btnHowMany.addActionListener(e -> new HowManyCasesBeforeFrame(courtSystem).setVisible(true));
        btnByDept.addActionListener(e -> new InactiveCasesByDepartmentFrame(courtSystem).setVisible(true));
        btnSpread.addActionListener(e -> showDiffbetweenTheLongestAndShortestCase());
    }

    // queries
    @Override
    protected void populateQueriesMenu(JMenu q) {
        q.add(mItem("Cases Finished Before Date",
                () -> new HowManyCasesBeforeFrame(courtSystem).setVisible(true)));
        q.add(mItem("Finished Cases by Department",
                () -> new InactiveCasesByDepartmentFrame(courtSystem).setVisible(true)));
        q.add(mItem("Diff between The Longest & Shortest Case", this::showDiffbetweenTheLongestAndShortestCase));
    }

    // data entry
    @Override
    protected void populateInputMenu(JMenu inputMenu) {
        inputMenu.add(mItem("Add Verdict",
                () -> new AddVerdictFrame(courtSystem, judge).setVisible(true)));
    }

    // data view
    @Override
    protected void populateViewMenu(JMenu viewMenu) {
        viewMenu.add(mItem("View/Manage My Cases",
                () -> new CasesFrame(courtSystem, judge).setVisible(true)));
    }

    // helper
    private void showDiffbetweenTheLongestAndShortestCase() {
        int diffDays = courtSystem.differnceBetweenTheLongestAndShortestCase(judge);
        String methodName = "differnceBetweenTheLongestAndShortestCase";

        if (diffDays == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "There are not enough closed cases to calculate the difference between the longest and the shortest case.",
                    "Result",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            String daysLabel = (diffDays == 1) ? "day" : "days";
            JOptionPane.showMessageDialog(
                    this,
                    "The difference between the longest and the shortest case is " 
                    + diffDays + " " + daysLabel + ".",
                    "Result",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

}
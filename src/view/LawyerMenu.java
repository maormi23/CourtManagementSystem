package view;

import javax.swing.*;
import java.awt.*;

import control.Court;
import model.Lawyer;

public class LawyerMenu extends BaseMenu {
    private static final long serialVersionUID = 1L;

    private final Lawyer lawyer; 

    public LawyerMenu(Court courtSystem, Lawyer lawyer) {
        super(courtSystem, "Lawyer Menu - " + lawyer.getFirstName() + " " + lawyer.getLastName());
        this.lawyer = lawyer;

        // layout
        placeCard(200, 120, 420, 380);

        // header
        JLabel title = new JLabel("Lawyer Menu", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(25, 25, 112));
        title.setBounds(60, 18, 300, 36);
        card.add(title);

        JLabel subtitle = new JLabel("Welcome, " + lawyer.getFirstName(), SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(80, 80, 95));
        subtitle.setBounds(60, 54, 300, 22);
        card.add(subtitle);

        // buttons
        Font btnFont = new Font("Segoe UI", Font.BOLD, 16);
        Color primary = new Color(70, 130, 180);
        Color text    = Color.WHITE;

        JButton btnMyCases  = makeButton("View Represented Cases",       btnFont, primary, text);
        JButton btnHowMany  = makeButton("Cases Finished Before Date",   btnFont, primary, text);
        JButton btnByDept   = makeButton("Finished Cases by Department", btnFont, primary, text);
        JButton btnLogout   = makeLogoutButton(btnFont);

        int btnW = 280, btnH = 42;
        int left = (420 - btnW) / 2;
        int top  = 92;
        int gap  = 16;

        btnMyCases.setBounds(left, top, btnW, btnH); top += btnH + gap;
        btnHowMany.setBounds(left, top, btnW, btnH); top += btnH + gap;
        btnByDept.setBounds(left,  top, btnW, btnH); top += btnH + gap;
        btnLogout.setBounds(left,  top, btnW, btnH);

        card.add(btnMyCases);
        card.add(btnHowMany);
        card.add(btnByDept);
        card.add(btnLogout);

        // actions
        btnMyCases.addActionListener(e -> new CasesFrame(courtSystem, lawyer).setVisible(true));
        btnHowMany.addActionListener(e -> new HowManyCasesBeforeFrame(courtSystem).setVisible(true));
        btnByDept.addActionListener(e -> new InactiveCasesByDepartmentFrame(courtSystem).setVisible(true));
    }

    // top menu: Queries
    @Override
    protected void populateQueriesMenu(JMenu q) {
        q.add(mItem("Cases Finished Before Date",
                () -> new HowManyCasesBeforeFrame(courtSystem).setVisible(true)));
        q.add(mItem("Finished Cases by Department",
                () -> new InactiveCasesByDepartmentFrame(courtSystem).setVisible(true)));
    }

    // top menu: Data View
    @Override
    protected void populateViewMenu(JMenu viewMenu) {
        viewMenu.add(mItem("View Represented Cases",
                () -> new CasesFrame(courtSystem, lawyer).setVisible(true)));
    }
}
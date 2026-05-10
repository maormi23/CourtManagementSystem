package view;

import javax.swing.*;
import java.awt.*;

import control.Court;

public class VisitorMenu extends BaseMenu {
    private static final long serialVersionUID = 1L;

    public VisitorMenu(Court courtSystem) {
        super(courtSystem, "Visitor Menu");

        // layout
        placeCard(200, 110, 400, 360);

        // header
        JLabel title = new JLabel("Visitor Menu", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(25, 25, 112));
        title.setBounds(50, 20, 300, 36);
        card.add(title);

        // buttons
        Font btnFont = new Font("Segoe UI", Font.BOLD, 16);
        Color primary = new Color(70, 130, 180);
        Color danger  = new Color(220, 20, 60);
        Color text    = Color.WHITE;

        JButton btnCheckIn        = makeButton("Visitor Check-In", btnFont, primary, text);
        JButton btnBrowseVerdicts = makeButton("Browse Verdicts",  btnFont, primary, text);
        JButton btnBack           = makeButton("Back to Login",    btnFont, danger,  text);

        int btnW = 260, btnH = 42;
        int left = (400 - btnW) / 2;
        int top  = 100;
        int gap  = 18;

        btnCheckIn.setBounds(left, top, btnW, btnH);             top += btnH + gap;
        btnBrowseVerdicts.setBounds(left, top, btnW, btnH);      top += btnH + gap;
        btnBack.setBounds(left, top, btnW, btnH);

        card.add(btnCheckIn);
        card.add(btnBrowseVerdicts);
        card.add(btnBack);

        // actions
        btnCheckIn.addActionListener(e -> new VisitorCheckInFrame(courtSystem).setVisible(true));
        btnBrowseVerdicts.addActionListener(e -> new VerdictsFrame(courtSystem).setVisible(true));
        btnBack.addActionListener(e -> {
            dispose();
            new LoginFrame(courtSystem).setVisible(true);
        });
    }

    // data entry menu
    @Override
    protected void populateInputMenu(JMenu inputMenu) {
        inputMenu.add(mItem("Visitor Check-In",
                () -> new VisitorCheckInFrame(courtSystem).setVisible(true)));
    }

    // data view menu
    @Override
    protected void populateViewMenu(JMenu viewMenu) {
        viewMenu.add(mItem("Browse Verdicts",
                () -> new VerdictsFrame(courtSystem).setVisible(true)));
    }
}
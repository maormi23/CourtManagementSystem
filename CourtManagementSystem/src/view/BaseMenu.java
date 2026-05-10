package view;

import control.Court;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class BaseMenu extends JFrame {
    private static final long serialVersionUID = 1L;

    protected final Court courtSystem;
    protected final JPanel card;

    protected BaseMenu(Court courtSystem, String title) {
        this.courtSystem = courtSystem;

        setTitle(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        if (enableMenuBar()) {
            installMenuBar(); // build top menus
        }

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

        card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 230));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2d.setColor(new Color(200, 200, 200, 150));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30);
            }
        };
        card.setOpaque(false);
        card.setLayout(null);
        card.setBounds(200, 110, 400, 420);
        ((JComponent) getContentPane()).add(card);
    }

    // layout helper
    protected void placeCard(int x, int y, int w, int h) {
        card.setBounds(x, y, w, h);
    }

    // button helpers
    protected JButton makeButton(String text, Font font, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setFont(font);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        b.addChangeListener(e -> {
            ButtonModel m = b.getModel();
            b.setBackground(m.isRollover() ? adjust(bg, 1.08f) : bg);
        });
        return b;
    }

    protected Color adjust(Color c, float factor) {
        int r = Math.min(255, Math.round(c.getRed() * factor));
        int g = Math.min(255, Math.round(c.getGreen() * factor));
        int b = Math.min(255, Math.round(c.getBlue() * factor));
        return new Color(r, g, b);
    }

    protected JButton makeExitButton(Font font) {
        JButton b = makeButton("Exit", font, new Color(105, 105, 105), Color.WHITE);
        b.addActionListener(e -> System.exit(0));
        return b;
    }

    protected JButton makeLogoutButton(Font font) {
        JButton b = makeButton("Logout", font, new Color(220, 20, 60), Color.WHITE);
        b.addActionListener(e -> {
            dispose();
            new LoginFrame(courtSystem).setVisible(true);
        });
        return b;
    }

    // hooks
    protected void populateMainMenu(JMenu menu) {}
    protected void populateQueriesMenu(JMenu queriesMenu) {}
    protected void populateInputMenu(JMenu inputMenu) {}
    protected void populateViewMenu(JMenu viewMenu) {}

    // visibility hook
    protected boolean enableMenuBar() { return true; }

    // small helper
    protected JMenuItem mItem(String title, Runnable action) {
        JMenuItem item = new JMenuItem(title);
        item.addActionListener(e -> action.run());
        return item;
    }

    // top menus
    private void installMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // "Menu"
        JMenu menu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
        logoutItem.addActionListener(e -> {
            dispose();
            new LoginFrame(courtSystem).setVisible(true);
        });
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        exitItem.addActionListener(e -> System.exit(0));
        menu.add(logoutItem);
        menu.add(exitItem);
        menu.addSeparator();
        populateMainMenu(menu);
        menuBar.add(menu); // always

        // "Queries" (only if has items)
        JMenu queries = new JMenu("Queries");
        populateQueriesMenu(queries);
        if (queries.getItemCount() > 0) {
            menuBar.add(queries);
        }

        // "Data Entry" (only if has items)
        JMenu input = new JMenu("Add/Remove");
        populateInputMenu(input);
        if (input.getItemCount() > 0) {
            menuBar.add(input);
        }

        // "Data View" (only if has items)
        JMenu view = new JMenu("View");
        populateViewMenu(view);
        if (view.getItemCount() > 0) {
            menuBar.add(view);
        }

        setJMenuBar(menuBar);
    }
}
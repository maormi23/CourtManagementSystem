package view;

import control.Court;
import enums.Specialization;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VerdictsFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final Court court;

    private final JTextField queryField = new JTextField();
    private final JComboBox<String> cbCategory = new JComboBox<>(new String[]{"All","CRIMINAL","OTHER"});
    private final JTable table = new JTable();
    private final JButton btnSearch = new JButton("Search");
    private final JButton btnDownload = new JButton("Download Selected");

    private final List<Verdict> shownVerdicts = new ArrayList<>();
    private final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    public VerdictsFrame(Court court) {
        this.court = court;

        setTitle("Browse Verdicts");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(980, 560);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        // Top controls
        JPanel top = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8,8,8,8);

        c.gridx=0; c.gridy=0; top.add(new JLabel("Keyword:"), c);
        c.gridx=1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx=1.0; top.add(queryField, c);
        c.gridx=2; c.weightx=0; c.fill = GridBagConstraints.NONE; top.add(new JLabel("Category:"), c);
        c.gridx=3; top.add(cbCategory, c);
        c.gridx=4; top.add(btnSearch, c);
        c.gridx=5; top.add(btnDownload, c);

        add(top, BorderLayout.NORTH);

        // Table
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnSearch.addActionListener(e -> runSearch());
        btnDownload.addActionListener(e -> downloadSelected());

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount()==2) openDetailsDialog();
            }
        });

        setEmpty();
    }

    private void setEmpty() {
        table.setModel(new DefaultTableModel(new String[]{"Result"},0));
        shownVerdicts.clear();
    }

    private void runSearch() {
        String q = queryField.getText().trim().toLowerCase(Locale.ROOT);
        String cat = (String) cbCategory.getSelectedItem();

        DefaultTableModel m = new DefaultTableModel(
                new String[]{"Case Code","Accused","Judge","Verdict Summary","Issued Date","Type"},0) {
            @Override public boolean isCellEditable(int r,int c){ return false; }
        };
        shownVerdicts.clear();

        for (model.Case cs : court.getAllCases().values()) {
            Verdict v = cs.getVerdict();
            if (v == null) continue;

            String summary = (v.getVerdictSummary()==null) ? "" : v.getVerdictSummary();
            String textLower = summary.toLowerCase(Locale.ROOT);
            Specialization sp = cs.getCaseType();

            boolean matchKeyword = q.isEmpty() || textLower.contains(q);
            boolean matchCat = "All".equals(cat) ||
                    ("CRIMINAL".equals(cat) && sp == Specialization.criminal) ||
                    ("OTHER".equals(cat) && sp != Specialization.criminal);

            if (matchKeyword && matchCat) {
                Accused a = cs.getAccused();
                String accused = (a==null) ? "" : a.getFirstName()+" "+a.getLastName()+" (ID "+a.getId()+")";

                Judge j = v.getJudge();
                String judge = (j==null) ? "" : j.getFirstName()+" "+j.getLastName()+" (ID "+j.getId()+")";

                m.addRow(new Object[]{
                        cs.getCode(),
                        accused,
                        judge,
                        summary,
                        v.getIssusedDate()==null ? "" : df.format(v.getIssusedDate()),
                        (sp==null? "" : sp.name())
                });
                shownVerdicts.add(v);
            }
        }

        table.setModel(m);
        if (m.getRowCount()==0) setEmpty();
        else table.setAutoCreateRowSorter(true);
    }

    private void openDetailsDialog() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        // convert if sorter active
        row = table.convertRowIndexToModel(row);
        Verdict v = shownVerdicts.get(row);

        StringBuilder sb = new StringBuilder();
        sb.append("Verdict ID: ").append(v.getVerdictID()).append('\n');
        sb.append("Issued: ").append(v.getIssusedDate()==null? "" : df.format(v.getIssusedDate())).append('\n');
        if (v.getJudge()!=null)
            sb.append("Judge: ").append(v.getJudge().getFirstName()).append(' ').append(v.getJudge().getLastName())
              .append(" (ID ").append(v.getJudge().getId()).append(")\n");
        sb.append("\nSummary:\n").append(v.getVerdictSummary()==null? "" : v.getVerdictSummary());

        JOptionPane.showMessageDialog(this, sb.toString(), "Judgment Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void downloadSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a judgment first.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        row = table.convertRowIndexToModel(row);
        Verdict v = shownVerdicts.get(row);

        JFileChooser ch = new JFileChooser();
        ch.setSelectedFile(new File("verdict_" + v.getVerdictID() + ".txt"));
        if (ch.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String txt = (v.getVerdictSummary()==null? "" : v.getVerdictSummary());
                Files.writeString(ch.getSelectedFile().toPath(), txt);
                JOptionPane.showMessageDialog(this, "Saved.", "Download", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to save file:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
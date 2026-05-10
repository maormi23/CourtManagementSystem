package view;

import control.Court;
import model.Department;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class InactiveCasesByDepartmentFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    public InactiveCasesByDepartmentFrame(Court courtSystem) {
        setTitle("Finished Cases Count by Department");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(620, 360);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Finished Cases Count by Department", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(title, BorderLayout.NORTH);

        String[] cols = {"Dept #", "Department Name", "Building", "Finished Cases"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        HashMap<Department, Integer> map = courtSystem.findInActiveCasesCountByDepartment();

     // Sort departments by their number before adding to table
     map.entrySet().stream()
         .sorted(Map.Entry.comparingByKey()) 
         .forEach(e -> {
             Department d = e.getKey();
             model.addRow(new Object[]{
                 d.getNumber(),
                 d.getName(), 
                 d.getBuilding(),
                 e.getValue()
             });
         });

        JTable table = new JTable(model);
        table.setRowHeight(22);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton close = new JButton("Close");
        close.addActionListener(e -> dispose());
        bottom.add(close);
        add(bottom, BorderLayout.SOUTH);
    }
}
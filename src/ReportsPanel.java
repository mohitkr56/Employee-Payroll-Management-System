
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class ReportsPanel extends JPanel {

    private final PayrollDAO payrollDAO = new PayrollDAO();

    private DefaultTableModel tableModel;
    private JTable reportTable;
    private TableRowSorter<DefaultTableModel> sorter;

    private JTextField searchField;
    private JLabel recordCountLabel;

    private final Color BACKGROUND = new Color(245, 247, 251);
    private final Color WHITE = Color.WHITE;
    private final Color BLUE = new Color(65, 105, 225);
    private final Color DARK = new Color(35, 45, 65);
    private final Color MUTED = new Color(115, 125, 145);
    private final Color BORDER = new Color(230, 234, 242);
    private final Color GREEN = new Color(20, 150, 110);

    public ReportsPanel() {

        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        add(createHeader(), BorderLayout.NORTH);
        add(createTable(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);

        loadReports();
    }

    // ================= HEADER =================

    private JPanel createHeader() {

        JPanel panel = new JPanel(new BorderLayout(15, 10));
        panel.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(
                new BoxLayout(titlePanel, BoxLayout.Y_AXIS)
        );
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Payroll Reports");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(DARK);

        JLabel subtitle = new JLabel(
                "View, search and export employee payroll reports"
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        // Search field

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setBackground(WHITE);
        searchPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER, 1, true),
                        new EmptyBorder(5, 10, 5, 10)
                )
        );

        JLabel searchIcon = new JLabel("Search");
        searchIcon.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );
        searchIcon.setForeground(MUTED);

        searchField = new JTextField(16);
        searchField.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );
        searchField.setBorder(null);
        searchField.setBackground(WHITE);
        searchField.setPreferredSize(
                new Dimension(180, 30)
        );

        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        searchField.getDocument().addDocumentListener(
                new DocumentListener() {

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        filterReports();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        filterReports();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        filterReports();
                    }
                }
        );

        panel.add(titlePanel, BorderLayout.WEST);
        panel.add(searchPanel, BorderLayout.EAST);

        return panel;
    }

    // ================= REPORT TABLE =================

    private JPanel createTable() {

        JPanel panel = new CardPanel();
        panel.setLayout(new BorderLayout(12, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setOpaque(false);

        JLabel heading = new JLabel("Payroll Records");
        heading.setFont(
                new Font("SansSerif", Font.BOLD, 18)
        );
        heading.setForeground(DARK);

        recordCountLabel = new JLabel("0 Records");
        recordCountLabel.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );
        recordCountLabel.setForeground(MUTED);

        tableHeader.add(heading, BorderLayout.WEST);
        tableHeader.add(recordCountLabel, BorderLayout.EAST);

        String[] columns = {
                "Payroll ID",
                "Employee ID",
                "Employee Name",
                "Department",
                "Month",
                "Gross Salary",
                "Deductions",
                "Net Salary"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reportTable = new JTable(tableModel);

        reportTable.setRowHeight(40);
        reportTable.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );

        reportTable.setForeground(DARK);
        reportTable.setBackground(WHITE);
        reportTable.setGridColor(BORDER);
        reportTable.setShowVerticalLines(false);
        reportTable.setShowHorizontalLines(true);
        reportTable.setFillsViewportHeight(true);

        reportTable.setSelectionBackground(
                new Color(232, 239, 255)
        );
        reportTable.setSelectionForeground(DARK);

        reportTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        // Table header design

        reportTable.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        reportTable.getTableHeader().setBackground(
                new Color(244, 247, 252)
        );

        reportTable.getTableHeader().setForeground(DARK);

        reportTable.getTableHeader().setPreferredSize(
                new Dimension(0, 42)
        );

        reportTable.getTableHeader().setReorderingAllowed(false);

        // Alternating row colors

        reportTable.setDefaultRenderer(
                Object.class,
                new DefaultTableCellRenderer() {

                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table,
                            Object value,
                            boolean isSelected,
                            boolean hasFocus,
                            int row,
                            int column) {

                        Component component =
                                super.getTableCellRendererComponent(
                                        table,
                                        value,
                                        isSelected,
                                        hasFocus,
                                        row,
                                        column
                                );

                        setBorder(
                                new EmptyBorder(0, 10, 0, 10)
                        );

                        if (!isSelected) {

                            component.setBackground(
                                    row % 2 == 0
                                            ? WHITE
                                            : new Color(249, 250, 253)
                            );

                            component.setForeground(DARK);
                        }

                        return component;
                    }
                }
        );

        // Sorting and filtering

        sorter = new TableRowSorter<>(tableModel);
        reportTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBorder(
                new LineBorder(BORDER, 1, true)
        );

        scrollPane.getViewport().setBackground(WHITE);

        panel.add(tableHeader, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ================= FOOTER =================

    private JPanel createFooter() {

        JPanel panel = new JPanel(
                new BorderLayout()
        );

        panel.setOpaque(false);

        JLabel footerText = new JLabel(
                "Payroll Management System"
        );

        footerText.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );

        footerText.setForeground(MUTED);

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );

        buttonPanel.setOpaque(false);

        JButton refreshButton = createButton(
                "Refresh Reports",
                new Color(90, 105, 135)
        );

        JButton exportButton = createButton(
                "Export CSV",
                GREEN
        );

        refreshButton.addActionListener(
                e -> loadReports()
        );

        exportButton.addActionListener(
                e -> exportCSV()
        );

        buttonPanel.add(refreshButton);
        buttonPanel.add(exportButton);

        panel.add(footerText, BorderLayout.WEST);
        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    // ================= LOAD REPORTS =================

    private void loadReports() {

        tableModel.setRowCount(0);

        try {

            List<Payroll> payrolls = payrollDAO.getAllPayrolls();

            for (Payroll p : payrolls) {

                tableModel.addRow(new Object[]{
                        p.getPayrollId(),
                        p.getEmpId(),
                        p.getEmployeeName(),
                        p.getDepartment(),
                        p.getPayrollMonth(),
                        p.getGrossSalary(),
                        p.getDeductions(),
                        p.getNetSalary()
                });
            }

            updateRecordCount();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load payroll reports.\n"
                            + e.getMessage(),
                    "Loading Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= SEARCH REPORTS =================

    private void filterReports() {

        String text = searchField.getText().trim();

        if (text.isEmpty()) {

            sorter.setRowFilter(null);

        } else {

            sorter.setRowFilter(
                    RowFilter.regexFilter(
                            "(?i)" + Pattern.quote(text)
                    )
            );
        }

        updateRecordCount();
    }

    // ================= RECORD COUNT =================

    private void updateRecordCount() {

        if (recordCountLabel == null || reportTable == null) {
            return;
        }

        int visibleRecords = reportTable.getRowCount();
        int totalRecords = tableModel.getRowCount();

        recordCountLabel.setText(
                visibleRecords + " of " + totalRecords + " Records"
        );
    }

    // ================= EXPORT CSV =================

    private void exportCSV() {

        if (reportTable.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "There are no payroll records to export.",
                    "No Records",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        JFileChooser chooser = new JFileChooser();

        chooser.setDialogTitle("Save Payroll Report");

        chooser.setSelectedFile(
                new File("Payroll_Report.csv")
        );

        int result = chooser.showSaveDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        if (!file.getName().toLowerCase().endsWith(".csv")) {

            file = new File(
                    file.getAbsolutePath() + ".csv"
            );
        }

        try (PrintWriter writer = new PrintWriter(
                new FileWriter(file, StandardCharsets.UTF_8))) {

            // CSV header

            for (int col = 0;
                 col < tableModel.getColumnCount();
                 col++) {

                if (col > 0) {
                    writer.print(",");
                }

                writer.print(
                        csvValue(tableModel.getColumnName(col))
                );
            }

            writer.println();

            // Export currently visible records

            for (int row = 0;
                 row < reportTable.getRowCount();
                 row++) {

                for (int col = 0;
                     col < reportTable.getColumnCount();
                     col++) {

                    if (col > 0) {
                        writer.print(",");
                    }

                    Object value = reportTable.getValueAt(row, col);

                    writer.print(
                            csvValue(
                                    value == null
                                            ? ""
                                            : value.toString()
                            )
                    );
                }

                writer.println();
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Payroll report exported successfully!\n\n"
                            + file.getAbsolutePath(),
                    "Export Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Export failed: " + e.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= CSV ESCAPING =================

    private String csvValue(String value) {

        return "\""
                + value.replace("\"", "\"\"")
                + "\"";
    }

    // ================= BUTTON DESIGN =================

    private JButton createButton(
            String title,
            Color color) {

        JButton button = new JButton(title);

        button.setBackground(color);
        button.setForeground(WHITE);

        button.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setBorder(
                new EmptyBorder(11, 16, 11, 16)
        );

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return button;
    }

    // ================= CARD DESIGN =================

    private class CardPanel extends JPanel {

        CardPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(WHITE);

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    18,
                    18
            );

            g2.setColor(BORDER);

            g2.drawRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    18,
                    18
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }
}

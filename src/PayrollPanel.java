
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class PayrollPanel extends JPanel {

    private final PayrollDAO payrollDAO = new PayrollDAO();

    private JTextField empIdField;
    private JTextField basicField;
    private JTextField hraField;
    private JTextField allowanceField;
    private JTextField deductionField;
    private JTextField monthField;

    private JLabel grossLabel;
    private JLabel netLabel;

    private DefaultTableModel tableModel;
    private JTable payrollTable;

    private final Color BACKGROUND = new Color(245, 247, 251);
    private final Color WHITE = Color.WHITE;
    private final Color BLUE = new Color(65, 105, 225);
    private final Color DARK = new Color(35, 45, 65);
    private final Color MUTED = new Color(115, 125, 145);
    private final Color BORDER = new Color(230, 234, 242);
    private final Color GREEN = new Color(20, 150, 110);

    public PayrollPanel() {

        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);

        loadPayrollRecords();
    }

    // ================= HEADER =================

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Payroll Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(DARK);

        JLabel subtitle = new JLabel(
                "Generate salaries and manage employee payroll records"
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        JButton refreshButton = createButton(
                "Refresh Records", BLUE
        );

        refreshButton.addActionListener(e -> loadPayrollRecords());

        header.add(titlePanel, BorderLayout.WEST);
        header.add(refreshButton, BorderLayout.EAST);

        return header;
    }

    // ================= MAIN CONTENT =================

    private JPanel createMainContent() {

        JPanel main = new JPanel(new BorderLayout(20, 20));
        main.setOpaque(false);

        // Make the payroll form scrollable
        JScrollPane formScrollPane = new JScrollPane(createPayrollForm());

        formScrollPane.setPreferredSize(new Dimension(360, 0));
        formScrollPane.setBorder(BorderFactory.createEmptyBorder());
        formScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        formScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        formScrollPane.getViewport().setOpaque(false);
        formScrollPane.setOpaque(false);
        formScrollPane.getVerticalScrollBar().setUnitIncrement(18);

        // Keep payroll history unchanged
        main.add(formScrollPane, BorderLayout.WEST);
        main.add(createHistoryTable(), BorderLayout.CENTER);

        return main;
    }
    // ================= PAYROLL FORM =================

    private JPanel createPayrollForm() {

        JPanel form = new CardPanel();
        form.setLayout(new GridBagLayout());
        form.setPreferredSize(new Dimension(340, 680));
        form.setBorder(new EmptyBorder(22, 22, 22, 22));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 12, 0);

        int row = 0;

        JLabel heading = new JLabel("Generate Payroll");
        heading.setFont(new Font("SansSerif", Font.BOLD, 19));
        heading.setForeground(DARK);

        gbc.gridy = row++;
        form.add(heading, gbc);

        JLabel description = new JLabel(
                "Enter employee and salary details"
        );
        description.setFont(new Font("SansSerif", Font.PLAIN, 12));
        description.setForeground(MUTED);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 20, 0);
        form.add(description, gbc);

        gbc.insets = new Insets(0, 0, 13, 0);

        empIdField = addField(
                form, gbc, "Employee ID", row++
        );

        basicField = addField(
                form, gbc, "Basic Salary (₹)", row++
        );

        hraField = addField(
                form, gbc, "HRA (₹)", row++
        );

        allowanceField = addField(
                form, gbc, "Allowances (₹)", row++
        );

        deductionField = addField(
                form, gbc, "Deductions (₹)", row++
        );

        monthField = addField(
                form, gbc, "Payroll Month", row++
        );

        // Salary summary

        JPanel summary = new JPanel(new GridLayout(2, 1, 5, 8));
        summary.setBackground(new Color(244, 247, 255));
        summary.setBorder(new EmptyBorder(13, 14, 13, 14));

        grossLabel = new JLabel("Gross Salary: ₹0.00");
        netLabel = new JLabel("Net Salary: ₹0.00");

        grossLabel.setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );
        grossLabel.setForeground(DARK);

        netLabel.setFont(
                new Font("SansSerif", Font.BOLD, 15)
        );
        netLabel.setForeground(GREEN);

        summary.add(grossLabel);
        summary.add(netLabel);

        gbc.gridy = row++;
        gbc.insets = new Insets(5, 0, 15, 0);
        form.add(summary, gbc);

        // Buttons

        JButton calculateButton = createButton(
                "Calculate Salary", GREEN
        );

        JButton generateButton = createButton(
                "Generate Payroll", BLUE
        );

        JButton clearButton = createButton(
                "Clear Form", new Color(105, 115, 135)
        );

        calculateButton.addActionListener(
                e -> calculateSalary()
        );

        generateButton.addActionListener(
                e -> generatePayroll()
        );

        clearButton.addActionListener(
                e -> clearForm()
        );

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 10, 0);
        form.add(calculateButton, gbc);

        gbc.gridy = row++;
        form.add(generateButton, gbc);

        gbc.gridy = row;
        gbc.insets = new Insets(0, 0, 0, 0);
        form.add(clearButton, gbc);

        return form;
    }

    // ================= FORM FIELDS =================

    private JTextField addField(
            JPanel panel,
            GridBagConstraints gbc,
            String label,
            int row) {

        gbc.gridy = row;

        JPanel fieldPanel = new JPanel(new BorderLayout(0, 6));
        fieldPanel.setOpaque(false);

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );
        fieldLabel.setForeground(DARK);

        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setForeground(DARK);
        field.setBackground(WHITE);
        field.setCaretColor(BLUE);
        field.setPreferredSize(new Dimension(200, 36));
        field.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER, 1, true),
                        new EmptyBorder(5, 10, 5, 10)
                )
        );

        fieldPanel.add(fieldLabel, BorderLayout.NORTH);
        fieldPanel.add(field, BorderLayout.CENTER);

        panel.add(fieldPanel, gbc);

        return field;
    }

    // ================= PAYROLL HISTORY =================

    private JPanel createHistoryTable() {

        JPanel panel = new CardPanel();
        panel.setLayout(new BorderLayout(12, 18));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel headingPanel = new JPanel(new BorderLayout());
        headingPanel.setOpaque(false);

        JPanel headingText = new JPanel();
        headingText.setLayout(
                new BoxLayout(headingText, BoxLayout.Y_AXIS)
        );
        headingText.setOpaque(false);

        JLabel heading = new JLabel("Payroll History");
        heading.setFont(
                new Font("SansSerif", Font.BOLD, 19)
        );
        heading.setForeground(DARK);

        JLabel subtitle = new JLabel(
                "View generated employee payroll records"
        );
        subtitle.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );
        subtitle.setForeground(MUTED);

        headingText.add(heading);
        headingText.add(Box.createVerticalStrut(5));
        headingText.add(subtitle);

        headingPanel.add(headingText, BorderLayout.WEST);

        String[] columns = {
                "Payroll ID",
                "Employee ID",
                "Employee Name",
                "Department",
                "Month",
                "Gross Salary",
                "Net Salary"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        payrollTable = new JTable(tableModel);

        payrollTable.setRowHeight(38);
        payrollTable.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );

        payrollTable.setForeground(DARK);
        payrollTable.setBackground(WHITE);
        payrollTable.setGridColor(BORDER);
        payrollTable.setShowVerticalLines(false);
        payrollTable.setSelectionBackground(
                new Color(232, 239, 255)
        );
        payrollTable.setSelectionForeground(DARK);
        payrollTable.setFillsViewportHeight(true);
        payrollTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        payrollTable.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        payrollTable.getTableHeader().setBackground(
                new Color(244, 247, 252)
        );

        payrollTable.getTableHeader().setForeground(DARK);
        payrollTable.getTableHeader().setPreferredSize(
                new Dimension(0, 40)
        );
        payrollTable.getTableHeader().setReorderingAllowed(false);

        payrollTable.setDefaultRenderer(
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
                                        table, value, isSelected,
                                        hasFocus, row, column
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

        JScrollPane scrollPane = new JScrollPane(payrollTable);
        scrollPane.setBorder(
                new LineBorder(BORDER, 1, true)
        );
        scrollPane.getViewport().setBackground(WHITE);

        JButton refreshButton = createButton(
                "Refresh History", new Color(90, 105, 135)
        );

        refreshButton.addActionListener(
                e -> loadPayrollRecords()
        );

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(refreshButton);

        panel.add(headingPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ================= CALCULATE SALARY =================

    private void calculateSalary() {

        try {

            double basic = Double.parseDouble(
                    basicField.getText().trim()
            );

            double hra = Double.parseDouble(
                    hraField.getText().trim()
            );

            double allowances = Double.parseDouble(
                    allowanceField.getText().trim()
            );

            double deductions = Double.parseDouble(
                    deductionField.getText().trim()
            );

            double gross = basic + hra + allowances;
            double net = gross - deductions;

            if (basic < 0 || hra < 0 || allowances < 0
                    || deductions < 0 || deductions > gross) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter valid salary values.",
                        "Invalid Salary",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            grossLabel.setText(
                    String.format("Gross Salary: ₹%.2f", gross)
            );

            netLabel.setText(
                    String.format("Net Salary: ₹%.2f", net)
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numeric salary values.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= GENERATE PAYROLL =================

    private void generatePayroll() {

        try {

            int empId = Integer.parseInt(
                    empIdField.getText().trim()
            );

            double basic = Double.parseDouble(
                    basicField.getText().trim()
            );

            double hra = Double.parseDouble(
                    hraField.getText().trim()
            );

            double allowances = Double.parseDouble(
                    allowanceField.getText().trim()
            );

            double deductions = Double.parseDouble(
                    deductionField.getText().trim()
            );

            String month = monthField.getText()
                    .trim().toUpperCase();

            double gross = basic + hra + allowances;

            // Validate payroll details

            if (empId <= 0 || basic < 0 || hra < 0
                    || allowances < 0 || deductions < 0
                    || deductions > gross || month.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter valid payroll details.",
                        "Invalid Details",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            // Duplicate payroll validation

            if (payrollDAO.payrollExists(empId, month)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Payroll already exists for Employee ID "
                                + empId + " for " + month + ".\n\n"
                                + "Duplicate payroll cannot be generated.",
                        "Duplicate Payroll",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            // Create payroll object

            Payroll payroll = new Payroll(
                    empId,
                    basic,
                    hra,
                    allowances,
                    deductions,
                    month
            );

            // Save payroll

            if (payrollDAO.generatePayroll(payroll)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Payroll generated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                clearForm();
                loadPayrollRecords();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Payroll generation failed.\n"
                                + "Please check the Employee ID "
                                + "and database connection.",
                        "Payroll Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numeric values.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= LOAD PAYROLL RECORDS =================

    private void loadPayrollRecords() {

        tableModel.setRowCount(0);

        List<Payroll> payrolls = payrollDAO.getAllPayrolls();

        for (Payroll p : payrolls) {

            tableModel.addRow(new Object[]{
                    p.getPayrollId(),
                    p.getEmpId(),
                    p.getEmployeeName(),
                    p.getDepartment(),
                    p.getPayrollMonth(),
                    p.getGrossSalary(),
                    p.getNetSalary()
            });
        }
    }

    // ================= CLEAR FORM =================

    private void clearForm() {

        empIdField.setText("");
        basicField.setText("");
        hraField.setText("");
        allowanceField.setText("");
        deductionField.setText("");
        monthField.setText("");

        grossLabel.setText("Gross Salary: ₹0.00");
        netLabel.setText("Net Salary: ₹0.00");
    }

    // ================= BUTTON DESIGN =================

    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);

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

    // ================= CARD PANEL =================

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
                    0, 0,
                    getWidth() - 1,
                    getHeight() - 1,
                    18, 18
            );

            g2.setColor(BORDER);

            g2.drawRoundRect(
                    0, 0,
                    getWidth() - 1,
                    getHeight() - 1,
                    18, 18
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }
}


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

public class EmployeePanel extends JPanel {

    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;
    private JLabel recordCountLabel;

    private final Color BACKGROUND = new Color(245, 247, 251);
    private final Color WHITE = Color.WHITE;
    private final Color BLUE = new Color(65, 105, 225);
    private final Color DARK = new Color(35, 45, 65);
    private final Color MUTED = new Color(115, 125, 145);
    private final Color BORDER = new Color(230, 234, 242);
    private final Color GREEN = new Color(20, 150, 110);
    private final Color RED = new Color(210, 65, 65);

    public EmployeePanel() {

        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        add(createHeader(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        loadEmployees();
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

        JLabel title = new JLabel("Employee Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(DARK);

        JLabel subtitle = new JLabel(
                "Manage employee profiles and organizational details"
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

        JLabel searchLabel = new JLabel("Search");
        searchLabel.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );
        searchLabel.setForeground(MUTED);

        searchField = new JTextField(17);
        searchField.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );
        searchField.setBorder(null);
        searchField.setBackground(WHITE);
        searchField.setPreferredSize(
                new Dimension(170, 30)
        );

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        searchField.getDocument().addDocumentListener(
                new DocumentListener() {

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        filterEmployees();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        filterEmployees();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        filterEmployees();
                    }
                }
        );

        panel.add(titlePanel, BorderLayout.WEST);
        panel.add(searchPanel, BorderLayout.EAST);

        return panel;
    }

    // ================= EMPLOYEE TABLE =================

    private JPanel createTablePanel() {

        JPanel panel = new CardPanel();

        panel.setLayout(new BorderLayout(12, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Table heading

        JPanel headingPanel = new JPanel(new BorderLayout());
        headingPanel.setOpaque(false);

        JLabel heading = new JLabel("Employee Directory");
        heading.setFont(
                new Font("SansSerif", Font.BOLD, 18)
        );
        heading.setForeground(DARK);

        recordCountLabel = new JLabel("0 Employees");
        recordCountLabel.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );
        recordCountLabel.setForeground(MUTED);

        headingPanel.add(heading, BorderLayout.WEST);
        headingPanel.add(recordCountLabel, BorderLayout.EAST);

        // Table columns

        String[] columns = {
                "Employee ID",
                "Employee Name",
                "Email",
                "Department",
                "Designation"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeeTable = new JTable(tableModel);

        employeeTable.setRowHeight(40);
        employeeTable.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );

        employeeTable.setForeground(DARK);
        employeeTable.setBackground(WHITE);
        employeeTable.setGridColor(BORDER);

        employeeTable.setShowVerticalLines(false);
        employeeTable.setShowHorizontalLines(true);
        employeeTable.setFillsViewportHeight(true);

        employeeTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        employeeTable.setSelectionBackground(
                new Color(232, 239, 255)
        );

        employeeTable.setSelectionForeground(DARK);

        employeeTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        // Table header

        employeeTable.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        employeeTable.getTableHeader().setBackground(
                new Color(244, 247, 252)
        );

        employeeTable.getTableHeader().setForeground(DARK);

        employeeTable.getTableHeader().setPreferredSize(
                new Dimension(0, 42)
        );

        employeeTable.getTableHeader().setReorderingAllowed(false);

        // Alternating row colors

        employeeTable.setDefaultRenderer(
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
        employeeTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(employeeTable);

        scrollPane.setBorder(
                new LineBorder(BORDER, 1, true)
        );

        scrollPane.getViewport().setBackground(WHITE);

        panel.add(headingPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ================= ACTION BUTTONS =================

    private JPanel createButtonPanel() {

        JPanel panel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 0)
        );

        panel.setOpaque(false);

        JButton addButton = createButton(
                "Add Employee",
                BLUE
        );

        JButton updateButton = createButton(
                "Update Employee",
                GREEN
        );

        JButton deleteButton = createButton(
                "Delete Employee",
                RED
        );

        JButton refreshButton = createButton(
                "Refresh",
                new Color(90, 105, 135)
        );

        addButton.addActionListener(
                e -> addEmployee()
        );

        updateButton.addActionListener(
                e -> updateEmployee()
        );

        deleteButton.addActionListener(
                e -> deleteEmployee()
        );

        refreshButton.addActionListener(
                e -> loadEmployees()
        );

        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(refreshButton);

        return panel;
    }

    // ================= LOAD EMPLOYEES =================

    private void loadEmployees() {

        tableModel.setRowCount(0);

        try {

            List<Employee> employees =
                    employeeDAO.getAllEmployees();

            for (Employee emp : employees) {

                tableModel.addRow(new Object[]{
                        emp.getEmpId(),
                        emp.getEmpName(),
                        emp.getEmail(),
                        emp.getDepartment(),
                        emp.getDesignation()
                });
            }

            updateRecordCount();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load employee records.\n"
                            + e.getMessage(),
                    "Loading Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= SEARCH EMPLOYEES =================

    private void filterEmployees() {

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

        if (recordCountLabel == null || employeeTable == null) {
            return;
        }

        int visible = employeeTable.getRowCount();
        int total = tableModel.getRowCount();

        recordCountLabel.setText(
                visible + " of " + total + " Employees"
        );
    }

    // ================= ADD EMPLOYEE =================

    private void addEmployee() {

        Employee emp = showEmployeeDialog(null);

        if (emp == null) {
            return;
        }

        if (employeeDAO.addEmployee(emp)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Employee added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadEmployees();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add employee.",
                    "Operation Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= UPDATE EMPLOYEE =================

    private void updateEmployee() {

        int selectedRow = employeeTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an employee first.",
                    "No Employee Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                employeeTable.convertRowIndexToModel(selectedRow);

        int empId = Integer.parseInt(
                tableModel.getValueAt(modelRow, 0).toString()
        );

        Employee emp = new Employee();

        emp.setEmpId(empId);

        emp.setEmpName(
                tableModel.getValueAt(modelRow, 1).toString()
        );

        emp.setEmail(
                tableModel.getValueAt(modelRow, 2).toString()
        );

        emp.setDepartment(
                tableModel.getValueAt(modelRow, 3).toString()
        );

        emp.setDesignation(
                tableModel.getValueAt(modelRow, 4).toString()
        );

        Employee updatedEmp = showEmployeeDialog(emp);

        if (updatedEmp == null) {
            return;
        }

        if (employeeDAO.updateEmployee(updatedEmp)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Employee updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadEmployees();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Employee update failed.",
                    "Operation Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= DELETE EMPLOYEE =================

    private void deleteEmployee() {

        int selectedRow = employeeTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an employee first.",
                    "No Employee Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                employeeTable.convertRowIndexToModel(selectedRow);

        int empId = Integer.parseInt(
                tableModel.getValueAt(modelRow, 0).toString()
        );

        String employeeName =
                tableModel.getValueAt(modelRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete "
                        + employeeName + "?\n\n"
                        + "Employee ID: " + empId,
                "Confirm Employee Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {

            if (employeeDAO.deleteEmployee(empId)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Employee deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadEmployees();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Deletion failed. Check whether payroll "
                                + "records exist for this employee.",
                        "Deletion Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    // ================= EMPLOYEE FORM DIALOG =================

    private Employee showEmployeeDialog(Employee existingEmployee) {

        JTextField nameField = createFormField();
        JTextField emailField = createFormField();
        JTextField departmentField = createFormField();
        JTextField designationField = createFormField();

        if (existingEmployee != null) {

            nameField.setText(existingEmployee.getEmpName());
            emailField.setText(existingEmployee.getEmail());
            departmentField.setText(existingEmployee.getDepartment());
            designationField.setText(existingEmployee.getDesignation());
        }

        JPanel form = new JPanel(new GridBagLayout());

        form.setBackground(WHITE);
        form.setBorder(new EmptyBorder(18, 20, 18, 20));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(7, 0, 7, 0);

        addDialogField(
                form, gbc, "Employee Name", nameField, 0
        );

        addDialogField(
                form, gbc, "Email Address", emailField, 1
        );

        addDialogField(
                form, gbc, "Department", departmentField, 2
        );

        addDialogField(
                form, gbc, "Designation", designationField, 3
        );

        String title = existingEmployee == null
                ? "Add New Employee"
                : "Update Employee";

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                title,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String department = departmentField.getText().trim();
        String designation = designationField.getText().trim();

        // Required field validation

        if (name.isEmpty() || department.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Employee name and department are required.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        Employee emp = new Employee();

        if (existingEmployee != null) {
            emp.setEmpId(existingEmployee.getEmpId());
        }

        emp.setEmpName(name);
        emp.setEmail(email);
        emp.setDepartment(department);
        emp.setDesignation(designation);

        return emp;
    }

    // ================= DIALOG FIELD =================

    private void addDialogField(
            JPanel panel,
            GridBagConstraints gbc,
            String label,
            JTextField field,
            int row) {

        JPanel fieldPanel = new JPanel(new BorderLayout(0, 6));
        fieldPanel.setOpaque(false);

        JLabel fieldLabel = new JLabel(label);

        fieldLabel.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        fieldLabel.setForeground(DARK);

        fieldPanel.add(fieldLabel, BorderLayout.NORTH);
        fieldPanel.add(field, BorderLayout.CENTER);

        gbc.gridy = row;

        panel.add(fieldPanel, gbc);
    }

    private JTextField createFormField() {

        JTextField field = new JTextField(22);

        field.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );

        field.setForeground(DARK);
        field.setBackground(WHITE);

        field.setCaretColor(BLUE);

        field.setPreferredSize(
                new Dimension(260, 36)
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER, 1, true),
                        new EmptyBorder(5, 10, 5, 10)
                )
        );

        return field;
    }

    // ================= BUTTON DESIGN =================

    private JButton createButton(
            String text,
            Color color) {

        JButton button = new JButton(text);

        button.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        button.setForeground(WHITE);
        button.setBackground(color);

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

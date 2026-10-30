import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainFrame extends JFrame {

    private static final Color BLUE = new Color(37, 99, 235);
    private static final Color BLUE_LIGHT = new Color(239, 246, 255);
    private static final Color BACKGROUND = new Color(244, 246, 248);
    private static final Color TEXT = new Color(30, 41, 59);
    private static final Color SECONDARY = new Color(100, 116, 139);
    private static final Color BORDER = new Color(226, 232, 240);
    private static final Color WHITE = Color.WHITE;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainContent = new JPanel(cardLayout);

    private final DashboardDAO dashboardDAO = new DashboardDAO();

    private JButton activeButton;

    private JLabel totalPayrollValue;
    private JLabel employeesValue;
    private JLabel averageSalaryValue;

    private PayrollTrendChart trendChart;
    private DepartmentChart departmentChart;

    private DefaultTableModel recentTableModel;

    private final NumberFormat currency =
            NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

    public MainFrame() {

        setTitle("Employee Payroll Management System | Anand Kumar Saha");
        setSize(1450, 900);
        setMinimumSize(new Dimension(1150, 750));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(BACKGROUND);
        mainContent.setBackground(BACKGROUND);

        mainContent.add(createDashboard(), "Dashboard");
        mainContent.add(new EmployeePanel(), "Employees");
        mainContent.add(new PayrollPanel(), "Payroll");
        mainContent.add(new ReportsPanel(), "Reports");

        add(createSidebar(), BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);

        cardLayout.show(mainContent, "Dashboard");
        loadDashboardData();
    }

    // =====================================================
    // SIDEBAR
    // =====================================================

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBackground(WHITE);
        sidebar.setBorder(BorderFactory.createMatteBorder(
                0, 0, 0, 1, BORDER
        ));

        JPanel menu = new JPanel();
        menu.setBackground(WHITE);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(26, 14, 18, 14));

        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        brand.setOpaque(false);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel brandIcon = new JLabel("●");
        brandIcon.setForeground(BLUE);
        brandIcon.setFont(new Font("SansSerif", Font.BOLD, 25));

        JLabel brandName = new JLabel("Payroll Manager");
        brandName.setForeground(TEXT);
        brandName.setFont(new Font("SansSerif", Font.BOLD, 18));

        brand.add(brandIcon);
        brand.add(brandName);

        menu.add(brand);
        menu.add(Box.createVerticalStrut(42));

        JLabel menuTitle = new JLabel("MAIN MENU");
        menuTitle.setFont(new Font("SansSerif", Font.BOLD, 10));
        menuTitle.setForeground(SECONDARY);
        menuTitle.setBorder(new EmptyBorder(0, 10, 14, 0));
        menuTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        menu.add(menuTitle);

        JButton dashboard = createNavButton("▦", "Dashboard", "Dashboard");
        JButton employees = createNavButton("♙", "Employees", "Employees");
        JButton payroll = createNavButton("₹", "Payroll", "Payroll");
        JButton reports = createNavButton("▤", "Reports", "Reports");

        menu.add(dashboard);
        menu.add(Box.createVerticalStrut(8));
        menu.add(employees);
        menu.add(Box.createVerticalStrut(8));
        menu.add(payroll);
        menu.add(Box.createVerticalStrut(8));
        menu.add(reports);

        sidebar.add(menu, BorderLayout.NORTH);

        JPanel footer = new JPanel();
        footer.setBackground(new Color(248, 250, 252));
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBorder(new EmptyBorder(18, 18, 22, 12));

        JLabel footerTitle = new JLabel("DESIGNED & DEVELOPED BY");
        footerTitle.setFont(new Font("SansSerif", Font.BOLD, 9));
        footerTitle.setForeground(SECONDARY);

        JLabel developer = new JLabel("Anand Kumar Saha");
        developer.setFont(new Font("SansSerif", Font.BOLD, 14));
        developer.setForeground(TEXT);

        JLabel technology = new JLabel("Java • Swing • Oracle DB");
        technology.setFont(new Font("SansSerif", Font.PLAIN, 10));
        technology.setForeground(SECONDARY);

        footer.add(footerTitle);
        footer.add(Box.createVerticalStrut(8));
        footer.add(developer);
        footer.add(Box.createVerticalStrut(5));
        footer.add(technology);

        sidebar.add(footer, BorderLayout.SOUTH);

        setActiveButton(dashboard);

        return sidebar;
    }

    private JButton createNavButton(
            String icon,
            String title,
            String page) {

        JButton button = new JButton("  " + icon + "     " + title);

        button.setPreferredSize(new Dimension(198, 42));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.setFont(new Font("SansSerif", Font.PLAIN, 13));
        button.setForeground(SECONDARY);
        button.setBackground(WHITE);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(10, 12, 10, 8));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> {
            cardLayout.show(mainContent, page);
            setActiveButton(button);

            if ("Dashboard".equals(page)) {
                loadDashboardData();
            }
        });

        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != activeButton) {
                    button.setBackground(BLUE_LIGHT);
                    button.setForeground(BLUE);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button != activeButton) {
                    button.setBackground(WHITE);
                    button.setForeground(SECONDARY);
                }
            }
        });

        return button;
    }

    private void setActiveButton(JButton button) {

        if (activeButton != null) {
            activeButton.setBackground(WHITE);
            activeButton.setForeground(SECONDARY);
        }

        activeButton = button;
        activeButton.setBackground(BLUE_LIGHT);
        activeButton.setForeground(BLUE);
    }

    // =====================================================
    // DASHBOARD
    // =====================================================

    private JPanel createDashboard() {

        JPanel dashboard = new JPanel(new BorderLayout(0, 20));
        dashboard.setBackground(BACKGROUND);
        dashboard.setBorder(new EmptyBorder(24, 30, 24, 30));

        dashboard.add(createTopBar(), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(createPageHeading());
        content.add(Box.createVerticalStrut(18));

        content.add(createKpiSection());
        content.add(Box.createVerticalStrut(20));

        content.add(createChartsSection());
        content.add(Box.createVerticalStrut(20));

        content.add(createRecentPayrollSection());

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        dashboard.add(scrollPane, BorderLayout.CENTER);

        return dashboard;
    }

    // =====================================================
    // TOP TOOLBAR
    // =====================================================

    private JPanel createTopBar() {

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setPreferredSize(new Dimension(900, 42));

        JLabel section = new JLabel("Overview");
        section.setFont(new Font("SansSerif", Font.BOLD, 13));
        section.setForeground(BLUE);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);

        JButton refresh = new JButton("↻ Refresh");
        refresh.setFont(new Font("SansSerif", Font.BOLD, 12));
        refresh.setForeground(BLUE);
        refresh.setBackground(WHITE);
        refresh.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(8, 13, 8, 13)
        ));

        refresh.setFocusPainted(false);
        refresh.setCursor(new Cursor(Cursor.HAND_CURSOR));

        refresh.addActionListener(e -> loadDashboardData());

        JLabel profile = new JLabel("  Anand Kumar Saha  ");
        profile.setFont(new Font("SansSerif", Font.BOLD, 12));
        profile.setForeground(TEXT);

        right.add(refresh);
        right.add(profile);

        topBar.add(section, BorderLayout.WEST);
        topBar.add(right, BorderLayout.EAST);

        return topBar;
    }

    private JPanel createPageHeading() {

        JPanel heading = new JPanel(new BorderLayout());
        heading.setOpaque(false);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Payroll Overview");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "Monitor employee payroll and salary performance."
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(SECONDARY);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        left.add(title);
        left.add(Box.createVerticalStrut(5));
        left.add(subtitle);

        JLabel date = new JLabel("EMPLOYEE PAYROLL MANAGEMENT");
        date.setFont(new Font("SansSerif", Font.BOLD, 10));
        date.setForeground(SECONDARY);

        heading.add(left, BorderLayout.WEST);
        heading.add(date, BorderLayout.EAST);

        return heading;
    }

    // =====================================================
    // KPI CARDS
    // =====================================================

    private JPanel createKpiSection() {

        JPanel section = new JPanel(new GridLayout(1, 3, 16, 0));
        section.setOpaque(false);
        section.setPreferredSize(new Dimension(900, 125));
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 125));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        totalPayrollValue = new JLabel("₹0.00");
        employeesValue = new JLabel("0");
        averageSalaryValue = new JLabel("₹0.00");

        section.add(createKpiCard(
                "Total Net Payroll",
                totalPayrollValue,
                "Total payable salary",
                "₹",
                BLUE
        ));

        section.add(createKpiCard(
                "Total Employees",
                employeesValue,
                "Registered employees",
                "♙",
                new Color(59, 130, 246)
        ));

        section.add(createKpiCard(
                "Average Salary",
                averageSalaryValue,
                "Average per payroll record",
                "↗",
                new Color(148, 163, 184)
        ));

        return section;
    }

    private JPanel createKpiCard(
            String title,
            JLabel value,
            String description,
            String icon,
            Color accent) {

        RoundedPanel card = new RoundedPanel(12, WHITE, BORDER);
        card.setLayout(new BorderLayout(8, 8));
        card.setBorder(new EmptyBorder(16, 18, 14, 18));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        titleLabel.setForeground(SECONDARY);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, 19));
        iconLabel.setForeground(accent);

        top.add(titleLabel, BorderLayout.WEST);
        top.add(iconLabel, BorderLayout.EAST);

        value.setFont(new Font("SansSerif", Font.BOLD, 24));
        value.setForeground(TEXT);

        JLabel detail = new JLabel(description);
        detail.setFont(new Font("SansSerif", Font.PLAIN, 10));
        detail.setForeground(SECONDARY);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));

        bottom.add(value);
        bottom.add(Box.createVerticalStrut(5));
        bottom.add(detail);

        card.add(top, BorderLayout.NORTH);
        card.add(bottom, BorderLayout.CENTER);

        return card;
    }

    // =====================================================
    // CHARTS
    // =====================================================

    private JPanel createChartsSection() {

        JPanel section = new JPanel(new GridLayout(1, 2, 16, 0));
        section.setOpaque(false);
        section.setPreferredSize(new Dimension(900, 285));
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 285));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        trendChart = new PayrollTrendChart();
        departmentChart = new DepartmentChart();

        section.add(createChartCard(
                "Monthly Payroll Trend",
                "Net salary by payment month",
                trendChart
        ));

        section.add(createChartCard(
                "Payroll by Department",
                "Net salary distribution",
                departmentChart
        ));

        return section;
    }

    private JPanel createChartCard(
            String title,
            String subtitle,
            JPanel chart) {

        RoundedPanel card = new RoundedPanel(12, WHITE, BORDER);
        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(new EmptyBorder(17, 18, 15, 18));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setForeground(TEXT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        subtitleLabel.setForeground(SECONDARY);

        heading.add(titleLabel);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitleLabel);

        card.add(heading, BorderLayout.NORTH);
        card.add(chart, BorderLayout.CENTER);

        return card;
    }

    // =====================================================
    // RECENT PAYROLL TABLE
    // =====================================================

    private JPanel createRecentPayrollSection() {

        RoundedPanel card = new RoundedPanel(12, WHITE, BORDER);
        card.setLayout(new BorderLayout(0, 14));
        card.setBorder(new EmptyBorder(17, 18, 17, 18));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));
        card.setPreferredSize(new Dimension(900, 300));

        JPanel heading = new JPanel(new BorderLayout());
        heading.setOpaque(false);

        JLabel title = new JLabel("Recent Payroll Records");
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        title.setForeground(TEXT);

        JButton viewAll = new JButton("View All  →");
        viewAll.setFont(new Font("SansSerif", Font.BOLD, 11));
        viewAll.setForeground(BLUE);
        viewAll.setBorderPainted(false);
        viewAll.setContentAreaFilled(false);
        viewAll.setFocusPainted(false);
        viewAll.setCursor(new Cursor(Cursor.HAND_CURSOR));

        viewAll.addActionListener(e -> {
            cardLayout.show(mainContent, "Reports");
        });

        heading.add(title, BorderLayout.WEST);
        heading.add(viewAll, BorderLayout.EAST);

        String[] columns = {
                "Payroll ID",
                "Employee Name",
                "Department",
                "Payroll Month",
                "Payment Date",
                "Net Salary"
        };

        recentTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(recentTableModel);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.getViewport().setBackground(WHITE);
        scroll.setPreferredSize(new Dimension(800, 180));

        card.add(heading, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);

        return card;
    }

    private void styleTable(JTable table) {

        table.setRowHeight(34);
        table.setFont(new Font("SansSerif", Font.PLAIN, 11));
        table.setForeground(TEXT);
        table.setBackground(WHITE);
        table.setGridColor(BORDER);
        table.setSelectionBackground(BLUE_LIGHT);
        table.setSelectionForeground(TEXT);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 11));
        header.setForeground(SECONDARY);
        header.setBackground(new Color(248, 250, 252));
        header.setPreferredSize(new Dimension(100, 36));
        header.setReorderingAllowed(false);
    }

    // =====================================================
    // LOAD ORACLE DATA
    // =====================================================

    private void loadDashboardData() {

        try {

            Object[] stats = dashboardDAO.getDashboardStats();

            if (stats != null && stats.length >= 4) {

                int employeeCount = stats[0] instanceof Number
                        ? ((Number) stats[0]).intValue() : 0;

                int payrollCount = stats[1] instanceof Number
                        ? ((Number) stats[1]).intValue() : 0;

                double net = stats[3] instanceof Number
                        ? ((Number) stats[3]).doubleValue() : 0;

                employeesValue.setText(String.valueOf(employeeCount));
                totalPayrollValue.setText(currency.format(net));

                double average = payrollCount > 0
                        ? net / payrollCount
                        : 0;

                averageSalaryValue.setText(currency.format(average));
            }

            List<Object[]> monthlyData =
                    dashboardDAO.getMonthlyPayrollTrend();

            List<Object[]> departmentData =
                    dashboardDAO.getDepartmentPayroll();

            List<Object[]> recentData =
                    dashboardDAO.getRecentPayrolls();

            if (monthlyData == null) {
                monthlyData = new ArrayList<>();
            }

            if (departmentData == null) {
                departmentData = new ArrayList<>();
            }

            if (recentData == null) {
                recentData = new ArrayList<>();
            }

            if (trendChart != null) {
                trendChart.setData(monthlyData);
            }

            if (departmentChart != null) {
                departmentChart.setData(departmentData);
            }

            if (recentTableModel != null) {

                recentTableModel.setRowCount(0);

                for (Object[] row : recentData) {

                    if (row == null || row.length < 6) {
                        continue;
                    }

                    double netSalary = row[5] instanceof Number
                            ? ((Number) row[5]).doubleValue() : 0;

                    recentTableModel.addRow(new Object[]{
                            row[0],
                            row[1],
                            row[2],
                            row[3],
                            row[4],
                            currency.format(netSalary)
                    });
                }
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load dashboard data.\n" + e.getMessage(),
                    "Dashboard Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // =====================================================
    // MONTHLY TREND LINE CHART
    // =====================================================

    private static class PayrollTrendChart extends JPanel {

        private List<Object[]> data = new ArrayList<>();

        PayrollTrendChart() {
            setOpaque(false);
        }

        void setData(List<Object[]> data) {
            this.data = data != null ? data : new ArrayList<>();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int width = getWidth();
            int height = getHeight();

            int left = 42;
            int right = 15;
            int top = 22;
            int bottom = 35;

            int chartWidth = width - left - right;
            int chartHeight = height - top - bottom;

            if (data.isEmpty()) {
                drawEmpty(g2, width, height);
                g2.dispose();
                return;
            }

            double max = 0;

            for (Object[] row : data) {
                max = Math.max(max, ((Number) row[1]).doubleValue());
            }

            if (max <= 0) {
                max = 1;
            }

            // Horizontal grid lines
            g2.setColor(new Color(226, 232, 240));

            for (int i = 0; i < 4; i++) {
                int y = top + i * chartHeight / 3;
                g2.drawLine(left, y, width - right, y);
            }

            int count = data.size();

            int[] xPoints = new int[count];
            int[] yPoints = new int[count];

            for (int i = 0; i < count; i++) {

                double value =
                        ((Number) data.get(i)[1]).doubleValue();

                xPoints[i] = left +
                        (count == 1 ? chartWidth / 2
                                : i * chartWidth / (count - 1));

                yPoints[i] = top + chartHeight -
                        (int) ((value / max) * chartHeight * 0.85);

                g2.setColor(SECONDARY);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 9));

                g2.drawString(
                        String.valueOf(data.get(i)[0]),
                        xPoints[i] - 10,
                        height - 12
                );
            }

            // Trend line
            g2.setColor(BLUE);
            g2.setStroke(new BasicStroke(
                    2.5f,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND
            ));

            for (int i = 0; i < count - 1; i++) {
                g2.drawLine(
                        xPoints[i],
                        yPoints[i],
                        xPoints[i + 1],
                        yPoints[i + 1]
                );
            }

            // Data points
            for (int i = 0; i < count; i++) {

                g2.setColor(WHITE);
                g2.fillOval(
                        xPoints[i] - 5,
                        yPoints[i] - 5,
                        10, 10
                );

                g2.setColor(BLUE);
                g2.fillOval(
                        xPoints[i] - 3,
                        yPoints[i] - 3,
                        6, 6
                );
            }

            g2.dispose();
        }

        private void drawEmpty(
                Graphics2D g2,
                int width,
                int height) {

            g2.setColor(SECONDARY);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g2.drawString(
                    "No payroll trend data available",
                    Math.max(10, width / 2 - 90),
                    height / 2
            );
        }
    }

    // =====================================================
    // DEPARTMENT BAR CHART
    // =====================================================

    private static class DepartmentChart extends JPanel {

        private List<Object[]> data = new ArrayList<>();

        DepartmentChart() {
            setOpaque(false);
        }

        void setData(List<Object[]> data) {
            this.data = data != null ? data : new ArrayList<>();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int width = getWidth();
            int height = getHeight();

            if (data.isEmpty()) {
                g2.setColor(SECONDARY);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
                g2.drawString(
                        "No department data available",
                        Math.max(10, width / 2 - 90),
                        height / 2
                );
                g2.dispose();
                return;
            }

            int count = data.size();
            int left = 20;
            int right = 15;
            int top = 20;
            int bottom = 38;

            int availableWidth = width - left - right;
            int chartHeight = height - top - bottom;

            double max = 0;

            for (Object[] row : data) {
                max = Math.max(
                        max,
                        ((Number) row[1]).doubleValue()
                );
            }

            if (max <= 0) {
                max = 1;
            }

            int slot = availableWidth / count;
            int barWidth = Math.min(30, Math.max(12, slot / 2));

            for (int i = 0; i < count; i++) {

                double value =
                        ((Number) data.get(i)[1]).doubleValue();

                int barHeight = (int) (
                        (value / max) * chartHeight * 0.85
                );

                int x = left + i * slot +
                        (slot - barWidth) / 2;

                int y = top + chartHeight - barHeight;

                g2.setColor(new Color(239, 246, 255));
                g2.fillRoundRect(
                        x,
                        top + 5,
                        barWidth,
                        chartHeight - 5,
                        9, 9
                );

                g2.setColor(BLUE);
                g2.fillRoundRect(
                        x,
                        y,
                        barWidth,
                        Math.max(4, barHeight),
                        9, 9
                );

                String label = String.valueOf(data.get(i)[0]);

                if (label.length() > 9) {
                    label = label.substring(0, 8) + "...";
                }

                g2.setColor(SECONDARY);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 9));

                FontMetrics fm = g2.getFontMetrics();

                g2.drawString(
                        label,
                        x + (barWidth - fm.stringWidth(label)) / 2,
                        height - 12
                );
            }

            g2.dispose();
        }
    }

    // =====================================================
    // ROUNDED PANEL
    // =====================================================

    private static class RoundedPanel extends JPanel {

        private final int radius;
        private final Color background;
        private final Color border;

        RoundedPanel(
                int radius,
                Color background,
                Color border) {

            this.radius = radius;
            this.background = background;
            this.border = border;

            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(background);
            g2.fillRoundRect(
                    0, 0,
                    getWidth() - 1,
                    getHeight() - 1,
                    radius, radius
            );

            g2.setColor(border);
            g2.drawRoundRect(
                    0, 0,
                    getWidth() - 1,
                    getHeight() - 1,
                    radius, radius
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }
}
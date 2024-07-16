package rupp;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class Dashboard {

    private final JFrame frame;
    private final JPanel mainPanel;
    private final CardLayout cardLayout;
    private JPanel dashboard;
    private JPanel newsalePanel;
    private JPanel viewSale;
    private JPanel inventoryPanel;
    private JPanel report;
    private JPanel setting;
    private JTable paymentTable;
    private DefaultTableModel paymentTableModel;
    private final ArrayList<Phone> phonesList;
    private JPanel navProductPanel;
    private String username;
    private JScrollPane scrollPane;
    private String photoPath;
    Font font24B = new Font("Arial", Font.BOLD, 24);
    Font font20B = new Font("Poppins", Font.BOLD, 20);
    Font font20 = new Font("Poppins", Font.PLAIN, 20);
    Font font18 = new Font("Poppins", Font.PLAIN, 18);
    Font font18B = new Font("Poppins", Font.BOLD, 18);
    Font font40 = new Font("Arial", Font.PLAIN, 40);
    Font font30 = new Font("Arial", Font.PLAIN, 30);
    Font font30B = new Font("Arial", Font.BOLD, 30);
    Font font17 = new Font("Arial", Font.PLAIN, 17);
    Cursor pointer = new Cursor(Cursor.HAND_CURSOR);

    public Dashboard(String username) {
        this.username = username;
        frame = new JFrame("Phone Shop Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        phonesList = new ArrayList<>();
        readProductsFromFile();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        frame.add(mainPanel);

        createDashboardPanel();
        mainPanel.add(dashboard, "Dashboard");

        createNewSalePanel();
        mainPanel.add(newsalePanel, "newSale");

        createViewSale();
        mainPanel.add(viewSale, "viewSale");

        createInventoryPanel();
        mainPanel.add(inventoryPanel, "inventoryPanel");

        creatReportPanel();
        mainPanel.add(report, "report");

        createSettingPanel();
        mainPanel.add(setting, "setting");

        createSidebar();

        frame.setVisible(true);
        updateProductList();

    }

    public void refreshUserProfile(String newUsername, String newPhotoPath) {
        this.username = newUsername;
        this.photoPath = newPhotoPath;

        // Update navigation panel
        // mainPanel.remove(dashboard);
        frame.add(mainPanel);

        createDashboardPanel();
        mainPanel.add(dashboard, "Dashboard");

        createNewSalePanel();
        mainPanel.add(newsalePanel, "newSale");

        createViewSale();
        mainPanel.add(viewSale, "viewSale");

        createInventoryPanel();
        mainPanel.add(inventoryPanel, "inventoryPanel");

        creatReportPanel();
        mainPanel.add(report, "report");

        createSettingPanel();
        mainPanel.add(setting, "setting");
        // createSidebar();
        // updateProductList();
        // Ensure the changes are reflected immediately
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
        updateProductList();

    }

    // Create Navigatoin class
    private JPanel navigation(String nameHeader) {
        // Create a navigation title
        JLabel navTitle = new JLabel(nameHeader);
        navTitle.setFont(font24B);
        navTitle.setForeground(Color.BLUE);
        navTitle.setHorizontalAlignment(SwingConstants.CENTER);
        navTitle.setBorder(new EmptyBorder(10, 10, 10, 10));

        String filePath = "D:\\RUPP\\Java Programming\\RUPP\\src\\rupp\\user_data.txt";
        String userName = username;
        final ImageIcon[] userLogo = { null }; // Use an array to make 'final' work

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line into username, password, and photo path
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String fileUsername = parts[0];
                    String password = parts[1];
                    String photoPath = parts[2];
                    this.photoPath = photoPath;

                    // Compare the input username with the username from the file
                    if (fileUsername.trim().equals(userName)) {
                        System.out.println("Password: " + password);
                        userLogo[0] = new ImageIcon(photoPath); // Assign to array element
                        break; // Exit the loop once the username is found
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (userLogo[0] == null) {
            // Handle case where userLogo is not found (e.g., set a default image)
            userLogo[0] = new ImageIcon("default_image.png"); // Provide a default image path here
        }

        // Scale the image to the desired size (e.g., 50x50 pixels)
        Image scaledImage = userLogo[0].getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        // Create a new ImageIcon with the scaled image
        ImageIcon scaledUserLogo = new ImageIcon(scaledImage);

        // User info components
        JLabel userLogoLabel = new JLabel(scaledUserLogo);
        userLogoLabel.setCursor(pointer);
        userLogoLabel.setToolTipText("Click to view profile");

        JButton userNamebtn = new JButton(userName);
        userNamebtn.setCursor(pointer);
        userNamebtn.setBorderPainted(false); // Remove border
        userNamebtn.setFocusPainted(false); // Remove focus border
        userNamebtn.setContentAreaFilled(false); // Remove background

        userNamebtn.addActionListener((ActionEvent e) -> {
            Image scaledImage1 = userLogo[0].getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            // Create a new ImageIcon with the scaled image
            ImageIcon scaledUserLogo1 = new ImageIcon(scaledImage1);
            showProfileDialog(userName, scaledUserLogo1);
        });

        // User info panel (right side)
        JPanel userInfoPanel = new JPanel(new BorderLayout());
        userInfoPanel.setBackground(Color.WHITE);
        userInfoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        userInfoPanel.add(userLogoLabel, BorderLayout.WEST);
        userInfoPanel.add(userNamebtn, BorderLayout.CENTER);

        // Header Panel (navigation title + user info panel)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(navTitle, BorderLayout.WEST);
        headerPanel.add(Box.createRigidArea(new Dimension(20, 0))); // Add space between navTitle and userInfoPanel
        headerPanel.add(userInfoPanel, BorderLayout.EAST);
        return headerPanel;
    }

    // Footer
    private JPanel Footer() {
        JLabel nameTeam = new JLabel("E1-G1");
        nameTeam.setFont(font18);
        nameTeam.setForeground(Color.BLUE);
        nameTeam.setCursor(pointer);
        JLabel poweredBy = new JLabel("Powered By:");
        poweredBy.setFont(font18);
        JLabel feedback = new JLabel("Feedback");
        feedback.setFont(font18);
        feedback.setForeground(Color.BLUE);
        feedback.setCursor(pointer);
        JLabel Slas = new JLabel("|");
        Slas.setFont(font18);
        JLabel systemGuidle = new JLabel("System Guidline");
        systemGuidle.setFont(font18);
        systemGuidle.setForeground(Color.BLUE);
        systemGuidle.setCursor(pointer);

        JPanel footerPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(poweredBy);
        leftPanel.add(nameTeam);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(feedback);
        centerPanel.add(Slas);
        centerPanel.add(systemGuidle);

        footerPanel.add(leftPanel, BorderLayout.WEST);
        footerPanel.add(centerPanel, BorderLayout.CENTER);

        return footerPanel;
    }

    // Create Side Bar
    private void createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS)); // Vertical layout for sidebar
        sidebar.setBackground(Color.DARK_GRAY);
        sidebar.setPreferredSize(new Dimension(200, frame.getHeight()));

        // Top bar setup
        JPanel topBar = new JPanel();
        topBar.setBackground(null);
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.Y_AXIS)); // Vertical layout for top bar

        // Logo setup
        ImageIcon originalImg = new ImageIcon("D:\\RUPP\\Java Programming\\RUPP\\src\\rupp\\logo.png");
        Image img = originalImg.getImage();
        Image resizedImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImg);
        JLabel imageLabel = new JLabel(resizedIcon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the image

        // Add logo to top bar
        topBar.add(Box.createRigidArea(new Dimension(0, 20))); // Space at the top
        topBar.add(imageLabel);
        topBar.add(Box.createRigidArea(new Dimension(0, 20))); // Space between image and buttons

        // Buttons setup
        JButton btnDashboard = new JButton("Dashboard");
        JButton btnAddProduct = new JButton("Add Product");
        JButton btnViewInventory = new JButton("Inventory");
        JButton btnNewSale = new JButton("New Sale");
        JButton btnViewSales = new JButton("View Sales");
        JButton btnReport = new JButton("Report");
        JButton btnSettings = new JButton("Settings");

        // Array of buttons
        JButton[] buttons = { btnDashboard, btnAddProduct, btnViewInventory, btnNewSale, btnViewSales, btnReport,
                btnSettings };

        for (JButton button : buttons) {
            button.setMaximumSize(new Dimension(160, 40));
            button.setFont(font20B);
            button.setCursor(pointer);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            topBar.add(button);
            topBar.add(Box.createRigidArea(new Dimension(0, 15))); // Space between buttons
        }

        // Bottom panel setup
        JLabel expireDateLabel = new JLabel("Expire Date: ");
        expireDateLabel.setForeground(Color.WHITE);
        JLabel dateLabel = new JLabel("2024-07-31");
        dateLabel.setForeground(Color.WHITE);

        // Create a panel to hold the labels
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        labelPanel.setBackground(null);

        // Add labels to the panel
        labelPanel.add(expireDateLabel);
        labelPanel.add(dateLabel);

        // Create the bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBackground(Color.DARK_GRAY);

        // Add label panel to the bottom of the bottom panel
        bottomPanel.add(labelPanel, BorderLayout.SOUTH);

        // Add top bar and bottom panel to sidebar
        sidebar.add(topBar);
        sidebar.add(Box.createVerticalGlue()); // Pushes bottom panel to the bottom
        sidebar.add(bottomPanel);

        // Add sidebar to frame
        frame.add(sidebar, BorderLayout.WEST);

        // Button actions (assuming these are defined elsewhere in your class)
        btnAddProduct.addActionListener(e -> showAddProductDialog());
        btnDashboard.addActionListener(e -> {
            cardLayout.show(mainPanel, "Dashboard");
            updateProductList();
        });
        btnNewSale.addActionListener(e -> cardLayout.show(mainPanel, "newSale"));
        btnViewSales.addActionListener(e -> cardLayout.show(mainPanel, "viewSale"));
        btnViewInventory.addActionListener(e -> cardLayout.show(mainPanel, "inventoryPanel"));
        btnReport.addActionListener(e -> cardLayout.show(mainPanel, "report"));
        btnSettings.addActionListener(e -> cardLayout.show(mainPanel, "setting"));
    }

    // Create Dashbord Pannel
    private void createDashboardPanel() {
        dashboard = new JPanel(new BorderLayout());

        // Create a footer panel
        JPanel footerPanel = Footer(); // Replace Footer() with your actual method to create the footer

        // Add the footer panel to the bottom of the dashboard
        dashboard.add(footerPanel, BorderLayout.SOUTH);

        updateDashboardPanels(); // Initial update of dashboard panels
    }

    // Update Dashboard Panel When have something change in product stock
    private void updateDashboardPanels() {
        dashboard.removeAll();
        updateSummaryPanel();
        updateRecentPurchasesPanel();
        updateTopProductsPanel();

        dashboard.revalidate();
        dashboard.repaint();
    }

    // Create Summary Label
    private JLabel createSummaryLabel(String title, String value, Color color) {
        JLabel label = new JLabel(
                "<html><div style='text-align: center;'><h2>" + value + "</h2><p>" + title + "</p></div></html>");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createLineBorder(color, 2));
        label.setBackground(color);
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(100, 100));
        return label;
    }

    private void updateSummaryPanel() {
        // Ensure the products are loaded into phonesList
        readProductsFromFile();
        // Create a navigation title
        JPanel headerPanel = navigation("Dashboard");
        // Summary Panel
        JPanel summaryPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        summaryPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        int totalProductsCount = phonesList.size();
        int lowStockCount = (int) phonesList.stream().filter(p -> p.getQty() > 0 && p.getQty() < 5).count();
        int outOfStockCount = (int) phonesList.stream().filter(p -> p.getQty() == 0).count();
        Phone mostStockProduct = phonesList.stream()
                .max((p1, p2) -> Integer.compare(p1.getQty(), p2.getQty())).orElse(null);

        JLabel totalProducts = createSummaryLabel("Total Products", String.valueOf(totalProductsCount), Color.ORANGE);
        totalProducts.setFont(font20B);
        JLabel lowStockProducts = createSummaryLabel("Low Stock Products", String.valueOf(lowStockCount),
                Color.MAGENTA);
        lowStockProducts.setFont(font20B);
        JLabel outOfStockProducts = createSummaryLabel("Out of Stock Products", String.valueOf(outOfStockCount),
                Color.RED);
        outOfStockProducts.setFont(font20B);
        JLabel mostStockProductLabel = createSummaryLabel("Most Stock Product",
                mostStockProduct != null ? mostStockProduct.getName() : "N/A", Color.GREEN);
        mostStockProductLabel.setFont(font20B);

        summaryPanel.add(totalProducts);
        summaryPanel.add(lowStockProducts);
        summaryPanel.add(outOfStockProducts);
        summaryPanel.add(mostStockProductLabel);

        // Add header panel and summary panel to the dashboard
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(summaryPanel, BorderLayout.CENTER);
        dashboard.add(mainPanel, BorderLayout.NORTH);
    }

    // Method to show profile dialog
    private void showProfileDialog(String userName, ImageIcon userLogo) {
        // Create dialog
        JDialog profileDialog = new JDialog(frame, "User Profile");
        profileDialog.setSize(380, 350);
        profileDialog.setResizable(false);
        profileDialog.setLayout(new BorderLayout());

        // Add WindowFocusListener to dispose dialog when it loses focus
        profileDialog.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                // No action needed when the window gains focus
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                profileDialog.dispose();
            }
        });

        // Calculate position to set dialog at top-right corner
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int dialogWidth = profileDialog.getSize().width;
        // int dialogHeight = profileDialog.getSize().height;
        int x = screenSize.width - dialogWidth - 10; // Adjust 10 pixels from the right edge
        int y = 100; // 10 pixels from the top edge
        profileDialog.setLocation(x, y);

        // Set dialog to stay on top
        profileDialog.setAlwaysOnTop(true);

        // Panel for user info
        JPanel userInfoPanel = new JPanel(new BorderLayout());
        userInfoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Add user logo
        JLabel userLogoLabel = new JLabel(userLogo);
        userLogoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userInfoPanel.add(userLogoLabel, BorderLayout.NORTH);

        // Add user name
        JLabel userNameLabel = new JLabel(userName);
        int fontSize = 30; // Example size, adjust as needed
        Font nameFont = new Font(userNameLabel.getFont().getName(), Font.BOLD, fontSize);
        userNameLabel.setFont(nameFont);
        userNameLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align user name
        userInfoPanel.add(userNameLabel, BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 20, 10)); // Add space above buttons

        // Add profile button
        JButton profileButton = new JButton("Profile");
        profileButton.setCursor(pointer);
        profileButton.setFont(font18); // Set the font for profileButton
        profileButton.setPreferredSize(new Dimension(120, 40)); // Set preferred size
        profileButton.addActionListener((ActionEvent e) -> {
            EditProfileDialog editProfileDialog = new EditProfileDialog(frame, userName, photoPath, this);
            editProfileDialog.setVisible(true);
        });
        buttonPanel.add(profileButton);

        // Add space between buttons
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Add 10 pixels of horizontal space

        // Add sign out button
        JButton signOutButton = new JButton("Sign Out");
        signOutButton.setCursor(pointer);
        signOutButton.setFont(font18);
        signOutButton.setPreferredSize(new Dimension(120, 40)); // Set preferred size
        signOutButton.addActionListener((ActionEvent e) -> {
            profileDialog.dispose();
            frame.dispose();
            SwingUtilities.invokeLater(() -> new PhoneShopManagementSystem());
        });
        buttonPanel.add(signOutButton);

        // Add panels to dialog
        profileDialog.add(userInfoPanel, BorderLayout.CENTER);
        profileDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Show dialog
        profileDialog.setVisible(true);
    }

    // Update Recently Purchase Pannel

    private void updateRecentPurchasesPanel() {
        JPanel recentPurchasePanel = new JPanel(new BorderLayout());

        TitledBorder titledBorder = BorderFactory.createTitledBorder("Recent Purchase Invoice");
        titledBorder.setTitleFont(font20B);
        recentPurchasePanel.setBorder(titledBorder);

        // Updated columns
        String[] recentColumns = { "No.", "Products Name", "Vendor Name", "Price", "Qty.", "Total", "Discount",
                "Net Total", "Purchase Date" };
        List<Object[]> recentDataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("recentlyBuy.txt"))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Object[] row = {
                        values[0],
                        values[1],
                        values[2],
                        "$" + String.format("%.2f", Double.valueOf(values[3])),
                        Integer.valueOf(values[4]),
                        "$" + String.format("%.2f", Double.valueOf(values[5])),
                        values[6] + "%",
                        "$" + String.format("%.2f", Double.valueOf(values[7])),
                        values[8]
                };
                recentDataList.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sort the data by purchase date in descending order
        recentDataList.sort((Object[] o1, Object[] o2) -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-H-m-s");
            try {
                Date date1 = sdf.parse((String) o1[8]);
                Date date2 = sdf.parse((String) o2[8]);
                return date2.compareTo(date1); // Newer dates first
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        });

        // Update "No." column and format purchase date
        for (int i = 0; i < recentDataList.size(); i++) {
            recentDataList.get(i)[0] = i + 1;
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd-H-m-s");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            try {
                Date date = inputFormat.parse((String) recentDataList.get(i)[8]);
                recentDataList.get(i)[8] = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Object[][] recentData = recentDataList.toArray(Object[][]::new);

        JTable recentTable = new JTable(recentData, recentColumns);
        recentTable.setFont(font18);
        recentTable.setRowHeight(30);

        // Set font size for table header
        JTableHeader tableHeader = recentTable.getTableHeader();
        tableHeader.setFont(font20B);

        // Set width for No. and Qty for 5px
        TableColumnModel columnNo = recentTable.getColumnModel();
        columnNo.getColumn(0).setPreferredWidth(5);
        TableColumnModel columnQty = recentTable.getColumnModel();
        columnQty.getColumn(4).setPreferredWidth(5);

        JScrollPane recentScrollPane = new JScrollPane(recentTable);
        recentPurchasePanel.add(recentScrollPane, BorderLayout.CENTER);

        dashboard.add(recentPurchasePanel, BorderLayout.CENTER);
        dashboard.revalidate(); // Ensure the panel is updated
        dashboard.repaint(); // Ensure the panel is updated
    }

    private void updateTopProductsPanel() {
        JPanel topProductsPanel = new JPanel(new BorderLayout());

        TitledBorder titledBorder = BorderFactory.createTitledBorder("Top Sale");
        titledBorder.setTitleFont(font20B);
        topProductsPanel.setBorder(titledBorder);

        String[] topProductsColumns = { "No.", "Products Name", "Vendor Name", "Price", "Qty.", "Total", "Discount",
                "Net Total", "Purchase Date" };
        List<Object[]> topProductsDataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("recentlyBuy.txt"))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Object[] row = {
                        0, // Placeholder for "No." column
                        values[1],
                        values[2],
                        "$" + String.format("%.2f", Double.valueOf(values[3])),
                        Integer.valueOf(values[4]),
                        "$" + String.format("%.2f", Double.valueOf(values[5])),
                        values[6] + "%",
                        "$" + String.format("%.2f", Double.valueOf(values[7])),
                        values[8]
                };
                topProductsDataList.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sort the data by quantity in descending order
        topProductsDataList.sort((Object[] o1, Object[] o2) -> Integer.compare((int) o2[4], (int) o1[4]) // Compare
                                                                                                         // quantities
        );

        // Update "No." column and format the date
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd-H-m-s");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd h:mm a");

        for (int i = 0; i < topProductsDataList.size(); i++) {
            topProductsDataList.get(i)[0] = i + 1;
            try {
                Date date = inputFormat.parse((String) topProductsDataList.get(i)[8]);
                topProductsDataList.get(i)[8] = outputFormat.format(date); // Format the date
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Object[][] topProductsData = topProductsDataList.toArray(Object[][]::new);

        JTable topProductsTable = new JTable(topProductsData, topProductsColumns);
        topProductsTable.setFont(font18);
        topProductsTable.setRowHeight(30);

        // Set font size for table header
        JTableHeader tableHeader = topProductsTable.getTableHeader();
        tableHeader.setFont(font20B);

        // Set width for No. and Qty for 5px
        TableColumnModel columnNo = topProductsTable.getColumnModel();
        columnNo.getColumn(0).setPreferredWidth(5);
        TableColumnModel columnQty = topProductsTable.getColumnModel();
        columnQty.getColumn(4).setPreferredWidth(5);

        JScrollPane topProductsScrollPane = new JScrollPane(topProductsTable);
        topProductsPanel.add(topProductsScrollPane, BorderLayout.CENTER);

        // Add footer panel
        JPanel footerPanel = Footer(); // Replace with actual method to create footer
        topProductsPanel.add(footerPanel, BorderLayout.SOUTH);

        dashboard.add(topProductsPanel, BorderLayout.SOUTH);
        dashboard.revalidate(); // Ensure the panel is updated
        dashboard.repaint(); // Ensure the panel is updated
    }

    // New Sale Panel
    private void createNewSalePanel() {
        newsalePanel = new JPanel(new BorderLayout()); // Use BorderLayout for proper alignment

        // Get navigation header panel
        JPanel headerPanel = navigation("New Sale");
        JPanel footerPanel = Footer(); // Call the Footer method

        // Other components setup
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.CYAN);

        JLabel lblPayment = new JLabel("Payment");
        JPanel navPaymentPanel = new JPanel();
        JLabel lblProduct = new JLabel("List Product");
        JPanel navListProductPanel = new JPanel();
        navProductPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        JScrollPane scrollPane = new JScrollPane(navProductPanel);
        JButton btnClear = new JButton("Clear all");
        JButton btnExit = new JButton("Exit");
        JButton btnInvoice = new JButton("Invoice");

        // Product side
        lblPayment.setFont(font40);
        lblPayment.setForeground(Color.BLUE);
        navPaymentPanel.setBackground(Color.white);

        // Payment side
        String[] columnNames = { "No.", "Name", "Price", "Qty", "Discount", "Total" };
        paymentTableModel = new DefaultTableModel(columnNames, 0);
        paymentTable = new JTable(paymentTableModel);
        JScrollPane paymentScrollPane = new JScrollPane(paymentTable);
        JTableHeader header = paymentTable.getTableHeader();
        header.setFont(font17);
        paymentTable.setFont(font17);
        paymentTable.setRowHeight(30);

        TableColumn colNo = paymentTable.getColumnModel().getColumn(0);
        TableColumn colPrice = paymentTable.getColumnModel().getColumn(2);
        TableColumn colQty = paymentTable.getColumnModel().getColumn(3);

        colNo.setPreferredWidth(5);
        colPrice.setPreferredWidth(10);
        colQty.setPreferredWidth(5);

        // Buttons
        btnClear.setFont(font30B);
        btnClear.setCursor(pointer);
        btnClear.setBackground(Color.MAGENTA);
        btnClear.setForeground(Color.white);

        btnInvoice.setFont(font30B);
        btnInvoice.setCursor(pointer);
        btnInvoice.setBackground(Color.green);
        btnInvoice.setForeground(Color.white);

        btnExit.setFont(font30B);
        btnExit.setCursor(pointer);
        btnExit.setBackground(Color.RED);
        btnExit.setForeground(Color.white);

        btnClear.addActionListener((ActionEvent e) -> {
            paymentTableModel.setRowCount(0);
        });

        btnExit.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });

        btnInvoice.addActionListener((ActionEvent e) -> {
            generateInvoice();
            writeProductsToFile();
            updateProductList();
            updateDashboardPanels();
            paymentTableModel.setRowCount(0);
        });

        // Product side
        lblProduct.setFont(font40);
        lblProduct.setForeground(Color.BLUE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        navListProductPanel.setBackground(Color.white);

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
        verticalScrollBar.setUnitIncrement(16);
        horizontalScrollBar.setUnitIncrement(16);
        verticalScrollBar.setBlockIncrement(100);
        horizontalScrollBar.setBlockIncrement(100);

        updateProductList();

        // Layout setup using GridBagLayout for responsiveness
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // Payment section
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        centerPanel.add(lblPayment, gbc);

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        centerPanel.add(paymentScrollPane, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0;
        JPanel paymentButtonPanel = new JPanel();
        paymentButtonPanel.setBackground(Color.CYAN); // Match centerPanel background
        paymentButtonPanel.setLayout(new BoxLayout(paymentButtonPanel, BoxLayout.X_AXIS));
        paymentButtonPanel.add(btnClear);
        paymentButtonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        paymentButtonPanel.add(btnInvoice);
        paymentButtonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        paymentButtonPanel.add(btnExit);
        centerPanel.add(paymentButtonPanel, gbc);

        // Product section
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        centerPanel.add(lblProduct, gbc);

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        centerPanel.add(scrollPane, gbc);

        // Adding header panel, center panel, and footer panel to newsalePanel
        newsalePanel.add(headerPanel, BorderLayout.NORTH);
        newsalePanel.add(centerPanel, BorderLayout.CENTER);
        newsalePanel.add(footerPanel, BorderLayout.SOUTH);
    }

    private void updateProductList() {
        navProductPanel.removeAll();

        for (Phone phone : phonesList) {
            JPanel productPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            JLabel picLabel;
            try {
                BufferedImage productImage = ImageIO.read(new File(phone.getImagePath()));
                Image scaledImage = productImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                ImageIcon productIcon = new ImageIcon(scaledImage);
                picLabel = new JLabel(productIcon);
            } catch (IOException e) {
                e.printStackTrace();
                picLabel = new JLabel("Image not found");
            }

            JLabel nameLabel = new JLabel(phone.getName());
            JLabel priceLabel = new JLabel("$" + phone.getPrice());
            JLabel qtyLabel = new JLabel("In stock : " + String.valueOf(phone.getQty()));
            JButton btnBuy = new JButton("Buy Now");
            btnBuy.setCursor(pointer);

            priceLabel.setFont(font18);
            nameLabel.setFont(font20);

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.insets = new Insets(5, 0, 5, 0);
            productPanel.add(picLabel, gbc);

            gbc.gridy = 1;
            productPanel.add(nameLabel, gbc);

            gbc.gridy = 2;
            productPanel.add(priceLabel, gbc);

            gbc.gridy = 3;
            productPanel.add(qtyLabel, gbc);

            gbc.gridy = 4;
            productPanel.add(btnBuy, gbc);

            productPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            productPanel.setBackground(Color.WHITE);
            productPanel.setCursor(pointer);

            btnBuy.addActionListener((ActionEvent e) -> {
                // Create a custom dialog for quantity and discount input
                JPanel dialogPanel = new JPanel(new GridLayout(0, 1));
                JTextField quantityField = new JTextField();
                quantityField.setFont(font20);
                JTextField discountField = new JTextField();
                discountField.setFont(font20);
                JLabel Qty = new JLabel("Quantity:");
                JLabel Dis = new JLabel("Discount (%):");
                Qty.setFont(font20);
                Dis.setFont(font20);

                dialogPanel.add(Qty);
                dialogPanel.add(quantityField);
                dialogPanel.add(Dis);
                dialogPanel.add(discountField);

                JOptionPane optionPane = new JOptionPane(dialogPanel, JOptionPane.PLAIN_MESSAGE,
                        JOptionPane.OK_CANCEL_OPTION);
                JDialog dialog = optionPane.createDialog(frame, "Enter Quantity and Discount");

                // Set the size of the dialog
                dialog.setSize(400, 250);
                dialog.setResizable(false); // Optional: make the dialog non-resizable
                dialog.setVisible(true);

                int option = (Integer) optionPane.getValue();
                if (option == JOptionPane.OK_OPTION) {
                    // User clicked OK, process the input
                    try {
                        int quantity = Integer.parseInt(quantityField.getText().trim());
                        String discountText = discountField.getText().trim();
                        float discount = discountText.isEmpty() ? 0 : Float.parseFloat(discountText);

                        if (quantity <= 0) {
                            JOptionPane.showMessageDialog(frame, "Quantity must be greater than zero.");
                            return;
                        }
                        if (quantity > phone.getQty()) {
                            JOptionPane.showMessageDialog(frame, "Not enough stock available.");
                            return;
                        }

                        // Calculate total with discount
                        double priceOfOne = phone.getPrice();
                        double discountedPrice = priceOfOne * (1 - discount / 100); // Apply discount
                        double totalPrice = discountedPrice * quantity;

                        String total = String.format("$%.2f", totalPrice);
                        String price = String.format("$%.2f", priceOfOne);
                        String discountFormat = String.format("%.2f%%", discount);

                        // Add to payment table
                        Object[] rowData = { paymentTableModel.getRowCount() + 1, phone.getName(), price, quantity,
                                discountFormat, total };
                        paymentTableModel.addRow(rowData);

                        // Update phone quantity
                        phone.setQty(phone.getQty() - quantity);

                        // Update product list display
                        updateProductList();
                        updateInventoryTable();

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid numbers.");
                    }
                }
            });

            navProductPanel.add(productPanel);
        }

        navProductPanel.revalidate();
        navProductPanel.repaint();
    }

    // Generate Invoice When Click Invoice
    private void generateInvoice() {
        double totalAmount = 0;

        try (PrintWriter writer = new PrintWriter("invoice.txt");
                FileWriter fileWriter = new FileWriter("recentlyBuy.txt", true);
                PrintWriter recentWriter = new PrintWriter(fileWriter)) {

            writer.println("Invoice:");
            writer.println("===========================================");

            // Check if the file is new and needs a header
            File recentlyBuyFile = new File("recentlyBuy.txt");
            if (recentlyBuyFile.length() == 0) {
                recentWriter.println("No.,Products Name,Vendor Name,Price,Qty.,Total,Discount,Net Total,Purchase Date");
            }

            for (int i = 0; i < paymentTableModel.getRowCount(); i++) {
                String name = (String) paymentTableModel.getValueAt(i, 1);
                double price = Double.parseDouble(paymentTableModel.getValueAt(i, 2).toString().substring(1));
                int qty = (int) paymentTableModel.getValueAt(i, 3);
                String discountText = (String) paymentTableModel.getValueAt(i, 4);
                double discount = Double.parseDouble(discountText.substring(0, discountText.length() - 1));
                double total = Double.parseDouble(paymentTableModel.getValueAt(i, 5).toString().substring(1));
                double netTotal = total - (total * discount / 100);
                String purchaseDate = new SimpleDateFormat("yyyy-MM-dd-H-m-s").format(new Date());

                writer.printf("%s - $%.2f x %d, Discount: %.2f%%, Total: $%.2f%n", name, price, qty, discount, total);
                recentWriter.printf("%d,%s,%s,%.2f,%d,%.2f,%.2f,%.2f,%s%n", i + 1, name, username, price, qty,
                        total, discount, netTotal, purchaseDate);
                totalAmount += total;
            }
            writer.println("===========================================");
            writer.printf("Total Amount: $%.2f%n", totalAmount);
            writer.println("Thank you for your purchase!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load the QR Code image from the PNG file
        BufferedImage qrCodeImage = null;
        try {
            qrCodeImage = ImageIO.read(new File("D:\\RUPP\\Java Programming\\RUPP\\src\\rupp\\images\\qr_code.png"));
            // Scale the image to 500x500
            if (qrCodeImage != null) {
                qrCodeImage = getScaledImage(qrCodeImage, 500, 500);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a custom dialog to show the success message, QR code, and total amount
        JDialog dialog = new JDialog(frame, "Invoice", true);
        dialog.setLayout(new BorderLayout());
        dialog.setBackground(Color.WHITE);

        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.setBackground(Color.WHITE);
        // Center the success message label
        JLabel successLabel = new JLabel("Invoice generated successfully!", SwingConstants.CENTER);
        JPanel successPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        successPanel.add(successLabel);
        dialog.add(successPanel, BorderLayout.NORTH);

        if (qrCodeImage != null) {
            JLabel qrLabel = new JLabel(new ImageIcon(qrCodeImage));
            dialogPanel.add(qrLabel, BorderLayout.CENTER);
        }

        JLabel totalLabel = new JLabel("Total to pay: $" + String.format("%.2f", totalAmount));
        totalLabel.setFont(font20B);
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dialogPanel.add(totalLabel, BorderLayout.SOUTH);

        JButton okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(100, 30)); // Set button size
        okButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Set cursor
        okButton.addActionListener(e -> dialog.dispose());
        okButton.setFont(font18B);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        dialog.add(dialogPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);

        // Update the inventory list
        updateProductList();
        updateInventoryTable();

        // Update the recent purchases panel
        updateRecentPurchasesPanel();
    }

    private BufferedImage getScaledImage(BufferedImage src, int width, int height) {
        BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(src, 0, 0, width, height, null);
        g2.dispose();
        return resizedImg;
    }

    // ViewSale Panel
    private JPanel createViewSale() {
        viewSale = new JPanel(new BorderLayout());
        JPanel headerPanel = navigation("View Sale");
        JPanel footerPanel = Footer();

        viewSale.add(headerPanel, BorderLayout.NORTH);
        viewSale.add(footerPanel, BorderLayout.SOUTH);
        return viewSale;

    }

    private void updateInventoryTable() {
        inventoryTableModel.setRowCount(0); // Clear the table

        // Re-populate the table with updated phone data
        for (Phone phone : phonesList) {
            inventoryTableModel.addProduct(phone); // Use the updated addProduct method
        }
    }

    public class InventoryTableModel extends DefaultTableModel {
        private final Class<?>[] columnTypes = new Class<?>[] {
                Integer.class, ImageIcon.class, String.class,
                Double.class, Integer.class, JButton.class, JButton.class
        };

        public InventoryTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnTypes[columnIndex];
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 5 || column == 6; // Allow editing for edit and delete button columns
        }

        public void addProduct(Phone phone) {
            int rowNum = getRowCount() + 1; // Get the next row number
            Object[] rowData = new Object[7];
            rowData[0] = rowNum;
            rowData[1] = new ImageIcon(phone.getImagePath()); // Assuming phone has a method getImagePath()
            rowData[2] = phone.getName();
            rowData[3] = phone.getPrice();
            rowData[4] = phone.getQty();
            rowData[5] = "Edit";
            rowData[6] = "Delete";
            addRow(rowData);
        }

        public void updateProduct(int row, Phone phone) {
            setValueAt(new ImageIcon(phone.getImagePath()), row, 1); // Assuming phone has a method getImagePath()
            setValueAt(phone.getName(), row, 2);
            setValueAt(phone.getPrice(), row, 3);
            setValueAt(phone.getQty(), row, 4);

            writeProductsToFile();
            updateProductList();
        }

        public void removeProduct(int row) {
            removeRow(row);
            updateRowNumbers(); // Update row numbers after removing a row
        }

        private void updateRowNumbers() {
            for (int i = 0; i < getRowCount(); i++) {
                setValueAt(i + 1, i, 0); // Update the "No." column
            }
        }
    }

    class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            if (value instanceof ImageIcon) {
                ImageIcon imageIcon = (ImageIcon) value;
                Image img = imageIcon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(img));
                setText("");
            } else {
                setIcon(null);
                setText(value != null ? value.toString() : "");
            }
            return this;
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setFont(font18); // Set your desired font
            setCursor(pointer);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            setText((value == null) ? "" : value.toString());
            setPreferredSize(new Dimension(70, 30)); // Adjust size as needed
            return this;
        }
    }

    class EditProductDialog extends JDialog {
        private final JTextField priceField;
        private final JTextField nameField;
        private final JTextField qtyField;
        private final JLabel imageLabel;
        private final JButton chooseImageButton;
        private final JButton saveButton;
        private final JButton cancelButton;
        private String imagePath;

        public EditProductDialog(JFrame parent, Phone phone) {
            super(parent, "Edit Product", true);
            setLayout(null);
            setSize(400, 440);

            // Set up components
            JLabel nameLabel = new JLabel("Name:");
            nameLabel.setFont(font18);
            JLabel priceLabel = new JLabel("Price:");
            priceLabel.setFont(font18);
            JLabel qtyLabel = new JLabel("Quantity:");
            qtyLabel.setFont(font18);
            nameField = new JTextField(phone.getName());
            nameField.setFont(font18);
            priceField = new JTextField(String.valueOf(phone.getPrice()));
            priceField.setFont(font18);
            qtyField = new JTextField(String.valueOf(phone.getQty()));
            qtyField.setFont(font18);
            imageLabel = new JLabel(new ImageIcon(
                    new ImageIcon(phone.getImagePath()).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
            imagePath = phone.getImagePath();
            chooseImageButton = new JButton("Choose Image");
            chooseImageButton.setFont(font18);
            chooseImageButton.setCursor(pointer);
            saveButton = new JButton("Save");
            saveButton.setFont(font18);
            saveButton.setCursor(pointer);
            cancelButton = new JButton("Cancel");
            cancelButton.setFont(font18);
            cancelButton.setCursor(pointer);
            // Set bounds for components
            nameLabel.setBounds(30, 30, 100, 30);
            nameField.setBounds(140, 30, 200, 30);
            priceLabel.setBounds(30, 80, 100, 30);
            priceField.setBounds(140, 80, 200, 30);
            qtyLabel.setBounds(30, 130, 100, 30);
            qtyField.setBounds(140, 130, 200, 30);
            imageLabel.setBounds(30, 180, 90, 90);
            chooseImageButton.setBounds(140, 210, 150, 30);
            saveButton.setBounds(30, 350, 120, 30);
            cancelButton.setBounds(220, 350, 120, 30);

            // Add components to the dialog
            add(nameLabel);
            add(nameField);
            add(priceLabel);
            add(priceField);
            add(qtyLabel);
            add(qtyField);
            add(imageLabel);
            add(chooseImageButton);
            add(saveButton);
            add(cancelButton);

            // Set up action listeners
            chooseImageButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    imagePath = selectedFile.getAbsolutePath();
                    imageLabel.setIcon(new ImageIcon(
                            new ImageIcon(imagePath).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
                }
            });

            saveButton.addActionListener(e -> {
                // Validation logic (if needed)
                writeProductsToFile();
                JOptionPane.showMessageDialog(frame, "Edit successfully!", "Edit",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            });

            cancelButton.addActionListener(e -> {
                dispose();
            });

            setLocationRelativeTo(parent);
        }

        public Phone getUpdatedPhone(Phone phone) {
            phone.setName(nameField.getText());
            phone.setPrice(Double.parseDouble(priceField.getText()));
            phone.setQty(Integer.parseInt(qtyField.getText()));
            phone.setImagePath(imagePath);

            return phone;
        }
    }

    private void deleteProductFromFile(Phone phone) {
        // Read the current list of products from the file
        List<Phone> phones = new ArrayList<>(phonesList);

        // Remove the phone from the list
        phones.removeIf(p -> p.getName().equals(phone.getName()) && p.getPrice() == phone.getPrice());

        // Write the updated list back to the file
        try (PrintWriter writer = new PrintWriter("products.txt")) {
            for (Phone p : phones) {
                writer.println(p.getName() + "," + p.getPrice() + "," + p.getQty() + "," + p.getImagePath());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private String label;
        private boolean isPushed;
        private final JTable table;
        private final JFrame parentFrame;

        public ButtonEditor(JCheckBox checkBox, JTable table, JFrame parentFrame) {
            super(checkBox);
            this.table = table;
            this.parentFrame = parentFrame;
            button = new JButton();
            button.setOpaque(true);
            button.setCursor(pointer);
            button.setFont(font18);
            button.addActionListener((ActionEvent e) -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int row = table.getSelectedRow();
                Phone phone = phonesList.get(row);
                if ("Edit".equals(label)) {
                    EditProductDialog dialog = new EditProductDialog(parentFrame, phone);
                    dialog.setVisible(true);
                    Phone updatedPhone = dialog.getUpdatedPhone(phone);
                    inventoryTableModel.updateProduct(row, updatedPhone);
                    updateDashboardPanels();
                } else if ("Delete".equals(label)) {
                    int response = JOptionPane.showConfirmDialog(
                            parentFrame,
                            "Are you sure you want to delete?",
                            "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        deleteProductFromFile(phone);
                        phonesList.remove(row);
                        inventoryTableModel.removeProduct(row);
                        updateDashboardPanels();
                    }
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    public void addProductToInventory(Phone phone) {
        inventoryTableModel.addProduct(phone);
    }

    public void updateProductInInventory(int row, Phone phone) {
        inventoryTableModel.updateProduct(row, phone);
    }

    public void removeProductFromInventory(int row) {
        inventoryTableModel.removeProduct(row);
    }

    // Inventory Panel
    private InventoryTableModel inventoryTableModel;
    private JTable inventoryTable;

    class PaddedCellRenderer extends DefaultTableCellRenderer {
        private final int padding;

        public PaddedCellRenderer(int padding) {
            this.padding = padding;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (component instanceof JComponent) {
                ((JComponent) component).setBorder(new EmptyBorder(padding, padding, padding, padding));
            }
            return component;
        }
    }

    // Custom renderer for the price column
    class PriceRenderer extends DefaultTableCellRenderer {
        private final NumberFormat currencyFormat;

        public PriceRenderer() {
            super();
            currencyFormat = NumberFormat.getCurrencyInstance();
        }

        @Override
        protected void setValue(Object value) {
            if (value != null) {
                value = currencyFormat.format(value);
            }
            super.setValue(value);
        }
    }

    private JPanel createInventoryPanel() {
        inventoryPanel = new JPanel(new BorderLayout());
        JPanel headerPanel = navigation("Inventory");
        JPanel footerPanel = Footer();

        String[] columnNames = { "No.", "Image", "Name", "Price", "Quantity", "Edit", "Delete" };
        inventoryTableModel = new InventoryTableModel(columnNames, 0);

        inventoryTable = new JTable(inventoryTableModel);
        inventoryTable.setFont(font18);
        inventoryTable.setRowHeight(90); // Set the row height to accommodate the image
        int padding = 10; // Adjust padding as needed
        PaddedCellRenderer paddedCellRenderer = new PaddedCellRenderer(padding);
        for (int i = 0; i < inventoryTable.getColumnCount(); i++) {
            inventoryTable.getColumnModel().getColumn(i).setCellRenderer(paddedCellRenderer);
        }

        inventoryTable.getColumn("No.").setPreferredWidth(30); // Adjust preferred width for the "No." column
        inventoryTable.getColumn("No.").setCellRenderer(new PaddedCellRenderer(padding)); // Apply padding to the "No."
                                                                                          // column
        inventoryTable.getColumn("Image").setCellRenderer(new ImageRenderer());
        inventoryTable.getColumn("Edit").setCellRenderer(new ButtonRenderer());
        inventoryTable.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox(), inventoryTable,
                (JFrame) SwingUtilities.getWindowAncestor(inventoryPanel)));
        inventoryTable.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        inventoryTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), inventoryTable,
                (JFrame) SwingUtilities.getWindowAncestor(inventoryPanel)));

        inventoryTable.getColumn("Price").setCellRenderer(new PriceRenderer());
        // Set font for table headers
        JTableHeader header = inventoryTable.getTableHeader();
        header.setFont(font20B); // Set desired font and size for headers

        JScrollPane inventoryScrollPane = new JScrollPane(inventoryTable);

        // Add padding around the scroll pane
        inventoryScrollPane.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));

        // Populate the table with initial phone data
        for (Phone phone : phonesList) {
            inventoryTableModel.addProduct(phone); // Use the updated addProduct method
        }

        inventoryPanel.add(headerPanel, BorderLayout.NORTH);
        inventoryPanel.add(inventoryScrollPane, BorderLayout.CENTER);
        inventoryPanel.add(footerPanel, BorderLayout.SOUTH);

        return inventoryPanel;
    }

    // Show Add Product Dialog
    private void showAddProductDialog() {
        JDialog dialog = new JDialog(frame, "Add Product", true);

        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(frame);
        dialog.setLayout(null);

        JLabel lblName = new JLabel("Product Name:");
        JTextField txtName = new JTextField();
        JLabel lblPrice = new JLabel("Price:");
        JTextField txtPrice = new JTextField();
        JLabel lblQty = new JLabel("Quantity:");
        JTextField txtQty = new JTextField();
        JLabel lblImagePath = new JLabel("Image Path:");
        JTextField txtImagePath = new JTextField();
        JButton btnBrowse = new JButton("Browse");
        JButton btnAdd = new JButton("Add");
        JButton btnCancel = new JButton("Cancel");

        // Set bounds for components
        lblName.setBounds(30, 30, 100, 30);
        txtName.setBounds(150, 30, 200, 30);
        lblPrice.setBounds(30, 70, 100, 30);
        txtPrice.setBounds(150, 70, 200, 30);
        lblQty.setBounds(30, 110, 200, 30);
        txtQty.setBounds(150, 110, 200, 30);
        lblImagePath.setBounds(30, 150, 100, 30);
        txtImagePath.setBounds(150, 150, 200, 30);
        btnBrowse.setBounds(360, 150, 80, 30);
        btnBrowse.setCursor(pointer);
        btnAdd.setBounds(150, 210, 80, 30);
        btnAdd.setCursor(pointer);
        btnCancel.setBounds(240, 210, 80, 30);
        btnCancel.setCursor(pointer);

        dialog.add(lblName);
        dialog.add(txtName);
        dialog.add(lblPrice);
        dialog.add(txtPrice);
        dialog.add(lblQty);
        dialog.add(txtQty);
        dialog.add(lblImagePath);
        dialog.add(txtImagePath);
        dialog.add(btnBrowse);
        dialog.add(btnAdd);
        dialog.add(btnCancel);

        btnBrowse.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                txtImagePath.setText(selectedFile.getAbsolutePath());
            }
        });

        btnAdd.addActionListener((ActionEvent e) -> {
            try {
                String name = txtName.getText();
                double price = Double.parseDouble(txtPrice.getText());
                int qty = Integer.parseInt(txtQty.getText());
                String imagePath = txtImagePath.getText();

                // Create a new Phone object
                Phone newPhone = new Phone(name, price, qty, imagePath);

                // Add the new phone to the list
                phonesList.add(newPhone);

                // Update the inventory
                addProductToInventory(newPhone);

                // Update the product list display
                updateProductList();

                // Write the updated product list to file
                writeProductsToFile();

                // Update the dashboard panels
                updateDashboardPanels();

                // Close the dialog
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input. Please check your entries.");
            }
        });

        btnCancel.addActionListener((ActionEvent e) -> {
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    // Report
    private void creatReportPanel() {
        report = new JPanel(new BorderLayout()); // Use BorderLayout for proper alignment
        // Get navigation header panel
        JPanel headerPanel = navigation("Report");
        JPanel footerPanel = Footer();

        report.add(headerPanel, BorderLayout.NORTH);
        report.add(footerPanel, BorderLayout.SOUTH);

    }

    // Setting Panel
    private void createSettingPanel() {
        setting = new JPanel(new BorderLayout()); // Use BorderLayout for proper alignment
        // Get navigation header panel
        JPanel headerPanel = navigation("Settings");
        JPanel footerPanel = Footer();

        setting.add(headerPanel, BorderLayout.NORTH);
        setting.add(footerPanel, BorderLayout.SOUTH);

    }

    // Read Products From File
    private void readProductsFromFile() {
        phonesList.clear();
        File file = new File("products.txt");
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String[] productData = scanner.nextLine().split(",");
                    if (productData.length == 4) {
                        String name = productData[0];
                        double price = Double.parseDouble(productData[1]);
                        int qty = Integer.parseInt(productData[2]);
                        String imagePath = productData[3];
                        phonesList.add(new Phone(name, price, qty, imagePath));
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // Write Products To File
    private void writeProductsToFile() {
        try (PrintWriter writer = new PrintWriter("products.txt")) {
            for (Phone phone : phonesList) {
                writer.printf("%s,%f,%d,%s%n", phone.getName(), phone.getPrice(), phone.getQty(), phone.getImagePath());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard("DefaultUser"));
    }

    public void dispose() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'dispose'");
    }

}
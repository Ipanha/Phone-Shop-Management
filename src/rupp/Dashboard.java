package rupp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class Dashboard {
    private final JFrame frame;
    private final JPanel mainPanel;
    private final CardLayout cardLayout;

    private JPanel dashboard;
    private JPanel newsalePanel;
    private JTable paymentTable;
    private DefaultTableModel paymentTableModel;
    private final ArrayList<Phone> phonesList;
    private JPanel navProductPanel;
    private JScrollPane scrollPane;

    public Dashboard() {
        frame = new JFrame("Phone Shop Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        phonesList = new ArrayList<>();
        readProductsFromFile();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        frame.add(mainPanel);

        createDashboardPanel();
        mainPanel.add(dashboard, "Dashboard");
        createNewSalePanel();
        mainPanel.add(newsalePanel, "NewSale");

        createSidebar();

        frame.setVisible(true);
        updateProductList();
    }

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

    private void createDashboardPanel() {
        dashboard = new JPanel(new BorderLayout());
        updateDashboardPanels();
    }

    private void updateDashboardPanels() {
        dashboard.removeAll();
        updateSummaryPanel();
        updateRecentPurchasesPanel();
        updateTopProductsPanel();
        dashboard.revalidate();
        dashboard.repaint();
    }

    private void navigation() {

    }

    private void updateSummaryPanel() {
        // Ensure the products are loaded into phonesList
        readProductsFromFile();
        Font font1 = new Font("Arial", Font.BOLD, 24);
        // Create a navigation title
        JLabel navTitle = new JLabel("Dashboard");
        navTitle.setFont(font1);
        navTitle.setForeground(Color.BLUE);
        navTitle.setHorizontalAlignment(SwingConstants.CENTER);
        navTitle.setBorder(new EmptyBorder(10, 10, 10, 10));

        // User info panel (right side)
        JPanel userInfoPanel = new JPanel(new BorderLayout());
        userInfoPanel.setBackground(Color.WHITE);
        userInfoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Simulated user data (replace with actual user data retrieval logic)
        ImageIcon userLogo = new ImageIcon("D:\\RUPP\\Java Programming\\RUPP\\src\\rupp\\images\\p1.png");
        // Scale the image to the desired size (e.g., 50x50 pixels)
        Image scaledImage = userLogo.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        // Create a new ImageIcon with the scaled image
        ImageIcon scaledUserLogo = new ImageIcon(scaledImage);
        String userName = "CHET PANHA"; // Replace with actual user name

        // User info components
        JLabel userLogoLabel = new JLabel(scaledUserLogo);
        userLogoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        userLogoLabel.setToolTipText("Click to view profile");

        // userLogoLabel.addMouseListener(new MouseAdapter() {
        // @Override
        // public void mouseClicked(MouseEvent e) {
        // showProfileDialog(userName, scaledUserLogo);
        // }
        // });

        JButton userNamebtn = new JButton(userName);
        userNamebtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        userNamebtn.setBorderPainted(false); // Remove border
        userNamebtn.setFocusPainted(false); // Remove focus border
        userNamebtn.setContentAreaFilled(false); // Remove background

        userNamebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Image scaledImage = userLogo.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                // Create a new ImageIcon with the scaled image
                ImageIcon scaledUserLogo = new ImageIcon(scaledImage);
                // userNamebtn.setSize(140);
                showProfileDialog(userName, scaledUserLogo);
            }
        });

        // Add components to user info panel
        userInfoPanel.add(userLogoLabel, BorderLayout.WEST);
        userInfoPanel.add(userNamebtn, BorderLayout.CENTER);

        // Header Panel (navigation title + user info panel)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(navTitle, BorderLayout.WEST);
        headerPanel.add(Box.createRigidArea(new Dimension(20, 0))); // Add space between navTitle and userInfoPanel
        headerPanel.add(userInfoPanel, BorderLayout.EAST);
        Font font2 = new Font("Arial", Font.BOLD, 20);
        // Summary Panel
        JPanel summaryPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        summaryPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        int totalProductsCount = phonesList.size();
        int lowStockCount = (int) phonesList.stream().filter(p -> p.getQty() > 0 && p.getQty() < 5).count();
        int outOfStockCount = (int) phonesList.stream().filter(p -> p.getQty() == 0).count();
        Phone mostStockProduct = phonesList.stream()
                .max((p1, p2) -> Integer.compare(p1.getQty(), p2.getQty())).orElse(null);

        JLabel totalProducts = createSummaryLabel("Total Products", String.valueOf(totalProductsCount), Color.ORANGE);
        totalProducts.setFont(font2);
        JLabel lowStockProducts = createSummaryLabel("Low Stock Products", String.valueOf(lowStockCount),
                Color.MAGENTA);
        lowStockProducts.setFont(font2);
        JLabel outOfStockProducts = createSummaryLabel("Out of Stock Products", String.valueOf(outOfStockCount),
                Color.RED);
        outOfStockProducts.setFont(font2);
        JLabel mostStockProductLabel = createSummaryLabel("Most Stock Product",
                mostStockProduct != null ? mostStockProduct.getName() : "N/A", Color.GREEN);
        mostStockProductLabel.setFont(font2);

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
        int dialogHeight = profileDialog.getSize().height;
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
        profileButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        Font buttonFont = new Font(profileButton.getFont().getName(), Font.BOLD, 18);
        profileButton.setFont(buttonFont); // Set the font for profileButton
        profileButton.setPreferredSize(new Dimension(120, 40)); // Set preferred size
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle profile button action (if needed)
            }
        });
        buttonPanel.add(profileButton);

        // Add space between buttons
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Add 10 pixels of horizontal space

        // Add sign out button
        JButton signOutButton = new JButton("Sign Out");
        signOutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signOutButton.setFont(buttonFont);
        signOutButton.setPreferredSize(new Dimension(120, 40)); // Set preferred size
        signOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profileDialog.dispose();
                frame.dispose();
                SwingUtilities.invokeLater(() -> new PhoneShopManagementSystem());
            }
        });
        buttonPanel.add(signOutButton);

        // Add panels to dialog
        profileDialog.add(userInfoPanel, BorderLayout.CENTER);
        profileDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Show dialog
        profileDialog.setVisible(true);
    }

    private void updateRecentPurchasesPanel() {
        JPanel recentPurchasePanel = new JPanel(new BorderLayout());

        // Create a custom font for the title
        Font titleFont = new Font("Poppins", Font.BOLD, 20);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Recent Purchase Invoice");
        titledBorder.setTitleFont(titleFont);
        recentPurchasePanel.setBorder(titledBorder);

        // Updated columns
        String[] recentColumns = { "No.", "Products Name", "Vendor Name", "Price", "Qty.", "Total", "Discount",
                "Net Total", "Purchase Date" };
        Object[][] recentData = {
                { "1", "P00019S", "Mike", 1500, 10, 1510, 100, 1410, "2024/07/01" },
                { "1", "P00019S", "Mike", 1500, 10, 1510, 100, 1410, "2024/07/01" },
                { "1", "P00019S", "Mike", 1500, 10, 1510, 100, 1410, "2024/07/01" },
                { "1", "P00019S", "Mike", 1500, 10, 1510, 100, 1410, "2024/07/01" },
                { "1", "P00019S", "Mike", 1500, 10, 1510, 100, 1410, "2024/07/01" },
                { "1", "P00019S", "Mike", 1500, 10, 1510, 100, 1410, "2024/07/01" },
                { "1", "P00019S", "Mike", 1500, 10, 1510, 100, 1410, "2024/07/01" },
                { "1", "P00019S", "Mike", 1500, 10, 1510, 100, 1410, "2024/07/01" },
                { "1", "P00019S", "Mike", 1500, 10, 1510, 100, 1410, "2024/07/01" },
                { "1", "P00019S", "Mike", 1500, 10, 1510, 100, 1410, "2024/07/01" },
                { "1", "P00019S", "Mike", 1500, 10, 1510, 100, 1410, "2024/07/01" },
                { "1", "P00019S", "Mike", 1500, 10, 1510, 100, 1410, "2024/07/01" },
                { "1", "P00019S", "Mike", 1500, 10, 1510, 100, 1410, "2024/07/01" },
                { "1", "P00019S", "Mike", 1500, 10, 1510, 100, 1410, "2024/07/01" },

        };

        JTable recentTable = new JTable(recentData, recentColumns);

        // Set font size for table cells
        Font tableFont = new Font("Poppins", Font.PLAIN, 18);
        recentTable.setFont(tableFont);
        recentTable.setRowHeight(30);

        // Set font size for table header
        JTableHeader tableHeader = recentTable.getTableHeader();
        tableHeader.setFont(new Font("Poppins", Font.BOLD, 20));

        JScrollPane recentScrollPane = new JScrollPane(recentTable);
        recentPurchasePanel.add(recentScrollPane, BorderLayout.CENTER);

        dashboard.add(recentPurchasePanel, BorderLayout.CENTER);
    }

    private void updateTopProductsPanel() {
        JPanel topProductsPanel = new JPanel(new BorderLayout());

        // Create a custom font for the title
        Font titleFont = new Font("Poppins", Font.BOLD, 20);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Top Purchase Product");
        titledBorder.setTitleFont(titleFont);
        topProductsPanel.setBorder(titledBorder);

        String[] topProductsColumns = { "No.", "Product Name", "Price", "Qty.", "Total", "Total" };
        Object[][] topProductsData = {
                { "509-GRPH", "Kaplan Melton Coat Navy", "Jackets", 50, 50, 2500 },
                { "307-CARB", "Patch Rugger LS Shirt Taupe", "Shirts", 10, 100, 1000 },
                { "409-CARB", "Waffle Hood Knit Olive", "Jackets", 15, 20, 300 },
                { "489-RTLC", "Red Textured Leather Cardholder", "Card Holder", 20, 10, 200 },
                { "489-RTLC", "Red Textured Leather Cardholder", "Card Holder", 20, 10, 200 },
                { "489-RTLC", "Red Textured Leather Cardholder", "Card Holder", 20, 10, 200 },
                { "489-RTLC", "Red Textured Leather Cardholder", "Card Holder", 20, 10, 200 },
                { "489-RTLC", "Red Textured Leather Cardholder", "Card Holder", 20, 10, 200 },
                { "489-RTLC", "Red Textured Leather Cardholder", "Card Holder", 20, 10, 200 },
                { "489-RTLC", "Red Textured Leather Cardholder", "Card Holder", 20, 10, 200 },
                { "489-RTLC", "Red Textured Leather Cardholder", "Card Holder", 20, 10, 200 },
                { "489-RTLC", "Red Textured Leather Cardholder", "Card Holder", 20, 10, 200 },
                { "489-RTLC", "Red Textured Leather Cardholder", "Card Holder", 20, 10, 200 },
                { "489-RTLC", "Red Textured Leather Cardholder", "Card Holder", 20, 10, 200 },
                { "489-RTLC", "Red Textured Leather Cardholder", "Card Holder", 20, 10, 200 },
        };

        JTable topProductsTable = new JTable(topProductsData, topProductsColumns);

        // Set font size for table cells
        Font tableFont = new Font("Poppins", Font.PLAIN, 18);
        topProductsTable.setFont(tableFont);
        topProductsTable.setRowHeight(30);

        // Set font size for table header
        JTableHeader tableHeader = topProductsTable.getTableHeader();
        tableHeader.setFont(new Font("Poppins", Font.BOLD, 20));

        JScrollPane topProductsScrollPane = new JScrollPane(topProductsTable);
        topProductsPanel.add(topProductsScrollPane, BorderLayout.CENTER);

        dashboard.add(topProductsPanel, BorderLayout.SOUTH);
    }

    private void createNewSalePanel() {
        newsalePanel = new JPanel(null);

        JLabel lblPayment = new JLabel("Payment");
        JPanel navPaymentPanel = new JPanel();
        JLabel lblProduct = new JLabel("List Product");
        JPanel navListProductPanel = new JPanel();
        navProductPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        scrollPane = new JScrollPane(navProductPanel);
        JButton btnClear = new JButton("Clear all");
        JButton btnExit = new JButton("Exit");
        JButton btnInvoice = new JButton("Invoice");
        JTextArea txtDisplay = new JTextArea();
        JButton btnBack = new JButton("Back");

        Font labelFont1 = new Font("Arial", Font.PLAIN, 40);
        Font labelFont2 = new Font("Arial", Font.PLAIN, 30);
        Font labelFont4 = new Font("Arial", Font.PLAIN, 17);

        // Product side
        lblPayment.setBounds(350, 30, 800, 50);
        lblPayment.setFont(labelFont1);
        lblPayment.setForeground(Color.BLUE);
        navPaymentPanel.setBounds(10, 10, 800, 100);
        navPaymentPanel.setBackground(Color.white);

        // Payment side
        String[] columnNames = { "No.", "Name", "Price", "Qty", "Total" };
        paymentTableModel = new DefaultTableModel(columnNames, 0);
        paymentTable = new JTable(paymentTableModel);
        JScrollPane paymentScrollPane = new JScrollPane(paymentTable);
        paymentScrollPane.setBounds(10, 120, 800, 700);
        JTableHeader header = paymentTable.getTableHeader();
        header.setFont(labelFont4);
        paymentTable.setFont(labelFont4);
        paymentTable.setRowHeight(30);

        // Create padding for text display
        int top = 10, left = 10, bottom = 10, right = 10;
        EmptyBorder padding = new EmptyBorder(top, left, bottom, right);
        txtDisplay.setBorder(padding);

        // Buttons
        btnBack.setBounds(10, 900, 150, 40);
        btnBack.setFont(labelFont2);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClear.setBounds(220, 900, 150, 40);
        btnClear.setFont(labelFont2);
        btnClear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInvoice.setBounds(440, 900, 150, 40);
        btnInvoice.setFont(labelFont2);
        btnInvoice.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExit.setBounds(650, 900, 150, 40);
        btnExit.setFont(labelFont2);
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Product side
        lblProduct.setBounds(1200, 30, 500, 50);
        lblProduct.setFont(labelFont1);
        lblProduct.setForeground(Color.BLUE);
        scrollPane.setBounds(840, 120, 850, 700);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        navListProductPanel.setBounds(840, 10, 850, 100);
        navListProductPanel.setBackground(Color.white);

        // Assuming scrollPane is your JScrollPane object
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();

        // Set the unit increment to a higher value for faster scrolling
        verticalScrollBar.setUnitIncrement(16);
        horizontalScrollBar.setUnitIncrement(16);

        // Optionally, you can also set the block increment for even faster scrolling
        verticalScrollBar.setBlockIncrement(100);
        horizontalScrollBar.setBlockIncrement(100);

        // Add products
        updateProductList();

        // Adding components to dashboard panel
        newsalePanel.add(lblPayment);
        newsalePanel.add(navPaymentPanel);
        newsalePanel.add(paymentScrollPane);
        newsalePanel.add(btnBack);
        newsalePanel.add(btnClear);
        newsalePanel.add(btnExit);
        newsalePanel.add(btnInvoice);
        newsalePanel.add(lblProduct);
        newsalePanel.add(navListProductPanel);
        newsalePanel.add(scrollPane);
        newsalePanel.setBackground(Color.cyan);

        // Button actions
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new PhoneShopManagementSystem();
            }
        });
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paymentTableModel.setRowCount(0);
            }
        });

        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        btnInvoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateInvoice();
            }
        });
    }

    private void createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(null);
        sidebar.setBackground(Color.LIGHT_GRAY);
        sidebar.setPreferredSize(new Dimension(200, frame.getHeight()));

        ImageIcon originalImg = new ImageIcon("D:\\RUPP\\Java Programming\\RUPP\\src\\rupp\\logo.png");
        Image img = originalImg.getImage();
        Image resizedImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Resize to 200x200 pixels
        ImageIcon resizedIcon = new ImageIcon(resizedImg);
        JLabel imageLabel = new JLabel(resizedIcon);
        imageLabel.setBounds(50, 20, resizedIcon.getIconWidth(), resizedIcon.getIconHeight());

        JButton btnAddProduct = new JButton("Add Product");
        JButton btnViewInventory = new JButton("View Inventory");
        JButton btnNewSale = new JButton("New Sale");
        JButton btnViewSales = new JButton("View Sales");
        JButton btnAddCustomer = new JButton("Add Customer");
        JButton btnViewCustomers = new JButton("View Customers");
        JButton btnGenerateReport = new JButton("Generate Report");
        JButton btnSettings = new JButton("Settings");
        JButton btnDashboard = new JButton("Dashboard");

        Font Font20 = new Font("Arial", Font.BOLD, 15);
        Cursor cur = new Cursor(Cursor.HAND_CURSOR);
        btnDashboard.setBounds(20, 140, 160, 40);
        btnDashboard.setFont(Font20);
        btnDashboard.setCursor(cur);
        btnNewSale.setBounds(20, 190, 160, 40);
        btnNewSale.setFont(Font20);
        btnNewSale.setCursor(cur);
        btnViewSales.setBounds(20, 240, 160, 40);
        btnViewSales.setFont(Font20);
        btnViewSales.setCursor(cur);
        btnAddProduct.setBounds(20, 290, 160, 40);
        btnAddProduct.setFont(Font20);
        btnAddProduct.setCursor(cur);
        btnViewInventory.setBounds(20, 340, 160, 40);
        btnViewInventory.setFont(Font20);
        btnViewInventory.setCursor(cur);
        btnGenerateReport.setBounds(20, 390, 160, 40);
        btnGenerateReport.setFont(Font20);
        btnGenerateReport.setCursor(cur);
        btnSettings.setBounds(20, 440, 160, 40);
        btnSettings.setFont(Font20);
        btnSettings.setCursor(cur);

        sidebar.add(imageLabel);
        sidebar.add(btnDashboard);
        sidebar.add(btnNewSale);
        sidebar.add(btnViewSales);
        sidebar.add(btnAddProduct);
        sidebar.add(btnViewInventory);
        sidebar.add(btnAddCustomer);
        sidebar.add(btnViewCustomers);
        sidebar.add(btnGenerateReport);
        sidebar.add(btnSettings);
        frame.add(sidebar, BorderLayout.WEST);

        btnAddProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddProductDialog();
            }
        });

        btnViewInventory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel inventoryPanel = createInventoryPanel();
                mainPanel.add(inventoryPanel, "Inventory");
                cardLayout.show(mainPanel, "Inventory");
            }
        });

        btnDashboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Dashboard");
                updateProductList();
            }
        });
        btnNewSale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "NewSale");
            }
        });

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
            JButton addToCartButton = new JButton("Add To Cart");
            addToCartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            priceLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            nameLabel.setFont(new Font("Arial", Font.BOLD, 20));

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
            productPanel.add(addToCartButton, gbc);

            productPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            productPanel.setBackground(Color.WHITE);
            productPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            addToCartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Ask for quantity
                    int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter Quantity:"));
                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(frame, "Quantity must be greater than zero.");
                        return;
                    }
                    if (quantity > phone.getQty()) {
                        JOptionPane.showMessageDialog(frame, "Not enough stock available.");
                        return;
                    }

                    // Calculate total
                    double price = phone.getPrice();
                    double total = price * quantity;

                    // Add to payment table
                    Object[] rowData = { paymentTableModel.getRowCount() + 1, phone.getName(), price, quantity, total };
                    paymentTableModel.addRow(rowData);

                    // Update phone quantity
                    phone.setQty(phone.getQty() - quantity);

                    // Update product list display
                    updateProductList();
                }
            });

            navProductPanel.add(productPanel);
        }

        navProductPanel.revalidate();
        navProductPanel.repaint();
    }

    private void generateInvoice() {
        try (PrintWriter writer = new PrintWriter("invoice.txt")) {
            writer.println("Invoice:");
            writer.println("===========================================");
            for (int i = 0; i < paymentTableModel.getRowCount(); i++) {
                String name = (String) paymentTableModel.getValueAt(i, 1);
                double price = (double) paymentTableModel.getValueAt(i, 2);
                int quantity = (int) paymentTableModel.getValueAt(i, 3);
                double total = (double) paymentTableModel.getValueAt(i, 4);

                writer.printf("%s - $%.2f x %d = $%.2f%n", name, price, quantity, total);
            }
            writer.println("===========================================");
            writer.println("Thank you for your purchase!");
            JOptionPane.showMessageDialog(frame, "Invoice generated successfully!", "Invoice",
                    JOptionPane.INFORMATION_MESSAGE);

            writeProductsToFile();

            // Update the dashboard panels after generating the invoice
            updateDashboardPanels();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // InventoryPanel
    private JPanel createInventoryPanel() {
        JPanel inventoryPanel = new JPanel(new BorderLayout());

        String[] columnNames = { "Name", "Price", "Quantity" };
        DefaultTableModel inventoryTableModel = new DefaultTableModel(columnNames, 0);

        JTable inventoryTable = new JTable(inventoryTableModel);
        JScrollPane inventoryScrollPane = new JScrollPane(inventoryTable);

        // Populate the table with phone data
        for (Phone phone : phonesList) {
            Object[] rowData = new Object[3];
            rowData[0] = phone.getName();
            rowData[1] = phone.getPrice();
            rowData[2] = phone.getQty();
            inventoryTableModel.addRow(rowData);
        }

        inventoryPanel.add(inventoryScrollPane, BorderLayout.CENTER);

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Dashboard");
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnBack);

        inventoryPanel.add(bottomPanel, BorderLayout.SOUTH);

        return inventoryPanel;
    }

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
        btnBrowse.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setBounds(150, 210, 80, 30);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.setBounds(240, 210, 80, 30);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

        btnBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    txtImagePath.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = txtName.getText();
                    double price = Double.parseDouble(txtPrice.getText());
                    int qty = Integer.parseInt(txtQty.getText());
                    String imagePath = txtImagePath.getText();

                    // Create a new Phone object
                    Phone newPhone = new Phone(name, price, qty, imagePath);

                    // Add the new phone to the list
                    phonesList.add(newPhone);

                    // Update the product list display
                    updateProductList();
                    // Write the updated product list to file
                    writeProductsToFile();
                    // Update the dashboard panels after generating the invoice
                    updateDashboardPanels();
                    // Close the dialog
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid input. Please check your entries.");
                }
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }

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
        SwingUtilities.invokeLater(() -> new Dashboard());
    }
}
package rupp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class Dashboard {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel dashboardPanel;
    private JMenuBar menuBar;
    private JMenu menuInventory, menuSales, menuCustomers, menuReports, menuSettings;
    private JMenuItem menuItemAddProduct, menuItemViewInventory, menuItemNewSale, menuItemViewSales;
    private JMenuItem menuItemAddCustomer, menuItemViewCustomers, menuItemGenerateReport;
    private JTable paymentTable;
    private DefaultTableModel paymentTableModel;

    public Dashboard() {
        frame = new JFrame("Phone Shop Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        frame.add(mainPanel);
        createDashboardPanel();
        mainPanel.add(dashboardPanel, "Dashboard");
        frame.setVisible(true);
    }

    private void createDashboardPanel() {
        dashboardPanel = new JPanel(null);

        JLabel lblPayment = new JLabel("Payment");
        JPanel navPaymentPanel = new JPanel();
        JLabel lblProduct = new JLabel("List Product");
        JPanel navListProductPanel = new JPanel();
        JPanel navProductPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        JScrollPane scrollPane = new JScrollPane(navProductPanel);
        JButton btnClear = new JButton("Clear all");
        JButton btnExit = new JButton("Exit");
        JButton btnInvoice = new JButton("Invoice");
        JTextArea txtDisplay = new JTextArea();

        Font labelFont1 = new Font("Arial", Font.PLAIN, 40);
        Font labelFont2 = new Font("Arial", Font.PLAIN, 30);
        Font labelFont3 = new Font("Arial", Font.PLAIN, 20);
        Font labelFont4 = new Font("Arial", Font.PLAIN, 17);

        // Payment side
        lblPayment.setBounds(370, 30, 500, 50);
        lblPayment.setFont(labelFont1);
        lblPayment.setForeground(Color.BLUE);
        navPaymentPanel.setBounds(10, 10, 900, 100);
        navPaymentPanel.setBackground(Color.white);
        
        // Use a JTable for the payment details
        String[] columnNames = {"No.", "Name", "Price", "Qty", "Total"};
        paymentTableModel = new DefaultTableModel(columnNames, 0);
        paymentTable = new JTable(paymentTableModel);
        JScrollPane paymentScrollPane = new JScrollPane(paymentTable);
        paymentScrollPane.setBounds(10, 120, 900, 700);
        JTableHeader header = paymentTable.getTableHeader();
        header.setFont(labelFont4);
        paymentTable.setFont(labelFont4);
        paymentTable.setRowHeight(30);

        // Create padding for text display
        int top = 10, left = 10, bottom = 10, right = 10;
        EmptyBorder padding = new EmptyBorder(top, left, bottom, right);
        txtDisplay.setBorder(padding);

        // Buttons
        btnClear.setBounds(80, 900, 150, 40);
        btnClear.setFont(labelFont2);
        btnExit.setBounds(750, 900, 150, 40);
        btnExit.setFont(labelFont2);
        btnInvoice.setBounds(400, 900, 150, 40);
        btnInvoice.setFont(labelFont2);

        // Product side
        lblProduct.setBounds(1350, 30, 500, 50);
        lblProduct.setFont(labelFont1);
        lblProduct.setForeground(Color.BLUE);
        scrollPane.setBounds(990, 120, 900, 700);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        navListProductPanel.setBounds(990, 10, 900, 100);
        navListProductPanel.setBackground(Color.white);


        // Sample phone data
        Phone[] phones = {
            new Phone("iPhone 12", 559.00, "D:/RUPP/Java Programming/RUPP/src/rupp/images/Iphone-12.png"),
            new Phone("iPhone 12 Pro LL/A", 659.00, "D:/RUPP/Java Programming/RUPP/src/rupp/images/iPhone-12-Pro.png"),
            new Phone("iPhone 12 Pro Max LL/A", 759.00, "D:/RUPP/Java Programming/RUPP/src/rupp/images/iPhone-12-Pro_Max.png"),
            new Phone("iPhone 13 128G", 619.00, "D:/RUPP/Java Programming/RUPP/src/rupp/images/iPhone-13.png"),
            new Phone("iPhone 13 Pro 128G", 719.00, "D:/RUPP/Java Programming/RUPP/src/rupp/images/iPhone-13-Pro.png"),
            new Phone("iPhone 13 Pro Max 128G USA (New)", 859.00, "D:/RUPP/Java Programming/RUPP/src/rupp/images/iPhone-13-Pro-Max.png"),
            new Phone("iPhone 14 ZP/A", 789.00, "D:/RUPP/Java Programming/RUPP/src/rupp/images/iPhone-14.png"),
            new Phone("iPhone 14 Pro 128G", 999.00, "D:/RUPP/Java Programming/RUPP/src/rupp/images/iPhone-14-Pro.png"),
            new Phone("iPhone 14 Pro MAX LL/A", 1049.00, "D:/RUPP/Java Programming/RUPP/src/rupp/images/iPhone-14-Pro-Max.png"),
            new Phone("iPhone 15 Plus", 919.00, "D:/RUPP/Java Programming/RUPP/src/rupp/images/Iphone-15.png"),
            new Phone("iPhone 15 Pro USA", 999.00, "D:/RUPP/Java Programming/RUPP/src/rupp/images/Iphone-15-Pro.png"),
            new Phone("iPhone 15 Pro Max", 1269.00, "D:/RUPP/Java Programming/RUPP/src/rupp/images/Iphone-15-Pro-Max.png"),
        };

        // Add products
        for (Phone phone : phones) {
            JPanel productPanel = new JPanel();
            productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));

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

            JLabel priceLabel = new JLabel("$" + phone.getPrice());
            JLabel nameLabel = new JLabel(phone.getName());
            JButton addToCartButton = new JButton("Add To Cart");

            picLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            priceLabel.setFont(labelFont3);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            nameLabel.setFont(labelFont4);
            addToCartButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            productPanel.add(Box.createVerticalStrut(10));
            productPanel.add(picLabel);
            productPanel.add(Box.createVerticalStrut(10));
            productPanel.add(priceLabel);
            productPanel.add(nameLabel);
            productPanel.add(Box.createVerticalStrut(10));
            productPanel.add(addToCartButton);

            navProductPanel.add(productPanel);

            addToCartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addPhoneToCart(phone);
                }
            });
        }

        // Adding components to dashboard panel
        dashboardPanel.add(lblPayment);
        dashboardPanel.add(navPaymentPanel);
        dashboardPanel.add(paymentScrollPane);
        dashboardPanel.add(btnClear);
        dashboardPanel.add(btnExit);
        dashboardPanel.add(btnInvoice);
        dashboardPanel.add(lblProduct);
        dashboardPanel.add(navListProductPanel);
        dashboardPanel.add(scrollPane);

        dashboardPanel.setBackground(Color.cyan);

        // Add menu bar
        menuBar = new JMenuBar();


        menuInventory = new JMenu("Inventory");
        menuItemAddProduct = new JMenuItem("Add Product");
        menuItemViewInventory = new JMenuItem("View Inventory");
        menuInventory.add(menuItemAddProduct);
        menuInventory.add(menuItemViewInventory);

        menuSales = new JMenu("Sales");
        menuItemNewSale = new JMenuItem("New Sale");
        menuItemViewSales = new JMenuItem("View Sales");
        menuSales.add(menuItemNewSale);
        menuSales.add(menuItemViewSales);

        menuCustomers = new JMenu("Customers");
        menuItemAddCustomer = new JMenuItem("Add Customer");
        menuItemViewCustomers = new JMenuItem("View Customers");
        menuCustomers.add(menuItemAddCustomer);
        menuCustomers.add(menuItemViewCustomers);

        menuReports = new JMenu("Reports");
        menuItemGenerateReport = new JMenuItem("Generate Report");
        menuReports.add(menuItemGenerateReport);

        menuSettings = new JMenu("Settings");

        menuBar.add(menuInventory);
        menuBar.add(menuSales);
        menuBar.add(menuCustomers);
        menuBar.add(menuReports);
        menuBar.add(menuSettings);

        frame.setJMenuBar(menuBar);

        // Button actions
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

    private void addPhoneToCart(Phone phone) {
    boolean found = false;
    int rowCount = paymentTableModel.getRowCount();
    for (int i = 0; i < rowCount; i++) {
        if (paymentTableModel.getValueAt(i, 1).equals(phone.getName())) {
            int qty = (int) paymentTableModel.getValueAt(i, 3);
            double price = (double) paymentTableModel.getValueAt(i, 2);
            paymentTableModel.setValueAt(qty + 1, i, 3);
            paymentTableModel.setValueAt(price * (qty + 1), i, 4);
            found = true;
            break;
        }
    }
    if (!found) {
        Object[] row = {rowCount + 1, phone.getName(), phone.getPrice(), 1, phone.getPrice()};
        paymentTableModel.addRow(row);
    }
}

   private void generateInvoice() {
    double totalPrice = 0.0;
    int rowCount = paymentTableModel.getRowCount();
    for (int i = 0; i < rowCount; i++) {
        double totalForRow = (double) paymentTableModel.getValueAt(i, 4);
        totalPrice += totalForRow;
    }
    
    String message = "Total Price: $" + totalPrice;
    JOptionPane.showMessageDialog(frame, message, "Invoice", JOptionPane.INFORMATION_MESSAGE);
}


    public static void main(String[] args) {
        new Dashboard();
    }
}

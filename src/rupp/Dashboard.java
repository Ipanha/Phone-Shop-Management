package rupp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class Dashboard {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel dashboardPanel;
    private JTable paymentTable;
    private DefaultTableModel paymentTableModel;
    private ArrayList<Phone> phonesList;
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
        mainPanel.add(dashboardPanel, "Dashboard");

        createSidebar();

        frame.setVisible(true);
    }

    private void createDashboardPanel() {
        dashboardPanel = new JPanel(null);

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
        dashboardPanel.add(lblPayment);
        dashboardPanel.add(navPaymentPanel);
        dashboardPanel.add(paymentScrollPane);
        dashboardPanel.add(btnBack);
        dashboardPanel.add(btnClear);
        dashboardPanel.add(btnExit);
        dashboardPanel.add(btnInvoice);
        dashboardPanel.add(lblProduct);
        dashboardPanel.add(navListProductPanel);
        dashboardPanel.add(scrollPane);

        dashboardPanel.setBackground(Color.cyan);

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

        btnNewSale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Dashboard");
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
                int qty = (int) paymentTableModel.getValueAt(i, 3);
                double total = (double) paymentTableModel.getValueAt(i, 4);

                // Find the product in the phonesList
                for (Phone phone : phonesList) {
                    if (phone.getName().equals(name)) {
                        // Deduct the sold quantity from the inventory
                        phone.setQty(phone.getQty() - qty);
                        break;
                    }
                }

                writer.printf("%s - $%.2f x %d = $%.2f%n", name, price, qty, total);
            }
            writer.println("===========================================");
            writer.println("Thank you for your purchase!");
            JOptionPane.showMessageDialog(frame, "Invoice generated successfully!", "Invoice",
                    JOptionPane.INFORMATION_MESSAGE);

            // After generating invoice, update the products file with new quantities
            writeProductsToFile();
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
        new Dashboard();
    }
}

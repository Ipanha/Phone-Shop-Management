package rupp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.imageio.ImageIO;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.util.ArrayList;

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
    private ArrayList<Phone> phonesList;
    private JPanel navProductPanel;
    private JScrollPane scrollPane;

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

        // Payment side
        lblPayment.setBounds(370, 30, 500, 50);
        lblPayment.setFont(labelFont1);
        lblPayment.setForeground(Color.BLUE);
        navPaymentPanel.setBounds(10, 10, 900, 100);
        navPaymentPanel.setBackground(Color.white);

        // Use a JTable for the payment details
        String[] columnNames = { "No.", "Name", "Price", "Qty", "Total" };
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
        btnBack.setBounds(10, 900, 150, 40);
        btnBack.setFont(labelFont2);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClear.setBounds(250, 900, 150, 40);
        btnClear.setFont(labelFont2);
        btnClear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInvoice.setBounds(500, 900, 150, 40);
        btnInvoice.setFont(labelFont2);
        btnInvoice.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExit.setBounds(750, 900, 150, 40);
        btnExit.setFont(labelFont2);
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Product side
        lblProduct.setBounds(1350, 30, 500, 50);
        lblProduct.setFont(labelFont1);
        lblProduct.setForeground(Color.BLUE);
        scrollPane.setBounds(990, 120, 900, 700);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        navListProductPanel.setBounds(990, 10, 900, 100);
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

        // Sample phone data
        phonesList = new ArrayList<>();
        phonesList.add(new Phone("iPhone 12", 559.00, "D:/RUPP/Java Programming/RUPP/src/rupp/images/Iphone-12.png"));
        phonesList.add(new Phone("iPhone 12", 559.00, "D:/RUPP/Java Programming/RUPP/src/rupp/images/Iphone-12.png"));
        phonesList.add(new Phone("iPhone 12 Pro LL/A", 659.00,
                "D:/RUPP/Java Programming/RUPP/src/rupp/images/iPhone-12-Pro.png"));
        phonesList.add(new Phone("iPhone 12 Pro Max LL/A", 759.00,
                "D:/RUPP/Java Programming/RUPP/src/rupp/images/iPhone-12-Pro_Max.png"));
        phonesList.add(
                new Phone("iPhone 13 128G", 619.00, "D:/RUPP/Java Programming/RUPP/src/rupp/images/iPhone-13.png"));
        phonesList.add(new Phone("iPhone 13 Pro 128G", 719.00,
                "D:/RUPP/Java Programming/RUPP/src/rupp/images/iPhone-13-Pro.png"));
        phonesList.add(new Phone("iPhone 13 Pro Max 128G USA (New)", 859.00,
                "D:/RUPP/Java Programming/RUPP/src/rupp/images/iPhone-13-Pro-Max.png"));
        phonesList.add(
                new Phone("iPhone 14 ZP/A", 789.00, "D:/RUPP/Java Programming/RUPP/src/rupp/images/iPhone-14.png"));
        phonesList.add(new Phone("iPhone 14 Pro 128G", 999.00,
                "D:/RUPP/Java Programming/RUPP/src/rupp/images/iPhone-14-Pro.png"));
        phonesList.add(new Phone("iPhone 14 Pro MAX LL/A", 1049.00,
                "D:/RUPP/Java Programming/RUPP/src/rupp/images/iPhone-14-Pro-Max.png"));
        phonesList.add(
                new Phone("iPhone 15 Plus", 919.00, "D:/RUPP/Java Programming/RUPP/src/rupp/images/Iphone-15.png"));
        phonesList.add(new Phone("iPhone 15 Pro USA", 999.00,
                "D:/RUPP/Java Programming/RUPP/src/rupp/images/Iphone-15-Pro.png"));
        phonesList.add(new Phone("iPhone 15 Pro Max", 1269.00,
                "D:/RUPP/Java Programming/RUPP/src/rupp/images/Iphone-15-Pro-Max.png"));

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

        menuItemAddProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddProductDialog();
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

            JLabel priceLabel = new JLabel("$" + phone.getPrice());
            JLabel nameLabel = new JLabel(phone.getName());
            JButton addToCartButton = new JButton("Add To Cart");
            addToCartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            priceLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 17));

            // Set constraints and add components
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.insets = new Insets(10, 0, 10, 0);
            productPanel.add(picLabel, gbc);

            gbc.gridy = 1;
            productPanel.add(priceLabel, gbc);

            gbc.gridy = 2;
            productPanel.add(nameLabel, gbc);

            gbc.gridy = 3;
            productPanel.add(addToCartButton, gbc);

            navProductPanel.add(productPanel);

            addToCartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addPhoneToCart(phone);
                }
            });
        }

        navProductPanel.revalidate();
        navProductPanel.repaint();
    }

    private void copyImageFile(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
    
    //Add Product Menu
    private void showAddProductDialog() {
        JDialog dialog = new JDialog(frame, "Add Product", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(frame);
        dialog.setLayout(null);

        JLabel lblName = new JLabel("Product Name:");
        JTextField txtName = new JTextField();
        JLabel lblPrice = new JLabel("Price:");
        JTextField txtPrice = new JTextField();
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
        lblImagePath.setBounds(30, 110, 100, 30);
        txtImagePath.setBounds(150, 110, 200, 30);
        btnBrowse.setBounds(360, 110, 80, 30);
        btnAdd.setBounds(150, 160, 80, 30);
        btnCancel.setBounds(240, 160, 80, 30);
        
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
                String name = txtName.getText();
                double price;
                try {
                    price = Double.parseDouble(txtPrice.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Please enter a valid price.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String imagePath = txtImagePath.getText();
                if (name.isEmpty() || imagePath.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill all fields.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Copy the image to a new location within the project
                File sourceFile = new File(imagePath);
                String fileName = sourceFile.getName();
                File destFile = new File("D:/RUPP/Java Programming/RUPP/src/rupp/images/" + fileName);
                try {
                    copyImageFile(sourceFile, destFile);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(dialog, "Failed to copy the image.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Save product details to a file
                try (PrintWriter out = new PrintWriter(new FileWriter("products.txt", true))) {
                    out.println(name + ";" + price + ";" + destFile.getAbsolutePath());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(dialog, "Failed to save product details.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Phone newPhone = new Phone(name, price, destFile.getAbsolutePath());
                phonesList.add(newPhone);
                updateProductList();
                dialog.dispose();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.add(lblName);
        dialog.add(txtName);
        dialog.add(lblPrice);
        dialog.add(txtPrice);
        dialog.add(lblImagePath);
        dialog.add(txtImagePath);
        dialog.add(btnBrowse);
        dialog.add(new JLabel());
        dialog.add(btnAdd);
        dialog.add(btnCancel);

        dialog.setVisible(true);
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
            Object[] row = { rowCount + 1, phone.getName(), phone.getPrice(), 1, phone.getPrice() };
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

class Phone {
    private String name;
    private double price;
    private String imagePath;

    public Phone(String name, double price, String imagePath) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }
}

       

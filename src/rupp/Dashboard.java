package rupp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.imageio.ImageIO;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.util.ArrayList;
import java.util.Scanner;

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

        phonesList = new ArrayList<>();
        readProductsFromFile();

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
//private void updateProductList() {
//        navProductPanel.removeAll();
//
//        for (Phone phone : phonesList) {
//            JPanel productPanel = new JPanel(new GridBagLayout());
//            GridBagConstraints gbc = new GridBagConstraints();
//
//            JLabel picLabel;
//            try {
//                BufferedImage productImage = ImageIO.read(new File(phone.getImagePath()));
//                Image scaledImage = productImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
//                ImageIcon productIcon = new ImageIcon(scaledImage);
//                picLabel = new JLabel(productIcon);
//            } catch (IOException e) {
//                e.printStackTrace();
//                picLabel = new JLabel("Image not found");
//            }
//
//            JLabel priceLabel = new JLabel("$" + phone.getPrice());
//            JLabel nameLabel = new JLabel(phone.getName());
//            JButton addToCartButton = new JButton("Add To Cart");
//            addToCartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
//
//            priceLabel.setFont(new Font("Arial", Font.PLAIN, 20));
//            nameLabel.setFont(new Font("Arial", Font.PLAIN, 17));
//
//            // Set constraints and add components
//            gbc.gridx = 0;
//            gbc.gridy = 0;
//            gbc.gridwidth = GridBagConstraints.REMAINDER;
//            gbc.insets = new Insets(10, 0, 10, 0);
//            productPanel.add(picLabel, gbc);
//
//            gbc.gridy = 1;
//            productPanel.add(priceLabel, gbc);
//
//            gbc.gridy = 2;
//            productPanel.add(nameLabel, gbc);
//
//            gbc.gridy = 3;
//            productPanel.add(addToCartButton, gbc);
//
//            navProductPanel.add(productPanel);
//
//            addToCartButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    addToPaymentTable(phone);
//                }
//            });
//        }
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

            priceLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            nameLabel.setFont(new Font("Arial", Font.BOLD, 20));

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
                    addToPaymentTable(phone);
                }
            });
        }

        navProductPanel.revalidate();
        navProductPanel.repaint();
    }

    private void addToPaymentTable(Phone phone) {
        boolean found = false;
        for (int i = 0; i < paymentTableModel.getRowCount(); i++) {
            if (paymentTableModel.getValueAt(i, 1).equals(phone.getName())) {
                int qty = (int) paymentTableModel.getValueAt(i, 3) + 1;
                paymentTableModel.setValueAt(qty, i, 3);
                paymentTableModel.setValueAt(qty * phone.getPrice(), i, 4);
                found = true;
                break;
            }
        }

        if (!found) {
            Object[] rowData = new Object[5];
            rowData[0] = paymentTableModel.getRowCount() + 1;
            rowData[1] = phone.getName();
            rowData[2] = phone.getPrice();
            rowData[3] = 1;
            rowData[4] = phone.getPrice();
            paymentTableModel.addRow(rowData);
        }
    }

    private void showAddProductDialog() {
        JDialog dialog = new JDialog(frame, "Add Product", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(frame);
        dialog.setLayout(null);

        JLabel lblName = new JLabel("Product Name:");
        JTextField txtName = new JTextField();
        JLabel lblPrice = new JLabel("Price:");
        JTextField txtPrice = new JTextField();
        JLabel lblImage = new JLabel("Image Path:");
        JTextField txtImage = new JTextField();
        JButton btnBrowse = new JButton("Browse");
        JButton btnAdd = new JButton("Add");
        JButton btnCancel = new JButton("Cancel");
        
         // Set bounds for components
        lblName.setBounds(30, 30, 100, 30);
        txtName.setBounds(150, 30, 200, 30);
        lblPrice.setBounds(30, 70, 100, 30);
        txtPrice.setBounds(150, 70, 200, 30);
        lblImage.setBounds(30, 110, 100, 30);
        txtImage.setBounds(150, 110, 200, 30);
        btnBrowse.setBounds(360, 110, 80, 30);
        btnAdd.setBounds(150, 160, 80, 30);
        btnCancel.setBounds(240, 160, 80, 30);
        

        dialog.add(lblName);
        dialog.add(txtName);
        dialog.add(lblPrice);
        dialog.add(txtPrice);
        dialog.add(lblImage);
        dialog.add(txtImage);
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
                    txtImage.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = txtName.getText();
                double price = Double.parseDouble(txtPrice.getText());
                String imagePath = txtImage.getText();
                Phone newPhone = new Phone(name, price, imagePath);
                phonesList.add(newPhone);
                writeProductToFile(newPhone);
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

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void writeProductToFile(Phone phone) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("products.txt", true))) {
            writer.write(phone.getName() + "," + phone.getPrice() + "," + phone.getImagePath());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readProductsFromFile() {
        File file = new File("products.txt");
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String[] productData = scanner.nextLine().split(",");
                    if (productData.length == 3) {
                        String name = productData[0];
                        double price = Double.parseDouble(productData[1]);
                        String imagePath = productData[2];
                        phonesList.add(new Phone(name, price, imagePath));
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
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
                writer.printf("%s - $%.2f x %d = $%.2f%n", name, price, qty, total);
            }
            writer.println("===========================================");
            writer.println("Thank you for your purchase!");
            JOptionPane.showMessageDialog(frame, "Invoice generated successfully!", "Invoice", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Dashboard();
            }
        });
    }
}

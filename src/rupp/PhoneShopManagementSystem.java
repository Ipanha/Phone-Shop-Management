package rupp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class PhoneShopManagementSystem {
    private final JFrame frame;
    private final JPanel mainPanel;
    private final CardLayout cardLayout;
    private JPanel loginPanel, registerPanel;

    // In-memory user storage
    private final Map<String, String> users = new HashMap<>();

    public PhoneShopManagementSystem() {
        frame = new JFrame("Phone Shop Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        frame.add(mainPanel);

        createLoginPanel();
        createRegisterPanel();

        mainPanel.add(loginPanel, "Login");
        mainPanel.add(registerPanel, "Register");

        cardLayout.show(mainPanel, "Login");

        frame.setVisible(true);
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(null);

        ImageIcon originalImg = new ImageIcon("D:\\RUPP\\Java Programming\\RUPP\\src\\rupp\\logo.png");
        Image img = originalImg.getImage();
        Image resizedImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH); // Resize to 200x200 pixels
        ImageIcon resizedIcon = new ImageIcon(resizedImg);
        JLabel imageLabel = new JLabel(resizedIcon);
        imageLabel.setBounds(500, 0, resizedIcon.getIconWidth(), resizedIcon.getIconHeight());
        
        JLabel nameStor = new JLabel("Space Phone Technology");
        JPanel navigationPanel = new JPanel();
        JLabel lblTitle = new JLabel("Welcome to the store.");
        JLabel lblUsername = new JLabel("Username:");
        JTextField txtUsername = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        JPasswordField txtPassword = new JPasswordField();
        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("Register");

        Font labelFont1 = new Font("Arial", Font.PLAIN, 40);
        Font labelFont2 = new Font("Arial", Font.PLAIN, 30);

        nameStor.setBounds(750, 50, 500, 50);
        nameStor.setFont(labelFont1);
        nameStor.setForeground(Color.BLUE);

        navigationPanel.setBounds(0, 0, 1920, 150);
        navigationPanel.setBackground(Color.white);
        lblTitle.setBounds(800, 200, 400, 50);
        lblTitle.setFont(labelFont1);

        lblUsername.setBounds(750, 320, 300, 45);
        lblUsername.setFont(labelFont2);

        txtUsername.setBounds(900, 320, 300, 45);
        txtUsername.setFont(labelFont2);

        lblPassword.setBounds(750, 400, 300, 45);
        lblPassword.setFont(labelFont2);

        txtPassword.setBounds(900, 400, 300, 45);
        txtPassword.setFont(labelFont2);

        btnLogin.setBounds(750, 500, 150, 40);
        btnLogin.setFont(labelFont2);

        btnRegister.setBounds(1050, 500, 150, 40);
        btnRegister.setFont(labelFont2);

        loginPanel.add(nameStor);
        loginPanel.add(imageLabel);
        loginPanel.add(navigationPanel);
        loginPanel.add(lblTitle);
        loginPanel.add(lblUsername);
        loginPanel.add(txtUsername);
        loginPanel.add(lblPassword);
        loginPanel.add(txtPassword);
        loginPanel.add(btnLogin);
        loginPanel.add(btnRegister);

        loginPanel.setBackground(Color.cyan);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());
                if (users.containsKey(username) && users.get(username).equals(password)) {
                    new Dashboard();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Register");
            }
        });
    }

    private void createRegisterPanel() {
        registerPanel = new JPanel(null);

        
        ImageIcon originalImg = new ImageIcon("D:\\RUPP\\Java Programming\\RUPP\\src\\rupp\\logo.png");
        Image img = originalImg.getImage();
        Image resizedImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH); // Resize to 200x200 pixels
        ImageIcon resizedIcon = new ImageIcon(resizedImg);
        JLabel imageLabel = new JLabel(resizedIcon);
        imageLabel.setBounds(500, 0, resizedIcon.getIconWidth(), resizedIcon.getIconHeight());
        
        JLabel nameStor = new JLabel("Space Phone Technology");
        JPanel navigationPanel = new JPanel();
        JLabel lblTitle = new JLabel("Welcome to the store.");
        JLabel lblNewUsername = new JLabel("New Username:");
        JTextField txtNewUsername = new JTextField();
        JLabel lblNewPassword = new JLabel("New Password:");
        JPasswordField txtNewPassword = new JPasswordField();
        JLabel lblConfirmPassword = new JLabel("Confirm Password:");
        JPasswordField txtConfirmPassword = new JPasswordField();
        JButton btnRegister = new JButton("Register");
        JButton btnBack = new JButton("Back");

        Font labelFont1 = new Font("Arial", Font.PLAIN, 40);
        Font labelFont2 = new Font("Arial", Font.PLAIN, 30);

        nameStor.setBounds(750, 50, 500, 50);
        nameStor.setFont(labelFont1);
        nameStor.setForeground(Color.BLUE);

        navigationPanel.setBounds(0, 0, 1920, 150);
        navigationPanel.setBackground(Color.white);

        lblTitle.setBounds(800, 200, 400, 50);
        lblTitle.setFont(labelFont1);

        lblNewUsername.setBounds(700, 320, 300, 45);
        lblNewUsername.setFont(labelFont2);

        txtNewUsername.setBounds(1000, 320, 300, 45);
        txtNewUsername.setFont(labelFont2);

        lblNewPassword.setBounds(700, 400, 300, 45);
        lblNewPassword.setFont(labelFont2);

        txtNewPassword.setBounds(1000, 400, 300, 45);
        txtNewPassword.setFont(labelFont2);

        lblConfirmPassword.setBounds(700, 480, 300, 45);
        lblConfirmPassword.setFont(labelFont2);

        txtConfirmPassword.setBounds(1000, 480, 300, 45);
        txtConfirmPassword.setFont(labelFont2);

        btnRegister.setBounds(700, 600, 150, 40);
        btnRegister.setFont(labelFont2);

        btnBack.setBounds(1150, 600, 150, 40);
        btnBack.setFont(labelFont2);

        registerPanel.add(nameStor);
        registerPanel.add(imageLabel);
        registerPanel.add(navigationPanel);
        registerPanel.add(lblTitle);
        registerPanel.add(lblNewUsername);
        registerPanel.add(txtNewUsername);
        registerPanel.add(lblNewPassword);
        registerPanel.add(txtNewPassword);
        registerPanel.add(lblConfirmPassword);
        registerPanel.add(txtConfirmPassword);
        registerPanel.add(btnRegister);
        registerPanel.add(btnBack);
        registerPanel.setBackground(Color.cyan);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newUsername = txtNewUsername.getText();
                String newPassword = new String(txtNewPassword.getPassword());
                String confirmPassword = new String(txtConfirmPassword.getPassword());
                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(frame, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (users.containsKey(newUsername)) {
                    JOptionPane.showMessageDialog(frame, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    users.put(newUsername, newPassword);
                    JOptionPane.showMessageDialog(frame, "Registration successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    cardLayout.show(mainPanel, "Login");
                }
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Login");
            }
        });
    }

    public static void main(String[] args) {
        new PhoneShopManagementSystem();
    }
}

package rupp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.swing.*;

public class PhoneShopManagementSystem {
    private final JFrame frame;
    private final JPanel mainPanel;
    private final CardLayout cardLayout;
    private JPanel loginPanel, registerPanel;
    // private String usernameLogin;
    // In-memory user storage
    private final String USERS_FILE = "D:\\RUPP\\Java Programming\\RUPP\\src\\rupp\\user_data.txt";
    private final Map<String, String> users = new HashMap<>();
    Cursor pointer = new Cursor(Cursor.HAND_CURSOR);
    // Font font40 = new Font("Arial", Font.PLAIN, 40);
    Font font30 = new Font("Arial", Font.PLAIN, 30);
    Font font35 = new Font("Arial", Font.PLAIN, 35);
    Font font35B = new Font("Arial", Font.BOLD, 35);
    Font font20 = new Font("Arial", Font.PLAIN, 20);
    Font font40B = new Font("Arial", Font.BOLD, 40);

    public PhoneShopManagementSystem() {
        frame = new JFrame("Phone Shop Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(1920, 1080);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        frame.add(mainPanel);
        loadUsersFromFile(USERS_FILE);
        createLoginPanel();
        createRegisterPanel();
        mainPanel.add(loginPanel, "Login");
        mainPanel.add(registerPanel, "Register");
        cardLayout.show(mainPanel, "Login");
        frame.setVisible(true);
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(new BorderLayout());

        // Center container to hold both panels
        JPanel centerContainer = new JPanel(new GridLayout(1, 2));
        centerContainer.setBackground(Color.WHITE);

        // West Panel (Login Form)
        JPanel westPanel = new JPanel(new GridBagLayout());
        westPanel.setBackground(Color.WHITE);
        GridBagConstraints westGbc = new GridBagConstraints();
        westGbc.insets = new Insets(10, 10, 10, 10);
        westGbc.anchor = GridBagConstraints.CENTER;

        JLabel lblTitle = new JLabel("LOGIN");
        lblTitle.setForeground(Color.BLUE);
        lblTitle.setFont(font40B);
        westGbc.gridx = 0;
        westGbc.gridy = 0;
        westGbc.gridwidth = 2;
        westPanel.add(lblTitle, westGbc);

        westGbc.gridwidth = 1;
        westGbc.anchor = GridBagConstraints.WEST;

        JTextField txtUsername = new JTextField(15);
        txtUsername.setFont(font30);
        txtUsername.setText("Username");
        txtUsername.setForeground(Color.GRAY);
        txtUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtUsername.getText().equals("Username")) {
                    txtUsername.setText("");
                    txtUsername.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtUsername.getText().isEmpty()) {
                    txtUsername.setForeground(Color.GRAY);
                    txtUsername.setText("Username");
                }
            }
        });
        westGbc.gridy++;
        westPanel.add(txtUsername, westGbc);

        JPasswordField txtPassword = new JPasswordField(15);
        txtPassword.setFont(font30);
        txtPassword.setText("Password");
        txtPassword.setForeground(Color.GRAY);
        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (new String(txtPassword.getPassword()).equals("Password")) {
                    txtPassword.setText("");
                    txtPassword.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (new String(txtPassword.getPassword()).isEmpty()) {
                    txtPassword.setForeground(Color.GRAY);
                    txtPassword.setText("Password");
                }
            }
        });
        westGbc.gridy++;
        westPanel.add(txtPassword, westGbc);

        // Eye icon button to toggle password visibility
        ImageIcon viewIcon = new ImageIcon("D:\\RUPP\\Java Programming\\RUPP\\src\\rupp\\icon\\hide.png");
        Image viewImg = viewIcon.getImage();
        Image resizedViewImg = viewImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon resizedViewIcon = new ImageIcon(resizedViewImg);

        ImageIcon hideIcon = new ImageIcon("D:\\RUPP\\Java Programming\\RUPP\\src\\rupp\\icon\\view.png");
        Image hideImg = hideIcon.getImage();
        Image resizedHideImg = hideImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon resizedHideIcon = new ImageIcon(resizedHideImg);

        JButton btnTogglePassword = new JButton(resizedViewIcon);
        btnTogglePassword.setOpaque(false);
        btnTogglePassword.setContentAreaFilled(false);
        btnTogglePassword.setBorderPainted(false);
        btnTogglePassword.setCursor(pointer);

        btnTogglePassword.addActionListener((ActionEvent e) -> {
            if (txtPassword.getEchoChar() != '\u0000') {
                txtPassword.setEchoChar((char) 0);
                btnTogglePassword.setIcon(resizedHideIcon);
            } else {
                txtPassword.setEchoChar('•');
                btnTogglePassword.setIcon(resizedViewIcon);
            }
        });

        westGbc.gridx++;
        westPanel.add(btnTogglePassword, westGbc);

        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setFont(font30);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(new Color(0, 122, 255));
        btnLogin.setOpaque(true);
        btnLogin.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        westGbc.gridx = 0;
        westGbc.gridy++;
        westGbc.gridwidth = 2;
        westPanel.add(btnLogin, westGbc);

        JLabel lblOr = new JLabel("or login with");
        lblOr.setFont(font20);
        westGbc.gridy++;
        westPanel.add(lblOr, westGbc);

        JPanel socialPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        socialPanel.setBackground(Color.WHITE);

        JButton btnFacebook = new JButton(
                new ImageIcon(new ImageIcon("D:\\RUPP\\Java Programming\\RUPP\\src\\rupp\\icon\\facebook.png")
                        .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        btnFacebook.setBackground(null);
        btnFacebook.setBorder(null);
        btnFacebook.setCursor(pointer);
        JButton btnGoogle = new JButton(
                new ImageIcon(new ImageIcon("D:\\RUPP\\Java Programming\\RUPP\\src\\rupp\\icon\\google.png").getImage()
                        .getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        btnGoogle.setBackground(null);
        btnGoogle.setBorder(null);
        btnGoogle.setCursor(pointer);
        JButton btnLinkedIn = new JButton(
                new ImageIcon(new ImageIcon("D:\\RUPP\\Java Programming\\RUPP\\src\\rupp\\icon\\linkedin.png")
                        .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        btnLinkedIn.setBackground(null);
        btnLinkedIn.setBorder(null);
        btnLinkedIn.setCursor(pointer);

        socialPanel.add(btnFacebook);
        socialPanel.add(btnGoogle);
        socialPanel.add(btnLinkedIn);

        westGbc.gridy++;
        westGbc.gridwidth = 2;
        westPanel.add(socialPanel, westGbc);

        // East Panel (Welcome Message)
        JPanel eastPanel = new JPanel(new GridBagLayout());
        eastPanel.setBackground(new Color(0, 150, 136));
        GridBagConstraints eastGbc = new GridBagConstraints();
        eastGbc.insets = new Insets(10, 10, 10, 10);
        eastGbc.anchor = GridBagConstraints.CENTER;

        JLabel lblWelcome = new JLabel(
                "<html><div style='text-align: center;'>Welcome back!<br>We are so happy to have you here.<br>It's great to see you again.<br> We hope you had a safe and enjoyable time away.</div></html>");
        lblWelcome.setFont(font35B);
        lblWelcome.setForeground(Color.WHITE);
        eastGbc.gridx = 0;
        eastGbc.gridy = 0;
        eastPanel.add(lblWelcome, eastGbc);

        JButton btnSignup = new JButton("No account yet? Register.");
        btnSignup.setFont(font20);
        btnSignup.setForeground(Color.WHITE);
        btnSignup.setBackground(new Color(0, 122, 255));
        btnSignup.setOpaque(true);
        btnSignup.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        btnSignup.setCursor(new Cursor(Cursor.HAND_CURSOR));

        eastGbc.gridy++;
        eastPanel.add(btnSignup, eastGbc);

        // Adding panels to center container
        centerContainer.add(westPanel);
        centerContainer.add(eastPanel);

        // Adding center container to login panel
        loginPanel.add(centerContainer, BorderLayout.CENTER);

        btnLogin.addActionListener((ActionEvent e) -> {
            String usernameLogin = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            if (users.containsKey(usernameLogin) && users.get(usernameLogin).equals(password)) {
                frame.dispose();
                SwingUtilities.invokeLater(() -> new Dashboard(usernameLogin));
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSignup.addActionListener((ActionEvent e) -> {
            cardLayout.show(mainPanel, "Register");
        });
    }

    private void createRegisterPanel() {
        registerPanel = new JPanel(new BorderLayout());

        // Center container to hold both panels
        JPanel centerContainer = new JPanel(new GridLayout(1, 2));
        centerContainer.setBackground(Color.WHITE);

        // West Panel (Login Form)
        JPanel westPanel = new JPanel(new GridBagLayout());
        westPanel.setBackground(Color.WHITE);
        GridBagConstraints westGbc = new GridBagConstraints();
        westGbc.insets = new Insets(10, 10, 10, 10);
        westGbc.anchor = GridBagConstraints.CENTER;

        JLabel lblTitle = new JLabel("REGISTER");
        lblTitle.setForeground(new Color(21, 201, 0));
        lblTitle.setFont(font40B);
        westGbc.gridx = 0;
        westGbc.gridy = 0;
        westGbc.gridwidth = 2;
        westPanel.add(lblTitle, westGbc);

        westGbc.gridwidth = 1;
        westGbc.anchor = GridBagConstraints.WEST;

        JTextField txtNewUsername = new JTextField(15);
        txtNewUsername.setFont(font30);
        txtNewUsername.setText("Username");
        txtNewUsername.setForeground(Color.GRAY);
        txtNewUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtNewUsername.getText().equals("Username")) {
                    txtNewUsername.setText("");
                    txtNewUsername.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtNewUsername.getText().isEmpty()) {
                    txtNewUsername.setForeground(Color.GRAY);
                    txtNewUsername.setText("Username");
                }
            }
        });
        westGbc.gridy++;
        westPanel.add(txtNewUsername, westGbc);

        JPasswordField txtNewPassword = new JPasswordField(15);
        txtNewPassword.setFont(font30);
        txtNewPassword.setText("Password");
        txtNewPassword.setForeground(Color.GRAY);
        txtNewPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (new String(txtNewPassword.getPassword()).equals("Password")) {
                    txtNewPassword.setText("");
                    txtNewPassword.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (new String(txtNewPassword.getPassword()).isEmpty()) {
                    txtNewPassword.setForeground(Color.GRAY);
                    txtNewPassword.setText("Password");
                }
            }
        });

        // Eye icon button to toggle password visibility
        ImageIcon viewIcon = new ImageIcon("D:\\RUPP\\Java Programming\\RUPP\\src\\rupp\\icon\\hide.png");
        Image viewImg = viewIcon.getImage();
        Image resizedViewImg = viewImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon resizedViewIcon = new ImageIcon(resizedViewImg);

        ImageIcon hideIcon = new ImageIcon("D:\\RUPP\\Java Programming\\RUPP\\src\\rupp\\icon\\view.png");
        Image hideImg = hideIcon.getImage();
        Image resizedHideImg = hideImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon resizedHideIcon = new ImageIcon(resizedHideImg);

        JButton btnTogglePassword = new JButton(resizedViewIcon);
        btnTogglePassword.setOpaque(false);
        btnTogglePassword.setContentAreaFilled(false);
        btnTogglePassword.setBorderPainted(false);
        btnTogglePassword.setCursor(pointer);

        btnTogglePassword.addActionListener((ActionEvent e) -> {
            if (txtNewPassword.getEchoChar() != '\u0000') {
                txtNewPassword.setEchoChar((char) 0);
                btnTogglePassword.setIcon(resizedHideIcon);
            } else {
                txtNewPassword.setEchoChar('•');
                btnTogglePassword.setIcon(resizedViewIcon);
            }
        });

        // Panel to hold password field and toggle button
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setBackground(null);
        passwordPanel.add(txtNewPassword, BorderLayout.CENTER);
        passwordPanel.add(btnTogglePassword, BorderLayout.EAST);

        westGbc.gridy++;
        westGbc.gridx = 0;
        westGbc.gridwidth = 2;
        westPanel.add(passwordPanel, westGbc);

        JPasswordField txtConPassword = new JPasswordField(15);
        txtConPassword.setFont(font30);
        txtConPassword.setText("Password");
        txtConPassword.setForeground(Color.GRAY);
        txtConPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (new String(txtConPassword.getPassword()).equals("Password")) {
                    txtConPassword.setText("");
                    txtConPassword.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (new String(txtConPassword.getPassword()).isEmpty()) {
                    txtConPassword.setForeground(Color.GRAY);
                    txtConPassword.setText("Password");
                }
            }
        });

        JButton btnToggleConPassword = new JButton(resizedViewIcon);
        btnToggleConPassword.setOpaque(false);
        btnToggleConPassword.setContentAreaFilled(false);
        btnToggleConPassword.setBorderPainted(false);
        btnToggleConPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnToggleConPassword.addActionListener((ActionEvent e) -> {
            if (txtConPassword.getEchoChar() != '\u0000') {
                txtConPassword.setEchoChar((char) 0);
                btnToggleConPassword.setIcon(resizedHideIcon);
            } else {
                txtConPassword.setEchoChar('•');
                btnToggleConPassword.setIcon(resizedViewIcon);
            }
        });

        JPanel confirmPasswordPanel = new JPanel(new BorderLayout());
        confirmPasswordPanel.setBackground(null);
        confirmPasswordPanel.add(txtConPassword, BorderLayout.CENTER);
        confirmPasswordPanel.add(btnToggleConPassword, BorderLayout.EAST);

        westGbc.gridy++;
        westGbc.gridx = 0;
        westGbc.gridwidth = 2;
        westPanel.add(confirmPasswordPanel, westGbc);

        JButton btnRegister = new JButton("REGISTER");
        btnRegister.setFont(font30);
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setBackground(new Color(21, 201, 0));
        btnRegister.setOpaque(true);
        btnRegister.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        btnRegister.setCursor(pointer);

        westGbc.gridx = 0;
        westGbc.gridy++;
        westGbc.gridwidth = 2;
        westPanel.add(btnRegister, westGbc);

        JLabel lblOr = new JLabel("or register with");
        lblOr.setFont(font20);
        westGbc.gridy++;
        westPanel.add(lblOr, westGbc);

        JPanel socialPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        socialPanel.setBackground(Color.WHITE);

        JButton btnFacebook = new JButton(
                new ImageIcon(new ImageIcon("D:\\RUPP\\Java Programming\\RUPP\\src\\rupp\\icon\\facebook.png")
                        .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        btnFacebook.setBackground(null);
        btnFacebook.setBorder(null);
        btnFacebook.setCursor(pointer);
        JButton btnGoogle = new JButton(
                new ImageIcon(new ImageIcon("D:\\RUPP\\Java Programming\\RUPP\\src\\rupp\\icon\\google.png").getImage()
                        .getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        btnGoogle.setBackground(null);
        btnGoogle.setBorder(null);
        btnGoogle.setCursor(pointer);
        JButton btnLinkedIn = new JButton(
                new ImageIcon(new ImageIcon("D:\\RUPP\\Java Programming\\RUPP\\src\\rupp\\icon\\linkedin.png")
                        .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        btnLinkedIn.setBackground(null);
        btnLinkedIn.setBorder(null);
        btnLinkedIn.setCursor(pointer);

        socialPanel.add(btnFacebook);
        socialPanel.add(btnGoogle);
        socialPanel.add(btnLinkedIn);

        westGbc.gridy++;
        westGbc.gridwidth = 2;
        westPanel.add(socialPanel, westGbc);

        // East Panel (Welcome Message)
        JPanel eastPanel = new JPanel(new GridBagLayout());
        eastPanel.setBackground(new Color(0, 150, 136));
        GridBagConstraints eastGbc = new GridBagConstraints();
        eastGbc.insets = new Insets(10, 10, 10, 10);
        eastGbc.anchor = GridBagConstraints.CENTER;

        JLabel lblWelcome = new JLabel(
                "<html><div style='text-align: center;'>Welcome back!<br>We are so happy to have you here.<br>It's great to see you again. <br>We hope you had a safe and enjoyable time away.</div></html>");
        lblWelcome.setFont(font35B);
        lblWelcome.setForeground(Color.WHITE);
        eastGbc.gridx = 0;
        eastGbc.gridy = 0;
        eastPanel.add(lblWelcome, eastGbc);

        JButton btnLogin = new JButton("Have Account? Login.");
        btnLogin.setFont(new Font("Arial", Font.PLAIN, 20));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(new Color(0, 122, 255));
        btnLogin.setOpaque(true);
        btnLogin.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        btnLogin.setCursor(pointer);

        eastGbc.gridy++;
        eastPanel.add(btnLogin, eastGbc);

        // Adding panels to center container
        centerContainer.add(eastPanel);
        centerContainer.add(westPanel);

        // Adding center container to login panel
        registerPanel.add(centerContainer, BorderLayout.CENTER);

        btnRegister.addActionListener((ActionEvent e) -> {
            String newUsername = txtNewUsername.getText();
            String newPassword = new String(txtNewPassword.getPassword());
            String confirmPassword = new String(txtConPassword.getPassword());

            if (newPassword.equals(confirmPassword)) {
                if (!users.containsKey(newUsername)) {
                    users.put(newUsername, newPassword);
                    saveUserToFile(newUsername, newPassword, null); // Pass null for photoPath initially
                    JOptionPane.showMessageDialog(frame, "Registration successful!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    cardLayout.show(mainPanel, "Login");
                } else {
                    JOptionPane.showMessageDialog(frame, "Username already exists!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Passwords do not match!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLogin.addActionListener((ActionEvent e) -> {
            cardLayout.show(mainPanel, "Login");
        });
    }

    private void saveUserToFile(String username, String password, String photoPath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            String photoPathValue = (photoPath != null && !photoPath.isEmpty()) ? photoPath : "N/A";
            writer.write(username + "," + password + "," + photoPathValue);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUsersFromFile(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String username = parts[0];
                    String password = parts[1];
                    // String photoPath = parts.length > 2 ? parts[2] : "N/A";
                    users.put(username, password);
                    // userPhotos.put(username, photoPath.equals("N/A") ? null : photoPath);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PhoneShopManagementSystem());
    }
}
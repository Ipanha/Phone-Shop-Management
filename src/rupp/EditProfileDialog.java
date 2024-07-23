package rupp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.*;
import javax.swing.*;

public class EditProfileDialog extends JDialog {
    private final JTextField newUsernameField;
    private final JPasswordField oldPasswordField;
    private final JPasswordField newPasswordField;
    private final JPasswordField confirmNewPasswordField;
    private final JLabel photoLabel;
    private String photoPath;
    private String newPhotoPath;
    private final Dashboard dashboard;

    public EditProfileDialog(JFrame parent, String username, String photoPath, Dashboard dashboard) {
        super(parent, "Edit Profile", true);
        this.dashboard = dashboard;
        this.photoPath = photoPath; // Initial photoPath

        setLayout(null);
        setSize(600, 600);

        // Font settings
        Font font18 = new Font(Font.SANS_SERIF, Font.PLAIN, 18);
        Font font18B = new Font(Font.SANS_SERIF, Font.BOLD, 18);
        Font font25B = new Font(Font.SANS_SERIF, Font.BOLD, 25);
        Cursor pointer = new Cursor(Cursor.HAND_CURSOR);

        // Set up components
        JLabel oldPasswordLabel = new JLabel("Old Password                   :");
        oldPasswordLabel.setFont(font18B);
        JLabel newUsernameLabel = new JLabel("New Username                :");
        newUsernameLabel.setFont(font18B);
        JLabel newPasswordLabel = new JLabel("New Password                 :");
        newPasswordLabel.setFont(font18B);
        JLabel confirmNewPasswordLabel = new JLabel("Confirm New Password  :");
        confirmNewPasswordLabel.setFont(font18B);

        oldPasswordField = new JPasswordField();
        oldPasswordField.setFont(font18B);
        newUsernameField = new JTextField(username);
        newUsernameField.setFont(font18B);
        newPasswordField = new JPasswordField();
        newPasswordField.setFont(font18B);
        confirmNewPasswordField = new JPasswordField();
        confirmNewPasswordField.setFont(font18B);

        photoLabel = new JLabel(new ImageIcon(
                new ImageIcon(photoPath).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
        JButton choosePhotoButton = new JButton("Choose Photo");
        choosePhotoButton.setFont(font18B);
        choosePhotoButton.setCursor(pointer);
        choosePhotoButton.setForeground(Color.WHITE);
        choosePhotoButton.setBackground(new Color(6, 200, 0));
        choosePhotoButton.setOpaque(true);
        choosePhotoButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JButton saveButton = new JButton("Save");
        saveButton.setFont(font25B);
        saveButton.setCursor(pointer);
        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(new Color(0, 62, 255));
        saveButton.setOpaque(true);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(font25B);
        cancelButton.setCursor(pointer);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(255, 0, 0));
        cancelButton.setOpaque(true);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Set bounds for components
        oldPasswordLabel.setBounds(30, 30, 230, 30);
        oldPasswordField.setBounds(270, 30, 230, 30);
        newUsernameLabel.setBounds(30, 80, 230, 30);
        newUsernameField.setBounds(270, 80, 230, 30);
        newPasswordLabel.setBounds(30, 130, 230, 30);
        newPasswordField.setBounds(270, 130, 230, 30);
        confirmNewPasswordLabel.setBounds(30, 180, 230, 30);
        confirmNewPasswordField.setBounds(270, 180, 230, 30);

        photoLabel.setBounds(30, 230, 200, 200);
        choosePhotoButton.setBounds(330, 330, 150, 40);
        saveButton.setBounds(30, 490, 110, 40);
        cancelButton.setBounds(340, 490, 140, 40);

        ImageIcon viewIcon = new ImageIcon("src/rupp/icon/hide.png");
        Image viewImg = viewIcon.getImage();
        Image resizedViewImg = viewImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon resizedViewIcon = new ImageIcon(resizedViewImg);

        ImageIcon hideIcon = new ImageIcon("src/rupp/icon/view.png");
        Image hideImg = hideIcon.getImage();
        Image resizedHideImg = hideImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon resizedHideIcon = new ImageIcon(resizedHideImg);

        // Eye icon button for Old Password field
        JButton btnToggleoldPasswordField = new JButton(resizedViewIcon);
        btnToggleoldPasswordField.setBounds(510, 20, 45, 45);
        btnToggleoldPasswordField.setOpaque(false);
        btnToggleoldPasswordField.setContentAreaFilled(false);
        btnToggleoldPasswordField.setBorderPainted(false);
        btnToggleoldPasswordField.setCursor(pointer);

        btnToggleoldPasswordField.addActionListener(
                new TogglePasswordActionListener(oldPasswordField, resizedViewIcon, resizedHideIcon));

        // Eye icon button for New Password field
        JButton btnTogglenewPasswordField = new JButton(resizedViewIcon);
        btnTogglenewPasswordField.setBounds(510, 120, 45, 45);
        btnTogglenewPasswordField.setOpaque(false);
        btnTogglenewPasswordField.setContentAreaFilled(false);
        btnTogglenewPasswordField.setBorderPainted(false);
        btnTogglenewPasswordField.setCursor(pointer);

        btnTogglenewPasswordField.addActionListener(
                new TogglePasswordActionListener(newPasswordField, resizedViewIcon, resizedHideIcon));

        // Eye icon button for Confirm New Password field
        JButton btnToggleconfirmNewPasswordField = new JButton(resizedViewIcon);
        btnToggleconfirmNewPasswordField.setBounds(510, 175, 45, 45);
        btnToggleconfirmNewPasswordField.setOpaque(false);
        btnToggleconfirmNewPasswordField.setContentAreaFilled(false);
        btnToggleconfirmNewPasswordField.setBorderPainted(false);
        btnToggleconfirmNewPasswordField.setCursor(pointer);

        btnToggleconfirmNewPasswordField.addActionListener(
                new TogglePasswordActionListener(confirmNewPasswordField, resizedViewIcon, resizedHideIcon));

        // Add components to the dialog
        add(oldPasswordLabel);
        add(oldPasswordField);
        add(newUsernameLabel);
        add(newUsernameField);
        add(newPasswordLabel);
        add(newPasswordField);
        add(confirmNewPasswordLabel);
        add(confirmNewPasswordField);
        add(photoLabel);
        add(btnToggleoldPasswordField);
        add(btnTogglenewPasswordField);
        add(btnToggleconfirmNewPasswordField);
        add(choosePhotoButton);
        add(saveButton);
        add(cancelButton);

        // Set up action listeners
        choosePhotoButton.addActionListener(new ChoosePhotoActionListener());

        saveButton.addActionListener(new SaveButtonActionListener(parent, username));

        cancelButton.addActionListener(e -> dispose());

        setLocationRelativeTo(parent);
    }

    // Inner class for toggle password visibility
    private class TogglePasswordActionListener implements ActionListener {
        private final JPasswordField passwordField;
        private final ImageIcon viewIcon;
        private final ImageIcon hideIcon;

        public TogglePasswordActionListener(JPasswordField passwordField, ImageIcon viewIcon, ImageIcon hideIcon) {
            this.passwordField = passwordField;
            this.viewIcon = viewIcon;
            this.hideIcon = hideIcon;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (passwordField.getEchoChar() != '\u0000') {
                passwordField.setEchoChar((char) 0);
                ((JButton) e.getSource()).setIcon(hideIcon);
            } else {
                passwordField.setEchoChar('•');
                ((JButton) e.getSource()).setIcon(viewIcon);
            }
        }
    }

    // Inner class for choose photo action listener
    private class ChoosePhotoActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(EditProfileDialog.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                newPhotoPath = "src/rupp/images/" + selectedFile.getName();

                File oldPhotoFile = new File(photoPath);
                if (oldPhotoFile.exists()) {
                    oldPhotoFile.delete();
                }

                try {
                    Files.copy(selectedFile.toPath(), Paths.get(newPhotoPath), StandardCopyOption.REPLACE_EXISTING);
                    photoPath = newPhotoPath; // Update the photoPath after successful copy
                    photoLabel.setIcon(new ImageIcon(
                            new ImageIcon(photoPath).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(EditProfileDialog.this, "Failed to copy the photo file.");
                }
            }
        }
    }

    // Inner class for save button action listener
    private class SaveButtonActionListener implements ActionListener {
        private final JFrame parent;
        private final String username;

        public SaveButtonActionListener(JFrame parent, String username) {
            this.parent = parent;
            this.username = username;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean photoChanged = !photoPath.equals(newPhotoPath);
            boolean isPasswordChange = newPasswordField.getPassword().length > 0;
            boolean isNameChange = !newUsernameField.getText().equals(username);

            String inputOldPassword = null;
            boolean passwordMatched = false;

            if (isPasswordChange) {
                inputOldPassword = new String(oldPasswordField.getPassword());
            } else {
                passwordMatched = true; // Skip password validation if only photo or username is being changed
            }

            String filePath = "src/rupp/File/user_data.txt";
            File file = new File(filePath);
            StringBuilder fileContents = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        String fileUsername = parts[0];
                        String filePassword = parts[1];
                        String filePhotoPath = parts[2];

                        if (fileUsername.trim().equals(username.trim())) {
                            if (isPasswordChange && !filePassword.trim().equals(inputOldPassword.trim())) {
                                JOptionPane.showMessageDialog(parent, "Old password is incorrect!", "Edit Profile",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            String newUsername = newUsernameField.getText();
                            String newPassword = new String(newPasswordField.getPassword());
                            String newPhotoPath = photoPath;

                            if (!isPasswordChange) {
                                newPassword = filePassword.trim();
                            }

                            String updatedLine = newUsername + "," + newPassword + "," + newPhotoPath;

                            fileContents.append(updatedLine).append("\n");
                        } else {
                            fileContents.append(line).append("\n");
                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(fileContents.toString());
                writer.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            JOptionPane.showMessageDialog(parent, "Profile updated successfully!", "Edit Profile",
                    JOptionPane.INFORMATION_MESSAGE);

            dashboard.refreshUserProfile(newUsernameField.getText(), photoPath);

            dispose();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setVisible(true);
        Dashboard dashboard = new Dashboard("Admin");
        EditProfileDialog editProfileDialog = new EditProfileDialog(frame, "Admin",
                "src/rupp/icon/view.png", dashboard);
        editProfileDialog.setVisible(true);
    }
}

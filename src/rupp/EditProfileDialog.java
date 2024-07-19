package rupp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import javax.swing.*;

public class EditProfileDialog extends JDialog {
    private final JTextField newUsernameField;
    private final JPasswordField oldPasswordField;
    private final JPasswordField newPasswordField;
    private final JPasswordField confirmNewPasswordField;
    private final JLabel photoLabel;
    private String photoPath;
    private final Dashboard dashboard;

    public EditProfileDialog(JFrame parent, String username, String photoPath, Dashboard dashboard) {
        super(parent, "Edit Profile", true);
        this.dashboard = dashboard;

        setLayout(null);
        setSize(550, 550);

        // Font settings
        Font font18 = new Font(Font.SANS_SERIF, Font.PLAIN, 18);
        Cursor pointer = new Cursor(Cursor.HAND_CURSOR);

        // Set up components
        JLabel oldPasswordLabel = new JLabel("Old Password:");
        oldPasswordLabel.setFont(font18);
        JLabel newUsernameLabel = new JLabel("New Username:");
        newUsernameLabel.setFont(font18);
        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setFont(font18);
        JLabel confirmNewPasswordLabel = new JLabel("Confirm New Password:");
        confirmNewPasswordLabel.setFont(font18);

        oldPasswordField = new JPasswordField();
        oldPasswordField.setFont(font18);
        newUsernameField = new JTextField(username);
        newUsernameField.setFont(font18);
        newPasswordField = new JPasswordField();
        newPasswordField.setFont(font18);
        confirmNewPasswordField = new JPasswordField();
        confirmNewPasswordField.setFont(font18);

        photoLabel = new JLabel(new ImageIcon(
                new ImageIcon(photoPath).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
        this.photoPath = photoPath;
        JButton choosePhotoButton = new JButton("Choose Photo");
        choosePhotoButton.setFont(font18);
        choosePhotoButton.setCursor(pointer);
        JButton saveButton = new JButton("Save");
        saveButton.setFont(font18);
        saveButton.setCursor(pointer);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(font18);
        cancelButton.setCursor(pointer);

        // Set bounds for components
        oldPasswordLabel.setBounds(30, 30, 200, 30);
        oldPasswordField.setBounds(250, 30, 200, 30);
        newUsernameLabel.setBounds(30, 80, 200, 30);
        newUsernameField.setBounds(250, 80, 200, 30);
        newPasswordLabel.setBounds(30, 130, 200, 30);
        newPasswordField.setBounds(250, 130, 200, 30);
        confirmNewPasswordLabel.setBounds(30, 180, 200, 30);
        confirmNewPasswordField.setBounds(250, 180, 200, 30);

        photoLabel.setBounds(30, 230, 200, 200);
        choosePhotoButton.setBounds(300, 300, 150, 30);
        saveButton.setBounds(30, 450, 120, 30);
        cancelButton.setBounds(330, 450, 120, 30);

        ImageIcon viewIcon = new ImageIcon("D:\\RUPP\\Java Programming\\RUPP\\src\\rupp\\icon\\hide.png");
        Image viewImg = viewIcon.getImage();
        Image resizedViewImg = viewImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon resizedViewIcon = new ImageIcon(resizedViewImg);

        ImageIcon hideIcon = new ImageIcon("D:\\RUPP\\Java Programming\\RUPP\\src\\rupp\\icon\\view.png");
        Image hideImg = hideIcon.getImage();
        Image resizedHideImg = hideImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon resizedHideIcon = new ImageIcon(resizedHideImg);

        // Eye icon button for Old Password field
        JButton btnToggleoldPasswordField = new JButton(resizedViewIcon);
        btnToggleoldPasswordField.setBounds(460, 20, 45, 45);
        btnToggleoldPasswordField.setOpaque(false);
        btnToggleoldPasswordField.setContentAreaFilled(false);
        btnToggleoldPasswordField.setBorderPainted(false);
        btnToggleoldPasswordField.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnToggleoldPasswordField.addActionListener((ActionEvent e) -> {
            if (oldPasswordField.getEchoChar() != '\u0000') {
                oldPasswordField.setEchoChar((char) 0);
                btnToggleoldPasswordField.setIcon(resizedHideIcon);
            } else {
                oldPasswordField.setEchoChar('•');
                btnToggleoldPasswordField.setIcon(resizedViewIcon);
            }
        });

        // Eye icon button for New Password field
        JButton btnTogglenewPasswordField = new JButton(resizedViewIcon);
        btnTogglenewPasswordField.setBounds(460, 120, 45, 45);
        btnTogglenewPasswordField.setOpaque(false);
        btnTogglenewPasswordField.setContentAreaFilled(false);
        btnTogglenewPasswordField.setBorderPainted(false);
        btnTogglenewPasswordField.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnTogglenewPasswordField.addActionListener((ActionEvent e) -> {
            if (newPasswordField.getEchoChar() != '\u0000') {
                newPasswordField.setEchoChar((char) 0);
                btnTogglenewPasswordField.setIcon(resizedHideIcon);
            } else {
                newPasswordField.setEchoChar('•');
                btnTogglenewPasswordField.setIcon(resizedViewIcon);
            }
        });

        // Eye icon button for Confirm New Password field
        JButton btnToggleconfirmNewPasswordField = new JButton(resizedViewIcon);
        btnToggleconfirmNewPasswordField.setBounds(460, 175, 45, 45);
        btnToggleconfirmNewPasswordField.setOpaque(false);
        btnToggleconfirmNewPasswordField.setContentAreaFilled(false);
        btnToggleconfirmNewPasswordField.setBorderPainted(false);
        btnToggleconfirmNewPasswordField.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnToggleconfirmNewPasswordField.addActionListener((ActionEvent e) -> {
            if (confirmNewPasswordField.getEchoChar() != '\u0000') {
                confirmNewPasswordField.setEchoChar((char) 0);
                btnToggleconfirmNewPasswordField.setIcon(resizedHideIcon);
            } else {
                confirmNewPasswordField.setEchoChar('•');
                btnToggleconfirmNewPasswordField.setIcon(resizedViewIcon);
            }
        });

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
        choosePhotoButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                this.photoPath = selectedFile.getAbsolutePath();
                photoLabel.setIcon(new ImageIcon(
                        new ImageIcon(this.photoPath).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
            }
        });

        saveButton.addActionListener(e -> {
            // Check if photo has changed
            boolean photoChanged = !photoPath.equals(this.photoPath);

            // Determine if old password validation is needed
            boolean isPasswordChange = newPasswordField.getPassword().length > 0;
            boolean isNameChange = !newUsernameField.getText().equals(username);

            // Read and validate old password only if username or password is being changed
            String inputOldPassword = null;
            boolean passwordMatched = false;

            if (isPasswordChange) {
                inputOldPassword = new String(oldPasswordField.getPassword());
            } else {
                passwordMatched = true; // Skip password validation if only photo or username is being changed
            }

            String filePath = "D:\\RUPP\\Java Programming\\RUPP\\src\\rupp\\user_data.txt";
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
                            // Validate old password if needed
                            if (isPasswordChange && !filePassword.trim().equals(inputOldPassword.trim())) {
                                JOptionPane.showMessageDialog(parent, "Old password is incorrect!", "Edit Profile",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            // Update new username, password, and photo path
                            String newUsername = newUsernameField.getText();
                            String newPassword = new String(newPasswordField.getPassword());
                            String newPhotoPath = this.photoPath;

                            // Use existing password if new password fields are empty
                            if (!isPasswordChange) {
                                newPassword = filePassword.trim();
                            }

                            // Construct the updated line
                            String updatedLine = newUsername + "," + newPassword + "," + newPhotoPath;

                            // Append the updated line to the StringBuilder
                            fileContents.append(updatedLine).append("\n");
                        } else {
                            // Append the existing line as it is
                            fileContents.append(line).append("\n");
                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // Write the updated contents back to the file
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(fileContents.toString());
                writer.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            JOptionPane.showMessageDialog(parent, "Profile updated successfully!", "Edit Profile",
                    JOptionPane.INFORMATION_MESSAGE);

            // Call method in Dashboard to refresh profile data
            dashboard.refreshUserProfile(newUsernameField.getText(), this.photoPath);

            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        setLocationRelativeTo(parent);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setVisible(true);
        Dashboard dashboard = new Dashboard("testUser");
        EditProfileDialog editProfileDialog = new EditProfileDialog(frame, "testUser",
                "D:\\RUPP\\Java Programming\\RUPP\\src\\rupp\\icon\\view.png", dashboard);
        editProfileDialog.setVisible(true);
    }
}

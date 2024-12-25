import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class RegistrationScreen extends JFrame {
    private Map<String, String> userCredentials;

    public RegistrationScreen(Map<String, String> userCredentials) {
        this.userCredentials = userCredentials;

        setTitle("Register User");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // 外边距
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 组件间距
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username label and text field
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        panel.add(userLabel, gbc);

        JTextField userText = new JTextField();
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7;
        panel.add(userText, gbc);

        // Password label and password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        panel.add(passwordLabel, gbc);

        JPasswordField passwordText = new JPasswordField();
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7;
        panel.add(passwordText, gbc);

        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(100, 149, 237)); // Blue background
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weighty = 0.3;
        panel.add(registerButton, gbc);

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(255, 69, 0)); // Orange-red background
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 1;
        panel.add(backButton, gbc);

        add(panel);

        // Register button event
        registerButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());

            // Check if the username or password is empty
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and Password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit the method if fields are empty
            }

            if (!userCredentials.containsKey(username)) {
                userCredentials.put(username, password);
                saveUserCredentials(); // Save to file
                JOptionPane.showMessageDialog(this, "User registered successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new LoginScreen(); // Go back to login screen
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Back button event
        backButton.addActionListener(e -> {
            new LoginScreen();
            dispose();
        });

        setVisible(true);
    }

    private void saveUserCredentials() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/users.txt"))) {
            for (Map.Entry<String, String> entry : userCredentials.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

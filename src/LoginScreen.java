import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends JFrame {
    private Map<String, String> userCredentials;

    public LoginScreen() {
        userCredentials = loadUserCredentials(); // Load user data from file

        setTitle("Food Distribution System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel and layout setup
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Outer margins
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username Label and TextField
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        panel.add(userLabel, gbc);

        JTextField userText = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        panel.add(userText, gbc);

        // Password Label and PasswordField
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        JPasswordField passwordText = new JPasswordField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordText, gbc);

        // Login button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        buttonPanel.add(loginButton);

        // Register button with Cyan background
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(0, 100, 0)); // Cyan background
        registerButton.setForeground(Color.WHITE); // White text
        registerButton.setFocusPainted(false);
        registerButton.setPreferredSize(new Dimension(150, 40));
        buttonPanel.add(registerButton);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        // Exit button with Red background
        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(new Color(255, 0, 0)); // Red background
        exitButton.setForeground(Color.WHITE); // White text
        exitButton.setFocusPainted(false);
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(exitButton, gbc);

        // Login button functionality
        loginButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());
            if (authenticateUser(username, password)) {
                // Create a FoodStore instance without directly showing the FoodStore page
                Map<String, Integer> foodWarehouse = new HashMap<>();
                foodWarehouse.put("Rice", 100);
                foodWarehouse.put("Bread", 50);
                FoodStore foodStore = new FoodStore(foodWarehouse, username);

                new RoleSelectionScreen(username, foodStore).showScreen(); // Pass the required objects
                dispose(); // Close the current window
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Register button functionality
        registerButton.addActionListener(e -> {
            new RegistrationScreen(userCredentials); // Open registration screen and pass user data
            dispose(); // Close the login window
        });

        // Exit button functionality
        exitButton.addActionListener(e -> {
            System.exit(0); // Exit the application
        });

        // Add the panel to the frame and display
        add(panel);
        setVisible(true);
    }

    // Method to load user credentials from a file
    private Map<String, String> loadUserCredentials() {
        Map<String, String> credentials = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    credentials.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("No users file found, starting fresh.");
        }
        return credentials;
    }

    // Method to authenticate user credentials
    private boolean authenticateUser(String username, String password) {
        return userCredentials.containsKey(username) && userCredentials.get(username).equals(password);
    }

    // Main method to start the LoginScreen
    public static void main(String[] args) {
        new LoginScreen(); // Initialize the login screen
    }
}

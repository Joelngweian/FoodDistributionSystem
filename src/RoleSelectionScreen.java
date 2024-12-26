import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.ArrayList;
import javax.swing.border.Border;
import javax.swing.BorderFactory;


public class RoleSelectionScreen extends JFrame {
    private String username;
    private FoodStore foodStore;  // Store the foodStore object
    private Map<String, Integer> foodWarehouse;
    private FoodDistributionLogScreen logScreen;

    public RoleSelectionScreen() {}

    public RoleSelectionScreen(String username ,FoodStore foodStore ) {
        this.username = username;
        this.foodStore = foodStore;  // Initialize the foodStore object
        this.foodWarehouse = foodStore.getFoodWarehouse();// Get the warehouse from foodStore

        if (this.foodStore == null)
        {
            throw new IllegalArgumentException("FoodStore instance cannot be null");
        }

        this.logScreen = new FoodDistributionLogScreen(username, foodStore);
    }

    public void showScreen() {
        // Set up the window properties
        setTitle("Select Role");
        setSize(600, 750); // Increased size for better layout, now fits Record button
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen

        // Create the panel with BoxLayout to stack buttons vertically
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));  // Stack components vertically
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center align the panel
        panel.setBackground(new Color(240, 240, 240)); // Light gray background for the panel

        // Display the username
        JLabel welcomeLabel = new JLabel("Welcome, " + username, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 40)); // Increased font size
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(welcomeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));  // Add some space after the label

        // Create role selection buttons
        JButton donorButton = createRoleButton("   Donor   ");
        JButton beneficiaryButton = createRoleButton("Beneficiary");
        JButton foodStoreButton = createRoleButton("   Staff   ");

        // Add buttons to the panel
        panel.add(donorButton);
        panel.add(Box.createRigidArea(new Dimension(0, 30))); // Add space between buttons
        panel.add(beneficiaryButton);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(foodStoreButton);
        panel.add(Box.createRigidArea(new Dimension(0, 50))); // Add space before action buttons

        // Create and add the Record button
        JButton recordButton = createActionButton("  Record  ");
        panel.add(recordButton);
        panel.add(Box.createRigidArea(new Dimension(0, 30))); // Add space between buttons

        // Create and add Log Out and Exit buttons
        JButton logOutButton = createActionButton("Log Out");
        JButton exitButton = createActionButton("   Exit   ");
        panel.add(logOutButton);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));  // Add space between action buttons
        panel.add(exitButton);

        donorButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "You selected Donor role");

            // Pass the correct FoodStore object to DonorScreen constructor
            new DonorScreen(username, new ArrayList<>(), foodStore ,logScreen);  // Pass the foodStore object
            dispose(); // Close the current window
        });

        beneficiaryButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "You selected Beneficiary role");
            new BeneficiaryScreen(foodStore.getFoodWarehouse(), username);  // Pass warehouse and username
            dispose(); // Close the current window
        });

        foodStoreButton.addActionListener(e -> {
            // Display confirmation dialog before navigating to the Food Store
            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to proceed as Food Store?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "You selected Food Store");

                // Create and show the FoodStore window
                new FoodStore(foodStore.getFoodWarehouse(), username).showScreen();  // Pass warehouse data and username

                // Close the RoleSelectionScreen window
                dispose(); // Close the current window
            }
        });

        recordButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Record button clicked");
            new RecordScreen(username).showScreen(); // Navigate to Record Screen
            dispose(); // Close the current window
        });

        logOutButton.addActionListener(e -> {
            new LoginScreen(); // Navigate back to the login page
            dispose(); // Close the current window
        });

        exitButton.addActionListener(e -> {
            System.exit(0); // Exit the application
        });

        // Add the panel to the window and display the window
        add(panel);
        setVisible(true);
    }

    // Helper method to create role buttons
    private JButton createRoleButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(600, 120));  // Increased button size (width: 600, height: 120)
        button.setFont(new Font("Arial", Font.PLAIN, 36));  // Increased font size
        button.setBackground(new Color(60, 179, 113));  // Green background
        button.setForeground(Color.WHITE);  // White text
        button.setFocusPainted(false);  // Remove focus border

        // Add a rounded border
        Border roundedBorder = BorderFactory.createLineBorder(new Color(50, 120, 50), 2);
        button.setBorder(roundedBorder);

        return button;
    }

    // Helper method to create action buttons (Log Out, Exit, Record)
    private JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(600, 120));  // Increased button size (width: 600, height: 120)
        button.setFont(new Font("Arial", Font.PLAIN, 36));  // Increased font size
        button.setBackground(new Color(220, 20, 60));  // Red background
        button.setForeground(Color.WHITE);  // White text
        button.setFocusPainted(false);  // Remove focus border

        // Add a rounded border
        Border roundedBorder = BorderFactory.createLineBorder(new Color(160, 0, 50), 2);
        button.setBorder(roundedBorder);

        return button;
    }

    public static void main(String[] args) {
        // Sample test username
        Map<String, Integer> sampleWarehouse = new java.util.HashMap<>();
        sampleWarehouse.put("Apple", 50);
        sampleWarehouse.put("Banana", 30);

        // Create a FoodStore object and pass the warehouse and username
        FoodStore foodStore = new FoodStore(sampleWarehouse, "TestUser");
        new RoleSelectionScreen("TestUser", foodStore);  // Pass the entire FoodStore object
    }
}

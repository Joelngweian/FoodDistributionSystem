import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class StaffScreen extends JFrame {
    private JTextField itemField;
    private JTextField quantityField;
    private JTextField statusField;
    private JButton distributeButton;
    private JButton returnButton;
    private Map<String, Integer> foodWarehouse;
    private String username;
    private FoodStore foodStore;
    private FoodDistributionLogScreen logScreen;

    public StaffScreen(Map<String, Integer> foodWarehouse, String username) {
        this.foodWarehouse = foodWarehouse;
        this.username = username;
        this.foodStore = new FoodStore(foodWarehouse, username);
        this.logScreen = new FoodDistributionLogScreen(username, foodStore);

        setTitle("Staff Screen");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Item:"), gbc);
        gbc.gridx = 1;
        itemField = new JTextField();
        panel.add(itemField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        quantityField = new JTextField();
        panel.add(quantityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        statusField = new JTextField();
        panel.add(statusField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        distributeButton = new JButton("Distribute Food");
        panel.add(distributeButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        JTextArea distributionArea = new JTextArea(8, 30);
        distributionArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(distributionArea);
        panel.add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        returnButton = new JButton("Return to Food Store");
        panel.add(returnButton, gbc);

        add(panel);

        distributeButton.addActionListener(e -> {
            String item = itemField.getText();
            int quantity;
            try {
                quantity = Integer.parseInt(quantityField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String status = statusField.getText();
            String locationTime = LocationTimeGenerator.generateRandomLocationTime();


            boolean isDonation = false;
            logScreen.recordFoodDistribution(item, quantity, status, locationTime , isDonation);
            distributionArea.append(String.format("Item: %s | Quantity: %d | Status: %s | Location: %s\n", item, quantity, status, locationTime));
        });

        returnButton.addActionListener(e -> {
            new FoodStore(foodWarehouse, username);
            dispose();
        });

        setVisible(true);
    }
}

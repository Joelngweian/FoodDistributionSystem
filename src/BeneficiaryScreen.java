import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class BeneficiaryScreen extends JFrame {
    private JTextField adultsField;
    private JTextField childrenField;
    private JButton distributeButton;
    private JButton returnButton;
    private Map<String, Integer> foodWarehouse;
    private String username;
    private FoodStore foodStore;
    private FoodDistributionLogScreen logScreen;
    private RecordScreen recordScreen;

    public BeneficiaryScreen(Map<String, Integer> foodWarehouse, String username) {
        this.foodWarehouse = foodWarehouse;
        this.username = username;
        this.foodStore = new FoodStore(foodWarehouse, username);
        this.logScreen = new FoodDistributionLogScreen(username, foodStore);
        this.recordScreen = new RecordScreen(username);

        setTitle("Beneficiary Screen");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Number of Adults:"), gbc);
        gbc.gridx = 1;
        adultsField = new JTextField();
        panel.add(adultsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Number of Children:"), gbc);
        gbc.gridx = 1;
        childrenField = new JTextField();
        panel.add(childrenField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        distributeButton = new JButton("Distribute Food");
        panel.add(distributeButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        JTextArea distributionArea = new JTextArea(8, 30);
        distributionArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(distributionArea);
        panel.add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        returnButton = new JButton("Return to Role Selection");
        panel.add(returnButton, gbc);

        add(panel);

        distributeButton.addActionListener(e -> {
            try {
                int adults = Integer.parseInt(adultsField.getText());
                int children = Integer.parseInt(childrenField.getText());

                String locationTime = LocationTimeGenerator.generateRandomLocationTime();
                String result = distributeFood(adults, children, locationTime);
                distributionArea.setText(result);

                if (result.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Food stock is empty, please wait for more donations.", "Out of Stock", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Your food is ready for pickup at:\n" + locationTime, "Pickup Location", JOptionPane.INFORMATION_MESSAGE);
                    int quantity = adults * 2 + children; // Example, adjust this based on your logic
                    boolean isDonation = false; // Set as false for distribution
                    recordScreen.recordUserActivity("Food Distribution", quantity, isDonation ,locationTime + " | Location: " + locationTime);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        });

        returnButton.addActionListener(e -> {
            new RoleSelectionScreen(username, foodStore).showScreen();
            dispose();
        });

        setVisible(true);
    }

    private String distributeFood(int adults, int children, String locationTime) {
        StringBuilder result = new StringBuilder();
        int totalAdults = adults * 2;
        boolean foodAvailable = false;

        for (String foodType : foodWarehouse.keySet()) {
            int totalFood = foodWarehouse.get(foodType);
            int neededFood = totalAdults + children;
            int foodToDistribute = Math.min(neededFood, totalFood);

            if (foodToDistribute > 0) {
                result.append(foodType).append(": ").append(foodToDistribute).append(" items\n");
                foodWarehouse.put(foodType, totalFood - foodToDistribute);
                foodAvailable = true;

                String action = "Distributed at " + locationTime;
                logScreen.recordFoodDistribution(foodType, foodToDistribute, action, locationTime , false);
            }
        }
        return foodAvailable ? result.toString() : "";
    }
}
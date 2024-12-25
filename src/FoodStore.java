import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class FoodStore extends JFrame {
    private Map<String, Integer> foodWarehouse;
    private String storeName;
    private FoodStore foodStore;
    private RecordScreen recordScreen;

    public FoodStore(Map<String, Integer> foodWarehouse, String storeName) {
        //System.out.println("FoodStore initialized with warehouse: " + foodWarehouse);
        this.foodWarehouse = foodWarehouse;
        this.storeName = storeName;
        //this.recordScreen = new RecordScreen(storeName);
    }

    public void showScreen() {
        setTitle("Food Store");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel panel = new JPanel(new BorderLayout());

        // Center: Food List Section
        JLabel foodListLabel = new JLabel("Donated Items:");
        DefaultListModel<String> foodListModel = new DefaultListModel<>();
        JList<String> foodList = new JList<>(foodListModel);
        JScrollPane scrollPane = new JScrollPane(foodList);

        // Populate the list with current foodWarehouse data
        updateFoodList(foodListModel);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(foodListLabel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // South: Buttons
        JPanel southPanel = new JPanel(new FlowLayout());

        // Return button
        JButton returnButton = new JButton("Return to Role Selection");
        returnButton.addActionListener(e -> {
            // Pass the foodWarehouse to RoleSelectionScreen
            new RoleSelectionScreen(storeName, this).showScreen();
            dispose(); // Close the current window
        });

        // View Food Distribution Log button
        JButton viewFoodLogButton = new JButton("View Food Distribution Log");
        viewFoodLogButton.addActionListener(e -> {
            dispose(); // Close the current FoodStore window
            new FoodDistributionLogScreen(storeName, this).showScreen(); // Open the Food Distribution Log screen
        });

        // Add the buttons to the south panel
        southPanel.add(returnButton);
        southPanel.add(viewFoodLogButton);

        // Add components to the main panel
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(panel);
        setVisible(true);
    }

    // Method to update the food list displayed in the UI
    private void updateFoodList(DefaultListModel<String> foodListModel) {
        // Clear the existing list
        foodListModel.clear();

        // Add the items from foodWarehouse to the list model
        for (Map.Entry<String, Integer> entry : foodWarehouse.entrySet()) {
            String item = entry.getKey() + ": " + entry.getValue();
            foodListModel.addElement(item);
        }
    }

    public void updateFoodWarehouse(String foodItem, int quantity) {
        // Check if food item exists in warehouse and update its quantity
        if (foodWarehouse.containsKey(foodItem)) {
            foodWarehouse.put(foodItem, foodWarehouse.get(foodItem) + quantity);
        } else {
            foodWarehouse.put(foodItem, quantity);
        }
    }

    public Map<String, Integer> getFoodWarehouse() {
        return foodWarehouse;
    }

    // Method to log food distribution activities (add this method)
    public void addToFoodDistributionLog(String foodType, int quantity, String locationTime) {
        // Here, you could log the distribution in a file, database, or memory.
        System.out.println("Food Distributed: " + foodType + ", Quantity: " + quantity + ", Location & Time: " + locationTime);
    }

    public static void main(String[] args) {
        // Sample data for testing
        Map<String, Integer> initialFoodStock = Map.of(
                "Rice", 100,
                "Canned Food", 50,
                "Flour", 30
        );

        new FoodStore(initialFoodStock, "TestUser");
    }
}
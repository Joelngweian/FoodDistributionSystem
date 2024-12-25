import src.DonationItem;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DonorScreen extends JFrame {
    private String username;
    private List<DonationItem> donationList;
    private JList<String> donationJList;
    private DefaultListModel<String> listModel;
    private FoodStore foodstore;
    private FoodDistributionLogScreen logScreen;

    // Constructor to initialize the screen with correct types
    public DonorScreen(String username, List<DonationItem> donationList, FoodStore foodstore ,FoodDistributionLogScreen logScreen) {
        this.username = username;
        this.donationList = donationList;
        this.foodstore = foodstore;
        this.logScreen = new FoodDistributionLogScreen(username, foodstore);

        setTitle("Donor Screen");
        setSize(600, 500); // Adjust window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Set padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel typeLabel = new JLabel("Select Donation Type:");
        String[] donationTypes = {"Grains", "Meat", "Water", "Clothes", "Vegetables", "Fruits"};
        JComboBox<String> typeComboBox = new JComboBox<>(donationTypes);

        JLabel detailsLabel = new JLabel("Enter Donation Details:");
        JTextField detailsField = new JTextField(15);

        JLabel quantityLabel = new JLabel("Enter Quantity:");
        JTextField quantityField = new JTextField(15);

        // Add components to input section
        gbc.gridx = 0; gbc.gridy = 0; panel.add(typeLabel, gbc);
        gbc.gridx = 1; panel.add(typeComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(detailsLabel, gbc);
        gbc.gridx = 1; panel.add(detailsField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(quantityLabel, gbc);
        gbc.gridx = 1; panel.add(quantityField, gbc);

        // Buttons section
        JButton addButton = new JButton("Add Donation");
        JButton deleteButton = new JButton("Delete Donation");
        JButton confirmButton = new JButton("Confirm Donation");
        JButton returnButton = new JButton("Return to Role Selection");

        gbc.gridx = 0; gbc.gridy = 3; panel.add(addButton, gbc);
        gbc.gridx = 1; panel.add(deleteButton, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(confirmButton, gbc);
        gbc.gridx = 1; panel.add(returnButton, gbc);

        // Donation list section
        listModel = new DefaultListModel<>();
        donationJList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(donationJList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Donation List"));

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1; gbc.weighty = 1;
        panel.add(scrollPane, gbc);

        add(panel);

        // Add donation action
        addButton.addActionListener(e -> {
            String type = (String) typeComboBox.getSelectedItem();
            String details = detailsField.getText();
            int quantity;
            try {
                quantity = Integer.parseInt(quantityField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!details.isEmpty()) {
                DonationItem item = new DonationItem(type, details, quantity);
                donationList.add(item);
                listModel.addElement(item.getCategory() + ": " + item.getDetails() + " (Quantity: " + item.getQuantity() + ")");
                detailsField.setText("");
                quantityField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Please enter donation details.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        confirmButton.addActionListener(e -> {
            if (!donationList.isEmpty()) {
                for (DonationItem item : donationList) {
                    // 获取捐赠信息
                    String locationTime = LocationTimeGenerator.generateRandomLocationTime();

                    // 记录捐赠信息到食物分配日志
                    logScreen.recordFoodDistribution(item.getCategory(), item.getQuantity(), "Successful", locationTime, true); // isDonation = true

                    // 更新 FoodStore 仓库信息
                    foodstore.updateFoodWarehouse(item.getCategory(), item.getQuantity());
                }

                // 清除捐赠列表和 UI
                donationList.clear();
                listModel.clear();

                // 重新加载文件并更新显示
                logScreen.loadLog();  // 假设你有一个方法来重新加载文件
                listModel.clear();  // 清空列表
                List<String[]> logEntries = logScreen.getLogEntries();
                if (logEntries != null && !logEntries.isEmpty())
                {
                    for (String[] entry : logEntries)
                    {
                    System.out.println(String.join(", ", entry));  // 假设 getLogEntries() 返回从文件加载的日志数据
                    }
                }
                    else
                    {
                        System.out.println("No log entries found.");
                    }

                String location = getRandomLocation();
                JOptionPane.showMessageDialog(this, "Donation successful! Your donation will be delivered to: " + location, "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No items to donate. Please add some items.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedIndex = donationJList.getSelectedIndex(); // 获取选中项的索引

            if (selectedIndex == -1) { // 检查是否选中某一项
                JOptionPane.showMessageDialog(this, "Please select an item to delete!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 从 donationList 和 listModel 中删除选中的项
            donationList.remove(selectedIndex); // 删除 donationList 中的对应项
            listModel.remove(selectedIndex);   // 删除 listModel 中的对应项

            JOptionPane.showMessageDialog(this, "Selected donation has been removed.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        returnButton.addActionListener(e -> {
            if (foodstore == null) {
                JOptionPane.showMessageDialog(this, "Error: FoodStore is not initialized", "Error", JOptionPane.ERROR_MESSAGE);
                return;  // Exit the method if foodstore is null
            }
            // Proceed if foodstore is not null
            new RoleSelectionScreen(username, foodstore).showScreen();
            dispose();
        });

        setVisible(true);
    }

    private String getRandomLocation() {
        List<String> locations = Arrays.asList(
                "Johor, Skudai Hall",
                "Johor, Gelang Patah Hall",
                "Kuala Lumpur, KL Convention Center",
                "Penang, Georgetown Hall",
                "Selangor, Shah Alam Stadium");

        List<String> times = Arrays.asList(
                "December 20, 2024, 10:00 AM",
                "December 21, 2024, 2:00 PM",
                "December 22, 2024, 11:00 AM",
                "December 23, 2024, 1:00 PM",
                "December 24, 2024, 4:00 PM");

        Random rand = new Random();
        return locations.get(rand.nextInt(locations.size())) + " / " + times.get(rand.nextInt(times.size()));
    }

    public static void main(String[] args) {
        Map<String, Integer> initialFoodStock = new HashMap<>();
        initialFoodStock.put("Rice", 100);
        initialFoodStock.put("Canned Food", 50);
        initialFoodStock.put("Flour", 30);

        // Example of navigating to DonorScreen
        FoodStore foodStoreUI = new FoodStore(initialFoodStock, "TestStore");
        System.out.println("FoodStore created: " + foodStoreUI);
        if (foodStoreUI == null)
        {
            System.out.println("FoodStore is null!");
        }
        List<DonationItem> donationList = new ArrayList<>();
        FoodStore foodstore = new FoodStore(initialFoodStock , "TestStore");
        FoodDistributionLogScreen logScreen = new FoodDistributionLogScreen("TestUser", foodStoreUI);
        DonorScreen donorScreen = new DonorScreen("TestUser", new ArrayList<>(), foodStoreUI , logScreen);
    }
}
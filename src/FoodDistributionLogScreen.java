import javax.swing.*;
import java.io.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.table.TableColumn;


public class FoodDistributionLogScreen extends JFrame {
    private String currentUser;
    private FoodStore foodStore;
    private JTable logTable;
    private DefaultTableModel tableModel;
    private List<String[]> logEntries = new ArrayList<>();

    public FoodDistributionLogScreen(String currentUser, FoodStore foodStore) {
        this.currentUser = currentUser;
        this.foodStore = foodStore;
        this.logEntries = new ArrayList<>();
    }

    public void showScreen() {
        loadLog();

        setTitle("Food Distribution Log");
        setSize(800, 400); // Adjust the size to fit the table properly
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the panel with BorderLayout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create the table with column names
        String[] columnNames = {"Timestamp", "User","Action" ,  "Item", "Quantity", "Status", "Location"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        logTable = new JTable(tableModel);
        logTable.setFillsViewportHeight(true); // Make the table take up the available space
        TableColumn column;
        for (int i = 0; i < logTable.getColumnCount(); i++) {
            column = logTable.getColumnModel().getColumn(i);
            switch (i) {
                case 0: column.setPreferredWidth(125); break;//TimeStamp
                case 1: column.setPreferredWidth(50); break;//User
                case 2: column.setPreferredWidth(90); break;//Action
                case 3: column.setPreferredWidth(50); break;//Item
                case 4: column.setPreferredWidth(50); break;//Quantity
                case 5: column.setPreferredWidth(60); break;//Status
                case 6: column.setPreferredWidth(400); break;//Location
                default: column.setPreferredWidth(100); break;
            }
        }
        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(logTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Create the "Return to Food Store" button
        JButton returnButton = new JButton("Return to Food Store");
        returnButton.setPreferredSize(new Dimension(200, 40));
        buttonPanel.add(returnButton);

        // Create the "Delete" button
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.setPreferredSize(new Dimension(200, 40));
        buttonPanel.add(deleteButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Load the log data into the table
        loadLog();

        // Return button action to dispose of the window
        returnButton.addActionListener(e -> {
            dispose();
            new FoodStore(foodStore.getFoodWarehouse(), currentUser); // Return to the Food Store screen
        });

        // Delete button action to delete the selected row
        deleteButton.addActionListener(e -> deleteSelectedLog());

        add(panel);
        setVisible(true);
    }

    public void loadLog() {
        // 确保 tableModel 已初始化
        if (tableModel == null) {
            System.err.println("Error: tableModel is not initialized.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader("log/food_distribution_log.txt"))) {
            String line;
            logEntries.clear(); // 清空旧的日志条目
            while ((line = reader.readLine()) != null) {
                // 按竖线分隔并解析日志条目
                String[] logParts = line.split(" \\| ");
                if (logParts.length == 7) { // 确保日志条目包含 7 部分
                    String timestamp = logParts[0].trim();
                    String user = logParts[1].replace("User: ", "").trim();
                    String action = logParts[2].replace("Action: ", "").trim();
                    String item = logParts[3].replace("Item: ", "").trim();
                    String quantity = logParts[4].replace("Quantity: ", "").trim();
                    String status = logParts[5].replace("Status: ", "").trim();
                    String location = logParts[6].replace("Location: ", "").trim();

                    // 添加到表格模型
                    tableModel.addRow(new Object[]{timestamp, user, action, item, quantity, status, location});

                    // 添加到 logEntries
                    logEntries.add(new String[]{timestamp, user, action, item, quantity, status, location});
                } else {
                    System.err.println("Invalid log format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> getLogEntries() {
        return new ArrayList<>(logEntries); // 返回日志条目的副本以防止外部修改
    }

    private void deleteSelectedLog() {
        // Get the selected row
        int selectedRow = logTable.getSelectedRow();
        if (selectedRow != -1) {
            // Remove the row from the table model
            tableModel.removeRow(selectedRow);

            // Rebuild the log file without the deleted entry
            rebuildLogFile();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rebuildLogFile() {
        // Rebuild the log file from the current table data
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("log/food_distribution_log.txt"))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                StringBuilder logEntry = new StringBuilder();
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    logEntry.append(tableModel.getValueAt(i, j));
                    if (j < tableModel.getColumnCount() - 1) {
                        logEntry.append(" | ");
                    }
                }
                writer.write(logEntry.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLogToFile(String logEntry) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("log/food_distribution_log.txt", true))) {
            writer.write(logEntry);
            writer.newLine(); // 换行
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to log file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void recordFoodDistribution(String category, int quantity, String status, String locationTime, boolean isDonation) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("log/food_distribution_log.txt", true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String user = this.currentUser;  // 或者其他相关的用户名
            String action = isDonation ? "Donation" : "Distribution";

            if (status == null || (!status.equals("Successful") && !status.equals("Unsuccessful"))) {
                status = "Successful";
            }
            // 构造日志条目
            String logEntry = String.format("%s | User: %s | Action: %s | Item: %s | Quantity: %d | Status: %s | Location: %s" ,
                                            timestamp, user, action, category, quantity, status, locationTime);


            // 添加到文件（确保这一部分已经正确）
            writeLogToFile(logEntry);
            // 将日志数据拆分并添加到表格模型
            String[] logParts = logEntry.split(" \\| ");
            if (tableModel != null) {
                tableModel.addRow(logParts);
            } else {
                System.err.println("tableModel is not initialized.");
            }


            // 刷新表格 UI
            SwingUtilities.invokeLater(() -> tableModel.fireTableDataChanged());
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception (log it, show an error message, etc.)
        }
    }
        public class FoodDistributionApp {
            public static void main(String[] args) {
                // Create a sample food warehouse for the FoodStore
                Map<String, Integer> foodWarehouse = new HashMap<>();
                foodWarehouse.put("Rice", 100);
                foodWarehouse.put("Bread", 50);

                // Create a FoodStore instance with a sample warehouse and a username
                FoodStore foodStore = new FoodStore(foodWarehouse, "TestUser");

                // Now instantiate FoodDistributionLogScreen with the currentUser and foodStore
                new FoodDistributionLogScreen("TestUser", foodStore);
            }
    }
}
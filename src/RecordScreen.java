import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RecordScreen extends JFrame {
    private String currentUser;
    private JTable logTable;
    private DefaultTableModel tableModel;

    public RecordScreen(String currentUser) {
        this.currentUser = currentUser;
    }

    public void showScreen() {
        setTitle("User Activity Log");
        setSize(800, 400); // Adjust the size to fit the table properly
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Initialize tableModel here
        String[] columnNames = {"Timestamp", "User", "Action", "Details"};
        tableModel = new DefaultTableModel(columnNames, 0); // Ensure tableModel is initialized here

        // Create the JTable with tableModel
        logTable = new JTable(tableModel);
        logTable.setFillsViewportHeight(true); // Make the table take up the available space

        // Create the panel and layout
        JScrollPane scrollPane = new JScrollPane(logTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton returnButton = new JButton("Return");
        returnButton.setPreferredSize(new Dimension(200, 40));
        panel.add(returnButton, BorderLayout.SOUTH);

        loadLog();

        // Load the log data into the table
        returnButton.addActionListener(e -> dispose());
        new RoleSelectionScreen().showScreen();
        add(panel);
        setVisible(true);
    }

    private void loadLog() {
        try (BufferedReader reader = new BufferedReader(new FileReader("log/user_activity_log.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming each log entry is separated by a pipe (|), as in the recordUserActivity method
                String[] logParts = line.split(" \\| ");
                if (logParts.length == 4) {
                    // Add the log entry as a row in the table
                    tableModel.addRow(logParts);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recordUserActivity(String category, int quantity, boolean isDonation ,  String locationTime) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("log/user_activity_log.txt", true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String user = this.currentUser;  // 获取当前用户名
            String action = isDonation ? "Donation" : "Distribution";  // 根据是否为捐赠判断动作

            // 确定状态为成功
            String status = "Successful"; // 状态固定为成功

            // 构造日志条目
            String logEntry = String.format("%s | User: %s | Action: %s | Item: %s | Quantity: %d | Donation:%b | Location: %s",
                    timestamp, user, action, category, quantity, isDonation , locationTime);

            // 输出到日志文件
            writer.write(logEntry);
            writer.newLine();  // 换行

            System.out.println("User activity log entry: " + logEntry); // 调试信息

        } catch (IOException e) {
            e.printStackTrace(); // 处理异常
        }
    }


    public static void main(String[] args)
    {
        // Test the RecordScreen functionality
        new RecordScreen("TestUser");
    }
}

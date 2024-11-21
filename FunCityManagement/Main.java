import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Main extends JFrame {
    private JTabbedPane tabbedPane;

    public Main() {
        setTitle("Fun City Management System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        
        tabbedPane.add("Customer Management", new CustomerPanel());
        tabbedPane.add("Machine Management", new MachinePanel());
        tabbedPane.add("Ticket Management", new TicketPanel());
        tabbedPane.add("Points Management", new PointsPanel());
        tabbedPane.add("Game Management", new GamePanel());

        add(tabbedPane);
    }

    public static void main(String[] args) {
        // Show the login page first
        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}

class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    public LoginPage() {
        
        setTitle("Login Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  
        
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 
        
        
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);

        loginButton = new JButton("Login");

        messageLabel = new JLabel("");
        
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        add(usernameLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        add(passwordLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        add(passwordField, gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbc);

        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.CENTER;
        add(messageLabel, gbc);

        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                
                boolean loginSuccess = LoginManagement.validateLogin(username, password);

                if (loginSuccess) {
                    messageLabel.setText("Login Successful");
                    messageLabel.setForeground(Color.GREEN);
                    
                    new Main().setVisible(true);
                    dispose();  
                } else {
                    messageLabel.setText("Invalid username or password");
                    messageLabel.setForeground(Color.RED);
                }
            }
        });
    }
}

class LoginManagement {
    public static boolean validateLogin(String username, String password) {
        
        return "admin".equals(username) && "password".equals(password);  
    }
}


class MachinePanel extends JPanel {
    private JTextField locationField, ticketCountField;
    private JCheckBox dispensesCheckbox;
    private JComboBox<String> statusComboBox;
    private DefaultTableModel tableModel;
    private JTable machineTable;

    public MachinePanel() {
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        
        JPanel locationPanel = createInputPanel("Location:", locationField = new JTextField());
        JPanel ticketCountPanel = createInputPanel("Ticket Count:", ticketCountField = new JTextField());
        JPanel dispensesPanel = createInputPanel("Dispenses Tickets:", dispensesCheckbox = new JCheckBox());
        JPanel statusPanel = createInputPanel("Status:", statusComboBox = new JComboBox<>(new String[]{"Operational", "Maintenance"}));

        inputPanel.add(locationPanel);
        inputPanel.add(ticketCountPanel);
        inputPanel.add(dispensesPanel);
        inputPanel.add(statusPanel);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Machine");
        JButton viewButton = new JButton("View Machines");
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);

        addButton.addActionListener(new AddMachineAction());
        viewButton.addActionListener(new ViewMachinesAction());

        inputPanel.add(buttonPanel);
        add(inputPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Machine ID", "Location", "Ticket Count", "Dispenses", "Status"}, 0);
        machineTable = new JTable(tableModel);
        add(new JScrollPane(machineTable), BorderLayout.CENTER);
    }

    private JPanel createInputPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 20));
        field.setPreferredSize(new Dimension(200, 25));
        panel.add(label);
        panel.add(field);
        return panel;
    }

    private class AddMachineAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String location = locationField.getText();
            int ticketCount = Integer.parseInt(ticketCountField.getText());
            boolean dispensesTickets = dispensesCheckbox.isSelected();
            String status = (String) statusComboBox.getSelectedItem();

            MachineManagement.addMachine(location, ticketCount, dispensesTickets, status);
            JOptionPane.showMessageDialog(MachinePanel.this, "Machine added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            locationField.setText("");
            ticketCountField.setText("");
            dispensesCheckbox.setSelected(false);
            statusComboBox.setSelectedIndex(0);
        }
    }

    private class ViewMachinesAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            tableModel.setRowCount(0);
            Object[][] machines = MachineManagement.getMachines();
            for (Object[] machine : machines) {
                tableModel.addRow(machine);
            }
        }
    }
}
class TicketPanel extends JPanel {
    private JTextField customerIdField, machineIdField;
    private DefaultTableModel tableModel;
    private JTable ticketTable;

    public TicketPanel() {
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        
        JPanel customerIdPanel = createInputPanel("Customer ID:", customerIdField = new JTextField());
        JPanel machineIdPanel = createInputPanel("Machine ID:", machineIdField = new JTextField());

        inputPanel.add(customerIdPanel);
        inputPanel.add(machineIdPanel);


        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Ticket");
        JButton viewButton = new JButton("View Tickets");
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);

        addButton.addActionListener(new AddTicketAction());
        viewButton.addActionListener(new ViewTicketsAction());

        inputPanel.add(buttonPanel);
        add(inputPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Ticket ID", "Customer ID", "Machine ID"}, 0);
        ticketTable = new JTable(tableModel);
        add(new JScrollPane(ticketTable), BorderLayout.CENTER);
    }

    private JPanel createInputPanel(String labelText, JTextField field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 20));
        field.setPreferredSize(new Dimension(200, 25));
        panel.add(label);
        panel.add(field);
        return panel;
    }

    private class AddTicketAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int customerId = Integer.parseInt(customerIdField.getText());
            int machineId = Integer.parseInt(machineIdField.getText());

            TicketManagement.addTicket(customerId, machineId);
            JOptionPane.showMessageDialog(TicketPanel.this, "Ticket added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            customerIdField.setText("");
            machineIdField.setText("");
        }
    }

    private class ViewTicketsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            tableModel.setRowCount(0);
            Object[][] tickets = TicketManagement.getTickets();
            for (Object[] ticket : tickets) {
                tableModel.addRow(ticket);
            }
        }
    }
}
class PointsPanel extends JPanel {
    private JTextField customerIdField, pointsField;
    private DefaultTableModel tableModel;
    private JTable pointsTable;

    public PointsPanel() {
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

  
        JPanel customerIdPanel = createInputPanel("Customer ID:", customerIdField = new JTextField());
        JPanel pointsPanel = createInputPanel("Points Earned:", pointsField = new JTextField());

        inputPanel.add(customerIdPanel);
        inputPanel.add(pointsPanel);


        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Points");
        JButton viewButton = new JButton("View Points");
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);

        addButton.addActionListener(new AddPointsAction());
        viewButton.addActionListener(new ViewPointsAction());

        inputPanel.add(buttonPanel);
        add(inputPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Points ID", "Customer ID", "Points"}, 0);
        pointsTable = new JTable(tableModel);
        add(new JScrollPane(pointsTable), BorderLayout.CENTER);
    }

    private JPanel createInputPanel(String labelText, JTextField field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 20));
        field.setPreferredSize(new Dimension(200, 25));
        panel.add(label);
        panel.add(field);
        return panel;
    }

    private class AddPointsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int customerId = Integer.parseInt(customerIdField.getText());
            int pointsEarned = Integer.parseInt(pointsField.getText());

            PointsManagement.addPoints(customerId, pointsEarned);
            JOptionPane.showMessageDialog(PointsPanel.this, "Points added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            customerIdField.setText("");
            pointsField.setText("");
        }
    }

    private class ViewPointsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            tableModel.setRowCount(0);
            Object[][] pointsData = PointsManagement.getPoints();
            for (Object[] points : pointsData) {
                tableModel.addRow(points);
            }
        }
    }
}

class GamePanel extends JPanel {
    private JTextField nameField, locationField, statusField;
    private DefaultTableModel tableModel;
    private JTable gameTable;

    public GamePanel() {
        setLayout(new BorderLayout(10, 10)); 

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 

        JPanel namePanel = createInputPanel("Name:", nameField = new JTextField());
        JPanel locationPanel = createInputPanel("Location:", locationField = new JTextField());
        JPanel statusPanel = createInputPanel("Status:", statusField = new JTextField());

        inputPanel.add(namePanel);
        inputPanel.add(locationPanel);
        inputPanel.add(statusPanel);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Game");
        JButton viewButton = new JButton("View Games");
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);

        addButton.addActionListener(new AddGameAction());
        viewButton.addActionListener(new ViewGamesAction());

        inputPanel.add(buttonPanel);

        add(inputPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Game ID", "Name", "Location", "Status"}, 0);
        gameTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(gameTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createInputPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(80, 20));
        textField.setPreferredSize(new Dimension(200, 25));
        panel.add(label);
        panel.add(textField);
        return panel;
    }

    
    private class AddGameAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();
            String location = locationField.getText().trim();
            String status = statusField.getText().trim();

            if (name.isEmpty() || location.isEmpty() || status.isEmpty()) {
                JOptionPane.showMessageDialog(GamePanel.this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            
            GameManagement.addGame(name, location, status);
            JOptionPane.showMessageDialog(GamePanel.this, "Game added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            
            nameField.setText("");
            locationField.setText("");
            statusField.setText("");
        }
    }

    
    private class ViewGamesAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            tableModel.setRowCount(0);
            
            Object[][] games = GameManagement.getGames();
            for (Object[] game : games) {
                tableModel.addRow(game);
            }
        }
    }
}
class CustomerPanel extends JPanel {
    private JTextField nameField;
    private JTextField emailField;
    private JTextField pointsField; 
    private JTextField balanceField; 
    private JTable customerTable;
    private DefaultTableModel tableModel;

    public CustomerPanel() {
        setLayout(new BorderLayout(10, 10));  

        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS)); 
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adding some padding around the form

        
        JPanel namePanel = createInputPanel("Name:", nameField = new JTextField());
        JPanel emailPanel = createInputPanel("Email:", emailField = new JTextField());
        JPanel pointsPanel = createInputPanel("Points:", pointsField = new JTextField());
        JPanel balancePanel = createInputPanel("Balance:", balanceField = new JTextField());

        inputPanel.add(namePanel);
        inputPanel.add(emailPanel);
        inputPanel.add(pointsPanel);
        inputPanel.add(balancePanel);

        
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Customer");
        JButton viewButton = new JButton("View Customers");
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);

        
        addButton.addActionListener(new AddCustomerAction());
        viewButton.addActionListener(new ViewCustomersAction());

        inputPanel.add(buttonPanel);

        
        add(inputPanel, BorderLayout.NORTH);

        
        tableModel = new DefaultTableModel(new String[]{"Customer ID", "Name", "Email", "Points", "Balance"}, 0);
        customerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    
    private JPanel createInputPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(80, 20));
        textField.setPreferredSize(new Dimension(200, 25));
        panel.add(label);
        panel.add(textField);
        return panel;
    }

    
    private class AddCustomerAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String pointsStr = pointsField.getText().trim(); 
            String balanceStr = balanceField.getText().trim(); 

            if (name.isEmpty() || email.isEmpty() || pointsStr.isEmpty() || balanceStr.isEmpty()) {
                JOptionPane.showMessageDialog(CustomerPanel.this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int points = Integer.parseInt(pointsStr); 
                double balance = Double.parseDouble(balanceStr); 

                
                CustomerManagement.addCustomer(name, email, points, balance);
                JOptionPane.showMessageDialog(CustomerPanel.this, "Customer added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

                
                nameField.setText("");
                emailField.setText("");
                pointsField.setText("");
                balanceField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(CustomerPanel.this, "Please enter valid numeric values for points and balance.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    
    private class ViewCustomersAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            tableModel.setRowCount(0);
            
            Object[][] customers = CustomerManagement.getCustomers();
            for (Object[] customer : customers) {
                tableModel.addRow(customer);
            }
        }
    }
}

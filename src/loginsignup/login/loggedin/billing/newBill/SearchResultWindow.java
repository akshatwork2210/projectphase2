package loginsignup.login.loggedin.billing.newBill;

import mainpack.MyClass;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Vector;

public class SearchResultWindow extends JFrame {
    private JPanel panel1;
    private JButton backButton;
    private JButton button2;
    private JTable orderSlipTable;
    private JLabel slipID;
    private JLabel cutomerName;
    private JLabel panaTypeLabel;
    int ID;


    public String getCutomerName() {
        String name;
        try {
            Statement stmt = MyClass.C.createStatement();
            ResultSet rs = stmt.executeQuery("select customer_name from order_slips where slip_id=" + ID + ";");
            if (rs.next()) {
                name = rs.getString(1);
                return name;
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }


    }

    public SearchResultWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(panel1);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyClass.newBill.setExtendedState(JFrame.MAXIMIZED_BOTH);
                MyClass.newBill.getBackButton().setEnabled(true);
                dispose();
            }
        });
        orderSlipTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    appendToBill(ID);

                }
            }
        });
    }

    private void appendToBill(int BID) {


        DefaultTableModel model = (DefaultTableModel) orderSlipTable.getModel();
        int selectedRow = orderSlipTable.getSelectedRow();
        String itemName = model.getValueAt(selectedRow, model.findColumn("Item Name")).toString();
        int maxQuantity = Integer.parseInt(model.getValueAt(selectedRow, model.findColumn("Quantity")).toString());

        int selectedQuantity = showPrompt(maxQuantity, itemName);
        if (selectedQuantity < 0) return;
        String query = "UPDATE order_slips "
                + "SET quantity = quantity - ? "
                + "WHERE slip_id = ? AND item_id = ?";
        try {
            Connection con = MyClass.newBill.getTransacTemp();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, selectedQuantity);
            statement.setInt(2, BID);
            statement.setInt(3, snoToItemIdMap.get(Integer.parseInt(orderSlipTable.getValueAt(orderSlipTable.getSelectedRow(), ((DefaultTableModel) orderSlipTable.getModel()).findColumn("sno")).toString())));
            statement.executeUpdate();
            JOptionPane.showMessageDialog(MyClass.searchResultWindow, "succesfully updated data");
            fetchData((DefaultTableModel) orderSlipTable.getModel());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    int showPrompt(int maxQuantity, String itemName) {
        String quantity = JOptionPane.showInputDialog(
                null,
                "How many " + itemName + "s would you like to include?",
                "Quantity Selection",
                JOptionPane.QUESTION_MESSAGE
        );

        try {
            int selectedQuantity = Integer.parseInt(quantity);
            if (selectedQuantity > 0 && selectedQuantity <= maxQuantity) {
                JOptionPane.showMessageDialog(null,
                        selectedQuantity + " " + itemName + "(s) added to the bill.");
                return selectedQuantity;
            } else {
                JOptionPane.showMessageDialog(null,
                        "Invalid quantity! Please enter a value between 1 and " + maxQuantity,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return -2;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "Please enter a valid number.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    public String getPanaType() {
        try {
            Statement statement = MyClass.C.createStatement();
            ResultSet rs = statement.executeQuery("select slip_type from order_slips where slip_id=" + ID + ";");
            if (rs.next()) {
                return rs.getString(1);
            } else {
                JOptionPane.showMessageDialog(this, "error occured panatype not found");
                return null;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "SQL EXCEPTION OCCURED");
            e.printStackTrace();
            return null;
        }

    }

    public SearchResultWindow(int bID) {
        this();
        this.ID = bID;
        slipID.setText("order slip id: " + ID);
        cutomerName.setText(getCutomerName());
        panaTypeLabel.setText(getPanaType());

        String[] columnNames = {"sno", "design id", "Item Name", "Quantity", "Plating", "Raw Material Cost", "Other Details"};//jtable content
        DefaultTableModel model = new DefaultTableModel(columnNames, 1) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderSlipTable.setModel(model);
        fetchData(model);
        orderSlipTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && orderSlipTable.getSelectedRow() != -1) {
                    appendToBill(ID);
                }
            }
        });
        pack();

    }

    HashMap<Integer, Integer> snoToItemIdMap;

    private void fetchData(DefaultTableModel model) {
        model.setRowCount(0);
        String query = "SELECT customer_name,slip_id,item_id,sno,design_id, item_name, quantity, plating_grams, raw_material_price, other_details " +
                "FROM order_slips WHERE slip_id = ? Order by item_id";

        try {
//            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password");
            PreparedStatement pstmt = MyClass.newBill.getTransacTemp().prepareStatement(query);
            pstmt.setInt(1, ID); // Use the slip_id provided
            ResultSet rs = pstmt.executeQuery();
            snoToItemIdMap = new HashMap<>();

            int slipID = 0;
            String customerName = "";

            while (rs.next()) {
                slipID = rs.getInt("slip_id");
                customerName = rs.getString("customer_name");
                int sno = rs.getInt("sno");
                int itemId = rs.getInt("item_id");

                snoToItemIdMap.put(sno, itemId); //
                model.addRow(new Object[]{
// Map sno to item_id

                        rs.getString("sno"),
                        rs.getString("design_id"),
                        rs.getString("item_name"),
                        rs.getInt("quantity"),
                        rs.getBigDecimal("plating_grams"),
                        rs.getBigDecimal("raw_material_price"),
                        rs.getString("other_details")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace(); // For debugging; consider proper error handling
        }

    }
}

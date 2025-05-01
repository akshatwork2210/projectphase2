package loginsignup.login.loggedin.billing.newBill;

import mainpack.MyClass;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.HashMap;
import java.util.Vector;

public class SearchResultWindow extends JFrame {
    private JPanel panel1;

    public void setSearchFlag(boolean searchFlag) {
        this.searchFlag = searchFlag;
    }

    public boolean isSearchFlag() {
        return searchFlag;
    }

    private boolean searchFlag=false;

    private JButton backButton;
    private JButton button2;
    private JTable orderSlipTable;
    private JLabel slipID;
    private JLabel cutomerName;
    private JLabel panaTypeLabel;
    int ID;

    public DefaultTableModel getTableModel() {
        return (DefaultTableModel) orderSlipTable.getModel();
    }

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
            throw new RuntimeException();
        }


    }

    public SearchResultWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(panel1);

        backButton.addActionListener(e -> {
            MyClass.newBill.setExtendedState(JFrame.MAXIMIZED_BOTH);
            MyClass.newBill.getBackButton().setEnabled(true);
            MyClass.newBill.getSubmitButton().setEnabled(true);

            dispose();
        });
        orderSlipTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    pushDetails(doSqlUpadates(ID));

                }
            }
        });
    }

    private Vector<Integer> doSqlUpadates(int BID) //this will also writen values to append to the bill
    {
        Vector<Integer> detailsToPush = new Vector<>();

        DefaultTableModel model = (DefaultTableModel) orderSlipTable.getModel();
        int selectedRow = orderSlipTable.getSelectedRow();
        String itemName = model.getValueAt(selectedRow, model.findColumn("Item Name")).toString();
        int maxQuantity = Integer.parseInt(model.getValueAt(selectedRow, model.findColumn("Quantity")).toString());

        int selectedQuantity = showPrompt(maxQuantity, itemName);

        if (selectedQuantity < 0) return null;
        String query = "UPDATE order_slips " + "SET billed_quantity = billed_quantity + ? " + "WHERE slip_id = ? AND item_id = ?";
        try {
            Connection con = MyClass.newBill.getTransacTemp();
            PreparedStatement statement = con.prepareStatement(query);
            int itemid = snoToItemIdMap.get(Integer.parseInt(orderSlipTable.getValueAt(orderSlipTable.getSelectedRow(), ((DefaultTableModel) orderSlipTable.getModel()).findColumn("sno")).toString()));
            statement.setInt(1, selectedQuantity);
            statement.setInt(2, BID);
            statement.setInt(3, itemid);
            statement.executeUpdate();
            detailsToPush.add(itemid);
            detailsToPush.add(selectedQuantity);
            JOptionPane.showMessageDialog(MyClass.searchResultWindow, "succesfully updated data");
            fetchData((DefaultTableModel) orderSlipTable.getModel());

        } catch (SQLException | NullPointerException e) {
            throw new RuntimeException();
        }
        return detailsToPush;
    }

    int showPrompt(int maxQuantity, String itemName) {
        String quantity = JOptionPane.showInputDialog(null, "How many " + itemName + "s would you like to include?", "Quantity Selection", JOptionPane.QUESTION_MESSAGE);

        try {
            int selectedQuantity = Integer.parseInt(quantity);
            if (selectedQuantity > 0 && selectedQuantity <= maxQuantity) {
                JOptionPane.showMessageDialog(null, selectedQuantity + " " + itemName + "(s) added to the bill.");
                return selectedQuantity;
            } else {
                JOptionPane.showMessageDialog(null, "Invalid quantity! Please enter a value between 1 and " + maxQuantity, "Error", JOptionPane.ERROR_MESSAGE);
                return -2;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
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
            throw new RuntimeException();

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
                    MyClass.newBill.notThroughOrderSlip = false;
                    try {
                        pushDetails(doSqlUpadates(ID));
                    } finally {
                        MyClass.newBill.notThroughOrderSlip = true;
                    }

                }
            }
        });
        pack();

    }

    private void pushDetails(Vector<Integer> detailsToPush) {//detailstopush-> item id at index 0 and quantity at index 1
        if (detailsToPush == null || detailsToPush.size() < 2) {
            JOptionPane.showMessageDialog(null, "Invalid details to push!");
            return;
        }
        Connection con = MyClass.newBill.getTransacTemp();
        int itemId = detailsToPush.get(0);
        int quantity = detailsToPush.get(1);

        JTable billTable = MyClass.newBill.getBillTable();
        DefaultTableModel model = (DefaultTableModel) billTable.getModel();

        String query = "SELECT  quantity,billed_quantity,slip_id,item_name, design_id, raw_material_price FROM order_slips WHERE item_id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, itemId);
            ResultSet rs = pstmt.executeQuery();
            int lastrow = billTable.getRowCount() - 1;


            if (rs.next()) {
                TableColumnModel columnModel = billTable.getColumnModel();
                setUpdateThroughSlip(true);
                MyClass.newBill.setItemID(itemId);
                setSearchFlag(true);
                model.setValueAt(rs.getString("slip_id") + "." + itemId + "/" + (rs.getInt("quantity") - rs.getInt("billed_quantity")), lastrow, columnModel.getColumnIndex("OrderSlip/quantity"));
                model.setValueAt(String.valueOf(quantity), lastrow, columnModel.getColumnIndex("Quantity"));
                model.setValueAt(rs.getString("item_name"), lastrow, columnModel.getColumnIndex("ItemName"));
                model.setValueAt(rs.getString("raw_material_price"), lastrow, columnModel.getColumnIndex("Raw"));
                model.setValueAt(rs.getString("design_id"), lastrow, columnModel.getColumnIndex("DesignID"));
                model.fireTableDataChanged();
                setSearchFlag(false);
            } else {
                JOptionPane.showMessageDialog(null, "Item not found in order_slips!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error occurred!");
            throw new RuntimeException();

        }

    }

    private void setUpdateThroughSlip(boolean b) {
        MyClass.newBill.updateThroughSlip = b;


    }


    HashMap<Integer, Integer> snoToItemIdMap;

    public void fetchData(DefaultTableModel model) {
        model.setRowCount(0);
        String query = "SELECT customer_name ,slip_id,item_id,sno,design_id, item_name, quantity, plating_grams, raw_material_price, other_details, billed_quantity " + "FROM order_slips WHERE slip_id = ? Order by item_id";

        try {
            PreparedStatement pstmt = MyClass.newBill.getTransacTemp().prepareStatement(query);
            pstmt.setInt(1, ID);
            ResultSet rs = pstmt.executeQuery();
            snoToItemIdMap = new HashMap<>();

            while (rs.next()) {

                int sno = rs.getInt("sno");
                int itemId = rs.getInt("item_id");

                snoToItemIdMap.put(sno, itemId); //
                model.addRow(new Object[]{
                        rs.getString("sno"), rs.getString("design_id"), rs.getString("item_name"), (rs.getInt("quantity") - rs.getInt("billed_quantity")), rs.getBigDecimal("plating_grams"), rs.getBigDecimal("raw_material_price"), rs.getString("other_details")});// now this
            }
        } catch (SQLException e) {
            throw new RuntimeException();

        }

    }
}

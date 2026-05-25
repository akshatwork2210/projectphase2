package loginsignup.login.loggedin.billing.newBill;

import mainpack.MyClass;
import utils.DBStructure;
import utils.UtilityMethods;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
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

    private boolean searchFlag = false;

    private JButton backButton;
    private JButton button2;
    private JTable orderSlipTable;
    //    private JLabel slipID;
    private JLabel cutomerName;
    private JLabel panaTypeLabel;
    private JComboBox<String> slipIDComboBox;
    int ID;

    public DefaultTableModel getTableModel() {
        return (DefaultTableModel) orderSlipTable.getModel();
    }

    public String getCutomerName() {
        String name;
        try (Connection con = MyClass.createConnection(); Statement stmt = con.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("select "+ DBStructure.ORDER_SLIPS_CUSTOMER_NAME + " from "+DBStructure.ORDER_SLIPS_TABLE+" where "+DBStructure.ORDER_SLIPS_SLIP_ID+"=" + ID + ";");) {
                if (rs.next()) {
                    name = rs.getString(1);
                    return name;
                } else {
                    throw new SQLException();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }


    }

    public SearchResultWindow() {


    }

    public void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("CREATE BILL");
        setContentPane(panel1);
        slipIDComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchResultWindow.this.ID = UtilityMethods.parseInt(slipIDComboBox.getSelectedItem());
                fetchData((DefaultTableModel) orderSlipTable.getModel());
            }
        });
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
        try (Connection con = MyClass.createConnection(); Statement statement = con.createStatement()) {
            try (ResultSet rs = statement.executeQuery("select slip_type from order_slips where slip_id=" + ID + ";");) {
                if (rs.next()) {
                    return rs.getString(1);
                } else {
                    JOptionPane.showMessageDialog(this, "error occured panatype not found");
                    return null;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "SQL EXCEPTION OCCURED");
            throw new RuntimeException();

        }

    }

    public SearchResultWindow(int bID) {
        init();
        System.out.println("more initial value of ID is "+ID);
        this.ID = bID;
        String customerName = "";

        slipIDComboBox.setModel(generateSlipListModel(customerName));
//        slipIDComboBox.setText("order slip id: " + ID);
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
        fetchData(model);

        pack();

    }

    public DefaultComboBoxModel<String> generateSlipListModel(String customerName) {
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        String query = "select " + DBStructure.ORDER_SLIPS_MAIN_SLIP_ID + " from " + DBStructure.ORDER_SLIPS_MAIN_TABLE + " where " + DBStructure.ORDER_SLIPS_MAIN_CUSTOMER_NAME + "=?";
        try (Connection con = MyClass.createConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, customerName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Thread.dumpStack();
                    System.out.println("data added " + rs.getString(DBStructure.ORDER_SLIPS_MAIN_SLIP_ID));
                    comboBoxModel.addElement(rs.getString(DBStructure.ORDER_SLIPS_MAIN_SLIP_ID));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return comboBoxModel;
    }

    private void pushDetails(Vector<Integer> detailsToPush) {//detailstopush-> item id at index 0 and quantity at index 1
        if (detailsToPush == null) return;
        if (detailsToPush.size() < 2) {
            JOptionPane.showMessageDialog(null, "Invalid details to push!");
            return;
        }

        Connection con = MyClass.newBill.getTransacTemp();
        int itemId = detailsToPush.get(0);
        int quantity = detailsToPush.get(1);

        JTable billTable = MyClass.newBill.getBillTable();
        DefaultTableModel model = (DefaultTableModel) billTable.getModel();

        String query = "SELECT "
                + DBStructure.ORDER_SLIPS_QUANTITY + ", "
                + DBStructure.ORDER_SLIPS_BILLED_QUANTITY + ", "
                + DBStructure.ORDER_SLIPS_SLIP_ID + ", "
                + DBStructure.ORDER_SLIPS_ITEM_NAME + ", "
                + DBStructure.ORDER_SLIPS_DESIGN_ID + ", "
                + DBStructure.ORDER_SLIPS_RAW_MATERIAL_PRICE
                + " FROM " + DBStructure.ORDER_SLIPS_TABLE
                + " WHERE " + DBStructure.ORDER_SLIPS_ITEM_ID + " = ?";
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
        String query = "SELECT "
                + DBStructure.ORDER_SLIPS_CUSTOMER_NAME + ", "
                + DBStructure.ORDER_SLIPS_SLIP_ID + ", "
                + DBStructure.ORDER_SLIPS_ITEM_ID + ", "
                + DBStructure.ORDER_SLIPS_SNO + ", "
                + DBStructure.ORDER_SLIPS_DESIGN_ID + ", "
                + DBStructure.ORDER_SLIPS_ITEM_NAME + ", "
                + DBStructure.ORDER_SLIPS_QUANTITY + ", "
                + DBStructure.ORDER_SLIPS_PLATING_GRAMS + ", "
                + DBStructure.ORDER_SLIPS_RAW_MATERIAL_PRICE + ", "
                + DBStructure.ORDER_SLIPS_OTHER_DETAILS + ", "
                + DBStructure.ORDER_SLIPS_BILLED_QUANTITY
                + " FROM " + DBStructure.ORDER_SLIPS_TABLE
                + " WHERE " + DBStructure.ORDER_SLIPS_SLIP_ID + " = ? "
                + " ORDER BY " + DBStructure.ORDER_SLIPS_ITEM_ID;
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

    public void setTableModel(DefaultComboBoxModel<String> comboBoxModel) {
        slipIDComboBox.setModel(comboBoxModel);
    }
}

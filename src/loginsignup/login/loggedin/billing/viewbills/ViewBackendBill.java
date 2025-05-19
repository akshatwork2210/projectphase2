package loginsignup.login.loggedin.billing.viewbills;

import testpackage.UtilityMethods;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import static mainpack.MyClass.*;
import static testpackage.UtilityMethods.*;

public class ViewBackendBill extends JFrame {
    private JButton backButton;
    private JTable billTable;
    private JComboBox<String> customerComboBox;
    private JComboBox<String> dateComboBox;
    private JButton prevButton;
    private JButton nextButton;
    private JPanel panel;
    private JTextField searchID;
    private JLabel customerNameLabel;
    private JLabel billIDLabel;
    private JLabel dateLabel;

    public ViewBackendBill() {

        searchID.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loadBillData(Integer.parseInt(searchID.getText()));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(viewBackendBill, "error", "error", JOptionPane.ERROR_MESSAGE);
                }
                searchID.setText("");
            }
        });
    }

    Vector<String> billDetails;

    public void init() {
        setContentPane(panel);
        generateAndAddDates(dateComboBox, true);
        generateAndAddNames(customerComboBox);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        System.out.println(0xb55);
        int m = 0b101001 + 0x545 + 0b10010111101001;
        billDetails = new Vector<>();
        billDetails.add("SNo");
        billDetails.add("OrderSlip");
        billDetails.add("ItemName");
        billDetails.add("Quantity");
        billDetails.add("DesignID");
        billDetails.add("L");
        billDetails.add("Raw");
        billDetails.add("dc");
        billDetails.add("M/CM");
        billDetails.add("Rh");
        billDetails.add("Nag");
        billDetails.add("Other");
        billDetails.add("OtherDetails");
        billDetails.add("+G");
        billDetails.add("Gold(ing g)");
        billDetails.add("gold cost");
        billDetails.add("total");
        prevButton.addActionListener(e -> {
            try {
                String query = "SELECT MAX(BillID) FROM bills WHERE BillID < ?";

                if (customerComboBox.getSelectedIndex() != 0) {
                    query += " AND customer_name = ?";
                }

                if (dateComboBox.getSelectedIndex() != 0) {
                    query += " AND DATE(date) = ?";
                }

                PreparedStatement stmt = C.prepareStatement(query);
                stmt.setInt(1, curBillID);

                int paramIndex = 2;
                if (customerComboBox.getSelectedIndex() != 0) {
                    stmt.setString(paramIndex++, customerComboBox.getSelectedItem().toString());
                }

                if (dateComboBox.getSelectedIndex() != 0) {
                    stmt.setString(paramIndex, dateComboBox.getSelectedItem().toString());
                }

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int prevID = rs.getInt(1);
                    if (!rs.wasNull()) {
                        loadBillData(prevID);
                        setCurBillID(prevID);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        nextButton.addActionListener(e -> {
            try {
                String query = "SELECT MIN(BillID) FROM bills WHERE BillID > ?";

                if (customerComboBox.getSelectedIndex() != 0) {
                    query += " AND customer_name = ?";
                }

                if (dateComboBox.getSelectedIndex() != 0) {
                    query += " AND DATE(date) = ?";
                }

                PreparedStatement stmt = C.prepareStatement(query);
                stmt.setInt(1, curBillID);

                int paramIndex = 2;
                if (customerComboBox.getSelectedIndex() != 0) {
                    stmt.setString(paramIndex++, customerComboBox.getSelectedItem().toString());
                }

                if (dateComboBox.getSelectedIndex() != 0) {
                    stmt.setString(paramIndex, dateComboBox.getSelectedItem().toString());
                }

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int nextID = rs.getInt(1);
                    if (!rs.wasNull()) {
                        loadBillData(nextID);
                        setCurBillID(nextID);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        DefaultTableModel tableModel = new DefaultTableModel(billDetails, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        billTable.setModel(tableModel);
        loadBillData(minBillID());

        backButton.addActionListener(e -> {
            setVisible(false);
            dispose();
            billingScreen.setVisible(true);
        });
    }

    public void setCurBillID(int curBillID) {
        this.curBillID = curBillID;
    }

    public int getCurBillID() {
        return curBillID;
    }

    //    void loadBillData(int billid) {
//        String query = "select*from billdetails where BillID=?";
//        try {
//            PreparedStatement stmt=C.prepareStatement(query);
//            stmt.setInt(1,billid);
//            ResultSet resultSet=stmt.executeQuery();
//
//            while (resultSet.next()){
//                Vector<String> data= new Vector<>();
//
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
    int curBillID;

    int loadBillData(int billid) {

        String query = "SELECT * FROM billdetails WHERE BillID = ?";
        try {
            PreparedStatement stmt = C.prepareStatement(query);
            stmt.setInt(1, billid);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(viewBackendBill, "empty resultset returned");
                return -1;
            }
            DefaultTableModel model = (DefaultTableModel) billTable.getModel();
            model.setRowCount(0); // Clear existing data
            String customer_name = "";
            do {

                Vector<String> row = new Vector<>();
                row.add(rs.getString("SNo") == null ? "" : rs.getString("SNo")); // SNo
                row.add(rs.getString("OrderSlipNumber") == null ? "" : rs.getString("OrderSlipNumber")); // OrderSlip/quantity
                row.add(rs.getString("ItemName") == null ? "" : rs.getString("ItemName")); // ItemName
                row.add(rs.getString("Quantity") == null ? "0" : rs.getString("Quantity")); // Quantity
                row.add(rs.getString("DesignID") == null ? "" : rs.getString("DesignID")); // DesignID
                row.add(rs.getString("LabourCost") == null ? "0" : rs.getString("LabourCost")); // L
                row.add(rs.getString("RawCost") == null ? "0" : rs.getString("RawCost")); // Raw
                row.add(rs.getString("DullChillaiCost") == null ? "0" : rs.getString("DullChillaiCost")); // DC
                row.add(rs.getString("MeenaColorMeenaCost") == null ? "0" : rs.getString("MeenaColorMeenaCost")); // M/CM
                row.add(rs.getString("RhodiumCost") == null ? "0" : rs.getString("RhodiumCost")); // Rh
                row.add(rs.getString("NagSettingCost") == null ? "0" : rs.getString("NagSettingCost")); // Nag
                row.add(rs.getString("OtherBaseCosts") == null ? "0" : rs.getString("OtherBaseCosts")); // Other
                row.add(rs.getString("OtherBaseCostNotes") == null ? "" : rs.getString("OtherBaseCostNotes")); // OtherDetails
                row.add(rs.getString("TotalBaseCosting") == null ? "0" : rs.getString("TotalBaseCosting")); // +G
                row.add(rs.getString("GoldPlatingWeight") == null ? "0" : rs.getString("GoldPlatingWeight")); // Gold(g)
                row.add(rs.getString("GoldRate") == null ? "0" : rs.getString("GoldRate")); // Gold Rate
                    row.add(rs.getString("TotalFinalCost") == null ? "0" : rs.getString("TotalFinalCost")); // Total

                model.addRow(row);
            } while (rs.next());
            query = "select customer_name,date from bills where billid =?";
            stmt.close();
            stmt = C.prepareStatement(query);
            stmt.setInt(1, billid);
            rs.close();
            rs = stmt.executeQuery();
            if (rs.next()) {
                customer_name = rs.getString(1);
                dateLabel.setText(UtilityMethods.parseDateString(rs.getDate(2)));
            }
            billIDLabel.setText("bill id:" + billid);
            customerNameLabel.setText(customer_name);
            curBillID = billid;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading bill data: " + e.getMessage(), e);
        }
        return 0;//success
    }

    int minBillID() {
        String query = "SELECT MIN(BillID) FROM billdetails";
        try (PreparedStatement stmt = C.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1; // If no bills exist
    }

}

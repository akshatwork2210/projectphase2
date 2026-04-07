package testpackage;

import mainpack.MyClass;

import javax.swing.*;
import java.sql.*;

public class DBStructure {

    //starting inventory table defination






    public static final int NOT_FOUND = -18 * 1;
    private static final int SQLEXCEPTIONOCCURED = -18 * 2;
    private static final int CONNECTION_NOT_CLOSE_ERROR = -18 * 3;



    // ====================== billdetails ======================
    public static final String BILLDETAILS_TABLE = "billdetails";
    public static final String BILLDETAILS_BILL_ID = "BillID";
    public static final String BILLDETAILS_SNO = "SNo";
    public static final String BILLDETAILS_ITEM_NAME = "ItemName";
    public static final String BILLDETAILS_DESIGN_ID = "DesignID";
    public static final String BILLDETAILS_ORDER_TYPE = "ordertype";
    public static final String BILLDETAILS_LABOUR_COST = "LabourCost";
    public static final String BILLDETAILS_DULL_CHILLAI_COST = "DullChillaiCost";
    public static final String BILLDETAILS_MEENA_COLOR_MEENA_COST = "MeenaColorMeenaCost";
    public static final String BILLDETAILS_RHODIUM_COST = "RhodiumCost";
    public static final String BILLDETAILS_NAG_SETTING_COST = "NagSettingCost";
    public static final String BILLDETAILS_OTHER_BASE_COSTS = "OtherBaseCosts";
    public static final String BILLDETAILS_OTHER_BASE_COST_NOTES = "OtherBaseCostNotes";
    public static final String BILLDETAILS_TOTAL_BASE_COSTING = "TotalBaseCosting";
    public static final String BILLDETAILS_GOLD_RATE = "GoldRate";
    public static final String BILLDETAILS_GOLD_PLATING_WEIGHT = "GoldPlatingWeight";
    public static final String BILLDETAILS_TOTAL_GOLD_COST = "TotalGoldCost";
    public static final String BILLDETAILS_TOTAL_FINAL_COST = "TotalFinalCost";
    public static final String BILLDETAILS_ORDER_SLIP_NUMBER = "OrderSlipNumber";
    public static final String BILLDETAILS_ITEMID_BILL = "itemid_bill";
    public static final String BILLDETAILS_RAW_COST = "RawCost";
    public static final String BILLDETAILS_QUANTITY = "quantity";

    // ====================== bills ======================
    public static final String BILLS_TABLE = "bills";
    public static final String BILLS_BILL_ID = "BillID";
    public static final String BILLS_DATE = "date";
    public static final String BILLS_CUSTOMER_NAME = "customer_name";
    public static final String BILLS_DRAFT = "drafts";

    // ====================== customers ======================
    public static final String CUSTOMERS_TABLE = "customers";
    public static final String CUSTOMERS_CUSTOMER_ID = "customer_id";
    public static final String CUSTOMERS_CUSTOMER_NAME = "customer_name";
    public static final String CUSTOMERS_BALANCE = "balance";
    public static final String CUSTOMERS_OPENING_ACCOUNT = "openingaccount";

    // ====================== inventory ======================
    public static final String INVENTORY_TABLE = "inventory";
    public static final String INVENTORY_DESIGN_ID = "DesignID";
    public static final String INVENTORY_TOTAL_QUANTITY = "TotalQuantity";
    public static final String INVENTORY_ITEM_NAME = "itemname";
    public static final String INVENTORY_BUY_PRICE = "price";
    public static final String INVENTORY_OPENING_STOCK = "openingstock";
    public static final String INVENTORY_SELL_PRICE = "sellPrice";

    // ====================== order_slips ======================
    public static final String ORDER_SLIPS_TABLE = "order_slips";
    public static final String ORDER_SLIPS_SLIP_TYPE = "slip_type";
    public static final String ORDER_SLIPS_CUSTOMER_NAME = "customer_name";
    public static final String ORDER_SLIPS_RAW_MATERIAL_PRICE = "raw_material_price";
    public static final String ORDER_SLIPS_ITEM_NAME = "item_name";
    public static final String ORDER_SLIPS_QUANTITY = "quantity";
    public static final String ORDER_SLIPS_PLATING_GRAMS = "plating_grams";
    public static final String ORDER_SLIPS_CREATED_AT = "created_at";
    public static final String ORDER_SLIPS_OTHER_DETAILS = "other_details";
    public static final String ORDER_SLIPS_DESIGN_ID = "design_id";
    public static final String ORDER_SLIPS_SLIP_ID = "slip_id";
    public static final String ORDER_SLIPS_ITEM_ID = "item_id";
    public static final String ORDER_SLIPS_SNO = "sno";
    public static final String ORDER_SLIPS_BILLED_QUANTITY = "billed_quantity";

    // ====================== order_slips_main ======================
    public static final String ORDER_SLIPS_MAIN_TABLE = "order_slips_main";
    public static final String ORDER_SLIPS_MAIN_SLIP_ID = "slip_id";
    public static final String ORDER_SLIPS_MAIN_SLIP_TYPE = "slip_type";
    public static final String ORDER_SLIPS_MAIN_CREATED_AT = "created_at";
    public static final String ORDER_SLIPS_MAIN_CUSTOMER_NAME = "customer_name";

    // ====================== ordertype ======================
    public static final String ORDERTYPE_TABLE = "ordertype";
    public static final String ORDERTYPE_TYPE_ID = "type_id";
    public static final String ORDERTYPE_TYPE_NAME = "type_name";

    // ====================== transactions ======================
    public static final String TRANSACTIONS_TABLE = "transactions";
    public static final String TRANSACTIONS_TRANSACTION_ID = "transaction_id";
    public static final String TRANSACTIONS_BILLID = "billid";
    public static final String TRANSACTIONS_AMOUNT = "amount";
    public static final String TRANSACTIONS_DATE = "date";
    public static final String TRANSACTIONS_CUSTOMER_NAME = "customer_name";
    public static final String TRANSACTIONS_REMARK = "remark";






    static int getStock(String designID) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection(MyClass.login.getUrl(), MyClass.login.getLoginID(), MyClass.login.getPassword());

            String query = "select " + INVENTORY_TOTAL_QUANTITY + "+" + INVENTORY_OPENING_STOCK + " FROM " + INVENTORY_TABLE + " WHERE " + INVENTORY_DESIGN_ID + " = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, designID);
            rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
            else return NOT_FOUND;
        } catch (SQLException e) {
            e.printStackTrace();
            return SQLEXCEPTIONOCCURED;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "contact technical xpert and restart the programm", "ERROR", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return CONNECTION_NOT_CLOSE_ERROR;
            }
        }
    }

    public static String getInventoryItemName(String designID) {
        String query = "select " + INVENTORY_ITEM_NAME + " from " + INVENTORY_TABLE + " where " + INVENTORY_DESIGN_ID + " = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection(MyClass.login.getUrl(), MyClass.login.getLoginID(), MyClass.login.getPassword());
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, designID);
            rs = pstmt.executeQuery();
            if (rs.next()) return rs.getString(1);
            else return String.valueOf(NOT_FOUND);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage() + ": error occured");
            return String.valueOf(SQLEXCEPTIONOCCURED);

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "contact technical xpert and restart the programm", "ERROR", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return String.valueOf(CONNECTION_NOT_CLOSE_ERROR);
            }
        }
    }

    public static double getSellPrice(String designID) {
        String query = "select " + INVENTORY_SELL_PRICE + " from " + INVENTORY_TABLE + " WHERE " + INVENTORY_DESIGN_ID + " = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection(MyClass.login.getUrl(), MyClass.login.getLoginID(), MyClass.login.getPassword());
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, designID);
            rs = pstmt.executeQuery();
            if (rs.next()) return rs.getDouble(1);
            else return NOT_FOUND;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage() + ": error occured");
            return SQLEXCEPTIONOCCURED;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "contact technical xpert and restart the programm", "ERROR", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return CONNECTION_NOT_CLOSE_ERROR;
            }
        }


    }

    public static double getBuyPrice(String designID) {
        String query = "select " + INVENTORY_BUY_PRICE + " from " + INVENTORY_TABLE + " WHERE " + INVENTORY_DESIGN_ID + " = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection(MyClass.login.getUrl(), MyClass.login.getLoginID(), MyClass.login.getPassword());
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, designID);
            rs = pstmt.executeQuery();
            if (rs.next()) return rs.getDouble(1);
            else return NOT_FOUND;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage() + ": error occured");
            return SQLEXCEPTIONOCCURED;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "contact technical xpert and restart the programm", "ERROR", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return CONNECTION_NOT_CLOSE_ERROR;
            }
        }


    }

    public static void setBuyPrice(String designID, double price) {
        String query = "update "+INVENTORY_TABLE+" set "+ INVENTORY_BUY_PRICE +" = ? where "+INVENTORY_DESIGN_ID+" = ?";
        try (Connection con = UtilityMethods.createConnection(); PreparedStatement stmt = con.prepareStatement(query);) {
            stmt.setDouble(1, price);
            stmt.setString(2, designID);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

    }

}

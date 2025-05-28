package testpackage;

import mainpack.MyClass;

import javax.swing.*;
import java.sql.*;

public class DBStructure {

    //starting inventory table defination
    public static final String INVENTORY_TABLE = "inventory";

    public static final String INVENTORY_DESIGN_ID = "DesignID";
    public static final String INVENTORY_TOTAL_QUANTITY = "TotalQuantity";
    public static final String INVENTORY_ITEM_NAME = "itemname";
    public static final String INVENTORY_PRICE = "getBuyPrice";
    public static final String INVENTORY_OPENING_STOCK = "OPENINGSTOCK";
    public static final String INVENTORY_SELL_PRICE="sellPrice";

    public static final String ORDER_SLIPS_MAIN = "order_slips_main";
    public static final int NOT_FOUND = -18 * 1;
    private static final int SQLEXCEPTIONOCCURED = -18 * 2;
    private static final int CONNECTION_NOT_CLOSE_ERROR = -18 * 3;

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

    public static double  getSellPrice(String designID) {
        String query="select "+INVENTORY_SELL_PRICE+ " from "+INVENTORY_TABLE+" WHERE "+INVENTORY_DESIGN_ID+" = ?";
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
        }
        finally {
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
        String query="select "+INVENTORY_PRICE+ " from "+INVENTORY_TABLE+" WHERE "+INVENTORY_DESIGN_ID+" = ?";
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
        }
        finally {
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


}

package loginsignup.login.loggedin.ordermanagement.generateorder;

import mainpack.MyClass;
import testpackage.DBStructure;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.classfile.attribute.PermittedSubclassesAttribute;
import java.sql.*;
import java.util.List;
import java.util.Vector;

import static testpackage.DBStructure.*;

public class InventorySelect extends JFrame {
    private static final int DESIGNID_INDEX = 0;
    private JPanel panel;
    private JButton backButton;
    private JTable inventoryTable;

    static void main() {
        InventorySelect inventorySelect = new InventorySelect();
        inventorySelect.init(null);
        inventorySelect.setVisible(true);
    }

    public InventorySelect() {
    }

    public void init(OrderGenerateForm orderGenerateForm) {

        setContentPane(panel);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

            }
        });
        Vector<String> data = new Vector<>(List.of(new String[]{"design id", "item name", "total quantity available"}));
        DefaultTableModel model = new DefaultTableModel(data, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        inventoryTable.setModel(model);
        fetchData(model);
        inventoryTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    int selectedRow=inventoryTable.getSelectedRow();
                    System.out.println("hi");
                    pushDataToGenerator(selectedRow,orderGenerateForm);

                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        pack();
    }

    private void pushDataToGenerator(int selectedRow,OrderGenerateForm orderGenerateForm) {
        String designID=inventoryTable.getModel().getValueAt(selectedRow,DESIGNID_INDEX).toString();
        int lastRow= orderGenerateForm.model.getRowCount()-1;
        orderGenerateForm.model.setValueAt(designID, lastRow,orderGenerateForm.DESIGN_ID_INDEX);

    }

    public void fetchData(DefaultTableModel model) {
        String query = "Select " + INVENTORY_DESIGN_ID + ", " + INVENTORY_ITEM_NAME + ", " + INVENTORY_TOTAL_QUANTITY + " from " + INVENTORY_TABLE;
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/raj","raj","akshat"); PreparedStatement preparedStatement = con.prepareStatement(query)) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new String[]{rs.getString(INVENTORY_DESIGN_ID), rs.getString(INVENTORY_ITEM_NAME), rs.getString(INVENTORY_TOTAL_QUANTITY)});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
    }
}

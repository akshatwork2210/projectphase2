package loginsignup.login.loggedin.ordermanagement.generateorder;

import mainpack.MyClass;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class OrderGenerateForm extends JFrame {
    private JPanel panel;
    private JButton backButton;
    TableModelListener modelListener;
    private JComboBox customerNameComboBox;
    private JComboBox panaTypeComboBox;
    private JTable orderSlip;
    private JButton submitButton;
    ArrayList<Integer[][]> ar;

    public OrderGenerateForm() {
        setContentPane(panel);
        pack();
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.orderScreen.setVisible(true);
            }
        });
        orderSlip.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                System.out.println(orderSlip.getSelectedRow());
            }
        });
    }
int prevRow =0;
    DefaultTableModel model;
    public void init() {

        ar = new ArrayList<>();
        // this method will be initializing functinality of this window
        String[] columnNames = {"design id", "Item Name", "Quantity", "Plating", "Raw Material Cost", "Other Details"};//jtable content
        orderSlip.getTableHeader().setReorderingAllowed(false);
        // Create a DefaultTableModel with columns and no rows initially
        model = new DefaultTableModel(columnNames, 0);
        model.addRow(new String[]{"", "", "", "", "", ""});
  modelListener=new TableModelListener() {
      @Override
      public void tableChanged(TableModelEvent e) {
          if (e.getType() == TableModelEvent.UPDATE) {
              int lastRow = model.getRowCount() - 1;
              boolean isRowFilled = false;

              // Check if any column in the last row is filled
              for (int i = 0; i < model.getColumnCount(); i++) {
                  Object value = model.getValueAt(lastRow, i);
                  if (value != null && !value.toString().trim().isEmpty()) {
                      isRowFilled = true;
                      break;
                  }
              }

              if (isRowFilled) {
                  model.addRow(new Object[]{"", null, "", null, ""});
              }



              int row = orderSlip.getSelectedRow(), column = orderSlip.getSelectedColumn();
              String cellContent = orderSlip.getModel().getValueAt(row, column).toString();
              DefaultTableModel newModel=null;




              if (column == 0 && prevRow == 0) {
                  prevRow++;
                  if (!cellContent.contentEquals("") && orderSlip.getSelectedColumn() == 0) {
//                            JOptionPane.showMessageDialog(MyClass.orderGenerateForm, "success in building algorithm");

                      try {

                          ResultSet resultSet = MyClass.S.executeQuery("Select * from inventory;");
                          while (resultSet.next()) {
                              if (resultSet.getString(1).contentEquals(cellContent)) {
                                  model.setValueAt(resultSet.getString("itemname"), row, 1);
                                  break;
                              } else {model.setValueAt("", row, 1);break;
                              }
                          }

                      } catch (SQLException ex) {
                          ex.printStackTrace();
                      }

                  }
                  prevRow =0;
              }
         disableName();
          }

      }
  };
        model.addTableModelListener(modelListener);


        ArrayList<String> customerNames = new ArrayList<>();
        try {


            ResultSet rs = MyClass.S.executeQuery("SELECT customer_name FROM customers");
            // Clear previous entries in combo boxes before populating
            customerNameComboBox.removeAllItems();
            customerNames.add("Select Customer");
            // Fetching the data and adding to lists
            while (rs.next()) {
                String customerName = rs.getString("customer_name");

                customerNames.add(customerName);
            }

            // Adding data to combo boxes


            for (String customerName : customerNames) {
                customerNameComboBox.addItem(customerName);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching customer data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        String[] panaTypes = {"Kachhe Ka Baaki", "Kachhe Ka Jama", "Repairing Pana"};
        DefaultComboBoxModel<String> panaTypeModel = new DefaultComboBoxModel<>(panaTypes);
        panaTypeComboBox.setModel(panaTypeModel);
        orderSlip.setModel(model);

    }

    private DefaultTableModel disableName() {
        Vector<Vector<Object>> tableData = new Vector<>();
         model = (DefaultTableModel) orderSlip.getModel();
        Vector<int[]> listOfDisableCells=new Vector<>();
        int[] arr=new int[2];
        for(int countRow=0;countRow<orderSlip.getRowCount();countRow++){

                if(!orderSlip.getModel().getValueAt(countRow,0).toString().contentEquals("")){
                    arr[0]=countRow;
                    arr[1]=1;
                    listOfDisableCells.add(arr);
                }

        }


        for (int ro = 0; ro < model.getRowCount(); ro++) {
            Vector<Object> rowData = new Vector<>();
            for (int col = 0; col < model.getColumnCount(); col++) {
                rowData.add(model.getValueAt(ro, col)); // Add cell data to row vector
            }
            tableData.add(rowData); // Add row vector to ArrayList
        }
        Vector<String> v=new Vector<>();
        for (int i=0;i<orderSlip.getColumnCount();i++){
            v.add(orderSlip.getColumnName(i));
        }for (int[] cells: listOfDisableCells){
            System.out.println("deleted array:"+cells[0]+","+cells[1]);
        }
        DefaultTableModel m=new DefaultTableModel(tableData,v){
            @Override
            public boolean isCellEditable(int row, int column) {
                for (int[] cell : listOfDisableCells) {
                    if (cell[0] == row && cell[1] == column) {

                        return false; // Disable this cell

                    }
                }
                return true; // Other cells remain editable
            }
        };

        m.addTableModelListener(modelListener);
        model=m;
        orderSlip.setModel(model);
        return m;
    }
}

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

public class OrderGenerateForm extends JFrame {
    private JPanel panel;
    private JButton backButton;
    private JComboBox customerName;
    private JComboBox panaType;
    private JTable orderSlip;

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
    public void init(){
        String[] columnNames = {"Item Name", "Quantity", "Plating", "Raw Material Cost", "Other Details"};
        orderSlip.getTableHeader().setReorderingAllowed(false);
        // Create a DefaultTableModel with columns and no rows initially
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        model.addRow(new String[]{"","","","",""});
        model.addTableModelListener(new TableModelListener() {
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

                    // If last row has some input, add a new empty row
                    if (isRowFilled) {
                        model.addRow(new Object[]{"", null, "", null, ""});
                    }
                }
            }
        });
        orderSlip.setModel(model);
    }
}

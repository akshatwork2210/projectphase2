package loginsignup.login.loggedin.accountingandledger;

import com.microsoft.schemas.office.office.STInsetMode;
import loginsignup.login.loggedin.accountingandledger.ledgerwindows.ItemLedger;
import mainpack.MyClass;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AALScreen extends JFrame {
    private JButton backButton;
    private JList<String> itemList;
    private JList customerList;
    private JButton cashStatementButton;
    private JButton expenseAccountButton;// component declaration
    private JPanel panel;

    public AALScreen() {

    }

    public void init() {
        setContentPane(panel);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        getListOfCustomers();
        getListOfItem();
        // Assuming you have a JList<String> itemList
        itemList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int index = itemList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        String selectedItem = itemList.getModel().getElementAt(index);
                        openItem(selectedItem);
                    }
                }
            }
        });

// Handle Enter key
        itemList.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ENTER"), "openItem");
        itemList.getActionMap().put("openItem", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = itemList.getSelectedValue();
                if (selectedItem != null) {
                    openItem(selectedItem);
                }
            }
        });


// Your method to handle the item


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();
                MyClass.mainScreen.setVisible(true);


            }
        });

    }

    private void openItem(String selectedItem) {
        setVisible(false);
        MyClass.itemLedger=new ItemLedger();
        MyClass.itemLedger.init(customerList.getSelectedValue()==null?"":customerList.getSelectedValue().toString());
        MyClass.itemLedger.setVisible(true);

    }

    private void getListOfItem() {
        String query = "select designid,itemname from inventory";
        DefaultListModel<String> model = new DefaultListModel<>();
        try {
            Statement statement = MyClass.C.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                model.addElement(rs.getString("DesignID") + "->" + rs.getString("itemname"));
            }
            customerList.setModel(model);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void getListOfCustomers() {

    }

}

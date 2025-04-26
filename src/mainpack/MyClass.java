package mainpack;

import loginsignup.LOGIN_SIGNUP;
import loginsignup.login.LOGIN;
import loginsignup.login.loggedin.MainScreen;
import loginsignup.login.loggedin.billing.BillingScreen;
import loginsignup.login.loggedin.billing.newBill.NewBill;
import loginsignup.login.loggedin.billing.newBill.SearchResultWindow;
import loginsignup.login.loggedin.billing.viewbills.ViewBackendBill;
import loginsignup.login.loggedin.billing.viewbills.ViewCustomerBill;
import loginsignup.login.loggedin.inventorymanagement.InventoryScreen;
import loginsignup.login.loggedin.inventorymanagement.addinventory.AddInventory;
import loginsignup.login.loggedin.ordermanagement.OrderScreen;
import loginsignup.login.loggedin.ordermanagement.generateorder.OrderGenerateForm;
import loginsignup.login.loggedin.ordermanagement.vieworders.ViewOrders;
import loginsignup.login.loggedin.transactionsandaccounts.Transactions;
import loginsignup.login.loggedin.transactionsandaccounts.newtransaction.NewTransaction;
import loginsignup.login.loggedin.transactionsandaccounts.viewTransactions.ViewTransactions;
import testpackage.UtilityMethods;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class MyClass {
    public static void main(String[] args) {


        login = new LOGIN();
        login_signup = new LOGIN_SIGNUP();
        billingScreen = new BillingScreen();
        mainScreen = new MainScreen();
        login_signup.setVisible(true);
        newBill = new NewBill();
        orderScreen = new OrderScreen();
        orderGenerateForm = new OrderGenerateForm();
        inventoryScreen = new InventoryScreen();
        addInventory = new AddInventory();
        transactions = new Transactions();
        viewBackendBill=new ViewBackendBill();
        viewTransactions = new ViewTransactions();
        newTransaction = new NewTransaction();
        searchResultWindow = new SearchResultWindow();
        login_signup.setVisible(false);
        login.getLOGINButton().doClick();
        mainScreen.setVisible(false);
        login.setVisible(false);
        transactions = new Transactions();
        viewOrders = new ViewOrders();
        viewCustomerBill = new ViewCustomerBill();
        billingScreen.getViewBillButton().doClick();
        UtilityMethods.printingThread = new Thread(() -> {
            while (true) {
                try {
                    Runnable job = UtilityMethods.printQueue.take();  // waits until a job is available
                    job.run();
                } catch (InterruptedException e) {
                    System.out.println("Printing thread interrupted");
                    break;
                }
            }
        }, "PrintingThread");

        UtilityMethods.printingThread.setDaemon(true);  // optional: will not block app from closing
        UtilityMethods.printingThread.start();

        //        transactions.getViewTransactionsButton().doClick();

//        orderScreen.getGenerateANewOrderButton().doClick();
//        orderScreen.getViewOrdersButton().doClick();
//        billingScreen.getNewBillButton().doClick();


    }

    public static ViewOrders viewOrders;
    public static NewTransaction newTransaction;
    public static SearchResultWindow searchResultWindow;
    public static ViewTransactions viewTransactions;
    public static ViewBackendBill viewBackendBill;

    public static Connection getConnection(String url, String user, String password) {
        Connection conn;
        try {
            // Construct the full JDBC URL

            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish Connection
            conn = DriverManager.getConnection(url, user, password);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            System.out.println("✅ Database Connected Successfully to: ");

        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL Driver Not Found!");
            throw new RuntimeException();
        } catch (SQLException e) {
            System.out.println("❌ Database Connection Failed!");
            throw new RuntimeException();
        }
        return conn;
    }

    public static Connection C;

    public static NewBill newBill;
    public static Transactions transactions;


    public static LOGIN login;
    public static BillingScreen billingScreen;

    public static ViewCustomerBill viewCustomerBill;

    public static MainScreen mainScreen;

    public static LOGIN_SIGNUP login_signup;
    public static OrderScreen orderScreen;
    public static OrderGenerateForm orderGenerateForm;
    public static InventoryScreen inventoryScreen;
    public static AddInventory addInventory;

    public static void positionFrames(JFrame topFrame, JFrame bottomFrame) {
        // Screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Position and size the frames
        topFrame.setBounds(0, 0, screenWidth, screenHeight / 2);
        bottomFrame.setBounds(0, screenHeight / 2, screenWidth, screenHeight / 2);
    }
}
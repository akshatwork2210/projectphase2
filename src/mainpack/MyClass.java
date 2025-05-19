package mainpack;

import loginsignup.LOGIN_SIGNUP;
import loginsignup.login.LOGIN;
import loginsignup.login.loggedin.AddParty;
import loginsignup.login.loggedin.MainScreen;
import loginsignup.login.loggedin.accountingandledger.AALScreen;
import loginsignup.login.loggedin.accountingandledger.ledgerwindows.LedgerWindow;
import loginsignup.login.loggedin.billing.BillingScreen;
import loginsignup.login.loggedin.billing.newBill.NewBill;
import loginsignup.login.loggedin.billing.newBill.SearchResultWindow;
import loginsignup.login.loggedin.billing.purcahseBill.PurchaseBill;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class MyClass {
    // 1. Create the mapping once (you can do this statically or in constructor)
   public static Map<String, String> codeToItemName ;

    public static final String PORT="jdbc:mysql://localhost:3306/";

    public static void main(String[] args) {

        {
            codeToItemName = new HashMap<>();
            codeToItemName.put("RH", "Rani Har");
            codeToItemName.put("RHS", "Rani Har Set");
            codeToItemName.put("MIDH", "Midium Har Nag");
            codeToItemName.put("MIDHS", "Midium Har Set");
            codeToItemName.put("MINH", "Mini Har Nag");
            codeToItemName.put("MINHS", "Mini Har Set");
            codeToItemName.put("R", "Ring");
            codeToItemName.put("TH", "Thegda");
            codeToItemName.put("KT", "Kandora Plus Thegda");
            codeToItemName.put("L", "Latkan");
            codeToItemName.put("TO", "Tops");

            codeToItemName.put("1J", "1 Step Jhumki");
            codeToItemName.put("2J", "2 Step Jhumki");
            codeToItemName.put("3J", "3 Step Jhumki");
            codeToItemName.put("4J", "4 Step Jhumki");
            codeToItemName.put("5J", "5 Step Jhumki");
            codeToItemName.put("PC", "Patli Chain");
            codeToItemName.put("MOC", "Moti Chain");
            codeToItemName.put("MIDC", "Midium Chain");
            codeToItemName.put("MINP", "Mini Pandal");
            codeToItemName.put("MIDP", "Midium Pandal");
            codeToItemName.put("BP", "Big Pandal");
            codeToItemName.put("3LMS4B", "3 Line MS Ladi 4Belt");
            codeToItemName.put("2LMS4B", "2 Line MS Ladi 4Belt");
            codeToItemName.put("3LMS2B", "3 Line MS Ladi 2Belt");
            codeToItemName.put("2LMS2B", "2 Line MS Ladi 2Belt");
            codeToItemName.put("NMS", "Nice MS Ladi");
            codeToItemName.put("RCP", "Regular Chain Patta");
            codeToItemName.put("NSCP", "Nice Small Chain Patta");
            codeToItemName.put("NRCP", "Nice Regular Chain Patta");


        }
        {
            purchaseBill = new PurchaseBill();
            login = new LOGIN();
            ledgerWindow =new LedgerWindow();
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
            viewBackendBill = new ViewBackendBill();
            addParty = new AddParty();
            aalScreen=new AALScreen();
            viewTransactions = new ViewTransactions();
            newTransaction = new NewTransaction();
            searchResultWindow = new SearchResultWindow();
            transactions = new Transactions();
            viewOrders = new ViewOrders();
            viewCustomerBill = new ViewCustomerBill();

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

        }
        login_signup.setVisible(false);
        mainScreen.setVisible(false);
        login.setVisible(false);
        login.getLOGINButton().doClick();
//        billingScreen.getPurchaseBillButton().doClick();
        //        billingScreen.getViewBillButton().doClick();
        UtilityMethods.printStartUp();
        UtilityMethods.printingThread.setDaemon(true);  // optional: will not block app from closing
        UtilityMethods.printingThread.start();

        //        transactions.getViewTransactionsButton().doClick();

//        orderScreen.getGenerateANewOrderButton().doClick();
//        orderScreen.getViewOrdersButton().doClick();
//        billingScreen.getNewBillButton().doClick();


    }

    public static PurchaseBill purchaseBill;
    public static AALScreen aalScreen;
    public static ViewOrders viewOrders;
    public static NewTransaction newTransaction;
    public static SearchResultWindow searchResultWindow;
    public static ViewTransactions viewTransactions;
    public static AddParty addParty;
    public static ViewBackendBill viewBackendBill;
    public static LedgerWindow ledgerWindow;

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

}
package mainpack;

import loginsignup.login.loggedin.MainScreen;
import loginsignup.login.loggedin.billing.BillingScreen;
import loginsignup.login.LOGIN;
import loginsignup.LOGIN_SIGNUP;
import loginsignup.login.loggedin.billing.newBill.NewBill;
import loginsignup.login.loggedin.billing.newBill.SearchResultWindow;
import loginsignup.login.loggedin.billing.newBill.ViewBill;
import loginsignup.login.loggedin.inventorymanagement.InventoryScreen;
import loginsignup.login.loggedin.inventorymanagement.addinventory.AddInventory;
import loginsignup.login.loggedin.ordermanagement.OrderScreen;
import loginsignup.login.loggedin.ordermanagement.generateorder.OrderGenerateForm;
import loginsignup.login.loggedin.ordermanagement.vieworders.ViewOrders;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

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
        searchResultWindow = new SearchResultWindow();
        login_signup.setVisible(false);
        login.getLOGINButton().doClick();
        mainScreen.setVisible(false);
        login.setVisible(false);

        viewOrders = new ViewOrders();
        viewBill=new ViewBill();
//        orderScreen.getGenerateANewOrderButton().doClick();
//        orderScreen.getViewOrdersButton().doClick();
        billingScreen.getNewBillButton().doClick();


    }

    public static ViewOrders viewOrders;
    public static SearchResultWindow searchResultWindow;

    public static Connection getConnection(String url, String user, String password) {
        Connection conn = null;
        try {
            // Construct the full JDBC URL

            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish Connection
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Database Connected Successfully to: ");

        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL Driver Not Found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Database Connection Failed!");
            e.printStackTrace();
        }
        return conn;
    }

    public static Connection C;

    public static void printPanel(JPanel panel) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Print Panel");

        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
                if (pageIndex > 0) {
                    return NO_SUCH_PAGE;
                }

                Graphics2D g2d = (Graphics2D) g;
                g2d.translate(pf.getImageableX(), pf.getImageableY());

                // Scale panel to fit page
                double scaleX = pf.getImageableWidth() / panel.getWidth();
                double scaleY = pf.getImageableHeight() / panel.getHeight();
                double scale = Math.min(scaleX, scaleY); // Maintain aspect ratio
                g2d.scale(scale, scale);

                panel.paint(g2d);
                return PAGE_EXISTS;
            }
        });

        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }

    public static NewBill newBill;
    public static LOGIN login;
    public static BillingScreen billingScreen;
    public static ViewBill viewBill;
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
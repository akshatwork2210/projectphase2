package loginsignup.login.loggedin;

import java.io.*;

import loginsignup.login.loggedin.accountingandledger.AALScreen;
import loginsignup.login.loggedin.billing.BillingScreen;
import loginsignup.login.loggedin.billing.newBill.NewBill;
import loginsignup.login.loggedin.inventorymanagement.InventoryScreen;
import loginsignup.login.loggedin.ordermanagement.OrderScreen;
import loginsignup.login.loggedin.transactionsandaccounts.Transactions;
import loginsignup.login.loggedin.transactionsandaccounts.newtransaction.NewTransaction;
import mainpack.MyClass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Random;

public class MainScreen extends JFrame {
    public void createBackup() {
        try {
            // Path to the batch file
            String backupScript = "C:\\path\\to\\backup.bat";  // Change this to actual path

            // Create ProcessBuilder to execute the batch file
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", backupScript);
            processBuilder.redirectErrorStream(true);

            // Start process
            Process process = processBuilder.start();

            // Capture output (for debugging)
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);  // Print batch file output
            }

            // Wait for process to complete
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Backup completed successfully.");
            } else {
                System.err.println("Backup failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String escapeForBatch(String input) {
        return input.replace("%", "%%")  // Escape %
                .replace("^", "^^")  // Escape ^
                .replace("&", "^&")  // Escape &
                .replace("|", "^|")  // Escape |
                .replace("<", "^<")  // Escape <
                .replace(">", "^>")  // Escape >
                .replace("!", "^!"); // Escape !
    }

    public void createFile(String dbUser, String dbPassword, String dbName) {
        String filePath = "tempBack.bat";
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yy___HH_mm_ss");
        String timestamp = sdf.format(new Date());
        timestamp += "_" + dbName + "_backup.sql";
        String backupFile = System.getProperty("user.dir") + "\\src\\resources\\" + timestamp;

        try {
//            dbPassword=escapeForBatch(dbPassword)
            // Create the batch file
            FileWriter writer = new FileWriter(filePath);
            writer.write("@echo off\n");
            writer.write("mysqldump -u " + dbUser + " -p" + escapeForBatch(dbPassword) + " " + dbName + " > \"" + backupFile + "\"\n");
            writer.close();

            // Run the batch file
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", filePath);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Capture output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);  // Debugging output
            }

            // Wait for process to complete
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                JOptionPane.showMessageDialog(this, "Backup completed successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Backup failed with exit code: " + exitCode, "eror", JOptionPane.ERROR_MESSAGE);
            }

            // Delete the batch file after execution
//            File file = new File(filePath);
//            if (file.delete()) {
//                System.out.println("Temporary file deleted.");
//            } else {
//                System.err.println("Failed to delete temporary file.");
//            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public MainScreen() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        this.setContentPane(panel);
        pack();

        billingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyClass.billingScreen = new BillingScreen();
                MyClass.billingScreen.setVisible(true);
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (MyClass.C != null && !MyClass.C.isClosed()) {
                        MyClass.C.close();
                    }
                    MyClass.login.nullLoginParameters();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(MyClass.mainScreen, "could ont close connection error code:" + ex.getErrorCode() + ":" + ex.getMessage());
                    return;
                }
                MyClass.login.setVisible(true);
                dispose();
            }
        });
        orderManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyClass.orderScreen = new OrderScreen();
                MyClass.orderScreen.setVisible(true);
            }
        });
        inventoryManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyClass.inventoryScreen = new InventoryScreen();
                MyClass.inventoryScreen.setVisible(true);
                MyClass.inventoryScreen.init();
            }
        });
        backUpDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createFile(MyClass.login.getLoginID(), MyClass.login.getPassword(), MyClass.login.getDatabase());
            }
        });
        transactionManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                MyClass.transactions = new Transactions();
                MyClass.transactions.init();
                MyClass.transactions.setVisible(true);
            }
        });
        addPartyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyClass.addParty = new AddParty();
                MyClass.addParty.init();
                MyClass.addParty.setVisible(true);
            }
        });
        accountingAndLedgerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyClass.aalScreen = new AALScreen();
                MyClass.aalScreen.setVisible(true);
                MyClass.aalScreen.init();
                setVisible(false);
            }
        });
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String text = textField.getText();
                if (text.startsWith("delete ")) {
                    String date = text.substring(7);
                    try {
                        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yy"));
                        deleteAndForwardBalance(localDate);
                    } catch (DateTimeParseException ex) {
                        return;
                    }

                }
                if (text.startsWith("randomGenerationOfBills ")) {
                    int numberofDays = Integer.parseInt(text.substring(24));
                    generateBillsAndTransaction(numberofDays);
                }
            }
        });
        try {
            setTitle("WELCOME " + MyClass.TITLE + ": " + MyClass.login.getLoginID().toUpperCase() + " ji".toUpperCase());
        }
    catch (NullPointerException ex){
            Thread.dumpStack();
    }
    }



    private void generateBillsAndTransaction(int numberOfDays) {

        Random random = new Random();
        for (int counter = 0; counter < numberOfDays; counter++) {
            boolean choice = random.nextBoolean();//true means generating transaction
            int numberOfTransactions = 1 + random.nextInt(4);
            if (!choice) {
                for (int subCounter = 0; subCounter < numberOfTransactions; subCounter++) {
                    MyClass.newBill = new NewBill();
                    MyClass.newBill.init();
                    int numberOfItems = 1 + random.nextInt(10);
                    int date = numberOfDays - counter;
                    MyClass.newBill.insertRandomValues(numberOfItems, date, null);
                    MyClass.newBill.getSubmitButton().doClick();
                    MyClass.newBill.getBackButton().doClick();
                }
            }
            if (choice) {
                MyClass.newTransaction = new NewTransaction();
                MyClass.newTransaction.init();
                int date = numberOfDays - counter;
                MyClass.newTransaction.generateTransactions(numberOfTransactions, date);
                MyClass.newTransaction.getBackButton().doClick();
            }

        }

    }

    private void deleteAndForwardBalance(LocalDate localDate) {
        System.out.println(localDate.toString() + " deleting data");
//        String updateQuery = "update customers c join billdetails bd on bd.customer_name=c.customer_name set openingAccount=openingAccount + (select sum(totalfinalcost) from billdetails bd2 where bd2.customer_name=c.customer_name and bd2.date<?));";
        String updateQuery =
                "UPDATE customers c " +
                        "LEFT JOIN ( " +
                        "    SELECT b.customer_name, SUM(bd.totalfinalcost) AS total_bills " +
                        "    FROM billdetails bd " +
                        "    JOIN bills b ON bd.billid = b.billid " +
                        "    WHERE b.date < ? " +
                        "    GROUP BY b.customer_name " +
                        ") AS bill_sum ON c.customer_name = bill_sum.customer_name " +
                        "LEFT JOIN ( " +
                        "    SELECT t.customer_name, SUM(t.amount) AS total_transactions " +
                        "    FROM transactions t " +
                        "    WHERE t.date < ? " +
                        "    GROUP BY t.customer_name " +
                        ") AS txn_sum ON c.customer_name = txn_sum.customer_name " +
                        "SET " +
                        "    c.openingAccount = c.openingAccount + IFNULL(bill_sum.total_bills, 0) - IFNULL(txn_sum.total_transactions, 0), " +
                        "    c.balance = c.balance - IFNULL(bill_sum.total_bills, 0) + IFNULL(txn_sum.total_transactions, 0)";


        String deleteBillsQuery = "delete from bills where date<?";
        String deleteTransactionsQuery = "delete from transactions where date<?";
        Connection con = null;
        try {
            con = DriverManager.getConnection(MyClass.login.getUrl(), MyClass.login.getLoginID(), MyClass.login.getPassword());
            con.setAutoCommit(false);
            PreparedStatement statement = con.prepareStatement(updateQuery);
            statement.setDate(1, java.sql.Date.valueOf(localDate));
            statement.setDate(2, java.sql.Date.valueOf(localDate));
            statement.executeUpdate();
            statement.close();
            statement = con.prepareStatement(deleteBillsQuery);
            statement.setDate(1, java.sql.Date.valueOf(localDate));
            statement.executeUpdate();
            statement.close();
            statement = con.prepareStatement(deleteTransactionsQuery);
            statement.setDate(1, java.sql.Date.valueOf(localDate));
            statement.executeUpdate();
            statement.close();
            con.commit();
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }

    private JButton billingButton;
    private JPanel panel;
    private JButton logoutButton;
    private JButton orderManagementButton;
    private JButton backUpDataButton;
    private JButton transactionManagementButton;
    private JButton addPartyButton;
    private JButton inventoryManagementButton;
    private JButton accountingAndLedgerButton;
    private JTextField textField;
}

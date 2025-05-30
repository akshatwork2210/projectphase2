package loginsignup.login.loggedin;

import java.io.*;

import loginsignup.login.loggedin.accountingandledger.AALScreen;
import mainpack.MyClass;
import org.w3c.dom.html.HTMLDivElement;

import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

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
        timestamp += "_backup.sql";
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
                MyClass.billingScreen.setVisible(true);
                setVisible(false);


            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                try {
                    MyClass.C.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                MyClass.login.setVisible(true);
            }
        });
        orderManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyClass.orderScreen.setVisible(true);
                setVisible(false);
            }
        });
        inventoryManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.inventoryScreen.setVisible(true);
                MyClass.inventoryScreen.init();
            }
        });
        backUpDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createFile(MyClass.login.getLoginID(), MyClass.login.getPassword(), "sample");
            }
        });
        transactionManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);

                MyClass.transactions.init();
                MyClass.transactions.setVisible(true);
            }
        });
        addPartyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyClass.addParty.setVisible(true);
                MyClass.addParty.init();
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
            }
        });
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
            if(con!=null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            try {
               if(con!=null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }

    private JButton billingButton;
    private JPanel panel;
    private JButton backButton;
    private JButton orderManagementButton;
    private JButton backUpDataButton;
    private JButton transactionManagementButton;
    private JButton addPartyButton;
    private JButton inventoryManagementButton;
    private JButton accountingAndLedgerButton;
    private JTextField textField;
}

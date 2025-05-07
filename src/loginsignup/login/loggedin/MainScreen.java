package loginsignup.login.loggedin;

import java.io.*;

import mainpack.MyClass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
    }

    private JButton billingButton;
    private JPanel panel;
    private JButton backButton;
    private JButton orderManagementButton;
    private JButton backUpDataButton;
    private JButton transactionManagementButton;
    private JButton addPartyButton;
    private JButton inventoryManagementButton;
}

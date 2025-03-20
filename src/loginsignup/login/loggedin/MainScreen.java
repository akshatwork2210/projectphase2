package loginsignup.login.loggedin;
import java.io.*;
import mainpack.MyClass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.SQLException;

public class MainScreen extends JFrame{
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


    public void createFile(String dbUser, String dbPassword, String dbName) {
        String filePath = "tempBack.bat";
        String backupFile = System.getProperty("user.dir") + "\\src\\resources\\backup.sql"; // Adjust path as needed

        try {
            // Create the batch file
            FileWriter writer = new FileWriter(filePath);
            writer.write("@echo off\n");
            writer.write("mysqldump -u " + dbUser + " -p" + dbPassword + " " + dbName + " > \"" + backupFile + "\"\n");
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
                JOptionPane.showMessageDialog(this,"Backup completed successfully.");
            } else {
                JOptionPane.showMessageDialog(this,"Backup failed with exit code: " + exitCode);
            }

            // Delete the batch file after execution
            File file = new File(filePath);
            if (file.delete()) {
                System.out.println("Temporary file deleted.");
            } else {
                System.err.println("Failed to delete temporary file.");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public MainScreen(){
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
        inventoryManagementButton.addActionListener(new ActionListener()
        {
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
createFile(MyClass.login.getLoginID(),MyClass.login.getPassword(),"sample");
            }
        });
    }
    private JButton billingButton;
    private JPanel panel;
    private JButton backButton;
    private JButton orderManagementButton;
    private JButton backUpDataButton;
    private JButton button2;
    private JButton button3;
    private JButton inventoryManagementButton;
}

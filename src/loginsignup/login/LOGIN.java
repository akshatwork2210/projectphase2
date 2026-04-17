package loginsignup.login;

import loginsignup.login.loggedin.MainScreen;
import mainpack.MyClass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LOGIN extends JFrame {
    private JTextField user;
    private JTextField passwordField;
    private JButton LOGINButton;
    private JButton QUITButton;
    private JButton BACKButton;
    private JPanel panel;
    private JCheckBox isProduction;
    private JTextField databaseField;
    private String loginID;
    private String password;
    private String host;
    private String database="";
    private String port="3306";

    public String getPort() {
        return port;
    }

    public String getUrl() {
        return url;
    }

    public String getDatabase() {
        return database;
    }

    private String url;

    public String getPassword() {
        return password;
    }

    public String getLoginID() {
        return loginID;
    }

    public LOGIN() {
        setContentPane(panel);
        setTitle(MyClass.TITLE+": LOGIN WINDOW");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        QUITButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        pack();
        BACKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.login_signup.setVisible(true);

            }
        });
        LOGIN temp=this;
        LOGINButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                loginID = user.getText();
               if(!"root".contentEquals(loginID) && !databaseField.getText().isEmpty()) {
                   JOptionPane.showMessageDialog(temp, "please do not enter database name for  a non root user");
                return;
               }
                host = "localhost";
                if(loginID.contentEquals("root"))
                    database = databaseField.getText();
                else
                    database=loginID;
                url = "jdbc:mysql://" + host + ":" + port + "/" + database;
                password = passwordField.getText();
                MyClass.mainScreen= new MainScreen();
                MyClass.mainScreen.setVisible(true);
                setVisible(false);
            }
        });
    }

    public JButton getLOGINButton() {
        return LOGINButton;
    }

    public void nullLoginParameters() {
        database = null;
        loginID = null;
        password = null;
    }
}

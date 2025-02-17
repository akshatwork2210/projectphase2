package mainpack;

import loginsignup.LOGIN;
import loginsignup.LOGIN_SIGNUP;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Myclass {
    public static void main(String[] args) {
        login=new LOGIN();
        login_signup=new LOGIN_SIGNUP();
        login_signup.setVisible(true);

    }
    public static Connection getConnection(String host, String database, String user, String password) {
        Connection conn = null;
        try {
            // Construct the full JDBC URL
            String url = "jdbc:mysql://" + host + ":3306/" + database;

            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish Connection
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Database Connected Successfully to: " + database);
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL Driver Not Found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Database Connection Failed!");
            e.printStackTrace();
        }
        return conn;
    }
    public static LOGIN login;
    public static LOGIN_SIGNUP login_signup;
}
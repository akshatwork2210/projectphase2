package testpackage;

import mainpack.MyClass;
import utils.DBStructure;

public class MainTestClass {
    public static void main(String[] args) {
        orderSlip();
    }


    static void orderSlip() {
        MyClass.main(null);
        MyClass.login_signup.clickLoginButton();
        MyClass.login.setUserText("shiv");
        MyClass.login.setPasswordText("shiv");
        MyClass.login.clickLogoutButton();
        MyClass.mainScreen.clickOrderManagementButton();
        MyClass.orderScreen.clickGenerateANewOrderButton();
        generateASlip();

    }
    static void generateASlip() {

    }
}
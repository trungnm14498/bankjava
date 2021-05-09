package features;

import java.text.NumberFormat;
import java.io.Serializable;

public class Account implements Serializable {
    private Customer owner;
    private String ownerName;
    private int accountID;
    private double accountBalance;
    private Boolean status;

    public Account(Customer owner, double accountBalance) {
        this.owner = owner;
        this.ownerName = owner.getName();
        this.accountBalance = accountBalance;
        this.status = true;
    }

    public Account(int ID,Customer owner, double accountBalance, Boolean status){
        this.accountID = ID;
        this.owner = owner;
        this.ownerName = owner.getName();
        this.accountBalance = accountBalance;
        this.status = status;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public int getAccountID() {
        return accountID;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        if(accountBalance >= 0) {
            this.accountBalance = accountBalance;
        } else {
            System.out.println("Negative account balance, please pay all other service fees !!!\n");
        }
    }

    public String toString() {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
        return "Account Owner's Name: " + ownerName + "\n" + "Account ID: " + accountID + "\n" +
                "Balance in the account: " + currencyFormatter.format(accountBalance);
    }

    public void setAccountID(int accountID){
        this.accountID = accountID;
    }
}

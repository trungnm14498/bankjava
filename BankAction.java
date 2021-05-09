package features;

import java.io.Serializable;
import java.time.LocalDate;

public class BankAction implements Serializable{
    private LocalDate date;

    public enum Action {
        OPEN, DEPOSIT, WITHDRAW, TRANSFER
    }

    private Action actionType;
    private double amount;
    public Account account;
    public Account account2;
    private int accountID1;
    private int accountID2;
    private int ID;

    public BankAction (Account account, LocalDate date, Action action, double amount){
        this.account = account;
        this.date = date;
        this.actionType = action;
        this.amount = amount;
        this.accountID1 = account.getAccountID();
    }

    public BankAction (Account account1, Account account2, LocalDate date, Action action, double amount){
        this.account = account1;
        this.accountID1 = account1.getAccountID();
        this.account2 = account2;
        this.accountID2 = account2.getAccountID();
        this.date = date;
        this.actionType = action;
        this.amount = amount;
    }

    public BankAction (Account account, LocalDate date,  Action action){
        this.account = account;
        this.accountID1 = account.getAccountID();
        this.date = date;
        this.actionType = action;
    }

    public int getAccountID1(){
        return accountID1;
    }

    public int getAccountID2(){
        return accountID2;
    }

    public Action getActionType() {
        return actionType;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getAmount(){
        return amount;
    }

    @Override
    public String toString() {
        if (this.actionType == Action.TRANSFER) {
            return "BankAction{" +
                    "date=" +date+", actionType=" + actionType +", amount=" + amount + ", account=" +account.getAccountID()
                    +", toAccount=" + account2.getAccountID() +'}';
        }
        else if(this.actionType == Action.OPEN){
            return "BankAction{" + "date=" +date+", actionType=" + actionType+", account="+account.getAccountID()+"}";
        }
        else{
            return "BankAction{" + "date=" +date+ ", actionType=" + actionType+ "amount="+ amount+ "account="
                    +account.getAccountID()+"}";
        }
    }
}

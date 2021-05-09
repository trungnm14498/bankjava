package sample;

import features.Account;
import features.BankCeo;
import features.BankAction;
import features.Customer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StatsWindow {
    private BankCeo bankCeo;
    private Customer currentUser;
    @FXML private Label summaInfo;
    @FXML private Label spentMonthInfo;
    @FXML private Label incomeMonthInfo;

    public void getBankCeo(BankCeo bankCeo){
        this.bankCeo = bankCeo;
    }
    public void getCurrentUserInfo(Customer customer){
        this.currentUser = customer;
        double s = bankCeo.userAccounts(currentUser).stream().mapToDouble(Account::getAccountBalance).sum();
        double spent = bankCeo.userAccounts(currentUser).stream().
                map(account -> bankCeo.getHistory(account).stream().
                        filter(bankAction -> (bankAction.getActionType().equals(BankAction.Action.WITHDRAW) |
                                (bankAction.getActionType().equals(BankAction.Action.TRANSFER)
                                        && bankAction.getAccountID1() == account.getAccountID())))
                        .mapToDouble(BankAction::getAmount).sum()).mapToDouble(Double::doubleValue).sum();

        double income = bankCeo.userAccounts(currentUser).stream().
                map(account -> bankCeo.getHistory(account).stream().
                        filter(bankEvent -> (bankEvent.getActionType().equals(BankAction.Action.DEPOSIT) |
                                (bankEvent.getActionType().equals(BankAction.Action.TRANSFER)
                                        && bankEvent.getAccountID2() == account.getAccountID())))
                        .mapToDouble(BankAction::getAmount).sum()).mapToDouble(Double::doubleValue).sum();

        System.out.println(spent);
        summaInfo.setText(String.valueOf(s));
        spentMonthInfo.setText(String.valueOf(spent));
        incomeMonthInfo.setText(String.valueOf(income));
    }
}

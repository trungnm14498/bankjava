package sample;

import features.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

public class OperationsWindow {
    private BankCeo bankCeo;
    private Account currentAccount;
    @FXML private TextField depositAmount;
    @FXML private TextField withdrawAmount;
    @FXML private TextField transferAmount;
    @FXML private TextField transferTargetId;
    @FXML private TableView<BankAction> operationTable;
    @FXML private Label balanceLabel;

    public void getBankCeo(BankCeo bankCeo){
        this.bankCeo = bankCeo;
    }
    public void getCurrentAccount(Account account){
        this.currentAccount = account;
        refreshTable();
    }

    public void performDeposit(ActionEvent event){
        double sum = Double.parseDouble(depositAmount.getText());
        bankCeo.deposit(currentAccount, sum);
        refreshTable();
    }
    public void performWithdraw(ActionEvent event){
        double sum = Double.parseDouble(withdrawAmount.getText());
        bankCeo.withdraw(currentAccount, sum);
        refreshTable();
    }
    public void performTransfer(ActionEvent event){
        double sum = Double.parseDouble(transferAmount.getText());
        int idTo = Integer.parseInt(transferTargetId.getText());
        int idFrom = currentAccount.getAccountID();
        bankCeo.transferMoney(idFrom, idTo, sum);
        refreshTable();
    }
    public void refreshTable(){
        operationTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("date"));
        operationTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("actionType"));
        operationTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("amount"));
        operationTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("accountID1"));
        operationTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("accountID2"));
        List<BankAction> events = bankCeo.getHistory(currentAccount);
        operationTable.setItems(FXCollections.observableArrayList(events));
        balanceLabel.setText(String.valueOf(currentAccount.getAccountBalance()));
        Working working = Working.getInstance();
        try{
            working.saveCeo(bankCeo);
        }
        catch (IOException e){
            System.out.println(e.getCause());
        }
    }
}

package sample;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import features.Account;
import features.BankCeo;
import features.Customer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainWindow {
    Customer currentUser;
    @FXML Label WelcomeLabel;
    @FXML GridPane accountsInfo;
    @FXML Button createAccountButton;
    private BankCeo bankCeo;

    public void getBankCeo(BankCeo bankCeo) {
        this.bankCeo = bankCeo;
    }

    public void refreshAccountList(){
        int index = 1;
        ArrayList<Account> currentUserAccount =  bankCeo.userAccounts(currentUser);
        accountsInfo.getChildren().clear();
        for (Account account: currentUserAccount) {
            Label labelID = new Label();
            labelID.setText(String.valueOf(account.getAccountID()));
            Label labelBalance = new Label();
            labelBalance.setText(String.valueOf(account.getAccountBalance()));
            Button closeAccountButton = new Button();
            closeAccountButton.setText("Close this account");
            closeAccountButton.setId(String.valueOf(account.getAccountID()));
            closeAccountButton.setOnAction(this::closeAccountPressed);
            Button operationButton = new Button();
            operationButton.setText("Open account");
            operationButton.setId("op" + String.valueOf(account.getAccountID()));
            operationButton.setOnAction(this::operationButtonPressed);
            accountsInfo.add(labelID, 0, index); // column=1 row=0
            accountsInfo.add(labelBalance, 1, index);  // column=2 row=0
            accountsInfo.add(closeAccountButton, 2, index);
            accountsInfo.add(operationButton, 3, index);
            index++;
        } }
    public void getCurrentUserInfo(String name,  LocalDate birthday,Customer.Sex sex, String email){
        this.currentUser = bankCeo.createCustomer(name, birthday, sex,  email);
        WelcomeLabel.setText("Welcome back, " + name);
        refreshAccountList();
    }
    public void createAccountPressed(ActionEvent event){
        bankCeo.createAccount(currentUser, 0);
        refreshAccountList();
    }
    public void closeAccountPressed(ActionEvent event){
        Button button = (Button) event.getSource();
        int id = Integer.parseInt(button.getId());
        bankCeo.closeAccount(id);
        refreshAccountList();
    }
    public void operationButtonPressed(ActionEvent event){
        Button button = (Button) event.getSource();
        String id = button.getId().substring(2);
        Account currentAccount = bankCeo.getAccountByID(Integer.parseInt(id));
        System.out.println(id);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("operationsWindow.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            OperationsWindow scene3Controller = fxmlLoader.getController();
            scene3Controller.getBankCeo(this.bankCeo);
            scene3Controller.getCurrentAccount(currentAccount);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOpacity(1);
            stage.setTitle("Bank App");
            stage.setScene(new Scene(root, 900, 600));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    refreshAccountList();
                    System.out.println('1');
                }
            });
            stage.showAndWait();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void statsButtonPressed(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("statsWindow.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            StatsWindow scene4Controller = fxmlLoader.getController();
            scene4Controller.getBankCeo(this.bankCeo);
            scene4Controller.getCurrentUserInfo(this.currentUser);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOpacity(1);
            stage.setTitle("Bank App");
            stage.setScene(new Scene(root, 900, 600));
            stage.showAndWait();
        }
        catch(IOException e) {
            e.printStackTrace();
        }}}
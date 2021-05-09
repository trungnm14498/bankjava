package sample;

import features.BankCeo;
import features.Customer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class Controller {
    @FXML private TextArea NameField;
    @FXML private DatePicker Birthday;
    @FXML private ToggleGroup Gender;
    @FXML private TextArea EmailField;
    @FXML private Button RegisterButton;
    private BankCeo bankCeo;

    public void getBankCeo(BankCeo bankCeo){
        this.bankCeo = bankCeo;
    }

    public boolean checker(String reason, Object obj){
        if (obj == null | obj == "") {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!!!");
            alert.setHeaderText("You didn't fill the field of " + reason);
            alert.setContentText("Please enter your " + reason);
            alert.showAndWait();
            return false;
        }
        else{
            return true;
        }
    }

    public void registerPressed(ActionEvent event){
        String name = NameField.getText();
        Boolean nameCheck = checker("name", name);
        LocalDate birthdayValue = Birthday.getValue();
        Boolean birthdayCheck = checker("birthday", birthdayValue);
        RadioButton selectedRadioButton = (RadioButton) Gender.getSelectedToggle();
        String toggleGroupValue = "";
        Boolean genderCheck = checker("gender", selectedRadioButton);
        if(genderCheck){
            toggleGroupValue = selectedRadioButton.getText();
        }
        String email = EmailField.getText();
        Boolean emailCheck = checker("Email", email);
        Customer.Sex sex = Customer.Sex.MALE;
        if (toggleGroupValue.equals("Female")){
            sex = Customer.Sex.FEMALE;
        }
        if (nameCheck && birthdayCheck && genderCheck && emailCheck){
            try {
                Stage close = (Stage) RegisterButton.getScene().getWindow();
                close.close();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                MainWindow scene2Controller = fxmlLoader.getController();
                scene2Controller.getBankCeo(this.bankCeo);
                scene2Controller.getCurrentUserInfo(name, birthdayValue, sex,  email);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setOpacity(1);
                stage.setTitle("Bank App");
                stage.setScene(new Scene(root, 900, 600));
                stage.showAndWait();
            }
            catch(IOException e) {
                e.printStackTrace();
            }

        }

    }
}

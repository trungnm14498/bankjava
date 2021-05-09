package sample;

import features.BankCeo;
import features.Working;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Working working = Working.getInstance();
        BankCeo restoredCeo = working.loadCeo();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Controller scene1Controller = loader.getController();
        scene1Controller.getBankCeo(restoredCeo);
        primaryStage.setTitle("Bank App");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            String url = "jdbc:postgresql://127.0.0.1:5432/BankDB";
            String user = "postgres";
            String password = "123456";
            Connection conn = DriverManager.getConnection(url,user,password);
            if (conn != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }
            String loadCustomersQuery = "SELECT * FROM x.customer";
            Statement loadCustomers = conn.createStatement();
            ResultSet customersX = loadCustomers.executeQuery(loadCustomersQuery);

            while(customersX.next()){

                int ID = customersX.getInt("ID");
                Boolean Gender = customersX.getBoolean("Gender");
                String Name = customersX.getString("Name");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        launch(args);
    }
}

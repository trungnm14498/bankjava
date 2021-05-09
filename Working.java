package features;

import java.io.*;
import java.time.LocalDate;

public class Working {
    private static Working instance;
    private Working(){
    }

    public static Working getInstance(){
        if (instance == null){
            instance = new Working();
        }
        return instance;
    }

    public void saveCeo(BankCeo bankCeo) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("controller.out"));
        objectOutputStream.writeObject(bankCeo);
    }
    public BankCeo loadCeo() throws IOException, ClassNotFoundException{
        BankCeo bankCeo = new BankCeo();
        bankCeo.load();
        return bankCeo;
    }
}

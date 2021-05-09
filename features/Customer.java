package features;

import java.time.LocalDate;
import java.time.Period;
import java.io.Serializable;
import java.time.chrono.IsoChronology;

public class Customer implements Serializable{
    public enum Sex {
        MALE, FEMALE
    }
    String name;
    LocalDate birthday;
    Sex gender;
    String emailAddress;
    int ID;

    public Customer(String nameArg, LocalDate birthdayArg, Sex genderArg,  String emailArg) {
        name = nameArg;
        gender = genderArg;
        birthday = birthdayArg;
        emailAddress = emailArg;
    }

    public Customer(int ID, String nameArg, LocalDate birthdayArg, Sex genderArg, String emailArg) {
        this.ID = ID;
        name = nameArg;
        birthday = birthdayArg;
        gender = genderArg;
        emailAddress = emailArg;
    }

    public int getAge() {
        return birthday.until(IsoChronology.INSTANCE.dateNow()).getYears();
    }

    public String getName() {
        return name;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setGender(Sex gender) {
        this.gender = gender;
    }

    public void setID(int ID){
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
}


package features;

import java.io.Serializable;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BankCeo implements Serializable {
    private ArrayList<Account> accountList;
    private ArrayList<Customer> cusList;
    private ArrayList<BankAction> actionList;
    private Connection conn;
    private PreparedStatement createCusStatement;
    private PreparedStatement createAccStatement;
    private PreparedStatement countStatement;
    private PreparedStatement checkCusStatement;
    private PreparedStatement depositStatement;
    private PreparedStatement closeStatement;
    private PreparedStatement actionStatement;
    private Statement loadAccounts;
    private Statement loadCustomers;
    private Statement loadActions;




    public BankCeo(){
        this.accountList = new ArrayList<Account>();
        this.cusList = new ArrayList<Customer>();
        this.actionList = new ArrayList<BankAction>();
        String url = "jdbc:postgresql://127.0.0.1:5432/BankDB";
        String user = "postgres";
        String password = "123456";
        try {
            this.conn = DriverManager.getConnection(url,user,password);

            String checkCusQuery = "SELECT ID, Birthday, Gender FROM x.customer WHERE (Name = ?) AND (Email = ?)";
            this.checkCusStatement = conn.prepareStatement(checkCusQuery);

            String createCusQuery = "INSERT INTO x.customer (Name, Birthday, Email, Gender) VALUES (?,?,?,?)";
            this.createCusStatement = conn.prepareStatement(createCusQuery, Statement.RETURN_GENERATED_KEYS);

            String countQuery = "SELECT COUNT(*) AS count FROM x.customer WHERE (Name = ?) AND (Email = ?)";
            this.countStatement = conn.prepareStatement(countQuery);

            String createAccQuery = "INSERT INTO x.account (Owner, Balance, Status) VALUES (?,?,1)";
            this.createAccStatement = conn.prepareStatement(createAccQuery);

            String depositQuery = "UPDATE x.account SET Balance = Balance + ? WHERE ID = ?";
            this.depositStatement = conn.prepareStatement(depositQuery);

            String closeQuery = "UPDATE x.account SET status = 0 WHERE ID = ?";
            this.closeStatement = conn.prepareStatement(closeQuery);

            String actionQuery = "INSERT INTO x.bankaction (Action, AccountID2, AccountID1, Amount, Date) VALUES (?,?,?,?,?)";
            this.actionStatement = conn.prepareStatement(actionQuery);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void load(){
        try {
            String loadCusQuery = "SELECT * FROM x.customer";
            this.loadCustomers = this.conn.createStatement();
            ResultSet customers = this.loadCustomers.executeQuery(loadCusQuery);
            while (customers.next()){
                int ID = customers.getInt("ID");
                Boolean Gender = customers.getBoolean("Gender");
                String Name = customers.getString("Name");
                String Email = customers.getString("Email");
                LocalDate Birthday = customers.getDate("Birthday").toLocalDate();
                Customer.Sex sexArg = Customer.Sex.MALE;
                if (Gender != true) {
                    sexArg = Customer.Sex.FEMALE;
                }
                this.cusList.add(new Customer(ID,Name,Birthday,sexArg,Email));
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            String loadAccQuery = "SELECT * FROM x.account";
            this.loadAccounts = this.conn.createStatement();
            ResultSet accounts = this.loadAccounts.executeQuery(loadAccQuery);
            while(accounts.next()){
                int ID = accounts.getInt("ID");
                int ownerID = accounts.getInt("Owner");
                double Balance = accounts.getDouble("Balance");
                boolean Status = accounts.getBoolean("Status");
                Customer customer = getUserByID(ownerID);
                this.accountList.add(new Account(ID,customer,Balance, Status));
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
    }
        try {
            String loadActQuery = "SELECT * FROM x.bankaction";
            this.loadActions = this.conn.createStatement();
            ResultSet actions = this.loadActions.executeQuery(loadActQuery);
            while (actions.next()) {
                int ID = actions.getInt("ID");
                String Action = actions.getString("Action");
                int AccountID2 = actions.getInt("AccountID2");
                int AccountID1 = actions.getInt("AccountID1");
                double Amount = actions.getDouble("Amount");
                LocalDate Date = actions.getDate("Date").toLocalDate();
                Account account1 = getAccountByID(AccountID1);
                switch (Action) {
                    case ("OPEN") -> this.actionList.add(new BankAction(account1, Date, BankAction.Action.OPEN));
                    case ("TRANSFER") -> {
                        Account account2 = getAccountByID(AccountID2);
                        this.actionList.add(new BankAction(account1, account2, Date, BankAction.Action.TRANSFER, Amount));
                    }
                    case ("DEPOSIT") -> this.actionList.add(new BankAction(account1, Date, BankAction.Action.DEPOSIT, Amount));
                    case ("WITHDRAW") -> this.actionList.add(new BankAction(account1, Date, BankAction.Action.WITHDRAW, Amount));
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
    } }

    public Customer createCustomer(String nameArg, LocalDate birthdayArg , Customer.Sex genderArg,  String emailArg){
        Customer customer = new Customer(nameArg, birthdayArg , genderArg,  emailArg);
        try {
            countStatement.setString(1,nameArg);
            countStatement.setString(2,emailArg);
            ResultSet resultSetCount = countStatement.executeQuery();
            resultSetCount.next();
            int rowCount = resultSetCount.getInt("count");
            if (rowCount == 0){
                createCusStatement.setString(1,nameArg);
                createCusStatement.setDate(2, Date.valueOf(birthdayArg));
                createCusStatement.setString(3, emailArg);
                Boolean sexArg = true;
                if (genderArg == Customer.Sex.FEMALE){
                    sexArg = false;
                }
                createCusStatement.setBoolean(4,sexArg);
                createCusStatement.executeUpdate();
                ResultSet rs = createCusStatement.getGeneratedKeys();
                int generateKey = 0;
                if (rs.next()) {
                    generateKey = rs.getInt(1);
                }
                customer.setID(generateKey);
            }
            else {
                checkCusStatement.setString(1,nameArg);
                checkCusStatement.setString(2,emailArg);
                ResultSet resultSetCustomer = checkCusStatement.executeQuery();
                resultSetCustomer.next();
                int Gender = resultSetCustomer.getInt("Gender");
                LocalDate Birthday = resultSetCustomer.getDate("Birthday").toLocalDate();
                int ID = resultSetCustomer.getInt("ID");
                customer.setID(ID);
                customer.setBirthday(Birthday);
                Customer.Sex sexArg = Customer.Sex.MALE;
                if (Gender == 0){
                    sexArg = Customer.Sex.FEMALE;
                }
                customer.setGender(sexArg);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        this.cusList.add(customer);
        return customer;
    }

    public Account createAccount(Customer owner, double accountBalance){
       Account myAcc = new Account(owner, accountBalance);
       try {
           createAccStatement.setInt(1,owner.ID);
           createAccStatement.setDouble(2,accountBalance);
           createAccStatement.executeUpdate();
           ResultSet rs = createAccStatement.getGeneratedKeys();
           int generatedKey = 0;
           if (rs.next()) {
               generatedKey = rs.getInt(1);
           }
           myAcc.setAccountID(generatedKey);
           this.actionStatement.setString(1, "OPEN");
           this.actionStatement.setNull(2, Types.INTEGER);
           this.actionStatement.setInt(3, generatedKey);
           this.actionStatement.setDouble(4, 0);
           this.actionStatement.setDate(5, Date.valueOf(LocalDate.now()));
           this.actionStatement.executeUpdate();
       }
       catch (Exception e){
           e.printStackTrace();
       }
       this.accountList.add(myAcc);

       actionList.add(new BankAction(myAcc, LocalDate.now(), BankAction.Action.OPEN));
       return myAcc;
       }

    public void closeAccount(int accountID){
        try {
            this.closeStatement.setInt(1, accountID);
            this.closeStatement.executeUpdate();
            Account accountToRemove = accountList.stream().filter(bankAccount -> accountID == bankAccount.getAccountID()).findAny().orElse(null);
            accountList.removeIf(account -> accountID == account.getAccountID());
            accountToRemove = null;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deposit (Account account, double amount){
        if (amount > 0) {
            try {
                this.depositStatement.setDouble(1, amount);
                this.depositStatement.setInt(2, account.getAccountID());
                this.depositStatement.executeUpdate();
                this.actionStatement.setString(1, "DEPOSIT");
                this.actionStatement.setNull(2, Types.INTEGER);
                this.actionStatement.setInt(3, account.getAccountID());
                this.actionStatement.setDouble(4, amount);
                this.actionStatement.setDate(5, Date.valueOf(LocalDate.now()));
                this.actionStatement.executeUpdate();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            account.setAccountBalance(account.getAccountBalance() + amount);
            actionList.add(new BankAction(account, LocalDate.now(), BankAction.Action.DEPOSIT, amount));
        } else {
            System.out.println("Please enter a valid amount!!!");
        }
    }

    public void withdraw (Account account, double amount){
        if (amount > 0 && amount < account.getAccountBalance()) {
            try {
                this.depositStatement.setDouble(1, -amount);
                this.depositStatement.setInt(2, account.getAccountID());
                this.depositStatement.executeUpdate();
                this.actionStatement.setString(1, "WITHDRAW");
                this.actionStatement.setNull(2, Types.INTEGER);
                this.actionStatement.setInt(3, account.getAccountID());
                this.actionStatement.setDouble(4, amount);
                this.actionStatement.setDate(5, Date.valueOf(LocalDate.now()));
                this.actionStatement.executeUpdate();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            account.setAccountBalance(account.getAccountBalance() -  amount);
            actionList.add(new BankAction(account, LocalDate.now(), BankAction.Action.WITHDRAW, amount));
        } else  {
            System.out.println("Please enter a valid amount!!!");
        }
    }

    public boolean transferMoney(int accountID1, int accountID2, double amount){
        Account account1 = accountList.stream().filter(Account -> accountID1 == Account.getAccountID()).findAny().orElse(null);
        Account account2 = accountList.stream().filter(Account -> accountID2 == Account.getAccountID()).findAny().orElse(null);
        if (amount <= account1.getAccountBalance()) {
            try {
                account1.setAccountBalance(account1.getAccountBalance() - amount);
                actionList.add(new BankAction(account1, account2, LocalDate.now(), BankAction.Action.TRANSFER, amount));
                this.depositStatement.setDouble(1, -amount);
                this.depositStatement.setInt(2, accountID1);
                this.depositStatement.executeUpdate();
                this.depositStatement.setDouble(1, amount);
                this.depositStatement.setInt(2, accountID2);
                this.depositStatement.executeUpdate();
                this.actionStatement.setString(1, "TRANSFER");
                this.actionStatement.setInt(2, accountID2);
                this.actionStatement.setInt(3, accountID1);
                this.actionStatement.setDouble(4, amount);
                this.actionStatement.setDate(5, Date.valueOf(LocalDate.now()));
                this.actionStatement.executeUpdate();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return true;
        }
        else {
            return false;
        }
    }
    public ArrayList<Account> userAccounts(Customer customer){
        return accountList.stream().filter(Account -> Account.getOwnerName().equals(customer.name)).collect(Collectors.toCollection(ArrayList::new));
    }
    public Account getAccountByID(int ID){
        return accountList.stream().filter(Account -> Account.getAccountID() == ID).findAny().get();
    }
    public Customer getUserByID(int ID){
        return cusList.stream().filter(customer -> customer.getID() == ID).findAny().get();
    }
    public List<BankAction> getHistory(Account account){
        return this.actionList.stream().filter(action -> (account.equals(action.account) | account.equals(action.account2))).collect(Collectors.toList());
    }
}

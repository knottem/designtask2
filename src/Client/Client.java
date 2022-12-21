package Client;

import Accounts.Account;
import Accounts.AccountFactory;
import Accounts.AccountType;
import Database.Database;
import Users.*;
import Utility.Utility;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

    LocalDate today = LocalDate.now();
    Utility utility = new Utility();
    FAQ faq = new FAQ();

    static Database database = Database.getDatabase();

    static volatile boolean wait = false;
    static int counter = 1;

    void Program() {
        boolean program = true;

        System.out.println("\nVälkommen till Bank Systemet Klient: " + counter);
        System.out.println("Dagens Datum: " + today + "\n");
        System.out.println("Kontaktinformation:\nEmail: bank@bank.com\nAdress: Bankgatan 1, Bankstaden.\n");
        while (program) {
            int answer = utility.inputInt("""
                    Vad vill du göra?
                    1. Logga in
                    2. Skapa ny användare
                    3. FAQ
                    4. Avsluta Programmet""");
            switch (answer) {
                case (1) -> {
                    System.out.println("\nSkriv in personnummer:");
                    Scanner scan = new Scanner(System.in);
                    String name = scan.nextLine();
                    boolean found = false;
                    for (int i = 0; i < database.getCustomers().size(); i++) {
                        if (name.equalsIgnoreCase(database.getCustomers().get(i).getNumber())) {
                            System.out.println("Skriv in lösenord:");
                            String password = scan.nextLine();
                            found = true;
                            if (password.equals(database.getCustomers().get(i).getPassword())) {
                                login(database.getCustomers().get(i));
                            } else {
                                System.out.println("Felaktigt lösenord.\n");
                            }
                            break;
                        }
                    }
                    for (int i = 0; i < database.getAdmins().size(); i++) {
                        if (name.equalsIgnoreCase(database.getAdmins().get(i).getNumber())) {
                            System.out.println("Skriv in lösenord:");
                            String password = scan.nextLine();
                            found = true;
                            if (password.equals(database.getAdmins().get(i).getPassword())) {
                                loginAdmin(database.getAdmins().get(i));
                            } else {
                                System.out.println("Felaktigt lösenord.\n");
                            }
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println("Användaren hittades inte\n");
                    }
                }
                case (2) -> {
                    addCustomer();
                    utility.sleep(1000);
                }
                case (3) -> {
                    faq.readingFAQ();
                    utility.sleep(1000);
                }
                case (4) -> {
                    wait = false;
                    program = false;
                }
                default -> System.out.println("Felaktigt nummer");
            }
        }
    }

    private void login (Customer customer){
        boolean startLoop = true;
        do {
            int answer = utility.inputInt("Välkommen " + customer.getName() +
                    "\n1. Överföra pengar\n2. Sätta in pengar\n3. Ta ut pengar\n4. Kolla dina konton\n5. Skapa nytt bankkonto\n6. Logga ut");
                switch (answer) {
                    case (1) -> utility.transfer(customer);
                    case (2) -> utility.deposit(customer);
                    case (3) -> utility.withdraw(customer);
                    case (4) -> utility.checkAccount(customer);
                    case (5) -> utility.createNewAccount(customer);
                    case (6) -> startLoop = false;
                    default -> System.out.println("Felaktigt nummer");
                }
            }while(startLoop);
    }

    private void loginAdmin (Admin admin){
        boolean startLoop = true;
        do {
            int answer = utility.inputInt("Välkommen " + admin.getName() +
                    "\n1. Uppdatera FAQ\n2. Ta bort Kund \n3. Ta bort konto\n4. Logga ut");
            switch (answer) {
                case (1) -> faq.writingFAQ();
                case (2) -> utility.deleteCustomer(admin);
                case (3) -> utility.deleteAccount(admin);
                case (4) -> startLoop = false;
                default -> System.out.println("Felaktigt nummer");
            }
        }while(startLoop);
    }

    private void addCustomer(){
        System.out.println("Skriv in ditt namn");
        Scanner scan = new Scanner(System.in);
        String name = scan.nextLine();
        System.out.println("Skriv in ditt lösenord");
        String password = scan.nextLine();
        System.out.println("Skriv in ditt personnummer");
        String idNumber = scan.nextLine();
        ArrayList<Account> temp = new ArrayList<>();
        Account temp2 = AccountFactory.getAccount(AccountType.BASICACCOUNT);
        temp2.setId(utility.createRandomNumber());
        temp2.setBalance(0);
        temp.add(temp2);
        database.getCustomers().add(new Customer(name, password, idNumber, temp));
        database.updateCustomerTextFile();
    }

    public static void main(String[] args) {
        Thread threadOne = new Thread(new ThreadClient());
        Thread threadTwo = new Thread(new ThreadClient());
        threadOne.start();
        wait = true;
        while (wait) {
            Thread.onSpinWait();
        }
        threadTwo.start();
    }

    static class ThreadClient implements Runnable {
        @Override
        public void run() {
            Client client = new Client();
            System.out.println(database);
            client.Program();
            counter++;
        }
    }
}

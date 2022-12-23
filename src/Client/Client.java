package Client;

import Database.Database;
import Users.*;
import Utility.FacadeSystem;
import Utility.History;

import java.time.LocalDate;

public class Client {

    LocalDate today = LocalDate.now();
    FacadeSystem facade = new FacadeSystem(this);
    FAQ faq = new FAQ();
    History history = new History();

    static Database database = Database.getDatabase();

    static volatile boolean wait = false;
    static int counter = 1;

    private void Program() {
        boolean program = true;

        System.out.println("\nVälkommen till Bank Systemet Klient: " + counter);
        System.out.println("Dagens Datum: " + today + "\n");
        System.out.println("Kontaktinformation:\nEmail: bank@bank.com\nAdress: Bankgatan 1, Bankstaden.\n");
        while (program) {
            int answer = facade.inputInt("""
                    Vad vill du göra?
                    1. Logga in
                    2. Skapa ny användare
                    3. FAQ
                    4. Avsluta Programmet""");
            switch (answer) {
                case (1) -> {
                    facade.loginAttempt();
                    facade.sleep(1000);
                }
                case (2) -> {
                    facade.addCustomer();
                    facade.sleep(1000);
                }
                case (3) -> {
                    faq.readingFAQ();
                    facade.sleep(1000);
                }
                case (4) -> {
                    wait = false;
                    program = false;
                }
                default -> System.out.println("Felaktigt nummer");
            }
        }
    }

    public void login (Customer customer){
        boolean startLoop = true;
        do {
            int answer = facade.inputInt("Välkommen " + customer.getName() +
                    "\n1. Överföra pengar\n2. Sätta in pengar\n3. Ta ut pengar\n4. Kolla dina konton\n5. Skapa nytt bankkonto\n6. Kolla din historik\n7. Logga ut");
                switch (answer) {
                    case (1) -> facade.transfer(customer);
                    case (2) -> facade.deposit(customer);
                    case (3) -> facade.withdraw(customer);
                    case (4) -> facade.checkAccount(customer);
                    case (5) -> facade.createNewAccount(customer);
                    case (6) -> history.readFile(customer);
                    case (7) -> startLoop = false;
                    default -> System.out.println("Felaktigt nummer");
                }
            }while(startLoop);
    }

    public void loginAdmin (Admin admin){
        boolean startLoop = true;
        do {
            int answer = facade.inputInt("Välkommen " + admin.getName() +
                    "\n1. Uppdatera FAQ\n2. Ta bort Kund \n3. Ta bort konto\n4. Logga ut");
            switch (answer) {
                case (1) -> faq.writingFAQ();
                case (2) -> facade.deleteCustomer(admin);
                case (3) -> facade.deleteAccount(admin);
                case (4) -> startLoop = false;
                default -> System.out.println("Felaktigt nummer");
            }
        }while(startLoop);
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

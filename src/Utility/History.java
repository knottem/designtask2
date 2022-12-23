package Utility;

import Users.Customer;
import Users.User;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class History {
    File file = new File("resources/history.txt");

    ArrayList<String> history = new ArrayList<>();

    public void writeToFile(String text, User user) {
        try(BufferedWriter print = new BufferedWriter(new FileWriter(file, true))){

            String textForHistoryTextFile = "User: "+ user.getName() + ".\n" +
                    "Activity: " + text + ".\n" +
                    "Time: " + LocalTime.now().withNano(0) + ".\n" +
                    "Date: " + LocalDate.now() + "\n\n";
            print.write(textForHistoryTextFile);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readFile(Customer customer){

        String temp;
        boolean found = false;

        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            while((temp = reader.readLine()) !=  null){
                if(temp.contains("User: " + customer.getName())){
                    System.out.println(reader.readLine());
                    System.out.println(reader.readLine());
                    System.out.println(reader.readLine() + "\n");
                    found = true;
                }
            }
            if(!found){
                System.out.println("Du har ingen historik \n");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
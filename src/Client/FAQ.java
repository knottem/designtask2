package Client;

import Users.Admin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FAQ {
    Path faq = Paths.get("resources/FrequentlyAskedQuestions.txt");
    public void readingFAQ() {

        String line;

            try (BufferedReader faqRead = Files.newBufferedReader(faq)) {
                while ((line = faqRead.readLine()) != null)
                    System.out.println(line);

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void writingFAQ(){
            Scanner scanFAQ = new Scanner(System.in);
            System.out.println("\nSkriv in en ny fråga:");
            String newQuestion = scanFAQ.nextLine();
            System.out.println("\nSkriv in ett nytt svar:");
            String newAnswer = scanFAQ.nextLine();

            try(PrintWriter writing = new PrintWriter(new BufferedWriter(new FileWriter("resources/FrequentlyAskedQuestions.txt", true)));){
                writing.append(System.lineSeparator() + newQuestion + " " + newAnswer);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }







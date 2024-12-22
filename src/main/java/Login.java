import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Login {

    public static void main(String[] args) throws IOException, ParseException {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter your username: ");
        String Inputusername = sc.nextLine();

        System.out.println("Enter your password: ");
        String Inputpassword = sc.nextLine();

        System.out.println("Enter your role: ");
        String Inputrole = sc.nextLine();

        String filepath = "./src/main/resources/users.json";
        JSONParser parser = new JSONParser();


        JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(filepath));

        boolean validCredentials = false;
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
            String username = jsonObject.get("username").toString();
            String password = jsonObject.get("password").toString();
            String role = jsonObject.get("role").toString();

            if (username.equalsIgnoreCase(Inputusername) && password.equals(Inputpassword) && role.equalsIgnoreCase(Inputrole)) {
                validCredentials = true;
                if (role.equalsIgnoreCase("admin")) {
                    System.out.println("Welcome admin! Please create new questions in the question bank.");
                    admin();
                } else if (role.equalsIgnoreCase("student")) {
                    System.out.println("Would you like to start again? Press 's' to start or 'q' to quit:");
                    String choice = sc.nextLine();
                    if (choice.equalsIgnoreCase("s")) {
                        student();
                    } else if (choice.equalsIgnoreCase("q")) {
                        break;
                    }



                }
                break;
            }
        }

        if (!validCredentials) {
            System.out.println("Invalid credentials or role.");
        }

        sc.close();
    }

    public static void admin() throws IOException, ParseException {
        String filepath = "./src/main/resources/quiz.json";
        int addquestion = 30;
        JSONParser jsonParser = new JSONParser();


        JSONArray jsonArray = (JSONArray) jsonParser.parse(new FileReader(filepath));

        int questionCount = jsonArray.size();

        if (questionCount >= addquestion) {
            System.out.println("The quiz already has 30 questions. No more questions can be added.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        while (questionCount < addquestion) {
            JSONObject newQuestion = new JSONObject();

            System.out.println("Input your question:");
            newQuestion.put("question", scanner.nextLine());

            System.out.println("Input option 1:");
            newQuestion.put("option1", scanner.nextLine());

            System.out.println("Input option 2:");
            newQuestion.put("option2", scanner.nextLine());

            System.out.println("Input option 3:");
            newQuestion.put("option3", scanner.nextLine());

            System.out.println("Input option 4:");
            newQuestion.put("option4", scanner.nextLine());

            System.out.print("Enter the correct option number (1-4):");
            int correctAnswer = scanner.nextInt();
            scanner.nextLine();
            newQuestion.put("answerkey", correctAnswer);

            jsonArray.add(newQuestion);
            questionCount++;

            System.out.println("Question added successfully!");

            if (questionCount < addquestion) {
                System.out.println("Do you want to add another question? Press 's' to add more or 'q' to quit:");
                String choice = scanner.nextLine();
                if (choice.equalsIgnoreCase("q")) {
                    break;
                }
            }
        }

        FileWriter fileWriter = new FileWriter(filepath);
        fileWriter.write(jsonArray.toJSONString());
        fileWriter.flush();
        fileWriter.close();

        System.out.println("All questions saved successfully!");
    }


    public  static  void  student() throws IOException, ParseException {
        String filepath = "./src/main/resources/quiz.json";
        JSONParser parser = new JSONParser();

        JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(filepath));

        Random random = new Random();
        int totalQuestions = jsonArray.size();
        Scanner scanner = new Scanner(System.in);

        boolean restart = true;
        while (restart) {
            int marks = 0;

            for (int i = 0; i < 10; i++) {
                int questionIndex = random.nextInt(totalQuestions);
                JSONObject questionObj = (JSONObject) jsonArray.get(questionIndex);

                System.out.println("Question " + (i + 1) + ": " + questionObj.get("question"));
                System.out.println("1. " + questionObj.get("option 1"));
                System.out.println("2. " + questionObj.get("option 2"));
                System.out.println("3. " + questionObj.get("option 3"));
                System.out.println("4. " + questionObj.get("option 4"));

                System.out.print("Enter your answer (1-4): ");
                int userAnswer = scanner.nextInt();

                int correctAnswer = Integer.parseInt(questionObj.get("answerkey").toString());
                if (userAnswer == correctAnswer) {
                   // System.out.println("Correct!");
                    marks++;
                } else if (userAnswer < 1 || userAnswer > 4) {
                    System.out.println("Invalid answer. Moving to the next question.");
                }
            }

            System.out.println("\nQuiz completed! Your score: " + marks + "/10");
            if (marks >= 8) {
                System.out.println("Excellent! You have got " + marks + " out of 10");
            } else if (marks >= 5) {
                System.out.println("Good. You have got " + marks + " out of 10");
            } else if (marks >= 2) {
                System.out.println("Very poor! You have got. You have got  " + marks + " out of 10");
            } else {
                System.out.println("Very sorry you are failed. You have got  " + marks + " out of 10");
            }

            System.out.println("Would you like to start again? Press 's' to start or 'q' to quit:");
            scanner.nextLine();
            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("q")) {
                System.out.println("Thank you for playing!");
                restart = false;
            }
        }

        scanner.close();
    }
    }


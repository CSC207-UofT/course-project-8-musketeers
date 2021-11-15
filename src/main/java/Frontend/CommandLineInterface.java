package Frontend;

import Backend.*;
import Backend.AxesUseCase;
import Backend.Exceptions.InvalidTermException;
import Backend.ExpressionReader;
import Backend.Expressions.Expression;
import Backend.Expressions.RealValuedExpression;
import Graphics.Grapher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandLineInterface {
    /**
     * The main method of the Command Line Interface. To run this inside IntelliJ, type
     * "java src/main/java/Frontend/CommandLineInterface.java" into the Terminal and type the required
     * commands and responses.
     * Accepted (i.e., structurally correct) user input:
     * - java src/main/java/Frontend/CommandLineInterface.java -eq "x^2 + 1 = 0" -graph BOUNDARY
     * - java src/main/java/Frontend/CommandLineInterface.java -dim 5
     * - java src/main/java/Frontend/CommandLineInterface.java -eq "x=0" -eq "y=6"
     * @param args An array of Strings containing the user inputs, split by a space " "
     */
    public static void main(String[] args) {
        CommandLineInterface cli = new CommandLineInterface();

        ArrayList<String> userInputs = new ArrayList<>(Arrays.asList(args));

        if (!cli.checkValidInput(userInputs)){
            return;
        }

        Axes axes = new Axes();
        AxesUseCase auc = new AxesUseCase();

        if (userInputs.contains("-load")) {
            String filename = cli.getCommandArgument("-load", userInputs);
            try {
                axes = auc.loadAxes(filename);
            } catch (IOException e) {
                System.out.println("Sorry axes corresponding to " + filename + " was not found");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        ExpressionReader er = new ExpressionReader(auc.getNamedFunctions(axes));
        Grapher grapher = new Grapher(axes);

        List<String[]> equationsAndDomains = cli.findAllEquations(args);

        for (String[] expArray: equationsAndDomains) {
            try {
                Expression<?> exp = er.read(expArray[0]);
                if (exp instanceof RealValuedExpression) {
                    // TODO: implement domain restrictions
                    auc.addExpression((RealValuedExpression) exp, axes);
                }
            } catch (InvalidTermException e) {
                System.out.println("Error with interpreting input <" + expArray[0] + ">:" + e.getMessage());
            }
        }

        try {
            String gType = cli.getCommandArgument("-graph", userInputs);
            grapher.graph(512, gType, "test.png");
        } catch (IOException e) {
            System.out.println("Image could not be saved");
            e.printStackTrace();
        }

        if (userInputs.contains("-save")){
            String filename = cli.getCommandArgument("-save", userInputs);
            try {
                auc.saveAxes(filename, axes);
            } catch (IOException e) {
                System.out.println("File could not be save");
                e.printStackTrace();
            }
        }
    }

    private boolean checkValidInput(List<String> userInputs){
        boolean userInputIsValid = true;
        // An array of strings containing accepted Strings. This is open to extension as other parts
        // of the code become available to be merged into this CLI.
        String[] acceptedCommands = {"-eq", "-dim", "-graph", "-save", "-load"};
        if (userInputs.size() % 2 != 0) {
            // There is an issue -- the (command, response) pair doesn't match up
            // Add null at the end for now, and find the issue
            userInputs.add(null);
        }
        for (int i = 0; i < userInputs.size(); i += 2) {
            String firstElementOfPair = userInputs.get(i).toLowerCase();
            String secondElementOfPair = userInputs.get(i + 1);
            userInputIsValid = isUserInputValid(acceptedCommands, firstElementOfPair, secondElementOfPair);
            if (!userInputIsValid) {
                break;
            }
        }
        return userInputIsValid;
    }

    private String getCommandArgument(String command, List<String> userInputs){
        return userInputs.get(userInputs.indexOf(command) + 1);
    }

    /**
     * Checks whether the user input is valid based on the first and the second element of user input pair.
     * A pair of the user input is VALID if all the following has been satisfied:
     * 1. the first element of the pair (which is a format of "-****") is in the array of accepted commands.
     * 2. the second element of the pair is not null (null implies one or more commands had missing responses)
     * 3. the first element of the pair is "-dim" and the second element of the pair (i.e., response) is a
     * positive integer (the dimension of a function can only be a positive dimension)
     * We expect there will be more checks to be done in the CLI level. Thus, this method is open for extension.
     * @param acceptedCommands an array of String containing accepted commands
     * @param firstElementOfPair the first element (i.e., command) of the (command, response) pair
     * @param secondElementOfPair the second element (i.e., response) of the (command, response) pair
     * @return whether the user input is valid
     */
    private boolean isUserInputValid(String[] acceptedCommands, String firstElementOfPair,
                                     String secondElementOfPair) {
        boolean userInputIsValid = true;
        if (!Arrays.asList(acceptedCommands).contains(firstElementOfPair)) {
            System.out.println("\"" + firstElementOfPair + "\" is not a valid command. Please try again.");
            userInputIsValid = false;
        } else if (secondElementOfPair == null) {
            System.out.println("\"" + firstElementOfPair + "\" has no valid response. Please try again.");
            userInputIsValid = false;
        } else if (firstElementOfPair.equals("-dim") && !isPositiveInteger(secondElementOfPair)) {
            System.out.println("-dim needs to be followed by a positive integer. Please try again.");
            userInputIsValid = false;
        }
        return userInputIsValid;
    }

    /**
     * Adapted from https://stackoverflow.com/questions/237159/
     * whats-the-best-way-to-check-if-a-string-represents-an-integer-in-java. Checks whether an input string
     * is a valid positive integer.
     * @param userInput The user input
     * @return whether the user input is a valid positive integer
     */
    private boolean isPositiveInteger(String userInput) {
        if (userInput == null) {
            return false;
        }
        int length = userInput.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (userInput.charAt(0) == '-' || userInput.equals("0")) {
            return false;
        }
        for (; i < length; i++) {
            char c = userInput.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    private List<String[]> findAllEquations(String[] arguments){
        List<String[]> equationsAndDomains = new ArrayList<>();

        // TODO: Move commands to constants
        for(int i = 0; i < arguments.length - 3; i+=2){
            if (arguments[i].equals("-eq")){
                if(arguments[i + 2].equals("-domain")){
                    equationsAndDomains.add(new String[]{arguments[i + 1], arguments[i + 3]});
                }
                else{
                    equationsAndDomains.add(new String[]{arguments[i + 1]});
                }
            }
        }
        return equationsAndDomains;
    }
}

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

        // An array of strings containing accepted Commands. This is open to extension as other parts
        // of the code become available to be merged into this CLI.
        String[] acceptedCommands = {"-eq", "-dim", "-graph", "-save", "-load"};

        if (!cli.checkValidInput(acceptedCommands, userInputs)){
            return;
        }

        Axes axes = new Axes();
        AxesUseCase auc = new AxesUseCase();

        if (userInputs.contains("-load")) {
            axes = cli.tryLoadingAxes(cli, userInputs, axes, auc);
        }

        ExpressionReader er = new ExpressionReader(auc.getNamedFunctions(axes));
        Grapher grapher = new Grapher(axes);
        List<String[]> equationsAndDomains = cli.findAllEquations(args);
        cli.tryInterpretingInput(axes, auc, er, equationsAndDomains);
        cli.tryGraphingAndSavingImage(cli, userInputs, grapher);

        if (userInputs.contains("-save")){
            cli.trySavingAxes(cli, userInputs, axes, auc);
        }
    }

    /**
     * Simple try and catch statements to save axes.
     * @param cli an instance of CommandLineInterface
     * @param userInputs an ArrayList containing user inputs
     * @param axes an instance of Axes
     * @param auc an instance of AxesUseCase
     */
    private void trySavingAxes(CommandLineInterface cli, ArrayList<String> userInputs,
                               Axes axes, AxesUseCase auc) {
        String filename = cli.getCommandArgument("-save", userInputs);
        try {
            auc.saveAxes(filename, axes);
        } catch (IOException e) {
            System.out.println("File could not be save");
            e.printStackTrace();
        }
    }

    /**
     * Simple try and catch statements to graph and save the resulting image.
     * @param cli An instance of CommandLineInterface
     * @param userInputs an ArrayList containing user inputs
     * @param grapher an instance of Grapher
     */
    private void tryGraphingAndSavingImage(CommandLineInterface cli, ArrayList<String> userInputs,
                                           Grapher grapher) {
        try {
            String gType = cli.getCommandArgument("-graph", userInputs);
            grapher.graph(512, gType, "test.png");
        } catch (IOException e) {
            System.out.println("Image could not be saved");
            e.printStackTrace();
        }
    }

    /**
     * Simple try and catch statements to interpret user input.
     * @param axes an instance of Axes
     * @param auc an instance of AxesUseCase
     * @param er an instance of ExpressionReader
     * @param equationsAndDomains a list of strings containing arrays of strings: equations and domains
     */
    private void tryInterpretingInput(Axes axes, AxesUseCase auc, ExpressionReader er,
                                      List<String[]> equationsAndDomains) {
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
    }

    /**
     * Simple try and catch statements to load axes.
     * @param cli an instance of CommandLineInterface
     * @param userInputs an ArrayList of strings representing
     * @param axes an instance of Axes
     * @param auc an instance of AxesUseCase
     * @return an instance of Axes, either the same one from axes or an updated one (if no errors were thrown)
     */
    private Axes tryLoadingAxes(CommandLineInterface cli, ArrayList<String> userInputs,
                                       Axes axes, AxesUseCase auc) {
        String filename = cli.getCommandArgument("-load", userInputs);
        try {
            axes = auc.loadAxes(filename);
        } catch (IOException e) {
            System.out.println("Sorry axes corresponding to " + filename + " was not found");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return axes;
    }

    /**
     * Check whether the list of user inputs is (structurally) valid.
     * For the list of user inputs to be valid, there are three minimum requirements to be satisfied:
     * 1. The length of the list of user inputs must be a multiple of 2. Otherwise, this indicates that
     * the command and the response pair does not match up, immediately resulting in an invalid user input.
     * In this case, a null value will be appended to the list to find the issue inside the
     * isUserInputPairValid method.
     * 2. Every input pair inside the userInputs must be valid. If null was appended after the first check,
     * isUserInputPairValid will automatically return false, thus immediately resulting in an invalid
     * user input. See isUserInputPairValid method for more information.
     * 3. There are no duplicate commands (except for "-eq") inside the list of user inputs.
     * Otherwise, this immediately results in an invalid user input.
     * @param acceptedCommands an array of Strings containing accepted commands
     * @param userInputs a list of Strings containing user inputs
     * @return whether userInputs is valid, as defined in the docstring
     */
    private boolean checkValidInput(String[] acceptedCommands, List<String> userInputs){
        boolean userInputIsValid = true;

        // First check: if the size of the user input list is not an even number, it means the command and
        // the response pair does not match up. A null will be appended for now to find an issue inside
        // the isUserInputPairValid method
        if (userInputs.size() % 2 != 0) {
            userInputs.add(null);
        }

        // Second check: every input pair must be valid. See isUserInputPairValid method for more information.
        for (int i = 0; i < userInputs.size(); i += 2) {
            String firstElementOfPair = userInputs.get(i).toLowerCase();
            String secondElementOfPair = userInputs.get(i + 1);
            userInputIsValid = isUserInputPairValid(acceptedCommands, firstElementOfPair, secondElementOfPair);
            if (!userInputIsValid) {
                break;
            }
        }

        // Third check: if the first two checks have passed, the array of user inputs contain valid pairs of
        // (command, response). This final check will check whether the user input contains
        // duplicate commands (except for "-eq"), immediately making the input invalid.
        if (userInputIsValid && isDuplicateCommandInUserInput(acceptedCommands, userInputs)) {
            // There should not be duplicate commands except for "-eq"
            // In this case, it is safe to simply print the error message and return false
            // TODO: Amend if incorrect
            System.out.println("Most commands (except for -eq) do not allow duplicates. Please try again.");
            return false;
        }

        return userInputIsValid;
    }

    /**
     * Checks whether the list of commands inside userInputs contains duplicates. Only "-eq" command is
     * allowed to be duplicates.
     * Precondition:
     * - userInputs contains valid input pairs (command, response)
     * @param acceptedCommands an array of Strings containing accepted commands
     * @param userInputs a list of strings containing user inputs
     * @return true if there are duplicates commands (except for "-eq"), false otherwise
     */
    private boolean isDuplicateCommandInUserInput(String[] acceptedCommands, List<String> userInputs) {
        ArrayList<String> listOfInputCommands = new ArrayList<>();
        for (int i = 0; i < userInputs.size(); i += 2) {
            listOfInputCommands.add(userInputs.get(i));
        }
        for (String command: acceptedCommands) {
            if (!command.equals("-eq") && listOfInputCommands.stream().filter(str -> str.equals(command))
                    .count() > 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the user argument of a given command.
     * @param command a given command
     * @param userInputs a list of Strings containing user inputs
     * @return the user argument corresponding to the command.
     */
    private String getCommandArgument(String command, List<String> userInputs){
        return userInputs.get(userInputs.indexOf(command) + 1);
    }

    /**
     * Checks whether the user input pair (command, response) is valid based on the first and the second
     * element of user input pair.
     * A pair of the user input is VALID if all the following is satisfied:
     * 1. the first element of the pair (which is a format of "-****") is in the array of accepted commands.
     * 2. the second element of the pair is not null (null implies one or more commands had missing responses)
     * 3. the first element of the pair is "-dim" and the second element of the pair (i.e., response) is a
     * positive integer (the dimension of a function can only be a positive dimension)
     * We expect there will be more checks to be done in the CLI level.
     * Thus, this method is open for extension.
     * @param acceptedCommands an array of String containing accepted commands
     * @param firstElementOfPair the first element (i.e., command) of the (command, response) pair
     * @param secondElementOfPair the second element (i.e., response) of the (command, response) pair
     * @return whether the user input is valid
     */
    private boolean isUserInputPairValid(String[] acceptedCommands, String firstElementOfPair,
                                         String secondElementOfPair) {
        boolean userInputPairIsValid = true;
        if (!Arrays.asList(acceptedCommands).contains(firstElementOfPair)) {
            System.out.println("\"" + firstElementOfPair + "\" is not a valid command. Please try again.");
            userInputPairIsValid = false;
        } else if (secondElementOfPair == null) {
            System.out.println("\"" + firstElementOfPair + "\" has no valid response. Please try again.");
            userInputPairIsValid = false;
        } else if (firstElementOfPair.equals("-dim") && !isPositiveInteger(secondElementOfPair)) {
            System.out.println("-dim needs to be followed by a positive integer. Please try again.");
            userInputPairIsValid = false;
        }
        return userInputPairIsValid;
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

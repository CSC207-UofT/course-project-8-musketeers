package Frontend;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandLineInterface {
    public static void main(String[] args) {
        CommandLineInterface cli = new CommandLineInterface();

        boolean userInputIsValid = true;
        String[] acceptedCommands = {"-eq", "-save", "-domain", "-dim", "-load", "-graph"};
        ArrayList<String> userInputs = new ArrayList<>(Arrays.asList(args));
        if (userInputs.size() % 2 != 0) {
            // There is an issue -- the (command, response) pair doesn't match up
            // Add null at the end for now, and find the issue
            userInputs.add(null);
        }
        for (int i = 0; i < userInputs.size(); i += 2) {
            String firstElementOfPair = userInputs.get(i).toLowerCase();
            String secondElementOfPair = userInputs.get(i + 1);
            if (!Arrays.asList(acceptedCommands).contains(firstElementOfPair)) {
                System.out.println("\"" + firstElementOfPair + "\" is not a valid command. Please try again.");
                userInputIsValid = false;
                break;
            } else if (secondElementOfPair == null) {
                System.out.println("\"" + firstElementOfPair + "\" has no valid response. Please try again.");
                userInputIsValid = false;
                break;
            } else if (firstElementOfPair.equals("-dim") && !cli.isPositiveInteger(secondElementOfPair)) {
                System.out.println("-dim needs to be followed by a positive integer. Please try again.");
                userInputIsValid = false;
                break;
            }
        }
        // At the point, the user input is at least a (structurally) valid input, with the correct pair of
        // (command, response).
        if (userInputIsValid) {
            System.out.println("Hooray!");
        }
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
}

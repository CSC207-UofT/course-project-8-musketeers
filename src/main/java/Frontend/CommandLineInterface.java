package Frontend;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandLineInterface {
    private static final String[] acceptedCommands = {"-eq", "-save", "-domain", "-2d", "-3d",
            "-load", "-graph"};

    public static void main(String[] args) {
        ArrayList<String> userInputs = new ArrayList<>(Arrays.asList(args));
        if (userInputs.size() % 2 != 0) {
            // There is an issue -- the (command, response) pair doesn't match up
            // Add null at the end for now, and find the issue
            userInputs.add(null);
            for (int i = 0; i < userInputs.size(); i += 2) {
                String firstElementOfPair = userInputs.get(i).toLowerCase();
                String secondElementOfPair = userInputs.get(i + 1);
                if (!Arrays.asList(acceptedCommands).contains(firstElementOfPair)) {
                    System.out.println(firstElementOfPair + " is not a valid command. Please try again.");
                    break;
                } else if (secondElementOfPair == null) {
                    System.out.println(firstElementOfPair + " has no valid response. Please try again.");
                    break;
                }
            }
        } else {
            for (int i = 0; i < userInputs.size(); i += 2) {
                String firstElementOfPair = userInputs.get(i).toLowerCase();
                String secondElementOfPair = userInputs.get(i + 1);
                if (!Arrays.asList(acceptedCommands).contains(firstElementOfPair)) {
                    System.out.println(firstElementOfPair + " is not a valid command. Please try again.");
                    break;
                }
            }
        }
    }
}

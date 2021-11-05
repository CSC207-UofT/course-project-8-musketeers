package Frontend;

import java.util.Arrays;
import java.util.Scanner;

public class CommandLineInterface {
    private static final Scanner s = new Scanner(System.in);

    public static void main(String[] args) {
        printLoadGraphOrQuit();

        String userInputExpectingLoadOrGraph = getValidUserInput(s.nextLine().toLowerCase(),
                new String[]{"load", "graph", "quit"});

        if (userInputExpectingLoadOrGraph.equals("load")) {
            System.out.println("Load graphs or do something else idk");
        } else if (userInputExpectingLoadOrGraph.equals("graph")) {
            print2D3DOrQuit();

            String userInputExpecting2DOr3D = getValidUserInput(s.nextLine().toLowerCase(),
                    new String[]{"2d", "3d", "quit"});

            if (userInputExpecting2DOr3D.equals("2d")) {
                printExplicitImplicitOrQuit();

                String userInputExpectingExplicitOrImplicit = getValidUserInput(s.nextLine().toLowerCase(),
                        new String[]{"explicit", "implicit", "quit"});

                if (userInputExpectingExplicitOrImplicit.equals("explicit")) {
                    System.out.println("Do something with explicit functions");
                    /*
                    TODO: Do the following for the explicit functions:
                        1. Ask for function name
                        2. Ask for number of variables
                        3. Ask for expression (using the function name provided by the user)
                        (e.g., type an expression for the function <function name>)
                        Tell the user to also put an optional restriction if they want, between the
                        brackets {}, and give an example
                        4. Find the first bracket { if present as a "restriction"
                        5. Separate the expression part and the restriction part
                        6. Print the two parts separately
                        7. Ask the user to either graph another one or save the current state of graphs
                        8. At any time of the execution, user can type "quit" to stop the program.
                        It will ask for a confirmation (Any unsaved graphs will be lost...)
                     */
                } else if (userInputExpectingExplicitOrImplicit.equals("implicit")) {
                    System.out.println("Do something with implicit functions");
                } else {
                    System.out.println("Thank you!");
                }
            } else if (userInputExpecting2DOr3D.equals("3d")) {
                printExplicitImplicitOrQuit();

                String userInputExpectingExplicitOrImplicit = getValidUserInput(s.nextLine().toLowerCase(),
                        new String[]{"explicit", "implicit", "quit"});

                if (userInputExpectingExplicitOrImplicit.equals("explicit")) {
                    System.out.println("Do something with explicit functions");
                } else if (userInputExpectingExplicitOrImplicit.equals("implicit")) {
                    System.out.println("Do something with implicit functions");
                } else {
                    System.out.println("Thank you!");
                }
            } else {
                System.out.println("Thank you!");
            }
        } else {
            System.out.println("Thank you!");
        }
    }

    /**
     * Print the print statements: explicit, implicit or quit.
     */
    private static void printExplicitImplicitOrQuit() {
        System.out.println("Type \"explicit\" to graph explicit functions, or");
        System.out.println("type \"implicit\" to graph implicit functions.");
        System.out.println("Type \"quit\" to stop the program at any time. ");
    }

    /**
     * Print the print statements: 2D, 3D or quit.
     */
    private static void print2D3DOrQuit() {
        System.out.println("Type \"2D\" to graph 2D graphs, or");
        System.out.println("type \"3D\" to graph 3D graphs.");
        System.out.println("Type \"quit\" to stop the program at any time. ");
    }

    /**
     * Checks whether the initial user input is a valid input (i.e., the userInput parameter is
     * in the array of Strings validInputs), and keeps waiting until the user types a valid input.
     * Representation invariant:
     *     - validInputs always contains "quit"
     * @param userInput The initial input from the user
     * @param validInputs The array of Strings containing only the valid inputs.
     * @return The valid user input
     */
    private static String getValidUserInput(String userInput, String[] validInputs) {
        while (!Arrays.asList(validInputs).contains(userInput)) {
            System.out.println("Invalid input. Please try again.");
            userInput = s.nextLine().toLowerCase();
        }
        return userInput;
    }

    /**
     * Print the initial print statements: load, graph or quit.
     */
    private static void printLoadGraphOrQuit() {
        System.out.println("Type \"load\" to load a saved graph, or");
        System.out.println("type \"graph\" to start graphing! ");
        System.out.println("Type \"quit\" to stop the program at any time. ");
    }
}

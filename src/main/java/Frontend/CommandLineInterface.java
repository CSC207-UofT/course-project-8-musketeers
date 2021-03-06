package Frontend;

import Backend.Axes;
import Backend.AxesUseCase;
import Backend.ExpressionReader;
import GUI.GLGUI;
import GUI.GUI;
import Graphics.Grapher;

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
     *
     * @param args An array of Strings containing the user inputs, split by a space " "
     */
    public static void main(String[] args) {
        CLIHelper cliHelper = new CLIHelper();
        List<String> userInputs = new ArrayList<>(Arrays.asList(args));

        // An array of strings containing accepted Commands. This is open to extension as other parts
        // of the code become available to be merged into this CLI.
        String[] acceptedCommands = {
                cliHelper.eqCommand,
                cliHelper.dimCommand,
                cliHelper.graphCommand,
                cliHelper.saveCommand,
                cliHelper.loadCommand,
                cliHelper.posCommand,
                cliHelper.domainCommand,
                cliHelper.interactiveCommand,
                cliHelper.nameCommand,
                cliHelper.sizeCommand
        };

        if (!cliHelper.checkValidInput(acceptedCommands, userInputs)) {
            return;
        }

        AxesUseCase auc = new AxesUseCase();
        Axes axes = auc.createAxes();

        if (userInputs.contains(cliHelper.loadCommand)) {
            axes = cliHelper.tryLoadingAxes(userInputs, axes, auc);
        }
        if (userInputs.contains(cliHelper.posCommand)) {
            cliHelper.trySettingOrigin(userInputs, axes, auc);
        }

        ExpressionReader er = new ExpressionReader(axes);
        Grapher grapher = new Grapher(axes);
        List<String[]> equationsAndDomains = cliHelper.findAllEquations(args);
        cliHelper.tryInterpretingInput(axes, auc, er, equationsAndDomains);
        int[] graphedImage = cliHelper.tryGraphingImage(userInputs, grapher);

        if (userInputs.contains(cliHelper.graphCommand)) {
            cliHelper.trySavingImage(graphedImage, userInputs);
        }

        if (userInputs.contains(cliHelper.interactiveCommand)) {
            GUI gui = new GLGUI(grapher, cliHelper.getCustomSize(userInputs));
            cliHelper.startGUI(userInputs, gui);
        }

        if (userInputs.contains(cliHelper.saveCommand)) {
            cliHelper.trySavingAxes(userInputs, axes, auc);
        }
    }
}
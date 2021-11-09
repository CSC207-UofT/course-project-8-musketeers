package Backend;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import Graphics.ImplicitGrapher;

import static Graphics.ImageTest.writeImage;

// TODO: for now Backend.ExpressionReader.read assumes that there are spaces between the necessary
// characters. Also Backend.ExpressionCreator assumes that all the brackets are appropriately
// placed. Might want to add a check for that

public class ExpressionReader {
    private final Constants constants = new Constants();

    /** Converts a string representation of an expression into an instance of Backend.Expression
     * @param expression The string representation of the expression to be converted
     * @return Backend.Expression object for the string provided
     */
    // e.g. x ^ 2 + 5 -> ["x", "^", "2", "+", "5"]
    // e.g. (2) + 3 or 3 + (2) -> ["(", "2", ")", "+", "3"]
    // e.g. cos(x) -> ["cos", "(", "x", ")"]
    public Expression read(String expression){
        ExpressionCreator ec = new ExpressionCreator();

        List<String> expressionList = expressionParser(expression);
        int equalsIndex = expressionList.indexOf("=");

        if (equalsIndex > 0) {
            return ec.create(expressionList.subList(equalsIndex + 1, expressionList.size()));
        }
        else{
            return ec.create(expressionList);
        }
    }




    /** expressionParser takes an input string and parses it to form a list. If given valid input, it will form
     * a list that gives us the corresponding valid expression tree.
     * If given invalid input, it will return an invalid list.
     * As minus and plus may also be unary operators as well as binary ones, if there is any instance of a unary usage
     * of these operators, expressionParser interprets them as binary similar to the following example:
     * -x is interpreted to be ["-1", "*", "x"]
     *
     * @param expression This is the expression the user has input.
     * @return A list which we can create an expression tree from.
     */
    public List<String> expressionParser(String expression) {
        List<String> parsed = new ArrayList<>(List.of());
        StringBuilder section = new StringBuilder(); // section will be storing any series of characters which are not
        // operators.
        // We will loop over expression to interpret it and add to parsed.
        for (int character = 0; character < expression.length(); character++) {
            String letter = String.valueOf(expression.charAt(character));
            //If its not alphanumeric, its a special character, in which case we add whatever section refers to into parsed
            //and add the special character aftwerwards.
            if (letter.matches("^.*[^a-zA-Z0-9 ].*$")){
                //we want to be sure that section does not refer to an empty string.
                if (!section.toString().equals("")) {
                    parsed.add(section.toString());
                }
                parsed.add(letter);
                section = new StringBuilder();

            }
            // If its a space, then we add whatever section refers to to parsed to ensure we dont mix "section" with the next
            //character.
            else if (letter.equals(" ")) {
                if (!section.toString().equals("")) {
                    parsed.add(section.toString());
                }
                section = new StringBuilder();
            }
            else {
                //If its neither of those cases. then it must be alphanumeric and so we append "section".
                section.append(letter);
            }
        }


        //As we finished looping over expression, we may have that section refers to the last bit of the input.
        // We add it to parsed if its nonempty.

        if (!section.toString().equals("")) {
            parsed.add(section.toString());
        }
        //We may have valid input from the user, but it may not be interpreted correctly as is.
        //We call this function to modify our parsed list to give a form that can be transformed into a valid AST.
        fixparsedlist(parsed);
        return parsed;

    }

    /**Edit parsed to interpret edge cases where unary operators are used correctly and give us a
     * list which can be converted into the correct AST that represents the user's input.
     *
     * @param parsed The list we have parsed in the first pass through expression parser.
     */

    public void fixparsedlist (List<String> parsed) {
        //
        handleoperators(parsed);
        handlesign(parsed);
    }

    /** We want to go through the list and replace any sequences of  "+" and "-" with the resulting sign.
     *
     * @param parsed The list we have parsed in the first pass through expression parser.
     */
    public void handleoperators(List<String> parsed) {
        for (int i = 0; i < parsed.size() - 1; i++) {
            if (parsed.get(i).equals("-") || parsed.get(i).equals("+")) {
                removedifferent(i, parsed);
            }

        }
    }

    /** Replace 2
     *
     * @param index the current index at which we have a "+" or "-"
     * @param parsed The parsed list that we are editing.
     */
    public void removedifferent(int index, List<String> parsed) {
        if (parsed.get(index).equals(parsed.get(index + 1))) {
            parsed.remove(index);
            parsed.remove(index);
            parsed.add(index,"+");
        }
        else if (parsed.get(index+1).equals("-") || parsed.get(index + 1).equals("+")) {
            parsed.remove(index);
            parsed.remove(index);
            parsed.add(index,"-");


        }
    }

    public void handlesign(List<String> parsed) {
        for (int i = 0; i < parsed.size(); i++) {
            String current = parsed.get(i);
            if (i ==0 && (current.equals("-") || current.equals("+"))) {
                parsed.remove(0);
                parsed.add(0, "*");
                if (current.equals("-")) {
                    parsed.add(0, "-1");
                }
                else {
                    parsed.add(0, "-1");
                }
            }
            else if (i > 0) {
                replaceunaryoperatorswithone(i, parsed);
            }
        }
    }

    private void replaceunaryoperatorswithone(int i, List<String> parsed) {
        if (parsed.get(i).equals("-")) {

            if (parsed.get(i-1).equals("(") || (constants.getOperators().contains(parsed.get(i-1)) &&
                    !parsed.get(i-1).equals("/") && !parsed.get(i-1).equals("^"))) {
                parsed.remove(i);
                parsed.add(i,"*");
                parsed.add(i,"-1");
            }
        }
        else if (parsed.get(i).equals("+")) {
            if (parsed.get(i-1).equals("(") || (constants.getOperators().contains(parsed.get(i-1)) &&
                    (!parsed.get(i-1).equals("/") && !parsed.get(i-1).equals("^")))) {
                parsed.remove(i);
                parsed.add(i,"*");
                parsed.add(i,"1");
            }
        }
    }



    // Try "( x ^ 2 + y ^ 2 - 1 ) ^ 3 - x ^ 2 * y ^ 3"!
    // mandel ( (x^2 - y^2 ) / (x^2 + y^2)^2 , (0 - 2 * x * y) / (x^2 + y^2)^2 )
    public static void main(String[] args) throws Exception {
        Axes axes = new Axes();
        int size = 512;
        int[] mainPixels = new int[size*size];
        int[] dims1 = {size,size};

        ExpressionReader er = new ExpressionReader();

        System.out.println("Please ensure that each 'unit' of information in" +
                " the input is spaced out:");
        System.out.println("e.g. \"cos ( x + y ) - sin ( x * y )\" or \"( x + y ) ^ 2 - 3\"");
        String test = "y - sqrt(x)";

        Expression func = er.read(test);
        axes.addExpression(func);
        axes.setScale(4f);
        float[] pos = {0.f, 0.f};
        axes.setOrigin(pos);

        ImplicitGrapher.graphImplicit(mainPixels, dims1[0], dims1[1], axes, true);

        writeImage(mainPixels, dims1[0], dims1[1], "sampleOutCool.png");
        System.out.println("...Done!");
    }

}

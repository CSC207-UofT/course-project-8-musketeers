package Backend;


import java.util.*;


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
            if (letter.matches("^.*[^a-zA-Z0-9. ].*$")){
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
        fixParsedlist(parsed);
        return parsed;

    }

    /**Edit parsed to interpret edge cases where unary operators are used correctly and give us a
     * list which can be converted into the correct AST that represents the user's input.
     *
     * @param parsed The list we have parsed in the first pass through expression parser.
     */

    private void fixParsedlist (List<String> parsed) {
        //
        fixLogicalOperators(parsed);
        handleOperators(parsed);
        handleSign(parsed);

    }

    /** We want to go through the list and replace any sequences of  "+" and "-" with the resulting sign.
     *
     * @param parsed The list we have parsed in the first pass through expression parser.
     */
    private void handleOperators(List<String> parsed) {

        int size = parsed.size();
        for (int i = 0; i < size - 1; i++) {
            if (parsed.get(i).equals("-") || parsed.get(i).equals("+")) {
                removeDifferent(i, parsed);
            }
            if (size != parsed.size()) {
                size = parsed.size();
                i--;
            }
        }
    }

    /** If character at next index is "-" or "+", remove both operators and return the correct sign.
     *
     * @param index the current index at which we have a "+" or "-"
     * @param parsed The parsed list that we are editing.
     */
    private void removeDifferent(int index, List<String> parsed) {
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

    /** Interpret "+" and "-" used in the unary context as "1*" and "-1*" respectively.
     *
     * @param parsed The parsed list we are editing.
     */
    private void handleSign(List<String> parsed) {
        for (int i = 0; i < parsed.size(); i++) {
            String current = parsed.get(i);
            //This function is called after handleOperatorshandleOperators, so there are no consecutive unary operators, ensuring it
            // makes sense.
            //Special case i = 0. We immediately know its a unary use of "+" and "-".
            if (i ==0 && (current.equals("-") || current.equals("+"))) {
                interpetOperator(i, current,parsed);
            }
            else if (i > 0 && (current.equals("-") || current.equals("+"))) {     //In this case, its more tricky to determine a unary usage of an operator.
                replaceUnaryOperatorsWithOne(i, parsed);
            }
        }
    }

    /**
     *
     * @param i current index in the parsed list.
     * @param parsed The parsed list we are editing to interpret "-" and "+" in a unary context.
     */
    private void replaceUnaryOperatorsWithOne(int i, List<String> parsed) {
        // specialcharacters will contain all characters where, if "-" or "+" appear after, they are used in
        // unary context.
        List<String> specialcharacters = constants.getOperators();
        specialcharacters.addAll(constants.getComparators());
        specialcharacters.addAll(constants.getLogicalOperators());
        specialcharacters.remove("/"); // The case where we have ??/-??" in the code is bad habit. We are enforcing
                                          // rule that we are not responsible for the interpretation of it. So
                                            // we remove "/"/
        specialcharacters.remove("^"); // Same for "??^-??"
        specialcharacters.add("(");
        // If the previous element of the parsed list is special, we interpet the operator as unary.
        if (specialcharacters.contains(parsed.get(i-1))) {
            interpetOperator(i, parsed.get(i), parsed);
        }

    }


    /**
     *
     * @param i current index where we have "-" or "+"
     * @param s Character at parsed[i]
     * @param parsed The parsed list wher we are interpretting unary operators.
     */
    private void interpetOperator(int i, String s, List<String> parsed) {
        parsed.remove(i);
        parsed.add(i, "*");
        if (s.equals("-")) {
            parsed.add(i,"-1");

        }
        else if (s.equals("+")) {
            parsed.add(i, "1");
        }
    }

    /** There are some logical operators which consist of 2 other successive logical operators. The first pass through
     * expressionparser interprets them seperately. This corrects this misinterpretation.
     *
     * @param parsed Parsed list we are editing to be interpreted as a correct expression.
     */
    private void fixLogicalOperators(List<String> parsed) {
        int size = parsed.size();
        for (int i = 0; i < size - 1; i++) {
            StringBuilder current = new StringBuilder(parsed.get(i));
            if ((current.toString().equals("<") || current.toString().equals(">")) && parsed.get(i+1).equals("=")) {
                concatenateOperators(parsed, current, i);
            }
        size = parsed.size();
        }
    }

    private void concatenateOperators(List<String> parsed, StringBuilder current, int i) {
        parsed.remove(i);
        parsed.remove(i);
        current.append("=");
        parsed.add(i, current.toString());
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
        String par = "hi(-+x)";
        System.out.println(er.expressionParser(par));
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

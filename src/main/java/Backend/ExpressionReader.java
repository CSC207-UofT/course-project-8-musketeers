package Backend;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


import Graphics.ImplicitGrapherTest;

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
     * As minus sign may also be a unary operator as well as a binary one, if there is any instance of a unary usage
     * of the minus sign, expressionParser interprets it as a binary one similar to the following example:
     * -x is interpreted to be ["(", "0", "-", "x", ")"]
     *
     * @param expression This is the expression the user has input.
     * @return A list which we can create an expression tree from.
     */
    public List<String> expressionParser(String expression) {
    List<String> parsed = new ArrayList<>(List.of());
    StringBuilder section = new StringBuilder(); // section will be storing any series of characters which are not
                                                // operators.

    for (int character = 0; character < expression.length(); character++) {
         String letter = String.valueOf(expression.charAt(character));

         //unlike other cases, if the minus sign is at the beginning, we dont need to add brackets to ensure
        // we are not changing the order of operations.

         if (letter.equals("-") && character == 0) {
             parsed.add("0");
             parsed.add(letter);
         }
         // We want to change the minus sign into a binary one if it is used in a unary context.
         // If there isnt a character after the minus sign, the expression is invalid and so it will be rejected in
         // ExpressionCreator.
         // We must also check if the minus sign is preceded by an open bracket, operator, "=" or "," as those are the
         // conditions here in which the minus sign is interpreted as unary.
        // if all conditions hold interpret "next letter" as  (0 - "next letter" ).

         else if (letter.equals("-") && expression.length() > character +1 &&
                 (constants.getOperators().contains(String.valueOf(expression.charAt(character- 1))) ||
                         String.valueOf(expression.charAt(character- 1)).equals("(") ||
                         String.valueOf(expression.charAt(character- 1)).equals(",") ||
                 String.valueOf(expression.charAt(character- 1)).equals("="))) {
             // no need to add an open brack if preceded by an open bracket
             if (!String.valueOf(expression.charAt(character- 1)).equals("(")) {
                 parsed.add("(");
             }
             parsed.add(("0"));
             parsed.add(letter);
             parsed.add(String.valueOf(expression.charAt(character+1)));
             parsed.add(")");
             character++;

         }
       else if (constants.getOperators().contains(letter) || letter.equals("(") ||
                 letter.equals(")") || letter.equals(",") || letter.equals("=")) {
            //we want to be sure that section does not refer to an empty string.
           if (!section.toString().equals("")) {
               parsed.add(section.toString());
           }

           parsed.add(letter);
           section = new StringBuilder();

        }
       else if (letter.equals(" ")) {
             if (!section.toString().equals("")) {
                 parsed.add(section.toString());
             }
           section = new StringBuilder();

         }

       else {section.append(letter);


         }
        }
        if (!section.toString().equals("")) {
            parsed.add(section.toString());

        }

    return parsed;

    }


    // Try "( x ^ 2 + y ^ 2 - 1 ) ^ 3 - x ^ 2 * y ^ 3"!
    public static void main(String[] args) throws Exception {
        Axes axes = new Axes();
        int size = 512;
        int[] mainPixels = new int[size*size];
        int[] dims1 = {size,size};

        ExpressionReader er = new ExpressionReader();

        System.out.println("Please ensure that each 'unit' of information in" +
                " the input is spaced out:");
        System.out.println("e.g. \"cos ( x + y ) - sin ( x * y )\" or \"( x + y ) ^ 2 - 3\"");
        String test = "mandel ( ( x ^ 2 - y ^ 2 ) / ( x ^ 2 + y ^ 2 ) ^ 2 , ( 0 - 2 * x * y ) / ( x ^ 2 + y ^ 2 ) ^ 2 )";
        Expression func = er.read(test);
        axes.addExpression(func);

        ImplicitGrapherTest.graphImplicit(mainPixels, dims1[0], dims1[1], func, 0.01f, 0.f, 0.f, false);

        writeImage(mainPixels, dims1[0], dims1[1], "sampleOutCool.png");
        System.out.println("...Done!");
    }

}

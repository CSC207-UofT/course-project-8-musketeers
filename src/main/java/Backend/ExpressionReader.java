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

    /** Converts a string representation of an expression into an instance of Backend.Expression
     * @param expression The string representation of the expression to be converted
     * @return Backend.Expression object for the string provided
     */
    // e.g. x ^ 2 + 5 -> ["x", "^", "2", "+", "5"]
    // e.g. (2) + 3 or 3 + (2) -> ["(", "2", ")", "+", "3"]
    // e.g. cos(x) -> ["cos", "(", "x", ")"]
    public Expression read(String expression){
        ExpressionCreator ec = new ExpressionCreator();

        List<String> expressionList = List.of(expression.split(" "));
        int equalsIndex = expressionList.indexOf("=");

        if (equalsIndex > 0) {
            return ec.create(expressionList.subList(equalsIndex + 1, expressionList.size()));
        }
        else{
            return ec.create(expressionList);
        }
    }


    // Try "( x ^ 2 + y ^ 2 - 1 ) ^ 3 - x ^ 2 * y ^ 3"!
    public static void main(String[] args) throws Exception {
        Axes axes = new Axes();
        int size = 512;
        int[] mainPixels = new int[size*size];
        int[] dims1 = {size,size};

        ExpressionReader er = new ExpressionReader();

        System.out.println("Please ensure that each 'unit' of information in" +
                "the input is spaced out:");
        System.out.println("e.g. \"cos ( x + y ) - sin ( x * y )\" or \"( x + y ) ^ 2 - 3\"");
//        String test = args[0];
        String test = "4 - 5 + 1";
        Expression func = er.read(test);
        axes.addExpression(func);

        Map<String, Double> varMap = new HashMap<>();

        System.out.println(test + " = " + func.evaluate(varMap));

//        ImplicitGrapherTest.graphImplicit(mainPixels, dims1[0], dims1[1], func, 0.1f, 0.f, 0.f, true);
//        writeImage(mainPixels, dims1[0], dims1[1], "sampleOutCool.png");
//        System.out.println("...Done!");
    }

}

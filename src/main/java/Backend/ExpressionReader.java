package Backend;

import java.util.HashMap;
import java.util.List;

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


    public static void main(String[] args) {

        Axes axes = new Axes();

        ExpressionReader er = new ExpressionReader();
        String test = "cos ( x ^ 2 )";

        Expression func = er.read(test);
        axes.addExpression(func);

        HashMap<String, Double> varMap = new HashMap<>();
        varMap.put("x", 0.0);
        varMap.put("y", 2.0);

        System.out.println(func.evaluate(varMap));

    }

}

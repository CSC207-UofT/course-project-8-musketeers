import java.util.HashMap;
import java.util.List;

// TODO: for now ExpressionReader.read assumes that there are spaces between the necessary
// characters. Also ExpressionCreator assumes that all the brackets are appropriately
// placed. Might want to add a check for that

public class ExpressionReader {

    /** Converts a string representation of an expression into an instance of Expression
     * @param expression The string representation of the expression to be converted
     * @return Expression object for the string provided
     */
    // e.g. x ^ 2 + 5 -> ["x", "^", "2", "+", "5"]
    // e.g. (2) + 3 or 3 + (2) -> ["(", "2", ")", "+", "3"]
    // e.g. cos(x) -> ["cos", "(", "x", ")"]
    public Expression read(String expression){
        ExpressionCreator ec = new ExpressionCreator();

        List<String> expressionList = List.of(expression.split(" "));
        int equalsIndex = expressionList.indexOf("=");
        return ec.create(expressionList.subList(equalsIndex + 1, expressionList.size()));
    }


    public static void main(String[] args) {

        Axes axes = new Axes();

        ExpressionReader er = new ExpressionReader();
        String test = "f(x) = x ^ 2 * ( 4 + y )";

        Expression func = er.read(test);
        axes.addExpression(func);

        HashMap<String, Double> varMap = new HashMap<>();
        varMap.put("x", 1.0);
        varMap.put("y", 1.0);

        System.out.println(func.evaluate(varMap));


    }

}

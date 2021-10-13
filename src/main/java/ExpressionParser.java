import java.util.*;

public class ExpressionParser {


    /**
     * @param expression the actual string representation of the expression e.g. x ^ 2 + 1
     * @return an Expression object instance for the input string
     */
    public Expression parse(String expression){

        List<String> splitExpression = List.of(expression.split(" "));

        Expression headExpression = operatorNodeCreator(splitExpression);

        return headExpression;
    }

    // TODO: Deal with parentheses
    // Recursively builds the expression
    private Expression operatorNodeCreator(List<String> arguments){
        Expression returnExpression = null;

        // TODO: Move these constants to somewhere reasonable
        Set<String> VARIABLES = Set.of("x", "y", "z");
        ArrayList<String> OPERATORS = new ArrayList(List.of(new String[]{"^", "/", "*", "+", "-"}));
        ArrayList<String> FUNCTIONS = new ArrayList(List.of(new String[]{"cos"}));

        // Base case for the recursion
        // One term means it's a variable, number or a function that takes in some input
        if (arguments.size() == 1){
            if (VARIABLES.contains(arguments.get(0))){
                returnExpression = new VariableExpression(arguments.get(0));
            } else if (arguments.get(0).contains("cos")){
                // clearly the above only works for "cos"
                // TODO: generalise the above for other functions
                String cosInput = arguments.get(0).substring(4, 5);
                returnExpression = new Cos(operatorNodeCreator(Collections.singletonList(cosInput)));
            }
            // Assuming that if we dont have a variable or function, we just have a number
            else{
                returnExpression = new NumberExpression(arguments.get(0));
            }
        }
        // This is the recursive part
        else {
            for (String op : OPERATORS) {
                // Decide what the operators in the expressions are
                int opIndex = arguments.indexOf(op);
                if (opIndex > 0) {
                    Expression lExpression = operatorNodeCreator(arguments.subList(0, opIndex));
                    Expression rExpression = operatorNodeCreator(arguments.subList(opIndex + 1, arguments.size()));

                    returnExpression = new OperatorExpression(op, lExpression, rExpression);

                }
            }
        }

        return returnExpression;

    }


    public static void main(String[] args) {
        String expr = "x ^ 2 + cos(0) - y";

        ExpressionParser expParser = new ExpressionParser();

        Expression exp = expParser.parse(expr);

        HashMap<String, Double> varMap = new HashMap();
        varMap.put("x", 1.0);
        varMap.put("y", 5.0);

        System.out.println(expr + " = " + exp.evaluate(varMap));
    }

}

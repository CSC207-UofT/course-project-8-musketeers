package Backend;

import java.util.HashMap;
import java.util.Map;

/**
 * Used for user defined functions, e.g. when the input is of the form f(x) = ...
 * This needs to be distinct from builtin functions as user defined necessarily
 * store an Expression that builtin functions don't need to store
 */
public class CustomFunctionExpression extends FunctionExpression{

    // the variables used in the function
    // e.g. f(x) = ... would have ["x"]
    // while f(x, y) = ... would have ["x", "y"]
    private final String[] variables;

    // the function stored which tells us how to evaluate this function
    // e.g. f(x) = x^2 would store the Expression corresponding to x^2
    private final Expression function;

    // It is necessary that length of inputs = length of variables
    public CustomFunctionExpression(String funcName, Expression[] inputs,
                                    Expression function, String[] variables){
        super(funcName, inputs);
        this.function = function;
        this.variables = variables;
    }

    @Override
    public double evaluate(Map<String, Double> arguments) {
        Map<String, Double> varMap = new HashMap<>();
        for (int i = 0; i < variables.length; i++){
            varMap.put(variables[i], getInputs()[i].evaluate(arguments));
        }
        return function.evaluate(varMap);
    }
}

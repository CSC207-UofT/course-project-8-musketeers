package Backend.Expressions;

import java.util.HashMap;
import java.util.Map;

/**
 * Used for user defined functions, e.g. when the input is of the form f(x) = ...
 * This needs to be distinct from builtin functions as user defined necessarily
 * store an Expression that builtin functions don't need to store
 */
public class CustomFunctionExpression extends FunctionExpression {

    // the variables used in the function
    // e.g. f(x) = ... would have ["x"]
    // while f(x, y) = ... would have ["x", "y"]
    private final String[] variables;

    // the function stored which tells us how to evaluate this function
    // e.g. f(x) = x^2 would store the Expression corresponding to x^2
    private final RealValuedExpression function;

    // It is necessary that length of inputs = length of variables
    public CustomFunctionExpression(String funcName, RealValuedExpression[] inputs,
                                    RealValuedExpression function, String[] variables){
        super(funcName, inputs);
        this.function = function;
        this.variables = variables;
    }

    public CustomFunctionExpression(String funcName, RealValuedExpression[] inputs,
                                    RealValuedExpression function, String[] variables, ComparatorExpression domain){
        super(funcName, inputs, domain);
        this.function = function;
        this.variables = variables;
    }

    @Override
    public Float evaluate(Map<String, Float> arguments) {
        Map<String, Float> varMap = new HashMap<>();
        for (int i = 0; i < variables.length; i++){
            // If we have f(2x) for example, we must evaluate 2x first.
            // This is what this for loop is for
            RealValuedExpression exp = getInputs()[i];

            if (exp instanceof FunctionExpression){
                ComparatorExpression expDomain = ((FunctionExpression) exp).getDomain();
                if (!expDomain.evaluate(arguments)){
                    // means the values are out of the domain for at least one of inputs
                    return Float.NaN;
                }
            }

            varMap.put(variables[i], getInputs()[i].evaluate(arguments));
        }

        // we check that the final input is in the domain of our function
        // For the example, above, we check that 2x is in the domain of f
        if (!getDomain().evaluate(varMap)){
            return Float.NaN;
        }
        return function.evaluate(varMap);
    }
}

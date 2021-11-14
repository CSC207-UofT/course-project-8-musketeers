package Backend.Expressions;

import java.util.HashMap;
import java.util.Map;

/**
 * Used for user defined functions, e.g. when the input is of the form f(x) = ...
 * This needs to be distinct from builtin functions as user defined necessarily
 * store an Expression that builtin functions don't need to store
 */
public class CustomFunctionExpression extends FunctionExpression {

    // the function stored which tells us how to evaluate this function
    // e.g. f(x) = x^2 would store the Expression corresponding to x^2
    private final RealValuedExpression function;


    /**
     * @param funcName String representing the name of a function
     * @param variables Array of strings representing the variables the function is in terms of
     * @param inputs Array of RealValuedExpressions representing the inputs to a function, in most cases these are variables
     * @param function A RealValued Expression representing the function itself
     */
    public CustomFunctionExpression(String funcName, String[] variables,
                                    RealValuedExpression[] inputs, RealValuedExpression function){
        super(funcName, variables);
        this.function = function;
        setInputs(inputs);
    }

    public CustomFunctionExpression(String funcName, String[] variables,
                                    RealValuedExpression[] inputs, RealValuedExpression function,
                                    BooleanValuedExpression domain){
        this(funcName, variables, inputs, function);
        setDomain(domain);
    }

    @Override
    public Float evaluate(Map<String, Float> arguments) {
        Map<String, Float> varMap = new HashMap<>();
        String[] variables = getVariables();
        for (int i = 0; i < variables.length; i++){
            // If we have f(2x) for example, we must evaluate 2x first.
            // This is what this for loop is for
            RealValuedExpression exp = getInputs()[i];

            if (exp instanceof FunctionExpression){
                BooleanValuedExpression expDomain = ((FunctionExpression) exp).getDomain();
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

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
     * @param function A RealValued Expression representing the function itself
     */

    public CustomFunctionExpression(String funcName, String[] variables, RealValuedExpression function){
        super(funcName, variables);
        this.function = function;
    }

    // These constructors will be useful when we fully implement custom functions
    public CustomFunctionExpression(String funcName, String[] variables, RealValuedExpression function,
                                    ComparatorExpression domain){
        this(funcName, variables, function);
        setDomain(domain);
    }

    public CustomFunctionExpression(String funcName, String[] variables,
                                    RealValuedExpression[] inputs, RealValuedExpression function){
        super(funcName, variables);
        this.function = function;
        setInputs(inputs);
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

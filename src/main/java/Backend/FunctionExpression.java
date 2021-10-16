package Backend;

import java.util.Map;

/**
 * Backend.FunctionExpression for explicit functions that the user defines
 * Essentially a way to associate user-defined functions with names
 * and allow them to play nicely with other Expressions
 */
public class FunctionExpression extends Expression {

    // if f(x) = x^2 + 5, then function would store the expression 'x^2 + 5'
    private final Expression function;

    public FunctionExpression(String funcName, Expression function){
        super(funcName);
        this.function = function;
    }

    @Override
    public double evaluate(Map<String, Double> arguments) {
        return function.evaluate(arguments);
    }
}

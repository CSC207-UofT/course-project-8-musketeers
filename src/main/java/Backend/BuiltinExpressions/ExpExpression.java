package Backend.BuiltinExpressions;

import Backend.Expression;
import Backend.FunctionExpression;

import java.util.Map;

public class ExpExpression extends FunctionExpression {
    // Only one input for exp of course
    // But the input can be an expression of any kind
    public ExpExpression(String[] variables){
        super("exp", variables);
    }

    @Override
    public float evaluate(Map<String, Float> arguments) {
        return (float) Math.exp(getInputs()[0].evaluate(arguments));
    }
}

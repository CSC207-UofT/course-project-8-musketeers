package Backend.BuiltinExpressions;

import Backend.FunctionExpression;

import java.util.Map;

public class ExpExpression extends FunctionExpression {
    // Only one input for exp of course
    public ExpExpression(String[] variables){
        super("exp", variables);
    }

    @Override
    public float evaluate(Map<String, Float> arguments) {
        return (float) Math.exp(getInputs()[0].evaluate(arguments));
    }
}

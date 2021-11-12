package Backend.BuiltinExpressions;

import Backend.Expression;
import Backend.FunctionExpression;

import java.util.Map;

public class TanExpression extends FunctionExpression {

    // Only one input for cos of course
    // But the input can be an expression of any kind
    public TanExpression(String[] variables){
        super("tan", variables);
    }

    @Override
    public float evaluate(Map<String, Float> arguments) {
        return (float) Math.tan(getInputs()[0].evaluate(arguments));
    }
}

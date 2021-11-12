package Backend.BuiltinExpressions;

import Backend.Expression;
import Backend.FunctionExpression;

import java.util.Map;

public class CosExpression extends FunctionExpression {

    // Only one input for cos of course
    // But the input can be an expression of any kind
    public CosExpression(String[] inputs){
        super("cos", inputs);
    }

    @Override
    public float evaluate(Map<String, Float> arguments) {
        return (float) Math.cos(getInputs()[0].evaluate(arguments));
    }
}

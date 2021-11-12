package Backend.BuiltinExpressions;

import Backend.Expression;
import Backend.FunctionExpression;

import java.util.Map;

public class SqrtExpression extends FunctionExpression {

    // Only one input for cos of course
    // But the input can be an expression of any kind
    public SqrtExpression(String[] variables){
        super("sqrt", variables);
    }

    @Override
    public float evaluate(Map<String, Float> arguments) {
        return (float) Math.sqrt(getInputs()[0].evaluate(arguments));
    }
}

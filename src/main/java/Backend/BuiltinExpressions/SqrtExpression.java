package Backend.BuiltinExpressions;

import Backend.Expression;
import Backend.FunctionExpression;

import java.util.Map;

public class SqrtExpression extends FunctionExpression {

    // Only one input for cos of course
    // But the input can be an expression of any kind
    public SqrtExpression(Expression[] inputs){
        super("sqrt", inputs);
    }

    @Override
    public double evaluate(Map<String, Double> arguments) {
        return Math.sqrt(getInputs()[0].evaluate(arguments));
    }
}

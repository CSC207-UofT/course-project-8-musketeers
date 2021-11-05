package Backend.BuiltinExpressions;

import Backend.Expression;
import Backend.FunctionExpression;

import java.util.Map;

public class SinExpression extends FunctionExpression {

    // Only one input for cos of course
    // But the input can be an expression of any kind
    public SinExpression(Expression[] inputs){
        super("sin", inputs);
    }

    @Override
    public double evaluate(Map<String, Double> arguments) {
        return Math.sin(getInputs()[0].evaluate(arguments));
    }
}

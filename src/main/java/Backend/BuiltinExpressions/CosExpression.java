package Backend.BuiltinExpressions;

import Backend.Expression;
import Backend.FunctionExpression;

import java.util.Map;

public class CosExpression extends FunctionExpression {

    // Only one input for cos of course
    // But the input can be an expression of any kind
    public CosExpression(Expression[] inputs){
        super("cos", inputs);
    }

    @Override
    public double evaluate(Map<String, Double> arguments) {
        return Math.cos(getInputs()[0].evaluate(arguments));
    }
}

package Backend.Expressions.BuiltInFunctions;

import Backend.Expressions.FunctionExpression;

import java.util.Map;

public class SquareRoot extends FunctionExpression {
    // Only one input for cos of course
    // But the input can be an expression of any kind
    public SquareRoot(String[] variables){
        super("sqrt", variables);
    }

    @Override
    public Float evaluate(Map<String, Float> arguments) {
        return (float) Math.sqrt(getInputs()[0].evaluate(arguments));
    }
}

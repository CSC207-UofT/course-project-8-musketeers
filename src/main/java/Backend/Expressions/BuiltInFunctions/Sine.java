package Backend.Expressions.BuiltInFunctions;

import Backend.Expressions.FunctionExpression;

import java.util.Map;

public class Sine extends FunctionExpression {
    // Only one input for cos of course
    // But the input can be an expression of any kind
    public Sine(String[] variables){
        super("sin", variables);
    }

    @Override
    public Float evaluate(Map<String, Float> arguments) {
        return (float) Math.sin(getInputs()[0].evaluate(arguments));
    }
}

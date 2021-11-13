package Backend.Expressions.BuiltInFunctions;

import Backend.Expressions.FunctionExpression;
import Backend.Expressions.RealValuedExpression;

import java.util.Map;

public class Cosine extends FunctionExpression { // TODO: Merge "cos", "sin", "tan"... (the similarly evaluated ones) to "SingleVariableFunctionExpression" or don't do that but created that as an abstract class anyway.
    // Only one input for cos of course
    // But the input can be an expression of any kind
    public Cosine(RealValuedExpression[] inputs){
        super("cos", inputs);

    }

    @Override
    public Float evaluate(Map<String, Float> arguments) {
        return (float) Math.cos(getInputs()[0].evaluate(arguments));
    }
}

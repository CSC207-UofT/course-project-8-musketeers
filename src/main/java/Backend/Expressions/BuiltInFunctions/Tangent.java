package Backend.Expressions.BuiltInFunctions;

import Backend.Expressions.Expression;
import Backend.Expressions.FunctionExpression;
import Backend.Expressions.RealValuedExpression;

import java.util.Map;

public class Tangent extends FunctionExpression {

    // Only one input for cos of course
    // But the input can be an expression of any kind
    public Tangent(RealValuedExpression[] inputs){
        super("tan", inputs);
    }

    @Override
    public Float evaluate(Map<String, Float> arguments) {
        return (float) Math.tan(getInputs()[0].evaluate(arguments));
    }
}

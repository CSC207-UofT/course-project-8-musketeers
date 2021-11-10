package Backend.Expressions;

import java.util.Map;

// TODO: Handle inputs like pi or do we? Even so, let's have it as "\pi" in the LaTex style, and as SpecialValue in CONSTANTS.

// Backend.Expressions.Expression for constants
public class NumberExpression extends RealValuedExpression {

    public NumberExpression(String num){
        super(num);
    }

    // Evaluating a number just means returning itself
    @Override
    public Float evaluate(Map<String, Float> arguments) {
        return Float.parseFloat(getItem());
    }
}

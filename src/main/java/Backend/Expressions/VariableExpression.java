package Backend.Expressions;

import Backend.Expressions.RealValuedExpression;

import java.util.Map;

public class VariableExpression extends RealValuedExpression { // TODO: Do we want to have boolean-valued variables?

    // Variables can only have names x, y, z
    public VariableExpression(String varName){
        super(varName);
    }

    // Evaluating a variable is simply using the map to figure out
    // what value has been assigned to it
    @Override
    public Float evaluate(Map<String, Float> arguments) {
        return arguments.get(getItem());
    }
}

package Backend;

import java.util.Map;

public class VariableExpression extends Expression {

    // Variables can only have names x, y, z
    public VariableExpression(String varName){
        super(varName);
    }

    // Evaluating a variable is simply using the map to figure out
    // what value has been assigned to it
    @Override
    public float evaluate(Map<String, Float> arguments) {
        return arguments.get(getItem());
    }
}

package Backend;

import java.util.Map;

// TODO: Handle inputs like pi

// Backend.Expression for constants
public class NumberExpression extends Expression {

    public NumberExpression(String num){
        super(num);
    }

    // Evaluating a number just means returning itself
    @Override
    public double evaluate(Map<String, Double> arguments) {

        try {
            return Double.parseDouble(getItem());
        }
        catch (Exception NumberFormatException){
            throw new NumberFormatException("Number expected but not found");
        }

    }
}

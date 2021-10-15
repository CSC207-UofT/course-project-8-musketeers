import java.util.Map;

// TODO: Handle inputs like pi

// Expression for constants
public class NumberExpression extends Expression {

    public NumberExpression(String num){
        super(num);
    }

    // Evaluating a number just means returning itself
    @Override
    public double evaluate(Map<String, Double> arguments) {
        return Double.parseDouble(getItem());
    }
}

import java.util.Map;

// TODO: Handle inputs like pi

// Node for constants
public class NumberNode extends Node{

    public NumberNode(String num){
        super(num);
    }

    // Evaluating a number just means returning itself
    @Override
    public double evaluate(Map<String, Double> arguments) {
        return Double.parseDouble(getItem());
    }
}

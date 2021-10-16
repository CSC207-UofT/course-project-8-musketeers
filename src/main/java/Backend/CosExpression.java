package Backend;

import java.util.Map;

public class CosExpression extends Expression{

    private Expression input;

    // Only one input for cos of course
    // But the input can be an expression of any kind
    public CosExpression(Expression input){
        super("cos");
        this.input = input;
    }

    @Override
    public double evaluate(Map<String, Double> arguments) {
        return Math.cos(input.evaluate(arguments));
    }
}

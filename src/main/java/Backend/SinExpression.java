package Backend;

import java.util.Map;

public class SinExpression extends Expression{

    private Expression input;

    // Only one input for cos of course
    // But the input can be an expression of any kind
    public SinExpression(Expression input){
        super("sin");
        this.input = input;
    }

    @Override
    public double evaluate(Map<String, Double> arguments) {
        return Math.sin(input.evaluate(arguments));
    }
}

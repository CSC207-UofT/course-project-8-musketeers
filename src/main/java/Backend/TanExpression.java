package Backend;

import java.util.Map;

public class TanExpression extends Expression{

    private Expression input;

    // Only one input for cos of course
    // But the input can be an expression of any kind
    public TanExpression(Expression input){
        super("tan");
        this.input = input;
    }

    @Override
    public double evaluate(Map<String, Double> arguments) {
        return Math.tan(input.evaluate(arguments));
    }
}

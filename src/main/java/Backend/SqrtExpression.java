package Backend;

import java.util.Map;

public class SqrtExpression extends Expression{

    private Expression input;

    // Only one input for cos of course
    // But the input can be an expression of any kind
    public SqrtExpression(Expression input){
        super("sqrt");
        this.input = input;
    }

    @Override
    public double evaluate(Map<String, Double> arguments) {
        return Math.sqrt(input.evaluate(arguments));
    }
}

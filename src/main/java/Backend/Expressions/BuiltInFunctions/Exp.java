package Backend.Expressions.BuiltInFunctions;

import Backend.Expressions.FunctionExpression;

import java.util.Map;

public class Exp extends FunctionExpression {

    public Exp (String[] variables){
        super("exp", variables);
    }

    @Override
    public Float evaluate(Map<String, Float> arguments) {
        return (float) Math.exp(getInputs()[0].evaluate(arguments));
    }
}

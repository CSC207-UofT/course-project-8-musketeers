package Backend.Expressions;

import Backend.Expressions.Expression;

import java.util.Map;

public abstract class BooleanValuedExpression extends Expression<Boolean> {
    public BooleanValuedExpression(String num){
        super(num);
    }

    @Override
    public abstract Boolean evaluate(Map<String, Float> arguments);
}


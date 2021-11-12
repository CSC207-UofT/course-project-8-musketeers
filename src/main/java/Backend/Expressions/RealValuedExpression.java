package Backend.Expressions;

import Backend.Expressions.Expression;

import java.util.Map;

public abstract class RealValuedExpression extends Expression<Float> {
    public RealValuedExpression(String num){
        super(num);
    }

    @Override
    public abstract Float evaluate(Map<String, Float> arguments);
}


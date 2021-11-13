package Backend.Expressions;

import java.util.Map;

public abstract class BooleanValuedExpression extends Expression<Boolean> {
    public BooleanValuedExpression(String num){
        super(num);
    }

    @Override
    public abstract Boolean evaluate(Map<String, Float> arguments);
}


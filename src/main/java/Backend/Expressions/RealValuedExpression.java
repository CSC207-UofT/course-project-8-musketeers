package Backend.Expressions;

import java.util.Map;

public abstract class RealValuedExpression extends Expression<Float> {
    public RealValuedExpression(String num){
        super(num);
    }

    @Override
    public abstract Float evaluate(Map<String, Float> arguments);
}


package Backend.Expressions;

import java.util.Map;

public class NumberExpression extends RealValuedExpression {

    public NumberExpression(String num) {
        super(num);
    }

    @Override
    public Float evaluate(Map<String, Float> arguments) {
        return Float.parseFloat(getItem());
    }

}

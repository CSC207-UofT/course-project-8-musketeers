package Backend.Expressions.BuiltInFunctions;

import Backend.Expressions.Expression;
import Backend.Expressions.FunctionExpression;
import Backend.Expressions.RealValuedExpression;

import java.util.Map;

public class Mandel extends FunctionExpression {
    public Mandel(RealValuedExpression[] inputs){
        super("mandel", inputs);
    }

    @Override
    public Float evaluate(Map<String, Float> arguments) {
        float cx = getInputs()[0].evaluate(arguments);
        float cy = getInputs()[1].evaluate(arguments);

        float x = 0;
        float y = 0;
        int i;
        for (i = 0; i < 100; i++) {
            if (x*x + y*y > 4) break;
            float xtemp = x*x - y*y + cx;
            y = 2*x*y + cy;
            x = xtemp;
        }
        return i / 100.f;
    }
}

package Backend.BuiltinExpressions;

import Backend.Expression;
import Backend.FunctionExpression;

import java.util.Map;

public class MandelExpression extends FunctionExpression {

    public MandelExpression(String[] variables){
        super("mandel", variables);
    }

    @Override
    public float evaluate(Map<String, Float> arguments) {
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

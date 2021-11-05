package Backend.BuiltinExpressions;

import Backend.Expression;
import Backend.FunctionExpression;

import java.util.Map;

public class MandelExpression extends FunctionExpression {

    public MandelExpression(Expression[] inputs){
        super("mandel", inputs);
    }

    @Override
    public double evaluate(Map<String, Double> arguments) {
        double cx = getInputs()[0].evaluate(arguments);
        double cy = getInputs()[1].evaluate(arguments);

        double x = 0;
        double y = 0;
        int i;
        for (i = 0; i < 100; i++) {
            if (x*x + y*y > 4) break;
            double xtemp = x*x - y*y + cx;
            y = 2*x*y + cy;
            x = xtemp;
        }
        return (double) i / 100.f;
    }
}

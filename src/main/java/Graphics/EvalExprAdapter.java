
package Graphics;

import Backend.Exceptions.InvalidTermException;
import Backend.Expressions.Expression;
import Backend.ExpressionReader;
import Backend.Expressions.RealValuedExpression;

import java.util.HashMap;
import java.util.Map;

public class EvalExprAdapter implements Evaluatable {
    private final Expression ex;
    private final Map<String, Float> varMap;

    public EvalExprAdapter(Expression expr) {
        this.ex = expr;
        this.varMap = new HashMap<>();
    }
    public float evaluate(float x, float y) {
        varMap.put("x", x);
        varMap.put("y", y);
        return (Float) ex.evaluate(varMap);
    }

    public static void main(String[] args) throws InvalidTermException {
        ExpressionReader er = new ExpressionReader();
        RealValuedExpression func = (RealValuedExpression) er.read("cos ( x + y )");
        EvalExprAdapter a = new EvalExprAdapter(func);
        System.out.println(a.evaluate(0.5f,0.5f));
    }
}
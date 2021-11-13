
package Graphics;

import Backend.Expression;
import Backend.ExpressionReader;

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
        return ex.evaluate(varMap);
    }

    @Override
    public float evaluate(float x) {
        varMap.put("x", x);
        return ex.evaluate(varMap);
    }

    public static void main(String[] args) {
        ExpressionReader er = new ExpressionReader();
        Expression func = er.read("cos ( x )");
        EvalExprAdapter a = new EvalExprAdapter(func);
        System.out.println(a.evaluate(0.5f,0.5f));
    }
}
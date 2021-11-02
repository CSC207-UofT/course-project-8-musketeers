
package Graphics;

import Backend.Expression;
import Backend.ExpressionReader;

import java.util.HashMap;
import java.util.Map;

public class EvalExprAdapter implements Evaluatable {
    private final Expression ex;
    private final Map<String, Double> varMap;

    public EvalExprAdapter(Expression expr) {
        this.ex = expr;
        this.varMap = new HashMap<>();
    }
    public float evaluate(float x, float y) {
        varMap.put("x", (double)x);
        varMap.put("y", (double)y);
        return (float)ex.evaluate(varMap);
    }

    public static void main(String[] args) {
        ExpressionReader er = new ExpressionReader();
        Expression func = er.read("cos ( x + y )");
        EvalExprAdapter a = new EvalExprAdapter(func);
        System.out.println(a.evaluate(0.5f,0.5f));
    }
}
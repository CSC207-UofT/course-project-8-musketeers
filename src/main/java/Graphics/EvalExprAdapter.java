// Deprecated, not necessary anymore
package Graphics;


import Backend.Expressions.RealValuedExpression;

import java.util.HashMap;
import java.util.Map;

public class EvalExprAdapter implements Evaluatable {
    private final RealValuedExpression ex;
    private final Map<String, Float> varMap;

    public EvalExprAdapter(RealValuedExpression expr) {
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

}
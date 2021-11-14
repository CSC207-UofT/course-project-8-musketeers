package Backend.Expressions;

import Graphics.Evaluatable;

import java.util.HashMap;
import java.util.Map;

public abstract class RealValuedExpression extends Expression<Float> implements Evaluatable {
    public RealValuedExpression(String num){
        super(num);
    }

    @Override
    public float evaluate(float x, float y) {
        Map<String, Float> varMap = new HashMap<>();
        varMap.put("x", x);
        varMap.put("y", y);
        return evaluate(varMap); // this evaluate is from Expression class
    }

    @Override
    public float evaluate(float x) {
        Map<String, Float> varMap = new HashMap<>();
        varMap.put("x", x);
        return evaluate(varMap);
    }
}


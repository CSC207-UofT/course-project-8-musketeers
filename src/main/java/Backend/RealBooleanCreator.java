package Backend;

import Backend.Exceptions.InvalidTermException;
import Backend.ExpressionBuilders.BooleanValuedExpressionFactory;
import Backend.ExpressionBuilders.RealValuedExpressionFactory;
import Backend.Expressions.Expression;
import Backend.Expressions.FunctionExpression;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RealBooleanCreator implements ExpressionCreator{ protected final ExpressionPropertyReporter vc;
    protected final Map<String, FunctionExpression> funcMap = new HashMap<>();
    protected final RealValuedExpressionFactory rf;
    protected final BooleanValuedExpressionFactory bf;

    public RealBooleanCreator(Map<String, FunctionExpression> funcMap, ExpressionPropertyReporter vc,
                              RealValuedExpressionFactory rf, BooleanValuedExpressionFactory bf) {
        for (String funcName : funcMap.keySet()) {
            this.funcMap.put(funcName, funcMap.get(funcName));
        }
        this.vc = vc;
        this.rf = rf;
        this.bf = bf;
    }

    public abstract Expression<?> create(List<String> terms) throws InvalidTermException;

}
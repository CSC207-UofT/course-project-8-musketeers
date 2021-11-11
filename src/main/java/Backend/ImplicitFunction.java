package Backend;

import java.util.Map;

/**
 * This is for graphing this of the form f(x, y) = g(x, y).
 * We rearrange it to solve for f(x, y) - g(x, y) = 0
 */
public class ImplicitFunction extends Expression{

    private final Expression expr;
    private ComparatorExpression domain;

    public ImplicitFunction(Expression lExpr, Expression rExpr){
        super("implicit");
        this.expr = new OperatorExpression("-", lExpr, rExpr);

        this.domain = (new Constants()).trivialDomain();
    }

    public ImplicitFunction(Expression lExpr, Expression rExpr, ComparatorExpression domain){
        this(lExpr, rExpr);
        setDomain(domain);
    }

    private void setDomain(ComparatorExpression domain){
        this.domain = domain;
    }

    @Override
    public float evaluate(Map<String, Float> arguments) {
        if (this.domain.evaluate(arguments) == -1) {
            return Float.NaN;
        }
        return this.expr.evaluate(arguments);
    }
}

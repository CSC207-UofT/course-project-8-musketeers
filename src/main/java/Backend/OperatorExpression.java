package Backend;

import java.util.HashMap;
import java.util.Map;

// Backend.OperatorExpression stores the 'Backend.Expression' with operators, e.g. E_1 + E_2
// where E_1, E_2 themselves are Expressions
public class OperatorExpression extends Expression {

    protected final Expression lExpression;
    protected final Expression rExpression;

    // OperatorExpressions store both the operation they correspond to
    // and the expressions to their left and right
    public OperatorExpression(String operation, Expression lExpression, Expression rExpression){
        super(operation);
        this.lExpression = lExpression;
        this.rExpression = rExpression;
    }

    // We evaluate this expression based on what the operator is
    @Override
    public float evaluate(Map<String, Float> arguments) {
        switch (getItem()) {
            case "+":
                return lExpression.evaluate(arguments) + rExpression.evaluate(arguments);
            case "-":
                return lExpression.evaluate(arguments) - rExpression.evaluate(arguments);
            case "*":
                return lExpression.evaluate(arguments) * rExpression.evaluate(arguments);
            case "/":
                return lExpression.evaluate(arguments) / rExpression.evaluate(arguments);
            case "^":
                return (float) Math.pow(lExpression.evaluate(arguments), rExpression.evaluate(arguments));
            default:
                // TODO: Should this be the default?
                // Throw some kind of custom exception?
                throw new IllegalArgumentException("Unexpected operator");
        }

    }

}

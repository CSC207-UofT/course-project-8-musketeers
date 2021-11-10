package Backend.Expressions;

import java.util.Map;

// Backend.Expressions.OperatorExpression stores the 'Backend.Expressions.Expression' with operators, e.g. E_1 + E_2
// where E_1, E_2 themselves are Expressions
public class OperatorExpression extends RealValuedExpression {

    private final RealValuedExpression lExpression;
    private final RealValuedExpression rExpression;

    // OperatorExpressions store both the operation they correspond to
    // and the expressions to their left and right
    public OperatorExpression(String operation, RealValuedExpression lExpression, RealValuedExpression rExpression){
        super(operation); // TODO: Future have a complete string representation rather than just a function name!
        this.lExpression = lExpression;
        this.rExpression = rExpression;
    }

    // We evaluate this expression based on what the operator is
    @Override
    public Float evaluate(Map<String, Float> arguments) {
        return switch (getItem()) {
            case "+" -> lExpression.evaluate(arguments) + rExpression.evaluate(arguments);
            case "-" -> lExpression.evaluate(arguments) - rExpression.evaluate(arguments);
            case "*" -> lExpression.evaluate(arguments) * rExpression.evaluate(arguments);
            case "/" -> lExpression.evaluate(arguments) / rExpression.evaluate(arguments);
            case "^" -> (float) Math.pow(lExpression.evaluate(arguments), rExpression.evaluate(arguments));
            default ->
                    // TODO: Should this be the default? DISAGREE (from TED), as any unrecognized character will be checked by ExpressionValidityChecker!
                    throw new IllegalArgumentException("Unexpected operator");
        };

    }

}

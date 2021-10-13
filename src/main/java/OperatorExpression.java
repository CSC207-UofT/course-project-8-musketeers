import java.util.Map;

// OperatorExpression stores the 'Expression' with operators, e.g. E_1 + E_2
// where E_1, E_2 themselves are Expressions
public class OperatorExpression extends Expression {

    private final Expression lExpression;
    private final Expression rExpression;

    // OperatorExpressions store both the operation they correspond to
    // and the expressions to their left and right
    public OperatorExpression(String operation, Expression lExpression, Expression rExpression){
        super(operation);
        this.lExpression = lExpression;
        this.rExpression = rExpression;
    }

    // We evaluate this expression based on what the operator is
    @Override
    public double evaluate(Map<String, Double> arguments) {

        return switch (getItem()) {
            case "+" -> lExpression.evaluate(arguments) + rExpression.evaluate(arguments);
            case "-" -> lExpression.evaluate(arguments) - rExpression.evaluate(arguments);
            case "*" -> lExpression.evaluate(arguments) * rExpression.evaluate(arguments);
            case "/" -> lExpression.evaluate(arguments) / rExpression.evaluate(arguments);
            case "^" -> Math.pow(lExpression.evaluate(arguments), rExpression.evaluate(arguments));
            default -> throw new IllegalArgumentException("Unexpected operator");
        };

    }

}

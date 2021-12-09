package Backend.Expressions;

import java.util.Map;

// Backend.Expressions.OperatorExpression stores the 'Backend.Expressions.Expression' with operators, e.g. E_1 + E_2
// where E_1, E_2 themselves are Expressions
public class ArithmeticOperatorExpression extends RealValuedExpression {

    private final RealValuedExpression lExpression;
    private final RealValuedExpression rExpression;

    // OperatorExpressions store both the operation they correspond to
    // and the expressions to their left and right
    public ArithmeticOperatorExpression(String operation, RealValuedExpression lExpression, RealValuedExpression rExpression) {
        super(operation);
        this.lExpression = lExpression;
        this.rExpression = rExpression;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        if (getItem().equals("^")) {
            str.append("pow(");
            str.append(lExpression);
            str.append(",");
            str.append(rExpression);
        } else {
            str.append("(");
            str.append(lExpression);
            str.append(getItem());
            str.append(rExpression);
            str.append(")");
        }
        return str.toString();
    }

    // We evaluate this expression based on what the operator is
    @Override
    public Float evaluate(Map<String, Float> arguments) {
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
                // If our program works, then this should never be run
                throw new IllegalArgumentException("Unexpected operator");
        }

    }

}

package Backend.Expressions;

import java.util.Map;


public class LogicalOperatorExpression extends BooleanValuedExpression {
    private final BooleanValuedExpression lExpression;
    private final BooleanValuedExpression rExpression;

    public LogicalOperatorExpression(String operation, BooleanValuedExpression lExpression, BooleanValuedExpression rExpression) {
        super(operation);
        this.lExpression = lExpression;
        this.rExpression = rExpression;
    }

    @Override
    public Boolean evaluate(Map<String, Float> arguments) {
        boolean trueComparison;

        switch (getItem()) {
            case "&":
                trueComparison = this.lExpression.evaluate(arguments) &&
                        this.rExpression.evaluate(arguments);
                return trueComparison;

            case "|":
                trueComparison = this.lExpression.evaluate(arguments) ||
                        this.rExpression.evaluate(arguments);
                return trueComparison;
            default:
                throw new IllegalStateException("Unrecognized Logical Operator!");

        }
    }

    @Override
    public String toString() {
        return "(" + lExpression + getItem() + rExpression + ")";
    }
}

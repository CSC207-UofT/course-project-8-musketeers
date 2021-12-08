package Backend.ExpressionBuilders;

import Backend.Expressions.*;

public class BooleanValuedExpressionFactory implements ExpressionFactory<BooleanValuedExpression> {
    // Below construct with (binary) operators.
    @Override
    public BooleanValuedExpression constructExpression(Expression<?> lExpr, String op, Expression<?> rExpr,
                                                       String operatorType) {
        BooleanValuedExpression expr;
        switch (operatorType) {
            case "Comparator":
                expr = new ComparatorExpression(op, (RealValuedExpression) lExpr, (RealValuedExpression) rExpr);
                break;
            case "Logical":
                expr = new LogicalOperatorExpression(op, (BooleanValuedExpression) lExpr,
                        (BooleanValuedExpression) rExpr);
                break;
            default:
                throw new IllegalStateException("Unrecognized Operator Type!");
        }

        return expr;
    }
}
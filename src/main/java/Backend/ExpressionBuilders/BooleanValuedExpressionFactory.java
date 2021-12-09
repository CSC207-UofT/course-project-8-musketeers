package Backend.ExpressionBuilders;

import Backend.Constants;
import Backend.Exceptions.CompoundCaseCreatorException;
import Backend.Exceptions.EmptyBuilderException;
import Backend.Exceptions.InvalidTermException;
import Backend.Expressions.*;

public class BooleanValuedExpressionFactory implements ExpressionFactory<BooleanValuedExpression> {
    /**This method creates an operator Expression in which the operator's inputs are the given left and right input
     * expressions e.g. applies < to 1 and 3 to get a 1 < 3 expression.
     *
     * @param lExpr The left input expression to the operator.
     * @param op The String representation of the operator based on which we want to create a new expression.
     * @param rExpr The right input expression to the operator.
     * @param operatorType The String representation of what type of operator it is
     *                     (currently either 'Logical' or 'Comparator')
     * @return The current ExpressionBuilder, which now contains the desired operator Expression.
     * @throws InvalidTermException If the input expressions are of invalid type e.g. 1 & 3.
     */
    @Override
    public BooleanValuedExpression constructExpression(Expression<?> lExpr, String op, Expression<?> rExpr,
                                                       String operatorType) throws InvalidTermException {
        BooleanValuedExpression expr;
        switch (operatorType) {
            case "Comparator":
                if (!(lExpr instanceof RealValuedExpression && rExpr instanceof RealValuedExpression)){
                    throw new CompoundCaseCreatorException("OperandTypeException!");
                }
                expr = new ComparatorExpression(op, (RealValuedExpression) lExpr, (RealValuedExpression) rExpr);
                break;
            case "Logical":
                if (!(lExpr instanceof BooleanValuedExpression && rExpr instanceof BooleanValuedExpression)){
                    throw new CompoundCaseCreatorException("OperandTypeException!");
                }
                expr = new LogicalOperatorExpression(op, (BooleanValuedExpression) lExpr,
                        (BooleanValuedExpression) rExpr);
                break;
            default:
                throw new IllegalStateException("Unrecognized Operator Type!");
        }

        return expr;
    }
}
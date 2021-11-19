package Backend.ExpressionBuilders;

import Backend.Constants;
import Backend.Exceptions.CompoundCaseCreatorException;
import Backend.Exceptions.EmptyBuilderException;
import Backend.Exceptions.InvalidTermException;
import Backend.Expressions.*;

import java.util.Map;

public class BooleanValuedExpressionBuilder extends ExpressionBuilder<BooleanValuedExpression> {
    /**This method creates an operator Expression in which the operator's inputs are the given left and right input
     * expressions e.g. applies < to 1 and 3 to get a 1 < 3 expression.
     *
     * @param lExprBuilder An ExpressionBuilder object containing the left input expression to the operator.
     * @param op The String representation of the operator based on which we want to create a new expression.
     * @param rExprBuilder An ExpressionBuilder object containing the right input expression to the operator.
     * @param operatorType The String representation of what type of operator it is
     *                     (currently either 'Logical' or 'Comparator')
     * @return The current ExpressionBuilder, which now contains the desired operator Expression.
     * @throws InvalidTermException If the input expressions are of invalid type e.g. 1 & 3.
     */
    @Override
    public BooleanValuedExpressionBuilder constructExpression(ExpressionBuilder<?> lExprBuilder,
                                                              String op,
                                                              ExpressionBuilder<?> rExprBuilder,
                                                              String operatorType) throws InvalidTermException {
        // in each case,
        switch (operatorType) {
            case "Comparator":
                if (!(lExprBuilder instanceof RealValuedExpressionBuilder &&
                        rExprBuilder instanceof RealValuedExpressionBuilder)){
                    throw new CompoundCaseCreatorException("OperandTypeException!");
                }
                this.expr = new ComparatorExpression(op, (RealValuedExpression) lExprBuilder.build(),
                        (RealValuedExpression) rExprBuilder.build());
                break;
            case "Logical":
                if (!(lExprBuilder instanceof BooleanValuedExpressionBuilder &&
                        rExprBuilder instanceof BooleanValuedExpressionBuilder)){
                    throw new CompoundCaseCreatorException("OperandTypeException!");
                }
                this.expr = new LogicalOperatorExpression(op, (BooleanValuedExpression) lExprBuilder.build(),
                        (BooleanValuedExpression) rExprBuilder.build());
                break;
            default:
                throw new IllegalStateException("Unrecognized Operator Type!");
                // TODO: Above: IllegalStateException or IllegalArgumentException? Java automatically defaults "IllegalStateException" so...
        }

        return this;
    }
}
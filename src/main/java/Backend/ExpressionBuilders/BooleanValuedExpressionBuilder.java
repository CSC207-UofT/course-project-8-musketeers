package Backend.ExpressionBuilders;

import Backend.Constants;
import Backend.Exceptions.EmptyBuilderException;
import Backend.Expressions.*;

import java.util.Map;

public class BooleanValuedExpressionBuilder extends ExpressionBuilder {
    private final Constants constants = new Constants();
    private BooleanValuedExpression expr = null;

    // Below construct with (binary) operators.
    @Override
    public BooleanValuedExpressionBuilder constructExpression(ExpressionBuilder lExprBuilder, String op,
                                                              ExpressionBuilder rExprBuilder, String operatorType)
            throws EmptyBuilderException{
        switch (operatorType) {
            case "Comparator":
                this.expr = new ComparatorExpression(op, (RealValuedExpression) lExprBuilder.build(),
                        (RealValuedExpression) rExprBuilder.build());
                break;
            case "Logical":
                this.expr = new LogicalOperatorExpression(op, (BooleanValuedExpression) lExprBuilder.build(),
                        (BooleanValuedExpression) rExprBuilder.build());
                break;
            default:
                throw new IllegalStateException("Unrecognized Operator Type!");
                // TODO: Above: IllegalStateException or IllegalArgumentException? Java automatically defaults "IllegalStateException" so...
        }

        return this;
    }

    public BooleanValuedExpression build() throws EmptyBuilderException {
        if (this.expr == null){
            throw new EmptyBuilderException("Builder's Expression hasn't been initialized");
        }
        return this.expr;
    }
}
package Backend.ExpressionBuilders;

import Backend.Constants;
import Backend.Exceptions.EmptyBuilderException;
import Backend.Expressions.*;

import java.util.Map;

public abstract class ExpressionBuilder {
    private final Constants constants = new Constants();
    private Expression<?> expr = null;

    // mandates that every ExpressionBuilder has the ability to handle binary operators
    // operatorType might not be necessary
    public abstract ExpressionBuilder constructExpression(ExpressionBuilder lExpr, String op, ExpressionBuilder rExpr,
                                                          String operatorType) throws EmptyBuilderException;

    public Expression<?> build() throws EmptyBuilderException {
        if (this.expr == null){
            throw new EmptyBuilderException("Builder's Expression hasn't been initialized");
        }
        return this.expr;
    }
}
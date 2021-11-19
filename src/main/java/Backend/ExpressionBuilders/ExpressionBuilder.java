package Backend.ExpressionBuilders;

import Backend.Constants;
import Backend.Exceptions.EmptyBuilderException;
import Backend.Exceptions.InvalidTermException;
import Backend.Expressions.*;

import java.util.Map;

public abstract class ExpressionBuilder<T extends Expression<?>> {
    // T is the type of expression that the ExpressionBuilder subclass builds.
    final Constants constants = new Constants();
    T expr;

    // mandates that every ExpressionBuilder has the ability to handle binary operators
    // operatorType might not be necessary
    public abstract ExpressionBuilder<T> constructExpression(ExpressionBuilder<?> lExprBuilder, String op,
                                                             ExpressionBuilder<?> rExprBuilder,
                                                             String operatorType) throws InvalidTermException;

    public T build() throws EmptyBuilderException {
        if (this.expr == null){
            throw new EmptyBuilderException("Builder's Expression hasn't been initialized");
        }
        return this.expr;
    }
}
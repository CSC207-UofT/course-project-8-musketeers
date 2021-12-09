package Backend.ExpressionBuilders;

import Backend.Constants;
import Backend.Exceptions.EmptyBuilderException;
import Backend.Exceptions.InvalidTermException;
import Backend.Expressions.*;
import java.util.Map;

public interface ExpressionFactory<T extends Expression<?>> {
    // T is the type of expression that the ExpressionBuilder subclass builds.
    Constants constants = new Constants();

    // mandates that every ExpressionBuilder has the ability to handle binary operators
    // operatorType might not be necessary
    public T constructExpression(Expression<?> lExpr, String op, Expression<?> rExpr, String operatorType) throws
            InvalidTermException;
}
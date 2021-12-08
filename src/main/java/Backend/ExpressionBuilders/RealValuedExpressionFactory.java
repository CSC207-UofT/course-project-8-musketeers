package Backend.ExpressionBuilders;

import Backend.Exceptions.EmptyBuilderException;
import Backend.Expressions.*;

import java.util.Map;

public class RealValuedExpressionFactory implements ExpressionFactory<RealValuedExpression> {
    // Below base case: Construct Number or Variable.
    public RealValuedExpression constructExpression(String input) {
        RealValuedExpression expr;
        if (this.constants.getVariables().contains(input)) {
            expr = new VariableExpression(input);
        } else expr = new NumberExpression(input);

        return expr;
    }

    @Override
    // Below construct with (binary) operators.
    public RealValuedExpression constructExpression(Expression<?> lExpr, String op, Expression<?> rExpr,
                                                    String operatorType) {
        RealValuedExpression expr;
        // the switch statement is mainly here in case we want to add more functionality
        switch (operatorType) {
            case "Arithmetic":
                expr = new ArithmeticOperatorExpression(op, (RealValuedExpression) lExpr,
                        (RealValuedExpression) rExpr);
                break;
            // If our program is correct, below should never happen.
            default:
                throw new IllegalStateException("Unrecognized Operator Type!");
        }

        return expr;
    }

    // Below should construct functions (including build-in and user-defined functions)
    public RealValuedExpression constructExpression(String funcName, RealValuedExpression[] inputs,
                                                    Map<String, FunctionExpression> funcMap)
            throws EmptyBuilderException {
        FunctionExpression oldFunc = funcMap.get(funcName);

        // We create a new copy of the function other the set inputs below would overwrite the original values
        FunctionExpression newFunc = new CustomFunctionExpression(funcName, oldFunc.getVariables(), oldFunc);
        newFunc.setInputs(inputs);

        return newFunc;
    }
}
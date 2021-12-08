package Backend.ExpressionBuilders;

import Backend.Constants;
import Backend.Exceptions.BaseCaseCreatorException;
import Backend.Exceptions.CompoundCaseCreatorException;
import Backend.Exceptions.EmptyBuilderException;
import Backend.Exceptions.InvalidTermException;
import Backend.Expressions.*;

import java.util.Map;

public class RealValuedExpressionFactory implements ExpressionFactory<RealValuedExpression> {
    // Below base case: Construct Number or Variable.
    public RealValuedExpression constructExpression(String input) throws InvalidTermException {
        RealValuedExpression expr;
        if (this.constants.getVariables().contains(input)){
            expr = new VariableExpression(input);
        }
        else { // only other valid possibility is for it to be a number and not a variable.
            try {
                Float.parseFloat(input); // check whether "term" represents a valid float.
            } catch (NumberFormatException e) {
                throw new BaseCaseCreatorException(BaseCaseCreatorException.ERRORMESSAGE_INVALID_SINGLE_TERM);
            }

            expr = new NumberExpression(input);
        }

        return expr;
    }

    /**This method creates an binary operator Expression in which the operator's inputs are the given left and right
     * input expressions e.g. applies + to 1 and 3 to get a 1 + 3 expression.
     *
     * @param lExpr The left input expression to the operator.
     * @param op The String representation of the operator based on which we want to create a new expression.
     * @param rExpr The right input expression to the operator.
     * @param operatorType The String representation of what type of operator it is (currently only 'Arithmetic')
     * @return The current ExpressionBuilder, which now contains the desired operator Expression.
     * @throws InvalidTermException If the input expressions are of invalid type e.g. (1 < 3) + 1.
     */
    @Override
    public RealValuedExpression constructExpression(Expression<?> lExpr, String op, Expression<?> rExpr,
                                                    String operatorType) throws InvalidTermException {
        RealValuedExpression expr;
        // the switch statement is mainly here in case we want to add more functionality
        switch (operatorType) {
            case "Arithmetic":
                if (!(lExpr instanceof RealValuedExpression && rExpr instanceof RealValuedExpression)){
                    throw new CompoundCaseCreatorException("OperandTypeException!");
                }
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
    public RealValuedExpression constructExpression(String funcName, Expression<?>[] inputs,
                                                    Map<String, FunctionExpression> funcMap)
            throws InvalidTermException {
        for (Expression<?> expr: inputs){
            if (!(expr instanceof RealValuedExpression)) {
                throw new CompoundCaseCreatorException("Invalid function input type: RealValuedExpression required");
            }
        }

        FunctionExpression oldFunc = funcMap.get(funcName);
        // We create a new copy of the function other the set inputs below would overwrite the original values
        FunctionExpression newFunc = new CustomFunctionExpression(funcName, oldFunc.getVariables(), oldFunc);
        newFunc.setInputs((RealValuedExpression[])inputs);

        return newFunc;
    }
}
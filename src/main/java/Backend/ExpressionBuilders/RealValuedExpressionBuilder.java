package Backend.ExpressionBuilders;

import Backend.Exceptions.BaseCaseCreatorException;
import Backend.Exceptions.CompoundCaseCreatorException;
import Backend.Exceptions.InvalidTermException;
import Backend.Expressions.*;

import java.util.Map;

public class RealValuedExpressionBuilder extends ExpressionBuilder<RealValuedExpression> {
    // Below base case: Construct Number or Variable.
    public RealValuedExpressionBuilder constructExpression(String input) throws InvalidTermException {
        if (this.constants.getVariables().contains(input)){
            this.expr = new VariableExpression(input);
        }
        else { // only other valid possibility is for it to be a number and not a variable.
            try {
                Float.parseFloat(input); // check whether "term" represents a valid float.
            } catch (NumberFormatException e) {
                throw new BaseCaseCreatorException(BaseCaseCreatorException.ERRORMESSAGE_INVALID_SINGLE_TERM);
            }

            this.expr = new NumberExpression(input);
        }

        return this;
    }

    /**This method creates an binary operator Expression in which the operator's inputs are the given left and right
     * input expressions e.g. applies + to 1 and 3 to get a 1 + 3 expression.
     *
     * @param lExprBuilder An ExpressionBuilder object containing the left input expression to the operator.
     * @param op The String representation of the operator based on which we want to create a new expression.
     * @param rExprBuilder An ExpressionBuilder object containing the right input expression to the operator.
     * @param operatorType The String representation of what type of operator it is (currently only 'Arithmetic')
     * @return The current ExpressionBuilder, which now contains the desired operator Expression.
     * @throws InvalidTermException If the input expressions are of invalid type e.g. (1 < 3) + 1.
     */
    @Override
    public ExpressionBuilder<RealValuedExpression> constructExpression(ExpressionBuilder<?> lExprBuilder, String op,
                                                                       ExpressionBuilder<?> rExprBuilder,
                                                                       String operatorType) throws InvalidTermException {
        if (!(lExprBuilder instanceof RealValuedExpressionBuilder) ||
                !(rExprBuilder instanceof RealValuedExpressionBuilder)){
            throw new CompoundCaseCreatorException("OperandTypeException!");
        }

        switch (operatorType) {
            case "Arithmetic":
                this.expr = new ArithmeticOperatorExpression(op, (RealValuedExpression) lExprBuilder.build(),
                        (RealValuedExpression) rExprBuilder.build());
                break;
            // If our program is correct, below should never happen.
            default:
                throw new IllegalStateException("Unrecognized Operator Type!");
            // TODO: Above: IllegalStateException or IllegalArgumentException? Java automatically defaults "IllegalStateException" so...
        }

        return this;
    }

    // Below should construct functions (including build-in and user-defined functions)
    // TODO: Future: Include User-Defined Functions once we made it clear how we want to treat them. E.g. Where to store them and how to handle them...
    public RealValuedExpressionBuilder constructExpression(String funcName, ExpressionBuilder<?>[] inputs,
                                                           Map<String, FunctionExpression> funcMap)
            throws InvalidTermException {
        // This is all we need to check if the function was given enough inputs.
        if (funcMap.get(funcName).getInputs().length != inputs.length){
            throw new CompoundCaseCreatorException("FunctionInputsSizeException!");
        }

        RealValuedExpression[] inputExpressions = new RealValuedExpression[inputs.length];
        for (int i=0; i < inputs.length; ++i){
            Expression<?> uncastedExpression = inputs[i].build();
            try{
                inputExpressions[i] = (RealValuedExpression) uncastedExpression;
            }
            catch (ClassCastException E) {
                throw new CompoundCaseCreatorException("Invalid function input type: RealValuedExpression required");
            }
        }

        FunctionExpression func = funcMap.get(funcName);
        func.setInputs(inputExpressions);
        this.expr = func;
        return this;
    }
}
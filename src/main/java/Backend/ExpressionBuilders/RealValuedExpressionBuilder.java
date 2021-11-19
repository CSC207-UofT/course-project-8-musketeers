package Backend.ExpressionBuilders;

import Backend.Constants;
import Backend.Exceptions.EmptyBuilderException;
import Backend.Expressions.*;

import java.util.Map;

public class RealValuedExpressionBuilder extends ExpressionBuilder<RealValuedExpression> {
    // Below base case: Construct Number or Variable.
    public RealValuedExpressionBuilder constructExpression(String input){
        if (this.constants.getVariables().contains(input)){
            this.expr = new VariableExpression(input);
        }
        else this.expr = new NumberExpression(input);

        return this;
    }

    @Override
    // Below construct with (binary) operators.
    public ExpressionBuilder<RealValuedExpression> constructExpression(ExpressionBuilder<?> lExprBuilder, String op,
                                                                       ExpressionBuilder<?> rExprBuilder,
                                                                       String operatorType) throws EmptyBuilderException{
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
    public RealValuedExpressionBuilder constructExpression(String funcName, RealValuedExpressionBuilder[] inputs,
                                                           Map<String, FunctionExpression> funcMap)
            throws EmptyBuilderException {
        RealValuedExpression[] inputExpressions = new RealValuedExpression[inputs.length];
        for (int i=0; i < inputs.length; ++i){
            // TODO: add a way to deal with the case where inputs[i].build() isn't a RealValuedExpression
            inputExpressions[i] = inputs[i].build();
        }

        FunctionExpression func = funcMap.get(funcName);
        func.setInputs(inputExpressions);
        this.expr = func;
        return this;
    }
}
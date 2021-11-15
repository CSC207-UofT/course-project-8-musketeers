package Backend;

import Backend.Expressions.*;

import java.util.Map;

public class ExpressionBuilder {
    private final Constants constants = new Constants();

    // Below base case: Construct Number or Variable.
    public RealValuedExpression constructExpression(String input){
        if (this.constants.getVariables().contains(input)){
            return new VariableExpression(input);
        }
        else return new NumberExpression(input);
    }

    // Below construct with (binary) operators.
    public Expression<?> constructExpression(Expression<?> lExpression, String op, Expression<?> rExpression, String operatorType){

        switch (operatorType) {
            case "Logical":
                return new LogicalOperatorExpression(op, (BooleanValuedExpression) lExpression,
                    (BooleanValuedExpression) rExpression);
            case "Comparator":
                return new ComparatorExpression(op, (RealValuedExpression) lExpression,
                    (RealValuedExpression) rExpression);
            case "Arithmetic":
                return new ArithmeticOperatorExpression(op, (RealValuedExpression) lExpression,
                    (RealValuedExpression) rExpression);
            // If our program is correct, below should never happen.
            default:
                throw new IllegalStateException("Unrecognized Operator Type!");
            // TODO: Above: IllegalStateException or IllegalArgumentException? Java automatically defaults "IllegalStateException" so...
        }
    }

    // Below should construct functions (including build-in and user-defined functions)
    // TODO: Future: Include User-Defined Functions once we made it clear how we want to treat them. E.g. Where to store them and how to handle them...
    public RealValuedExpression constructExpression(String funcName, RealValuedExpression[] inputs,
                                                    Map<String, FunctionExpression> funcMap){
        FunctionExpression func = funcMap.get(funcName);
        func.setInputs(inputs);
        return func;
    }
}
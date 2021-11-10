package Backend;

import Backend.Expressions.*;
import Backend.Expressions.BuiltInFunctions.*;

public class ExpressionBuilder {
    private final Constants constants = new Constants();

    // public ExpressionBuilder(){}

    // Below base case: Construct Number or Variable.
    public Expression constructExpression(String input){
        if (this.constants.getVariables().contains(input)){
            return new VariableExpression(input);
        }
        else return new NumberExpression(input);
    }

    // TODO: Determine how to construct all kinds of operators.
    public Expression constructExpression(Expression lExpression, String op, Expression rExpression){
        if (this.constants.getOperators().contains(op)){
            return new OperatorExpression(op, lExpression, rExpression);
        }

        if (this.constants.getComparators().contains(op)){
            return new ComparatorExpression(op, lExpression, rExpression);
        }

        if (this.constants.getLogicalOperators().contains(op)){
            return new LogicalOperatorExpression(op, lExpression, rExpression);
        }

        return new NumberExpression("5");
    }

    // Below construct functions (including build-in and user-defined functions)
    public RealValuedExpression constructExpression(String funcName, RealValuedExpression[] inputs){
        return switch (funcName) {
            case "cos" -> new Cosine(inputs);
            case "sin" -> new Sine(inputs);
            case "tan" -> new Tangent(inputs);
            case "sqrt" -> new SquareRoot(inputs);
            case "mandel" -> new Mandel(inputs);
            default -> throw new IllegalArgumentException("Unrecognised function");
            // TODO: Recheck this default!
            // default: return new CustomFunctionExpression()
        };
    }
}
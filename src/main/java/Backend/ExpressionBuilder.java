package Backend;

import Backend.Expressions.*;
import Backend.Expressions.BuiltInFunctions.*;

public class ExpressionBuilder {
    private final Constants constants = new Constants();

    // public ExpressionBuilder(){}

    // Below base case: Construct Number or Variable.
    public RealValuedExpression constructExpression(String input){
        if (this.constants.getVariables().contains(input)){
            return new VariableExpression(input);
        }
        else return new NumberExpression(input);
    }

    // Below construct with (binary) operators.
    public Expression<?> constructExpression(Expression<?> lExpression, String op,
                                                           Expression<?> rExpression){
        if (this.constants.getOperators().contains(op)){
            return new OperatorExpression(op, (RealValuedExpression) lExpression, (RealValuedExpression) rExpression);
        }

        else if (this.constants.getComparators().contains(op)){
            return new ComparatorExpression(op, (RealValuedExpression) lExpression, (RealValuedExpression) rExpression);
        }

        else if (this.constants.getLogicalOperators().contains(op)){
            return new LogicalOperatorExpression(op, (BooleanValuedExpression) lExpression,
                    (BooleanValuedExpression) rExpression);
        }

        else {
            // If our program is correct, below should never happen.
            throw new IllegalArgumentException("Unrecognized operator!"); // TODO: Or user-defined exception?
        }
    }

    // Below shuold construct functions (including build-in and user-defined functions)
    // TODO: Include User-Defined Functions once we made it clear how we want to treat them. E.g. Where to store them and how to handle them...
    public RealValuedExpression constructExpression(String funcName, RealValuedExpression[] inputs){
        return switch (funcName) {
            case "cos" -> new Cosine(inputs);
            case "sin" -> new Sine(inputs);
            case "tan" -> new Tangent(inputs);
            case "sqrt" -> new SquareRoot(inputs);
            case "mandel" -> new Mandel(inputs);
            /* If our program is correct, below should never happen. */
            default -> throw new IllegalArgumentException("Unrecognized function!"); // TODO: Or user-defined exception?
            // TODO: Recheck this default!
            // default: return new CustomFunctionExpression()
        };
    }
}
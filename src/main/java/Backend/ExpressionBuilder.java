package Backend;

import Backend.Expressions.*;
import Backend.Expressions.BuiltInFunctions.*;

public class ExpressionBuilder {
    private final Constants constants = new Constants();

    public ExpressionBuilder(){}

    public Expression constructExpression(String input){
        if (this.constants.getVariables().contains(input)){
            return new VariableExpression(input);
        }
        else return new NumberExpression(input);
    }

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

    public Expression constructExpression(String funcName, Expression[] inputs){
        switch (funcName){
            case "cos": return new Cosine(inputs);
            case "sin": return new Sine(inputs);
            case "tan": return new Tangent(inputs);
            case "sqrt": return new SquareRoot(inputs);
            case "mandel": return new Mandel(inputs);
            default: throw new IllegalArgumentException("Unrecognised function");
        }
    }
}
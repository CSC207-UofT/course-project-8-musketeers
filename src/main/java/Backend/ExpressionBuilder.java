package Backend;

import java.util.List;
import Backend.BuiltinExpressions.*;

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
            case "cos": return new CosExpression(inputs);
            case "sin": return new SinExpression(inputs);
            case "tan": return new TanExpression(inputs);
            case "sqrt": return new SqrtExpression(inputs);
            case "mandel": return new MandelExpression(inputs);
            default: throw new IllegalArgumentException("Unrecognised function");
        }
    }
}
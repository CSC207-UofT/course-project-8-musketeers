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

        if (this.constants.getLogicalOperators().contains(op)){
            return new LogicalOperatorExpression(op, lExpression, rExpression);
        }

        // This is the case where the user has not inputted a valid op String.
        // TODO: use a better error return Expression
        return new NumberExpression("5");
    }

    /**
     * This method currently only constructs comparator expressions.
     *
     * @param expressions The
     * @param ops
     * @return
     */
    public Expression constructExpression(List<Expression> expressions, List<String> ops){
        return new ComparatorExpression(expressions, ops);
    }

    public Expression constructExpression(String funcName, Expression[] inputs){
        String[] variables = new String[inputs.length];
        FunctionExpression func;
        switch (funcName){
            case "cos":
                func = new CosExpression(variables);
                break;
            case "sin":
                func = new SinExpression(variables);
                break;
            case "tan":
                func = new TanExpression(variables);
                break;
            case "sqrt":
                func = new SqrtExpression(variables);
                break;
            case "mandel":
                func = new MandelExpression(variables);
                break;
            default: throw new IllegalArgumentException("Unrecognised function");
        }
        func.setInputs(inputs);
        return func;
    }
}
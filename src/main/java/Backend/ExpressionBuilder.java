package Backend;

import java.util.List;
import java.util.Map;

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

    /** Used to build FunctionExpression like cos, sin or user-defined functions.
     * Also allows us to incorporate composition of functions (e.g. f(2x)) using the inputs parameter
     * @param funcName The name of a function
     * @param inputs The inputs to the function, likely to be variables but could be more complex
     * @param funcMap A map between the names of functions and their corresponding Expressions
     * @return Returns an expression of the corresponding where the inputs are as given
     */
    public Expression constructExpression(String funcName, Expression[] inputs,
                                          Map<String, FunctionExpression> funcMap){
        FunctionExpression func = funcMap.get(funcName);
        func.setInputs(inputs);
        return func;
    }
}
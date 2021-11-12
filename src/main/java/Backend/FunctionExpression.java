package Backend;

import java.util.List;

/**
 * An abstract class that both builtin functions and user-defined functions inherit from
 */
public abstract class FunctionExpression extends Expression {

    private final String[] variables;
    private Expression[] inputs;
    private ComparatorExpression domain;

    public FunctionExpression(String funcName, String[] variables){
        super(funcName);
        this.variables = variables;
        this.domain = trivialDomain();
        Expression[] inputs = new Expression[variables.length];
        for(int i = 0; i < inputs.length; i++){
            inputs[i] = new VariableExpression(variables[i]);
        }
        this.inputs = inputs;
    }

    public FunctionExpression(String funcName, String[] variables, ComparatorExpression domain){
        this(funcName, variables);
        this.domain = domain;
    }

    private ComparatorExpression trivialDomain(){
        Expression lExpr = new NumberExpression("1");
        Expression rExpr = new NumberExpression("0");
        return new ComparatorExpression(List.of(lExpr, rExpr), List.of(">"));
    }

    public ComparatorExpression getDomain(){
        return this.domain;
    }

    public Expression[] getInputs(){
        return inputs;
    }

    public void setInputs(Expression[] inputs){
        this.inputs = inputs;
    }

    public String[] getVariables(){
        return this.variables;
    }

}

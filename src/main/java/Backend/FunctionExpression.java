package Backend;

import java.util.List;

/**
 * An abstract class that both builtin functions and user-defined functions inherit from
 */
public abstract class FunctionExpression extends Expression {

    private final String[] variables;
    private Expression[] inputs;
    private ComparatorExpression domain;

    /**
     * @param funcName The name of the function
     * @param variables The variables that the function is in terms of
     */
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

    /**
     * @param funcName The name of the function
     * @param variables The variables that the function is in terms of
     * @param domain The domain of a function, if not provided we assume domain to be everything
     */
    public FunctionExpression(String funcName, String[] variables, ComparatorExpression domain){
        this(funcName, variables);
        this.domain = domain;
    }

    /** Used to define the 'default' domain of everything.
     * @return A ComparatorExpression representing "1 > 0" which always evaluates to True. This forms
     * our 'default' domain for functions
     */
    private ComparatorExpression trivialDomain(){
        Expression lExpr = new NumberExpression("1");
        Expression rExpr = new NumberExpression("0");
        return new ComparatorExpression(List.of(lExpr, rExpr), List.of(">"));
    }

    /** Gets the domain of a function
     * @return A ComparatorExpression representing the domain of the function
     */
    public ComparatorExpression getDomain(){
        return this.domain;
    }

    /** Gets the inputs to a function
     * @return An Array of Expressions representing the inputs to a function.
     * In most cases, inputs will be variables, but may be more complex, for example via composition of functions
     */
    public Expression[] getInputs(){
        return inputs;
    }

    /** Sets the inputs to a function
     * @param inputs An array of inputs representing the inputs to a function.
     * In most cases, inputs will be variables, but may be more complex, for example via composition of functions
     */
    public void setInputs(Expression[] inputs){
        this.inputs = inputs;
    }

    /** Gets the variables the function is expressed in terms of
     * @return An array of variables the function is in terms of
     */
    public String[] getVariables(){
        return this.variables;
    }


}

package Backend.Expressions;

/**
 * An abstract class that both builtin functions and user-defined functions inherit from
 */
public abstract class FunctionExpression extends RealValuedExpression {

    private final String[] variables;
    private RealValuedExpression[] inputs;
    private BooleanValuedExpression domain;

    /**
     * @param funcName String representing the name of functions
     * @param variables Array of strings that represent the variables that the function is in terms of
     */
    public FunctionExpression(String funcName, String[] variables){
        super(funcName);
        this.variables = variables;
        this.domain = trivialDomain();
        RealValuedExpression[] inputs = new RealValuedExpression[variables.length];
        for(int i = 0; i < inputs.length; i++){
            inputs[i] = new VariableExpression(variables[i]);
        }
        this.inputs = inputs;
    }

    /**
     * @param funcName String representing the name of functions
     * @param variables Array of strings that represent the variables that the function is in terms of
     * @param domain The domain of a function, if not provided we assume domain to be everything
     */
    public FunctionExpression(String funcName, String[] variables, BooleanValuedExpression domain){
        this(funcName, variables);
        this.domain = domain;
    }

    /** Used to define the 'default' domain of everything.
     * @return A BooleanValuedExpression representing "1 > 0" which always evaluates to True. This forms
     * our 'default' domain for functions
     */
    private BooleanValuedExpression trivialDomain(){
        RealValuedExpression lExpr = new NumberExpression("1");
        RealValuedExpression rExpr = new NumberExpression("0");
        return new ComparatorExpression(">", lExpr, rExpr);
    }

    /** Gets the domain of a function
     * @return A BooleanValuedExpression representing the domain of the function
     */
    public BooleanValuedExpression getDomain(){
        return this.domain;
    }


    /** Sets the domain of a function
     * @param domain BooleanValuedExpression representing the domain of a function
     */
    public void setDomain(BooleanValuedExpression domain){
        this.domain = domain;
    }

    /** Gets the inputs to a function
     * @return An Array of RealValuedExpressions representing the inputs to a function.
     * In most cases, inputs will be variables, but may be more complex, for example via composition of functions
     */
    public RealValuedExpression[] getInputs(){
        return inputs;
    }

    /** Sets the inputs to a function
     * @param inputs An array of RealValuedExpressions representing the inputs to a function.
     * In most cases, inputs will be variables, but may be more complex, for example via composition of functions
     */
    public void setInputs(RealValuedExpression[] inputs){
        this.inputs = inputs;
    }

    /** Gets the variables the function is expressed in terms of
     * @return Array of strings that represent the variables that the function is in terms of
     */
    public String[] getVariables(){
        return this.variables;
    }


}

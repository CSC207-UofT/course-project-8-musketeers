package Backend.Expressions;

/**
 * An abstract class that both builtin functions and user-defined functions inherit from
 */
public abstract class FunctionExpression extends RealValuedExpression {

    private final String[] variables;
    private RealValuedExpression[] inputs;

    /**
     * @param funcName String representing the name of functions
     * @param variables Array of strings that represent the variables that the function is in terms of
     */
    public FunctionExpression(String funcName, String[] variables){
        super(funcName);
        this.variables = variables;
        RealValuedExpression[] inputs = new RealValuedExpression[variables.length];
        for(int i = 0; i < inputs.length; i++){
            inputs[i] = new VariableExpression(variables[i]);
        }
        this.inputs = inputs;
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

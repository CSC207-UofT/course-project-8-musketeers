package Backend.Expressions;

/**
 * An abstract class that both builtin functions and user-defined functions inherit from
 */
public abstract class FunctionExpression extends RealValuedExpression {

    private String[] variables;
    private RealValuedExpression[] inputs;

    /**
     * @param funcName  String representing the name of functions
     * @param variables Array of strings that represent the variables that the function is in terms of
     */
    public FunctionExpression(String funcName, String[] variables) {
        super(funcName);
        this.variables = variables;
        this.inputs = stringArrayToExpressions(variables);
    }

    /**
     * @param vars An array of string representing the variables of a function
     * @return An array of expressions for the variables of a function
     */
    private RealValuedExpression[] stringArrayToExpressions(String[] vars) {
        RealValuedExpression[] expressions = new RealValuedExpression[vars.length];
        for (int i = 0; i < expressions.length; i++) {
            expressions[i] = new VariableExpression(vars[i]);
        }
        return expressions;
    }

    /**
     * Gets the inputs to a function
     *
     * @return An Array of RealValuedExpressions representing the inputs to a function.
     * In most cases, inputs will be variables, but may be more complex, for example via composition of functions
     */
    public RealValuedExpression[] getInputs() {
        return inputs;
    }

    /**
     * Sets the inputs to a function
     *
     * @param inputs An array of RealValuedExpressions representing the inputs to a function.
     *               In most cases, inputs will be variables, but may be more complex, for example via composition of functions
     */
    public void setInputs(RealValuedExpression[] inputs) {
        this.inputs = inputs;
    }

    /**
     * Gets the variables the function is expressed in terms of
     *
     * @return Array of strings that represent the variables that the function is in terms of
     */
    public String[] getVariables() {
        return this.variables;
    }

    /**
     * Sets the variables the function is expressed in terms of
     *
     * @param variables Array of strings that represent the variables that the function is in terms of
     */
    public void setVariables(String[] variables) {
        this.variables = variables;
        this.inputs = stringArrayToExpressions(variables);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(getItem());
        str.append("(");
        for (int i = 0; i < getInputs().length; i++) {

            if (i > 0) {
                str.append(",");
            }
            str.append(getInputs()[i].toString());
        }
        str.append(")");
        return str.toString();

    }

}

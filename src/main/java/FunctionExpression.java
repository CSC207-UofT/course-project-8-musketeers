/**
 * FunctionExpression are for things like cos, sin, sqrt, etc.
 * They can have any number of inputs.
 * See Cos for example
 */
public abstract class FunctionExpression extends Expression {

    private Expression[] inputs;

    public FunctionExpression(String funcName){
        super(funcName);
    }

}

package Backend;

/**
 * An abstract class that both builtin functions and user-defined functions inherit from
 */
public abstract class FunctionExpression extends Expression {

    private final Expression[] inputs;

    public FunctionExpression(String funcName, Expression[] inputs){
        super(funcName);
        this.inputs = inputs;
    }

    public Expression[] getInputs(){
        return inputs;
    }

}

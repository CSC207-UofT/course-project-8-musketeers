package Backend;

/**
 * An abstract class that both builtin functions and user-defined functions inherit from
 */
public abstract class FunctionExpression extends Expression {

    private final Expression[] inputs;
    private ComparatorExpression domain;

    public FunctionExpression(String funcName, Expression[] inputs){
        super(funcName);
        this.inputs = inputs;
        this.domain = (new Constants()).trivialDomain();
    }

    public FunctionExpression(String funcName, Expression[] inputs, ComparatorExpression domain){
        this(funcName, inputs);
        this.domain = domain;
    }

    public ComparatorExpression getDomain(){
        return this.domain;
    }

    public Expression[] getInputs(){
        return inputs;
    }

}

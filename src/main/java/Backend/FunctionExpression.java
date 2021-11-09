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
        this.domain = trivialDomain();
    }

    public FunctionExpression(String funcName, Expression[] inputs, ComparatorExpression domain){
        this(funcName, inputs);
        this.domain = domain;
    }

    private ComparatorExpression trivialDomain(){
        Expression lExpr = new NumberExpression("1");
        Expression rExpr = new NumberExpression("0");
        return new ComparatorExpression(">", lExpr, rExpr);
    }

    public ComparatorExpression getDomain(){
        return this.domain;
    }

    public Expression[] getInputs(){
        return inputs;
    }

}

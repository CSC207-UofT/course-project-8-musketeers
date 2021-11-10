package Backend.Expressions;

/**
 * An abstract class that both builtin functions and user-defined functions inherit from
 */
public abstract class FunctionExpression extends RealValuedExpression { // TODO: Confirm with Rishibh that we agree to have all Build-In functions to be real-valued.

    private final RealValuedExpression[] inputs;
    private BooleanValuedExpression domain;

    public FunctionExpression(String funcName, RealValuedExpression[] inputs){
        super(funcName);
        this.inputs = inputs;
        this.domain = trivialDomain();
    }

    public FunctionExpression(String funcName, RealValuedExpression[] inputs, BooleanValuedExpression domain){
        this(funcName, inputs);
        this.domain = domain;
    }

    private ComparatorExpression trivialDomain(){
        RealValuedExpression lExpr = new NumberExpression("1");
        RealValuedExpression rExpr = new NumberExpression("0");
        return new ComparatorExpression(">", lExpr, rExpr);
    }

    public BooleanValuedExpression getDomain(){
        return this.domain;
    }

    public RealValuedExpression[] getInputs(){
        return inputs;
    }

}

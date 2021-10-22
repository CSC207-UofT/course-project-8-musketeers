package Backend;

/**
 * ComparatorExpression allows for the graphing of regions of the graphing space through an inequality
 * rather than typical equalities.
 */
public class ComparatorExpression extends Expression {

    // if x^2 + 5 >= 0 is to be stored, then expr would store the expression 'x^2 + 5'
    private final Expression expr;

    public ComparatorExpression(Expression expr){
        super(">=");
        this.expr = expr;
    }

    @Override
    public double evaluate(Map<String, Double> arguments) {
        double greaterThan;
        if (expr.evaluate(arguments) >= 1) {greaterThan = 1;}
        else {greaterThan = -1;}

        return greaterThan;
    }
}

package Backend;

import java.util.Map;

/**
 * ComparatorExpression allows for the graphing of regions of the graphing space through an inequality
 * rather than typical equalities.
 */
public class ComparatorExpression extends Expression {

    // if x^2 + 5 >= 0 is to be stored, then expr would store the expression 'x^2 + 5'
    private final Expression lExpression;
    private final Expression rExpression;

    public ComparatorExpression(String comparatorOp, Expression lExpression, Expression rExpression){
        super(comparatorOp);
        this.lExpression = lExpression;
        this.rExpression = rExpression;
    }

    @Override
    public double evaluate(Map<String, Double> arguments) {
        boolean comparisonHolds;
        double pixelValue;

        double lExpressionVal = this.lExpression.evaluate(arguments);
        double rExpressionVal = this.rExpression.evaluate(arguments);

        switch(getItem()){
            case ">=":
                comparisonHolds = lExpressionVal >= rExpressionVal;
                break;
            case "<=":
                comparisonHolds = lExpressionVal <= rExpressionVal;
                break;
            case ">":
                comparisonHolds = lExpressionVal > rExpressionVal;
                break;
            case "<":
                comparisonHolds = lExpressionVal < rExpressionVal;
                break;
            case "==":
                comparisonHolds = lExpressionVal == rExpressionVal;
                break;
            default:
                comparisonHolds = false;
                break;
        }

        if (comparisonHolds) {pixelValue = 1;}
        else {pixelValue = -1;}

        return pixelValue;
    }
}

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
    public float evaluate(Map<String, Float> arguments) {
        boolean comparisonHolds;
        float pixelValue;

        float lExpressionVal = this.lExpression.evaluate(arguments);
        float rExpressionVal = this.rExpression.evaluate(arguments);

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
                // TODO: Should this be the default?
                // Throw exception instead?
                comparisonHolds = false;
                break;
        }

        if (comparisonHolds) {pixelValue = 1;}
        else {pixelValue = -1;}

        return pixelValue;
    }
}

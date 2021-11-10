package Backend.Expressions;

import java.util.Map;

/**
 * ComparatorExpression allows for the graphing of regions of the graphing space through an inequality
 * rather than typical equalities.
 */
public class ComparatorExpression extends BooleanValuedExpression {

    // if x^2 + 5 >= 0 is to be stored, then expr would store the expression 'x^2 + 5'
    private final RealValuedExpression lExpression;
    private final RealValuedExpression rExpression;

    public ComparatorExpression(String comparatorOp, RealValuedExpression lExpression, RealValuedExpression rExpression){
        super(comparatorOp);
        this.lExpression = lExpression;
        this.rExpression = rExpression;
    }

    @Override
    public Boolean evaluate(Map<String, Float> arguments) {
        boolean comparisonHolds;
        // float pixelValue;

        float lExpressionVal = this.lExpression.evaluate(arguments);
        float rExpressionVal = this.rExpression.evaluate(arguments);

        comparisonHolds = switch (getItem()) {
            case ">=" -> lExpressionVal >= rExpressionVal;
            case "<=" -> lExpressionVal <= rExpressionVal;
            case ">" -> lExpressionVal > rExpressionVal;
            case "<" -> lExpressionVal < rExpressionVal;
            case "=" -> lExpressionVal == rExpressionVal;
            default ->
                    // TODO: Should this be the default?
                    // Throw exception instead?
                    false;
        };

//        if (comparisonHolds) {pixelValue = 1;}
//        else {pixelValue = -1;}
//
//        return pixelValue;
        return comparisonHolds;
    }
}

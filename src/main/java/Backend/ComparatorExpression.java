package Backend;

import java.util.List;
import java.util.Map;

/**
 * ComparatorExpression allows for the graphing of regions of the graphing space through an inequality
 * rather than typical equalities.
 */
public class ComparatorExpression extends Expression {

    // if x^2 + 5 >= 0 is to be stored, then expr would store the expression 'x^2 + 5'
    private final List<Expression> expressions;
    private final List<String> ops;

    public ComparatorExpression(List<Expression> expressions, List<String> ops){
        super("Comparators"); //
        this.expressions = expressions;
        this.ops = ops;
    }

    public List<String> getOps(){
        return this.ops;
    }

    public List<Expression> getExpressions(){
        return this.expressions;
    }

    @Override
    public float evaluate(Map<String, Float> arguments) {
        boolean comparisonHolds;
        float pixelValue = 1; // we assume that the comparisons are true by default

        for (int i = 0; i < this.ops.size(); ++i){
            comparisonHolds = evaluateHelper(arguments, this.expressions.get(i), this.ops.get(i),
                    this.expressions.get(i+1));

            // if even a single comparison is false, set pixelValue to -1
            if (!comparisonHolds){
                pixelValue = -1;
                break;
            }
        }

        return pixelValue;
    }

    private boolean evaluateHelper(Map<String, Float> arguments, Expression lExpression, String op,
                                   Expression rExpression){
        boolean comparisonHolds;

        float lExpressionVal = lExpression.evaluate(arguments);
        float rExpressionVal = rExpression.evaluate(arguments);

        switch(op){
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

        return comparisonHolds;
    }
}

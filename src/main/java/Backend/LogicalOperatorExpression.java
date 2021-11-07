package Backend;

import java.util.Map;

public class LogicalOperatorExpression extends OperatorExpression {
    public LogicalOperatorExpression(String operation, Expression lExpression, Expression rExpression) {
        super(operation, lExpression, rExpression);
    }

    @Override
    public float evaluate(Map<String, Float> arguments) {
        boolean trueComparison;

        switch (getItem()){
            case "&":
                trueComparison = this.lExpression.evaluate(arguments) == 1 &&
                        this.rExpression.evaluate(arguments) == 1;
                return booleanToFloat(trueComparison);
            case "|":
                trueComparison = this.lExpression.evaluate(arguments) == 1 ||
                        this.rExpression.evaluate(arguments) == 1;
                return booleanToFloat(trueComparison);
            case "!":
                trueComparison = this.rExpression.evaluate(arguments) == 1;
                return booleanToFloat(trueComparison);
            default:
                return 0.f;
        }
    }

    private float booleanToFloat(boolean trueComparison){
        if (trueComparison) {return 1.f;}
        else {return -1.f;}
    }
}

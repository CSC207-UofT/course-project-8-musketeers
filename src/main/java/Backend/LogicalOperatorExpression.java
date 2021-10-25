package Backend;

import java.util.Map;

public class LogicalOperatorExpression extends OperatorExpression {
    public LogicalOperatorExpression(String operation, Expression lExpression, Expression rExpression) {
        super(operation, lExpression, rExpression);
    }

    @Override
    public double evaluate(Map<String, Double> arguments) {
        boolean trueComparison;

        switch (getItem()){
            case "&":
                trueComparison = this.lExpression.evaluate(arguments) == 1 &&
                        this.rExpression.evaluate(arguments) == 1;
                return booleanToDouble(trueComparison);
            case "|":
                trueComparison = this.lExpression.evaluate(arguments) == 1 ||
                        this.rExpression.evaluate(arguments) == 1;
                return booleanToDouble(trueComparison);
            case "!":
                trueComparison = this.rExpression.evaluate(arguments) == 1;
                return booleanToDouble(trueComparison);
            default:
                return 0.0;
        }
    }

    private double booleanToDouble(boolean trueComparison){
        if (trueComparison) {return 1.0;}
        else {return -1.0;}
    }
}

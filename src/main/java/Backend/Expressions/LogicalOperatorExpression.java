package Backend.Expressions;

import java.util.Map;

// TODO: Can we do "RealVal", "BooleanVal" as interface so that Logicals can extends Operator?
// TODO: An alternative is to add an Binary Operator Interface?
public class LogicalOperatorExpression extends BooleanValuedExpression {
    private final BooleanValuedExpression lExpression;
    private final BooleanValuedExpression rExpression;
    public LogicalOperatorExpression(String operation, BooleanValuedExpression lExpression, BooleanValuedExpression rExpression) {
        super(operation); // TODO: Future have a complete string representation rather than just a function name!
        this.lExpression = lExpression;
        this.rExpression = rExpression;
    }

    @Override
    public Boolean evaluate(Map<String, Float> arguments) {
        boolean trueComparison;

        switch (getItem()) {
            case "&":
                trueComparison = this.lExpression.evaluate(arguments) &&
                        this.rExpression.evaluate(arguments);
                return trueComparison;

            case "|":
                trueComparison = this.lExpression.evaluate(arguments) ||
                        this.rExpression.evaluate(arguments);
                return trueComparison;

//            case "NOT": // TODO: Decide whether to have this as UnaryOperator or PsuedoBinaryOperator? Actually, let's add this in future!
//                trueComparison = this.rExpression.evaluate(arguments);
//                return trueComparison;
            default:
                throw new IllegalStateException("Unrecognized Logical Operator!");

        }
    }

// TODO: Below a helper Louis may need!
//    private float booleanToFloat(boolean trueComparison){
//        if (trueComparison) {return 1.f;}
//        else {return -1.f;}
//    }
}

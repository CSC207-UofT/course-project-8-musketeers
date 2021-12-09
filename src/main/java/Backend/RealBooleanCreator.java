package Backend;

import Backend.Exceptions.InvalidTermException;
import Backend.ExpressionBuilders.BooleanValuedExpressionFactory;
import Backend.ExpressionBuilders.RealValuedExpressionFactory;
import Backend.Expressions.Expression;
import Backend.Expressions.FunctionExpression;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The RealBooleanCreator is an abstract class that is incomplete. This follows the demo from sourcemaking.com. The
 * purpose is to bury implementation details. It is also in the algorithm family which is specified by the interface
 * "ExpressionCreator".
 */
public abstract class RealBooleanCreator implements ExpressionCreator{
    protected final ExpressionPropertyReporter propertyReporter;
    protected final Map<String, FunctionExpression> funcMap = new HashMap<>();
    protected final RealValuedExpressionFactory realValuedExpressionFactory = new RealValuedExpressionFactory();
    protected final BooleanValuedExpressionFactory booleanValuedExpressionFactory = new
            BooleanValuedExpressionFactory();

    /**
     * Constructor for RealBooleanCreatorImp.
     *
     * @param funcMap A map of function names to the functions themselves.
     * @param vc A property reporter that reports properties of list of strings.
     */
    public RealBooleanCreator(Map<String, FunctionExpression> funcMap, ExpressionPropertyReporter vc) {
        for (String funcName : funcMap.keySet()) {
            this.funcMap.put(funcName, funcMap.get(funcName));
        }
        this.propertyReporter = vc;
    }

    /**
     *
     * @param terms        The list of terms as accepted by the create method.
     * @return An Expression wildcard which is the desired expression.
     * "Logical" or "Comparator". Both expressions returned represent <terms> in a valid format which can be graphed.
     * @throws InvalidTermException If terms cannot be interpreted
     */
    public abstract Expression<?> create(List<String> terms) throws InvalidTermException;

}
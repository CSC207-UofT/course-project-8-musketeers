package Backend;

import Backend.Exceptions.InvalidTermException;
import Backend.Expressions.Expression;
import java.util.List;

public interface ExpressionCreator {
    Expression<?> create(List<String> terms) throws InvalidTermException; /* It's fine to have
    non-validity-report-creator class implement this, as checked on online forum */
}
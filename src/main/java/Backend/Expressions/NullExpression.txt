package Backend.Expressions;

import java.util.Map;

// TODO: LIKELY TO DELETE THIS CLASS AND HAVE THE PROGRAM NOT ACCEPTING NULL AS EXPRESSION. ALSO, PUT OFF UNARY OPERATOR FOR NOW.

public class NullExpression extends BooleanValuedExpression{
    public NullExpression(String whatever) {super("NULL");} // TODO: Recheck whether this inherience relationship is appropriate, since we don't need the parameter "whatever".

    @Override
    public Boolean evaluate(Map<String, Float> arguments) {
        return false;
    }
}

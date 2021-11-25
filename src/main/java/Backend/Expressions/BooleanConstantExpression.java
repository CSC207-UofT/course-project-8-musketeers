package Backend.Expressions;

import java.util.Map;

public class BooleanConstantExpression extends BooleanValuedExpression{

    public BooleanConstantExpression(String item){
        super(item);
    }

    @Override
    public Boolean evaluate(Map<String, Float> arguments) {
        switch (getItem()){
            case "true": return true;
            case "false": return false;
            default: throw new IllegalArgumentException("Unrecognised boolean constant: " + getItem());
        }
    }
}

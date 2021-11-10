package Backend.Expressions;

import java.util.Map;

public abstract class Expression<T> {

    // Expressions store their string representation
    private final String item;

    public Expression(String item){
        this.item = item;
    }

    public String getItem() {
        return item;
    }

    // All Expressions can be evaluated
    // The arguments Map tells us what values the variables should take
    public abstract T evaluate(Map<String, Float> arguments);
}

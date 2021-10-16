package Backend;

import java.util.Map;


public abstract class Expression {

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
    public abstract double evaluate(Map<String, Double> arguments);
}

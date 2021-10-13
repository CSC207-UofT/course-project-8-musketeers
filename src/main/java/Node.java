import java.util.Map;

// My 'Node' is equivalent to what we are calling 'Expression' now

public abstract class Node {

    // Each node stores an item, be it a variable name, the operator, function name, etc.
    private final String item;

    public Node(String item){
        this.item = item;
    }

    public String getItem() {
        return item;
    }

    // All Nodes can be evaluated
    // The arguments Map tells us what values the variables should take
    public abstract double evaluate(Map<String, Double> arguments);
}

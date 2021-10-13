import java.util.Map;

public class CosNode extends FunctionNode{

    private Node input;

    // Only one input for cos of course
    // But the input can be an expression of any kind
    public CosNode(Node input){
        super("cos");
        this.input = input;
    }

    @Override
    public double evaluate(Map<String, Double> arguments) {
        return Math.cos(input.evaluate(arguments));
    }
}

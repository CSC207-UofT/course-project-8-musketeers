import java.util.Map;

// OperatorNode stores the 'Expression' with operators, e.g. E_1 + E_2
// where E_1, E_2 themselves are expressions
public class OperatorNode extends Node{

    private final Node lNode;
    private final Node rNode;

    // OperatorNodes store both the operation they correspond to
    // and the expressions to their left and right
    public OperatorNode(String operation, Node lNode, Node rNode){
        super(operation);
        this.lNode = lNode;
        this.rNode = rNode;
    }

    // We evaluate this expression based on what the operator is
    @Override
    public double evaluate(Map<String, Double> arguments) {

        return switch (getItem()) {
            case "+" -> lNode.evaluate(arguments) + rNode.evaluate(arguments);
            case "-" -> lNode.evaluate(arguments) - rNode.evaluate(arguments);
            case "*" -> lNode.evaluate(arguments) * rNode.evaluate(arguments);
            case "/" -> lNode.evaluate(arguments) / rNode.evaluate(arguments);
            case "^" -> Math.pow(lNode.evaluate(arguments), rNode.evaluate(arguments));
            default -> throw new IllegalArgumentException("Unexpected operator");
        };

    }

}

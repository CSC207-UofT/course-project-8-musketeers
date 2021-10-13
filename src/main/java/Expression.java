import java.util.Map;


// TODO: Adding new functions (such as sin or sqrt) for example is kind of a pain
// would be nice to have a better way of doing things

/**
 * Expressions are things that can be evaluated. This includes
 * variables, constants, any function (e.g. cos, sin, sqrt, etc.) of these
 * or any combination of the above using operators (e.g. +, -, *, /)
 *
 */
public class Expression {

    // The 'head' node is the top level node and the last one to be evaluated
    // In most cases this will be an operator (+, -, *, /, ^) with expressions to
    // the left and right which are stored in the Node itself
    // Recall that my 'Node' is equivalent what we're calling 'Expression'
    private final Node head;

    public Expression(Node head){
        this.head = head;
    }

    public Double evaluate(Map<String, Double> parameters){
        return head.evaluate(parameters);
    }

}

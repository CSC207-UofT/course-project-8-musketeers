/**
 * FunctionNodes are for things like cos, sin, sqrt, etc.
 * They can have any number of inputs.
 * See CosNode for example
 */
public abstract class FunctionNode extends Node{

    private Node[] inputs;

    public FunctionNode(String funcName){
        super(funcName);
    }

}

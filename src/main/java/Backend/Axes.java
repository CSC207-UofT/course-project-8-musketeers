package Backend;

// TODO: ADJUST/DECIDE WHAT TO DO WITH "Expression", "RealValuedExpression", "BooleanValuedExpression". USE WILDCARD AND CASTING!!!


import Backend.Expressions.BuiltInFunctions.*;
import Backend.Expressions.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Axes represents the Euclidean Space in which our Expressions will be graphed.
 * - stores a 'scale' attribute (type float)
 * - stores attributes for the x and y coordinates of the origin
 * - collection of Expressions
 * - methods to add/remove functions to the above collection
 * - a getter for the collection of Expressions
 */

public class Axes implements Serializable {
    private float scale;
    private float dimensionSize;
    private float[] origin;
    private final List<RealValuedExpression> exprCollection;
    private final Map<String, FunctionExpression> namedExpressions = initialNamedExpressions();

    //Constructors
    public Axes(){
        this.scale = 1;
        this.dimensionSize = 2;
        this.origin = new float[2];
        this.exprCollection = new ArrayList<>();
    }

    /**
     * Constructor that accepts 3 floats as input
     * @param a The Scale of Axes
     * @param b The x coordinate of the origin
     * @param c The y coordinate of the origin
     */
    public Axes(float a, float b, float c){
        this.scale = a;
        this.origin = new float[]{b, c};
        this.exprCollection = new ArrayList<>();
    }

    /** This is just to find what our initial named functions are, i.e. the ones that are builtin
     * @return A map between the name of a function and the corresponding expression
     */
    private Map<String, FunctionExpression> initialNamedExpressions(){
        String[] oneVarInput = new String[] {"x"};
        String[] twoVarInput = new String[] {"x", "y"};
        return new HashMap<>(Map.of (
                "cos", new Cosine(oneVarInput),
                "sin", new Sine(oneVarInput),
                "tan", new Tangent(oneVarInput),
                "sqrt", new SquareRoot(oneVarInput),
                "mandel", new Mandel(twoVarInput),
                "exp", new Exp(oneVarInput)
        )
        );
    }

    /**
     * Constructor for Axes
     * @param a  the scale parameter
     * @param origin  the origin parameter
     */
    public Axes(float a, float[] origin){
        this.scale = a;
        this.dimensionSize = origin.length;
        this.origin = origin;
        this.exprCollection = new ArrayList<>();
    }



    //Getter and Setter methods for scale, origin:
    public float getScale(){return this.scale;}

    public void setScale(Float scale) {
        this.scale = scale;
    }

    public float[] getOrigin(){return this.origin;}

    public void setOrigin(float x, float y){this.origin = new float[]{x, y};}

    //overload setter for origin. can take an array of floats
    public void setOrigin(float[] p){this.origin = p;}

    public List<RealValuedExpression> getExpressions(){
        return this.exprCollection;
    }

    public void addExpression(RealValuedExpression expr){
        this.exprCollection.add(expr);

        // if a user adds a named function, we want to add it our collection
        if (expr instanceof FunctionExpression){
            namedExpressions.put(expr.getItem(), (FunctionExpression) expr);
        }
    }

    public void removeExpression(RealValuedExpression expr){
        this.exprCollection.remove(expr);

        if (namedExpressions.containsKey(expr.getItem())){
            namedExpressions.remove(expr.getItem(), (FunctionExpression) expr);
        }
    }

    public Map<String, FunctionExpression> getNamedExpressions() {
        return namedExpressions;
    }
}

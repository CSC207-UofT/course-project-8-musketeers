package Backend;


import Backend.Expressions.*;

import java.io.Serializable;
import java.util.*;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

/**
 * Axes represents the Euclidean Space in which our Expressions will be graphed.
 * - stores a 'scale' attribute (type float)
 * - stores attributes for the coordinates of the origin
 * - collection of Expressions
 * - methods to add/remove functions to the above collection
 * - a getter for the collection of Expressions
 */

public class Axes implements Serializable {
    private float scale;
    private final float dimensionSize; // to be used later
    private float[] origin;
    private final List<RealValuedExpression> exprCollection;
    private final Map<String, FunctionExpression> namedExpressions = initialNamedExpressions();

    private final PropertyChangeSupport observable; // For observer design pattern

    //Constructors
    public Axes(){
        this(5, new float[2]);

    }

    /**
     * Constructor that accepts 3 floats as input
     * @param scale The Scale of Axes
     * @param ox The x coordinate of the origin
     * @param oy The y coordinate of the origin
     */
    public Axes(float scale, float ox, float oy){
        this(scale, new float[]{ox, oy});
    }

    /**
     * Constructor for Axes
     * @param scale the scale parameter
     * @param origin  the origin parameter
     */
    public Axes(float scale, float[] origin){
        this.scale = scale;
        this.dimensionSize = origin.length;
        this.origin = origin;
        this.exprCollection = new ArrayList<>();

        this.observable = new PropertyChangeSupport(this);
    }

    /** This is just to find what our initial named functions are, i.e. the ones that are builtin
     * @return A map between the name of a function and the corresponding expression
     */
    private Map<String, FunctionExpression> initialNamedExpressions(){
        Constants constants = new Constants();
        Set<String> builtinFunctions = constants.getBuiltinFunctions();
        Map<String, FunctionExpression> funcMap = new HashMap<>();

        for (String funcName: builtinFunctions){
            FunctionExpression func;
            if (constants.getTwoVarFunctions().contains(funcName)){
                func = new BuiltinFunctionExpression(funcName, new String[]{"x", "y"});
            }
            else{
                func = new BuiltinFunctionExpression(funcName, new String[]{"x"});
            }
            funcMap.put(funcName, func);
        }

        return funcMap;
    }

    /**
     * Add a new observer to observe the changes to this class.
     * @param observer Object that is observing Axes
     */
    public void addObserver(PropertyChangeListener observer) {
        observable.addPropertyChangeListener("funcMap", observer);
    }

    //Getter and Setter methods for scale, origin:
    public float getScale(){return this.scale;}

    public void setScale(float scale){this.scale = scale;}

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

            observable.firePropertyChange("funcMap", null, expr);
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

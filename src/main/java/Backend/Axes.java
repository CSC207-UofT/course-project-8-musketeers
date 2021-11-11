package Backend;

//import java.awt.*;
//import java.awt.geom.Point2D;
import Backend.BuiltinExpressions.CosExpression;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.Map.entry;



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
    private final List<Expression> exprCollection;

//    private final Map<String, Expression> builtinExpr= new HashMap<String, Expression>();


//    Map<String, Expression> builtinExpr = Map.ofEntries(
//            entry("cos", new CosExpression())
//
//    );


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

    public Axes(float a, float[] origin){
        this.scale = a;
        this.dimensionSize = origin.length;
        this.origin = origin;
        this.exprCollection = new ArrayList<>();
    }



    //Getter and Setter methods for scale, origin:
    public float getScale(){return this.scale;}

    public void setScale(Float scale){this.scale = scale;}

    public float[] getOrigin(){return this.origin;}

    public void setOrigin(float x, float y){this.origin = new float[]{x, y};}

    //overload setter for origin. can take an array of floats
    public void setOrigin(float[] p){this.origin = p;}



    public List<Expression> getExpressions(){
        return this.exprCollection;
    }

    public void addExpression(Expression expr){
        this.exprCollection.add(expr);
    }

    public void removeExpression(Expression expr){
        this.exprCollection.remove(expr);
    }






}

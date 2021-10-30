package Backend;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

//        - stores a 'scale' attribute (type float)
//        - stores attributes for the x and y coordinates of the origin
//        - collection of Expressions
//        - a method to add functions to the above collection
//        - a getter for the collection of Expressions
public class Axes {
    private float scale;
    private Point2D.Float origin;
    private final List<Expression> exprCollection; //should be final right??

    public Axes(){
        this.scale = 1;
        this.origin = new Point2D.Float();
        this.exprCollection = new ArrayList<>();

    }
    public Axes(float a, float b, float c){
        this.scale = a;
        this.origin = new Point2D.Float(b, c);
        this.exprCollection = new ArrayList<>();

    }

    //private List<Expression> expressionList = new ArrayList<>();
    public float getScale(){return this.scale;}

    public void setScale(Float scale){this.scale = scale;}

//    public int getxO(){return this.xO;}
//    public int gety0(){return this.y0;}

    public Point2D.Float getOrigin(){return this.origin;}

    public void setOrigin(float x, float y){this.origin = new Point2D.Float(x, y);}
    //overload setter origin. can take a point or 2 ints
    public void setOrigin(Point2D.Float p){this.origin = p;}



    public List<Expression> getExpressions(){
        return this.exprCollection;
    }
    public void addExpression(Expression expr){
        exprCollection.add(expr);
    }



}

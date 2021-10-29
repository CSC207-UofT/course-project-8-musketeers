package Backend;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//        - stores a 'scale' attribute (type float)
//        - stores attributes for the x and y coordinates of the origin
//        - collection of Expressions
//        - a method to add functions to the above collection
//        - a getter for the collection of Expressions
public class Axes {
    private float scale;
    private Point origin;
    private final List<Expression> exprCollection; //should be final right??

    public Axes(){
        this.scale = 1;
        this.origin = new Point();
        this.exprCollection = new ArrayList<>();

    }

    //private List<Expression> expressionList = new ArrayList<>();
    public float getScale(){return this.scale;}

    public void setScale(Float scale){this.scale = scale;}

//    public int getxO(){return this.xO;}
//    public int gety0(){return this.y0;}

    public Point getOrigin(){return this.origin;}

    public void setOrigin(int x, int y){this.origin = new Point(x,y);}
    //overload setter origin. can take a point or 2 ints
    public void setOrigin(Point p){this.origin = p;}



    public List<Expression> getExpressions(){
        return this.exprCollection;
    }
    public void addExpression(Expression expr){
        exprCollection.add(expr);
    }



}

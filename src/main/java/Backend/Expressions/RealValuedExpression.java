package Backend.Expressions;

import Graphics.Evaluatable;

import java.util.HashMap;
import java.util.Map;

public abstract class RealValuedExpression extends Expression<Float> implements Evaluatable {

    private BooleanValuedExpression domain; // defines where expression is defined

    public RealValuedExpression(String num){
        super(num);
        this.domain = trivialDomain();
    }

    public RealValuedExpression(String num, BooleanValuedExpression domain){
        super(num);
        this.domain = domain;
    }

    @Override
    public float evaluate(float x, float y) {
        Map<String, Float> varMap = new HashMap<>();
        varMap.put("x", x);
        varMap.put("y", y);
        if (domain.evaluate(varMap)){
            return evaluate(varMap); // this evaluate is from Expression class
        }
        else{
            return Float.NaN;
        }

    }

    @Override
    public float evaluate(float x) {
        Map<String, Float> varMap = new HashMap<>();
        varMap.put("x", x);
        return evaluate(varMap);
    }

    /** Used to define the 'default' domain of everything.
     * @return A BooleanValuedExpression representing "1 > 0" which always evaluates to True. This forms
     * our 'default' domain for functions
     */
    private BooleanValuedExpression trivialDomain(){
        return new BooleanConstantExpression("true");
    }

    /** Gets the domain of an expression
     * @return A BooleanValuedExpression representing the domain of the expression
     */
    public BooleanValuedExpression getDomain(){
        return this.domain;
    }


    /** Sets the domain of a function
     * @param domain BooleanValuedExpression representing the domain of the expression
     */
    public void setDomain(BooleanValuedExpression domain){
        this.domain = domain;
    }
}


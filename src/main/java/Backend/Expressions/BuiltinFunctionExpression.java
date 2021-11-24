package Backend.Expressions;

import Backend.Constants;

import java.util.Map;

public class BuiltinFunctionExpression extends FunctionExpression{

    public BuiltinFunctionExpression(String funcName){
        super(funcName, new String[]{"x"}); // by default we assume all functions are in one variable

        // TODO: Remove instance of constants
        Constants constants = new Constants();
        // If function have multiple inputs, we fix this
        if (constants.getTwoVarFunctions().contains(funcName)){
            setInputs(twoVarInputs());
        }
    }

    private RealValuedExpression[] twoVarInputs(){
        return new RealValuedExpression[] {new VariableExpression("x"), new VariableExpression("y")};
    }

    @Override
    public Float evaluate(Map<String, Float> arguments) {

        switch (getItem()){
            case "cos": return cosEvaluate(arguments);
            case "sin": return sinEvaluate(arguments);
            case "tan": return tanEvaluate(arguments);
            case "sqrt": return sqrtEvaluate(arguments);
            case "exp": return expEvaluate(arguments);
            case "mandel": return mandelEvaluate(arguments);
            default: throw new IllegalArgumentException("Undefined function: " + getItem());
        }
    }


    private float cosEvaluate(Map<String, Float> arguments) {
        return (float) Math.cos(getInputs()[0].evaluate(arguments));
    }

    private float sinEvaluate(Map<String, Float> arguments) {
        return (float) Math.sin(getInputs()[0].evaluate(arguments));
    }

    private float tanEvaluate(Map<String, Float> arguments) {
        return (float) Math.tan(getInputs()[0].evaluate(arguments));
    }

    private float sqrtEvaluate(Map<String, Float> arguments) {
        return (float) Math.sqrt(getInputs()[0].evaluate(arguments));
    }

    private float expEvaluate(Map<String, Float> arguments) {
        return (float) Math.exp(getInputs()[0].evaluate(arguments));
    }

    private float mandelEvaluate(Map<String, Float> arguments) {
        float cx = getInputs()[0].evaluate(arguments);
        float cy = getInputs()[1].evaluate(arguments);

        float x = 0;
        float y = 0;
        int i;
        for (i = 0; i < 100; i++) {
            if (x*x + y*y > 4) break;
            float xtemp = x*x - y*y + cx;
            y = 2*x*y + cy;
            x = xtemp;
        }
        return i / 100.f;
    }

}

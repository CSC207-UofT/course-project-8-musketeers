package Graphics;

/**
 * This is an interface for evaluatable expressions
 * implemented by RealValuedExpression, EvalExprAdapter
 */

public interface Evaluatable {

    /**
     * @param x a variable
     * @param y another variable
     * @return f(x, y)
     */
    float evaluate(float x, float y);

    /**
     * @param x a variable
     * @return the evaluation of f(x)
     */
    float evaluate(float x);
}
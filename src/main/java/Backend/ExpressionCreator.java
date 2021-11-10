package Backend;

import Backend.Expressions.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionCreator {

    private final Constants constants = new Constants();
    private final ExpressionValidityChecker vc = new ExpressionValidityChecker();
    private final ExpressionBuilder eb = new ExpressionBuilder();

    /** Converts a (valid) expression (represented as a list) into an Backend.Expression
     * @param terms A list of terms in the expression (see below for how they should be broken up
     * @return An Backend.Expression (AST) representation of the expression
     */
    // e.g. x ^ 2 + 5 -> ["x", "^", "2", "+", "5"]
    // e.g. (2) + 3 or 3 + (2) -> ["(", "2", ")", "+", "3"]
    // e.g. cos(x) -> ["cos", "(", "x", ")"]
//    public <T extends Expression> T create(List<String> terms, ) {
//        T returnExpression;
//        List<String> minimalTerms = bracketsReduction(terms);

}
package Backend;

import Backend.Expressions.BooleanValuedExpression;
import Backend.Expressions.Expression;
import Backend.Expressions.RealValuedExpression;
import java.util.ArrayList;
import java.util.List;

public class ExpressionCreator {

    private final Constants constants = new Constants();
    private final ExpressionValidityChecker vc = new ExpressionValidityChecker();
    private final ExpressionBuilder eb = new ExpressionBuilder();

    // TODO: Have one create method which takes realVal and boolVal as helpers. Use JAVA WILDCARD AND (UP/DOWN)CASTING.

    public Expression<?> create(List<String> terms) {
        List<String> minimalTerms = bracketsReduction(terms);
        int minimalTermsSize = minimalTerms.size();

        vc.preCheck(minimalTerms); // A basic current-level, NON-RECURSIVE check for the validity of the expression. // TODO: Precheck will be shared in realVal and boolVal, especially the "InvalidTermException" shouldn't be thrown because of logicals or comparators in realVal as we have the precondition.

        if (containComparatorOrLogical(minimalTerms)) {
            return booleanValuedCreate(minimalTerms);
        }
        else {
            return realValuedCreate(minimalTerms);
        }
    }

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
    // TODO: Below precondition: Should be real-valued expressions, so if there's logicals or comparators, then some exception
    //  will be thrown, or program crashes, depends..
    public RealValuedExpression realValuedCreate(List<String> terms) {
        RealValuedExpression resultingExpression;
        int termsSize = terms.size();

        if (termsSize == 1) {
            String term = terms.get(0);
            vc.singleTermCheck(term);
            resultingExpression = eb.constructExpression(term);
        }

        // TODO: Below should also add User-Defined Function check (function names stored in Axes)
        else if (constants.getBuildInFunctions().contains(terms.get(0)) &&
                containsOuterBrackets(terms.subList(1, termsSize))) { // The second condition is to prevent treating case like "cos(x) + 1" as a semi-base case, where the terms arenot entirely within a function (a semi-base case example: "cos(x + 1^2 - 2sin(x))").
            RealValuedExpression[] inputs = findFunctionInputs(terms);
            resultingExpression = eb.constructExpression(terms.get(0), inputs);
        }

        else {
            vc.arithmeticOperatorCheck(terms); // TODO: Should we make this checker more recursive and less static (i.e. only check for curr level?)
            resultingExpression = createOnRealValuedOperators(terms);
        }

        return resultingExpression;
    }

    // Below precondition: There exists at least one comparator or logical in "terms".
    public BooleanValuedExpression booleanValuedCreate(List<String> terms) {
        BooleanValuedExpression resultingExpression;
        // No base case with minimalTermSize == 0 because "no term => no logical and comparator => realVal".
        // TODO: Base case with incomplete operands!

        // TODO: Convert chained comparators to ... AND/& ...
        List<String> unchainedTerms = unchainComparators(terms); // Only unchain the outer ones.
        vc.logicalOperatorCheck(unchainedTerms); // Only check the outer ones.
        vc.comparatorCheck(unchainedTerms); // Only check the outer ones.
        resultingExpression = createOnBooleanValuedOperators(unchainedTerms);

        return resultingExpression;
    }

    private RealValuedExpression createOnRealValuedOperators(List<String> terms) {
        // TODO TEDTEDTED DO
    }

    private BooleanValuedExpression createOnBooleanValuedOperators(List<String> terms) {
        // TODO TEDTEDTED DO
    }

    private boolean containComparatorOrLogical(List<String> terms) { // TODO: Decide whether only checker for outer ones or everything (including ones within a pair of bracket)?
        // TODO
    }

    private List<String> unchainComparators(List<String> terms) { // Only unchain the outer ones.
        // TODO: Convert chained comparators to ... AND/& ...
    }

    private List<String> bracketsReduction(List<String> terms) {
        List<String> terms_copy = terms;
        while (containsOuterBrackets(terms_copy)) {
            terms_copy = terms_copy.subList(1, terms_copy.size() - 1);
        }
        return terms_copy;
    }

    private boolean containsOuterBrackets(List<String> terms){

        if (terms.size() <= 1){
            return false;
        }

        // Same bracketCounter idea as in getOuterOperators
        // However in this case we want to ensure that we only reach 0
        // when we get to the end of the expression
        // Reaching 0 before then means that the corresponding ')'
        // is in the middle of the expression
        int counter = 0;

        for (int i = 0; i < terms.size() - 1; i++){
            String c = terms.get(i);
            if (c.equals("(")){
                counter += 1;
            } else if (c.equals(")")){
                counter -= 1;
            }

            if (counter == 0){
                return false;
            }
        }
        return true;
    }

    /**
     * @param terms List of terms as accepted by create, assumed to be of the form [func, (, ..., )]
     * @return A list of Expressions where each expression is an input to some function
     */
    private RealValuedExpression[] findFunctionInputs (List<String> terms){
        List<Integer> commaIndices = findCommaIndices(terms);
        // we add the final index (corresponding to ')' )
        // this ensures that between every pair of indices in commaIndices
        // we have an input expression
        commaIndices.add(terms.size() - 1);

        RealValuedExpression[] inputs = new RealValuedExpression[commaIndices.size()];
        // start at 2 because first item if function name and second item is '('
        int startInd = 2;

        for (int i = 0; i < inputs.length; i++){
            RealValuedExpression inputExp = realValuedCreate(terms.subList(startInd, commaIndices.get(i))); // If two commas adjacent to each other (i.e. has nothing in between, then there will be NullExpression thrown, but we should catch it!)
            // TODO: Have changed my mind, now we don't accept NullExpression!
            // TODO: Catch some exception to convert them to some InvalidFunctionInputsException!

            inputs[i] = inputExp;
            startInd = commaIndices.get(i) + 1;
        }

        return inputs;
    }

    /**
     * Assumed to be for inputs like [min, (, x, y, )] but could be used for anything
     * @param terms List of terms as accepted by create
     * @return List of indices corresponding to where the "," character appears
     */
    private List<Integer> findCommaIndices(List<String> terms){
        List<Integer> commaIndices = new ArrayList<>();

        int startInd = 0;

        while (terms.subList(startInd, terms.size()).contains(",")){
            commaIndices.add(terms.indexOf(","));
            startInd = terms.indexOf(",") + 1;
        }

        return commaIndices;
    }

}
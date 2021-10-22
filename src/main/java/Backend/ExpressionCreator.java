package Backend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


// TODO: create method is too long, break into helper methods
// TODO: check validity of expressions
// TODO: Handle functions like cos/sin/sqrt in input
public class ExpressionCreator {

    private final Constants constants = new Constants();


    /** Converts a (valid) expression (represented as a list) into an Backend.Expression
     * @param terms A list of terms in the expression (see below for how they should be broken up
     * @return An Backend.Expression (AST) representation of the expression
     */
    // e.g. x ^ 2 + 5 -> ["x", "^", "2", "+", "5"]
    // e.g. (2) + 3 or 3 + (2) -> ["(", "2", ")", "+", "3"]
    // e.g. cos(x) -> ["cos", "(", "x", ")"]
    public Expression create(List<String> terms){
        Expression returnExpression = null;

        // Base case for the recursion
        // One term means it's a variable, number or a function that takes in some input
        if (terms.size() == 1){
            String term = terms.get(0);
            if (constants.getVariables().contains(term)){
                returnExpression = new VariableExpression(term);
            }
            // Assuming that if we don't have a variable or function, we just have a number
            else{
                returnExpression = new NumberExpression(term);
            }
        }

        else if(constants.getFunctions().contains(terms.get(0)) &&
                containsOuterBrackets(terms.subList(1, terms.size()))) {
            returnExpression = buildBuiltInFunctionExpression(terms);
        }

        // Recursive step
        else {
            // We first find what operators are not inside any brackets
            // (operators inside brackets are dealt with deeper in the recursion)
            // Then we sort them by (reverse) order of precedence to ensure
            // that order of operations is maintained

            // TODO: Don't need to get all the necessary operators, only one with the lowest precedence
            Map<String, Integer> operatorAndIndices = getOuterOperators(terms);

            // This for loop is a bit gratuitous? because there shouldn't be more than one comparison operator anyway?
            // Mainly just to get a working version of ComparisonExpression ready.
            for(String op: constants.getComparisons()){

                if(operatorAndIndices.containsKey(op)) {
                    int opIndex = operatorAndIndices.get(op);

                    List<String> leftTerms = terms.subList(0, opIndex);
                    List<String> rightTerms = terms.subList(opIndex + 1, terms.size());

                    // Via induction, we only need to deal with cases
                    // where the left or right expressions are contained in a pair of brackets
                    // e.g. (2 + 3) * 5
                    // If this is not the case, we will get to such a case recursively
                    if (containsOuterBrackets(leftTerms)) {
                        // we remove the first and last term which we know are '(' and ')' respectively
                        leftTerms = leftTerms.subList(1, leftTerms.size() - 1);
                    }
                    if (containsOuterBrackets(rightTerms)) {
                        rightTerms = rightTerms.subList(1, rightTerms.size() - 1);
                    }

                    // Recursively create expressions for the left and right terms
                    Expression lExpression = create(leftTerms);
                    Expression rExpression = create(rightTerms);

                    returnExpression = new ComparatorExpression(op, lExpression, rExpression);

                    break;
                }
            }

            for(String op: constants.getOperators()){

                if(operatorAndIndices.containsKey(op)) {
                    int opIndex = operatorAndIndices.get(op);

                    List<String> leftTerms = terms.subList(0, opIndex);
                    List<String> rightTerms = terms.subList(opIndex + 1, terms.size());

                    // Via induction, we only need to deal with cases
                    // where the left or right expressions are contained in a pair of brackets
                    // e.g. (2 + 3) * 5
                    // If this is not the case, we will get to such a case recursively
                    if (containsOuterBrackets(leftTerms)) {
                        // we remove the first and last term which we know are '(' and ')' respectively
                        leftTerms = leftTerms.subList(1, leftTerms.size() - 1);
                    }
                    if (containsOuterBrackets(rightTerms)) {
                        rightTerms = rightTerms.subList(1, rightTerms.size() - 1);
                    }

                    // Recursively create expressions for the left and right terms
                    Expression lExpression = create(leftTerms);
                    Expression rExpression = create(rightTerms);

                    returnExpression = new OperatorExpression(op, lExpression, rExpression);

                    break;
                }
            }
        }

        return returnExpression;
    }

    private Expression buildBuiltInFunctionExpression(List<String> terms){
        Expression innerExpression = create(terms.subList(2, terms.size() - 1));
        switch (terms.get(0)){
            case "cos": return new CosExpression(innerExpression);
            case "sin": return new SinExpression(innerExpression);
            case "tan": return new TanExpression(innerExpression);
            case "sqrt": return new SqrtExpression(innerExpression);
            default: throw new IllegalArgumentException("Unrecognised function");
        }
    }

    /** Returns a map of operators that are not in any brackets (in the order that they appear)
     *  along with the indices that they appear at.
     *  If multiple instances of the same operator are present (outside any brackets),
     *  then only the first appearance is noted
     * @param terms The list of terms as accepted by the create method
     *              e.g. ["2", "*", "(", "5", "+", "6", ")", "-", "9"]
     * @return The list of operators that are not in any brackets. For the example above, we get
     *              {"*": 1, "-": 7}
     */
    private Map<String, Integer> getOuterOperators(List<String> terms){

        Map<String, Integer> operatorAndIndex= new HashMap<>();

        // We use the bracketCounter to track whether we are inside
        // a pair of brackets or not
        int bracketCounter = 0;

        // We iterate over the terms, if we encounter ')', we increment counter
        // by 1 and if we encounter '(' we decrement it by 1
        // Thus we know we are outside every pair of brackets when counter is 0
        // We need to go in reverse order as the operators at the end
        // have lower precedence and those up ahead.
        // e.g. 2 - 1 - 3 == (2 - 1) - 3 != 2 - (1 - 3)
        for (int i = terms.size() - 1; i > -1; i--){
            String term = terms.get(i);

            if (term.equals(")")){
                bracketCounter += 1;
            } else if (term.equals("(")){
                bracketCounter -= 1;
            }

            if (bracketCounter == 0){
                if (constants.getOperators().contains(term) &&
                        !operatorAndIndex.containsKey(term)){
                    operatorAndIndex.put(term, i);
                }

                else if (constants.getComparisons().contains(term) &&
                        !operatorAndIndex.containsKey(term)){
                    operatorAndIndex.put(term, i);
                }
            }
        }

        return operatorAndIndex;
    }


    /**
     * @param terms The list of terms as accepted by the create method
     *              e.g. ["2", "*", "(", "5", "+", "6", ")", "-", "9"]
     * @return Returns True if and only if the terms are entirely within a pair of brackets
     *         ["(", "4", "+", "3", ")"] -> true (this list represents "(4 + 3)")
     *         ["(", "4", "), "+", "(", "3", ")"] -> false (this list represents "(4) + (3)")
     */
    private boolean containsOuterBrackets(List<String> terms){

        if (terms.size() == 1){
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

    private int findCorrespondingBracket(List<String> terms, int startInd){
        int bracketCounter = 0;

        for (int i = startInd; i < terms.size(); i++){
            if (terms.get(i).equals("(")){
                bracketCounter += 1;
            } else if (terms.get(i).equals(")")){
                bracketCounter -= 1;
            }

            if (bracketCounter == 0){
                return i;
            }
        }

        return -1;
    }

}

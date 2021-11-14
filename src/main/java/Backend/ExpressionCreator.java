package Backend;

import java.util.*;


/**
 * This class is used to create Expressions after it has been parsed by ExpressionReader into an
 * appropriate List of strings, in particular each item in the List should correspond to one 'unit'
 * of information.
 */
// TODO: create method is too long, break into helper methods
// TODO: check validity of expressions
public class ExpressionCreator {

    private final Constants constants = new Constants();
    private final ExpressionBuilder eb = new ExpressionBuilder();
    private final Axes axes;
    // TODO: We can likely use the Observer design pattern to remove dependency on Axes

    public ExpressionCreator(){
        this.axes = new Axes();
    }

    public ExpressionCreator(Axes axes){
        this.axes = axes;
    }

    /** Converts a (valid) expression (represented as a list) into an Expression
     * @param terms A list of terms in the expression (see below for how they should be broken up
     * @return An Expression (AST) representation of the expression
     */
    // e.g. x ^ 2 + 5 -> ["x", "^", "2", "+", "5"]
    // e.g. (2) + 3 or 3 + (2) -> ["(", "2", ")", "+", "3"]
    // e.g. cos(x) -> ["cos", "(", "x", ")"]
    public Expression create(List<String> terms) {
        Expression returnExpression;


        // Base case for the recursion
        // One term means it's a variable, number or a function that takes in some input
        if (terms.size() == 1) {
            String term = terms.get(0);
            returnExpression = this.eb.constructExpression(term);
        }

        // check that we have only one expression that we are composing with our built-in functions
        else if (axes.getNamedExpressions().containsKey(terms.get(0)) &&
                containsOuterBrackets(terms.subList(1, terms.size()))) {
//            System.out.println(terms.get(0));
//            System.out.println(terms);
            Expression[] inputs = findFunctionInputs(terms.subList(2, terms.size() - 1));
            returnExpression = this.eb.constructExpression(terms.get(0), inputs, axes.getNamedExpressions());
        }

        // Recursive step
        else {
            // We first find what operators are not inside any brackets
            // (operators inside brackets are dealt with deeper in the recursion)
            // Then we sort them by (reverse) order of precedence to ensure
            // that order of operations is maintained

            // TODO: Don't need to get all the necessary operators, only one with the lowest precedence
            Map<String, List<Integer>> operatorAndIndices = getOuterOperators(terms);

            // Go through the different types of operators in reverse order of precedence.
            // TODO: use a better empty expression representation than null
            returnExpression = createExpressionRecursiveHelper("Logical", operatorAndIndices, terms);
            if (returnExpression == null) {
                // for comparator expressions
                returnExpression = createChainedExpressionRecursiveHelper(operatorAndIndices, terms);
            }
            if (returnExpression == null) {
                returnExpression = createExpressionRecursiveHelper("Operator", operatorAndIndices, terms);
            }
        }

        return returnExpression;
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
    private Map<String, List<Integer>> getOuterOperators(List<String> terms){

        Map<String, List<Integer>> operatorAndIndex = new HashMap<>();

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
                // If term is an operator/logical operator and operatorAndIndex doesn't already have an index for
                // that operator stored.
                if (!operatorAndIndex.containsKey(term) && constants.getOperators().contains(term)
                        || constants.getLogicalOperators().contains(term)){
                    // if term is an operator or logical operator
                    operatorAndIndex.put(term, List.of(i));
                }
                // if term is a comparator
                else if (constants.getComparators().contains(term)){
                    if (operatorAndIndex.containsKey("Comparators")){
                        List<Integer> comparatorIndices = new ArrayList<>(operatorAndIndex.get("Comparators"));
                        comparatorIndices.add(0, i);
                        operatorAndIndex.put("Comparators", comparatorIndices);
                    }
                    else{
                        operatorAndIndex.put("Comparators", List.of(i));
                    }
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

        if (terms.size() == 1){ return false; }

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
     * @param expressionType The type of Expression that needs to be created. Can only be 'Operator', 'Comparator'
     *                       or 'Logical'.
     * @param operatorAndIndices A Map whose keys are either operators/logical operators (e.g. '+', '-') or the
     *                           String "Comparator", which contains the indices of all comparators not contained in
     *                           brackets. This method doesn't construct ComparatorExpression objects and so ignores
     *                           this "Comparator" key.
     * @param terms The list of terms representing an expression for which to create an Expression object.
     * @return Returns the Expression object corresponding to terms.
     */
    private Expression createExpressionRecursiveHelper(String expressionType,
                                                       Map<String, List<Integer>> operatorAndIndices,
                                                       List<String> terms) {
        // TODO: javadoc
        List<String> candidateOperators;
        switch (expressionType) {
            case "Operator":
                candidateOperators = constants.getOperators();
                break;
            case "Logical":
                candidateOperators = constants.getLogicalOperators();
                break;
            default:
                // TODO: raise exception here instead
                return null;
        }

        for (String op : candidateOperators) {
            if (operatorAndIndices.containsKey(op)) {
                // when op is an operator or logical operator, operatorAndIndices.get(op) can only contain one index
                // as designed in the getOuterOperators method
                int opIndex = operatorAndIndices.get(op).get(0);

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

                return this.eb.constructExpression(lExpression, op, rExpression);
            }
        }

        return null;
    }

    /**
     * @param operatorAndIndices A Map whose keys are either operators/logical operators (e.g. '+', '-') or the
     *                           String "Comparator", which contains the indices of all comparators not contained in
     *                           brackets. This method only constructs ComparatorExpressions right now, so it only
     *                           uses this "Comparator" key.
     * @param terms The list of terms representing an expression for which to create an Expression object.
     * @return Returns the Expression object corresponding to terms.
     */
    private Expression createChainedExpressionRecursiveHelper(Map<String, List<Integer>> operatorAndIndices,
                                                              List<String> terms) {
        if (operatorAndIndices.containsKey("Comparators")) {
            List<Integer> opIndices = operatorAndIndices.get("Comparators");

            List<List<String>> subExpressionLists = new ArrayList<>();
            // add the sublist between the start of terms and the first comparator
            subExpressionLists.add(terms.subList(0,opIndices.get(0)));
            for (int i=0; i < opIndices.size() - 1; ++i){
                // extract sublists between adjacent comparators
                List<String> subExpressionList = terms.subList(opIndices.get(i) + 1, opIndices.get(i + 1));
                subExpressionLists.add(subExpressionList);
            }
            // add the sublist between the last comparator and the end of terms
            List<String> lastSublist = terms.subList(opIndices.get(opIndices.size() - 1) + 1, terms.size());
            subExpressionLists.add(lastSublist);

            // Via induction, we only need to deal with cases
            // where the left or right expressions are contained in a pair of brackets
            // e.g. (2 + 3) * 5
            // If this is not the case, we will get to such a case recursively
            List<Expression> subExpressions = new ArrayList<>();
            for (List<String> subExpressionList: subExpressionLists){
                if (containsOuterBrackets(subExpressionList)){
                    // we remove the first and last term which we know are '(' and ')' respectively
                    subExpressionList = subExpressionList.subList(1, subExpressionList.size() - 1);
                }
                // create Expression from each sublist
                subExpressions.add(create(subExpressionList));
            }

            List<String> comparatorOps = new ArrayList<>();
            for (int index: opIndices){
                comparatorOps.add(terms.get(index));
            }

            return this.eb.constructExpression(subExpressions, comparatorOps);
        }

        return null;
    }


    /**
     * @param inputStrings List of inputs to a function.
     *                     e.g. for "mandel(x, y)", it would be ["x", ",", "y"]
     * @return A list of Expressions where each expression is an input to some function
     */
    private Expression[] findFunctionInputs (List<String> inputStrings){
        List<Integer> commaIndices = findOuterCommaIndices(inputStrings);

        // We add an index for the very end of the list
        // this ensures that between every pair of indices in commaIndices
        // we have an input expression
        commaIndices.add(inputStrings.size());

        Expression[] inputs = new Expression[commaIndices.size()];

        int startInd = 0;
        for (int i = 0; i < inputs.length; i++){
            inputs[i] = create(inputStrings.subList(startInd, commaIndices.get(i)));
            startInd = commaIndices.get(i) + 1; // + 1 to skip over the index of the comma itself
        }

        return inputs;
    }


    /**
     * Assumed to be for inputs like [min, (, x, y, )] but could be used for anything
     * @param terms List of inputs to some function, separated by commas
     * @return List of indices corresponding to where the "," string appears
     */
    private List<Integer> findOuterCommaIndices(List<String> terms){
        List<Integer> commaIndices = new ArrayList<>();
        // TODO: Repeated code from findOuterOperators, factor out common code
        int counter = 0;

        for (int i = 0; i < terms.size(); i++){
            String c = terms.get(i);
            if (c.equals("(")){
                counter += 1;
            } else if (c.equals(")")){
                counter -= 1;
            }

            if (counter == 0){
                if (terms.get(i).equals(",")){
                    commaIndices.add(i);
                }
            }

        }

        return commaIndices;
    }
}

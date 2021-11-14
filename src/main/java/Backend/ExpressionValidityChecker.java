package Backend;

import Backend.Exceptions.BaseCaseCreatorException;
import Backend.Exceptions.CompoundCaseCreatorException;
import Backend.Exceptions.InvalidTermException;

import java.util.*;


public class ExpressionValidityChecker {
    Constants constants;
    Set<String> validFuncs;
    public ExpressionValidityChecker(Set<String> validFuncs) {
        this.constants = new Constants();
        this.validFuncs = validFuncs;
    }

    public void preCheck(List<String> terms) throws InvalidTermException {
        if (terms.size() == 0) {
            throw new BaseCaseCreatorException("NullExpressionException!");
        }
        else if (terms.size() == 1) { // Important (e.g. for "findFunctionInputs" and "createOnOperators" to catch the
            // correct exception (BaseCaseException rather than potentially InvalidTermException)).
            String term = terms.get(0);
            if (!(checkNumber(term) | constants.getVariables().contains(term))) {
                throw new BaseCaseCreatorException("InvalidSingleExpressionException!");
            }
        }
        else{ // TODO: Perhaps below have the "check..." to throw exceptions to avoid (if/else-if) blocks?
            if (!checkAllTermsValid(terms)) {
                throw new CompoundCaseCreatorException("InvalidTermException!");
            } else if (!checkMatchingBrackets(terms)) {
                throw new CompoundCaseCreatorException("UnmatchedBracketsException!");
            } else if (!checkFunctionBrackets(terms)) {
                throw new CompoundCaseCreatorException("FunctionBracketsException!");
            }
            else if (!checkCommasWithinFunctions(terms)) {
                throw new CompoundCaseCreatorException("CommasNotWithinFunctions!");
            }
            // TODO: Recheck whether above one block is redundant because of the below the newly added one block.
            //  Furthermore, check whether above would have been regarded as "InvalidOperandException!" before?

            // TODO: Below one block may be redundant as it's likely to work without it thanks to "ExpressionReader", as it
            //  promises certain preconditions. But this is way safer.
            else if (!checkMultipleTermsConnection(terms)) {
                throw new CompoundCaseCreatorException("NonConnectedMultipleTermsException!");
            } else if (!checkFunctionInputSize(terms)) {
                throw new CompoundCaseCreatorException("FunctionInputsSizeException!");
            }
        }
    }

    // Don't need the three recursive checker since we implicitly have them checked in "ExpressionCreator" already!

    // Below method: First input is one (left or right) operand, and the second input is the operator type.
    public void operandsTypeCheck(List<String> leftTerms, String operatorType, List<String> rightTerms) throws CompoundCaseCreatorException {
        switch (operatorType) {
            case "Logical": {
                if (!(containsOperator(leftTerms, "Logical") || containsOperator(rightTerms, "Logical"))) {
                    throw new CompoundCaseCreatorException("OperandTypeException!");
                }
                break;
            }
            case "Comparator": {
                if (!(containsOperator(leftTerms, "Comparator") || containsOperator(rightTerms, "Comparator"))) {
                    throw new CompoundCaseCreatorException("OperandTypeException!");
                }
                break;
            }
            case "Arithmetic": {
                if (containsOperator(leftTerms, "Comparator") ||
                        containsOperator(leftTerms, "Logical") ||
                        containsOperator(rightTerms, "Comparator") ||
                        containsOperator(rightTerms, "Logical")) {
                    throw new CompoundCaseCreatorException("OperandTypeException!");
                }
                break;
            }
            // In theory, this should be thrown
            default:
                throw new IllegalStateException("Unrecognized Operator Type!");
        }
    }

    private boolean checkNumber(String term) { // TODO: Is this good practice by using RuntimeException?
        try {
            Double num = Double.parseDouble(term); // Just check for whether "term" represents a number.
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // TODO: Use "findCorrespondingBracket" helper to make this method more recursive. Or NO? Since
    //  "checkMatchingBrackets" might have to be static?! Because many recursive helpers rely on matching brackets as
    //  they use the helper "findCorrespondingBraket"? Otherwise we'd have to handle many exceptions.
    private boolean checkAllTermsValid(List<String> terms) {
        for (int i = 0; i <= terms.size() - 1; i++) {
            String term = terms.get(i);

            if (!(checkNumber(term) |
                    constants.getVariables().contains(term) |
                    constants.getAllOperators().contains(term) |
                    validFuncs.contains(term) |
                    constants.getSpecialCharacters().contains(term))
            ) { return false; }
        }
        return true;
    }

    private boolean checkMatchingBrackets(List<String> terms) {
        int counter = 0;
        int index = 0;
        while (counter >= 0 && index <= terms.size() - 1) {
            String curr = terms.get(index);
            if (curr.equals("(")) {
                counter++;
            }
            else if (curr.equals(")")) {
                counter--;
            }
            index++;
        }
        return counter == 0;
    }

    // Below one helper's precondition: checkMatchingBrackets(terms) is true.
    private boolean checkFunctionBrackets(List<String> terms) { // TODO: Check correctness!
        Map<String, List<Integer>> functionsAndIndexLists = getOuterItems(terms, new ArrayList<>(validFuncs));
        for (List<Integer> indices: functionsAndIndexLists.values()) {
            for (Integer index: indices) {
                // Below only checks for whether it's possible to have two brackets after the function, but doesn't care
                // whether function inputs are correct.
                if (index >= terms.size() - 2) {
                    return false;
                } else if (!((terms.get(index + 1).equals("(")))) { // TODO: Recheck ".equals" and "==" in Java.
                    return false;
                } // This works thanks to the "precheck" (the precondition).
            }
        }
        return true;
    }

    private boolean checkCommasWithinFunctions(List<String> terms) {
        // TODO: WARNING: Be careful in future that a comma can be outside, or inside of function brackets, or inside of any other brackets...
        return getOuterItems(terms, List.of(new String[]{","})).isEmpty();
    }

    // TODO: Support User-Defined Functions!
    private boolean checkFunctionInputSize(List<String> terms) {

        Map<String, List<Integer>> functionsAndIndexLists = getOuterItems(terms, new ArrayList<>(validFuncs));
        List<String> functionInputTerms;
        Map<String, List<Integer>> commaAndIndexLists;
        int size;

        for (List<Integer> indices: functionsAndIndexLists.values()) {
            for (Integer index: indices) {
                functionInputTerms = terms.subList(index + 1, findCorrespondingBracket(terms, index + 1) + 1);

                if (getOuterItems(functionInputTerms, List.of(new String[]{","})).size() == 0) {
                    size = 0;
                }
                else {
                    commaAndIndexLists = getOuterItems(functionInputTerms, List.of(new String[]{","}));
                    size = commaAndIndexLists.get(",").size();
                }

                if (constants.getBuiltInFunctionsAndInputSizes().get(terms.get(index)) - 1 != size) {
                    return false;
                }
            } // This works thanks to checkers in "precheck" that is done before this checker.
        }
        return true;
    }

    private boolean checkMultipleTermsConnection(List<String> terms) { // TODO: Recheck correctness (logically)!
        if (!(validFuncs.contains(terms.get(0)) &&
                enclosedByOuterBrackets(terms.subList(1, terms.size())))) { // The second condition is to prevent treating case like "cos(x) + 1" as a semi-base case, where the terms are not entirely within a function (a semi-base case example: "cos(x + 1^2 - 2sin(x))").
            return !(getOuterItems(terms, constants.getAllOperators()).isEmpty());
        }
        return true;
    }

    // TODO: Update below method documentation!
    /** Returns a map of operators that are not in any brackets (in the order that they appear)
     *  along with the indices that they appear at.
     *  If multiple instances of the same operator are present (outside any brackets),
     *  then only the first appearance is noted
     * @param terms The list of terms as accepted by the create method
     *              e.g. ["2", "*", "(", "5", "+", "6", ")", "-", "9"]
     * @param items Can only be one of the following three
     * @return The list of operators that are not in any brackets. For the example above, we get
     *              {"*": 1, "-": 7}.
     */
    // TODO: Update the second parameter to "Collection<String>" type. Similarly for all other helpers!!!
    public Map<String, List<Integer>> getOuterItems(List<String> terms, List<String> items) { // TODO: Change the implementation to using "findCorrespondingBracket"!
        Map<String, List<Integer>> operatorsAndIndices = new HashMap<>();
        int bracketCounter = 0;
        for (int i = terms.size() - 1; i > -1; i--){
            String term = terms.get(i);

            if (term.equals(")")){
                bracketCounter += 1;
            } else if (term.equals("(")){
                bracketCounter -= 1;
            }

            if (bracketCounter == 0){
                if (items.contains(term) && !operatorsAndIndices.containsKey(term)) {
                    operatorsAndIndices.put(term, List.of(i));
                }
                else if (items.contains(term) && operatorsAndIndices.containsKey(term)) {
                    operatorsAndIndices.get(term).add(i);
                }
            }
        }
        return operatorsAndIndices;

    }

    // TODO: IMPORTANT!!! ADD BELOW PRECONDITION 2 TO MOST HELPER IN "ExpresionValidityChecker" and "ExpressionCreator"!
    // Below precondition:
    //     1. terms.get(index) must be either "(" or ")" to derive the right functionality it promises.
    //     2. Use this helper before ensuring that checkMatchingBrackets(terms) is true.
    private int findCorrespondingBracket(List<String> terms, int index) {
        int Counter = 0;
        boolean initialState = true;
        boolean rightTraverse = true;

        while (0 <= index && index <= terms.size() - 1){
            // Below section initializes the traversal direction.
            if (initialState) {
                if (terms.get(index).equals(")")) { rightTraverse = false; }
                initialState = false;
            }

            // Below section tries to find the corresponding bracket.
            if (terms.get(index).equals("(")){ Counter += 1; }
            else if (terms.get(index).equals(")")){ Counter -= 1; }

            if (Counter == 0){ return index; }

            // Below section decides the traversal direction.
            if (rightTraverse) { index++; }
            else { index--; }
        }
        throw new IllegalArgumentException("Precondition Violated When Finding The Corresponding Bracket!");
    }

    public boolean enclosedByOuterBrackets(List<String> terms){ // TODO: Could have used "fidnCorrespondingBracket" to improve readability but unsure if it's a good thing to exert precondition (that checkMatchingBrackets(terms) evaluates to true)).

        if (terms.size() <= 1){
            return false;
        }

        // We want the "counter" to reach to 0 IFF we get to the end of the expression (return true, and false
        // otherwise). Reaching 0 before means that the corresponding ')' is in the middle of the expression
        int counter = 0;

        for (int i = 0; i <= terms.size() - 1; i++){
            String c = terms.get(i);
            if (c.equals("(")){
                counter += 1;
            } else if (c.equals(")")){
                counter -= 1;
            }

            if (counter == 0 && i < terms.size() - 1){
                return false;
            }
        }
        return counter == 0;
    }

    // TODO: IMPORTANT! Now that we want all checkers to be recursive rather than static, so maybe using the
    //  "findCorrespondingBracket" helper would help with the implementation of all these checkers to skip index! To
    //  reduce runtime! Or no because "findCorrespondingBracket" helper also takes much runtime! Or maybe yes, since
    //  runtime is likely to be the same, and we improve code readability and reduce the likelihood of bugs.

    // TODO: Decide whether only checker for outer ones or everything (including ones within a pair of bracket)?
    // Below for now only check for comparator and logical operator. In case we want more, we just add another case.
    // I think for now let's do a THROUGHOUT/STATIC check, highly likely to do the same in future though.
    public boolean containsOperator(List<String> terms, String operatorType) {
        List<String> operators; // TODO: Confirm with Rishibh whether we should have all constants in list form? Be careful if it is a Set in Constants.
        switch (operatorType) {
            case "Logical": {
                operators = constants.getLogicalOperators();
                break;
            }
            case "Comparator": {
                operators = constants.getComparators();
                break;
            }
            default:
                // In theory this should really never be run, hence why we have a RunTimeException
                throw new IllegalStateException("Unrecognized Operator Type!");
        }

        for (String term: terms) { // This way saves expected runtime by a lot!
            for (String op: operators) {
                if (term.equals(op)) { // TODO: Recheck Java "equals" and "==".
                    return true;
                }
            }
        }
        return false;
    }

}


























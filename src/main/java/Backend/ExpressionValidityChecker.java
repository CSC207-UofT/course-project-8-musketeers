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

    /** A preliminary check that throws an exception if the input expression from the user is invalid in specific ways.
     *
     * @param terms The parsed expression input by the user.
     * @throws InvalidTermException
     */
    /** A preliminary check that throws an exception if the input expression from the user is invalid in specific ways.
     *
     * @param terms The parsed expression input by the user.
     * @throws InvalidTermException
     */
    public void preCheck(List<String> terms) throws InvalidTermException {
        if (terms.size() == 0) {
            throw new BaseCaseCreatorException("NullExpressionException!");
        }
        else if (terms.size() == 1) { // Important (e.g. for <findFunctionInputs> and <createOnOperators> to catch the
            // correct exception (BaseCaseException rather than potentially InvalidTermException)).
            String term = terms.get(0);
            if (!(checkNumber(term) | constants.getVariables().contains(term))) {
                throw new BaseCaseCreatorException("InvalidSingleExpressionException!");
            }
        }
        else{ // TODO: Perhaps below have the "check..." to throw exceptions to avoid (if/else-if) blocks?
            if (!checkAllTermsValid(terms)) { //check if all terms are terms we can interpret.
                throw new CompoundCaseCreatorException("InvalidTermException!");
            } else if (!checkMatchingBrackets(terms)) { //Check if each bracket has a corresponding bracket and we dont
                                                        // any extra brackets.
                throw new CompoundCaseCreatorException("UnmatchedBracketsException!");
            } else if (!checkFunctionBrackets(terms)) { //Check that brackets
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

    // Don't need the three recursive checker since we implicitly have them checked in <ExpressionCreator> already!

    // Below method: First input is one (left or right) operand, and the second input is the operator type.

    public void operandsTypeCheck(List<String> leftTerms, String operatorType, List<String> rightTerms) throws CompoundCaseCreatorException {

        switch (operatorType) {
            case "Logical": {
                if (!((containsOperator(leftTerms, "Comparator") ||
                        containsOperator(leftTerms, "Logical")) &&
                        (containsOperator(rightTerms, "Comparator") ||
                                containsOperator(rightTerms, "Logical")))){
                    throw new CompoundCaseCreatorException("OperandTypeException!");
                }
                break;
            }
            case "Arithmetic": case "Comparator": {
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

    public void realValuedPreconditionCheck(List<String> terms) throws CompoundCaseCreatorException {
        if (containsOperator(terms, "Logical") || containsOperator(terms, "Comparator")) {
            throw new CompoundCaseCreatorException("NotRealValuedExpressionException!");
        }
    }

    /**
     *
     * @param term A single term from the parsed expression.
     * @return True if term represents a double, False otherwise.
     */
    private boolean checkNumber(String term) { // TODO: Is this good practice by using RuntimeException?
        try {
            Float.parseFloat(term); // Just check for whether "term" represents a number.
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // TODO: Use "findCorrespondingBracket" helper to make this method more recursive. Or NO? Since
    //  "checkMatchingBrackets" might have to be static?! Because many recursive helpers rely on matching brackets as
    //  they use the helper "findCorrespondingBraket"? Otherwise we'd have to handle many exceptions.

    /**
     *
     * @param terms The list representing parsed expression representing the user's input.
     * @return True if all terms in the expression are valid terms, False otherwise. (e.g. Returns false if "cap"
     * is a term in the list)
     */
    private boolean checkAllTermsValid(List<String> terms) {
        for (int i = 0; i <= terms.size() - 1; i++) {
            String term = terms.get(i);

            if (!(checkNumber(term) | // If a term is not a number,
                    constants.getVariables().contains(term) | // or a variable,
                    constants.getAllOperators().contains(term) | // or an operator,
                    constants.getBuiltInFunctions().contains(term) | // or a built in function
                    constants.getSpecialCharacters().contains(term)) // or a special character which is accepted,
            ) { return false; } //then return false as its not something we can interpret.
        }
        return true;
    }


    /**
     *
     * @param terms The list representing parsed expression representing the user's input.
     * @return True if and only if each open bracket "(" has a corresponding  closed bracket ")" after it,
     * and every closed bracket ")" has a corresponding open bracket "(" before it.
     */
    private boolean checkMatchingBrackets(List<String> terms) {
        int counter = 0;
        int index = 0;
        // counter is equal to "number of open brackets - number of closed brackets" in terms before the index'th term.
        while (counter >= 0 && index <= terms.size() - 1) { // end the loop if counter becomes negative.
                                                            // That means theres a closed bracket without a corresponding
                                                            // open bracket before it.
            String curr = terms.get(index);
            if (curr.equals("(")) {
                counter++;
            }
            else if (curr.equals(")")) {
                counter--;
            }
            index++;
        }
        return counter == 0; // If negative, it is as explained above. If positive, then theres an open bracket without
                            // a corresponding closed bracket after it. If 0, then each bracket has a matching bracket.
    }


    /** Check that, if a function is called in the expression, then it is immediately succeeded by an open bracket.
     * By the following precondition, this ensures theres a corresponding closed bracket.
     * Precondtion: CheckMatchingBrackets is called before this funcion is called, and it returns True.
     *
     * @param terms The list representing parsed expression representing the user's input.
     * @return True if and only if a function call in <terms> has brackets where may find its inputs.
     * Example: ["cos","(",")"] returns True, ["cos","x"] returns False.
     */
    private boolean checkFunctionBrackets(List<String> terms) { // TODO: Check correctness!
        Map<String, List<Integer>> functionsAndIndexLists = getOuterItems(terms, new ArrayList<>(validFuncs));
        for (List<Integer> indices: functionsAndIndexLists.values()) {
            for (Integer index: indices) {
                // Below only checks for whether it's possible to have two brackets after the function, but doesn't care
                // whether function inputs are correct.
                if (index >= terms.size() - 2) {
                    return false;
                } else if (!((terms.get(index + 1).equals("(")))) {
                    return false;
                } // This works thanks to the "precheck" (the precondition).
            }
        }
        return true;
    }

    /**Checks that, if any commas exist in the list of terms, they are within function brackets only. That is the only
     * case where a comma MAY be part of a valid expression.
     *
     * @param terms The list of terms as accepted by the create method.
     * @return True if and only if commas are within function brackets, if they exist.
     */
    private boolean checkCommasWithinFunctions(List<String> terms) {
        // TODO: WARNING: Be careful in future that a comma can be outside, or inside of function brackets, or inside of any other brackets...
        return getOuterItems(terms, List.of(new String[]{","})).isEmpty();
    }

    // TODO: Support User-Defined Functions!

    /** Checks if each function called in the expression has correct input size.
     *
     * @param terms The list of terms as accepted by the create method.
     * @return True if and only if all functions have correct input size. Example: ["cos","(","x",")] returns true,
     * ["sin","(","x","y",")"] returns false.
     */
    private boolean checkFunctionInputSize(List<String> terms) {

        Map<String, List<Integer>> functionsAndIndexLists = getOuterItems(terms, new ArrayList<>(validFuncs));
        List<String> functionInputTerms;
        Map<String, List<Integer>> commaAndIndexLists;
        int size;

        for (List<Integer> indices: functionsAndIndexLists.values()) {
            for (Integer index: indices) {
                //Get the list representing the scope of the function.
                functionInputTerms = terms.subList(index + 2, findCorrespondingBracket(terms, index + 1));

                if (getOuterItems(functionInputTerms, List.of(new String[]{","})).size() == 0) { //if no "," appear
                    // within the input of the function that are outside all brackets within the function,
                    // then there is only one input to the function.
                    size = 0;
                }
                else { // Otherwise, count how many commas appear within the function input that are outside all
                    // brackets within the function.
                    commaAndIndexLists = getOuterItems(functionInputTerms, List.of(new String[]{","}));
                    size = commaAndIndexLists.get(",").size();
                }
                    // Compare with the expected input size for this function. If different, then return false.
                if (constants.getBuiltInFunctionsAndInputSizes().get(terms.get(index)) - 1 != size) {

                    return false;
                }
            } // This works thanks to checkers in "precheck" that is done before this checker.
        }
        // All functions have correct input sizes, so return true.
        return true;
    }

    /**
     *
     * @param terms The list of terms as accepted by the create method.
     * @return True if and only if <terms> represents one or zero functions, variables, or numbers.
     */

    private boolean checkMultipleTermsConnection(List<String> terms) { // TODO: Recheck correctness (logically)!
        if (!(validFuncs.contains(terms.get(0)) &&
                enclosedByOuterBrackets(terms.subList(1, terms.size())))) { // The second condition is to prevent treating case like "cos(x) + 1" as a semi-base case, where the terms are not entirely within a function (a semi-base case example: "cos(x + 1^2 - 2sin(x))").
            return !(getOuterItems(terms, constants.getAllOperators()).isEmpty());
        }
        return true;
    }

    // TODO: Update below method documentation!
    /** Returns a map of items that are not in any brackets (in the order that they appear)
     *  along with the indices that they appear at being represented as a list.
     * @param terms The list of terms as accepted by the create method
     *              e.g. ["2", "*", "(", "5", "+", "6", ")", "-", "9"]
     * @param items Any collection of specific terms which we may want to keep track of.
     *             For example, constants.getOperators
     * @return A map of terms that are in items that are not in any brackets, with their values being the list of
     *              indices they appear at.
     *             For the example above, with the list of items being constants.getOperators, we get
     *             {"*": [1], "-": [7]}.
     */
    // TODO: Update the second parameter to "Collection<String>" type. Similarly for all other helpers!!!
    public Map<String, List<Integer>> getOuterItems(List<String> terms, List<String> items) { // TODO: Change the implementation to using "findCorrespondingBracket"!
        Map<String, List<Integer>> itemsAndIndices = new HashMap<>();
        int bracketCounter = 0;
        for (int i = terms.size() - 1; i > -1; i--){
            String term = terms.get(i);

            if (term.equals(")")){
                bracketCounter += 1;
            } else if (term.equals("(")){
                bracketCounter -= 1;
            }

            if (bracketCounter == 0){ // This means we are outside any brackets.
                // check if <term> is in <items> and if it isnt already in <itemsAndIndices>
                if (items.contains(term) && !itemsAndIndices.containsKey(term)) {
                    itemsAndIndices.put(term, new ArrayList<>(List.of(i))); // Add <term> to the map, along with the index it appears at.
                }
                // If <term> is in <items> and is a key in <itemsAndIndices>, append the list of indices (its value).
                else if (items.contains(term) && itemsAndIndices.containsKey(term)) {
                    itemsAndIndices.get(term).add(i); //
                }
            }
        }
        return itemsAndIndices;

    }

    // TODO: IMPORTANT!!! ADD BELOW PRECONDITION 2 TO MOST HELPER IN "ExpresionValidityChecker" and "ExpressionCreator"!
    // Below precondition:
    //     1. terms.get(index) must be either "(" or ")" to derive the right functionality it promises.
    //     2. Use this helper before ensuring that checkMatchingBrackets(terms) is true.

    /** Find the corresponding bracket of a bracket in the list of terms.
     * Precondition: parsed.get(index) is either "(" or ")".
     *
     * @param terms The list of terms as accepted by the create method.
     * @param index The index of a specific bracket.
     * @return Index of the corresponding bracket of <parsed.get(index)
     */
    private int findCorrespondingBracket(List<String> terms, int index) {
        int counter = 0;
        boolean initialState = true;
        boolean rightTraverse = true;

        while (0 <= index && index <= terms.size() - 1){
            // Below section initializes the traversal direction. <rightTraverse> =
            // True means "go right" and False means "go left" to find the corresponding bracket.
            if (initialState) {
                if (terms.get(index).equals(")")) {
                    rightTraverse = false; // Go left to find corresponding "(". Otherwise, go right to find ")".
                }
                initialState = false;
            }

            // Below section tries to find the corresponding bracket.
            if (terms.get(index).equals("(")){ counter += 1; }
            else if (terms.get(index).equals(")")){ counter -= 1; }

            if (counter == 0) { // By the precondition, we know that <counter> is not 0 in the first
                // of the loop. Exactly one of the previous if conditions will be true. 0nce <counter> is 0,
                // we passed an equal number of the opposite brackets as the original one. So we are at the corresponding \
                // bracket.
                return index;  // return the index of that bracket.
            }

            // Below section decides the traversal direction.
            if (rightTraverse) { index++; } //Keep going right to find  corresponding ")".
            else { index--; } // Keep going left to find corresponding "(" .
        }
        throw new IllegalArgumentException("Precondition Violated When Finding The Corresponding Bracket!");
    }

    /** Checks if the list of input terms begins with "(" and ends with the corresponding ")".
     *
     * @param terms A sublist of the list of terms accepted by the create method.
     * @return True if and only if <terms> begins with "(", and the corresponding closed bracket for the first term
     * is the last term of the list. Example: The input ["(", "x" "+", "3", ")"] returns true.
     */
    public boolean enclosedByOuterBrackets(List<String> terms){
        // TODO: Could have used "fidnCorrespondingBracket" to improve readability but unsure
        //  if it's a good thing to exert precondition (that checkMatchingBrackets(terms) evaluates to true)).

        if (terms.size() <= 1){ // There can't exist two brackets, so <terms> can't be enclosed by outer brackets.
            return false;
        }

        // We want the "counter" to reach to 0 IFF we get to the end of the expression (return true, and false
        // otherwise). Reaching 0 before means that the corresponding ')' is in the middle of the expression
        int counter = 0;

        for (int i = 0; i <= terms.size() - 1; i++){
            String currentTerm = terms.get(i);
            if (currentTerm.equals("(")){
                counter += 1;
            } else if (currentTerm.equals(")")){
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

    /** Checks if a list of terms contains an operator of a specific type.
     *
     * @param terms A sublist of the input to the create method.
     * @param operatorType The type of operator we want to look for. Either "Logical" or "Comparator" is accepted.
     * @return True if and only if <terms> contains an operator of the same type as <operatorType>.
     */
    public boolean containsOperator(List<String> terms, String operatorType) throws IllegalStateException {
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
            if (operators.contains(term)) {
                return true;
            }
        }
        return false;
    }

}


























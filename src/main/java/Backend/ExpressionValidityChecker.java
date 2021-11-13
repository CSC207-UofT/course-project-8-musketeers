package Backend;

import Backend.Exceptions.BaseCaseCreatorException;
import Backend.Exceptions.CompoundCaseCreatorException;

import java.util.List;
import java.util.Map;

// TODO: Exception tree. Have "InvalidInputsException" as a parent exception of below two.





public class ExpressionValidityChecker {
    Constants constants;

    public ExpressionValidityChecker() {
        this.constants = new Constants();
    }

    public void preCheck(List<String> terms) throws BaseCaseCreatorException, CompoundCaseCreatorException {
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
        else{ // TODO: Perhaps below have the "check..." to throw exceptions to avoid (if/else if) blocks?
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
    public void operandsTypeCheck(List<String> leftTerms, String operatorType, List<String> rightTerms) {
        // TODO
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
                    constants.getBuildInFunctions().contains(term) |
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
        Map<String, List<Integer>> functionsAndIndexLists = getOuterItems(terms, constants.getBuildInFunctions().stream().toList());
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
        // RISHIBH'S ANOTHER GENIUS IDEA: just check whether there's any comma outside by using "getOuterItems".
        // TODO: WARNING: Be careful in future that a comma can be outside, or inside of function brackets, or inside of any other brackets...
        return getOuterItems(terms, List.of(new String[]{","})).isEmpty();
    }

    private boolean checkFunctionInputSize(List<String> terms) {
        // TODO: NTNTNTNTNT Implementation structure similar to "checkFunctionBrackets" and need "varNum" attribute in all functions (Built-in) for now!
    }

    private boolean checkMultipleTermsConnection(List<String> terms) { // TODO: Recheck correctness (logically)!
        if (!(constants.getBuildInFunctions().contains(terms.get(0)) &&
                containsOuterBrackets(terms.subList(1, terms.size())))) { // The second condition is to prevent treating case like "cos(x) + 1" as a semi-base case, where the terms are not entirely within a function (a semi-base case example: "cos(x + 1^2 - 2sin(x))").
            return !(getOuterItems(terms, constants.getAllOperators()).isEmpty());
        }
        return true;
    }

    public Map<String, List<Integer>> getOuterItems(List<String> terms, List<String> items) {
        // TODO
    }

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

    public boolean containsOuterBrackets(List<String> terms){ // TODO: Check the correctness of this helper!

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
}


























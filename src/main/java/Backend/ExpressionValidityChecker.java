package Backend;

import Backend.Expressions.Expression;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: Exception tree.

class BaseCaseCreatorException extends Exception {
    public BaseCaseCreatorException(String message) {
        super(message);
    }
}

class CompoundCaseCreatorException extends Exception {
    public CompoundCaseCreatorException(String message) {
        super(message);
    }
}

public class ExpressionValidityChecker {
    Constants constants;

    public ExpressionValidityChecker() {
        this.constants = new Constants();
    }

    public void preCheck(List<String> terms) throws BaseCaseCreatorException, CompoundCaseCreatorException {
        if (terms.size() == 0){
            throw new BaseCaseCreatorException("NullExpressionException!");
        }
        else if (!checkAllTermsValid(terms)) {
            throw new CompoundCaseCreatorException("InvalidTermException!");
        }
        else if (!checkMatchingBrackets(terms)) {
            throw new CompoundCaseCreatorException("UnmatchedBracketsException!");
        }
        else if (!checkFunctionBrackets(terms)) {
            throw new CompoundCaseCreatorException("FunctionBracketsException!");
        }
        else if (!checkCommasWithinFunctions(terms)) {
            throw new CompoundCaseCreatorException("CommasNotWithinFunctions!");
        }
        else if (!checkFunctionInputsSize(terms)) {
            throw new CompoundCaseCreatorException("FunctionInputsSizeException!");
        }
    }

    /**
     * @param term An expression that consists of a single term e.g. "2", "x", etc
     */
    public void singleTermCheck(String term) throws BaseCaseCreatorException {
        if (!(checkNumber(term) | constants.getVariables().contains(term))) {
           throw new BaseCaseCreatorException("InvalidSingleExpressionException!");
        }
    }

    // TODO: Don't need below three recursive checker since we implicitly have them in "ExpressionCreator" already!
//    public void arithmeticOperatorCheck(List<String> terms) {}
//
//    public void logicalOperatorCheck(List<String> terms) {}
//
//    public void comparatorCheck(List<String> terms) {}

    private boolean checkNumber(String term) {
        try {
            Double num = Double.parseDouble(term); // Just check for whether "term" represents a number.
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean checkAllTermsValid(List<String> terms) {
        for (int i = 0; i <= terms.size() - 1; i++) {
            String term = terms.get(i);

            if (!(checkNumber(term) |
                    constants.getVariables().contains(term) |
                    constants.getOperators().contains(term) |
                    constants.getComparators().contains(term) |
                    constants.getLogicalOperators().contains(term) |
                    constants.getBuildInFunctions().contains(term) |
                    constants.getSpecialCharacters().contains(term))
            ) {
                return false;
            }
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

    private boolean checkFunctionBrackets(List<String> terms) {
        Map<Integer, String> indexAndFunction = getOuterFunctions(terms);
        for (int index : indexAndFunction.keySet()) {
            // Below only checks for whether it's posible to have two brackets after the function, but doesn't care
            // whether function inputs are correct.
            if (index >= terms.size() - 2) {
                return false;
            }
            else if (terms.get(index + 1).equals("(")) {
                return findCorrespondingBracket(terms, index + 1) != -1;
            }
            else { return false; }
        }
        return true;
    }

    private boolean checkCommasWithinFunctions(List<String> terms) {
        // TODO
    }

    private boolean checkFunctionInputsSize(List<String> terms) {
        // TODO
    }

    private Map<Integer, String> getOuterFunctions(List<String> terms) {
        // Original thought: throw exception "InvalidTermException" if it detects any unacceptable term!
        // Current thought: NO. The rule may change in the future, so let it just do what it should do, and have "checkOuterInvalidTerm" do what it does.
        // Idea similar to "getOuterOperators"!
        Map<Integer, String> indexAndFunction = new HashMap<>();
        int bracketCounter = 0;
        for (int i = 0; i <= terms.size() - 1; i++){
            String term = terms.get(i);

            if (term.equals("(")){
                bracketCounter += 1;
            } else if (term.equals(")")){
                bracketCounter -= 1;
            }

            if (bracketCounter == 0){
                if (constants.getBuildInFunctions().contains(term)) {
                    indexAndFunction.put(i, term);
                }
            }
        }
        return indexAndFunction;
    }

    private int findCorrespondingBracket(List<String> terms, int index) {
        int Counter = 0;
        boolean initialState = true;
        boolean rightTraverse = true;

        while (0 <= index && index <= terms.size()){
            // Below section initializes the traversal direction.
            if (initialState) {
                if (terms.get(index).equals(")")) {
                    rightTraverse = false;
                }
                initialState = false;
            }

            // Below section tries finding the corresponding bracket.
            if (terms.get(index).equals("(")){
                Counter += 1;
            }
            else if (terms.get(index).equals(")")){
                Counter -= 1;
            }
            if (Counter == 0){
                return index;
            }

            // Below section decides the traversal direction.
            if (rightTraverse) { index++; }
            else { index--; }
        }
        return -1;
    }

    // TODO: IMPORTANT! Now that we want all checkers to be recursive rather than static, so maybe using the
    //  "findCorrespondingBracket" helper would help with the implementation of all these checkers to skip index! To
    //  reduce runtime! Or no because "findCorrespondingBracket" helper also takes much runtime! Or maybe yes, since
    //  runtime is likely to be the same, and we improve code readability and reduce the likelihood of bugs.
}


























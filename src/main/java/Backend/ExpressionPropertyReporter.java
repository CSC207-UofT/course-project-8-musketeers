package Backend;

import Backend.Exceptions.BaseCaseCreatorException;
import Backend.Exceptions.CompoundCaseCreatorException;
import Backend.Exceptions.InvalidTermException;
import Backend.Expressions.FunctionExpression;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ExpressionPropertyReporter checks whether a list representing an expression is actually representing a valid
 * expression.
 * <p>
 * The checker works recursively within RealBooleanCreatorImp. RealBooleanCreatorImp creates the tree recursively, and
 * this class is responsible for checking the correctness of each step at a surface level. As this happens with
 * each recursive call, this class fully determines whether an expression is valid or not.
 */
public class ExpressionPropertyReporter implements PropertyChangeListener {
    Constants constants;
    public Map<String, FunctionExpression> definedFuncs = new HashMap<>();
    Map<String, Integer> funcNumInputs = new HashMap<>();

    public ExpressionPropertyReporter(Map<String, FunctionExpression> definedFuncs) {
        this.constants = new Constants();

        for (String funcName : definedFuncs.keySet()) {
            this.definedFuncs.put(funcName, definedFuncs.get(funcName));
        }

        for (String funcName : definedFuncs.keySet()) {
            funcNumInputs.put(funcName, definedFuncs.get(funcName).getInputs().length);
        }
    }

    /**
     * @param event An event denoting that Axes object has been modified. This method is specifically listening for
     *              functions being added to named functions
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if ("funcMap".equals(event.getPropertyName())) {
            FunctionExpression exp = (FunctionExpression) event.getNewValue();
            definedFuncs.put(exp.getItem(), exp);
            funcNumInputs.put(exp.getItem(), exp.getInputs().length);
        }
    }

    /**
     * A function name is valid if and only if it consists entirely of alphabets and
     * is not taken is not already in definedFuncs
     *
     * @param name Determine whether a given string is a valid function name
     * @return True if and only if name is a valid function name
     */
    public boolean validFuncName(String name) {
        return !definedFuncs.containsKey(name) && name.matches("[a-zA-Z]+");
    }

    /**
     * A preliminary check that throws an exception if the input expression from the user is invalid in specific ways.
     * This function will be called recursively in RealBooleanCreatorImp on operands of operators,
     * which ensures correctness.
     *
     * @param terms A parsed list as accepted by the create method.
     * @throws InvalidTermException If terms immediately found to be invalid
     */
    public void preCheck(List<String> terms) throws InvalidTermException {
        if (terms.size() == 0) {
            throw new BaseCaseCreatorException(BaseCaseCreatorException.ERRORMESSAGE_EMPTY_EXPRESSION);
        } else if (!checkAllTermsValid(terms)) { //check if all terms are terms we can interpret.
            throw new CompoundCaseCreatorException("InvalidTermException!");
        } else if (terms.size() == 1){
            return;
        } else if (!checkMatchingBrackets(terms)) { //Check if each bracket has a corresponding bracket and we dont
                                                    // any extra brackets.
            throw new CompoundCaseCreatorException("UnmatchedBracketsException!");
        } else if (!checkFunctionBrackets(terms)) { //Check that brackets
            throw new CompoundCaseCreatorException("FunctionBracketsException!");
        }
        else if (!checkCommasWithinFunctions(terms)) {
            throw new CompoundCaseCreatorException("CommasNotWithinFunctions!");
        }
        else if (!checkMultipleTermsConnection(terms)) {
            throw new CompoundCaseCreatorException("NonConnectedMultipleTermsException!");
        }
    }

    /**
     * @param term A single term from the parsed expression.
     * @return True if term represents a double, False otherwise.
     */
    private boolean checkNumber(String term) {
        try {
            Float.parseFloat(term); // Just check for whether "term" represents a number.
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * @param terms The list of terms as accepted by the create method.
     * @return True if all terms in the expression are valid terms, False otherwise. (e.g. Returns false if "c5ap"
     * is a term in the list)
     */
    private boolean checkAllTermsValid(List<String> terms) {
        for (int i = 0; i <= terms.size() - 1; i++) {
            String term = terms.get(i);

            if (!(checkNumber(term) | // If a term is not a number,
                    constants.getVariables().contains(term) | // or a variable,
                    constants.getAllOperators().contains(term) | // or an operator,
                    definedFuncs.containsKey(term) | // or a built in function
                    constants.getSpecialCharacters().contains(term)) // or a special character which is accepted,
            ) {
                return false;
            } //then return false as its not something we can interpret.
        }
        return true;
    }


    /**
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
            } else if (curr.equals(")")) {
                counter--;
            }
            index++;
        }
        return counter == 0; // If negative, it is as explained above. If positive, then theres an open bracket without
        // a corresponding closed bracket after it. If 0, then each bracket has a matching bracket.
    }


    /**
     * Check that, if a function is called in the expression, then it is immediately succeeded by an open bracket.
     * By the following precondition, this ensures theres a corresponding closed bracket.
     * Precondtion: CheckMatchingBrackets is called before this funcion is called, and it returns True.
     *
     * @param terms The list representing parsed expression representing the user's input.
     * @return True if and only if a function call in <terms> has brackets where may find its inputs.
     * Example: ["cos","(",")"] returns True, ["cos","x"] returns False.
     */
    private boolean checkFunctionBrackets(List<String> terms) {
        Map<String, List<Integer>> functionsAndIndexLists = getOuterItems(terms, new ArrayList<>(definedFuncs.keySet()));
        for (List<Integer> indices : functionsAndIndexLists.values()) {
            for (Integer index : indices) {
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

    /**
     * Checks that, if any commas exist in the list of terms, they are within function brackets only. That is the only
     * case where a comma MAY be part of a valid expression.
     *
     * @param terms The list of terms as accepted by the create method.
     * @return True if and only if commas are within function brackets, if they exist.
     */
    private boolean checkCommasWithinFunctions(List<String> terms) {
        return getOuterItems(terms, List.of(new String[]{","})).isEmpty();
    }

    /** This method ensures that if no operators appear in an expression then it's just a function call
     *
     * @param terms The list of terms as accepted by the create method.
     * @return True if and only if <terms> represents one or zero functions, variables, or numbers.
     */

    private boolean checkMultipleTermsConnection(List<String> terms) {
        if (!(definedFuncs.containsKey(terms.get(0)) &&
                enclosedByOuterBrackets(terms.subList(1, terms.size())))) {
            return !(getOuterItems(terms, constants.getAllOperators()).isEmpty());
        }
        return true;
    }

    /**
     * Finds the indices in which some given collection of items appears in
     *
     * @param terms The list of terms as accepted by the create method
     *              e.g. ["2", "*", "(", "5", "+", "6", ")", "-", "9"]
     * @param items Any collection of specific <terms> which we may want to keep track of.
     *              For example, constants.getOperators
     * @return A map of terms that are in items that are not in any brackets, with their values being the list of
     * indices they appear at.
     * For the example above, with the list of items being constants.getOperators, we get
     * {"*": [1], "-": [7]}.
     */
    public Map<String, List<Integer>> getOuterItems(List<String> terms, List<String> items) {
        Map<String, List<Integer>> itemsAndIndices = new HashMap<>();
        int bracketCounter = 0;
        for (int i = terms.size() - 1; i > -1; i--) {
            String term = terms.get(i);

            if (term.equals(")")) {
                bracketCounter += 1;
            } else if (term.equals("(")) {
                bracketCounter -= 1;
            }

            if (bracketCounter == 0) { // This means we are outside any brackets.
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


    /**
     * Checks if the list of input terms begins with "(" and ends with the corresponding ")".
     *
     * @param terms A sublist of the list of terms accepted by the create method.
     * @return True if and only if <terms> begins with "(", and the corresponding closed bracket for the first term
     * is the last term of the list. Example: The input ["(", "x" "+", "3", ")"] returns true.
     */
    public boolean enclosedByOuterBrackets(List<String> terms) {
        if (terms.size() <= 1) { // There can't exist two brackets, so <terms> can't be enclosed by outer brackets.
            return false;
        }

        // We want the "counter" to reach to 0 IFF we get to the end of the expression (return true, and false
        // otherwise). Reaching 0 before means that the corresponding ')' is in the middle of the expression
        int counter = 0;

        for (int i = 0; i <= terms.size() - 1; i++) {
            String currentTerm = terms.get(i);
            if (currentTerm.equals("(")) {
                counter += 1;
            } else if (currentTerm.equals(")")) {
                counter -= 1;
            }

            if (counter == 0 && i < terms.size() - 1) {
                return false;
            }
        }
        return counter == 0;
    }

    /**
     * Checks if a list of terms contains an operator of a specific type.
     *
     * @param terms        A sublist of the input to the create method.
     * @param operatorType The type of operator we want to look for. Either "Logical" or "Comparator" is accepted.
     * @return True if and only if <terms> contains an operator of the same type as <operatorType>.
     */
    public boolean containsOperator(List<String> terms, String operatorType) throws IllegalStateException {
        List<String> operators;
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
                // In theory this should really never run, hence why we have a RunTimeException
                throw new IllegalStateException("Unrecognized Operator Type!");
        }

        for (String term : terms) { // This way saves expected runtime by a lot!
            if (operators.contains(term)) {
                return true;
            }
        }
        return false;
    }
}


























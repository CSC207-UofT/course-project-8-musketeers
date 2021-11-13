package Backend;

import Backend.Exceptions.BaseCaseCreatorException;
import Backend.Exceptions.CompoundCaseCreatorException;
import Backend.Expressions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionCreator {

    private final Constants constants = new Constants();
    private final ExpressionValidityChecker vc = new ExpressionValidityChecker();
    private final ExpressionBuilder eb = new ExpressionBuilder();

    /* IMPORTANT FOR EVERYONE TO KNOW!!! ONLY "create" CAN CALL ITS TWO HELPERS BELOW!!! BECAUSE "create" IS THE
       COMPLETE VERSION OF CREATION AS IT HAS ALL CHECKER!!! */
    public Expression<?> create(List<String> terms) throws BaseCaseCreatorException, CompoundCaseCreatorException {
        List<String> minimalTerms = bracketsReduction(terms);
        int minimalTermsSize = minimalTerms.size();

        vc.preCheck(minimalTerms); // A basic current-level, NON-RECURSIVE check for the validity of the expression.
        /* Precheck will be shared in realVal and boolVal, especially the "InvalidTermException" shouldn't be
           thrown due to logicals or comparators in realVal, this is because that we have the precondition. In other
           words, in "realValCreate", we don't check for the existence of comparator or logical operator, thanks to the
           precondition. */

        if (containsLogicalOperator(minimalTerms) || containsComparator(minimalTerms)) {
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
    /* Below precondition: Should be real-valued expressions, so if there are logicals or comparators, then it's likely
       to get into infinite recursion, and example input would be "<<". */
    private RealValuedExpression realValuedCreate(List<String> terms) throws CompoundCaseCreatorException {
        RealValuedExpression resultingExpression;
        int termsSize = terms.size();
        String operatorType = "Arithmetic";

        if (termsSize == 1) {
            String term = terms.get(0);
            resultingExpression = eb.constructExpression(term);
        }
        // TODO: Below should also add User-Defined Function check (function names stored in Axes)
        else if (constants.getBuildInFunctions().contains(terms.get(0)) &&
                vc.containsOuterBrackets(terms.subList(1, termsSize))) { // The second condition is to prevent treating case like "cos(x) + 1" as a semi-base case, where the terms are not entirely within a function (a semi-base case example: "cos(x + 1^2 - 2sin(x))").
            RealValuedExpression[] inputs = findFunctionInputs(terms);
            resultingExpression = eb.constructExpression(terms.get(0), inputs);
        }
        else {
            resultingExpression = (RealValuedExpression) createOnOperators(terms, operatorType);
        }

        return resultingExpression;
    }

    // Below precondition: There exists at least one comparator or logical in input "terms".
    public BooleanValuedExpression booleanValuedCreate(List<String> terms) throws CompoundCaseCreatorException {
        BooleanValuedExpression resultingExpression;
        String operatorType;
        // No base case with terms.size() == 0 because "no term => no logical and comparator => realVal".

        List<String> unchainedTerms = unchainComparators(terms); // Only unchain the outer comparators.
        if (containsLogicalOperator(unchainedTerms)) {
            operatorType = "Logical";
        }
        else {
            operatorType = "Comparator";
        }
        resultingExpression = (BooleanValuedExpression) createOnOperators(unchainedTerms, operatorType);

        return resultingExpression;
    }

    private Expression<?> createOnOperators(List<String> terms, String operatorType) throws CompoundCaseCreatorException {
        Map<String, List<Integer>> operatorsAndIndices = getOuterOperators(terms, operatorType);
        Expression<?> lExpr, rExpr;
        List<String> operators;

        switch (operatorType) {
            case "Logical" -> operators = constants.getLogicalOperators();
            case "Comparator" -> operators = constants.getComparators();
            case "Arithmetic" -> operators = constants.getArithmeticOperators();
            default -> throw new IllegalStateException("Unrecognized Operator Type!");
        }

        for (String op : operators) {
            if (operatorsAndIndices.containsValue(op)) {
                int opIndex = operatorsAndIndices.get(op);
                List<String> leftTerms = terms.subList(0, opIndex);
                List<String> rightTerms = terms.subList(opIndex + 1, terms.size());
                vc.operandsTypeCheck(leftTerms, operatorType, rightTerms);
                try {
                    lExpr = create(leftTerms);
                    rExpr = create(rightTerms);
                } catch (BaseCaseCreatorException e) {
                    throw new CompoundCaseCreatorException("Invalid Operand Exception!");
                }
                return eb.constructExpression(lExpr, op, rExpr, operatorType);
            }
        }
        /* Below should never happen if we additionally add the "checkCommasWithinFunctions" checker, which we have
           decided to add it now. Now, abandoned "checkCommasWithinFunctions" with "checkMultipleTermsConnection", as
           the latter is more general and safer, but the former works too, thanks to "ExpressionReader". */
        throw new CompoundCaseCreatorException("SomethingExceptionUncaughtException");
    }

    // TODO: Decide whether only checker for outer ones or everything (including ones within a pair of bracket)?
    private boolean containsLogicalOperator(List<String> terms) {
        // TODO Maybe merge this and below into one.
    }

    private boolean containsComparator(List<String> terms) {
        // TODO
    }

    private List<String> unchainComparators(List<String> terms) { // Only unchain the outer ones.
        // TODO: Convert chained comparators to ... AND/& ...
    }

    private List<String> bracketsReduction(List<String> terms) {
        List<String> terms_copy = terms;
        while (vc.containsOuterBrackets(terms_copy)) {
            terms_copy = terms_copy.subList(1, terms_copy.size() - 1);
        }
        return terms_copy;
    }



    /**
     * @param terms List of terms as accepted by create, assumed to be of the form [func, (, ..., )]
     * @return A list of Expressions where each expression is an input to some function
     */
    private RealValuedExpression[] findFunctionInputs (List<String> terms) throws CompoundCaseCreatorException {
        List<Integer> commaIndices = findCommaIndices(terms);
        // we add the final index (corresponding to ')' )
        // this ensures that between every pair of indices in commaIndices
        // we have an input expression
        commaIndices.add(terms.size() - 1);

        RealValuedExpression[] inputs = new RealValuedExpression[commaIndices.size()];
        // start at 2 because first item if function name and second item is '('
        int startInd = 2;

        for (int i = 0; i < inputs.length; i++){
            try {
                RealValuedExpression inputExp = (RealValuedExpression) create(terms.subList(startInd, commaIndices.get(i)));
                /* Above: If two commas adjacent to each other (i.e. has nothing in between, then there will be
                   something like NullExpressionException (a BaseCaseException) thrown, but we should catch it!) */

                inputs[i] = inputExp;
                startInd = commaIndices.get(i) + 1;
            } catch (BaseCaseCreatorException e) {
                throw new CompoundCaseCreatorException("InvalidFunctionInputsException!");
            }
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

    /** Returns a map of operators that are not in any brackets (in the order that they appear)
     *  along with the indices that they appear at.
     *  If multiple instances of the same operator are present (outside any brackets),
     *  then only the first appearance is noted
     * @param terms The list of terms as accepted by the create method
     *              e.g. ["2", "*", "(", "5", "+", "6", ")", "-", "9"]
     * @param operatorType Can only be one of the following three
     * @return The list of operators that are not in any brackets. For the example above, we get
     *              {"*": 1, "-": 7}.
     */
    // Below precondition: "operatorType" can ONLY be one of the following three: "Arithmetic", "Logical", or "Comparator".
    // TODO: Have "getOuterOperators", "getOuterFunctions", "getOuter..." the same structure, but STRING TO LIST OF INTEGERS IN EXPRVC!
    private Map<Integer, String> getOuterOperators(List<String> terms, String operatorType){

        Map<Integer, String> indexAndOperator = new HashMap<>();

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
                        !indexAndOperator.containsValue(term)) {
                    indexAndOperator.put(i, term);
                }
            }
        }
        return indexAndOperator;
    }
}
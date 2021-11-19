package Backend;

import Backend.Exceptions.*;
import Backend.ExpressionBuilders.*;
import Backend.Expressions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**  The ExpressionCreator class is responsible for creating an expression tree representing the user input if the
 * expression the user input is valid.
 *
 * For example, the expression "2*x+3" is transformed to the following tree:
 * 2
 *     *
 * x
 *     +
 * 3
 * Following the standard for drawing trees set by csc148.
 *
 * If the expression is not valid, the expression tree will not be created and an exception is thrown.
 */
public class ExpressionCreator {

    private final Constants constants = new Constants();
    private final ExpressionValidityChecker vc;
    private final Map<String, FunctionExpression> funcMap;
    // TODO: We can likely use Observer Design Pattern to have funcMap be updated automatically when new functions are added to Axes

    /** Constructor for ExpressionCreator.
     *
     * @param funcMap A map of function names to the functions themselves.
     */
    public ExpressionCreator(Map<String, FunctionExpression> funcMap){
        this.funcMap = funcMap;
        this.vc = new ExpressionValidityChecker(funcMap);
    }

    public Expression<?> create(List<String> terms) throws InvalidTermException {
        ExpressionBuilder<?> eb = createExpressionBuilder(terms);
        return eb.build();
    }

    /* IMPORTANT FOR EVERYONE TO KNOW!!! ONLY "create" CAN CALL ITS TWO HELPERS BELOW!!! BECAUSE "create" IS THE
       COMPLETE VERSION OF CREATION AS IT HAS ALL CHECKER!!! */

    /** If the input list represents a valid expression, this method builds an expression tree from the input list.
     * Otherwise, an exception is thrown.
     *
     * @param terms A list of terms representing a parsed expression input by the user.
     * @return An expression tree representing the expression represented by <terms>
     * @throws InvalidTermException If <terms> represents an invalid expression, then we throw this exception and the
     * expression tree is not built.
     */
    public ExpressionBuilder<?> createExpressionBuilder(List<String> terms) throws InvalidTermException {
        List<String> minimalTerms = bracketsReduction(terms); // remove unnecessary enclosing brackets.

        vc.preCheck(minimalTerms); // A basic current-level, NON-RECURSIVE check for the validity of the expression.
        /* Precheck will be shared in realVal and boolVal, especially the "InvalidTermException" shouldn't be
           thrown due to logical or comparators in realVal, this is because that we have the precondition. In other
           words, in "realValCreate", we don't check for the existence of comparator or logical operator, thanks to the
           precondition. */

        if (vc.containsOperator(minimalTerms, "Logical") ||
                vc.containsOperator(minimalTerms, "Comparator")) { //check if logical and comparator operators
                                                                            // are in the expression.
            return booleanValuedCreate(minimalTerms); //if so, create boolean valued expression.
        }
        else {
            return realValuedCreate(minimalTerms); // If not, create real valued expression.
        }
    }

    /** Converts a (valid) expression (represented as a list) into a Backend.Expression
     * Precondition: Should be real-valued expressions, so if there are logical or comparators, then it's likely
     *        to get into infinite recursion, and example input would be "<=".
     *
     * @param terms A list of terms in the expression (see below for how they should be broken up
     * @return A Backend.Expression (AST) representation of the expression
     */

    private RealValuedExpressionBuilder realValuedCreate(List<String> terms) throws InvalidTermException {

        int termsSize = terms.size();
        String operatorType = "Arithmetic";

        // create empty real valued expression builder we will return.
        RealValuedExpressionBuilder eb = new RealValuedExpressionBuilder();

        if (termsSize == 1) {
            String term = terms.get(0); //Its only one term.
            eb.constructExpression(term); //create expression based on that term.
        }
        else if (funcMap.containsKey(terms.get(0)) && // check if first term is a function call.
                vc.enclosedByOuterBrackets(terms.subList(1, termsSize))) { //check if whole expression is one function.
            // The second condition is to prevent treating case like "cos(x) + 1" as a semi-base case,
            // where the terms are not entirely within a function (a semi-base case example: "cos(x + 1^2 - 2sin(x))").


            // We use sublist to remove function name and brackets. Construct a list of expressions, each corresponding
            // to a function input.
            RealValuedExpressionBuilder[] inputs = findFunctionInputs(terms.subList(2, termsSize - 1));
            //construct expression.
            eb.constructExpression(terms.get(0), inputs, funcMap);
        }
        else { //otherwise, there must be some operators within the function. We construct a new expression
            // by calling createOnOperators.
            eb = (RealValuedExpressionBuilder) createOnOperators(terms, operatorType);
        }

        return eb;
    }

    /**
     * Precondition: There exists at least one comparator or logical operator in input <terms>.
     *
     * @param terms The list of terms as accepted by the create method.
     * @return A BooleanValuedExpression tree that represents the input list.
     * @throws InvalidTermException If the list of terms represents an invalid expression then this exception is thrown.
     */
    // Below precondition: There exists at least one comparator or logical in input "terms".
    private BooleanValuedExpressionBuilder booleanValuedCreate(List<String> terms) throws InvalidTermException {
        String operatorType;
        // No base case with terms.size() == 0 because "no term => no logical and comparator => realVal".

        List<String> unchainedTerms = unchainComparators(terms); // Only unchain the outer comparators.
        if (vc.containsOperator(unchainedTerms, "Logical")) {
            operatorType = "Logical";
        }
        else { // Thanks to the precondition.
            operatorType = "Comparator";
        }

        return (BooleanValuedExpressionBuilder) createOnOperators(unchainedTerms, operatorType);
    }

    /**
     *
     * @param terms The list of terms as accepted by the create method.
     * @param operatorType The type of operator we will be splitting <terms> based on. Can be "Logical" or "Comparator"
     *                     or "Arithmetic"
     * @return A RealValuedExpression if operatorType is "Arithmetic", a BooleanValuedExpression if operator is
     * "Logical" or "Comparator". Both expressions returned represent <terms> in a valid format which can be graphed.
     * @throws InvalidTermException If
     */
    private ExpressionBuilder<?> createOnOperators(List<String> terms, String operatorType) throws InvalidTermException {
        ExpressionBuilder<?> lExpr, rExpr, eb; // initialise the expressions on the left and right of the operands.
        List<String> operators; // List which will store the operators of type <operatorType>.

        switch (operatorType) {
            case "Logical":
                operators = constants.getLogicalOperators();
                eb = new BooleanValuedExpressionBuilder();
                break;
            case "Comparator":
                operators = constants.getComparators();
                eb = new BooleanValuedExpressionBuilder();
                break;
            case "Arithmetic":
                operators = constants.getArithmeticOperators();
                eb = new RealValuedExpressionBuilder();
                break;
            default: // throw exception if <operatorType> is invalid.
                throw new IllegalStateException("Unrecognized Operator Type!");
        }
        //create a map of all operators of type <operatorType> that are not within any brackets, with the corresponding
        // values being lists of the indices they appear at.
        Map<String, List<Integer>> operatorsAndIndices = vc.getOuterItems(terms, operators);

        for (String op : operators) { // iterate through all operators of the correct type.
            if (operatorsAndIndices.containsKey(op)) {
                List<Integer> indexList = operatorsAndIndices.get(op); // get the list of indices <op> appears at.
                int opIndex = indexList.get(0); // focus on the first appearance.
                List<String> leftTerms = terms.subList(0, opIndex); // split according to that appearance.
                List<String> rightTerms = terms.subList(opIndex + 1, terms.size());

                vc.operandsTypeCheck(leftTerms, operatorType, rightTerms); //check if the type of operators in the rest
                // of the expression match what is expected.
                try {  // check if any issues arise from creating an expression from <leftTerms> and <rightTerms>.
                    lExpr = createExpressionBuilder(leftTerms);
                    rExpr = createExpressionBuilder(rightTerms);
                } catch (BaseCaseCreatorException e) { //throw an exception if either is invalid.
                    throw new CompoundCaseCreatorException("Invalid Operand Exception!");
                }
                // if both are valid, construct an expression tree with the root being <op>, left child being <lExpr>
                // and right child being <rExpr>.

                // TODO: double check that there is never an issue with lExpr and rExpr's types
                return eb.constructExpression(lExpr, op, rExpr, operatorType);
            }
        }
        /* Below should never happen if we additionally add the "checkCommasWithinFunctions" checker, which we have
           decided to add it now. Now, abandoned "checkCommasWithinFunctions" with "checkMultipleTermsConnection", as
           the latter is more general and safer, but the former works too, thanks to "ExpressionReader". */
        throw new CompoundCaseCreatorException("SomethingExceptionUncaughtException");
    }

    private List<String> unchainComparators(List<String> terms) { // Only unchain the outer ones.
        // Convert chained comparators to ... AND ...
        // E.g. 1 < x < 2 -> 1 < x & x < 2

        List<String> unchainedTerms = new ArrayList<>();

        Map<String, List<Integer>> comparatorsAndIndices = vc.getOuterItems(terms, constants.getComparators()); // TODO: For the time being, let's use this helper, but in future, we can improve runtime by writing a more specific helper!

        // TODO: Future have another more efficient helper.
        List<Integer> indices = new ArrayList<>();
        for (List<Integer> subIndices: comparatorsAndIndices.values()) {
            indices.addAll(subIndices);
        }
        Collections.sort(indices);

        if (indices.size() <= 1){
            return terms;
        }

        int firstComparatorIndex = indices.get(0);
        int secondComparatorIndex = indices.get(1);

        List<String> inBetweenTerms = terms.subList(firstComparatorIndex + 1, secondComparatorIndex);

        if (!vc.containsOperator(inBetweenTerms, "Logical")) {
            unchainedTerms.addAll(terms.subList(0, secondComparatorIndex));
            unchainedTerms.add("&");
            unchainedTerms.addAll(unchainComparators(terms.subList(firstComparatorIndex + 1, terms.size())));
        }
        else {
            unchainedTerms.addAll(terms.subList(0, secondComparatorIndex + 1));
            if (secondComparatorIndex <= terms.size() - 2) {
                unchainedTerms.addAll(unchainComparators(terms.subList(secondComparatorIndex + 1, terms.size())));
            }
        }
        return unchainedTerms;
    }

    /** Remove brackets which are enclosing the expression represented by the input list <terms>.
     *
     * @param terms List representing terms of the expression input by the user.
     * @return A list without unnecessary brackets enclosing the expression. Ex: ["(","(", "cos","(","x",")",")",")"]
     * is input, and ["cos","(","x",")"] is returned.
     */
    private List<String> bracketsReduction(List<String> terms) {
        List<String> terms_copy = terms; // TODO: Not really copying, fix in future!!!!!!
        while (vc.enclosedByOuterBrackets(terms_copy)) {
            terms_copy = terms_copy.subList(1, terms_copy.size() - 1);
        }
        return terms_copy;
    }

    /** A method which returns the list of expressions corresponding to the inputs of a function.
     *
     * @param terms List of terms that are the inputs to some function with the function name and brackets removed.
     * @return A list of Expressions where each expression is a separate input to the function.
     */
    private RealValuedExpressionBuilder[] findFunctionInputs (List<String> terms) throws InvalidTermException {
        List<Integer> commaIndices = vc.getOuterItems(terms, List.of(",")).get(","); //get list of the indices where
        // commas appear outside any brackets. These correspond to separating the input for the function wer are

        // calling this method for.
        if (commaIndices == null){
            commaIndices = new ArrayList<>();
        }
        //
        commaIndices.add(terms.size()); // Add the index of ")" that marks the end of the function input to ensure
        // the last input to the function is considered in the following loop.
        // TODO: Recheck Correctness (logical sense).
        // for below, the number of commas outside brackets + 1 represents the number of inputs to the function, if
        // the terms are in a valid format.
        RealValuedExpressionBuilder[] inputs = new RealValuedExpressionBuilder[commaIndices.size()];
        int startInd = 0;

        for (int i = 0; i < inputs.length; i++){
            // this list represents all terms within a pair of commas, thus representing one input to the function.
            List<String> inputTerm = terms.subList(startInd, commaIndices.get(i));
            vc.realValuedPreconditionCheck(inputTerm); //check each input is a RealValuedExpression.
            try { // Ensure that each input can be constructed as an expression (otherwise it's an invalid input).
                // TODO: Check that a inputTerm always produces a RealValuedExpression
                RealValuedExpressionBuilder inputExp = (RealValuedExpressionBuilder) createExpressionBuilder(inputTerm);
                /* Above: If two commas adjacent to each other (i.e. has nothing in between, then there will be
                   something like NullExpressionException (a BaseCaseException) thrown, but we should catch it!) */

                inputs[i] = inputExp; // Add the expression for this input.
                startInd = commaIndices.get(i) + 1; // Start looking at the next input.
            } catch (BaseCaseCreatorException e) {
                throw new CompoundCaseCreatorException("InvalidFunctionInputsException!");
            }
        }
        // All inputs have been transformed into expressions. All were created, therefore they are in a valid format.
        // Return the list of expressions representing the input.
        return inputs;
    }

}
package Backend;

import Backend.Exceptions.BaseCaseCreatorException;
import Backend.Exceptions.CompoundCaseCreatorException;
import Backend.Exceptions.InvalidTermException;
import Backend.Expressions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ExpressionCreator {

    private final Constants constants = new Constants();
    private final ExpressionBuilder eb = new ExpressionBuilder();
    private final Axes ax;
    private final ExpressionValidityChecker vc;
    // TODO: We can likely use Observer Design Pattern to remove dependency on Axes

    public ExpressionCreator(Axes ax){
        this.ax = ax;
        this.vc = new ExpressionValidityChecker(ax.getNamedExpressions().keySet());
    }
    public ExpressionCreator(){
        this(new Axes());
    }

    /* IMPORTANT FOR EVERYONE TO KNOW!!! ONLY "create" CAN CALL ITS TWO HELPERS BELOW!!! BECAUSE "create" IS THE
       COMPLETE VERSION OF CREATION AS IT HAS ALL CHECKER!!! */
    public Expression<?> create(List<String> terms) throws InvalidTermException {
        List<String> minimalTerms = bracketsReduction(terms); // remove unnecessary enclosing brackets.
        int minimalTermsSize = minimalTerms.size();

        vc.preCheck(minimalTerms); // A basic current-level, NON-RECURSIVE check for the validity of the expression.
        /* Precheck will be shared in realVal and boolVal, especially the "InvalidTermException" shouldn't be
           thrown due to logicals or comparators in realVal, this is because that we have the precondition. In other
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
     * Precondition: Should be real-valued expressions, so if there are logicals or comparators, then it's likely
     *        to get into infinite recursion, and example input would be "<=".
     *
     * @param terms A list of terms in the expression (see below for how they should be broken up
     * @return An Backend.Expression (AST) representation of the expression
     */

    private RealValuedExpression realValuedCreate(List<String> terms) throws InvalidTermException {

        RealValuedExpression resultingExpression; // create empty real valued expression we will return.
        int termsSize = terms.size();
        String operatorType = "Arithmetic";

        if (termsSize == 1) {
            String term = terms.get(0); //Its only one term.
            resultingExpression = eb.constructExpression(term); //create expression based on that term.
        }
        else if (ax.getNamedExpressions().containsKey(terms.get(0)) && // check if first term is a function call.
                vc.enclosedByOuterBrackets(terms.subList(1, termsSize))) { //check if whole expression is one function.
            // The second condition is to prevent treating case like "cos(x) + 1" as a semi-base case,
            // where the terms are not entirely within a function (a semi-base case example: "cos(x + 1^2 - 2sin(x))").


            // sublist is to remove function name and brackets. Construct expression corresponding to function input.
            RealValuedExpression[] inputs = findFunctionInputs(terms.subList(2, termsSize - 1));
            //construct expression.
            resultingExpression = eb.constructExpression(terms.get(0), inputs, ax.getNamedExpressions());
        }
        else { //otherwise, there must be some operators within the function. We construct a new expression
            // by calling createOnOperators.
            resultingExpression = (RealValuedExpression) createOnOperators(terms, operatorType);
        }

        return resultingExpression;
    }

    // Below precondition: There exists at least one comparator or logical in input "terms".
    private BooleanValuedExpression booleanValuedCreate(List<String> terms) throws InvalidTermException {
        BooleanValuedExpression resultingExpression;
        String operatorType;
        // No base case with terms.size() == 0 because "no term => no logical and comparator => realVal".

        List<String> unchainedTerms = unchainComparators(terms); // Only unchain the outer comparators.
        if (vc.containsOperator(unchainedTerms, "Logical")) {
            operatorType = "Logical";
        }
        else { // Thanks to the precondition.
            operatorType = "Comparator";
        }
        resultingExpression = (BooleanValuedExpression) createOnOperators(unchainedTerms, operatorType);

        return resultingExpression;


    }

    /**
     *
     * @param terms The list of terms that
     * @param operatorType
     * @return
     * @throws InvalidTermException
     */
    private Expression<?> createOnOperators(List<String> terms, String operatorType) throws InvalidTermException {
        Expression<?> lExpr, rExpr;
        List<String> operators;
        switch (operatorType) {
            case "Logical":
                operators = constants.getLogicalOperators();
                break;
            case "Comparator":
                operators = constants.getComparators();
                break;
            case "Arithmetic":
                operators = constants.getArithmeticOperators();
                break;
            default:
                throw new IllegalStateException("Unrecognized Operator Type!");
        }

        Map<String, List<Integer>> operatorsAndIndices = vc.getOuterItems(terms, operators);


        for (String op : operators) {
            if (operatorsAndIndices.containsKey(op)) {
                List<Integer> indexList = operatorsAndIndices.get(op);
                int opIndex = indexList.get(0);
                List<String> leftTerms = terms.subList(0, opIndex);
                List<String> rightTerms = terms.subList(opIndex + 1, terms.size());

//                vc.operandsTypeCheck(leftTerms, operatorType, rightTerms);
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
        List<String> terms_copy = terms;
        while (vc.enclosedByOuterBrackets(terms_copy)) {
            terms_copy = terms_copy.subList(1, terms_copy.size() - 1);
        }
        return terms_copy;
    }

    /**
     * @param terms List of terms that are the inputs to some function with the function name and brackets removed
     * @return A list of Expressions where each expression is an input to some function
     */
    private RealValuedExpression[] findFunctionInputs (List<String> terms) throws InvalidTermException {
        List<Integer> commaIndices = vc.getOuterItems(terms, List.of(",")).get(",");
        // we add the final index (corresponding to ')' )
        // this ensures that between every pair of indices in commaIndices
        // we have an input expression
        if (commaIndices == null){
            commaIndices = new ArrayList<>();
        }
        commaIndices.add(terms.size()); // TODO: Recheck Correctness (logical sense).
        RealValuedExpression[] inputs = new RealValuedExpression[commaIndices.size()];
        // start at 2 because first item if function name and second item is '('
        int startInd = 0;

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

}
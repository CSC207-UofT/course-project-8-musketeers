package Backend;

import Backend.Exceptions.InvalidTermException;
import Backend.ExpressionBuilders.BooleanValuedExpressionFactory;
import Backend.ExpressionBuilders.RealValuedExpressionFactory;
import Backend.Expressions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The ExpressionReader class is responsible for parsing the user input expression that will be graphed.
 */
public class ExpressionReader {
    private final Constants constants = new Constants();
    private final ExpressionCreator expressionCreator;
    public final ExpressionValidityChecker validityChecker;

    /**
     * Constructor for ExpressionReader.
     *
     * @param funcMap A map of function names to the actual functions.
     */
    public ExpressionReader(Map<String, FunctionExpression> funcMap) {
        this.validityChecker = new ExpressionValidityChecker(funcMap);
        this.expressionCreator = new ExpressionCreator(funcMap, this.validityChecker, new RealValuedExpressionFactory(),
                new BooleanValuedExpressionFactory());
    }

    /**
     * @param axes Axes object that ExpressionCreator and ValidityChecker are going to be 'configured' to
     *             i.e. this is the Axes object that they will observing
     */
    public ExpressionReader(Axes axes) {
        this(axes.getNamedExpressions());

        axes.addObserver(this.expressionCreator);
        axes.addObserver(this.validityChecker);
    }

    /**
     * @param expressionInput An array of strings that has either one or two items. First item is the expression and the second item is the domain (if provided)
     * @return A RealValuedExpression with the appropriate domain
     * @throws InvalidTermException If either expression cannot be read
     */
    public RealValuedExpression readForGraphing(String[] expressionInput) throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) read(expressionInput[0]);
        if (expressionInput.length != 1) {
            BooleanValuedExpression domain = (BooleanValuedExpression) read(expressionInput[1]);
            exp.setDomain(domain);
        }
        return exp;
    }

    /**
     * Converts a string representation of an expression into an instance of Expression
     *
     * @param expression The string representation of the expression to be converted
     * @return Expression object for the string provided
     */
    public Expression<?> read(String expression) throws InvalidTermException {
        List<String> terms = expressionParser(expression);
        if (validityChecker.containsOperator(terms, "Logical") || validityChecker.containsOperator(terms, "Comparator")) {
            return booleanValuedRead(terms);
        } else {
            if (!terms.contains("=")) {
                // If no "=" then just a regular expression
                return realValuedRead(terms);
            }
            if (isExplicit(terms)) {
                return explicitRead(terms);
            } else {
                return implicitRead(terms);
            }
        }
    }


    /**
     * We say a list of terms appears to be an explicit expression if and only if:
     * 0. There is an equals sign in the expression
     * 1. the first term is all alphabets (lower or upper case), assumed to be the name of the function
     * 2. The second term is '(' and the term before the equals sign in ')'
     * 3. Everything in between alternates between alphabets and commas
     * 4. The alphabets are variables, as defined in Constants.VARIABLES
     *
     * @param terms A list of strings representing userInput after parsing by expressionParser()
     * @return true if and only if the expression appears to be an explicit function
     */
    private boolean isExplicit(List<String> terms) {

        // "=" assumed to be present
        // funcHeader is going to be something like ["f", "(", "x", "y", ")]
        List<String> funcHeader = terms.subList(0, terms.indexOf("="));

        // check the first term is a valid function name (all alphabets and is not already taken)
        if (!validityChecker.validFuncName(funcHeader.get(0))){
            return false;
        }
        // Check condition 2
        if (!validityChecker.enclosedByOuterBrackets(funcHeader.subList(1, funcHeader.size()))){
            return false;
        }

        // checks until the last comma
        for (int i = 2; i < funcHeader.size() - 2; i++) {
            // Check condition 4
            if (!constants.getVariables().contains(funcHeader.get(i))) {
                return false;
            }
            // Check condition 3
            if (!funcHeader.get(i + 1).equals(",")) {
                return false;
            }
        }
        // the term preceding the final bracket is a variable
        return constants.getVariables().contains(funcHeader.get(funcHeader.size() - 2));
    }

    /**
     * @param terms A list of strings representing userInput after parsing by expressionParser()
     * @return A RealValuedExpression representing left - right for the implicit function
     * @throws InvalidTermException If the expression is invalid
     */
    private RealValuedExpression implicitRead(List<String> terms) throws InvalidTermException {
        int eqIndex = terms.indexOf("=");
        List<String> lTerms = new ArrayList<>(terms.subList(0, eqIndex));
        List<String> rTerms = terms.subList(eqIndex + 1, terms.size());
        lTerms.addAll(List.of("-", "("));
        lTerms.addAll(rTerms);
        lTerms.add(")");
        return realValuedRead(lTerms);
    }

    /**
     * @param terms A list of strings representing userInput after parsing by expressionParser()
     * @return A CustomFunctionExpression containing the necessary for the given function, upcasted to a RealValuedExpression
     * @throws InvalidTermException If the expression is invalid
     */
    private RealValuedExpression explicitRead(List<String> terms) throws InvalidTermException {
        int eqIndex = terms.indexOf("=");
        RealValuedExpression function = realValuedRead(terms.subList(eqIndex + 1, terms.size()));
        String funcName = terms.get(0);
        String[] variables = findFunctionVars(terms.subList(0, eqIndex));
        return new CustomFunctionExpression(funcName, variables, function);
    }

    /**
     * @param funcHeader The header of a function is everything to the left of '=' when defining a function
     * @return An array of Strings containing the variables that the function is in terms of
     * For ["f", "(", "x", ")"] -> ["x"]
     */
    private String[] findFunctionVars(List<String> funcHeader) {
        List<String> varTerms = funcHeader.subList(2, funcHeader.size() - 1);
        String[] variables = new String[(varTerms.size() + 1) / 2];
        for (int i = 0; i < variables.length; i++) {
            variables[i] = varTerms.get(2 * i);
        }
        return variables;
    }

    // Below precondition: Should be real-valued expressions, so if there's logicals or comparators, then some exception
    // will be thrown, or program crashes, depends.. //
    // E.g. "x^2 + y" is acceptable; "x = 4" will evoke some exceptions.
    private RealValuedExpression realValuedRead(List<String> terms) throws InvalidTermException {
        return (RealValuedExpression) expressionCreator.create(terms);
    }

    // Below precondition: Should be boolean-valued expressions, so if there's no logicals or comparators at all, then
    // some exception will be thrown.
    // E.g. "x = 4" is acceptable; "x^2 + y" will evoke some exceptions.
    private BooleanValuedExpression booleanValuedRead(List<String> terms) throws InvalidTermException {
        return (BooleanValuedExpression) expressionCreator.create(terms);
    }

    /**
     * expressionParser takes an input string and parses it to form a list. See below for examples
     * As minus and plus may also be unary operators as well as binary ones, if there is any instance of a unary usage
     * of these operators, expressionParser interprets them as binary similar to the following example:
     * -x is interpreted to be ["-1", "*", "x"]
     *
     * @param expression This is the expression the user has input.
     * @return A list which we can create an expression tree from.
     */
    // e.g. x ^ 2 + 5 -> ["x", "^", "2", "+", "5"]
    // e.g. (2) + 3 or 3 + (2) -> ["(", "2", ")", "+", "3"]
    // e.g. cos(x) -> ["cos", "(", "x", ")"]
    private List<String> expressionParser(String expression) {
        List<String> parsed = new ArrayList<>();
        StringBuilder section = new StringBuilder(); // section will be storing any series of characters which are not
        // operators.
        // We will loop over expression to interpret it and add to parsed.
        for (int character = 0; character < expression.length(); character++) {
            String letter = String.valueOf(expression.charAt(character));
            //If its not alphanumeric, its a special character, in which case we add whatever section refers to into parsed
            //and add the special character aftwerwards.
            if (letter.matches("^.*[^a-zA-Z0-9. ].*$")) {
                //we want to be sure that section does not refer to an empty string.
                if (!section.toString().equals("")) {
                    parsed.add(section.toString());
                }
                parsed.add(letter);
                section = new StringBuilder();

            }
            // If its a space, then we add whatever section refers to to parsed to ensure we dont mix "section" with the next
            //character.
            else if (letter.equals(" ")) {
                if (!section.toString().equals("")) {
                    parsed.add(section.toString());
                }
                section = new StringBuilder();
            } else {
                //If its neither of those cases. then it must be alphanumeric and so we append "section".
                section.append(letter);
            }
        }


        //As we finished looping over expression, we may have that section refers to the last bit of the input.
        // We add it to parsed if its nonempty.

        if (!section.toString().equals("")) {
            parsed.add(section.toString());
        }
        //We may have valid input from the user, but it may not be interpreted correctly as is.
        //We call this function to modify our parsed list to give a form that can be transformed into a valid AST.
        fixParsedlist(parsed);
        return parsed;

    }

    /**
     * Edit parsed to interpret edge cases where unary operators are used correctly and give us a
     * list which can be converted into the correct AST that represents the user's input.
     *
     * @param parsed The list we have parsed in the first pass through expression parser.
     */

    private void fixParsedlist(List<String> parsed) {
        //
        fixLogicalOperators(parsed); // account for logical operators which may not be read correctly.
        handleOperators(parsed); // remove all accounts of sequences of "+" and "-".
        handleSign(parsed); // intepret unary usage of "-" and "+" correctly.

    }

    /**
     * We want to go through the list and replace any sequences of  "+" and "-" with the resulting sign.
     *
     * @param parsed The list we have parsed in the first pass through expression parser.
     */
    private void handleOperators(List<String> parsed) {

        int size = parsed.size();
        for (int i = 0; i < size - 1; i++) {
            if (parsed.get(i).equals("-") || parsed.get(i).equals("+")) {
                removeDifferent(i, parsed);
            }
            if (size != parsed.size()) {
                size = parsed.size();
                i--;
            }
        }
    }

    /**
     * If character at next index is "-" or "+", remove both operators and return the correct sign.
     *
     * @param index  the current index at which we have a "+" or "-"
     * @param parsed The parsed list that we are editing.
     */
    private void removeDifferent(int index, List<String> parsed) {
        if (parsed.get(index).equals(parsed.get(index + 1))) {
            parsed.remove(index);
            parsed.remove(index);
            parsed.add(index, "+");
        } else if (parsed.get(index + 1).equals("-") || parsed.get(index + 1).equals("+")) {
            parsed.remove(index);
            parsed.remove(index);
            parsed.add(index, "-");

        }
    }

    /**
     * Interpret "+" and "-" used in the unary context as "1*" and "-1*" respectively.
     *
     * @param parsed The parsed list we are editing.
     */
    private void handleSign(List<String> parsed) {
        for (int i = 0; i < parsed.size(); i++) {
            String current = parsed.get(i);
            //This function is called after handleOperators, so there are no consecutive unary operators, ensuring it
            // makes sense.
            //Special case i = 0. We immediately know its a unary use of "+" and "-".
            if (i == 0 && (current.equals("-") || current.equals("+"))) {
                interpretOperator(i, current, parsed);
            } else if (i > 0 && (current.equals("-") || current.equals("+"))) {
                //In this case, its more tricky to determine a unary usage of an operator.
                determineAndReplaceUnaryOperators(i, parsed);
            }
        }
    }

    /**
     * Determine if "-" and "+" are being used in a unary context and interpret them appropriately.
     *
     * @param i      current index in the parsed list.
     * @param parsed The parsed list we are editing to interpret "-" and "+" in a unary context.
     */
    private void determineAndReplaceUnaryOperators(int i, List<String> parsed) {
        // specialCharacters will contain all characters where, if "-" or "+" appear after, they are used in
        // unary context.
        List<String> specialCharacters = constants.getAllOperators();
        specialCharacters.removeAll(List.of("/", "^")); // The case where we have ??/-??" in the code is bad habit. We are enforcing
        // rule that we are not responsible for the interpretation of it. So we remove "/" and Same for "??^-??"
        specialCharacters.addAll(List.of("(", "=", ","));
        // If the previous element of the parsed list is special, we interpet the operator as unary.
        if (specialCharacters.contains(parsed.get(i - 1))) {
            interpretOperator(i, parsed.get(i), parsed);
        }

    }


    /**
     * Replace a unary  account of "-" or "+" in the parsed list and replace with "-1" followed by "*", or "1"
     * followed by "*" respectively.
     *
     * @param i      current index where we have "-" or "+"
     * @param s      Character at parsed[i]
     * @param parsed The parsed list where we are interpreting unary operators.
     */
    private void interpretOperator(int i, String s, List<String> parsed) {
        parsed.remove(i);
        parsed.add(i, "*");
        if (s.equals("-")) {
            parsed.add(i, "-1");

        } else if (s.equals("+")) {
            parsed.add(i, "1");
        }
    }

    /**
     * There are some logical operators which consist of 2 other successive logical operators. The first pass through
     * expressionParser interprets them separately. This corrects this misinterpretation.
     *
     * @param parsed Parsed list we are editing to be interpreted as a correct expression.
     */
    private void fixLogicalOperators(List<String> parsed) {
        int size = parsed.size();
        for (int i = 0; i < size - 1; i++) { //We go upto size - 1 to ensure parsed.get(i+1) exists. If
            // parsed.get(size-1) equals "<" or ">" then it would be invalid input anyway and rejected later.
            StringBuilder current = new StringBuilder(parsed.get(i));
            if ((current.toString().equals("<") || current.toString().equals(">")) && parsed.get(i + 1).equals("=")) {
                concatenateOperators(parsed, current, i); // replace "<" followed by "=" with "<=". And same for ">".
            }
            size = parsed.size(); //update the size so we dont have index out of bounds error.
        }
    }

    /**
     * A method which converts an instance of "<" or ">" followed by "=" in the parsed list into one "<="
     * Precondition: parsed.get(i) equals "<" or ">" and parsed.get(i+1) exists and is equal to "=".
     *
     * @param parsed  The parsed list we are editing.
     * @param current A StringBuilder term which is equal to parsed.get(i).
     * @param i       index of the list.
     */
    private void concatenateOperators(List<String> parsed, StringBuilder current, int i) {
        parsed.remove(i);
        parsed.remove(i);
        current.append("=");
        parsed.add(i, current.toString());
    }

}

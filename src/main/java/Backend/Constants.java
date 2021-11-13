package Backend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Constants {

    // List used for operators as the order of operators matter and is used to control the order of precedence in expressions.
    // Sets used for these as only need containment and order is irrelevant.
    private final Set<String> VARIABLES = Set.of("x", "y", "z");
    private final Set<String> BUILDINFUNCTIONS = Set.of("cos", "sin", "tan", "sqrt", "mandel");
    private final Set<String> SPECIALCHARACTERS = Set.of("(", ")", ","); // Ted: TODO: Future maybe "\" for "\pi", "\floor", ...
    private final List<String> LOGICALOPERATORS = new ArrayList<>(List.of(new String[]{"|", "&"})); // TODO: Recheck The order or "AND" and "OR"!
    private final List<String> COMPARATORS = new ArrayList<>(List.of(new String[]{"=", "<", ">", "<=", ">="}));
    private final List<String> ARITHMETICOPERATORS = new ArrayList<>(List.of(new String[]{"+", "-", "*", "/", "^"}));
    private final List<String> BOOLEANVALUEDOPERATORS = Stream.of(LOGICALOPERATORS, COMPARATORS).flatMap(Collection::stream).collect(Collectors.toList());
    private final List<String> BINARYOPERATORS = Stream.of(LOGICALOPERATORS, COMPARATORS, ARITHMETICOPERATORS).flatMap(Collection::stream).collect(Collectors.toList());


    public List<String> getLogicalOperators() {
        return LOGICALOPERATORS;
    }

    public List<String> getComparators() {
        return COMPARATORS;
    }
    public List<String> getArithmeticOperators() { return ARITHMETICOPERATORS; }

    public List<String> getBooleanValuedOperators() { return BOOLEANVALUEDOPERATORS; }

    public List<String> getAllOperators() { return BINARYOPERATORS; }

    public Set<String> getBuildInFunctions() {
        return BUILDINFUNCTIONS;
    }

    public Set<String> getVariables() {
        return VARIABLES;
    }

    public Set<String> getSpecialCharacters() { return SPECIALCHARACTERS; }
}

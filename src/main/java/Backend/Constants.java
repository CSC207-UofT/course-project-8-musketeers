package Backend;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.util.Map.entry;

public class Constants {

    // List used for operators as the order of operators matter and is used to control the order of precedence in expressions.
    // Sets used for these as only need containment and order is irrelevant.
    private final Set<String> VARIABLES = Set.of("x", "y", "z");
//    private final Set<String> BUILTINFUNCTIONS = Set.of("cos", "sin", "tan", "sqrt", "mandel"); // TODO: We should be able to remove this from here as it's being stored in Axes
    private final Set<String> SPECIALCHARACTERS = Set.of("(", ")", ","); // Ted: TODO: Future maybe "\" for "\pi", "\floor", ...
    private final List<String> LOGICALOPERATORS = new ArrayList<>(List.of(new String[]{"|", "&"})); // TODO: Recheck The order or "AND" and "OR"!
    private final List<String> COMPARATORS = new ArrayList<>(List.of(new String[]{"<", ">", "<=", ">="}));
    private final List<String> ARITHMETICOPERATORS = new ArrayList<>(List.of(new String[]{"+", "-", "*", "/", "^"}));
//    private final Map<String, Integer> BUILTINFUNCTIONSANDINPUTSIZES = Map.ofEntries(
//            entry("cos", 1),
//            entry("sin", 1),
//            entry("tan", 1),
//            entry("sqrt", 1),
//            entry("mandel", 2)
//    );


    public List<String> getLogicalOperators() {
        return LOGICALOPERATORS;
    }

    public List<String> getComparators() {
        return COMPARATORS;
    }

    public List<String> getArithmeticOperators() { return ARITHMETICOPERATORS; }

    public List<String> getBooleanValuedOperators() {
        return Stream.of(LOGICALOPERATORS, COMPARATORS).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public List<String> getAllOperators() {
        return Stream.of(LOGICALOPERATORS, COMPARATORS, ARITHMETICOPERATORS).flatMap(Collection::stream).collect(Collectors.toList());
    }

//    public Set<String> getBuiltInFunctions() {
//        return BUILTINFUNCTIONS; // TODO: Can just use "BUILTINFUNCTIONSANDINPUTSIZES". But we'll see.
//    }

    public Set<String> getVariables() {
        return VARIABLES;
    }

    public Set<String> getSpecialCharacters() { return SPECIALCHARACTERS; }

//    public Map<String, Integer> getBuiltInFunctionsAndInputSizes() { return BUILTINFUNCTIONSANDINPUTSIZES; }
}

package Backend;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Constants {

    // List used for operators as the order of operators matter and is used to control the order of precedence in expressions.
    // Sets used for these as only need containment and order is irrelevant.
    private final Set<String> VARIABLES = Set.of("x", "y", "z");
    private final Set<String> BUILTINONEVARFUNCTIONS = Set.of("cos", "sin", "tan", "sqrt", "exp", "arctan", "arccos", "arcsin", "log");
    private final Set<String> BUILTINTWOVARFUNCTIONS = Set.of("mandel", "max", "min");
    private final Set<String> SPECIALCHARACTERS = Set.of("(", ")", ",");

    private final List<String> LOGICALOPERATORS = new ArrayList<>(List.of(new String[]{"|", "&"}));
    private final List<String> COMPARATORS = new ArrayList<>(List.of(new String[]{"<", ">", "<=", ">="}));
    private final List<String> ARITHMETICOPERATORS = new ArrayList<>(List.of(new String[]{"+", "-", "*", "/", "^"}));


    public List<String> getLogicalOperators() {
        return LOGICALOPERATORS;
    }

    public List<String> getComparators() {
        return COMPARATORS;
    }

    public List<String> getArithmeticOperators() {
        return ARITHMETICOPERATORS;
    }

    public List<String> getBooleanValuedOperators() {
        return Stream.of(LOGICALOPERATORS, COMPARATORS).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public List<String> getAllOperators() {
        return Stream.of(LOGICALOPERATORS, COMPARATORS, ARITHMETICOPERATORS).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public Set<String> getVariables() {
        return VARIABLES;
    }

    public Set<String> getOneVarFunctions() {
        return BUILTINONEVARFUNCTIONS;
    }

    public Set<String> getTwoVarFunctions() {
        return BUILTINTWOVARFUNCTIONS;
    }

    public Set<String> getBuiltinFunctions() {
        Set<String> allFuncs = new HashSet<>();
        allFuncs.addAll(BUILTINONEVARFUNCTIONS);
        allFuncs.addAll(BUILTINTWOVARFUNCTIONS);
        return allFuncs;
    }

    public Set<String> getSpecialCharacters() {
        return SPECIALCHARACTERS;
    }

}

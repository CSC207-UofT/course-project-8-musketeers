package Backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Constants {

    // List used for operators as the order of operators matter and is used to control the order
    // of precedence in expressions
    // sets used for these as only need containment and order is irrelevant
    private final List<String> OPERATORS = new ArrayList<>(List.of(new String[]{"+", "-", "/", "*", "^"}));
    private final List<String> COMPARATORS = new ArrayList<>(List.of(new String[]{"<=", ">=", "<", ">", "="}));
    private final List<String> LOGICALOPERATORS = new ArrayList<>(List.of(new String[]{"AND", "OR", "NOT"}));
    private final Set<String> VARIABLES = Set.of("x", "y", "z");
    private final Set<String> BUILDINFUNCTIONS = Set.of("cos", "sin", "tan", "sqrt", "mandel");
    private final Set<String> SPECIALCHARACTERS = Set.of("(", ")", ","); // Ted: TODO: Future maybe "\" for "\pi", "\floor", ...
    // public Set<String> USERDEFINEDFUNCTIONS = Set.of();

    public List<String> getOperators() { return OPERATORS; }

    public List<String> getComparators() {
        return COMPARATORS;
    }

    public List<String> getLogicalOperators() {
        return LOGICALOPERATORS;
    }

    public Set<String> getBuildInFunctions() {
        return BUILDINFUNCTIONS;
    }

//    public Set<String> getUserDefinedFunctions() { return USERDEFINEDFUNCTIONS; }

    public Set<String> getVariables() {
        return VARIABLES;
    }

    public Set<String> getSpecialCharacters() { return SPECIALCHARACTERS; }
}

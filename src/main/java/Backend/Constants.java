package Backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Constants {

    // List used for operators as the order of operators matter and is used to control the order
    // of precedence in expressions
    private final List<String> OPERATORS = new ArrayList<>(List.of(new String[]{"+", "-", "/", "*", "^"}));
    // sets used for these as only need containment and order is irrelevant
    private final Set<String> VARIABLES = Set.of("x", "y", "z");
    private final Set<String> FUNCTIONS = Set.of("cos", "sin", "tan", "sqrt");

    public List<String> getOperators() {
        return OPERATORS;
    }

    public Set<String> getFunctions() {
        return FUNCTIONS;
    }

    public Set<String> getVariables() {
        return VARIABLES;
    }
}

package Backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Constants {

    private final List<String> OPERATORS = new ArrayList<>(List.of(new String[]{"-", "+", "*", "/", "^"}));
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

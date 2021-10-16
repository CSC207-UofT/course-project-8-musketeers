package Backend;

import java.util.ArrayList;
import java.util.List;

public class Axes {

    private List<Expression> expressionList = new ArrayList<>();

    public void addExpression(Expression expr){
        expressionList.add(expr);
    }

    public List<Expression> getExpressions(){
        return expressionList;
    }

}

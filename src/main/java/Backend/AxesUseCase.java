package Backend;
import Backend.Exceptions.InvalidCommandArguments;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * A Use Case class for Axes
 * Responsible for changing attributes of axes
 */

public class AxesUseCase {
    /**
     * Saves Axes ax to fileName
     * @param fileName  String representing name of file
     * @param ax  instance of axes to be saved
     */
    public void saveAxes(String fileName, Axes ax) throws IOException {
        DataReadWriter d = new DataReadWriter();
        d.fileSave(fileName, ax);
    }

    /**
     * @param fileName  Name of file to deserialize
     * @return  an instance of Axes
     */
    public Axes loadAxes(String fileName) throws IOException, ClassNotFoundException {
        DataReadWriter d = new DataReadWriter();
        return d.fileRead(fileName);
    }



    /**
     * methods to get and/or change Axes attributes
     */
    public float getScale(Axes ax){return ax.getScale();}

    public void setScale(float scale, Axes ax) throws InvalidCommandArguments {
        if (scale <= 0) {
            throw new InvalidCommandArguments("Invalid Command Argument: " + scale +
                    " must be a positive float");
        } else {
            ax.setScale(scale);
        }
    }

    public float[] getOrigin(Axes ax){return ax.getOrigin();}

    public void setOrigin(float[] o, Axes ax){ax.setOrigin(o);}

    public List<Expression> getExpressions(Axes ax){
        return ax.getExpressions();
    }

    public void addExpression(Expression expr, Axes ax){
        List<Expression> list1 = ax.getExpressions();
        list1.add(expr);
        ax.setExpressions(list1);
        if (expr instanceof FunctionExpression){
            Map<String, FunctionExpression> namedExpressions = ax.getNamedExpressions();
            namedExpressions.put(expr.getItem(), (FunctionExpression) expr);
            ax.setNamedExpressions(namedExpressions);
        }
    }

    public void removeExpression(Expression expr, Axes ax){
        List<Expression> list1 = ax.getExpressions();
        list1.remove(expr);
        ax.setExpressions(list1);
        Map<String, FunctionExpression> namedExpressions = ax.getNamedExpressions();

        if (namedExpressions.containsKey(expr.getItem())){
            namedExpressions.remove(expr.getItem(), (FunctionExpression) expr);
        }
        ax.setNamedExpressions(namedExpressions);

    }

}

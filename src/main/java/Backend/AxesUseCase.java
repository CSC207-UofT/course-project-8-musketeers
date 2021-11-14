package Backend;
import Backend.DataReadWriter;
import Backend.Exceptions.InvalidCommandArguments;

import java.io.IOException;
import java.util.List;

/**
 * A Use Case class for Axes
 * Should be able to save instances of Axes and load saved Axes
 * file name will be provided from command line
 */

public class AxesUseCase {


    public void saveAxes(String fileName, Axes ax) throws IOException {
        DataReadWriter d = new DataReadWriter();
        d.fileSave(fileName, ax);

    }
    public Axes loadAxes(String fileName) throws IOException, ClassNotFoundException {
        DataReadWriter d = new DataReadWriter();
        return d.fileRead(fileName);
    }

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
        ax.addExpression(expr);
    }

    public void removeExpression(Expression expr, Axes ax){ax.removeExpression(expr);}







}

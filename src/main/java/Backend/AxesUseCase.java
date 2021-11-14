package Backend;
import Backend.Exceptions.InvalidCommandArguments;
import Backend.Expressions.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * A Use Case class for Axes
 * Should be able to save instances of Axes and load saved Axes
 * file name will be passed from command line
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

    public List<RealValuedExpression> getExpressions(Axes ax){
        return ax.getExpressions();
    }

    public void addExpression(RealValuedExpression expr, Axes ax){
        ax.addExpression(expr);
    }

    public void removeExpression(RealValuedExpression expr, Axes ax){ax.removeExpression((RealValuedExpression) expr);}

    public Map<String, FunctionExpression> getNamedFunctions(Axes ax){
        return ax.getNamedExpressions();
    }


}


package GUI;

import Backend.Axes;
import Backend.AxesUseCase;
import Graphics.Grapher;

/**
 * A controller for GUI classes
 */
public class GUIHelper {
    public int imgDim;
    private Grapher grapher;
    private AxesUseCase auc;
    private String gType;

    public GUIHelper(Grapher g, int dim) {
        grapher = g;
        auc = new AxesUseCase();
        imgDim = dim;
    }
    public void setgType(String g) {
        gType = g;
    }
    public void setGraphPos(float[] p) {
        grapher.setPos(p);
    }
    public int[] drawGraph() {
        return grapher.graph(imgDim,gType);
    }
}
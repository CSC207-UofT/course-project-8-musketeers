
package GUI;

import Graphics.Grapher;

/**
 * A controller for GUI classes
 */
public class GUIHelper {
    public int imgDim;
    private final Grapher grapher;
    private String gType;

    public GUIHelper(Grapher g, int dim) {
        grapher = g;
        imgDim = dim;
    }
    public void setgType(String g) {
        gType = g;
    }
    public void setGraphPos(float[] p) {
        grapher.setPos(p);
    }
    public void setGraphScale(float scale) { grapher.setScale(scale); }
    public int[] drawGraph() {
        return grapher.graph(imgDim,gType);
    }
}
package Graphics;

import Backend.*;
import Backend.Expressions.FunctionExpression;
import Backend.Expressions.RealValuedExpression;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * This class is the main link between Backend and Graphics.
 * It runs over the list of Expressions in Axes and calls the appropriate graphers
 */
public class Grapher {

    private final Axes axes;
    private final ExplicitGrapher expGrapher = new ExplicitGrapher(); // Explicit graphers for explicit functions
    private final ImplicitGrapher impGrapher = new ImplicitGrapher(); // Implicit grapher for implicit functions
    private final AxesUseCase auc = new AxesUseCase(); // Use case class to interact with Axes
    private final AxesDrawer ad = new AxesDrawer(); // AxesDrawer to draw Axes and grids

    /**
     * @param axes Axes object that we are graphing
     */
    public Grapher(Axes axes){
        this.axes = axes;
    }

    /**
     * @param size Size of the output image
     * @param gType The type of graphs, one of BOUNDARY, REGION, GRAYSCALE (for now)
     * @param imgName Name of the output image file
     * @throws IOException If image cannot be saved
     */
    public void graph(int size, String gType, String imgName) throws IOException {
        int[] pixels = new int[size*size];

        Arrays.fill(pixels, impGrapher.WHITE);

        float[] graphData = new float[]{auc.getScale(axes), auc.getOrigin(axes)[0], auc.getOrigin(axes)[1]};

        for (RealValuedExpression exp: auc.getExpressions(axes)) {
            if (exp instanceof FunctionExpression){
                expGrapher.graph(pixels, size, size, exp, graphData);
            }
            else {
                impGrapher.graph(pixels, size, size, exp, graphData, stringToGType(gType));
            }
        }
        pixels = ad.drawAxes(pixels, size, size, graphData);
        pixels = ad.drawGrid(pixels, size, size, graphData);
        ImageWriter writer = new ImageWriter();
        writer.writeImage(pixels, size, size, imgName);
    }

    /**
     * @param gtype The type of graph that is to be graphed
     * @return The type of graph that is to be graphed from the GraphType enum
     */
    private GraphType stringToGType(String gtype){
        Map<String, GraphType> gtypeMap = Map.of(
                "BOUNDARY", GraphType.BOUNDARY,
                "REGION", GraphType.REGION,
                "GRAYSCALE", GraphType.GRAYSCALE);
        return gtypeMap.get(gtype);
    }


}

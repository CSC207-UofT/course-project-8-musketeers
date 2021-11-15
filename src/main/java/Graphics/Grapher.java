package Graphics;

import Backend.*;
import Backend.Expressions.FunctionExpression;
import Backend.Expressions.RealValuedExpression;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Grapher {

    private final Axes axes;
    private final ExplicitGrapher expGrapher = new ExplicitGrapher();
    private final ImplicitGrapher impGrapher = new ImplicitGrapher();
    private final AxesUseCase auc = new AxesUseCase();
    private final AxesDrawer ad = new AxesDrawer();

    public Grapher(Axes axes){
        this.axes = axes;
    }

    public void graph(int size, String gType, String imgName) throws IOException {
        int[] pixels = new int[size*size];
        float[] graphData = new float[]{auc.getScale(axes), auc.getOrigin(axes)[0], auc.getOrigin(axes)[1]};

        for (RealValuedExpression exp: axes.getExpressions()) {
            if (exp instanceof FunctionExpression){
                expGrapher.graph(pixels, size, size, exp, graphData);
            }
            else {
                impGrapher.graph(pixels, size, size, exp, graphData, stringToGType(gType));
            }
        }
        pixels = ad.drawAxes(pixels, size, size, graphData);
        writeImage(pixels, size, size, imgName);
    }

    private void writeImage(int[] pixels, int iw, int ih, String fname) throws IOException {
        BufferedImage outImage = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_ARGB);

        outImage.setRGB(0, 0, iw, ih, pixels, 0, iw);

        ImageIO.write(outImage, "png", new File(fname));
    }

    private GraphType stringToGType(String gtype){
        Map<String, GraphType> gtypeMap = Map.of(
                "BOUNDARY", GraphType.BOUNDARY,
                "REGION", GraphType.REGION,
                "GRAYSCALE", GraphType.GRAYSCALE);
        return gtypeMap.get(gtype);
    }


}

package basic;

import static basic.ImageTest.*;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sin;

public class RaymarcherTest {
    public static String fmtHex255(int i) {
        // String.valueOf is necessary otherwise it treats char as numeric.
        return String.valueOf("0123456789ABCDEF".charAt(i>>4))
                + String.valueOf("0123456789ABCDEF".charAt(i&15));
    }

    // IQ polynomial smooth min
    public static float sminCubic( float a, float b, float k ) {
        float h = (float)max( k-Math.abs(a-b), 0.0 )/k;
        return (float)(min( a, b ) - h*h*h*k*(1.0/6.0));
    }

    public static class Func1 implements Evaluatable3D {
        public float evaluate(float cx, float cy, float cz) {
            //return sphere(cx, cy, cz, new float3(0.5f,0,0), 2);
            return sminCubic(
                    sminCubic(sphere(cx, cy, cz, new float3(-1,0,0), 2),
                        sphere(cx, cy, cz, new float3(1.5f,1,1), 3), 0.5f)
                    , sphere(cx, cy, cz, new float3(0, -5, 0), 4), 0.8f)
                    + (float)(0.05f*sin(cx*3)*sin(cy*3)*sin(cz*3))
                    + (float)(0.03f*sin((cx+8)*5)*sin((cy+6)*5)*sin((cz+5)*5));
        }
        public float sphere(float cx, float cy, float cz, float3 o, float r) {
            return (float)(Math.sqrt((cx-o.x)*(cx-o.x)
                                     + (cy-o.y)*(cy-o.y)
                                     + (cz-o.z)*(cz-o.z)) - r);
        }
    }


   public static void main(String args[]) throws Exception {
      int size = 256;
      int[] mainPixels = new int[size*size];
      int[] dims1 = {size,size};

      Evaluatable3D f1 = new Func1();
      raymarch(mainPixels, dims1[0], dims1[1], f1);
      writeImage(mainPixels, dims1[0], dims1[1], "sampleOut3D.png");
   }


  public static void raymarch(int[] pixels, int w, int h, Evaluatable3D func) {
      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {


            float cx = (float)(x - w/2) / (float)(w/2);
            float cy = -(float)(y - h/2) / (float)(h/2);

            // Ray origin, ray direction
            float3 ro = new float3(0.f, 0.f, -5.f);
            float3 rd = new float3(cx, cy, 1);

            float3 pos = ro;

            boolean hit = false;

            for (int i = 0; i < 128; i++) {
                // func must be a Signed Distance Function for now
                // but can/will extend to arbitrary implicit functions
                float val = func.evaluate(pos.x, pos.y, pos.z);

                if (val <= 0) {
                    hit = true;
                    break;
                }

                // Minimum step size
                val = max(0.01f, val);
                pos = pos.plus(rd.mult(val));
             }
            if (hit) {
                pixels[y*w+x] = (int)Long.parseLong(doLighting(func, pos, rd), 16);
            } else {
                pixels[y*w+x] = (int)Long.parseLong("FF121618", 16);
            }
         }
      }
  }
  public static String doLighting(Evaluatable3D func, float3 pos, float3 rd) {
      float3 norm = func.deriv(pos.x, pos.y, pos.z);


      float3 lightDir = new float3(1,1,-1).normalize();
      float3 bounceDir = new float3(-1.2f, -1, 0.2f).normalize();

      // Diffuse Lambert
      float3 light = new float3(1,0.8f,0.6f).mult(max(0, norm.dot(lightDir)));
      // rear/bounce
      light = light.plus(new float3(0.2f, 0.3f, 0.5f).mult(max(0, norm.dot(bounceDir))));

      // Specular
      float3 vv = rd.mult(-1);
      light = light.plus(new float3((float)max(0, Math.pow(norm.dot((lightDir.plus(vv)).normalize()), 128))));

      // Ambient
      light = light.plus(new float3(0.02f));

      // Clamp
      light.x = min(1, light.x);
      light.y = min(1, light.y);
      light.z = min(1, light.z);

      String outR = fmtHex255((int)(255*Math.sqrt(light.x)));
      String outG = fmtHex255((int)(255*Math.sqrt(light.y)));
      String outB = fmtHex255((int)(255*Math.sqrt(light.z)));

      return "FF" + outR + outG + outB;
  }
}
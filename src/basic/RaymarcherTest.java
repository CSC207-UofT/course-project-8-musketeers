package basic;

import static basic.ImageTest.*;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sin;

public class RaymarcherTest {
    public static String fmtHex255(int i) {
        return String.valueOf("0123456789ABCDEF".charAt(i>>4))
                + String.valueOf("0123456789ABCDEF".charAt(i&15));
    }

    // IQ polynomial smooth min (k = 0.1);
    public static float sminCubic( float a, float b, float k ) {
        float h = (float)max( k-Math.abs(a-b), 0.0 )/k;
        return (float)(min( a, b ) - h*h*h*k*(1.0/6.0));
    }

    public static class float3 {
        public float x;
        public float y;
        public float z;
        public float3(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        public float3(float a) {
            this.x = a;
            this.y = a;
            this.z = a;
        }
        public float3 normalize() {
            float d = (float)Math.sqrt(x*x+y*y+z*z);
            return new float3(x/d, y/d, z/d);
        }
        public float dot(float3 other) {
            return x*other.x + y*other.y + z*other.z;
        }
        public float3 plus(float3 other) {
            return new float3(x+other.x, y+other.y, z+other.z);
        }
        public float3 mult(float c) {
            return new float3(x*c, y*c, z*c);
        }
    }

    public interface TestFunction {
        public float evaluate(float cx, float cy, float cz);

        default float3 deriv(float cx, float cy, float cz) {
            float e = 0.01f;
            float c = evaluate(cx, cy, cz);
            return new float3(
                    evaluate(cx+e, cy, cz) - c,
                    evaluate(cx, cy+e, cz) - c,
                    evaluate(cx, cy, cz+e) - c
            ).normalize();
            //return new float3(0,0,0);
        };
    }
    public static class Func1 implements TestFunction {
        public float evaluate(float cx, float cy, float cz) {
            //return sphere(cx, cy, cz, new float3(0.5f,0,0), 2);
            return //sminCubic(
                    sminCubic(sphere(cx, cy, cz, new float3(-1,0,0), 2),
                        sphere(cx, cy, cz, new float3(1.5f,1,1), 3), 0.5f)
                    //, sphere(cx, cy, cz, new float3(0, -5, 0), 4), 0.8f)
                    + (float)(0.05f*sin(cx*3)*sin(cy*3)*sin(cz*3))
                    + (float)(0.03f*sin((cx+8)*5)*sin((cy+6)*5)*sin((cz+5)*5));
        }
        public float sphere(float cx, float cy, float cz, float3 o, float r) {
            return (float)(Math.sqrt((cx-o.x)*(cx-o.x)
                                     + (cy-o.y)*(cy-o.y)
                                     + (cz-o.z)*(cz-o.z))
                            - r);
        }
    }


   public static void main(String args[]) throws Exception {
       int size = 256;
      int[] mainPixels = new int[size*size];
      int[] dims1 = {size,size};

      TestFunction f1 = new Func1();
      raymarch(mainPixels, dims1[0], dims1[1], f1);
      writeImage(mainPixels, dims1[0], dims1[1], "sampleOut3D.png");
   }

   public static String print3(float[] p) {
        return p[0] + " " + p[1] + " " + p[2];
   }

  public static void raymarch(int[] pixels, int w, int h, TestFunction func) {
      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
             //if ((x != w/2) || (y != h/2)) continue;
             //if ((x != w-1) || (y != h-1)) continue;


             float cx = (float)(x - w/2) / (float)(w/2);
            float cy = -(float)(y - h/2) / (float)(h/2);

            // Ray origin, ray direction
            float[] ro = {0.f, 0.f, -5.f};
            float[] rd = {cx, cy, 1};

            float[] pos = ro;

            boolean hit = false;

            //System.out.println("ro " + print3(ro) + " rd " + print3(rd));

            for (int i = 0; i < 128; i++) {
                float val = func.evaluate(pos[0], pos[1], pos[2]);

                //System.out.println("pos " + print3(pos));
                //System.out.println("val " + val);

                if (val <= 0) {
                    hit = true;
                    break;
                }

                val = max(0.01f, val);
                pos[0] += val*rd[0];
                pos[1] += val*rd[1];
                pos[2] += val*rd[2];
             }
            if (hit) {
                float3 norm = func.deriv(pos[0], pos[1], pos[2]);


                float3 lightDir = new float3(1,1,-1).normalize();
                float3 bounceDir = new float3(-1.2f, -1, 0.2f).normalize();

                // Diffuse Lambert
                float3 light = new float3(1,0.8f,0.6f).mult(max(0, norm.dot(lightDir)));
                // rear/bounce
                light = light.plus(new float3(0.2f, 0.3f, 0.5f).mult(max(0, norm.dot(bounceDir))));

                // Specular
                float3 vv = new float3(-rd[0], -rd[1], -rd[2]);
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
                pixels[y*w+x] = (int)Long.parseLong("FF" + outR + outG + outB, 16);
            } else {
                pixels[y*w+x] = (int)Long.parseLong("FF121618", 16);
            }
         }
      }
  }
}
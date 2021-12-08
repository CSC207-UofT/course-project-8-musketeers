/*
 * Adapted from LWJGL example
 */

package GUI;

import Graphics.RGBA;

import javax.swing.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static Graphics.ImageTest.getImDims;
import static Graphics.ImageTest.readImage;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.*;


public class DemoTest {
    static int clicks;
    static int progID;

    static float zx;
    static float zy;
    static float mousex;
    static float mousey;

    static boolean textureTest;

    String equation;

    public DemoTest(String eq) {
        this.equation = eq;
    }

    public static void main(String[] args) throws IOException {
        textureTest = false;
        String eq = JOptionPane.showInputDialog(null, "Enter"); // Has to be implicit for now.
//        String eq = "(cos(x + y) + sin(x*y))/4 + 0.5";
        if (args.length > 0) { // If main has at least one argument, then the first one is the equation.
            eq = args[0];
        }
        if (args.length > 1) {
            textureTest = true;
            System.out.println("testing texture");
        }
        DemoTest guiDemo = new DemoTest(eq);
        guiDemo.initGL();
        System.out.println("Finished."); // After the program ends.
    }

    public static int imgToTex(int[] pixels, int iw, int ih) {
        ByteBuffer tbuf = ByteBuffer.allocateDirect(4 * iw * ih);
        byte[] pixbytes = new byte[4 * iw * ih];
        for (int i = 0; i < iw * ih; i++) {
            RGBA rgba = new RGBA(pixels[i]);
            pixbytes[4 * i] = (byte) (rgba.r);
            pixbytes[4 * i + 1] = (byte) (rgba.g);
            pixbytes[4 * i + 2] = (byte) (rgba.b);
            pixbytes[4 * i + 3] = (byte) (rgba.a);
        }
        tbuf.put(pixbytes);
        tbuf.flip();
        int tid = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, tid);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, iw, ih, 0, GL_RGBA, GL_UNSIGNED_BYTE, tbuf);
        return tid;
    }

    public static void makeShader(String eq) throws IOException {
        String vertShader;
        String fragShader;

        vertShader = new String(Files.readAllBytes(Paths.get("src/main/java/GUI/basicVertex.c")));
        fragShader = new String(Files.readAllBytes(Paths.get("src/main/java/GUI/demoFrag.c")));

        fragShader = fragShader.replace("[INSERT EQUATION HERE]", eq);

        if (textureTest) {
            fragShader = fragShader.replace("//[INSERT TEXTURE TEST]", "fragColor = texture(texTest, tc*wh);");
        }

        System.out.println(fragShader);

        progID = glCreateProgram();
        int vsID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vsID, vertShader);
        glCompileShader(vsID);
        if (glGetShaderi(vsID, GL_COMPILE_STATUS) != GL_TRUE) {
            System.out.println(glGetShaderInfoLog(vsID, glGetShaderi(vsID, GL_INFO_LOG_LENGTH)));
        }
        System.out.println("vs created");

        int fsID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fsID, fragShader);
        glCompileShader(fsID);
        if (glGetShaderi(fsID, GL_COMPILE_STATUS) != GL_TRUE) {
            System.out.println(glGetShaderInfoLog(fsID, glGetShaderi(fsID, GL_INFO_LOG_LENGTH)));
        }
        System.out.println("fs created");


        glAttachShader(progID, vsID);
        glAttachShader(progID, fsID);
        glLinkProgram(progID);
    }

    private static void cursor_pos_callback(long l, double x, double y) {
        mousex = (float) (x - 400) / 200.f;
        mousey = -(float) (y - 400) / 200.f;
        glUniform1f(0, mousex + zx);
        glUniform1f(1, mousey + zy);
    }

    public void initGL() throws IOException {
        glfwInit();
        long window = createWindow();
        glfwSetMouseButtonCallback(window, DemoTest::mouseCallback); // Records when the mouse button is being pressed.
        glfwSetCursorPosCallback(window, DemoTest::cursor_pos_callback); // Records the position of the cursor.

        FloatBuffer buffer = memAllocFloat(3 * 2 * 2);
        float[] vtest = {
                -0.9f, -0.9f, 0.9f, -0.9f, -0.9f, 0.9f,
                0.9f, -0.9f, -0.9f, 0.9f, 0.9f, 0.9f
        };
        buffer.put(vtest);

        buffer.flip();

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);


        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0L);
        glEnableVertexAttribArray(0);

        int vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);


        makeShader(this.equation);
        glUseProgram(progID);
        memFree(buffer);

        glEnableClientState(GL_VERTEX_ARRAY);
        glVertexPointer(2, GL_FLOAT, 0, 0L);

        glClearColor(0.1f, 0.2f, 0.3f, 0.0f);
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            glDrawArrays(GL_TRIANGLES, 0, 6);

            glfwSwapBuffers(window);
        }
        glfwTerminate();
    }

    /**
     * In this example, we will use OpenGL to draw a single triangle on a window.
     * <p>
     * As mentioned above, we have to use off-heap memory for this in order to communicate the virtual memory address to
     * OpenGL, which in turn will read the data we provided at that address.
     * <p>
     * The example here will upload the position vectors of a simple triangle to an OpenGL Vertex Buffer Object.
     */


    private static long createWindow() {
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        long window = glfwCreateWindow(800, 800, "Intro2", NULL, NULL);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        createCapabilities();
        return window;
    }

    private static void mouseCallback(long win, int button, int action, int mods) {
        /* Print a message when the user pressed down a mouse button */
        if (action == GLFW_PRESS) {
            int tid = 0;
            try {
                int iw = getImDims("sampleOut3D.png")[0];
                int ih = getImDims("sampleOut3D.png")[1];
                tid = imgToTex(readImage("sampleOut3D.png"), iw, ih);
            } catch (Exception e) {
                System.out.println("Can't read image");
            }
            glUniform1i(3, tid);

            System.out.println("Pressed! " + clicks);
            if (button == GLFW_MOUSE_BUTTON_LEFT) {
                clicks += 1;
            } else {
                clicks -= 1;
            }
            glUniform1f(2, 1.f / (float) Math.pow(1.1f, clicks));

            zx += mousex;
            zy += mousey;
            glUniform1f(0, mousex + zx);
            glUniform1f(1, mousey + zy);
        }
    }
}

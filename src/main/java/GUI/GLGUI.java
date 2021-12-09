/*
 * Adapted from LWJGL examples
 * Copyright 2012-2021 Lightweight Java Game Library
 * BSD 3-clause License
 * License terms: https://www.lwjgl.org/license
 */

package GUI;

import Graphics.Grapher;
import Graphics.RGBA;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

/**
 * A User Interface which uses GLFW
 * TODO: split into UI, Controller, Presenter
 */
public class GLGUI extends GLFWApp implements GUI {
    private int progID;

    // To comply with GLFW specifications these need to be static
    static float mousex;
    static float mousey;
    static boolean dragMove = false;
    static boolean prevDragMove = false;
    static float prevMouseX = 0;
    static float prevMouseY = 0;
    static float initialMouseX = 0;
    static float initialMouseY = 0;
    static float changeInMouseX = 0;
    static float changeInMouseY = 0;
    static float graphScale = 5;
    static float scaleInterval = 1.1f;
    static float panSensitivity = 5.f;

    private final int imgDim;

    private GUIHelper guiHelper;

    public GLGUI(Grapher grapher, int imgDim) {
        this.imgDim = imgDim;
        this.guiHelper = new GUIHelper(grapher, imgDim);
    }

    public void setgType(String gType) {
        guiHelper.setgType(gType);
    }

    /**
     * Converts an int[] RGBA data to a GL texture
     * set to uniform 0
     *
     * @param pixels array representing image
     * @param iw     width of input
     * @param ih     height of input
     */
    private static void imgToTex(int[] pixels, int iw, int ih) {
        // Convert int[] RGBA to packed byte[] RGBA for OpenGL use
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
        // Attach to GL texture, set filtering
        int tid = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, tid);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, iw, ih, 0, GL_RGBA, GL_UNSIGNED_BYTE, tbuf);
    }

    /**
     * Compiles and links GL shader templates
     */
    private void makeShader() throws IOException {
        String vertShader;
        String fragShader;

        vertShader = new String(Files.readAllBytes(Paths.get("src/main/java/GUI/basicVertex.c")));
        fragShader = new String(Files.readAllBytes(Paths.get("src/main/java/GUI/demoFrag.c")));

        fragShader = fragShader.replace("[INSERT EQUATION HERE]", "1");

        fragShader = fragShader.replace("//[INSERT TEXTURE TEST]", "fragColor = texture(texTest, tc*wh);");

        progID = glCreateProgram();
        int vsID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vsID, vertShader);
        glCompileShader(vsID);
        if (glGetShaderi(vsID, GL_COMPILE_STATUS) != GL_TRUE) {
            System.out.println(glGetShaderInfoLog(vsID, glGetShaderi(vsID, GL_INFO_LOG_LENGTH)));
        }

        int fsID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fsID, fragShader);
        glCompileShader(fsID);
        if (glGetShaderi(fsID, GL_COMPILE_STATUS) != GL_TRUE) {
            // Error compiling fragment shader
            System.out.println(glGetShaderInfoLog(fsID, glGetShaderi(fsID, GL_INFO_LOG_LENGTH)));
        }

        glAttachShader(progID, vsID);
        glAttachShader(progID, fsID);
        glLinkProgram(progID);
    }

    public void initGUI() {
        try {
            initGL();
        } catch (IOException e) {
            System.out.println("Error reading assets!");
        }
    }

    /**
     * Initializes the GLFW and GL environments.
     */
    private void initGL() throws IOException {
        glfwInit();
        long window = createWindow();
        glfwSetMouseButtonCallback(window, GLGUI::mouseCallback);
        glfwSetCursorPosCallback(window, GLGUI::cursor_pos_callback);
        glfwSetKeyCallback(window, GLGUI::keyboardCallback);

        setupGL();
        makeShader();
        glUseProgram(progID);

        startLoop(window);
    }

    /**
     * Enters mainloop for UI window
     *
     * @param window handle of the window
     */
    private void startLoop(long window) {
        int[] pixels;
        while (!glfwWindowShouldClose(window)) {
            float[] newO = {prevMouseX + changeInMouseX, prevMouseY + changeInMouseY};
            guiHelper.setGraphPos(newO);
            guiHelper.setGraphScale(graphScale);
            pixels = guiHelper.drawGraph();
            imgToTex(pixels, this.imgDim, this.imgDim);

            glfwPollEvents();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            glDrawArrays(GL_TRIANGLES, 0, 6);

            glfwSwapBuffers(window);
        }
        glfwTerminate();
    }



    private static void cursor_pos_callback(long l, double x, double y) {
        mousex = (float) (x - 400) / 200.f;
        mousey = (float) (y - 400) / 200.f;

        if (dragMove) {
            if (!prevDragMove) {
                initialMouseX = mousex;
                initialMouseY = mousey;
                prevDragMove = true;
            } else {
                changeInMouseX = -(mousex - initialMouseX) * graphScale / panSensitivity;
                changeInMouseY = (mousey - initialMouseY) * graphScale / panSensitivity;
            }
        } else {
            prevMouseX += changeInMouseX;
            prevMouseY += changeInMouseY;
            changeInMouseX = 0;
            changeInMouseY = 0;
        }
    }

    private static void mouseCallback(long win, int button, int action, int mods) {
        // Below Ted: Mouse drag:
        if (action == GLFW_PRESS && button == GLFW_MOUSE_BUTTON_LEFT) {
            dragMove = true;
        }
        if (action == GLFW_RELEASE && button == GLFW_MOUSE_BUTTON_LEFT) {
            dragMove = false;
            prevDragMove = false;
        }
    }

    private static void keyboardCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (key == GLFW_KEY_UP) {
                graphScale *= scaleInterval;
            } else if (key == GLFW_KEY_DOWN) {
                graphScale /= scaleInterval;
            }
        }
    }
}

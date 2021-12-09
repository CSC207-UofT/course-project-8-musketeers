/*
 * Adapted from LWJGL examples
 * Copyright 2012-2021 Lightweight Java Game Library
 * BSD 3-clause License
 * License terms: https://www.lwjgl.org/license
 */

package GUI;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;


public class Demo3D extends GLFWApp {
    /**
     * This is a demonstration of 3D implicit / explicit graphing
     *   using OpenGL fragment shaders.
     *   It is not integrated with the rest of our project yet.
     */

    static int clicks;
    static int progID;

    static float zx;
    static float zy;
    static float mousex;
    static float mousey;

    public Demo3D() {
    }

    /**
     * Create vertex shaders and fragment shaders. An OpenGL Technical thing. Shaders are small programs that run in
     * parts of GPU in parallel.
     */
    public static void makeShader() throws IOException {
        String vertShader;
        String fragShader;

        vertShader = new String(Files.readAllBytes(Paths.get("src/main/java/GUI/basicVertex.c")));
        fragShader = new String(Files.readAllBytes(Paths.get("src/main/java/GUI/demoFrag3D.c")));


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

    /**
     * Track the positions of cursor in the associated window.
     * @param l References the window.
     * @param x x position of the cursor.
     * @param y y position of the cursor.
     */
    private static void cursor_pos_callback(long l, double x, double y) {
        mousex = (float) (x - 400) / 200.f;
        mousey = -(float) (y - 400) / 200.f;
        glUniform1f(0, mousex + zx);
        glUniform1f(1, mousey + zy);
    }


    public static void main(String[] args) throws IOException {
        glfwInit();
        Demo3D demo = new Demo3D();
        long window = demo.createWindow();
        glfwSetMouseButtonCallback(window, Demo3D::mouseCallback);
        glfwSetCursorPosCallback(window, Demo3D::cursor_pos_callback);

        demo.setupGL();
        makeShader();
        glUseProgram(progID);

        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            glDrawArrays(GL_TRIANGLES, 0, 6);

            glfwSwapBuffers(window);
        }
        glfwTerminate();
        System.out.println("Fin.");
    }

    /**
     * Track the information (clicking) from the mouse.
     * @param win References the window.
     * @param button the button being activated
     * @param action Action like press or release
     * @param mods mode.
     */
    private static void mouseCallback(long win, int button, int action, int mods) {
        /* Print a message when the user pressed down a mouse button */
        if (action == GLFW_PRESS) {
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

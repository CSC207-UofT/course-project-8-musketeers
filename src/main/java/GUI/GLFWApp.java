
package GUI;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.*;

public class GLFWApp {

    /**
     * Prepares an OpenGL context which
     *   contains a rectangle ready to
     *   be drawn on using shaders.
     */
    protected void setupGL() {
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

        memFree(buffer);

        glEnableClientState(GL_VERTEX_ARRAY);
        glVertexPointer(2, GL_FLOAT, 0, 0L);
        glClearColor(0.1f, 0.2f, 0.3f, 0.0f);
    }

    /**
     * Method that creates a GLFW window
     * @return window handle
     */
    protected long createWindow() {
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        long window = glfwCreateWindow(800, 800, "CSC207 Project", NULL, NULL);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        createCapabilities();
        return window;
    }
}
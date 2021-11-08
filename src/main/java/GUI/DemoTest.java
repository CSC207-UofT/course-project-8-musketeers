/*
 * Adapted from LWJGL example
 */

package GUI;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;


public class DemoTest {
    static int clicks;
    static int progID;

    static float zx;
    static float zy;
    static float mousex;
    static float mousey;

    public static void makeShader() {
        String vertShader = """
				#version 330
				layout (location = 0) in vec3 aPos;

				void main() {
				    gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);
				}""";
        String fragShader = """
				#version 330
				out vec4 fragColor;
				uniform float xpos;
				uniform float ypos;
				uniform float vscale;

				float mandel (float cx, float cy) {
					float x = 0;
					float y = 0;
					int i;
					for (i = 0; i < 100; i++) {
						if ((x*x + y*y) > 4) {
						  break;
						}
						float xtemp = x*x - y*y + cx;
						y = 2*x*y + cy;
						x = xtemp;
					}
					return sqrt(i / 100.f);
				}

				void main() {
				  vec2 tc = gl_FragCoord.xy;
				  vec2 wh = 1 / vec2(800, 800);
				  float cx = (((tc*wh).x - 0.5f) * 1.f + xpos);
				  float cy = (((tc*wh).y - 0.5f) * 1.f + ypos);
				  float m = mandel(cx * vscale, cy * vscale);
				  fragColor = vec4(m, m, m, 1.0);
				  //fragColor = vec4((tc*wh).x, (tc*wh).y, 0.6, 1.0);
				}""";

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
        mousex = (float)(x-400)/200.f;
        mousey = -(float)(y-400)/200.f;
        glUniform1f(0, mousex + zx);
        glUniform1f(1, mousey + zy);
    }

    /**
     * In this example, we will use OpenGL to draw a single triangle on a window.
     * <p>
     * As mentioned above, we have to use off-heap memory for this in order to communicate the virtual memory address to
     * OpenGL, which in turn will read the data we provided at that address.
     * <p>
     * The example here will upload the position vectors of a simple triangle to an OpenGL Vertex Buffer Object.
     */
    public static void main(String[] args) {
        glfwInit();
        long window = createWindow();
        glfwSetMouseButtonCallback(window, DemoTest::mouseCallback);
        glfwSetCursorPosCallback(window, DemoTest::cursor_pos_callback);

        FloatBuffer buffer = memAllocFloat(3 * 2*2);
        float[] vtest = {
                -0.9f,-0.9f,0.9f,-0.9f,-0.9f,0.9f,
                0.9f,-0.9f,-0.9f,0.9f,0.9f,0.9f
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

        makeShader();
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
        System.out.println("Fin.");
    }

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
            System.out.println("Pressed! " + clicks);
            if (button == GLFW_MOUSE_BUTTON_LEFT) {
                clicks += 1;
            } else {clicks -= 1;}
            glUniform1f(2, 1.f/(float)Math.pow(1.1f,clicks));

            zx += mousex;
            zy += mousey;
            glUniform1f(0, mousex + zx);
            glUniform1f(1, mousey + zy);
        }
    }
}


package GUI;

/**
 * An interface adapter / presenter
 * to invert dependency on a concrete GUI class
 */
public interface GUI{
    void initGUI();
    void setgType(String gType);
}
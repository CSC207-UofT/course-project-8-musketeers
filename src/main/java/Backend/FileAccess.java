package Backend;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileAccess {
    void fileSave(String fileName, Object graphs) throws IOException;

    Axes fileRead(String fileName) throws IOException, ClassNotFoundException;

}

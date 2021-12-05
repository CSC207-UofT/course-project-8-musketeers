package Backend;

import Backend.Expressions.FileAccess;

import java.io.*;

public class DataReadWriter implements FileAccess {
    /**
     *
     * @param fileName the name of the file
     * @param graphs Axes object I think
     * @throws IOException in case it fails somehow
     */

    public void fileSave(String fileName, Object graphs) throws IOException{
        OutputStream file = new FileOutputStream(fileName);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(graphs);
        output.close();
    }

    /**
     *
     * @param fileName name of the file
     * @return An ArrayList of Axes
     * @throws IOException In case it fails
     * @throws ClassNotFoundException in case it fails
     */

    public Axes fileRead(String fileName) throws IOException, ClassNotFoundException {
        InputStream file = new FileInputStream(fileName);
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream(buffer);
        return (Axes) input.readObject();
    }

}

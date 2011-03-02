package taberystwyth.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class DataDirectory {
    
    private static final Logger LOG = Logger.getLogger(DataDirectory.class);
    
    private static final DataDirectory INSTANCE = new DataDirectory();
    
    public static DataDirectory getInstance() {
        return INSTANCE;
    }
    
    private DataDirectory() {
        /* VOID */
    }
    
    /**
     * The path to the data directory. On unix this is "~/.taberystwyth/"
     */
    private final String DIRECTORY_PATH = System.getProperty("user.home")
                    + System.getProperty("file.separator") + ".taberystwyth"
                    + System.getProperty("file.separator");
    
    /**
     * The path to the index file. On unix this is "~/.taberystwyth/index"
     */
    private final String INDEX_PATH = DIRECTORY_PATH + "index";
    
    /**
     * Returns the current index, unless there is none, in which case it
     * returns an empty index
     * 
     * @return the current index
     */
    public HashMap<String, String> getIndex() {
        HashMap<String, String> returnValue = null;
        File index = new File(INDEX_PATH);
        try {
            FileInputStream fis = new FileInputStream(index);
            ObjectInputStream ois = new ObjectInputStream(fis);
            returnValue = (HashMap<String, String>) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            returnValue = new HashMap<String, String>();
        }
        return returnValue;
    }
    
    /**
     * Sets the current index with the argument provided
     * 
     * @param map
     *            the new index
     */
    public void setIndex(HashMap<String, String> map) {
        LOG.info("Index file is : " + INDEX_PATH);
        File index = new File(INDEX_PATH);
        try {
            /*
             * Ensure that the data directory has been created
             */
            createDirectory();
            
            /*
             * Delete and recreate the index file
             */
            if (index.exists()) {
                index.delete();
            }
            index.createNewFile();
            
            FileOutputStream fos = new FileOutputStream(index);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(map);
            fos.close();
            oos.close();
        } catch (IOException e) {
            LOG.fatal("Was unable to write to the index file", e);
            System.exit(1);
        }
    }
    
    /**
     * Returns the current data directory
     * 
     * @return the data directory
     */
    public File getDirectory() {
        LOG.info("Data directory is: " + DIRECTORY_PATH);
        File directory = new File(DIRECTORY_PATH);
        
        try {         
            createDirectory();
            
            if (!directory.isDirectory()) {
                IOException ioe = new IOException("Tried to open data "
                                + "directory but it exists "
                                + "and is not a directory: "
                                + DIRECTORY_PATH);
                LOG.fatal(ioe);
                throw ioe;
            }
        } catch (IOException ioe) {
            /*
             * Basically nothing can be done for it, so let's just crash and
             * hope someone is reading the log
             */
            LOG.fatal("Something went wrong with getting the data " + 
                            "directory", ioe);
            System.exit(1);
        }
        
        return directory;
    }
    
    /**
     * If the data directory does not already exist, make it.  If the data
     * directory does already exist, nothing is done
     * @throws IOException
     */
    private void createDirectory() throws IOException{
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            /*
             * Create the directory
             */
            boolean success = directory.mkdir();
            if (!success) {
                IOException ioe = new IOException(
                                "Tried to open as data directory but failed: "
                                                + DIRECTORY_PATH);
                LOG.fatal(ioe);
                throw ioe;
            }
            LOG.info("Created data directory: " + DIRECTORY_PATH);
            
            /*
             * Create the index
             */
            setIndex(new HashMap<String, String>());
            LOG.info("Created the index: " + INDEX_PATH);
        }

    }
}

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
    
    File index = new File(getDirectory().getAbsolutePath()
                    + System.getProperty("file.separator") + "index");
    
    public HashMap<String, File> getIndex() {
        HashMap<String, File> returnValue = null;
        try {
            FileInputStream fis = new FileInputStream(index);
            ObjectInputStream ois = new ObjectInputStream(fis);
            returnValue = (HashMap<String, File>) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e){
            LOG.fatal("Unable to read index file", e);
            System.exit(1);
        }
        return returnValue;
    }
    
    public void setIndex(HashMap<String, File> map) {
        try {
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
    
    public File getDirectory() {
        String directoryPath = System.getProperty("user.home")
                        + System.getProperty("file.separator")
                        + ".taberystwyth"
                        + System.getProperty("file.separator");
        LOG.info("Directory set as: " + directoryPath);
        File directory = new File(directoryPath);
        
        try {
            if (!directory.exists()) {
                /*
                 * Create the directory
                 */
                boolean success = directory.mkdir();
                if (!success) {
                    IOException ioe = new IOException(
                                    "Tried to open as data directory but failed: "
                                                    + directoryPath);
                    LOG.fatal(ioe);
                    throw ioe;
                }
                LOG.info("Created data directory: " + directoryPath);
                
                /*
                 * Create the index
                 */
                setIndex(new HashMap<String, File>());
            }
            
            if (!directory.isDirectory()) {
                IOException ioe = new IOException("Tried to open data "
                                + "directory but it exists "
                                + "and is not a directory: " + directoryPath);
                LOG.fatal(ioe);
                throw ioe;
            }
        } catch (IOException ioe) {
            /*
             * Basically nothing can be done for it, so let's just crash and
             * hope someone is reading the log
             */
            System.exit(1);
        }
        
        return directory;
    }
}

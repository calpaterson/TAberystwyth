package taberystwyth.utils;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

public class DataDirectory {
    
    private static final Logger LOG = Logger
                    .getLogger(DataDirectory.class);
    
    private static final DataDirectory INSTANCE = new DataDirectory();
    
    public static DataDirectory getInstance() {
        return INSTANCE;
    }
    
    private DataDirectory() {
        /* VOID */
    }
    
    public File getDirectory() {
        // FIXME: Statically hacked
        String directoryPath = System.getProperty("user.home") +
                               System.getProperty("file.separator") +
                               ".taberystwyth" + 
                               System.getProperty("file.separator");
        LOG.info("Directory set as: " + directoryPath);
        File directory = new File(directoryPath);
        
        try {
            if (!directory.exists()) {
                boolean success = directory.mkdir();
                if (!success) {
                    IOException ioe = new IOException(
                                    "Tried to open as data directory but failed: "
                                                    + directoryPath);
                    LOG.fatal(ioe);
                    throw ioe;
                }
                LOG.info("Created data directory: " + directoryPath);
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

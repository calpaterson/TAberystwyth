package taberystwyth.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.Server;

public class TabServer extends Observable implements Runnable {
    
    private static final Logger LOG = Logger.getLogger(TabServer.class);
    
    private static final TabServer INSTANCE = new TabServer();
    
    private static final int UPDATE_FREQUENCY = 250;
    
    private static JdbcConnectionPool connectionPool;
    
    public static synchronized JdbcConnectionPool getConnectionPool() {
        return connectionPool;
    }
    
    private Server server;
    
    public static TabServer getInstance() {
        return INSTANCE;
    }
    
    private TabServer() {
        try {
            server = Server.createTcpServer(
                            new String[] { "-tcpPort", "1337", "-tcp",
                                    "-tcpSSL", "-tcpAllowOthers" }).start();
            new Thread(this).start();
            LOG.info(server.getStatus());
        } catch (SQLException e) {
            LOG.fatal("Unable to start server", e);
        }
    }
    
    public void createDatabase(File location) {
        connectionPool = JdbcConnectionPool.create(
                        "jdbc:h2:" + location.getAbsolutePath(), "sa", "sa");
        InputStream sqlStream = TabServer.class
                        .getResourceAsStream("/schema.sql");
        evaluateSQLFile(sqlStream);
        try {
            sqlStream.close();
        } catch (IOException e) {
            LOG.error("Unable to close SQL stream", e);
        }
        LOG.info("Created database: " + location.getAbsolutePath());
    }
    
    public void openDatabase(File location) {
        connectionPool = JdbcConnectionPool.create(
                        "jdbc:h2:" + location.getAbsolutePath()
                                        + ";IFEXISTS=TRUE", "sa", "sa");
        LOG.info("Opened database: " + location.getAbsolutePath());
    }
    
    /**
     * Evaluates a given SQL file against the current connection.
     */
    private void evaluateSQLFile(InputStream file) {
        char[] cbuf = new char[10000];
        InputStreamReader fr = null;
        BufferedReader br = null;
        try {
            fr = new InputStreamReader(file);
            br = new BufferedReader(fr);
            br.read(cbuf);
            
            /*
             * This block is some disgusting magic that loads all of the SQL
             * statements in the given file
             */
            String fileContents = new String(cbuf);
            String[] statements = fileContents.split(";");
            Connection conn = getConnectionPool().getConnection();
            for (int i = 0; i < (statements.length - 1); ++i) {
                statements[i] = statements[i].concat(";");
                Statement state = conn.createStatement();
                state.execute(statements[i]);
                state.close();
            }
            conn.close();
            LOG.debug("Evaluated SQL file");
        } catch (Exception e) {
            LOG.error("Unable to evaluate SQL file", e);
            
        } finally {
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
                LOG.error("Unable to evaluate SQL file", e);
            }
        }
    }
    
    @Override
    public void addObserver(Observer o) {
        /*
         * Every time that we add a new observer, we want to trigger all of
         * observers to update.
         */
        setChanged();
        super.addObserver(o);
        notifyObservers();
        
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(UPDATE_FREQUENCY);
                setChanged();
                notifyObservers();
            } catch (InterruptedException e) {
                LOG.error("TabServer thread was interupted", e);
            }
        }
    }
}

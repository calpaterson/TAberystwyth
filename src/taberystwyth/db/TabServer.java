package taberystwyth.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.Server;

public class TabServer implements Observer {
    
    private static final Logger LOG = Logger.getLogger(TabServer.class);
    
    private static final TabServer INSTANCE = new TabServer();
    
    private static JdbcConnectionPool connectionPool;
    
    public static synchronized JdbcConnectionPool getConnectionPool() {
        return connectionPool;
    }

    private Server server;
    
    public static TabServer getInstance(){
        return INSTANCE;
    }
    
    private TabServer(){
        /* VOID */
        try {
            server = Server.createTcpServer(new String[] {"-tcpAllowOthers"}).start();
            LOG.info(server.getStatus());
        } catch (SQLException e) {
            LOG.fatal("Unable to start server", e);
        }
    }
    
    public void createDatabase(File location){
        connectionPool = JdbcConnectionPool.create(
                "jdbc:h2:" + location.getAbsolutePath(), "sa", "sa");
        LOG.info("Created database: " + location.getAbsolutePath());
    }
    
    public void openDatabase(File location){
        connectionPool = JdbcConnectionPool.create(
                "jdbc:h2:" + location.getAbsolutePath() + ";IFEXISTS=TRUE", "sa", "sa");
        LOG.info("Opened database: " + location.getAbsolutePath());
    }
    
    /**
     * Evaluates a given SQL file against the current connection.
     */
    private void evaluateSQLFile(File file) {
            char[] cbuf = new char[10000];
            FileReader fr = null;
            BufferedReader br = null;
            try {
                fr = new FileReader(file);
                br = new BufferedReader(fr);
                br.read(cbuf);
                
                /*
                 * This block is some disgusting magic that loads all of
                 * the SQL statements in the given file
                 */
                String fileContents = new String(cbuf);
                String[] statements = fileContents.split(";");
                Connection conn = getConnectionPool().getConnection();
                for (int i = 0; i < (statements.length - 1); ++i) {
                    statements[i] = statements[i].concat(";");
                    Statement state = conn.createStatement();
                    state.execute(statements[i]);
                }
                conn.close();
                LOG.debug("Evaluated SQL file: " + file.getAbsolutePath());
            } catch (Exception e) {
                LOG.fatal("Unable to evaluate: " + file.getAbsolutePath(), e);
                
            } finally {
                try {
                    br.close();
                    fr.close();
                } catch (IOException e) {
                    LOG.error("Unable to read file: " + file.getAbsolutePath(), e);
                }
            }
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub
        
    }

    public void addObserver(Observer o) {
        // TODO Auto-generated method stub
        
    }
}

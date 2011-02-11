package taberystwyth.db;

import java.io.File;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.Server;

public class TabServer {
    
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
}

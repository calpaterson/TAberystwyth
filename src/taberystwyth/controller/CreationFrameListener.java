package taberystwyth.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import taberystwyth.db.TabServer;
import taberystwyth.utils.DataDirectory;
import taberystwyth.view.CreationFrame;
import taberystwyth.view.OverviewFrame;

public class CreationFrameListener implements ActionListener {
    
    private static final Logger LOG = Logger
                    .getLogger(CreationFrameListener.class);
    
    private static final CreationFrameListener INSTANCE = new CreationFrameListener();
    
    public static CreationFrameListener getInstance() {
        return INSTANCE;
    }
    
    private CreationFrameListener() {
        /* VOID */
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        CreationFrame frame = CreationFrame.getInstance();
        if (e.getActionCommand().equals("Create")) {
            /*
             * Create and start the database in the correct place
             */
            String tournamentName = frame.getTournamentName();
            String fileName = unfuck(tournamentName);
            HashMap<String, String> index = DataDirectory.getInstance().getIndex();
            index.put(tournamentName, fileName);
            DataDirectory.getInstance().setIndex(index);
            File directory = DataDirectory.getInstance().getDirectory();
            TabServer.getInstance().createDatabase(
                            new File(directory + System.getProperty("file.separator") + fileName));
            
            /*
             * Insert the name of the tournament
             */
            try {
                Connection conn = TabServer.getInstance()
                                .getConnectionPool().getConnection();
                PreparedStatement p = conn
                                .prepareStatement(
                           "insert into tournament_name (\"name\") values(?);");
                p.setString(1, frame.getTournamentName());
                p.execute();
                p.close();
                conn.close();
            } catch (SQLException e1) {
                LOG.fatal("Unable to use the database", e1);
                System.exit(1);
            }
            
            frame.setVisible(false);
            OverviewFrame.getInstance().createAndShow();
        } else if (e.getActionCommand().equals("Cancel")) {
            frame.setVisible(false);
            /*
             * FIXME: This is not really desirable (shutting down the VM
             * randomly is apparently a bad thing (who would have guessed?)).
             * There should probably be some kind of flow in and around the
             * "open a new tab" dialog and this frame, but I'm not going to do
             * it now.
             */
            System.exit(0);
        }
    }
    
    /**
     * Takes a tournament name (ie: some string including spaces and whatever
     * and returns a [a-z|0-9|-] string that can be used for the file name
     * 
     * @param tournamentName
     *            the full (ie: with spaces) title of the tournament
     * @return all lowercase, no spaces
     */
    private String unfuck(String tournamentName) {
        String returnValue = tournamentName.replace(' ', '_');
        returnValue = returnValue.toLowerCase();
        // FIXME: This next replace does not work
        returnValue = returnValue.replaceAll("[^0-9a-z_]", "");
        LOG.error("unfuck turned " + tournamentName + " into " + returnValue);
        return returnValue;
    }
    
}

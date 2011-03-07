package taberystwyth.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import taberystwyth.controller.DataEntryListListener;
import taberystwyth.db.TabServer;

/**
 * @author Roberto Sarrionandia [r@sarrionandia.com] A frame to show a list of
 *         rooms that the user can select from to show the data entry frame for
 *         that particular room
 */
public class DataEntryListFrame extends JFrame {
    
    private static final Logger LOG = Logger
                    .getLogger(ResultEntryFrame.class);
    private static DataEntryListFrame instance = new DataEntryListFrame();
    private Connection conn;
    DefaultComboBoxModel roomListModel = new DefaultComboBoxModel();
    JComboBox roomList;
    
    DataEntryListListener listener;
    
    JButton btnEnterData = new JButton("Enter Data");
    JButton btnClose = new JButton("Close");
    
    public DataEntryListFrame() {
        // Layout stuff
        setLayout(new MigLayout("wrap 2, flowx, fillx", "[left]rel[right]"));
        setTitle("Select Room");
        
        roomList = new JComboBox(roomListModel);
        
        this.add(roomList, "span");
        this.add(btnClose);
        this.add(btnEnterData);
        
        listener = new DataEntryListListener(this);
        btnClose.addActionListener(listener);
        btnEnterData.addActionListener(listener);
        
        // Move/resize the frame
        this.pack();
        this.setMinimumSize(getSize());
        setLocationRelativeTo(OverviewFrame.getInstance());
        
    }
    
    /**
     * @return The instance of the DataEntryListFrame
     */
    public static DataEntryListFrame getInstance() {
        return instance;
    }
    
    /**
     * Get the current round number
     * 
     * @return the number of the current round
     */
    private int roundNumber() {
        try {
            Connection conn = TabServer.getConnectionPool().getConnection();
            PreparedStatement stmt = conn
                            .prepareStatement("select max(\"round\") from rooms;");
            ResultSet rs = stmt.executeQuery();
            
            rs.next();
            int roundNumber = rs.getInt(1);
            conn.close();
            return roundNumber;
        } catch (SQLException e) {
            LOG.error("Can't read round number", e);
            return (Integer) null;
        }
    }
    
    /**
     * Update the list of rooms
     */
    private void updateRoomList() {
        roomListModel = new DefaultComboBoxModel();
        
        // Get a database connection
        try {
            conn = TabServer.getConnectionPool().getConnection();
        } catch (SQLException e) {
            LOG.fatal("Couldn't get a database connection", e);
        }
        
        // Set up the statement
        try {
            PreparedStatement stmt = conn
                            .prepareStatement("select \"location\" from rooms where \"round\" = ?;");
            stmt.setInt(1, roundNumber());
            
            //execute query
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()) {
                roomListModel.addElement(rs.getString(1));
            }
            
            roomList.setModel(roomListModel);

        } catch (SQLException e) {
            LOG.fatal("Couldn't prepare an SQL statement to get a list of rooms",
                            e);
        }
                
        this.pack();
        this.setMinimumSize(getSize());
        
        // Close the database connection
        try {
            conn.close();
        } catch (SQLException e) {
            LOG.error("Unable to close the database connection", e);
        }
        
    }
    
    public void setVisible(boolean b){
        if(b){
            updateRoomList();
            super.setVisible(b);
        }
        else super.setVisible(b);
    }

    public void enterData() {
        ResultEntryFrame.getInstance().changeRoom(roomList.getSelectedItem().toString());
        ResultEntryFrame.getInstance().setVisible(true);
        
    }
    
}

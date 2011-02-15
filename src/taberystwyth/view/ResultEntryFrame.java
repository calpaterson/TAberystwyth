package taberystwyth.view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import taberystwyth.controller.RoomBoxListener;
import taberystwyth.db.TabServer;

/**
 * A frame which lets users enter the results for the current round
 * 
 * @author Roberto Sarrionandia [r@sarrionandia.com]
 *
 */
public class ResultEntryFrame extends JFrame {
    
 
    private static final long serialVersionUID = 6978383396277378717L;
    private Logger LOG = Logger.getLogger(ResultEntryFrame.class);
    private static ResultEntryFrame instance = new ResultEntryFrame();
    
    // JLabels
    JLabel titleLabel = new JLabel("Enter Data For Round X"); // FIXME
    JLabel chooseRoomLabel = new JLabel("Room:");
    
    JLabel fPropLabel = new JLabel("First Proposition");
    JLabel fOpLabel = new JLabel("First Opposition");
    JLabel sPropLabel = new JLabel("Second Proposition");
    JLabel sOpLabel = new JLabel("Second Opposition");
    
    JLabel fPropS1Label = new JLabel("Speaker");
    JLabel fPropS2Label = new JLabel("Speaker");
    JLabel fOpS1Label = new JLabel("Speaker");
    JLabel fOpS2Label = new JLabel("Speaker");
    JLabel sPropS1Label = new JLabel("Speaker");
    JLabel sPropS2Label = new JLabel("Speaker");
    JLabel sOpS1Label = new JLabel("Speaker");
    JLabel sOpS2Label = new JLabel("Speaker");
    
    // ComboBox
    DefaultComboBoxModel rooms = new DefaultComboBoxModel();
    JComboBox roomBox = new JComboBox();
    JComboBox fPropPosition;
    JComboBox fOpPosition;
    JComboBox sPropPosition;
    JComboBox sOpPosition;
    
    // Text fields
    JTextField fPropS1Points = new JTextField(3);
    JTextField fPropS2Points = new JTextField(3);
    JTextField fOpS1Points = new JTextField(3);
    JTextField fOpS2Points = new JTextField(3);
    JTextField sPropS1Points = new JTextField(3);
    JTextField sPropS2Points = new JTextField(3);
    JTextField sOpS1Points = new JTextField(3);
    JTextField sOpS2Points = new JTextField(3);
    
    // Buttons
    JButton clear = new JButton("Clear");
    JButton save = new JButton("Save");
    
    private ResultEntryFrame() {
        setLayout(new MigLayout("wrap 4, flowx, fillx", "[left]rel[right]"));
        setTitle("Enter Results");
        
        /*
         * When the window is closed, make it invisible
         */
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                ResultEntryFrame.getInstance().setVisible(false);
            }
        });
        
        // Setup comboboxes
        DefaultComboBoxModel fPropPositions = new DefaultComboBoxModel();
        fPropPositions.addElement("1st");
        fPropPositions.addElement("2nd");
        fPropPositions.addElement("3rd");
        fPropPositions.addElement("4th");
        
        DefaultComboBoxModel fOpPositions = new DefaultComboBoxModel();
        fOpPositions.addElement("1st");
        fOpPositions.addElement("2nd");
        fOpPositions.addElement("3rd");
        fOpPositions.addElement("4th");
        
        DefaultComboBoxModel sPropPositions = new DefaultComboBoxModel();
        sPropPositions.addElement("1st");
        sPropPositions.addElement("2nd");
        sPropPositions.addElement("3rd");
        sPropPositions.addElement("4th");
        
        DefaultComboBoxModel sOpPositions = new DefaultComboBoxModel();
        sOpPositions.addElement("1st");
        sOpPositions.addElement("2nd");
        sOpPositions.addElement("3rd");
        sOpPositions.addElement("4th");
        
        fPropPosition = new JComboBox(fPropPositions);
        fOpPosition = new JComboBox(fOpPositions);
        sPropPosition = new JComboBox(sPropPositions);
        sOpPosition = new JComboBox(sOpPositions);
        
        roomBox.addItemListener(new RoomBoxListener());
        
        // Setup the GUI
        add(titleLabel, "span");
        
        add(chooseRoomLabel, "span 2");
        add(roomBox, "span 2");
        
        add(fPropLabel, "span 2, split 2");
        add(fPropPosition, "align left");
        add(fOpLabel, "span 2, split 2");
        add(fOpPosition, "align left");
        
        add(fPropS1Label, "span 2, split 2");
        add(fPropS1Points, "align left");
        add(fOpS1Label, "span 2, split 2");
        add(fOpS1Points, "align left");
        
        add(fPropS2Label, "span 2, split 2");
        add(fPropS2Points, "align left");
        add(fOpS2Label, "span 2, split 2");
        add(fOpS2Points, "align left");
        
        add(sPropLabel, "span 2, split 2");
        add(sPropPosition, "align left");
        add(sOpLabel, "span 2, split 2");
        add(sOpPosition, "align left");
        
        add(sPropS1Label, "span 2, split 2");
        add(sPropS1Points, "align left");
        add(sOpS1Label, "span 2, split 2");
        add(sOpS1Points, "align left");
        
        add(sPropS2Label, "span 2, split 2");
        add(sPropS2Points, "align left");
        add(sOpS2Label, "span 2, split 2");
        add(sOpS2Points, "align left");
        
        add(clear, "span 2");
        add(save, "span 2");
        
        // Pack
        // FIXME Make size a little wider
        this.pack();
        this.setMinimumSize(getSize());
        setLocationRelativeTo(OverviewFrame.getInstance());
        
    }
    
    /**
     * Get's the instance of the frame singleton
     * @return An instance of the frame
     */
    public static ResultEntryFrame getInstance() {
        return instance;
    }
    
    @Override
    public void setVisible(boolean b) {
        if (b) {
            this.refreshComponents();
            super.setVisible(b);
        } else
            super.setVisible(b);
    }
    
    /**
     * Update the list of rooms and set the components to the first 
     * room that happens to be selected
     */
    private void refreshComponents() {
        // Get a list of rooms
        try {
            PreparedStatement stmt = TabServer.getConnectionPool()
                            .getConnection()
                            .prepareStatement("select * from locations;");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                rooms.addElement(rs.getString("name"));
            }
            
            roomBox.setModel(rooms);
        } catch (SQLException e) {
            LOG.error("Can't get list of rooms", e);
        }
        
        // Change the round number
        titleLabel.setText("Insert Data for Round " + roundNumber());
        
        // Update the window size
        pack();
        this.setMinimumSize(getSize());
        
        changeRoom();
        
    }
    
    /**
     * Update the components to the selected room in the 
     * roomBox
     */
    public void changeRoom() {
        int round = roundNumber();
        String room = roomBox.getSelectedItem().toString();
        
        try {
            Connection conn = TabServer.getConnectionPool().getConnection();
            //Find out which teams are in this room
            PreparedStatement stmt = conn
                            .prepareStatement("select * from rooms where \"round\" = ? and \"location\" = ?;");
            stmt.setInt(1, round);
            stmt.setString(2, room);
            ResultSet rs = stmt.executeQuery();
            
            //Add the team names to the dialog
            rs.next();
            fPropLabel.setText(rs.getString("first_prop"));
            fOpLabel.setText(rs.getString("first_op"));
            sPropLabel.setText(rs.getString("second_prop"));
            sOpLabel.setText(rs.getString("second_op"));
            
            //Add the speaker names to the dialog
            stmt = conn.prepareStatement("select * from teams where \"name\" = ?");
            stmt.setString(1, fPropLabel.getText());
            rs = stmt.executeQuery();
            rs.next();
            fPropS1Label.setText(rs.getString("speaker1"));
            fPropS2Label.setText(rs.getString("speaker2"));
            
            stmt.setString(1, fOpLabel.getText());
            rs = stmt.executeQuery();
            rs.next();
            fOpS1Label.setText(rs.getString("speaker1"));
            fOpS2Label.setText(rs.getString("speaker2"));

            stmt.setString(1, sPropLabel.getText());
            rs = stmt.executeQuery();
            rs.next();
            sPropS1Label.setText(rs.getString("speaker1"));
            sPropS2Label.setText(rs.getString("speaker2"));

            stmt.setString(1, sOpLabel.getText());
            rs = stmt.executeQuery();
            rs.next();
            sOpS1Label.setText(rs.getString("speaker1"));
            sOpS2Label.setText(rs.getString("speaker2"));

            pack();
            setMinimumSize(getSize());
            
            //Set the team positions
            
            //The two prepared statements
            PreparedStatement countPositions = conn.prepareStatement("select count(\"position\") from team_results " +
            		"where \"team\" = ? and \"round\" = ?;");
            PreparedStatement getPosition = conn.prepareStatement("select * from team_results " +
            		"where \"team\" = ? and \"round\" = ?;");
            countPositions.setInt(2, round);
            getPosition.setInt(2, round);
            
            //Team position for first prop
            countPositions.setString(1, fPropLabel.getText());
            rs = countPositions.executeQuery();
            rs.next();
            if(rs.getInt(1) > 0){
                getPosition.setString(1, fPropLabel.getText());
                rs = getPosition.executeQuery();
                rs.next();
                fPropPosition.setSelectedIndex(rs.getInt("position") - 1);
            }
            
            //Team position for first op
            countPositions.setString(1, fOpLabel.getText());
            rs = countPositions.executeQuery();
            rs.next();
            if(rs.getInt(1) > 0){
                getPosition.setString(1, fOpLabel.getText());
                rs = getPosition.executeQuery();
                rs.next();
                fOpPosition.setSelectedIndex(rs.getInt("position") - 1);
            }
            
            //Team position for second prop
            countPositions.setString(1, sPropLabel.getText());
            rs = countPositions.executeQuery();
            rs.next();
            if(rs.getInt(1) > 0){
                getPosition.setString(1, sPropLabel.getText());
                rs = getPosition.executeQuery();
                rs.next();
                sPropPosition.setSelectedIndex(rs.getInt("position") - 1);
            }

            
            //Team position for second op
            countPositions.setString(1, sOpLabel.getText());
            rs = countPositions.executeQuery();
            rs.next();
            if(rs.getInt(1) > 0){
                getPosition.setString(1, sOpLabel.getText());
                rs = getPosition.executeQuery();
                rs.next();
                sOpPosition.setSelectedIndex(rs.getInt("position") - 1);
            }


            conn.close();
            
        } catch (SQLException e) {
            LOG.error("Can't read database", e);
        }
        
    }
    
    @SuppressWarnings("cast")
    private int roundNumber() {
        try {
            Connection conn = TabServer.getConnectionPool().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                                            "select max(\"round\") from rooms;");
            ResultSet rs = stmt.executeQuery();
            
            rs.next();
            int roundNumber = rs.getInt(1);
            conn.close();
            return  roundNumber;
        } catch (SQLException e) {
            LOG.error("Can't read round number", e);
            return (Integer) null;
        }

        
    }
    


    
}

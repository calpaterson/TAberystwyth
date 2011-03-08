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

import taberystwyth.controller.ResultEntryFrameListener;
import taberystwyth.db.TabServer;

/**
 * A frame which lets users enter the results for the current round
 * 
 * @author Roberto Sarrionandia [r@sarrionandia.com]
 *
 */
public class ResultEntryFrame extends JFrame {
    
 
    private static final long serialVersionUID = 6978383396277378717L;
    private static final Logger LOG = Logger.getLogger(ResultEntryFrame.class);
    private static ResultEntryFrame instance = new ResultEntryFrame();
    
    String room;
    
    private ResultEntryFrameListener listener = new ResultEntryFrameListener(this);
    
    // JLabels
    private JLabel titleLabel = new JLabel("Enter Data For Round X"); // FIXME
    
    private JLabel fPropLabel = new JLabel("First Proposition");
    private  JLabel fOpLabel = new JLabel("First Opposition");
    private JLabel sPropLabel = new JLabel("Second Proposition");
    private JLabel sOpLabel = new JLabel("Second Opposition");
    
    private JLabel fPropS1Label = new JLabel("Speaker");
    private JLabel fPropS2Label = new JLabel("Speaker");
    private  JLabel fOpS1Label = new JLabel("Speaker");
    private JLabel fOpS2Label = new JLabel("Speaker");
    private JLabel sPropS1Label = new JLabel("Speaker");
    private JLabel sPropS2Label = new JLabel("Speaker");
    private JLabel sOpS1Label = new JLabel("Speaker");
    private JLabel sOpS2Label = new JLabel("Speaker");
    
    // ComboBox
    private JComboBox fPropPosition;
    private JComboBox fOpPosition;
    private  JComboBox sPropPosition;
    private  JComboBox sOpPosition;
    
    // Text fields
    private JTextField fPropS1Points = new JTextField(3);
    private JTextField fPropS2Points = new JTextField(3);
    private JTextField fOpS1Points = new JTextField(3);
    private JTextField fOpS2Points = new JTextField(3);
    private JTextField sPropS1Points = new JTextField(3);
    private JTextField sPropS2Points = new JTextField(3);
    private  JTextField sOpS1Points = new JTextField(3);
    private  JTextField sOpS2Points = new JTextField(3);
    
    // Buttons
    private JButton clear = new JButton("Clear");
    private JButton save = new JButton("Save");
    
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
        
        
        // Setup the GUI
        add(titleLabel, "span");
                
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
        
        save.addActionListener(listener);
        
        // Pack
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
    
    
    /**
     * Update the components to the selected room in the 
     * roomBox
     * @param room The location we want to display for
     */
    public void changeRoom(String room) {
        this.room = room;
        int round = roundNumber();
        
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

            
            //FIXME also set speaker points

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
    
    /**
     * Commit the current frame to the database
     */
    public void commit(){
        String query = 
            "insert into team_results " +
            "(\"round\", \"team\", \"position\") " +
            "values (?,?,?);";
        
        Connection c;
        try {
            c = TabServer.getConnectionPool().getConnection();
            PreparedStatement stmt = c.prepareStatement(query);
            
            //First Prop Result
            stmt.setInt(1, roundNumber());
            stmt.setString(2, fPropLabel.getText());
            stmt.setInt(3, positionToInteger(fPropPosition.getSelectedItem().toString()));
            stmt.execute();
            
            //Second Prop Result
            stmt.setInt(1, roundNumber());
            stmt.setString(2, sPropLabel.getText());
            stmt.setInt(3, positionToInteger(sPropPosition.getSelectedItem().toString()));
            stmt.execute();
            
            //First Op Result
            stmt.setInt(1, roundNumber());
            stmt.setString(2, fOpLabel.getText());
            stmt.setInt(3, positionToInteger(fOpPosition.getSelectedItem().toString()));
            stmt.execute();

            //Second Op Result
            stmt.setInt(1, roundNumber());
            stmt.setString(2, sOpLabel.getText());
            stmt.setInt(3, positionToInteger(sOpPosition.getSelectedItem().toString()));
            stmt.execute();
            
            /*
             * Speaker scores
             */
            
            //First Prop Speaker 1
            insertUpdateSpeakerScore(
                            fPropS1Label.getText(),
                            fPropLabel.getText(),
                            Integer.parseInt(fPropS1Points.getText()));
            //First Prop Speaker 2
            insertUpdateSpeakerScore(
                            fPropS2Label.getText(),
                            fPropLabel.getText(),
                            Integer.parseInt(fPropS2Points.getText()));
            //Second Prop Speaker 1
            insertUpdateSpeakerScore(
                            sPropS1Label.getText(),
                            sPropLabel.getText(),
                            Integer.parseInt(sPropS1Points.getText()));
            //Second Prop Speaker 2
            insertUpdateSpeakerScore(
                            sPropS2Label.getText(),
                            sPropLabel.getText(),
                            Integer.parseInt(sPropS2Points.getText()));
            //First Op Speaker 1
            insertUpdateSpeakerScore(
                            fOpS1Label.getText(),
                            fOpLabel.getText(),
                            Integer.parseInt(fOpS1Points.getText()));
            //First Op Speaker 2
            insertUpdateSpeakerScore(
                            fOpS2Label.getText(),
                            fOpLabel.getText(),
                            Integer.parseInt(fOpS2Points.getText()));
            //Second Op Speaker 1
            insertUpdateSpeakerScore(
                            sOpS1Label.getText(),
                            sOpLabel.getText(),
                            Integer.parseInt(sOpS1Points.getText()));
            //Second Op Speaker 2
            insertUpdateSpeakerScore(
                            sOpS2Label.getText(),
                            sOpLabel.getText(),
                            Integer.parseInt(sOpS2Points.getText()));


        } catch (SQLException e) {
            LOG.fatal("Unable to insert results into database", e);
        }
        
        
    }
    
    /**
     * Convert a string position to its integer representation
     * @param position ("1st", "2nd", "3rd" or "4th")
     * @return  (1,2,3 or 4)
     */
    private int positionToInteger(String position){
        if(position.equals("1st")) return 1;
        else if (position.equals("2nd")) return 2;
        else if (position.equals("3rd")) return 3;
        else if (position.equals("4th")) return 4;
        else return 0;
    }
    
    /**
     * If a speaker score already exists, update it. Otherwise insert it
     * @param speaker Name of the speaker
     * @param instutition Institution of the speaker
     */
    private void insertUpdateSpeakerScore(String speaker, String institution, int points){
        final String insertQuery = "insert into speaker_results " +
        "(\"round\", \"speaker\", \"institution\", \"points\") " +
        "values(?, ?, ?, ?);";
        
        final String updateQuery = "update speaker_results " +
        		"set \"points\" = ? " +
        		"where \"speaker\" = ? and \"institution\" = ?;";
        
        try {
            Connection conn = TabServer.getConnectionPool().getConnection();
            PreparedStatement stmt;
            
            if (speakerInserted(speaker, institution)){
                //Update the speaker points
                stmt = conn.prepareStatement(updateQuery);
                
                stmt.setInt(1, points);
                stmt.setString(1, speaker);
                stmt.setString(3, institution);
                
                stmt.execute();
                
            }
            
            else{
                //Insert the speaker points
                stmt = conn.prepareStatement(insertQuery);
                
                stmt.setInt(1, roundNumber());
                stmt.setString(2, speaker);
                stmt.setString(3, institution);
                stmt.setInt(4, points);
                
                stmt.execute();
                
            }
            conn.close();
            
        } catch (SQLException e) {
            LOG.fatal("Unable to insert speaker score", e);
        }
        
    }
    
    /**
     * Checks if a speaker has already been inserted
     * @param speaker THe speaker to check for
     * @param institution The institution the speaker belongs to
     * @return Whether or not they have been inserted
     */
    private boolean speakerInserted(String speaker, String institution){
        final String query = "select count (*) from speaker_results where " +
        		"\"speaker\" = ? and \"institution\" = ?";
        try {
            Connection conn = TabServer.getConnectionPool().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            
            stmt.setString(1, speaker);
            stmt.setString(2, institution);
            
            ResultSet rs = stmt.executeQuery();
            conn.close();
            stmt.close();
            
            rs.next();
            int count = rs.getInt(1);
            rs.close();
            
            return(count > 0);
            
        } catch (SQLException e) {
            LOG.fatal("Can't tell if the speaker has already been inserted", e);
            return false;
        }
        
    }
    


    
}

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
    


    
}

package taberystwyth.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import net.miginfocom.swing.MigLayout;

public class AboutDialog extends JDialog {
    
    private static final long serialVersionUID = 1L;
    
    private static AboutDialog instance = new AboutDialog();

    public static AboutDialog getInstance() {
        return instance;
    }

    protected BufferedImage aduLogo;
    
    private AboutDialog(){
        super(OverviewFrame.getInstance(), "About", true);
        setLayout(new MigLayout("wrap 1", "[center]"));
        
        /*
         * Prepare the imageLabel
         */
        try {
            aduLogo = ImageIO.read(this.getClass().getResourceAsStream("/adu.jpeg"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(OverviewFrame.getInstance(), 
                    "The file \"adu.jpeg\" was not loaded correctly.", 
                    "Error", ERROR);
            e.printStackTrace();
        }
        JLabel imageLabel = new JLabel(new ImageIcon(aduLogo));
        imageLabel.setSize(200, 294);
        
        /*
         * The captions
         */
        JLabel name = new 
            JLabel("TAberystwyth v0.0, Bitch.");
        JLabel desc = new JLabel("A Debating Competition Organiser");

        /*
         * The close button
         */
        JButton close = new JButton("Close");;
        close.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            setVisible(false);
          }
        });

        /*
         * Add everything
         */
        add(imageLabel);
        add(name);
        add(desc);
        add(close);
        
        setResizable(false);
        pack();
        setLocationRelativeTo(OverviewFrame.getInstance());
    }
    
}

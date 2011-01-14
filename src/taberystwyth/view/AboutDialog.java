package taberystwyth.view;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AboutDialog extends JDialog {
    
    private static final long serialVersionUID = 1L;
    
    private static AboutDialog instance = new AboutDialog();

    public static AboutDialog getInstance() {
        return instance;
    }

    protected BufferedImage aduLogo;
    
    private AboutDialog(){
        super(OverviewFrame.getInstance(), "About", true);
        
        /*
         * Load the image and put it in a label
         */
        try {
            aduLogo = ImageIO.read(new File("adu.jpeg"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(OverviewFrame.getInstance(), 
                    "The file \"adu.jpeg\" was not loaded correctly.", 
                    "Error", ERROR);
            e.printStackTrace();
        }
        JLabel imageLabel = new JLabel(new ImageIcon(aduLogo));
        imageLabel.setSize(200, 294);
        
        Box box = Box.createVerticalBox();
        box.add(Box.createGlue());
    

        
        JLabel name = new 
            JLabel("TAberystwyth, Bitch.");
        JLabel desc = new JLabel("A Debating Competition Organiser");
        box.add(imageLabel,"Center");
        box.add(name);
        //box.add(desc);
        box.add(Box.createGlue());
        getContentPane().add(box);

        JPanel panel = new JPanel();
        JButton ok = new JButton("OK");
        panel.add(ok);
        getContentPane().add(panel, "South");

        ok.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            setVisible(false);
          }

        });

        pack();
    }
    
}

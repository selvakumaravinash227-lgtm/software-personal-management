package spms.view;

import javax.swing.*;
import java.awt.*;

/** Java Swing desktop entry point. */
public final class SpmsApplication {
    private SpmsApplication(){}
    public static void main(String[] args){SwingUtilities.invokeLater(()->{try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}catch(Exception ignored){}new LoginFrame().setVisible(true);});}
}

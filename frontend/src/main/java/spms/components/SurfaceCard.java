package spms.components;
import spms.theme.AppTheme;import javax.swing.*;import javax.swing.border.*;
public final class SurfaceCard extends JPanel {public SurfaceCard(){setBackground(AppTheme.SURFACE);setBorder(new CompoundBorder(new LineBorder(AppTheme.BORDER,1,true),new EmptyBorder(18,20,18,20)));}}

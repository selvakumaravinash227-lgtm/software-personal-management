package spms.components;
import spms.theme.AppTheme;import javax.swing.*;import javax.swing.border.*;import java.awt.*;
public final class PrimaryButton extends JButton {public PrimaryButton(String text){super(text);setFont(AppTheme.BODY.deriveFont(Font.BOLD));setForeground(Color.WHITE);setBackground(AppTheme.TEAL);setFocusPainted(false);setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));setBorder(new CompoundBorder(new LineBorder(AppTheme.TEAL,1,true),new EmptyBorder(9,15,9,15)));}}

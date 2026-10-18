package spms.components;
import spms.theme.AppTheme;import javax.swing.*;import javax.swing.border.*;import java.awt.*;
public final class SecondaryButton extends JButton {public SecondaryButton(String text){super(text);setFont(AppTheme.BODY);setForeground(AppTheme.INK);setBackground(Color.WHITE);setFocusPainted(false);setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));setBorder(new CompoundBorder(new LineBorder(AppTheme.BORDER,1,true),new EmptyBorder(9,15,9,15)));}}

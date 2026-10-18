package spms.view;

import spms.components.*;
import spms.controller.AuthenticationController;
import spms.model.User;
import spms.theme.AppTheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;

/** Authentication module login screen. Role is always obtained from the verified account. */
public final class LoginFrame extends JFrame {
    public LoginFrame(){
        super("Software Personnel Management System · Secure Access");setDefaultCloseOperation(EXIT_ON_CLOSE);setSize(1000,650);setMinimumSize(new Dimension(840,560));setLocationRelativeTo(null);setLayout(new GridLayout(1,2));
        JPanel brand=new JPanel();brand.setBackground(AppTheme.NAVY);brand.setBorder(new EmptyBorder(64,56,48,40));brand.setLayout(new BoxLayout(brand,BoxLayout.Y_AXIS));
        JLabel mark=new JLabel("SPMS  /  PEOPLE OPERATIONS");mark.setForeground(new Color(104,211,204));mark.setFont(AppTheme.BODY.deriveFont(Font.BOLD,12));brand.add(mark);brand.add(Box.createVerticalStrut(54));
        JLabel title=new JLabel("<html>SOFTWARE PERSONNEL<br>MANAGEMENT SYSTEM</html>");title.setForeground(Color.WHITE);title.setFont(new Font("Segoe UI Semibold",Font.BOLD,30));brand.add(title);brand.add(Box.createVerticalStrut(16));
        JLabel subtitle=new JLabel("Workforce Intelligence Platform");subtitle.setForeground(new Color(188,204,216));subtitle.setFont(AppTheme.BODY.deriveFont(16f));brand.add(subtitle);brand.add(Box.createVerticalGlue());JLabel footer=new JLabel("PEOPLE  ·  DELIVERY  ·  PERFORMANCE");footer.setForeground(new Color(119,144,165));brand.add(footer);add(brand);
        JPanel wrap=new JPanel(new GridBagLayout());wrap.setBackground(AppTheme.WORKSPACE);JPanel form=new JPanel();form.setOpaque(false);form.setLayout(new BoxLayout(form,BoxLayout.Y_AXIS));form.setPreferredSize(new Dimension(330,360));
        JLabel welcome=new JLabel("Welcome back");welcome.setForeground(AppTheme.INK);welcome.setFont(AppTheme.TITLE);form.add(welcome);JLabel info=new JLabel("Sign in to your organization workspace");info.setForeground(AppTheme.MUTED);form.add(info);form.add(Box.createVerticalStrut(30));
        JTextField username=new JTextField();JPasswordField password=new JPasswordField();addField(form,"Username",username);addField(form,"Password",password);
        JCheckBox visible=new JCheckBox("Show password");visible.setOpaque(false);visible.setForeground(AppTheme.MUTED);visible.addActionListener(e->password.setEchoChar(visible.isSelected()?(char)0:'•'));form.add(visible);
        JLabel message=new JLabel(" ");message.setForeground(new Color(174,65,65));form.add(message);form.add(Box.createVerticalStrut(12));PrimaryButton login=new PrimaryButton("Sign in  →");login.setAlignmentX(Component.LEFT_ALIGNMENT);login.setMaximumSize(new Dimension(330,44));form.add(login);form.add(Box.createVerticalStrut(14));JLabel hint=new JLabel("Your access is verified from your SPMS account.");hint.setForeground(AppTheme.MUTED);form.add(hint);wrap.add(form);add(wrap);
        Runnable authenticate=()->{char[] secret=password.getPassword();try{User user=new AuthenticationController().signIn(username.getText(),secret);if(user==null){message.setText("Username or password is incorrect.");return;}dispose();new MainFrame(user).setVisible(true);}catch(Exception ex){message.setText(ex.getMessage()==null?"Sign in failed. Check the database connection.":ex.getMessage());}finally{Arrays.fill(secret,'\0');}};
        login.addActionListener(e->authenticate.run());password.addActionListener(e->authenticate.run());
    }
    private void addField(JPanel parent,String title,JComponent input){JLabel l=new JLabel(title);l.setFont(AppTheme.BODY.deriveFont(Font.BOLD));parent.add(l);parent.add(Box.createVerticalStrut(6));input.setFont(AppTheme.BODY);input.setPreferredSize(new Dimension(330,40));input.setMaximumSize(new Dimension(330,40));parent.add(input);parent.add(Box.createVerticalStrut(16));}
}

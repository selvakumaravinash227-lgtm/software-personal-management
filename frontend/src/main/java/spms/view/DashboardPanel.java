package spms.view;

import spms.components.SurfaceCard;
import spms.controller.ModuleController;
import spms.model.User;
import spms.theme.AppTheme;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

/** Live overview. Metric values always come from SPMS_DB. */
public final class DashboardPanel extends JPanel {
    public DashboardPanel(ModuleController controller,User user){setOpaque(false);setLayout(new BorderLayout(0,15));JLabel welcome=new JLabel("Welcome back, "+user.username());welcome.setFont(AppTheme.SECTION);welcome.setForeground(AppTheme.INK);add(welcome,BorderLayout.NORTH);
        try{Map<String,String> values=controller.dashboard();JPanel body=new JPanel(new BorderLayout(0,14));body.setOpaque(false);JPanel metrics=new JPanel(new GridLayout(1,4,12,0));metrics.setOpaque(false);for(String key:new String[]{"Total Employees","Present Today","On Leave","Pending Requests"}){SurfaceCard card=new SurfaceCard();card.setLayout(new BoxLayout(card,BoxLayout.Y_AXIS));JLabel k=new JLabel(key.toUpperCase());k.setForeground(AppTheme.MUTED);k.setFont(AppTheme.BODY.deriveFont(Font.BOLD,10));JLabel v=new JLabel(values.getOrDefault(key,"0"));v.setForeground(AppTheme.INK);v.setFont(new Font("Segoe UI Semibold",Font.BOLD,29));card.add(k);card.add(Box.createVerticalStrut(14));card.add(v);metrics.add(card);}body.add(metrics,BorderLayout.NORTH);
            JPanel lower=new JPanel(new GridLayout(1,3,12,12));lower.setOpaque(false);SurfaceCard payroll=new SurfaceCard();payroll.setLayout(new BoxLayout(payroll,BoxLayout.Y_AXIS));JLabel ph=new JLabel("Payroll summary · current month");ph.setFont(AppTheme.SECTION);JLabel amount=new JLabel("₹ "+values.getOrDefault("Payroll This Month","0.00"));amount.setFont(new Font("Segoe UI Semibold",Font.BOLD,27));amount.setForeground(AppTheme.TEAL);payroll.add(ph);payroll.add(Box.createVerticalStrut(25));payroll.add(amount);payroll.add(new JLabel("Net payroll recorded in SPMS_DB"));
            SurfaceCard distribution=new SurfaceCard();distribution.setLayout(new BoxLayout(distribution,BoxLayout.Y_AXIS));JLabel dh=new JLabel("Department distribution");dh.setFont(AppTheme.SECTION);distribution.add(dh);distribution.add(Box.createVerticalStrut(12));String groups=values.getOrDefault("Department Distribution","");if(groups.isBlank())distribution.add(new JLabel("No department data yet."));else for(String group:groups.split("\\|")){String[] pair=group.split("=",2);if(pair.length==2){JLabel line=new JLabel(pair[0]+"     "+pair[1]+" active");line.setForeground(AppTheme.INK);distribution.add(line);distribution.add(Box.createVerticalStrut(6));}}
            SurfaceCard activity=new SurfaceCard();activity.setLayout(new BoxLayout(activity,BoxLayout.Y_AXIS));JLabel ah=new JLabel("Recent activity");ah.setFont(AppTheme.SECTION);activity.add(ah);activity.add(Box.createVerticalStrut(14));String recent=values.getOrDefault("Recent Activity","");if(recent.isBlank())activity.add(new JLabel("No recent activity yet."));else for(String line:recent.split("\\|")){JLabel item=new JLabel("•  "+line);item.setForeground(AppTheme.INK);activity.add(item);activity.add(Box.createVerticalStrut(8));}lower.add(payroll);lower.add(distribution);lower.add(activity);body.add(lower,BorderLayout.CENTER);add(body,BorderLayout.CENTER);
        }catch(Exception e){SurfaceCard empty=new SurfaceCard();empty.add(new JLabel("Connect SPMS_DB to load dashboard metrics. Check your .env configuration."));add(empty,BorderLayout.CENTER);}}
}

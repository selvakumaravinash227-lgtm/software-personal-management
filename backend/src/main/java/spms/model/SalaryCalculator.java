package spms.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class SalaryCalculator {
    private SalaryCalculator(){}
    public static BigDecimal net(BigDecimal basic,BigDecimal allowances,BigDecimal deductions){
        if(basic==null||allowances==null||deductions==null||basic.signum()<0||allowances.signum()<0||deductions.signum()<0)throw new IllegalArgumentException("Salary components must be non-negative.");
        BigDecimal net=basic.add(allowances).subtract(deductions); if(net.signum()<0)throw new IllegalArgumentException("Deductions cannot exceed gross pay.");return net.setScale(2,RoundingMode.HALF_UP);
    }
}

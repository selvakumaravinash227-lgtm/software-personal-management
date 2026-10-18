package edu.spms.web.repository;

import edu.spms.web.entity.PayrollPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PayrollPaymentRepository extends JpaRepository<PayrollPaymentEntity,Long> {
    Optional<PayrollPaymentEntity> findFirstByPayroll_IdAndStatusOrderByConfirmedAtDesc(Long payrollId,String status);
    boolean existsByPayroll_IdAndStatus(Long payrollId,String status);
}

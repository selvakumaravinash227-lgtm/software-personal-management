package edu.spms.web.repository;
import edu.spms.web.entity.PayrollEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
public interface PayrollRepository extends JpaRepository<PayrollEntity,Long> {
 List<PayrollEntity> findAllByOrderByPayMonthDesc();
 List<PayrollEntity> findAllByPayMonthBetween(LocalDate start,LocalDate end);
 @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select p from PayrollEntity p where p.id = :id") Optional<PayrollEntity> findForUpdate(@Param("id") Long id);
}

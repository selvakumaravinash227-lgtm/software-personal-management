package edu.spms.web.repository;
import edu.spms.web.entity.LeaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Collection;
import java.time.LocalDate;
public interface LeaveRepository extends JpaRepository<LeaveEntity,Long> {
 List<LeaveEntity> findAllByOrderByRequestedAtDesc();
 long countByStatus(String status);
 boolean existsByEmployee_IdAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(String employeeId,Collection<String> statuses,LocalDate requestedEnd,LocalDate requestedStart);
}

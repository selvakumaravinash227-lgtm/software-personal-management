package edu.spms.web.repository;
import edu.spms.web.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.Optional;
public interface EmployeeRepository extends JpaRepository<EmployeeEntity,String> {
 @Lock(LockModeType.PESSIMISTIC_WRITE)
 @Query("select e from EmployeeEntity e where e.id = :id")
 Optional<EmployeeEntity> findForUpdate(@Param("id") String id);
}

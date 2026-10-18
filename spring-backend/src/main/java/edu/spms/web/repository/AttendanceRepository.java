package edu.spms.web.repository;
import edu.spms.web.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
public interface AttendanceRepository extends JpaRepository<AttendanceEntity,Long> { List<AttendanceEntity> findAllByOrderByWorkDateDesc(); long countByWorkDateAndStatus(LocalDate date,String status); }

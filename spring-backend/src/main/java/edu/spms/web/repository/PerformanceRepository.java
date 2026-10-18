package edu.spms.web.repository;
import edu.spms.web.entity.PerformanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PerformanceRepository extends JpaRepository<PerformanceEntity,Long> { List<PerformanceEntity> findAllByOrderByEvaluatedAtDesc(); }

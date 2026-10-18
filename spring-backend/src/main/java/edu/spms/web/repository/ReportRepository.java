package edu.spms.web.repository;
import edu.spms.web.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ReportRepository extends JpaRepository<ReportEntity,Long> { List<ReportEntity> findAllByOrderByGeneratedAtDesc(); }

package edu.spms.web.repository;
import edu.spms.web.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface DepartmentRepository extends JpaRepository<DepartmentEntity,Integer> { Optional<DepartmentEntity> findByNameIgnoreCase(String name); }

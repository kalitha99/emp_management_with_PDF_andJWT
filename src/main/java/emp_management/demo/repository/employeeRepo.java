package emp_management.demo.repository;


import emp_management.demo.model.employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface employeeRepo extends JpaRepository<employee,Long> {
}

package hh.Repository;

import hh.model.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IEmployeeRepo extends JpaRepository<Employee, Long> {
    Page<Employee> findAll(Pageable pageable);

    Page<Employee> findAllByNameContaining(String fullName, Pageable pageable);
}

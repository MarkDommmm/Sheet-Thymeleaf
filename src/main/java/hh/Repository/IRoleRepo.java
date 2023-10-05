package hh.Repository;

import hh.model.entity.Department;
import hh.model.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface IRoleRepo extends JpaRepository<Role,Long> {
    Set<Role> findAllByIdIn(Set<Long> ids);
    Page<Role> findAllByroleNameContaining(String fullName, Pageable pageable);
}

package my.academy.springbookstore.repository.role;

import my.academy.springbookstore.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}

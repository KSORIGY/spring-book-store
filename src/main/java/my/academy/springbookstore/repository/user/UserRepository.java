package my.academy.springbookstore.repository.user;

import java.util.Optional;
import my.academy.springbookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

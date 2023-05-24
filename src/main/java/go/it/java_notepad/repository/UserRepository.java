package go.it.java_notepad.repository;

import go.it.java_notepad.entity.User;
import go.it.java_notepad.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    List<User> findByRole(UserRole userRole);
}

package app.repository;

import app.domain.Position;
import app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
        User findByEmail(String email);
        User findByPositions(Set<Position> position);
}
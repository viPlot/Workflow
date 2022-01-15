package app.repository;

import app.domain.Position;
import app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
        User findByEmail(String email);
        User findByPositions(Position position);
}
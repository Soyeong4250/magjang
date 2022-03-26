package B208.mag_jang.repository;

import B208.mag_jang.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataJpaUserRepository extends JpaRepository<User, Long>, UserRepository {
    @Override
    Optional<User> findByNickName(String NickName);
    Optional<User> findByNaverId(String naverId);
    List<User> findTop10ByOrderByRankPointDesc();
}

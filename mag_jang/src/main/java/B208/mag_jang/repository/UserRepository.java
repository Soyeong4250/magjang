package B208.mag_jang.repository;

import B208.mag_jang.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByNickName(String NickName);
    Optional<User> findByNaverId(String naverId);
    List<User> findTop10ByOrderByRankPointDesc();
}

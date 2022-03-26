package B208.mag_jang.service;

import B208.mag_jang.domain.User;
import B208.mag_jang.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

//@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

//    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User join(User user){
//        validateDuplicateUser(user);
        userRepository.save(user);
        return user;
    }

    private void validateDuplicateUser(User user) {
        userRepository.findByNickName(user.getNickName())
                .ifPresent(u -> {
                    throw new IllegalStateException("이미 존재하는 닉네임입니다.");
                });
    }

    public User findUserUsingNaverId(String naverId){
        return userRepository.findByNaverId(naverId)
                .orElseGet(() -> JoinNewUserWithNaverId(naverId));
    }

    public User JoinNewUserWithNaverId(String naverId){
        User user = new User();
        user.setNaverId(naverId);
        return join(user);
    }

    public String setNickName(Long userId, String nickName){
        if (userRepository.findByNickName(nickName).isPresent()) {
            return "이미 존재하는 닉네임입니다!";
        }
        Optional<User> user = userRepository.findById(userId);
//                .ifPresent(u -> u.setNickName(nickName));
        if (user.isPresent()) {
            user.ifPresent(u -> u.setNickName(nickName));
            return "true";
        } else {
            return "유저 데이터 오류! 관리자에게 문의하세요";
        }
    }

    public Optional<User> getUser(String nickname){
        return userRepository.findByNickName(nickname);
    }

    public void setRankPoint(String nickname, int value){
        Optional<User> user = userRepository.findByNickName(nickname);
        user.ifPresent(u -> u.setRankPoint(u.getRankPoint()+value));
    }

    public void setWinAmount(String nickname){
        Optional<User> user = userRepository.findByNickName(nickname);
        user.ifPresent(u -> u.setWinAmount(u.getWinAmount()+1));
    }

    public void setProGangAmount(String nickname){
        Optional<User> user = userRepository.findByNickName(nickname);
        user.ifPresent(u -> u.setProGangAmount(u.getProGangAmount()+1));
    }

    public List<User> getRank() {
        return userRepository.findTop10ByOrderByRankPointDesc();
    }
}

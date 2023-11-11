package pwr.chesstournamentsbackend.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pwr.chesstournamentsbackend.dto.RegisterDTO;
import pwr.chesstournamentsbackend.model.Category;
import pwr.chesstournamentsbackend.model.User;
import pwr.chesstournamentsbackend.repository.CategoryRepository;
import pwr.chesstournamentsbackend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;
@Service
public class UserService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public UserService(UserRepository userRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
    public Optional<User> findUserByUid(String uid) {
        return userRepository.findByUid(uid);
    }

    public User updateUser(Integer id, User user) {
        if (userRepository.existsById(id)) {
            user.setUserId(id);
            return userRepository.save(user);
        } else {
            throw new EntityNotFoundException("User not found with id " + id);
        }
    }
    public User registerUser(RegisterDTO registerDTO) throws FirebaseAuthException {
            String idToken = registerDTO.getIdToken();
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            User newUser = new User();
            newUser.setUid(uid);
            newUser.setEmail(decodedToken.getEmail());
            newUser.setLogin(registerDTO.getLogin());
            newUser.setName(registerDTO.getName());
            newUser.setSurname(registerDTO.getSurname());
            newUser.setAge(registerDTO.getAge());
            Category category = categoryRepository.findById(registerDTO.getCategoryId()).orElseThrow(() -> new EntityNotFoundException("Category not found with id " + registerDTO.getCategoryId()));
            newUser.setCategory(category);
            return userRepository.save(newUser);
    }
}

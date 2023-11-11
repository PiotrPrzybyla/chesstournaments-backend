package pwr.chesstournamentsbackend.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.chesstournamentsbackend.dto.LoginDTO;
import pwr.chesstournamentsbackend.dto.RegisterDTO;
import pwr.chesstournamentsbackend.model.User;
import pwr.chesstournamentsbackend.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserApiController {
    public final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer user_id) {
        return userService.findById(user_id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer user_id) {
        userService.deleteUser(user_id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{user_id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer user_id, @RequestBody User user) {
        User updatedUser = userService.updateUser(user_id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
        try {

               User newUser = userService.registerUser(registerDTO);
//            if (userService.getUserByUid(newUser.getUid()).isPresent()) {
//                return new ResponseEntity<>("User already exists in local database", HttpStatus.BAD_REQUEST);
//            }
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (FirebaseAuthException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }


    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestHeader(value = "Authorization") String idTokenHeader, HttpSession session) {
        try {
            String idToken = idTokenHeader.startsWith("Bearer ") ? idTokenHeader.substring(7) : idTokenHeader;

            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);

            session.setAttribute("uid", decodedToken.getUid());
            session.setAttribute("email", decodedToken.getEmail());
            String sessionToken = session.getId();

            Map<String, Object> sessionInfo = new HashMap<>();
            sessionInfo.put("sessionToken", sessionToken);
            sessionInfo.put("uid", decodedToken.getUid());

            return ResponseEntity.ok(sessionInfo);

        } catch (FirebaseAuthException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Nieprawidłowy token ID.");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    @GetMapping("/isLogged")
    public ResponseEntity<?> checkSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session.getAttribute("uid") != null) {
            return new ResponseEntity<>(Map.of("message", "Użytkownik zalogowany."), HttpStatus.OK);

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Użytkownik niezalogowany");
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            Cookie cookie = new Cookie("JSESSIONID", null);
            cookie.setPath(request.getContextPath());
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        return ResponseEntity.ok(Map.of("message", "Użytkownik wylogowany pomyślnie."));
    }
}

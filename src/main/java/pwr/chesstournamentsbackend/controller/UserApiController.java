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
import pwr.chesstournamentsbackend.dto.ResponseMessage;
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

    @GetMapping
    public ResponseEntity<User> getUserById(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("uid") != null){
            String uid = (String) session.getAttribute("uid");
            return userService.findUserByUid(uid)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
        return  new ResponseEntity<>(new User(), HttpStatus.UNAUTHORIZED);

    }
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
        try {
            User newUser = userService.registerUser(registerDTO);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (FirebaseAuthException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(@RequestHeader(value = "Authorization") String idTokenHeader, HttpSession session) {
        try {
            String idToken = idTokenHeader.startsWith("Bearer ") ? idTokenHeader.substring(7) : idTokenHeader;

            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);

            session.setAttribute("uid", decodedToken.getUid());
            session.setAttribute("email", decodedToken.getEmail());

            return new ResponseEntity<>(new ResponseMessage("User is logged"), HttpStatus.OK);

        } catch (FirebaseAuthException e) {
            return new ResponseEntity<>(new ResponseMessage("Wrong ID token"), HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/isLogged")
    public ResponseEntity<ResponseMessage> checkSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session.getAttribute("uid") != null) {
            return new ResponseEntity<>(new ResponseMessage("User is logged"), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new ResponseMessage("User is not logged"), HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<ResponseMessage> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            Cookie cookie = new Cookie("JSESSIONID", null);
            cookie.setPath(request.getContextPath());
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        return new ResponseEntity<>(new ResponseMessage("User logged out"), HttpStatus.OK);
    }
}

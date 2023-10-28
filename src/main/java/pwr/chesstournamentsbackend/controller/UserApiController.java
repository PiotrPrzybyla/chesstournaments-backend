package pwr.chesstournamentsbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.chesstournamentsbackend.model.User;
import pwr.chesstournamentsbackend.service.UserService;

@RestController
@RequestMapping("/api/user")
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

}

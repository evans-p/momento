package gr.evansp.momento.controller;

import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users/v1/")
public class UserManagementController {

  @PostMapping("register")
  public ResponseEntity<?> registerUser() {
    return ResponseEntity.of(Optional.of(""));
  }

  @GetMapping("")
  public ResponseEntity<?> getMe(@PathVariable String userId) {
    return ResponseEntity.of(Optional.of(""));
  }

  @GetMapping("{userId}")
  public ResponseEntity<?> getUser(@PathVariable String userId) {
    return ResponseEntity.of(Optional.of(""));
  }

  @GetMapping("{userId}/followers")
  public ResponseEntity<?> getFollowers(@PathVariable String userId) {
    return ResponseEntity.of(Optional.of(""));
  }

  @PutMapping("{userId}")
  public ResponseEntity<?> updateUser(@RequestBody String userId) {
    return ResponseEntity.of(Optional.of(""));
  }

  @PostMapping("{userId}/follow")
  public ResponseEntity<?> follow(@PathVariable String userId) {
    return ResponseEntity.of(Optional.of(""));
  }

  @PostMapping("{userId}/unfollow")
  public ResponseEntity<?> unfollow(@PathVariable String userId) {
    return ResponseEntity.of(Optional.of(""));
  }
}

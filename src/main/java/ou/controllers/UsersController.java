package ou.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ou.entities.Role;
import ou.entities.User;
import ou.services.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/api/users", produces = "application/json")
public class UsersController {
    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findUserById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable("id") Long id,
                                               @Valid @RequestBody User user) {
        return new ResponseEntity<>(userService.updateUserById(id, user), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("Successfully deleted!");
    }

    @GetMapping("/{id}/roles")
    public ResponseEntity<Set<Role>> updateUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findUserById(id).getRoles(), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<Set<Role>> addRoleToUser(@PathVariable("id") Long id,
                                                   @RequestBody String roleName) {
        return new ResponseEntity<>(userService.addRoleToUser(id, roleName), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/roles")
    public ResponseEntity<Set<Role>> deleteRoleFromUser(@PathVariable("id") Long id,
                                                        @RequestBody String roleName) {
        return new ResponseEntity<>(userService.deleteRoleFromUser(id, roleName), HttpStatus.CREATED);
    }

}

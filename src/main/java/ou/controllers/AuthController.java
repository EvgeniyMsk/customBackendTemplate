package ou.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ou.entities.User;
import ou.requests.LoginRequest;
import ou.responses.LoginResponse;
import ou.services.AuthService;
import ou.services.UserService;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping(path = "/api/auth", produces = "application/json")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthService authService,
                          UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/signIn")
    public ResponseEntity<LoginResponse> signIn(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateRequest(loginRequest);
    }

    @PostMapping("/signUp")
    public ResponseEntity<Object> signUp(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (!bindingResult.hasErrors())
            return new ResponseEntity<>(userService.create(user), HttpStatus.CREATED);
        return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/me")
    public ResponseEntity<User> showMe(Principal principal) {
        return new ResponseEntity<>(userService.findByUserName(principal.getName()), HttpStatus.OK);
    }
}

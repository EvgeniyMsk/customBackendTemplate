package ou.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ou.entities.Role;
import ou.entities.Status;
import ou.entities.User;
import ou.repository.RoleRepository;
import ou.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
    }

    public User register(User user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        Set<Role> roleList = new HashSet<>();
        roleList.add(roleUser);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(roleList);
        user.setStatus(Status.ACTIVE);
        User registeredUser = userRepository.save(user);
        log.info("IN REGISTER - user: {} successfully registered", registeredUser);
        return registeredUser;
    }

    public List<User> findAll() {
        List<User> result = userRepository.findAll();
        log.info("IN FINDALL - users: {} found", result.size());
        return result;
    }

    public User findByUserName(String username) {
        User result = userRepository.findByUserName(username);
        log.info("IN FINDBYUSERNAME - user: {} found", result);
        return result;
    }

    public User findById(Long id) {
        User result = userRepository.findById(id).orElse(null);
        log.info("IN findById - user: {} found", result);
        return result;
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
        log.info("IN deleteById - user with: {} successfully deleted", id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User temp = userRepository.findByUserName(username);
        if (temp == null)
            throw new UsernameNotFoundException("User not found");
        return temp;
    }
}

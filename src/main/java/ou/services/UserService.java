package ou.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
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
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        log.info("IN CREATE:" + user);
        Role roleUser = roleRepository.findByName("ROLE_USER");
        Set<Role> roleList = new HashSet<>();
        roleList.add(roleUser);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roleList);
        user.setStatus(Status.ACTIVE);
        User registeredUser = userRepository.save(user);
        log.info("IN REGISTER - user: {} successfully registered", registeredUser);
        return registeredUser;
    }

    public User findByUserName(String username) {
        User result = userRepository.findByUserName(username);
        log.info("IN FINDBYUSERNAME - user: {} found", result);
        return result;
    }

    public void deleteUserById(Long id) {
        try {
            userRepository.deleteById(id);
            log.info("IN deleteById - user with: {} successfully deleted", id);
        } catch (Exception e)
        {
            log.warn("IN deleteById - user with: {} not exists", id);
        }
    }

    public List<User> findAllUsers() {
        List<User> result = userRepository.findAll();
        log.info("IN FINDALL - users: {} found", result.size());
        return result;
    }

    public User findUserById(Long id) {
        User result = userRepository.findById(id).orElse(null);
        log.info("IN findById - user: {} found", result);
        return result;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User temp = userRepository.findByUserName(username);
        if (temp == null)
            throw new UsernameNotFoundException("User not found");
        return temp;
    }

    public Set<Role> addRoleToUser(Long id, String roleName) {
        try {
            JSONObject jsonObject = new JSONObject(roleName);
            Role role = roleRepository.findByName(jsonObject.getString("roleName"));
            User user = userRepository.findById(id).orElse(null);
            if (user != null && role != null)
            {
                user.getRoles().add(role);
                User updatedUser = userRepository.save(user);
                return updatedUser.getRoles();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public Set<Role> deleteRoleFromUser(Long id, String roleName) {
        try {
            JSONObject jsonObject = new JSONObject(roleName);
            Role role = roleRepository.findByName(jsonObject.getString("roleName"));
            User user = userRepository.findById(id).orElse(null);
            if (user != null && role != null)
            {
                user.getRoles().remove(role);
                User updatedUser = userRepository.save(user);
                return updatedUser.getRoles();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public User updateUserById(Long id, User user) {
        User temp = userRepository.findById(id).orElse(null);
        if (temp != null)
        {
            String userName = user.getUsername() == null ? temp.getUsername() : user.getUsername();
            String firstName = user.getFirstName() == null ? temp.getFirstName() : user.getFirstName();
            String lastName = user.getLastName() == null ? temp.getLastName() : user.getLastName();
            String email = user.getEmail() == null ? temp.getEmail() : user.getEmail();
            String password = user.getPassword() == null ? temp.getPassword() : passwordEncoder.encode(user.getPassword());
            temp.setUserName(userName);
            temp.setFirstName(firstName);
            temp.setLastName(lastName);
            temp.setEmail(email);
            temp.setPassword(password);
            return userRepository.save(temp);
        } else
            return null;
    }
}

package ou;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ou.repository.RoleRepository;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class MainApplicationTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testRoleRepo() {
        System.out.println(roleRepository.findByName("ROLE_USER"));
    }
}

package ou;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class MainApplicationTest {
    @Test
    public void test() {
        Optional<Integer> optional = Optional.of(74);
        optional.ifPresent(System.out::println);
    }
}

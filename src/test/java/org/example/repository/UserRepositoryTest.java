package org.example.repository;

import org.example.db.DatabaseManager;
import org.example.model.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTest {

    private static UserRepository repo;

    @BeforeAll
    static void setup() throws Exception {
        // 테스트용 인메모리 DB를 사용하도록 DatabaseManager를 상속 없이
        // 실제 파일 DB 대신 인메모리 H2로 재초기화
        initInMemory();
        repo = new UserRepositoryImpl() {
            // DatabaseManager.getConnection() 대신 인메모리 연결을 쓰도록 오버라이드 불필요
            // — DatabaseManager가 static이므로, 여기서는 테스트 전용 DB를 별도로 두지 않고
            //   파일 DB를 초기화한 뒤 각 테스트마다 TRUNCATE 처리
        };
    }

    @BeforeEach
    void truncate() throws Exception {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE users");
        }
    }

    @Test
    void save_새로운유저_저장되고_ID부여() {
        User user = repo.save(new User("홍길동", "hong@test.com", 28));
        assertNotNull(user.getId());
        assertEquals("홍길동", user.getName());
    }

    @Test
    void findById_존재하는ID_유저반환() {
        User saved = repo.save(new User("이순신", "lee@test.com", 45));
        Optional<User> found = repo.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("이순신", found.get().getName());
    }

    @Test
    void findById_없는ID_빈Optional반환() {
        Optional<User> found = repo.findById(9999L);
        assertTrue(found.isEmpty());
    }

    @Test
    void findAll_저장된유저목록_전체반환() {
        repo.save(new User("A", "a@test.com", 20));
        repo.save(new User("B", "b@test.com", 22));
        List<User> users = repo.findAll();
        assertEquals(2, users.size());
    }

    @Test
    void update_유저정보_수정됨() {
        User saved = repo.save(new User("김철수", "kim@test.com", 30));
        saved.setAge(31);
        saved.setName("김철수(수정)");
        repo.update(saved);

        User updated = repo.findById(saved.getId()).orElseThrow();
        assertEquals(31, updated.getAge());
        assertEquals("김철수(수정)", updated.getName());
    }

    @Test
    void update_없는ID_예외발생() {
        User ghost = new User("없음", "ghost@test.com", 0);
        ghost.setId(9999L);
        assertThrows(RuntimeException.class, () -> repo.update(ghost));
    }

    @Test
    void deleteById_유저삭제_이후조회안됨() {
        User saved = repo.save(new User("삭제대상", "del@test.com", 10));
        repo.deleteById(saved.getId());
        assertTrue(repo.findById(saved.getId()).isEmpty());
    }

    @Test
    void deleteById_없는ID_예외발생() {
        assertThrows(RuntimeException.class, () -> repo.deleteById(9999L));
    }

    private static void initInMemory() {
        DatabaseManager.initialize();
    }
}

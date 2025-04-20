package eg1;

import static org.assertj.core.api.Assertions.assertThat;
import static util.TestUtils.executeCommit;
import static util.TestUtils.genNumStr;

import io.silver.domain.eg1.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class EntityManagerTests {

    static EntityManagerFactory entityManagerFactory;
    EntityManager entityManager;

    @BeforeAll
    static void setUp() {
        entityManagerFactory = Persistence.createEntityManagerFactory("grepp-hibernate-exp");
    }

    @BeforeEach
    void init() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    void close(){
        entityManager.close();
    }

    @AfterAll
    static void tearDown() {
        entityManagerFactory.close();
    }


    @Test
    @DisplayName("EntityManager & entityManagerFactory test")
    void em_test() throws Exception {

        Map<String, Object> properties = entityManagerFactory.getProperties();

        String jdbcDriver = properties.get("jakarta.persistence.jdbc.driver").toString();
        String jdbcUrl = properties.get("jakarta.persistence.jdbc.url").toString();

        log.info("jdbcDriver = {}", jdbcDriver);
        log.info("jdbcUrl = {}", jdbcUrl);

        assertThat(jdbcDriver).isEqualTo("com.mysql.cj.jdbc.Driver");
        assertThat(jdbcUrl).isEqualTo("jdbc:mysql://localhost:3306/grepp_hibernate_test");

    }

    @Test
    @DisplayName("save test")
    void save_test() throws Exception {

        Member member = genMember(genMemberName());

        executeCommit(entityManager, () -> {
            entityManager.persist(member);
        });

    }

    @Test
    @DisplayName("select test")
    void select_test() throws Exception {

        Member member = genMember(genMemberName());

        executeCommit(entityManager, () -> {
            entityManager.persist(member);
            Member findMember = entityManager.find(Member.class, member.getId());


            assertThat(findMember).isEqualTo(member);
            assertThat(findMember.getId()).isEqualTo(member.getId());
            assertThat(findMember.getName()).isEqualTo(member.getName());
        });


        executeCommit(entityManager, () -> {
            Member findMember = entityManager.find(Member.class, member.getId());
            assertThat(findMember.getId()).isEqualTo(member.getId());

            findMember.setName("ADMIN");
        });

        executeCommit(entityManager, () -> {
            Member findMember = entityManager.find(Member.class, member.getId());
            assertThat(findMember.getName()).isEqualTo("ADMIN");

            entityManager.detach(findMember); // Persistence Context에서 나감
            findMember.setName("MEMBER"); // 반영 X
        });

        executeCommit(entityManager, () -> {
            Member findMember = entityManager.find(Member.class, member.getId()); // DB에서 가져옴
            assertThat(findMember.getName()).isEqualTo("ADMIN");
            assertThat(member.getName()).isEqualTo("MEMBER");

            Member merge = entityManager.merge(member); // 1차 캐시에 있던 것
            // merge 할 때, 영속성 컨텍스트에 이미 영속 객체가 있다면, 그 객체랑 같은 객체를 반환해주고,
            // 만약, 영속 객체가 없다면 DB에서 조회해서 객체를 생성하고, detached된 객체의 값을 복사해서 넣어준다.
//            Member merge = entityManager.merge(findMember);

            assertThat(merge).isEqualTo(findMember);
            assertThat(merge.getName()).isEqualTo("MEMBER");
            assertThat(findMember.getName()).isEqualTo("MEMBER");

        });
    }

    @Test
    @DisplayName("write-behind test")
    void write_behind_test() throws Exception {

        Member member1 = genMember(genMemberName());
        Member member2 = genMember(genMemberName());

        executeCommit(entityManager, () -> {
            entityManager.persist(member1);
            entityManager.persist(member2);

            log.info("아직 쿼리 실행 안 함. 나중에 SQL문 나간다~");
        });


    }

    private Member genMember(String genMemberName) {

        return Member.builder()
                .id(genMemberName)
                .name(genMemberName)
                .build();

    }

    private String genMemberName() {
        return "USER_" + genNumStr();
    }

}

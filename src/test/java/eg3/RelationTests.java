package eg3;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static util.TestUtils.executeCommit;

import io.silver.domain.eg3._1.Player;
import io.silver.domain.eg3._1.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class RelationTests {


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
    @DisplayName("check data")
    void check_data_test() throws Exception {

        List<Player> bus = new ArrayList<>();

        executeCommit(entityManager, () -> {
//            Team team = entityManager.find(Team.class, 1);
            Player player = entityManager.find(Player.class, 1); // fetch type을 lazy로 안 주면 join해서 가져옴.
            Team team = player.getTeam();

            bus.add(player);
        });

        Player player = bus.get(0); // 영속성 컨텍스트 안에서 가져오는 것


    }

    @Test
    @DisplayName("proxy test")
    void proxy_test() throws Exception {

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Player player = null;

        try {
            player = entityManager.find(Player.class, 1);
            log.info("player.getName() = {}", player.getName());

//            entityManager.detach(player);
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            transaction.commit();
        }

//        entityManager.close();

        Team team = player.getTeam();

        log.info("team = {}", team.getClass());

        log.info("team.getName() = {}", team.getName());

        log.info("team = {}", team.getClass());
//        assertThatThrownBy(
//                () -> {
//                    team.getName();
//                }
//        ).isInstanceOf(LazyInitializationException.class);
    }

    @Test
    @DisplayName("getReference test")
    void get_reference_exception_test() throws Exception {

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Player player = null;

        try {
            player = entityManager.getReference(Player.class, 1); // 프록시 객체임. (초기화 X)

//            entityManager.detach(player);
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            transaction.commit();
        }

        log.info("player.getClass() = {}", player.getClass());

        log.info("player.getName() = {}", player.getName()); // 이 때 초기화 된다. 
        entityManager.close();

        log.info("player.getClass() = {}", player.getClass());
        log.info("player.getName() = {}", player.getName()); // entityManager.close() 했지만, 이젠 1차 캐시에 있기 때문에 실제 객체처럼 동작할 수 있다.
        
        // getReference로 가져오면 처음부터 끝까지 프록시 타입이다.
        // 하지만, 처음 초기화 이후에는 실제 객체처럼 동작한다.


    }
    
    @Test
    @DisplayName("proxy check")
    void proxy_check() throws Exception {

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Player player = null;

        try {
            player = entityManager.getReference(Player.class, 1);
        } catch (Exception e){
            transaction.rollback();
        }

        transaction.commit();
        entityManager.close();

//        String name = player.getName();


        boolean result = entityManagerFactory.getPersistenceUnitUtil().isLoaded(player);

//        assertThat(result).isTrue();

    }

}

package eg2;

import static util.TestUtils.executeCommit;

import io.silver.domain.eg2._3.GymMembership;
import io.silver.domain.eg2._3.Level;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EntityObjTests {

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
    @DisplayName("table create")
    void table_create() throws Exception {



    }

    @Test
    @DisplayName("enum type test")
    void enum_test() throws Exception {

        executeCommit(entityManager, () -> {
            GymMembership gymMembership1 = new GymMembership();
            gymMembership1.setMembershipLevel(Level.GOLD);

            GymMembership gymMembership2 = new GymMembership();
            gymMembership2.setMembershipLevel(Level.SILVER);

            GymMembership gymMembership3 = new GymMembership();
            gymMembership3.setMembershipLevel(Level.BRONZE);

            entityManager.persist(gymMembership1);
            entityManager.persist(gymMembership2);
            entityManager.persist(gymMembership3);
        });

    }

}

package eg4;

import static org.assertj.core.api.Assertions.assertThat;
import static util.TestUtils.executeCommit;

import io.silver.domain.eg4._1.Coffee;
import io.silver.domain.eg4._1.CoffeeDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class JpqlTests {

    static EntityManagerFactory entityManagerFactory;
    EntityManager entityManager;

    @BeforeAll
    static void init() {
        entityManagerFactory =
                Persistence.createEntityManagerFactory("grepp-hibernate-exp");
    }

    @BeforeEach
    void setUp() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    void close() {
        entityManager.close();
    }

    @AfterAll
    static void tearDown() {
        entityManagerFactory.close();
    }

    @Test
    @DisplayName("jpql select test")
    void jpql_select_test() throws Exception {

        executeCommit(entityManager, () -> {
            Coffee coffee = Coffee.builder()
                    .name("Americano")
                    .amount(1)
                    .price(1_500)
                    .build();

            entityManager.persist(coffee);

            coffee = Coffee.builder()
                    .name("바닐라라떼")
                    .amount(1)
                    .price(2000)
                    .build();

            entityManager.persist(coffee);

            coffee = Coffee.builder()
                    .name("아포카토")
                    .amount(1)
                    .price(3000)
                    .build();

            entityManager.persist(coffee);

            coffee = Coffee.builder()
                    .name("말차라떼")
                    .amount(1)
                    .price(3500)
                    .build();

            entityManager.persist(coffee);

            coffee = Coffee.builder()
                    .name("밀크티")
                    .amount(1)
                    .price(3500)
                    .build();

            entityManager.persist(coffee);

            coffee = Coffee.builder()
                    .name("카푸치노")
                    .amount(1)
                    .price(3500)
                    .build();

            entityManager.persist(coffee);

            coffee = Coffee.builder()
                    .name("아인슈페너")
                    .amount(1)
                    .price(3500)
                    .build();

            entityManager.persist(coffee);

            coffee = Coffee.builder()
                    .name("슈크림라떼")
                    .amount(1)
                    .price(3500)
                    .build();

            entityManager.persist(coffee);

            coffee = Coffee.builder()
                    .name("카페모카")
                    .amount(1)
                    .price(3500)
                    .build();

            entityManager.persist(coffee);

            coffee = Coffee.builder()
                    .name("콜드브루")
                    .amount(1)
                    .price(3500)
                    .build();

            entityManager.persist(coffee);

        });


        executeCommit(entityManager, () -> {
            String jpql = "select c from Coffee c";

            TypedQuery<Coffee> query = entityManager.createQuery(jpql, Coffee.class);
            List<Coffee> coffeeList = query.getResultList();

            log.info("======커피 목록======");

            for (Coffee coffee : coffeeList) {
                log.info("coffee.getName() = {}", coffee.getName());
            } // name만 사용하는데 쿼리문을 보면 모든 필드를 다 조회한다.

            log.info("===================");
        });

        executeCommit(entityManager, () -> {
            String jpql = "select c.name from Coffee c";
            List<String> resultList = entityManager.createQuery(jpql, String.class).getResultList();

            log.info("======커피 목록======");

            for (String coffeeName : resultList) {
                log.info("coffeeName = {}", coffeeName);
            }
            log.info("===================");
        });

        executeCommit(entityManager, () -> {
            String jpql1 = "select c from Coffee c where c.name=:name";

            TypedQuery<Coffee> query = entityManager.createQuery(jpql1, Coffee.class);
            query.setParameter("name", "아인슈페너");
            List<Coffee> coffeeList = query.getResultList();

            log.info("======커피 목록======");

            for (Coffee coffee : coffeeList) {
                log.info("coffee.getPrice() = {}", coffee.getPrice());
            } // name만 사용하는데 쿼리문을 보면 모든 필드를 다 조회한다.

            log.info("===================");
        });

        executeCommit(entityManager, () -> {
            String jqpl = "select c.name, c.price from Coffee c";
            String jpql1 = "select new io.silver.domain.eg4._1.CoffeeDto(c.name, c.price) from Coffee c";

            TypedQuery<CoffeeDto> query = entityManager.createQuery(jpql1, CoffeeDto.class);
            List<CoffeeDto> orderList = query.getResultList();


            log.info("======커피 주문 목록======");
            for (CoffeeDto coffeeDto : orderList) {
                log.info("{} : {}", coffeeDto.getName(), coffeeDto.getPrice());
            }
            log.info("===================");

        });

        executeCommit(entityManager, () -> {
            String jpql = "select c from Coffee c";

            TypedQuery<Coffee> query = entityManager.createQuery(jpql, Coffee.class);
            query.setFirstResult(0); // OFFSET
            query.setMaxResults(5); // LIMIT

            List<Coffee> coffeeList = query.getResultList();

            assertThat(coffeeList.size()).isEqualTo(5);

            for (Coffee coffee : coffeeList) {
                log.info("coffee.getName() = {}", coffee.getName());
            }
        });

    }

}

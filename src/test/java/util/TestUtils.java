package util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestUtils {

    public static String genNumStr() {
        int randNum = (int)(Math.random()* 100_000);
        return Integer.toString(randNum);
    }

    public static void executeCommit(EntityManager entityManager,Runnable action) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            action.run();
        } catch (Exception e) {
            transaction.rollback();
            log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            transaction.commit();
        }
    }


}

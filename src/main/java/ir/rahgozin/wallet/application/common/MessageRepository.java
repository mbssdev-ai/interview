package ir.rahgozin.wallet.application.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(value = """
            SELECT *
            FROM message
            WHERE status = 'PENDING'
            FOR UPDATE SKIP LOCKED
            LIMIT 100
            """,
            nativeQuery = true)
    List<Message> lockPendingMessages();
}

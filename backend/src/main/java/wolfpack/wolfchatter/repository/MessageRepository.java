package wolfpack.wolfchatter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wolfpack.wolfchatter.model.Marker;
import wolfpack.wolfchatter.model.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllByMarker(Marker marker);
}

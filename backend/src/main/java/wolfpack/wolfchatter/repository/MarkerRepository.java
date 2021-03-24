package wolfpack.wolfchatter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wolfpack.wolfchatter.model.Marker;


@Repository
public interface MarkerRepository extends JpaRepository<Marker, Integer> {
}

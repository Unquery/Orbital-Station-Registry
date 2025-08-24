package src.repository;

import src.model.CosmicStation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CosmicStationRepository extends CrudRepository<CosmicStation, Long> {


    @Query("SELECT CASE WHEN COUNT(s)>0 THEN true ELSE false END " +
            "FROM CosmicStation s WHERE size(s.performedOn) < s.missionCapacity")
    boolean existsFreeStation();

    @EntityGraph(attributePaths = "performedOn")
    @Query("SELECT s FROM CosmicStation s WHERE size(s.performedOn) < s.missionCapacity")
    List<CosmicStation> findFreeStationsWithPerformedOn();

    @EntityGraph(attributePaths = "performedOn")
    @Query("SELECT s FROM CosmicStation s WHERE size(s.performedOn) < s.missionCapacity")
    Page<CosmicStation> findFreeStationsWithPerformedOn(Pageable pageable);

    @Query("SELECT s FROM CosmicStation s LEFT JOIN FETCH s.performedOn WHERE s.id = :id")
    Optional<CosmicStation> findByIdWithPerformedOn(@Param("id") Long id);
}

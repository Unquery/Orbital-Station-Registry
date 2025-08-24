package src.repository;

import src.model.Mission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MissionRepository extends CrudRepository<Mission, Long> {


    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Mission m WHERE m.missionCode = :missionCode")
    boolean existsByMissionCode(@Param("missionCode") String missionCode);

    @Query("SELECT m FROM Mission m JOIN m.performedBy s WHERE s.id = :stationId")
    Page<Mission> findByStationId(@Param("stationId") Long stationId, Pageable pageable);
}

package lk.ijse.spms.parkingspaceservice.repository;

import lk.ijse.spms.parkingspaceservice.entity.ParkingSpace;
import lk.ijse.spms.parkingspaceservice.entity.ParkingSpaceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long> {

    List<ParkingSpace> findByCityAndStatus(String city, ParkingSpaceStatus status);

    List<ParkingSpace> findByCity(String city);

    List<ParkingSpace> findByStatus(ParkingSpaceStatus status);

    @Query("SELECT p FROM ParkingSpace p WHERE " +
            "(:city IS NULL OR :city = '' OR LOWER(p.city) = LOWER(:city)) AND " +
            "(:zone IS NULL OR :zone = '' OR LOWER(p.zone) = LOWER(:zone)) AND " +
            "(:available IS NULL OR " +
            " (:available = true AND p.status = lk.ijse.spms.parkingspaceservice.entity.ParkingSpaceStatus.AVAILABLE) OR " +
            " (:available = false AND p.status <> lk.ijse.spms.parkingspaceservice.entity.ParkingSpaceStatus.AVAILABLE))")
    List<ParkingSpace> searchParkingSpaces(
            @Param("city") String city,
            @Param("zone") String zone,
            @Param("available") Boolean available
    );
}

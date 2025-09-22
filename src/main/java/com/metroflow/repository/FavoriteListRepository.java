package com.metroflow.repository;

import com.metroflow.model.dto.FavoriteList;
import com.metroflow.model.dto.FavoriteListPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface FavoriteListRepository extends JpaRepository<FavoriteList, Long> {

    // 즐겨찾는 역에 등록된 역인지 확인하는 쿼리
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN 1 ELSE 0 END FROM FavoriteList f " +
            "JOIN f.station s WHERE f.user.userId = :userId AND s.stationName = :stationName")
    int isFavorite(@Param("userId") String userId, @Param("stationName") String stationName);

    // 즐겨찾기 리스트에서 user_id, station_name 기준으로 staion_id를 반환하는 쿼리
    @Query("SELECT f.station.stationId " +
            "FROM FavoriteList f JOIN SubwayStation s ON f.station.stationId = s.stationId " +
            "WHERE f.user.userId = :userId AND s.stationName = :stationName")
    List<Long> getFavoriteListStationIds(@Param("userId") String userId, @Param("stationName") String stationName);

    // 즐겨찾기 등록 전 중복 데이터 체크 쿼리
    @Query("SELECT f FROM FavoriteList f WHERE f.user.userId = :userId AND f.station.stationId = :stationId")
    List<FavoriteList> checkisDuplicateFavoriteList(@Param("userId") String userId, @Param("stationId") Long stationId);

    // JPA 기본 인터페이스
    List<FavoriteList> findByUser_UserIdAndStation_StationId(String userId, Long stationId);
    
    // 유저 id 기준으로 즐겨찾기 데이터를 모두 가져오는 쿼리
    @Query("SELECT f FROM FavoriteList f WHERE f.user.userId = :userId")
    List<FavoriteList> findAllByUserId(@Param("userId") String userId);

    // 유저 id 기준으로 페이지에 표시 할 즐겨찾기 데이터를 가져오는 쿼리
    @Query("SELECT f.user.userId AS userId, f.station.stationId AS stationId, s.stationLine AS stationLine, s.stationName AS stationName, " +
            "t.directionType AS directionType, t.dayType AS dayType, ti AS subwayTime " +
            "FROM FavoriteList f " +
            "JOIN SubwayStation s ON f.station.stationId = s.stationId " +
            "JOIN SubwayType t ON t.subwayStation.stationId = f.station.stationId " +
            "JOIN SubwayTime ti ON ti.typeId = t.typeId " +
            "WHERE f.user.userId = :userId")
    Page<FavoriteListPageDto> findFavoriteListByUserId(@Param("userId") String userId, Pageable pageable);


}

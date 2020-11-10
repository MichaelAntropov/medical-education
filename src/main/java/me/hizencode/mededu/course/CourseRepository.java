package me.hizencode.mededu.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<CourseEntity, Integer> {

    Page<CourseEntity> findAllByNameContains(String name, Pageable pageable);

    //TODO: Check alternatives for query
    @Query(value = "SELECT c FROM CourseEntity c join CourseSpecialityEntity cs on c.id = cs.courseId  " +
            "where cs.specialityId in :specialities GROUP BY c.id HAVING COUNT(c.id) >= :duplicates")
    Page<CourseEntity> findAllBySpecialities(@Param("specialities") List<Integer> specialities,
                                             @Param("duplicates") Long duplicates, Pageable pageable);

    //TODO: Check alternatives for query
    @Query(value = "SELECT c FROM CourseEntity c join CourseSpecialityEntity cs on c.id = cs.courseId  " +
            "where c.name LIKE %:name% and cs.specialityId in :specialities GROUP BY c.id HAVING COUNT(c.id) >= :duplicates")
    Page<CourseEntity> findAllByNameAndSpecialities(@Param("name") String name,
                                                    @Param("specialities") List<Integer> specialities,
                                                    @Param("duplicates") Long duplicates,
                                                    Pageable pageable);
}

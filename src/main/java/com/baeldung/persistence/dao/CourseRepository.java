package com.baeldung.persistence.dao;

import com.baeldung.persistence.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {


    @Query("SELECT c FROM Course c WHERE lower(c.title) LIKE lower(concat('%',:keyword,'%'))"
            + " OR lower(c.description) LIKE lower(concat('%',:keyword,'%'))")
    public List<Course> search(@Param("keyword") String keyword);
}

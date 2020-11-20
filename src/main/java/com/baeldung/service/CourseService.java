package com.baeldung.service;

import com.baeldung.persistence.dao.CourseRepository;
import com.baeldung.persistence.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public List<Course> listAll(String keyword) {
        if (keyword != null) {
            return courseRepository.search(keyword);
        }
        return courseRepository.findAll();
    }
}

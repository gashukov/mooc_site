package com.baeldung.web.controller;

import com.baeldung.persistence.model.Course;
import com.baeldung.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class CourseController {
    @Autowired
    private CourseService courseService;

    @RequestMapping("/courses")
    public String viewHomePage(Model model, @Param("keyword") String keyword) {
        List<Course> listProducts = courseService.listAll(keyword);
        model.addAttribute("listCourses", listProducts);
        model.addAttribute("keyword", keyword);

        return "courses";
    }
}

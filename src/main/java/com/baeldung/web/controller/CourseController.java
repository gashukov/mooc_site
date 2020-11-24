package com.baeldung.web.controller;

import com.baeldung.persistence.model.Course;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class CourseController {

    private final EntityManager entityManager;

    private final int resultsPerPage = 10;
    private int currentPage;
    private int totalCourses;

    public CourseController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

//    @RequestMapping("/courses")
//    public String coursesSearch(Model model, @Param("keyword") String keyword) {
//        List<Course> listProducts = courseService.listAll(keyword);
//        model.addAttribute("listCourses", listProducts);
//        model.addAttribute("keyword", keyword);
//
//        return "courses";
//    }

    @RequestMapping("/courses")
    public String coursesSearch2(Model model, @RequestParam("page") Optional<Integer> page, @Param("keyword") String keyword) {

        currentPage = page.orElse(1);

        List<Course> listCourses = getCourseBasedOnWord(keyword, currentPage, resultsPerPage);
        model.addAttribute("listCourses", listCourses);
        model.addAttribute("keyword", keyword);
        int totalPages = totalCourses / resultsPerPage + (totalCourses % resultsPerPage == 0 ? 0 : 1);

        List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("resultsPerPage", resultsPerPage);
        model.addAttribute("currentPage", currentPage);
        System.out.println(currentPage);
        System.out.println(totalPages);
        System.out.println(totalCourses);
//        model.addAttribute("totalPages", totalPages);


        return "courses";
    }

    @Transactional
    public List<Course> getCourseBasedOnWord(String word, int page, int resultsPerPage){

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        QueryBuilder qb = fullTextEntityManager
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Course.class)
                .get();

        Query query;
        if (word == null || word.isEmpty()) {
            query = qb.all().createQuery();
        } else {
            query = qb.keyword().fuzzy().withEditDistanceUpTo(2)
                    .onFields("title", "description")
                    .matching(word)
                    .createQuery();
        }

        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, Course.class);

        totalCourses = fullTextQuery.getResultSize();
        System.out.println("total " + totalCourses);

        fullTextQuery.setMaxResults(resultsPerPage);
        fullTextQuery.setFirstResult((page-1) * resultsPerPage);

        List<Course> result = fullTextQuery.getResultList();

        return result ;
    }
}

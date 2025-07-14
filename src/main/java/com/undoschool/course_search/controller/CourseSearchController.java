package com.undoschool.course_search.controller;

import com.undoschool.course_search.document.CourseDocument;
import com.undoschool.course_search.dto.SearchRequest;
import com.undoschool.course_search.service.CourseSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class CourseSearchController {

    @Autowired
    private final CourseSearchService courseSearchService;

    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }
    @GetMapping
    public List<CourseDocument> searchCourse(@ModelAttribute SearchRequest request) throws Exception {
        return courseSearchService.searchCourses(request);
    }
}

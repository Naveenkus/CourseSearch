package com.undoschool.course_search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.undoschool.course_search.document.CourseDocument;
import com.undoschool.course_search.repository.CourseRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataLoaderService {

    @Autowired
    private final CourseRepository courseRepository;

    @PostConstruct
    public void loadData() {
        try {
            ObjectMapper mapper =  new ObjectMapper();
            InputStream inputStream = new ClassPathResource("sample-courses.json").getInputStream();

            List<CourseDocument> courses = mapper.readValue(inputStream, new TypeReference<>() {});
            courseRepository.saveAll(courses);
            System.out.println("Courses indexed succesfully");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

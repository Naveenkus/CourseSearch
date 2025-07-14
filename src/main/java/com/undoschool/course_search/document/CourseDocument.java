package com.undoschool.course_search.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.Instant;

@Data
@Document(indexName = "courses")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseDocument {

    @Id
    private String id;

    private String title;
    private String description;
    private String category;
    private String type;
    private String gradeRange;

    private int minAge;
    private int maxAge;

    private double price;

    private Instant nextSessionDate;
}

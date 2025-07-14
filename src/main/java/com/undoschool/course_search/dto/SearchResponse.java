package com.undoschool.course_search.dto;

import com.undoschool.course_search.document.CourseDocument;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResponse {
    private long total;
    private List<CourseDocument> courses;
}
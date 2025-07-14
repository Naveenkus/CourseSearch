package com.undoschool.course_search.dto;

import lombok.Data;

@Data
public class SearchRequest {
    private String q;
    private Integer minAge;
    private Integer maxAge;
    private String category;
    private String type;
    private Double minPrice;
    private Double maxPrice;
    private String startDate;
    private String sort;
    private Integer page = 0;
    private Integer size = 10;

}

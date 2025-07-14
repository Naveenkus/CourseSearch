package com.undoschool.course_search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.undoschool.course_search.document.CourseDocument;
import com.undoschool.course_search.dto.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseSearchService {

    @Autowired
    private final ElasticsearchClient elasticsearchClient;

    public List<CourseDocument> searchCourses(SearchRequest request) throws Exception {
        List<Query> mustQueries = new ArrayList<>();

        if (request.getQ() != null && !request.getQ().isEmpty()){
            mustQueries.add(MultiMatchQuery.of(m-> m
                    .fields("title", "description")
                    .query(request.getQ())
            )._toQuery());
        }
        if (request.getCategory() != null){
            mustQueries.add(TermQuery.of(t -> t
                    .field("category.keyword")
                    .value(request.getCategory())
            )._toQuery());
        }

        if (request.getType() != null){
            mustQueries.add(TermQuery.of(t -> t
                    .field("type.keyword")
                    .value(request.getType())
            )._toQuery());
        }

        if (request.getMinAge() != null || request.getMaxAge() != null){
            RangeQuery.Builder ageRange = new RangeQuery.Builder().field("minAge");

            if (request.getMinAge() != null) ageRange.gte(JsonData.of(request.getMinAge()));
            if (request.getMaxAge() != null) ageRange.lte(JsonData.of(request.getMaxAge()));

            mustQueries.add(ageRange.build()._toQuery());
        }

        if (request.getStartDate() != null) {
            mustQueries.add(RangeQuery.of(r -> r
                    .field("nextSessionDate")
                    .gte(JsonData.of(request.getStartDate()))
                    )._toQuery());
        }

        String sortField = "nextSessionDate";
        SortOrder sortOrder = SortOrder.Asc;

        if ("priceAsc".equals(request.getSort())) {
            sortField = "price";
            sortOrder = SortOrder.Asc;
        } else if ("priceDesc".equals(request.getSort())) {
            sortField = "price";
            sortOrder = SortOrder.Desc;
        }

        final String finalSortField = sortField;
        final SortOrder finalSortOrder = sortOrder;

        co.elastic.clients.elasticsearch.core.SearchRequest searchRequest =
                new co.elastic.clients.elasticsearch.core.SearchRequest.Builder()
                .index("courses")
                .from(request.getPage() * request.getSize())
                .size(request.getSize())
                .query(q -> q.bool(b -> b.must(mustQueries)))
                .sort(s -> s.field(f -> f.field(finalSortField).order(finalSortOrder)))
                .build();

        SearchResponse<CourseDocument> response = elasticsearchClient.search(searchRequest, CourseDocument.class);

        return response.hits().hits().stream()
                .map(hit -> hit.source())
                .toList();
    }
}

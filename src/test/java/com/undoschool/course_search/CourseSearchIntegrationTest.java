package com.undoschool.course_search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.undoschool.course_search.document.CourseDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ExtendWith(SpringExtension.class)
public class CourseSearchIntegrationTest {

    @Container
    static ElasticsearchContainer container = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:8.13.4")
            .withEnv("discovery.type", "single-node");

    private ElasticsearchClient client;

    @BeforeEach
    void setup() throws Exception {
        container.start();

        // Manually build client using container's address
        client = ElasticsearchConfig.buildClient("http://" + container.getHttpHostAddress());

        // Index a sample document
        CourseDocument course = new CourseDocument();
        course.setId("test1");
        course.setTitle("Test Math Course");
        course.setDescription("Learn basic math");
        course.setCategory("Math");
        course.setType("COURSE");
        course.setMinAge(6);
        course.setMaxAge(10);
        course.setPrice(100.0);
        course.setNextSessionDate(Instant.now());

        client.index(IndexRequest.of(i -> i
                .index("courses")
                .id(course.getId())
                .document(course)
        ));

        // Force refresh
        client.indices().refresh(r -> r.index("courses"));
    }

    @Test
    void testBasicSearchReturnsResults() throws Exception {
        SearchRequest request = SearchRequest.of(s -> s
                .index("courses")
                .query(q -> q.match(MatchQuery.of(m -> m
                        .field("title")
                        .query("math")
                )._toQuery().match()))
        );

        SearchResponse<CourseDocument> response = client.search(request, CourseDocument.class);

        List<CourseDocument> results = response.hits().hits().stream()
                .map(hit -> hit.source())
                .toList();

        assertFalse(results.isEmpty());
        assertEquals("Test Math Course", results.get(0).getTitle());
    }
}

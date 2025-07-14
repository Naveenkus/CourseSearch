package com.undoschool.course_search.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.StringReader;

@Component
public class ElasticsearchIndexConfig {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @PostConstruct
    public void createIndex() {
        try {
            ExistsRequest existsRequest = ExistsRequest.of(e -> e.index("courses"));
            boolean exists = elasticsearchClient.indices().exists(existsRequest).value();

            if (!exists) {
                String mappingJson = """
                {
                  "mappings": {
                    "properties": {
                      "id": { "type": "keyword" },
                      "title": { 
                        "type": "text",
                        "analyzer": "standard"
                      },
                      "description": { 
                        "type": "text",
                        "analyzer": "standard"
                      },
                      "category": { 
                        "type": "text",
                        "fields": {
                          "keyword": { "type": "keyword" }
                        }
                      },
                      "type": { 
                        "type": "text",
                        "fields": {
                          "keyword": { "type": "keyword" }
                        }
                      },
                      "gradeRange": { "type": "keyword" },
                      "minAge": { "type": "integer" },
                      "maxAge": { "type": "integer" },
                      "price": { "type": "double" },
                      "nextSessionDate": { "type": "date" }
                    }
                  }
                }
                """;

                CreateIndexRequest createIndexRequest = CreateIndexRequest.of(c -> c
                        .index("courses")
                        .withJson(new StringReader(mappingJson))
                );

                elasticsearchClient.indices().create(createIndexRequest);
                System.out.println("Index 'courses' created successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

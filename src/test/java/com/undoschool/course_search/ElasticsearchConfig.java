package com.undoschool.course_search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

public class ElasticsearchConfig {
    public static ElasticsearchClient buildClient(String hostUrl) {
        RestClient restClient = RestClient.builder(HttpHost.create(hostUrl)).build();
        return new ElasticsearchClient(new RestClientTransport(restClient, new JacksonJsonpMapper()));
    }
}

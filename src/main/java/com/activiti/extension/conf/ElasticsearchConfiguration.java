package com.activiti.extension.conf;

import com.activiti.service.reporting.ReportingIndexManagerImpl;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ElasticsearchConfiguration {
    @Value("${elastic-search.rest-client.address}")
    private String hostname;
    @Value("${elastic-search.rest-client.port}")
    private int port;
    @Value("${elastic-search.rest-client.scheme}")
    private String scheme;
    @Value("${elastic-search.rest-client.connect-timeout}")
    private int connectTimeout;
    @Value("${elastic-search.rest-client.socket-timeout}")
    private int socketTimeout;
    @Value("${elastic-search.rest-client.max-retry-timeout}")
    private int maxRetryTimeout;
    @Value("${elastic-search.rest-client.username}")
    private String username;
    @Value("${elastic-search.rest-client.password}")
    private String password;

    @Bean
    @Primary
    @Qualifier("restClient")
    public RestClient getRestClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        System.out.println("Inside the getRestClient() Bean++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost(hostname, port, scheme))
                .setHttpClientConfigCallback(httpClientBuilder ->
                        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider))
                .setRequestConfigCallback(requestConfigBuilder ->
                        requestConfigBuilder.setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout))
                .setMaxRetryTimeoutMillis(maxRetryTimeout);
        ReportingIndexManagerImpl rim;

        return restClientBuilder.build();
    }
}

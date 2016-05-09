package com.tenforce.uv.extractor.httpClient;

import eu.unifiedviews.helpers.dpu.ontology.EntityDescription;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class for httpClient.
 *
 * @author Tenforce
 */
@EntityDescription.Entity(type = HttpClientVocabulary.STR_CONFIG_CLASS)
public class httpClientConfig_V1 {

    @EntityDescription.Property(uri = HttpClientVocabulary.STR_CONFIG_HAS_URI)
    private String uri;

    @EntityDescription.Property(uri = HttpClientVocabulary.STR_CONFIG_HAS_PARAM)
    private List<HttpClientPair_V1> params = new ArrayList<>();

    @EntityDescription.Property(uri = HttpClientVocabulary.STR_CONFIG_HAS_HEADER)
    private List<HttpClientPair_V1> headers = new ArrayList<>();

    @EntityDescription.Property(uri = HttpClientVocabulary.STR_CONFIG_HAS_METHOD)
    private String method;

    @EntityDescription.Property(uri = HttpClientVocabulary.STR_CONFIG_HAS_BODY)
    private String body;

    @EntityDescription.Property(uri = HttpClientVocabulary.STR_CONFIG_OAUTH_SECRET)
    private String oauthConsumerSecret = "";

    @EntityDescription.Property(uri = HttpClientVocabulary.STR_CONFIG_OAUTH_KEY)
    private String oauthConsumerKey = "";

    public List<HttpClientPair_V1> getHeaders() {
        return headers;
    }

    public void setHeaders(List<HttpClientPair_V1> headers) {
        this.headers = headers;
    }

    public List<HttpClientPair_V1> getParams() {
        return params;
    }

    public void setParams(List<HttpClientPair_V1> params) {
        this.params = params;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOauthConsumerSecret() {
        return oauthConsumerSecret;
    }

    public void setOauthConsumerSecret(String oauthConsumerSecret) {
        this.oauthConsumerSecret = oauthConsumerSecret;
    }

    public String getOauthConsumerKey() {
        return oauthConsumerKey;
    }

    public void setOauthConsumerKey(String oauthConsumerKey) {
        this.oauthConsumerKey = oauthConsumerKey;
    }
}

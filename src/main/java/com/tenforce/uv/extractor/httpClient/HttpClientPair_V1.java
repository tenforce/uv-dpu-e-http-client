package com.tenforce.uv.extractor.httpClient;

import eu.unifiedviews.helpers.dpu.ontology.EntityDescription;

@EntityDescription.Entity(type = HttpClientVocabulary.STR_PAIR_CLASS)
public class HttpClientPair_V1 {
    @EntityDescription.Property(uri = HttpClientVocabulary.STR_PAIR_KEY)
    private String name;
    @EntityDescription.Property(uri = HttpClientVocabulary.STR_PAIR_VALUE)
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}


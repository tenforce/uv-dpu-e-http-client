package com.tenforce.uv.extractor.httpClient;

import eu.unifiedviews.helpers.dpu.ontology.EntityDescription;

import java.io.Serializable;

@EntityDescription.Entity(type = HttpClientVocabulary.STR_PAIR_CLASS)
public class HttpClientPair_V1 implements Serializable {
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

    public HttpClientPair_V1(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public HttpClientPair_V1() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpClientPair_V1 that = (HttpClientPair_V1) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return !(value != null ? !value.equals(that.value) : that.value != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HttpClientPair_V1{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}


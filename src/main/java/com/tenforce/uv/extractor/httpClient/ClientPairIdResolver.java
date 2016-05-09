package com.tenforce.uv.extractor.httpClient;

import com.vaadin.data.util.AbstractBeanContainer;

public class ClientPairIdResolver implements AbstractBeanContainer.BeanIdResolver<String, HttpClientPair_V1> {
  public String getIdForBean(HttpClientPair_V1 bean) {
    return bean != null ? bean.getName() : null;
  }
}


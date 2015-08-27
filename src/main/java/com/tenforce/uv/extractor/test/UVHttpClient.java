package com.tenforce.uv.extractor.test;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.util.Map;

public class UVHttpClient {

    public CloseableHttpResponse sendRequest(String uri, String method, Map<String, String> headers, Map<String, String> params, String body) throws IOException {

        CloseableHttpClient client = HttpClients.createDefault();
        NameValuePair[] parameters = new NameValuePair[params.size()];

        int i = 0;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            parameters[i] = new BasicNameValuePair(entry.getKey(), entry.getValue());
            i++;
        }


        RequestBuilder requestBuilder;
        if (method.equalsIgnoreCase("get")) {
            requestBuilder = RequestBuilder.get();
        } else if (method.equalsIgnoreCase("post")) {
            requestBuilder = RequestBuilder.post()
                    .setEntity(new StringEntity(body));
        } else {
            throw new IllegalArgumentException("Unsupported method");
        }

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }

        HttpUriRequest request = requestBuilder
                .setUri(uri)
                .addParameters(parameters)
                .build();
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream inputStream = entity.getContent();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            File temp = File.createTempFile("downloaded", "file");
            temp.deleteOnExit();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(temp));
            int dataByte;
            while ((dataByte = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(dataByte);
            }
            bufferedInputStream.close();
            bufferedOutputStream.close();
            inputStream.close();
            client.close();
        }
        return response;
    }
}

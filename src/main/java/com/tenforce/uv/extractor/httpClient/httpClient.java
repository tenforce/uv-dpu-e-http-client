package com.tenforce.uv.extractor.httpClient;

import eu.unifiedviews.dpu.DPU;
import eu.unifiedviews.dpu.DPUException;
import eu.unifiedviews.helpers.dpu.config.ConfigHistory;
import eu.unifiedviews.helpers.dpu.context.ContextUtils;
import eu.unifiedviews.helpers.dpu.exec.AbstractDpu;
import eu.unifiedviews.helpers.dpu.extension.ExtensionInitializer;
import eu.unifiedviews.helpers.dpu.extension.faulttolerance.FaultTolerance;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;

/**
 * Main data processing unit class.
 *
 * @author Tenforce
 */
@DPU.AsExtractor
public class httpClient extends AbstractDpu<httpClientConfig_V1> {

    private static final Logger LOG = LoggerFactory.getLogger(httpClient.class);

    @ExtensionInitializer.Init
    public FaultTolerance faultTolerance;

	public httpClient() {
		super(httpClientVaadinDialog.class, ConfigHistory.noHistory(httpClientConfig_V1.class));
	}
		
    @Override
    protected void innerExecute() throws DPUException {

        ContextUtils.sendShortInfo(ctx, "httpClient.message");
        CloseableHttpClient client = HttpClients.createDefault();
        NameValuePair[] parameters = new NameValuePair[config.getParams().size()];

        int i = 0;
        for (Map.Entry<String, String> entry : config.getParams().entrySet()) {
            parameters[i] = new BasicNameValuePair(entry.getKey(), entry.getValue());
            i++;
        }

        try {
            RequestBuilder requestBuilder;
            if (config.getMethod().equalsIgnoreCase("get")) {
                requestBuilder = RequestBuilder.get();
            } else if (config.getMethod().equalsIgnoreCase("post")) {
                requestBuilder = RequestBuilder.post()
                        .setEntity(new StringEntity(config.getBody()));
            } else {
                throw new IllegalArgumentException("Unsupported method");
            }

            for (Map.Entry<String, String> entry : config.getHeaders().entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }

            HttpUriRequest request = requestBuilder
                    .setUri(config.getUri())
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
            } else {
                ContextUtils.sendError(ctx, "No file in response", null);
            }
        } catch (IOException ioe) {
            ContextUtils.sendError(ctx, ioe.getMessage(), null);
        }
        
    }

}

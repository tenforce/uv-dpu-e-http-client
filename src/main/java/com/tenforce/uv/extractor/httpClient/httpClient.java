package com.tenforce.uv.extractor.httpClient;

import eu.unifiedviews.dataunit.DataUnit;
import eu.unifiedviews.dataunit.files.FilesDataUnit;
import eu.unifiedviews.dataunit.files.WritableFilesDataUnit;
import eu.unifiedviews.dataunit.rdf.RDFDataUnit;
import eu.unifiedviews.dpu.DPU;
import eu.unifiedviews.dpu.DPUException;
import eu.unifiedviews.helpers.dataunit.files.FilesDataUnitUtils;
import eu.unifiedviews.helpers.dataunit.resource.Resource;
import eu.unifiedviews.helpers.dataunit.resource.ResourceHelpers;
import eu.unifiedviews.helpers.dpu.config.ConfigHistory;
import eu.unifiedviews.helpers.dpu.context.ContextUtils;
import eu.unifiedviews.helpers.dpu.exec.AbstractDpu;
import eu.unifiedviews.helpers.dpu.extension.ExtensionInitializer;
import eu.unifiedviews.helpers.dpu.extension.faulttolerance.FaultTolerance;
import eu.unifiedviews.helpers.dpu.extension.faulttolerance.FaultToleranceUtils;
import eu.unifiedviews.helpers.dpu.extension.rdf.RdfConfiguration;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import org.apache.commons.io.FileUtils;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Main data processing unit class.
 *
 * @author Tenforce
 */
@DPU.AsExtractor
public class httpClient extends AbstractDpu<httpClientConfig_V1> {

  private static final Logger LOG = LoggerFactory.getLogger(httpClient.class);

  @RdfConfiguration.ContainsConfiguration
  @DataUnit.AsInput(name = "config", optional = true)
  public RDFDataUnit rdfConfiguration;

  @DataUnit.AsOutput(name = "output")
  public WritableFilesDataUnit filesOutput;

  @ExtensionInitializer.Init
  public FaultTolerance faultTolerance;

  @ExtensionInitializer.Init
  public RdfConfiguration _rdfConfiguration;

  public httpClient() {
    super(httpClientVaadinDialog.class, ConfigHistory.noHistory(httpClientConfig_V1.class));
  }

  @Override
  protected void innerExecute() throws DPUException {
    ContextUtils.sendShortInfo(ctx, "httpClient.message");
    CloseableHttpClient client = HttpClients.createDefault();
    NameValuePair[] parameters = new NameValuePair[config.getParams().size()];

    int i = 0;
    for (HttpClientPair_V1 entry : config.getParams()) {
      parameters[i] = new BasicNameValuePair(entry.getName(), entry.getValue());
      i++;
    }

    LOG.info("\nMethod: " + config.getMethod() + "\nURI: " + config.getUri() + "\nBody: " + config.getBody() + "\n");
    HttpEntity responseBody;
    try {
      HttpUriRequest request = buildRequest(parameters);
      if (! config.getOauthConsumerKey().isEmpty() && !config.getOauthConsumerSecret().isEmpty())
        signRequestWithOauth(request);
      CloseableHttpResponse response = client.execute(request);
      validateResponse(response);
      responseBody = response.getEntity();
    } catch (Exception e) {
      throw ContextUtils.dpuException(ctx, e, "httpClient.execute.exception");
    }


    final String fileName = "downloaded.file";
    final FilesDataUnit.Entry destinationFile = faultTolerance.execute(new FaultTolerance.ActionReturn<FilesDataUnit.Entry>() {

      @Override
      public FilesDataUnit.Entry action() throws Exception {
        return FilesDataUnitUtils.createFile(filesOutput, fileName);
      }
    });
    // Add some metadata, TODO: Improve this code!
    faultTolerance.execute(new FaultTolerance.Action() {

      @Override
      public void action() throws Exception {
        final Resource resource = ResourceHelpers.getResource(filesOutput, fileName);
        final Date now = new Date();
        resource.setCreated(now);
        resource.setLast_modified(now);
        ResourceHelpers.setResource(filesOutput, fileName, resource);
      }
    }, "httpClient.execute.exception");
    // Copy file.
    try {
      FileUtils.copyInputStreamToFile(responseBody.getContent(),
          FaultToleranceUtils.asFile(faultTolerance, destinationFile));
    } catch (IOException ex) {
      throw ContextUtils.dpuException(ctx, ex, "httpClient.execute.exception");
    }

  }

  private void signRequestWithOauth(HttpUriRequest request) throws DPUException {
    OAuthConsumer consumer = new CommonsHttpOAuthConsumer(config.getOauthConsumerKey(), config.getOauthConsumerSecret());
    try {
      consumer.sign(request);
    } catch (Exception e) {
      throw ContextUtils.dpuException(ctx, e, "httpClient.execute.exception");
    }
  }

  private HttpUriRequest buildRequest(NameValuePair[] parameters) throws UnsupportedEncodingException {
    RequestBuilder requestBuilder = RequestBuilder.create(config.getMethod());
    requestBuilder.setUri(config.getUri());
    if (config.getBody() != null)
      requestBuilder.setEntity(new StringEntity(config.getBody()));
    for (HttpClientPair_V1 entry : config.getHeaders()) {
      requestBuilder.addHeader(entry.getName(), entry.getValue());
    }
    return requestBuilder
        .setUri(config.getUri())
        .addParameters(parameters)
        .build();

  }

  private void validateResponse(CloseableHttpResponse response) {
    int code = response.getStatusLine().getStatusCode();
    if (code < 200 || code > 206)
      throw new RuntimeException("server returned non 2xx status code:" + code);
  }

}

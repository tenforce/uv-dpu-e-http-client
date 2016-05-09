package com.tenforce.uv.extractor.httpClient;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;
import eu.unifiedviews.dpu.config.DPUConfigException;
import eu.unifiedviews.helpers.dpu.vaadin.dialog.AbstractDialog;

import java.util.ArrayList;
import java.util.UUID;


/**
 * Vaadin configuration dialog for httpClient.
 *
 * @author Tenforce
 */
public class httpClientVaadinDialog extends AbstractDialog<httpClientConfig_V1> {
  private final VerticalLayout mainLayout = new VerticalLayout();
  private boolean showHeaders = false;
  private VerticalLayout headersLayout = new VerticalLayout();
  private TextField uriField;
  private ComboBox methodMenu;
  private Table headersTable = new Table();
  private TextArea bodyTextArea;
  BeanContainer<String, HttpClientPair_V1> headersContainer = new BeanContainer<>(HttpClientPair_V1.class);

  public httpClientVaadinDialog() {
    super(httpClient.class);
    headersContainer.setBeanIdProperty("name");
    headersContainer.setBeanIdResolver(new ClientPairIdResolver());
  }

  @Override
  public void setConfiguration(httpClientConfig_V1 configuration) throws DPUConfigException {
    uriField.setValue(configuration.getUri());
    methodMenu.setValue(configuration.getMethod());
    bodyTextArea.setValue(configuration.getBody());
    headersContainer.addAll(configuration.getHeaders());
  }

  @Override
  public httpClientConfig_V1 getConfiguration() throws DPUConfigException {
    httpClientConfig_V1 configuration = new httpClientConfig_V1();
    configuration.setUri(uriField.getValue());
    configuration.setBody(bodyTextArea.getValue());
    configuration.setMethod((String) methodMenu.getValue());
    configuration.setHeaders(pairsInContainer(headersContainer));
    return configuration;
  }


  @Override
  public void buildDialogLayout() {
    mainLayout.setWidth("100%");
    mainLayout.setHeight("-1px");
    mainLayout.setMargin(true);
    mainLayout.addComponent(new Label(ctx.tr("httpClient.dialog.label")));
    mainLayout.addComponent(buildTopBar());
    buildHeadersTable();
    mainLayout.addComponent(headersLayout);
    bodyTextArea = new TextArea("Body:");
    bodyTextArea.setWidth("800px");
    bodyTextArea.setHeight("300px");
    mainLayout.addComponent(bodyTextArea);
    setCompositionRoot(mainLayout);
  }

  private void buildHeadersTable() {
    headersTable.setCaption("Headers");
    headersTable.addGeneratedColumn("", new RemoveButtonGenerator());
    headersTable.setContainerDataSource(headersContainer);
    headersTable.setWidth("800px");
    headersTable.setHeight("150px");
    headersTable.setEditable(true);
    headersTable.setImmediate(true);
    Button addRow = new Button("Add header");
    addRow.addClickListener(new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {
        headersContainer.addItem(UUID.randomUUID().toString(),new HttpClientPair_V1());
      }
    });
    headersLayout.setVisible(showHeaders);
    headersLayout.addComponents(headersTable, addRow);
  }

  private Component buildTopBar() {
    uriField = new TextField();
    uriField.addValidator(new StringLengthValidator(
        "URI Field cannot be empty!",
        null, null, true));
    uriField.setWidth(500, Unit.PIXELS);

    methodMenu = new ComboBox();
    methodMenu.setTextInputAllowed(false);
    methodMenu.setNullSelectionAllowed(false);
    methodMenu.addItem("GET");
    methodMenu.addItem("POST");
    methodMenu.addItem("PUT");
    methodMenu.addItem("PATCH");
    methodMenu.addItem("DELETE");
    methodMenu.setValue("GET");
    methodMenu.setWidth("150px");

    Button headersToggle = new Button("HEADERS");
    headersToggle.setWidth("150px");
    headersToggle.addClickListener(new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {
        showHeaders = !showHeaders;
        headersLayout.setVisible(showHeaders);
      }
    });

    HorizontalLayout topBar = new HorizontalLayout();
    topBar.addComponent(methodMenu);
    topBar.addComponent(uriField);
    topBar.addComponent(headersToggle);
    return topBar;
  }

  private ArrayList<HttpClientPair_V1> pairsInContainer(BeanContainer<String, HttpClientPair_V1> container) {
    ArrayList<HttpClientPair_V1> list = new ArrayList<>(container.size());
    for (Object itemId : container.getItemIds())
      list.add(container.getItem(itemId).getBean());
    return list;
  }
}

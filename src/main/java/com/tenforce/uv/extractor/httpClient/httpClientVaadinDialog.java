package com.tenforce.uv.extractor.httpClient;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;
import eu.unifiedviews.dpu.config.DPUConfigException;
import eu.unifiedviews.helpers.dpu.vaadin.dialog.AbstractDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * Vaadin configuration dialog for httpClient.
 *
 * @author Tenforce
 */
public class httpClientVaadinDialog extends AbstractDialog<httpClientConfig_V1> {

    private TextField uriField;
    private ComboBox methodMenu;
    private TextArea bodyTextArea;
    private List<HttpClientPair_V1> headersList = new ArrayList<>();
    private List<HttpClientPair_V1> paramsList = new ArrayList<>();


    public httpClientVaadinDialog() {
        super(httpClient.class);
    }

    @Override
    public void setConfiguration(httpClientConfig_V1 configuration) throws DPUConfigException {
        uriField.setValue(configuration.getUri());
        methodMenu.setValue(configuration.getMethod());
        bodyTextArea.setValue(configuration.getBody());
        headersList = configuration.getHeaders();
        paramsList = configuration.getParams();
    }

    @Override
    public httpClientConfig_V1 getConfiguration() throws DPUConfigException {
        httpClientConfig_V1 configuration = new httpClientConfig_V1();
        configuration.setUri(uriField.getValue());
        configuration.setBody(bodyTextArea.getValue());
        configuration.setMethod((String) methodMenu.getValue());
        configuration.setHeaders(headersList);
        configuration.setParams(paramsList);
        return configuration;
    }

    @Override
    public void buildDialogLayout() {
        final VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setWidth("100%");
        mainLayout.setHeight("-1px");
        mainLayout.setMargin(true);

        mainLayout.addComponent(new Label(ctx.tr("httpClient.dialog.label")));


        VerticalLayout upperLayout = new VerticalLayout();
        uriField = new TextField();
        uriField.addValidator(new StringLengthValidator(
                "URI Field cannot be empty!",
                null, null, true));
        uriField.setWidth(500, Unit.PIXELS);
        Label uriLabel = new Label("URI:");
        Label methodLabel = new Label("Method:");
        methodMenu = new ComboBox();
        methodMenu.setTextInputAllowed(false);
        methodMenu.setNullSelectionAllowed(false);
        methodMenu.addItem("GET");
        methodMenu.addItem("POST");
        methodMenu.setValue("GET");
        bodyTextArea = new TextArea("Body:");
        bodyTextArea.setWidth(600, Unit.PIXELS);

        upperLayout.addComponent(uriLabel);
        upperLayout.addComponent(uriField);
        upperLayout.addComponent(methodLabel);
        upperLayout.addComponent(methodMenu);
        upperLayout.addComponent(bodyTextArea);

        mainLayout.addComponent(upperLayout);
        mainLayout.addComponent(buildParamsLayout("header", headersList));
        mainLayout.addComponent(buildParamsLayout("param", paramsList));
        setCompositionRoot(mainLayout);
    }

    private VerticalLayout buildParamsLayout(final String keyName, final List<HttpClientPair_V1> params) {

        // Building headers for a table
        final VerticalLayout mainLayout = new VerticalLayout();
        final TextField keyText = new TextField();
        final TextField valueText = new TextField();
        keyText.setWidth(300, Unit.PIXELS);
        valueText.setWidth(300, Unit.PIXELS);
        // Add button setup
        Button addButton = new Button("Add " + keyName);
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                {
                    {
                        final HorizontalLayout values = new HorizontalLayout();
                        final String keyTextValue = keyText.getValue();
                        String valueTextValue = valueText.getValue();
                        if (keyTextValue == null || valueTextValue == null
                                || keyTextValue.isEmpty() || valueTextValue.isEmpty()) {
                            Notification.show("Empty field!");
                            return;
                        }
                        if (!addParam(keyTextValue, valueTextValue, params)) {
                            Notification.show("Duplicate or null" + keyName + "!");
                            return;
                        }
                        Label key = new Label(keyTextValue);
                        Label value = new Label(valueTextValue);
                        key.setWidth(300, Unit.PIXELS);
                        value.setWidth(300, Unit.PIXELS);
                        keyText.setValue("");
                        valueText.setValue("");

                        values.addComponent(key);
                        values.addComponent(value);
                        Button removeButton = new Button("Remove " + keyName);
                        removeButton.addClickListener(new Button.ClickListener() {
                            @Override
                            public void buttonClick(Button.ClickEvent clickEvent) {
                                mainLayout.removeComponent(values);
                                params.remove(keyTextValue);
                            }
                        });
                        values.addComponent(removeButton);

                        mainLayout.addComponent(values);
                    }
                }
            }
        });
        HorizontalLayout addLayout = new HorizontalLayout();

        addLayout.addComponent(keyText);
        addLayout.addComponent(valueText);
        addLayout.addComponent(addButton);
        VerticalLayout paramsLayout = new VerticalLayout();
        paramsLayout.addComponent(mainLayout);
        paramsLayout.addComponent(addLayout);

        return paramsLayout;
    }

    private boolean addParam(String header, String value, List<HttpClientPair_V1> params) {
        for (HttpClientPair_V1 pair : params) {
            if (pair.getName().equals(header)) {
                return false;
            }
        }

        if (header == null || header.isEmpty()) {
            return false;
        }
        HttpClientPair_V1 pair = new HttpClientPair_V1();
        pair.setName(header);
        pair.setValue(value);
        params.add(pair);
            return true;
    }
}

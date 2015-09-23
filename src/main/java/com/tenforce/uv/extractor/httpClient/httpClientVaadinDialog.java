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
    private VerticalLayout headersLayout = new VerticalLayout();
    private VerticalLayout paramsLayout = new VerticalLayout();

    public httpClientVaadinDialog() {
        super(httpClient.class);
    }

    @Override
    public void setConfiguration(httpClientConfig_V1 configuration) throws DPUConfigException {
        uriField.setValue(configuration.getUri());
        methodMenu.setValue(configuration.getMethod());
        bodyTextArea.setValue(configuration.getBody());

        headersLayout.removeAllComponents();
        paramsLayout.removeAllComponents();

        headersList.clear();
        paramsList.clear();

        for (HttpClientPair_V1 pair : configuration.getHeaders()) {
            generateRow(pair.getName(), pair.getValue(), "header", headersLayout, headersList);
        }
        for (HttpClientPair_V1 pair : configuration.getParams()) {
            generateRow(pair.getName(), pair.getValue(), "param", paramsLayout, paramsList);
        }
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
        methodMenu.addItem("PUT");
        methodMenu.addItem("DELETE");
        methodMenu.setValue("GET");
        bodyTextArea = new TextArea("Body:");
        bodyTextArea.setWidth(600, Unit.PIXELS);

        upperLayout.addComponent(uriLabel);
        upperLayout.addComponent(uriField);
        upperLayout.addComponent(methodLabel);
        upperLayout.addComponent(methodMenu);
        TabSheet tabsheet = new TabSheet();
        upperLayout.addComponent(tabsheet);
        tabsheet.addTab(buildParamsLayout("header", headersList, headersLayout), "Headers");
        tabsheet.addTab(buildParamsLayout("param", paramsList, paramsLayout), "Parameters");
        tabsheet.addTab(bodyTextArea, "Body");
        mainLayout.addComponent(upperLayout);
        setCompositionRoot(mainLayout);
    }

    private VerticalLayout buildParamsLayout(final String keyName, final List<HttpClientPair_V1> params, final VerticalLayout layout) {

        final TextField keyText = new TextField();
        final TextField valueText = new TextField();
        keyText.setWidth(300, Unit.PIXELS);
        valueText.setWidth(300, Unit.PIXELS);

        Button addButton = new Button("Add " + keyName);
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (!validateParams(keyText.getValue(), valueText.getValue(), params)) {
                    return;
                }
                generateRow(keyText.getValue(), valueText.getValue(), keyName, layout, params);
                keyText.setValue("");
                valueText.setValue("");
            }
        });
        HorizontalLayout addLayout = new HorizontalLayout();

        addLayout.addComponent(keyText);
        addLayout.addComponent(valueText);
        addLayout.addComponent(addButton);
        VerticalLayout paramsLayout = new VerticalLayout();
        paramsLayout.addComponent(layout);
        paramsLayout.addComponent(addLayout);

        return paramsLayout;
    }

    private boolean validateParams(String key, String value, List<HttpClientPair_V1> params) {
        if (key == null || value == null
                || key.trim().isEmpty() || value.trim().isEmpty()) {
            Notification.show("Empty field!");
            return false;
        }
        for (HttpClientPair_V1 pair : params) {
            if (pair.getName().equals(key)) {
                Notification.show("Duplicate value!");
                return false;
            }
        }
        return true;
    }

    private void generateRow(final String keyText, final String valueText, String keyName, final VerticalLayout layout, final List<HttpClientPair_V1> params) {
        final HorizontalLayout values = new HorizontalLayout();

        Label key = new Label(keyText);
        Label value = new Label(valueText);
        key.setWidth(300, Unit.PIXELS);
        value.setWidth(300, Unit.PIXELS);

        values.addComponent(key);
        values.addComponent(value);
        Button removeButton = new Button("X");
        params.add(new HttpClientPair_V1(keyText, valueText));
        removeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                layout.removeComponent(values);
                params.remove(new HttpClientPair_V1(keyText, valueText));
            }
        });

        values.addComponent(removeButton);
        layout.addComponent(values);
    }
}

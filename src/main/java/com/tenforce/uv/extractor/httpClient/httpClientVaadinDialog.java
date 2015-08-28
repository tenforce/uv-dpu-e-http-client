package com.tenforce.uv.extractor.httpClient;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;
import eu.unifiedviews.dpu.config.DPUConfigException;
import eu.unifiedviews.helpers.dpu.vaadin.dialog.AbstractDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Vaadin configuration dialog for httpClient.
 *
 * @author Tenforce
 */
public class httpClientVaadinDialog extends AbstractDialog<httpClientConfig_V1> {

    private httpClientConfig_V1 configuration = new httpClientConfig_V1();

    public httpClientVaadinDialog() {
        super(httpClient.class);
    }

    @Override
    public void setConfiguration(httpClientConfig_V1 configuration) throws DPUConfigException {
        this.configuration = configuration;
    }

    @Override
    public httpClientConfig_V1 getConfiguration() throws DPUConfigException {
        configuration.setUri("http://google.com");
        configuration.setBody("body");
        configuration.setMethod("get");
        configuration.setHeaders(new ArrayList<HttpClientPair_V1>());
        configuration.setParams(new ArrayList<HttpClientPair_V1>());
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
        final TextField uriField = new TextField();
        uriField.addValidator(new StringLengthValidator(
                "URI Field cannot be empty!",
                null, null, true));
        uriField.setWidth(500, Unit.PIXELS);
        Label uriLabel = new Label("URI:");
        Label methodLabel = new Label("Method:");
        final ComboBox methodMenu = new ComboBox();
        methodMenu.setTextInputAllowed(false);
        methodMenu.setNullSelectionAllowed(false);
        methodMenu.addItem("GET");
        methodMenu.addItem("POST");
        methodMenu.setValue("GET");
        final TextArea bodyTextArea = new TextArea("Body:");
        bodyTextArea.setWidth(600, Unit.PIXELS);

        upperLayout.addComponent(uriLabel);
        upperLayout.addComponent(uriField);
        upperLayout.addComponent(methodLabel);
        upperLayout.addComponent(methodMenu);
        upperLayout.addComponent(bodyTextArea);

        mainLayout.addComponent(upperLayout);
        mainLayout.addComponent(buildParamsLayout("header", new ArrayList<HttpClientPair_V1>()));
        mainLayout.addComponent(buildParamsLayout("param", new ArrayList<HttpClientPair_V1>()));
        setCompositionRoot(mainLayout);
        configuration.setUri(uriField.getValue());
        configuration.setBody(bodyTextArea.getValue());

        configuration.setMethod((String) methodMenu.getValue());
        configuration.setHeaders(new ArrayList<HttpClientPair_V1>());
        configuration.setParams(new ArrayList<HttpClientPair_V1>());
    }

    private VerticalLayout buildParamsLayout(final String keyName, final List<HttpClientPair_V1> paramMap) {

        // Building headers for a table
        final VerticalLayout mainLayout = new VerticalLayout();
//        final Label headerHeader = new Label(keyName + ":");
//        final Label valueHeader = new Label("value:");
//        headerHeader.setWidth(300, Unit.PIXELS);
//        valueHeader.setWidth(300, Unit.PIXELS);
//        mainLayout.addComponent(new HorizontalLayout() {
//            {
//                addComponent(headerHeader);
//                addComponent(valueHeader);
//            }
//        });

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
//                        if (!addParam(keyTextValue, valueTextValue, paramMap)) {
//                            Notification.show("Duplicate " + keyName + "!");
//                            return;
//                        }
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
                                paramMap.remove(keyTextValue);
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

    private boolean addParam(String header, String value, Map<String, String> paramMap) {
        if (paramMap.containsKey(header)) {
            return false;
        } else {
            paramMap.put(header, value);
            return true;
        }
    }
}

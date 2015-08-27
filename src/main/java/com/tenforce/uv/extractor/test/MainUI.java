package com.tenforce.uv.extractor.test;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MainUI extends UI {

    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> params = new HashMap<>();
    private UVHttpClient client = new UVHttpClient();


    @Override
    protected void init(final VaadinRequest request) {
        VerticalLayout view = new VerticalLayout();
        view.setMargin(true);

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
        methodMenu.addItem("get");
        methodMenu.addItem("post");
        methodMenu.setValue("get");
        final TextArea bodyTextArea = new TextArea("Body:");
        bodyTextArea.setWidth(350, Unit.PIXELS);

        upperLayout.addComponent(uriLabel);
        upperLayout.addComponent(uriField);
        upperLayout.addComponent(methodLabel);
        upperLayout.addComponent(methodMenu);
        upperLayout.addComponent(bodyTextArea);

        final Label result = new Label();
        result.setWidth(700, Unit.PIXELS);
        result.setReadOnly(true);

        Button checkButton = new Button("Check");
        checkButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                System.out.println("========================URI: " + uriField.getValue() + " Selection: " + methodMenu.getValue() + "Body: " + bodyTextArea + "\n" + headers + "\n" + params);
                try {
                    CloseableHttpResponse response = client.sendRequest(uriField.getValue(), (String) methodMenu.getValue(), headers, params, bodyTextArea.getValue());
                    System.out.println(response.getStatusLine().getStatusCode());
                    System.out.println(response);
                    System.out.println();
                    result.setValue("Response: " + response.getStatusLine().getStatusCode() + "\nResponse entity:\n:" + response.getEntity() + "\nType" + response.getEntity().getContentType());
                    headers.clear();
                    params.clear();
                    response.close();

                } catch (IOException e) {
                    throw new RuntimeException("IO Error occured: " + e.getMessage());
                }
            }
        });

        view.addComponent(upperLayout);
        view.addComponent(buildParamsLayout("header", headers));
        view.addComponent(buildParamsLayout("param", params));
        view.addComponent(checkButton);
        view.addComponent(result);
        setContent(view);
    }

    private VerticalLayout buildParamsLayout(final String keyName, final Map<String, String> paramMap) {


        // Building headers for a table
        final VerticalLayout mainLayout = new VerticalLayout();
        final TextField headerHeader = new TextField(keyName + ":");
        headerHeader.setReadOnly(true);
        final TextField valueHeader = new TextField("value:");
        valueHeader.setReadOnly(true);
        mainLayout.addComponent(new HorizontalLayout() {
            {
                addComponent(headerHeader);
                addComponent(valueHeader);
            }
        });

        final TextField keyText = new TextField();
        final TextField valueText = new TextField();
        keyText.setWidth(350, Unit.PIXELS);
        valueText.setWidth(350, Unit.PIXELS);
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
                        if (!addParam(keyTextValue, valueTextValue, paramMap)) {
                            Notification.show("Duplicate " + keyName + "!");
                            return;
                        }
                        TextField key = new TextField(keyTextValue);
                        TextField value = new TextField(valueTextValue);
                        key.setWidth(350, Unit.PIXELS);
                        value.setWidth(350, Unit.PIXELS);
                        key.setReadOnly(true);
                        value.setReadOnly(true);

                        values.addComponent(key);
                        values.addComponent(value);
                        Button removeButton = new Button("X");
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
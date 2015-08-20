package com.tenforce.uv.extractor.httpClient;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import eu.unifiedviews.dpu.config.DPUConfigException;
import eu.unifiedviews.helpers.dpu.vaadin.dialog.AbstractDialog;

/**
 * Vaadin configuration dialog for httpClient.
 *
 * @author Tenforce
 */
public class httpClientVaadinDialog extends AbstractDialog<httpClientConfig_V1> {

    public httpClientVaadinDialog() {
        super(httpClient.class);
    }

    @Override
    public void setConfiguration(httpClientConfig_V1 c) throws DPUConfigException {

    }

    @Override
    public httpClientConfig_V1 getConfiguration() throws DPUConfigException {
        final httpClientConfig_V1 c = new httpClientConfig_V1();

        return c;
    }

    @Override
    public void buildDialogLayout() {
        final VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setWidth("100%");
        mainLayout.setHeight("-1px");
        mainLayout.setMargin(true);

        mainLayout.addComponent(new Label(ctx.tr("httpClient.dialog.label")));

        setCompositionRoot(mainLayout);
    }
}

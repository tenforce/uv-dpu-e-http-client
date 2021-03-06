package com.tenforce.uv.extractor.httpClient;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

public class RemoveButtonGenerator implements Table.ColumnGenerator {
  Button headersToggle;
  public RemoveButtonGenerator(Button toggle) {
    headersToggle = toggle;
  }

  @Override
  public Component generateCell(final Table source, final Object itemId, Object columnId) {
    Button remove = new Button("x");
    remove.addClickListener(new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {
        source.removeItem(itemId);
        headersToggle.setCaption("HEADERS (" + source.size() + ")");
      }
    });
    return remove;
  }
}

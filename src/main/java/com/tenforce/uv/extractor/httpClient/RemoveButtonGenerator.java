package com.tenforce.uv.extractor.httpClient;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

public class RemoveButtonGenerator implements Table.ColumnGenerator {
  @Override
  public Component generateCell(final Table source, final Object itemId, Object columnId) {
    Button remove = new Button("x");
    remove.addClickListener(new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {
        source.removeItem(itemId);
      }
    });
    return remove;
  }
}

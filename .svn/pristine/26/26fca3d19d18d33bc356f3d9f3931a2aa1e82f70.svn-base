package com.fundamental.view;

import com.vaadin.data.Item;

/**
 * @author Henry Barrientos
 */
public class MntBase {

    private boolean filterByProperty(final String prop, final Item item, final String text) {
        if (item == null || item.getItemProperty(prop) == null || item.getItemProperty(prop).getValue() == null) {
            return false;
        }
        String val = item.getItemProperty(prop).getValue().toString().trim().toLowerCase();
        if (val.contains(text.toLowerCase().trim())) {
            return true;
        }
        return false;
    }

}

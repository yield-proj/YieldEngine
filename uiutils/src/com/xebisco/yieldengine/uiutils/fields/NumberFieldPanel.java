package com.xebisco.yieldengine.uiutils.fields;

import com.xebisco.yieldengine.uiutils.NumberField;

public class NumberFieldPanel<T extends Number> extends StringFieldPanel {
    public NumberFieldPanel(String name, T value, boolean allowNegatives, boolean editable) {
        super(name, new NumberField(value.getClass(), allowNegatives), editable);
    }
}

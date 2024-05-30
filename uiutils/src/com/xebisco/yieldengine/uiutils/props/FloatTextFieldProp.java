package com.xebisco.yieldengine.uiutils.props;

import javax.swing.text.PlainDocument;

public class FloatTextFieldProp extends TextFieldProp {
    public FloatTextFieldProp(String name, float value, boolean prettyString) {
        super(name, String.valueOf(value), prettyString);
        ((PlainDocument) field().getDocument()).setDocumentFilter(new FloatFilter());
    }
}

package com.xebisco.yieldengine.uiutils.props;

import javax.swing.text.PlainDocument;

public class LongTextFieldProp extends TextFieldProp {
    public LongTextFieldProp(String name, long value, boolean prettyString) {
        super(name, String.valueOf(value), prettyString);
        ((PlainDocument) field().getDocument()).setDocumentFilter(new LongFilter());
    }
}

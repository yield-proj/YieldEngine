package com.xebisco.yieldengine.uiutils.props;

import javax.swing.text.PlainDocument;

public class IntTextFieldProp extends TextFieldProp {
    public IntTextFieldProp(String name, int value, boolean prettyString) {
        super(name, String.valueOf(value), prettyString);
        ((PlainDocument) field().getDocument()).setDocumentFilter(new IntFilter());
    }
}

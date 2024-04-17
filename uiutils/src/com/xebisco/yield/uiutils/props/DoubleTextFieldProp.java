package com.xebisco.yield.uiutils.props;

import javax.swing.text.PlainDocument;

public class DoubleTextFieldProp extends TextFieldProp {
    public DoubleTextFieldProp(String name, double value, boolean prettyString) {
        super(name, String.valueOf(value), prettyString);
        ((PlainDocument) field().getDocument()).setDocumentFilter(new DoubleFilter());
    }
}

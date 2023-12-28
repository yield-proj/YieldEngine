package com.xebisco.yield.editor.app.props;

import javax.swing.text.PlainDocument;

public class DoubleTextFieldProp extends TextFieldProp {
    public DoubleTextFieldProp(String name, double value) {
        super(name, String.valueOf(value));
        ((PlainDocument) field().getDocument()).setDocumentFilter(new DoubleFilter());
    }
}

package com.xebisco.yield.uiutils.props;

import javax.swing.text.PlainDocument;

public class IntTextFieldProp extends TextFieldProp {
    public IntTextFieldProp(String name, int value) {
        super(name, String.valueOf(value));
        ((PlainDocument) field().getDocument()).setDocumentFilter(new IntFilter());
    }
}

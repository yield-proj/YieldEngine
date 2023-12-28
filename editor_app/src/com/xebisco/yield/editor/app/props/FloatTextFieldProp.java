package com.xebisco.yield.editor.app.props;

import javax.swing.text.PlainDocument;

public class FloatTextFieldProp extends TextFieldProp {
    public FloatTextFieldProp(String name, float value) {
        super(name, String.valueOf(value));
        ((PlainDocument) field().getDocument()).setDocumentFilter(new FloatFilter());
    }
}

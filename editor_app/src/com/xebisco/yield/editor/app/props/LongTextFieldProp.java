package com.xebisco.yield.editor.app.props;

import javax.swing.text.PlainDocument;

public class LongTextFieldProp extends TextFieldProp {
    public LongTextFieldProp(String name, long value) {
        super(name, String.valueOf(value));
        ((PlainDocument) field().getDocument()).setDocumentFilter(new LongFilter());
    }
}

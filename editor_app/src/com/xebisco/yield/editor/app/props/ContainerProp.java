package com.xebisco.yield.editor.app.props;

import javax.swing.*;

public abstract class ContainerProp extends Prop {
    public ContainerProp() {
        super(null, null);
    }

    public abstract JPanel render();
}

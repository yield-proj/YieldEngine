package com.xebisco.yieldengine.uiutils.props;

import javax.swing.*;

public abstract class ContainerProp extends Prop {
    public ContainerProp() {
        super(null, null);
    }

    public abstract JPanel render();
}

package com.xebisco.yieldengine.core;

import com.xebisco.yieldengine.utils.ComboString;
import com.xebisco.yieldengine.utils.Editable;
import com.xebisco.yieldengine.utils.Visible;

import java.io.Serializable;

public class EntityHeader implements Serializable {
    @Visible
    @Editable
    private String name;
    @Visible
    @Editable
    @ComboString(type = Global.class, method = "getTags")
    private String[] tags = new String[] {
            Global.getTags()[0]
    };
    @Visible
    @Editable
    @ComboString(type = Global.class, method = "getLayers")
    private String layer = Global.getLayers()[0];
    @Visible
    @Editable
    private boolean enabled = true;

    public EntityHeader(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public EntityHeader setName(String name) {
        this.name = name;
        return this;
    }

    public String getLayer() {
        return layer;
    }

    public EntityHeader setLayer(String layer) {
        this.layer = layer;
        return this;
    }

    public String[] getTags() {
        return tags;
    }

    public EntityHeader setTags(String[] tags) {
        this.tags = tags;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public EntityHeader setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
}

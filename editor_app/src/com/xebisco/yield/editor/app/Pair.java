package com.xebisco.yield.editor.app;

import java.io.Serializable;

public record Pair<F, S>(F first, S second) implements Serializable {
}

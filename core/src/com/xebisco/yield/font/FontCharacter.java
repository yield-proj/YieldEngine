package com.xebisco.yield.font;

import com.xebisco.yield.Vector2D;
import com.xebisco.yield.texture.Texture;

public record FontCharacter(Texture texture, Vector2D bearing, int advance) {

}
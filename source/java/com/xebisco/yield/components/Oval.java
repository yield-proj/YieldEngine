package com.xebisco.yield.components;

import com.xebisco.yield.Obj;

public class Oval extends Rectangle {
    @Override
    public void process(Obj obj) {
        super.process(obj);
        obj.type = Obj.ShapeType.OVAL;
    }
}

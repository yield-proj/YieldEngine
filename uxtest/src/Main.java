/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.xebisco.yield.*;

public class Main extends YldGame {

    Entity text;

    @Override
    public void create() {
        /*instantiate(e -> {
            e.addComponent(new Rectangle(new Vector2(100, 100)));
            e.addComponent(new YldScript() {
                @Override
                public void update(float delta) {
                    transform.translate(new Vector2(input.getAxis("Horizontal"), input.getAxis("Vertical")).mul(delta * 100f));
                }
            });
            e.center();
        });*/
        graphics.rect(100, 100);
        graphics.setColor(Colors.RED);
        text = graphics.text("Hello, World!");
        text.center();


        //text = graphics.text("AAA").getComponent(Text.class);
        /*instantiate(e -> {
            UXMain main = new UXMain(new Vector2(getView().getWidth(), getView().getHeight()), new UXPalette());
            UXPanel panel = main.getMainPanel();
            UXButton button = panel.add(UXButton.class, false, "Ok", new Vector2(-50, -50), new Vector2(150, 70));
            UXButton button2 = panel.add(UXButton.class, false, "Cancel", new Vector2(-50 - 180, -50), new Vector2(150, 70));
            panel.add(UXTitleText.class, false, "Welcome!", new Vector2(80, 80));
            panel.add(UXText.class, false, "This is YieldUX.", new Vector2(90, 80 + 140));
            panel.add(UXSlider.class, true, 200f, new Vector2(0, 0));
            button.setAction(() -> Yld.log("Ok"));
            button2.setAction(() -> Yld.log("Cancel"));
            //panel.getComponents().forEach(s -> {if(s instanceof UXRect) ((UXRect) s).setBackground(main.getPalette().foreground2);});
            e.addComponent(main);
        });*/
    }

    @Override
    public void update(float delta) {
        if(input.isPressed(Key.W)) {
            text.setIndex(1);}
        if(input.isPressed(Key.S))
            text.setIndex(-1);
       /* if (text != null) {
            text.getEntity().center();
            text.setContents(String.valueOf(getFrames()));
        }
        if (getFrames() == 300)
            setScene(Main.class);*/
    }

    public static void main(String[] args) {
        launch(new Main());
    }
}
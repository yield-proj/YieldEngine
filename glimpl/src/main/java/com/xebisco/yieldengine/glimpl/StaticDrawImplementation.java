/*
 * Copyright [2022-2024] [Xebisco]
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yieldengine.glimpl;

import com.xebisco.yieldengine.core.Transform;
import com.xebisco.yieldengine.core.camera.ICamera;
import com.xebisco.yieldengine.core.io.texture.Texture;
import com.xebisco.yieldengine.core.render.DrawInstruction;
import com.xebisco.yieldengine.glimpl.mem.OGLArrayMemory;
import com.xebisco.yieldengine.glimpl.mem.OGLTextureIDGetter;
import com.xebisco.yieldengine.glimpl.shader.ShaderCreationException;
import com.xebisco.yieldengine.glimpl.shader.ShaderLinkException;
import com.xebisco.yieldengine.glimpl.shader.ShaderProgram;
import com.xebisco.yieldengine.glimpl.shader.abstractions.ArrayContext;
import com.xebisco.yieldengine.glimpl.shader.abstractions.IArrayMemory;
import org.joml.*;

import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public final class StaticDrawImplementation {

    private static ShaderProgram rectangleShader, imageShader;
    private static IArrayMemory arrayMemory;
    private static ArrayContext rectangleArrayContext, imageArrayContext, circleArrayContext;

    public static void init() throws ShaderCreationException, ShaderLinkException {
        loadShaders();
        loadVertexArrays();
    }

    private static void loadShaders() throws ShaderCreationException, ShaderLinkException {
        rectangleShader = ShaderProgram.create(
                "#version 330\n" +
                        "in vec2 position;\n" +
                        "uniform mat4 transformationMatrix;\n" +
                        "uniform mat4 viewMatrix;\n" +
                        "void main() {\n" +
                        "gl_Position = viewMatrix * transformationMatrix * vec4(position, 0.0, 1.0);\n" +
                        "}",
                "#version 330\n" +
                        "uniform vec4 color;\n" +
                        "out vec4 fragColor;\n" +
                        "void main() {\n" +
                        "fragColor = color;\n" +
                        "}"
        );
        rectangleShader.link();
        imageShader = ShaderProgram.create(
                "#version 330\n" +
                        "in vec2 position;\n" +
                        "in vec2 texCoord;\n" +
                        "out vec2 fragTexCoord;\n" +
                        "uniform mat4 transformationMatrix;\n" +
                        "uniform mat4 viewMatrix;\n" +
                        "uniform float offsetX;\n" +
                        "uniform float offsetY;\n" +
                        "void main() {\n" +
                        "gl_Position = viewMatrix * transformationMatrix * (vec4(position, 0.0, 1.0) + vec4(offsetX, offsetY, 0.0, 0.0));\n" +
                        "fragTexCoord = texCoord;\n" +
                        "}",
                "#version 330\n" +
                        "uniform vec4 color;\n" +
                        "uniform sampler2D texture_sampler;\n" +
                        "out vec4 fragColor;\n" +
                        "in vec2 fragTexCoord;\n" +
                        "void main() {\n" +
                        "fragColor = texture(texture_sampler, fragTexCoord) * color;\n" +
                        "}"
        );
        imageShader.link();
    }

    private static void loadVertexArrays() {
        arrayMemory = new OGLArrayMemory();

        rectangleArrayContext = arrayMemory.createArrayContext(new int[]{0, 1, 3, 3, 1, 2});
        arrayMemory.createVertexArray(new Vector2f[]{
                new Vector2f(-.5f, .5f),
                new Vector2f(-.5f, -.5f),
                new Vector2f(.5f, -.5f),
                new Vector2f(.5f, .5f)
        }, 0, rectangleArrayContext);

        imageArrayContext = arrayMemory.createArrayContext(new int[]{0, 1, 3, 3, 1, 2});
        arrayMemory.createVertexArray(new Vector2f[]{
                new Vector2f(-.5f, .5f),
                new Vector2f(-.5f, -.5f),
                new Vector2f(.5f, -.5f),
                new Vector2f(.5f, .5f)
        }, 0, imageArrayContext);

        arrayMemory.createVertexArray(new Vector2f[]{
                new Vector2f(0, 0),
                new Vector2f(0, 1),
                new Vector2f(1, 1),
                new Vector2f(1, 0)
        }, 1, imageArrayContext);

        Vector2fc[] circleVertices = buildCircle(40);

        circleArrayContext = arrayMemory.createArrayContext(circleVertices.length);

        arrayMemory.createVertexArray(circleVertices, 0, circleArrayContext);
    }

    public static Vector4fc colorValue(int color) {
        float r = ((color >> 16) & 0xff) / 255.0f;
        float g = ((color >> 8) & 0xff) / 255.0f;
        float b = ((color) & 0xff) / 255.0f;
        float a = ((color >> 24) & 0xff) / 255.0f;
        return new Vector4f(r, g, b, a);
    }

    public static void drawInstruction(DrawInstruction instruction) {
        Vector2fc size;
        Vector4fc color;
        switch (instruction.getType()) {
            case "clr":
                Vector4fc backgroundColor = colorValue((Integer) instruction.getDrawObjects()[0]);
                glClearColor(backgroundColor.x(), backgroundColor.y(), backgroundColor.z(), 1);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                break;
            case "dw_rect":
                drawRectangle(instruction.getTransform(), instruction.getCamera(), (Vector2fc) instruction.getDrawObjects()[1], colorValue((Integer) instruction.getDrawObjects()[0]));
                break;
            case "dw_img":
                size = (Vector2fc) instruction.getDrawObjects()[1];
                drawImage(((OGLTextureIDGetter) instruction.getDrawObjects()[2]).getTextureID(), instruction.getTransform(), instruction.getCamera(), size.x(), size.y(), colorValue((Integer) instruction.getDrawObjects()[0]), 0, 0);
                break;
            case "dw_text":
                color = colorValue((Integer) instruction.getDrawObjects()[0]);
                //noinspection unchecked
                HashMap<Character, Texture> font = (HashMap<Character, Texture>) instruction.getDrawObjects()[1];
                String text = (String) instruction.getDrawObjects()[2];

                String[] lines = text.split("\n");

                int height = font.get(' ').getHeight();

                for (int i = 0; i < lines.length; i++) {
                    float x = 0;

                    char[] chars = lines[i].toCharArray();

                    for (char c : chars) {
                        Texture tex = font.get(c);
                        if (tex == null) continue;
                        x -= tex.getWidth() / 2f;
                    }

                    for (char c : chars) {
                        Texture tex = font.get(c);
                        if (tex == null) continue;
                        drawImage(((OGLTextureIDGetter) tex.getImageReference()).getTextureID(), instruction.getTransform(), instruction.getCamera(), tex.getWidth(), tex.getHeight(), color, (x + tex.getWidth() / 2f) / (float) tex.getWidth(), (height / 4f * lines.length - height * i) / (float) tex.getHeight());
                        x += tex.getWidth();
                    }
                }
                break;
            case "dw_ln":
                color = colorValue((Integer) instruction.getDrawObjects()[0]);
                Vector2fc point1 = (Vector2fc) instruction.getDrawObjects()[1], point2 = (Vector2fc) instruction.getDrawObjects()[2];
                Transform t = new Transform(instruction.getTransform());
                float cat1 = point1.x() - point2.x(), cat2 = point1.y() - point2.y();
                t.translate(cat1 / -2f, cat2 / -2f);
                t.rotateZ((float) Math.atan2(cat2, cat1));
                drawRectangle(t, instruction.getCamera(), new Vector2f((float) Math.sqrt(Math.pow(cat1, 2) + Math.pow(cat2, 2)), (float) instruction.getDrawObjects()[3]), color);
                break;
            case "dw_ellipse":
                rectangleShader.bind();

                color = colorValue((Integer) instruction.getDrawObjects()[0]);

                size = (Vector2fc) instruction.getDrawObjects()[1];

                rectangleShader.setUniform("transformationMatrix", instruction.getTransform().getTransformMatrix(new Vector3f(size.x(), size.y(), 1)));
                rectangleShader.setUniform("viewMatrix", instruction.getCamera().getViewMatrix());

                rectangleShader.setUniform("color", color);

                glBindVertexArray((int) circleArrayContext.getContextObject());

                glEnableVertexAttribArray(0);

                glDrawArrays(GL_TRIANGLES, 0, circleArrayContext.getVertexCount());

                glDisableVertexAttribArray(0);

                rectangleShader.unbind();
                break;
            default:
                throw new IllegalDrawInstructionTypeException(instruction.getType());
        }
    }

    private static Vector2fc[] buildCircle(int vCount) {
        float angle = 360f / vCount;

        List<Vector2f> temp = new ArrayList<>();

        for (int i = 0; i < vCount; i++) {
            float currentAngle = (float) Math.toRadians(angle * i);
            float x = (float) (Math.cos(currentAngle)) / 2f;
            float y = (float) (Math.sin(currentAngle)) / 2f;

            temp.add(new Vector2f(x, y));
            temp.add(new Vector2f());

            currentAngle = (float) Math.toRadians(angle * (i + 1));
            x = (float) (Math.cos(currentAngle)) / 2f;
            y = (float) (Math.sin(currentAngle)) / 2f;

            temp.add(new Vector2f(x, y));
        }

        return temp.toArray(new Vector2f[0]);
    }

    private static void drawRectangle(Transform transform, ICamera camera, Vector2fc size, Vector4fc color) {
        rectangleShader.bind();

        rectangleShader.setUniform("transformationMatrix", transform.getTransformMatrix(new Vector3f(size.x(), size.y(), 1)));
        rectangleShader.setUniform("viewMatrix", camera.getViewMatrix());

        rectangleShader.setUniform("color", color);

        glBindVertexArray((int) rectangleArrayContext.getContextObject());

        glEnableVertexAttribArray(0);

        glDrawElements(GL_TRIANGLES, rectangleArrayContext.getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);

        rectangleShader.unbind();
    }

    private static void drawImage(int textureID, Transform transform, ICamera camera, float width, float height, Vector4fc color, float offsetX, float offsetY) {
        imageShader.bind();

        imageShader.setUniform("transformationMatrix", transform.getTransformMatrix(new Vector3f(width, height, 1)));
        imageShader.setUniform("viewMatrix", camera.getViewMatrix());

        imageShader.setUniform("color", color);
        imageShader.setUniform("texture_sampler", 0);
        imageShader.setUniform("offsetX", offsetX);
        imageShader.setUniform("offsetY", offsetY);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureID);

        glBindVertexArray((int) imageArrayContext.getContextObject());

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, imageArrayContext.getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        imageShader.unbind();
    }
}

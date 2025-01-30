package com.xebisco.yieldengine.glimpl;

import com.xebisco.yieldengine.core.Transform;
import com.xebisco.yieldengine.core.camera.ICamera;
import com.xebisco.yieldengine.core.graphics.yldg1.Paint;
import com.xebisco.yieldengine.core.graphics.yldg1.YLDG1;
import com.xebisco.yieldengine.core.io.texture.Texture;
import com.xebisco.yieldengine.glimpl.mem.OGLArrayMemory;
import com.xebisco.yieldengine.glimpl.mem.OGLTextureIDGetter;
import com.xebisco.yieldengine.glimpl.shader.ShaderCreationException;
import com.xebisco.yieldengine.glimpl.shader.ShaderLinkException;
import com.xebisco.yieldengine.glimpl.shader.ShaderProgram;
import com.xebisco.yieldengine.glimpl.shader.abstractions.ArrayContext;
import com.xebisco.yieldengine.glimpl.shader.abstractions.IArrayMemory;
import com.xebisco.yieldengine.utils.Color4f;
import org.joml.*;

import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class YLDG1GLImpl implements YLDG1 {

    @Override
    public void initContext() {
        try {
            loadShaders();
            loadVertexArrays();
        } catch (ShaderCreationException | ShaderLinkException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawRect(float width, float height, Paint paint) {
        drawPolygon(paint.getTransform(), paint.getCamera(), new Vector2f(width, height), paint.getColor(), rectangleArrayContext, 0, 0, true);
    }

    @Override
    public void drawText(String str, Paint paint) {
        //noinspection unchecked
        HashMap<Character, Texture> font = (HashMap<Character, Texture>) paint.getFont().getFontReference();
        String[] lines = str.split("\n");

        int height = font.get(' ').getHeight();

        for (int i = 0; i < lines.length; i++) {
            float x1 = 0;

            char[] chars = lines[i].toCharArray();

            for (char c : chars) {
                Texture tex = font.get(c);
                if (tex == null) continue;
                x1 -= tex.getWidth() / 2f;
            }

            for (char c : chars) {
                Texture tex = font.get(c);
                if (tex == null) continue;
                drawImage(((OGLTextureIDGetter) tex.getImageReference()).getTextureID(), paint.getTransform(), paint.getCamera(), tex.getWidth(), tex.getHeight(), paint.getColor(), (x1 + tex.getWidth() / 2f) / (float) tex.getWidth(), (height / 4f * lines.length * i) / (float) tex.getHeight());
                x1 += tex.getWidth();
            }
        }
    }

    @Override
    public void drawImage(float width, float height, Paint paint) {
        drawImage(((OGLTextureIDGetter) paint.getTexture().getImageReference()).getTextureID(), paint.getTransform(), paint.getCamera(), width, height, paint.getColor(), 0, 0);
    }

    @Override
    public void drawEllipse(float width, float height, Paint paint) {
        drawPolygon(paint.getTransform(), paint.getCamera(), new Vector2f(width, height), paint.getColor(), circleArrayContext, 0, 0, false);
    }

    @Override
    public void drawLine(float x1, float y1, float x2, float y2, Paint paint) {
        Vector2fc point1 = new Vector2f(x1, y1), point2 = new Vector2f(x2, y2);
        Transform t = new Transform(paint.getTransform());
        float cat1 = point1.x() - point2.x(), cat2 = point1.y() - point2.y();
        t.translate(cat1 / -2f + x1, cat2 / -2f + y1);
        float rot = (float) Math.atan2(cat2, cat1);
        if (rot != 0)
            t.rotateZ(rot);
        drawPolygon(t, paint.getCamera(), new Vector2f((float) Math.sqrt(Math.pow(cat1, 2) + Math.pow(cat2, 2)), paint.getStrokeSize()), paint.getColor(), rectangleArrayContext, 0, 0, true);
    }

    @Override
    public float stringWidth(String str, Paint paint) {
        //noinspection unchecked
        HashMap<Character, Texture> font = (HashMap<Character, Texture>) paint.getFont().getFontReference();
        String[] lines = str.split("\n");

        float x = 0;

        for (String line : lines) {
            float x1 = 0;

            char[] chars = line.toCharArray();


            for (char c : chars) {
                Texture tex = font.get(c);
                if (tex == null) continue;
                x1 += tex.getWidth();
            }
            if (x1 > x) x = x1;
        }
        return x;
    }

    @Override
    public float stringHeight(String str, Paint paint) {
        //noinspection unchecked
        HashMap<Character, Texture> font = (HashMap<Character, Texture>) paint.getFont().getFontReference();
        return font.get(' ').getHeight();
    }

    @Override
    public void clearScreen(Paint paint) {
        Color4f backgroundColor = paint.getColor();
        glClearColor(backgroundColor.x(), backgroundColor.y(), backgroundColor.z(), backgroundColor.w());
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    private static ShaderProgram rectangleShader, imageShader;
    private static IArrayMemory arrayMemory;
    private static ArrayContext rectangleArrayContext, imageArrayContext, circleArrayContext;

    private static void loadShaders() throws ShaderCreationException, ShaderLinkException {
        rectangleShader = ShaderProgram.create(
                "#version 330\n" +
                        "in vec2 position;\n" +
                        "uniform mat4 transformationMatrix;\n" +
                        "uniform mat4 viewMatrix;\n" +
                        "uniform float offsetX;\n" +
                        "uniform float offsetY;\n" +
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

    @Override
    public void dispose() {

    }

    public static Vector4fc colorValue(int color) {
        float r = ((color >> 16) & 0xff) / 255.0f;
        float g = ((color >> 8) & 0xff) / 255.0f;
        float b = ((color) & 0xff) / 255.0f;
        float a = ((color >> 24) & 0xff) / 255.0f;
        return new Vector4f(r, g, b, a);
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

    private static void drawPolygon(Transform transform, ICamera camera, Vector2fc size, Vector4fc color, ArrayContext arrayContext, float offsetX, float offsetY, boolean drawElements) {
        rectangleShader.bind();

        rectangleShader.setUniform("transformationMatrix", transform.getTransformMatrix(new Vector3f(size.x(), size.y(), 1)));
        rectangleShader.setUniform("viewMatrix", camera.getViewMatrix());
        imageShader.setUniform("offsetX", offsetX);
        imageShader.setUniform("offsetY", offsetY);

        rectangleShader.setUniform("color", color);

        glLineWidth(4);

        glBindVertexArray((int) arrayContext.getContextObject());

        glEnableVertexAttribArray(0);

        if (drawElements)
            glDrawElements(GL_TRIANGLES, arrayContext.getVertexCount(), GL_UNSIGNED_INT, 0);
        else glDrawArrays(GL_TRIANGLES, 0, arrayContext.getVertexCount());

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

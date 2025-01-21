package com.xebisco.yieldengine.core.io;

import com.xebisco.yieldengine.core.Global;
import com.xebisco.yieldengine.core.IDispose;
import com.xebisco.yieldengine.core.io.audio.Audio;
import com.xebisco.yieldengine.core.io.audio.AudioSource;
import com.xebisco.yieldengine.core.io.audio.IAudioLoader;
import com.xebisco.yieldengine.core.io.audio.IAudioPlayer;
import com.xebisco.yieldengine.core.io.text.Font;
import com.xebisco.yieldengine.core.io.text.IFontLoader;
import com.xebisco.yieldengine.core.io.texture.ITextureLoader;
import com.xebisco.yieldengine.core.io.texture.Texture;
import com.xebisco.yieldengine.core.io.texture.TextureFilter;
import com.xebisco.yieldengine.core.io.texture.TextureMap;
import com.xebisco.yieldengine.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public final class IO implements IDispose {
    private static IO instance;

    private final IAbsolutePathGetter absolutePathGetter;
    private final ITextureLoader textureLoader;
    private final IFontLoader fontLoader;
    private final IAudioLoader audioLoader;
    private final IAudioPlayer audioPlayer;

    private final List<Texture> textures = new ArrayList<>();
    private final List<TextureMap> textureMaps = new ArrayList<>();
    private final List<Font> fonts = new ArrayList<>();
    private final List<Audio> audios = new ArrayList<>();
    private final List<AudioSource> audioSources = new ArrayList<>();

    private final Texture defaultTexture;
    private final Font defaultFont;

    public IO(IAbsolutePathGetter absolutePathGetter, ITextureLoader textureLoader, IFontLoader fontLoader, IAudioLoader audioLoader, IAudioPlayer audioPlayer) {
        this.absolutePathGetter = absolutePathGetter;
        this.textureLoader = textureLoader;
        this.fontLoader = fontLoader;
        this.audioLoader = audioLoader;
        this.audioPlayer = audioPlayer;

        defaultTexture = loadTexture("yieldIcon.png", TextureFilter.LINEAR, false);
        defaultFont = loadFont("OpenSans-Regular.ttf", 40f, true, false);
    }

    public String getAbsolutePath(String path) {
        return absolutePathGetter.getAbsolutePath(path);
    }

    public Texture loadTexture(String path, TextureFilter filter, boolean addToTextureList) {
        Global.debug("Loading texture: " + path);
        Texture texture = textureLoader.loadTexture(absolutePathGetter.getAbsolutePath(path), filter);
        if (addToTextureList)
            textures.add(texture);
        return texture;
    }

    public Texture loadTexture(String path, TextureFilter filter) {
        return loadTexture(path, filter, true);
    }

    public Texture loadTexture(String path) {
        return loadTexture(path, TextureFilter.NEAREST);
    }

    public void unloadTexture(Texture texture) {
        Global.debug("Unloading texture: " + texture);
        textureLoader.unloadTexture(texture.getImageReference());
        textures.remove(texture);
    }

    public void unloadAllTextures() {
        Global.debug("Unloading all textures.");
        while (!textures.isEmpty())
            unloadTexture(textures.get(0));
    }

    public TextureMap loadTextureMap(String path, boolean addToTextureMapList) {
        Global.debug("Loading texture map: " + path);
        TextureMap map = textureLoader.loadTextureMap(absolutePathGetter.getAbsolutePath(path));
        if (addToTextureMapList)
            textureMaps.add(map);
        return map;
    }

    public TextureMap loadTextureMap(String path) {
        return loadTextureMap(path, true);
    }

    public void unloadTextureMap(TextureMap textureMap) {
        Global.debug("Unloading texture map: " + textureMap);
        textureLoader.unloadTextureMap(textureMap.getImageReference());
        textureMaps.remove(textureMap);
    }

    public void unloadAllTextureMaps() {
        Global.debug("Unloading all texture maps.");
        while (!textureMaps.isEmpty())
            unloadTextureMap(textureMaps.get(0));
    }

    public Font loadFont(String path, float size, boolean antiAliasing, boolean addToFontList) {
        Global.debug("Loading font: " + path);
        Font font = fontLoader.loadFont(absolutePathGetter.getAbsolutePath(path), size, antiAliasing);
        if (addToFontList)
            fonts.add(font);
        return font;
    }

    public Font loadFont(String path, float size, boolean antiAliasing) {
        return loadFont(path, size, antiAliasing, true);
    }

    public Font loadFont(String path, float size) {
        return loadFont(path, size, false);
    }

    public void unloadFont(Font font) {
        Global.debug("Unloading font: " + font);
        fonts.remove(font);
    }

    public void unloadAllFonts() {
        Global.debug("Unloading all fonts.");
        while (!fonts.isEmpty())
            unloadFont(fonts.get(0));
    }

    public Audio loadAudio(String path) {
        Global.debug("Loading audio: " + path);
        Audio audio = audioLoader.loadAudio(absolutePathGetter.getAbsolutePath(path));
        audios.add(audio);
        return audio;
    }

    public void unloadAudio(Audio audio) {
        Global.debug("Unloading audio: " + audio);
        audioLoader.unloadAudio(audio.getAudioReference());
        audios.remove(audio);
    }

    public void unloadAllAudios() {
        Global.debug("Unloading all audios.");
        while (!audios.isEmpty())
            unloadAudio(audios.get(0));
    }

    @Override
    public void dispose() {
        unloadAllTextures();
        unloadTexture(defaultTexture);
        unloadAllFonts();
        unloadFont(defaultFont);
        unloadAllAudios();
        unloadAllTextureMaps();
    }

    public static IO getInstance() {
        return instance;
    }

    public static void setInstance(IO instance) {
        IO.instance = instance;
    }

    public Texture getDefaultTexture() {
        return defaultTexture;
    }

    public Font getDefaultFont() {
        return defaultFont;
    }

    public IAbsolutePathGetter getAbsolutePathGetter() {
        return absolutePathGetter;
    }

    public ITextureLoader getTextureLoader() {
        return textureLoader;
    }

    public IFontLoader getFontLoader() {
        return fontLoader;
    }

    public List<Texture> getTextures() {
        return textures;
    }

    public List<Font> getFonts() {
        return fonts;
    }

    public IAudioLoader getAudioLoader() {
        return audioLoader;
    }

    public IAudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public List<Audio> getAudios() {
        return audios;
    }

    public List<AudioSource> getAudioSources() {
        return audioSources;
    }
}
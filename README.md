<img align="right" src="yieldIcon.png" height="200" width="200">

![image](https://img.shields.io/github/languages/top/xebisco/YieldEngine?style=round-square)
![image](https://img.shields.io/github/downloads/xebisco/YieldEngine/total?style=round-square)
![image](https://img.shields.io/github/contributors/xebisco/yieldengine?style=round-square)
  <a href="https://yield-proj.github.io/YieldEngine/index.html" target="_blank">
    <img alt="Documentation" src="https://img.shields.io/badge/documentation-javadoc-brightgreen.svg" />
  </a>
  <a href="http://www.apache.org/licenses/" target="_blank">
    <img alt="License: Apache License 2.0" src="https://img.shields.io/badge/License-Apache License 2.0-yellow.svg" />
  </a>

<h1>Welcome to the official YieldEngine repo 👋</h1>

## 🎮 A 2D cross-platform game engine
A powerful scene-oriented JVM game engine☕, created with the objective of being a simple and efficient engine for creating 2d games in any JVM capable language.
The engine can produce executables capable of running in a number of platforms, including the major desktop platforms (Linux, macOS, Windows), and mobile platforms in the future 😉.

### 🏠 [Website](https://yield.xebisco.com/)

### 🪧 [Hello, World! Demo](https://github.com/yield-proj/YieldEngine/tree/master/hello-world)


## 🪪 Documentation
The Yield Engine documentation is hosted on github websites and is contained in this repository. [Check the docs](https://yield-proj.github.io/YieldEngine/index.html) or [Check the docs folder](https://github.com/yield-proj/YieldEngine/tree/master/docs).

## Core Repository Structure
```
core/
└── src/main/java/com/xebisco/yieldengine/core/
    ├── Scene.java         # Core scene management
    ├── Entity.java        # Base entity class for game objects
    ├── Component.java     # Base component class for entity behaviors
    ├── components/        # Built-in components for common functionality
    │   ├── AudioEmitter.java    # Spatial audio source component
    │   ├── AudioListener.java   # Audio receiver component
    │   ├── Rectangle.java       # 2D rectangle rendering
    │   ├── Sprite.java         # 2D image rendering
    │   └── Text.java           # Text rendering
    ├── graphics/         # Graphics system implementation
    │   └── yldg1/       # Primary graphics implementation
    ├── input/           # Input handling system
    └── io/             # Resource management (textures, audio, fonts)
```

### Quick Start
1. Create a basic game scene:
```java
Scene gameScene = new Scene();
gameScene.setBackgroundColor(new Color4f(0.2f, 0.3f, 0.8f));
Global.setCurrentScene(gameScene);
```

2. Add entities to the scene:
```java
Entity player = new Entity(new EntityHeader("Player"), new Transform());
player.addComponents(new Rectangle().setColor(new Color4f(1, 0, 0)));
gameScene.getEntities().add(player);
```

3. Start the game loop:
```java
LoopContext loop = Global.getOpenGLOpenALLoopContext(800, 600);
gameScene.create();
loop.run();
```

### More Detailed Examples
1. Creating a sprite with texture:
```java
Texture texture = IO.getInstance().loadTexture("player.png");
Entity sprite = new Entity(new EntityHeader("Sprite"), new Transform());
sprite.addComponents(new Sprite(texture));
```

2. Adding audio to a scene:
```java
AudioEmitter emitter = new AudioEmitter("background.wav");
emitter.setGain(0.5f);
emitter.setLooping(true);
entity.addComponents(emitter);
```

## Data Flow
The engine processes game data through a component-based entity system, managing rendering and updates through a scene graph.

```ascii
[Input]    ->   [Scene Controller]   ->  [Entity Updates] -> [Component Processing]
                                               |
                                               v
[Resource Manager] <- [Graphics/Audio] <- [Render Queue]
```

Component interactions:
1. [Scene/SceneController](https://github.com/yield-proj/YieldEngine/blob/master/core/src/main/java/com/xebisco/yieldengine/core/Scene.java) manages entity lifecycle and updates
2. [Entities](https://github.com/yield-proj/YieldEngine/blob/master/core/src/main/java/com/xebisco/yieldengine/core/Entity.java) contain components and maintain hierarchical relationships
3. [Components](https://github.com/yield-proj/YieldEngine/blob/master/core/src/main/java/com/xebisco/yieldengine/core/Component.java) handle specific behaviors (rendering, audio, physics)
4. [Graphics](https://github.com/yield-proj/YieldEngine/blob/master/core/src/main/java/com/xebisco/yieldengine/core/graphics) system processes render queue using YLDG1 implementations
5. Resource manager[(IO)](https://github.com/yield-proj/YieldEngine/blob/master/core/src/main/java/com/xebisco/yieldengine/core/Entity.java) handles asset loading and unloading
6. [Input](https://github.com/yield-proj/YieldEngine/blob/master/core/src/main/java/com/xebisco/yieldengine/core/input) system provides keyboard and mouse state updates
7. [Audio](https://github.com/yield-proj/YieldEngine/blob/master/core/src/main/java/com/xebisco/yieldengine/core/io/audio) system manages spatial audio through emitter/listener components


## 😁 Getting the engine
### Compiled jars
A compiled version of every main release is hosted here on github, check [Releases](https://github.com/yield-proj/YieldEngine/releases), to choose witch version of the engine to download.
### Compile it yourself
If you really want, you can also compile the [YieldEngine](https://github.com/yield-proj/YieldEngine/archive/refs/heads/master.zip) to fit your needs.

## Contributing

Contributions, issues and feature requests are welcome!🤝<br />To start take a look at the [contributing guide](https://github.com/yield-proj/YieldEngine/blob/master/CONTRIBUTING.md).

## 📝 License

Copyright © 2023 [Xebisco](https://github.com/Xebisco).
This project is [Apache License 2.0](LICENSE) licensed 📝.

## Author

👤 **Xebisco**

* Website: xebisco.com
* Twitter: [@Xebisco](https://twitter.com/Xebisco)
* Github: [@Xebisco](https://github.com/Xebisco)
* LinkedIn: [Vítor Toledo de Oliveira](https://www.linkedin.com/in/v%C3%ADtor-toledo-077438213/)

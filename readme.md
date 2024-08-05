# Ardent Engine

Ardent Engine is a free and open source general purpose game engine written in Java currently in its early stage of development.

## Main features

* Intuitive visual editor application.
* Support for multiple platforms.
* 2D and 3D rendering and physics engine.
* Player input via Keyboard/Mouse and Gamepad.

## Core pillars

### Modularity

The engine is designed to be modular.
This will allow components to be added, removed, or replaced without altering the engine's core module.

For example, it will be possible to change the rendering module from OpenGL to Vulkan and vice versa by changing a dependency.

### Ease of use

Ease of use is the primary focus of the engine's design.
Some systems were made to be similar to other popular engines users may be more familiar with.

Some classes deliberately avoid the usage of Java's typical encapsulation (getters and setters) to allow user to access their properties more easily.

### Portability

The final goal will be to allow users to create the game once and build for several different platforms.

### Efficiency

Most parts of the engine will be designed with execution speed in mind.
For this reason, most of the work related to rendering will be carried out by glsl shaders.

Performance-critical parts of the engine will make use of the Java Native Interface for maximum efficiency.
The engine will be responsible for loading the correct library for the current operating system to maintain the advantage of portability.

## Road map

### Version 0.1

| Feature                  | Status      |
|--------------------------|-------------|
| 3D rendering of meshes   | In progress |
| 2D rendering of textures | Done        |
| 2D physics in Java       | Missing     |
| Shaders system           | Done        |
| Basic 3D lighting        | In progress |
| Resource manager         | Done        |
| Nuklear bindings         | Missing     |
| Stub editor application  | Missing     |

### Version 0.2

* Functioning editor application
* Rendering of 3D models
* Materials system
* Support for multiple windows
* Audio module (OpenAL bindings)

### Version 0.3

* 3D physics in Java
* More complex 2D rendering (Tile maps, lines, polygons...)
* Finished 3D lighting system
* Basic 2D lighting
* GUI system (without Nuklear)

### Version 0.4

* Vulkan bindings
* Physics system using JNI (either Jolt or Bullet)

### Version 0.5

* Android support
* Support for other JVM languages (Scala, Kotlin...)

### Version 1.0 (Minimum viable product)

* TODO

## Contributing

Ardent Engine was developed by a single person and was started as a simple rendering engine written for a bachelor thesis.

Your contributions are always welcome!
Please submit a pull request or open an issue if you want to contribute with bug  fixes, code improvements, documentation, and better unit test coverage.

## Support

Support the project with a donation:

* [PayPal](https://paypal.me/hexagonnico)
* [Ko-fi](https://ko-fi.com/HexagonNico)
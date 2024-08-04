[![Maven Central](https://img.shields.io/maven-central/v/de.pirckheimer-gymnasium/jbox2d-library.svg?style=flat)](https://central.sonatype.com/artifact/de.pirckheimer-gymnasium/jbox2d-library)
[![javadoc](https://javadoc.io/badge2/de.pirckheimer-gymnasium/jbox2d-library/javadoc.svg)](https://javadoc.io/doc/de.pirckheimer-gymnasium/jbox2d-library)

# jbox2d

Why this fork:

jbox2d has not been updated on Maven Central for a long time:
https://central.sonatype.com/artifact/org.jbox2d/jbox2d

However, there have been many commits in the Git repository since the last release: https://github.com/jbox2d/jbox2d

JBox2d is a Java port of the C++ physics engines
[LiquidFun](http://google.github.io/liquidfun/) and [Box2d](http://box2d.org).

If you're looking to deploy on the web, see
[PlayN](https://github.com/playn/playn), which compiles JBox2d through GWT so
it runs in the browser. The JBox2d library has GWT support out of the box.
Also, [TeaVM](http://teavm.org/) support jbox2d in the browser as well.

If you've downloaded this as an archive, you should find the built java jars in
the 'target' directories of each project.

---

- `jbox2d-library` - this is the main physics library. The only dependency is the
  SLF4J logging library.
- `jbox2d-serialization` - this adds serialization tools. Requires google's
  protocol buffer library installed to fully build
  (http://code.google.com/p/protobuf/), but this is optional, as the generated
  sources are included.
- `jbox2d-testbed` - A simple framework for creating and running physics tests.
- `jbox2d-testbed-jogl` - The testbed with OpenGL rendering.
- `jbox2d-jni-broadphase` - Experiment with moving parts of the engine to C++. Not
  faster.


![](https://raw.githubusercontent.com/engine-pi/jbox2d/main/screencasts/BulletTest.gif)

![](https://raw.githubusercontent.com/engine-pi/jbox2d/main/screencasts/Car.gif)

![](https://raw.githubusercontent.com/engine-pi/jbox2d/main/screencasts/DamBreak.gif)

![](https://raw.githubusercontent.com/engine-pi/jbox2d/main/screencasts/Dominos.gif)

![](https://raw.githubusercontent.com/engine-pi/jbox2d/main/screencasts/DrawingParticles.gif)

![](https://raw.githubusercontent.com/engine-pi/jbox2d/main/screencasts/LiquidTimer.gif)

![](https://raw.githubusercontent.com/engine-pi/jbox2d/main/screencasts/Particles.gif)

![](https://raw.githubusercontent.com/engine-pi/jbox2d/main/screencasts/WaveMachine.gif)

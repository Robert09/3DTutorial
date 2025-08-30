package me.formercanuck.engine;

import me.formercanuck.engine.graphics.Renderer;
import me.formercanuck.engine.graphics.Window;
import me.formercanuck.engine.scene.Scene;

public interface IGameLogic {

    void cleanup();

    void init(Window window, Scene scene, Renderer render);

    void input(Window window, Scene scene, long diffTimeMillis);

    void update(Window window, Scene scene, long diffTimeMillis);
}

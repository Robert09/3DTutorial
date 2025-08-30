package me.formercanuck.engine.graphics;

import me.formercanuck.engine.scene.Scene;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private SceneRenderer sceneRenderer;

    public Renderer() {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        sceneRenderer = new SceneRenderer();
    }

    public void cleanup() {
        sceneRenderer.cleanup();
    }

    public void render(Window window, Scene scene) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, window.getWidth(), window.getHeight());

        sceneRenderer.render(scene);
    }
}
package me.formercanuck.game;

import me.formercanuck.engine.IGameLogic;
import me.formercanuck.engine.graphics.Mesh;
import me.formercanuck.engine.graphics.Model;
import me.formercanuck.engine.graphics.Renderer;
import me.formercanuck.engine.graphics.Window;
import me.formercanuck.engine.scene.Entity;
import me.formercanuck.engine.scene.Scene;
import me.formercanuck.engine.utils.Configuration;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;

public class FormerFactory implements IGameLogic {

    private static Configuration configuration;

    private Entity cubeEntity;
    private Vector4f displInc = new Vector4f();
    private float rotation;

    public FormerFactory() {
        configuration = new Configuration("formerfactory");

        int timesRun = configuration.getInt("timesRun", 0);
        configuration.set("timesRun", ++timesRun);

        configuration.save();
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void init(Window window, Scene scene, Renderer render) {

        float[] positions = new float[]{
                // Front face
                -0.5f,  0.5f, -1.5f, // 0
                -0.5f, -0.5f, -1.5f, // 1
                0.5f, -0.5f, -1.5f, // 2
                0.5f,  0.5f, -1.5f, // 3

                // Back face
                -0.5f,  0.5f, -3.5f, // 4
                -0.5f, -0.5f, -3.5f, // 5
                0.5f, -0.5f, -3.5f, // 6
                0.5f,  0.5f, -3.5f, // 7
        };

        float[] colors = new float[]{
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        };
        int[] indices = new int[]{
                0, 1, 3, 3, 1, 2
        };

        List<Mesh> meshList = new ArrayList<>();
        Mesh mesh = new Mesh(positions, colors, indices);
        meshList.add(mesh);
        String cubeModelId = "cube-model";
        Model model = new Model(cubeModelId, meshList);
        scene.addModel(model);

        cubeEntity = new Entity("cube-entity", cubeModelId);
        cubeEntity.setPosition(0, 0, -2);
        scene.addEntity(cubeEntity);
    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis) {
        displInc.zero();
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            displInc.y = 1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            displInc.y = -1;
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            displInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            displInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            displInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_Q)) {
            displInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            displInc.w = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            displInc.w = 1;
        }

        displInc.mul(diffTimeMillis / 1000.0f);

        Vector3f entityPos = cubeEntity.getPosition();
        cubeEntity.setPosition(displInc.x + entityPos.x, displInc.y + entityPos.y, displInc.z + entityPos.z);
        cubeEntity.setScale(cubeEntity.getScale() + displInc.w);
        cubeEntity.updateModelMatrix();
    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {
        rotation += 1.5;
        if (rotation > 360) {
            rotation = 0;
        }
        cubeEntity.setRotation(1, 1, 1, (float) Math.toRadians(rotation));
        cubeEntity.updateModelMatrix();
    }

    public static Configuration getConfiguration() {
        return configuration;
    }
}

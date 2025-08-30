package me.formercanuck.game;

import me.formercanuck.engine.GameEngine;
import me.formercanuck.engine.graphics.Window;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        GameEngine engine = new GameEngine("Former Factory", new Window.WindowOptions(), new FormerFactory());
        engine.start();
//        new Window("Former Factory", 1280, 720, false).run();
    }
}

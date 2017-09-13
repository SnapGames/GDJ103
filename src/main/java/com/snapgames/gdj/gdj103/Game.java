/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * GDJ102
 * 
 * @year 2017
 */
package com.snapgames.gdj.gdj103;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * the basic Game container is a JPanel child.
 * 
 * @author Frederic
 *
 */
public class Game extends JPanel {

	/**
	 * The title for the game instance.
	 */
	private String title = "game";

	/**
	 * Game display space dimension
	 */
	private Dimension dimension = null;

	/**
	 * Active window for this Game.
	 */
	private Window window;

	/**
	 * Flag to activate debug information display.
	 */
	private boolean debug = true;

	/**
	 * flag representing the exit request status. true => exit
	 */
	private boolean exit = false;

	/**
	 * Rendering interface.
	 */
	private Graphics2D g;

	/**
	 * Input manager
	 */
	private InputHandler inputHandler;

	/**
	 * List of managed objects.
	 */
	private List<GameObject> objects = new ArrayList<>();

	GameObject player = null;

	/**
	 * the default constructor for the {@link Game} panel with a game
	 * <code>title</code>.
	 * 
	 * @param title
	 *            the title for the game.
	 */
	private Game(String title) {
		this.title = title;
		this.dimension = new Dimension(640, 400);
		exit = false;
		inputHandler = new InputHandler();
	}

	/**
	 * Initialize the Game object with <code>g</code>, the Graphics2D interface
	 * to render things.
	 */
	private void initialize() {
		player = new GameObject("player", WIDTH / 2, HEIGHT / 2, 32, 32, 1, 1, Color.BLUE);
		objects.add(player);
	}

	/**
	 * The main Loop !
	 */
	private void loop() {
		long currentTime = System.currentTimeMillis();
		long lastTime = currentTime;
		while (!exit) {
			currentTime = System.currentTimeMillis();
			long dt = currentTime - lastTime;
			input();
			update(dt);
			g = (Graphics2D) window.frame.getGraphics();
			render(g);
			lastTime = currentTime;
		}
	}

	/**
	 * Manage Game input.
	 */
	private void input() {
		// Process keys
		if (inputHandler.getKeyReleased(KeyEvent.VK_ESCAPE)) {
			setExit(true);
		}
		if (inputHandler.getKeyPressed(KeyEvent.VK_UP)) {
			player.dy = -2;
		}
		if (inputHandler.getKeyPressed(KeyEvent.VK_DOWN)) {
			player.dy = +2;
		}
		if (inputHandler.getKeyPressed(KeyEvent.VK_LEFT)) {
			player.dx = -2;
		}
		if (inputHandler.getKeyPressed(KeyEvent.VK_RIGHT)) {
			player.dx = +2;
		}
		inputHandler.clean();
	}

	/**
	 * Update game internals
	 */
	private void update(long dt) {

		if (!objects.isEmpty()) {
			for (GameObject o : objects) {
				o.update(dt);
			}
		}
	}

	/**
	 * render all the beautiful things.
	 * 
	 * @param g
	 */
	private void render(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		if (!objects.isEmpty()) {
			for (GameObject o : objects) {
				o.draw(g);
				if (debug) {
					g.setColor(Color.GREEN);
					g.drawString(o.name, o.x + o.width + 10, o.y);
				}
			}
		}

	}

	/**
	 * free all resources used by the Game.
	 */
	private void release() {
		objects.clear();
		window.frame.dispose();
	}

	/**
	 * the only public method to start game.
	 */
	public void run() {
		initialize();
		loop();
		release();
		System.exit(0);
	}

	/**
	 * return the title of the game.
	 * 
	 * @return
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * return the dimension of the Game display.
	 * 
	 * @return
	 */
	public Dimension getDimension() {
		return dimension;
	}

	/**
	 * Set the active window for this game.
	 * 
	 * @param window
	 *            the window to set as active window for the game.
	 */
	public void setWindow(Window window) {
		this.window = window;
	}

	/**
	 * @return the exit
	 */
	public boolean isExit() {
		return exit;
	}

	/**
	 * @param exit
	 *            the exit to set
	 */
	public void setExit(boolean exit) {
		this.exit = exit;
	}

	/**
	 * @return the inputHandler
	 */
	public InputHandler getInputHandler() {
		return inputHandler;
	}

	/**
	 * The main entry point to start our GDJ103 game.
	 * 
	 * @param argv
	 */
	public static void main(String[] argv) {
		Game game = new Game("GDJ103");
		Window window = new Window(game);
		game.run();
	}
}

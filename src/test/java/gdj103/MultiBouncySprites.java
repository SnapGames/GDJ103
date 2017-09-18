/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * GDJ103
 * 
 * @year 2017
 */
package gdj103;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;

import com.snapgames.gdj.gdj103.RenderHelper;
import com.snapgames.gdj.gdj103.RenderHelper.TextPosition;

/**
 * The MultiBouncySprites is animated screen frames displaying multi balls
 * bouncing on screen limit with a small sound.
 * 
 * @author Frédéric Delorme
 *
 */
public class MultiBouncySprites {

	/**
	 * Internal Object to manage BouncyBall update and render.
	 * 
	 * @author Frédéric Delorme
	 *
	 */
	public class Sprite {
		public BufferedImage sprite;
		public float x = 0, y = 0, dx = 200, dy = 200;

		public Sprite(BufferedImage sprite, float x, float y, float dx, float dy) {
			super();
			this.sprite = sprite;
			this.x = x;
			this.y = y;
			this.dx = dx;
			this.dy = dy;
		}

	}

	public class InputHandler implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
			int nbToAdd = 1;
			if (e.isShiftDown()) {
				nbToAdd = 10;
			} else if (e.isControlDown()) {
				nbToAdd = 100;
			}
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
			case KeyEvent.VK_X:
				isActive = false;
				break;
			case KeyEvent.VK_PAGE_UP:
			case KeyEvent.VK_PLUS:
				initializeBalls(nbToAdd);
				break;
			case KeyEvent.VK_PAGE_DOWN:
			case KeyEvent.VK_MINUS:
				for (int i = 0; i < nbToAdd; i++) {
					if (objects.size() > 0) {
						objects.remove(0);
					}
				}
				break;
			case KeyEvent.VK_M:
				isManualyMute = !isManualyMute;
				break;
			case KeyEvent.VK_R:
				objects.clear();
				initializeBalls(10);
				break;
			case KeyEvent.VK_PAUSE:
			case KeyEvent.VK_P:
				isPause = !isPause;
				break;
			case KeyEvent.VK_H:
			case KeyEvent.VK_F1:
				showHelp = !showHelp;
				break;
			case KeyEvent.VK_F11:
				
				break;
			}

		}

		@Override
		public void keyPressed(KeyEvent e) {
		}
	}

	public class GameLoop {
		private float targetFpsTime = 0.0f;
		private long lastTime = 0;
		private long lastSecond = 0;
		private int frames = 0;

		
		public GameLoop() {
			super();
		}
		
		
		public void init(MultiBouncySprites game) {
			targetFpsTime = (1000.0f / game.targetFps);
			lastTime = 0;
			lastSecond = 0;
			lastTime = lastSecond = System.nanoTime();
			frames = 0;
			game.isActive = true;
		}

		public void loop(MultiBouncySprites game, InputHandler input) {
			while (game.isActive) {
				long deltaTime = System.nanoTime() - lastTime;
				lastTime += deltaTime;
				if (!game.isPause) {
					game.update(deltaTime);
				}
				do {
					do {
						Graphics2D g = (Graphics2D) game.strategy.getDrawGraphics();
						game.render(g);
						g.dispose();
					} while (game.strategy.contentsRestored());
					game.strategy.show();
				} while (game.strategy.contentsLost());
				frames++;
				long wait = (long) targetFpsTime - ((System.nanoTime() - lastTime) / 1000000);
				if (wait < 0) {
					wait = 5;
				}
				try {
					Thread.sleep(wait);
				} catch (InterruptedException e) {
					System.err.println("Error while trying to wait " + wait + " ms:" + e.getMessage());
				}
				game.renderingFrameDuration = System.nanoTime() - lastTime;
				long timeSinceLastSecond = System.nanoTime() - lastSecond;
				if (timeSinceLastSecond >= 1e9) {
					fps = frames;
					frames = 0;
					lastSecond += 1e9;
				}
			}
		}
	}

	/**
	 * Screen size.
	 */
	private final int WIDTH = 800, HEIGHT = WIDTH / 16 * 9;
	/**
	 * is game active
	 */
	private boolean isActive = true;
	/**
	 * is game in pause mode ?
	 */
	private boolean isPause = false;

	private boolean showHelp = true;

	private boolean isMute = false;
	private boolean isManualyMute = false;

	/**
	 * FPS counter for on-screen display
	 */
	private int fps = 0;
	long renderingFrameDuration = 0;

	/**
	 * Font to render the FPS counter.
	 */
	private Font fpsFont = null;
	private Font pauseFont = null;

	/**
	 * Buffer Strategy for screen rendering.
	 */
	private BufferStrategy strategy;
	/**
	 * Resource to display a blue ball
	 */
	private BufferedImage sprite;

	/**
	 * The frame per second targeted by the engine.
	 */
	private float targetFps = 60.0f;
	/**
	 * Resource to play bouncy sound.
	 */
	private Clip bounce;
	/**
	 * Canvas containing all the game mechanism.
	 */
	private Canvas canvas = null;

	/**
	 * The list of Sprite to be animated on Screen.
	 */
	private List<Sprite> objects = new CopyOnWriteArrayList<>();
	private InputHandler inputHandler;

	/**
	 * A beautiful class to tests things.
	 */
	public MultiBouncySprites() {

		JFrame frame = new JFrame("Bouncy Sprite");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIgnoreRepaint(true);
		frame.setResizable(true);

		canvas = new Canvas();
		canvas.setIgnoreRepaint(true);
		canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		inputHandler = new InputHandler();
		canvas.addKeyListener(inputHandler);
		frame.add(canvas);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		canvas.requestFocus();
		canvas.createBufferStrategy(2);
		strategy = canvas.getBufferStrategy();
	}

	/**
	 * Initialize resources
	 */
	public void init() {
		try {

			// prepare fonts
			fpsFont = new Font(Font.MONOSPACED, Font.PLAIN, 14);
			pauseFont = new Font(Font.SANS_SERIF, Font.PLAIN, 32);

			// load PNG
			sprite = ImageIO.read(MultiBouncySprites.class.getResource("/res/data/bouncy-ball.png"));

			// load WAV
			bounce = AudioSystem.getClip();
			bounce.open(AudioSystem.getAudioInputStream(MultiBouncySprites.class.getResource("/res/data/bounce.wav")));
		} catch (Exception exc) {
			exc.printStackTrace();
			exit();
		}
		// initialize some balls.
		initializeBalls(10);

	}

	/**
	 * 
	 */
	private void initializeBalls(int nbBalls) {
		for (int i = 0; i < nbBalls; i++) {
			objects.add(createBall());
		}
	}

	/**
	 * @return
	 */
	private Sprite createBall() {
		float speedFactor = 400;
		return new Sprite(sprite, canvas.getWidth() / 2, canvas.getHeight() / 2,
				(float) Math.random() * speedFactor - (speedFactor / 2),
				(float) Math.random() * speedFactor - (speedFactor / 2));
	}

	/**
	 * Run the Bouncy Sprite
	 */
	public void run() {
		init();

		GameLoop gameloop = new GameLoop();
		gameloop.init(this);

		gameloop.loop(this, inputHandler);
		exit();

	}

	/**
	 * Compute the next frame.
	 * 
	 * @param deltaTime
	 */
	public void update(long deltaTime) {
		double delta = deltaTime / 1e9;
		// if there is too much, we switch audio to mute (for a safer audition :)
		isMute = isManualyMute || objects.size() > 50;
		// update all objects in the pipe.
		for (Sprite o : objects) {
			if (o.x < 0 || o.x > (canvas.getWidth() - sprite.getWidth())) {
				o.dx = -o.dx;
				playSound(bounce);
			}
			if (o.y < 0 || o.y > (canvas.getHeight() - sprite.getHeight())) {

				o.dy = -o.dy;
				playSound(bounce);
			}
			o.x += o.dx * delta;
			o.y += o.dy * delta;
		}
	}

	/**
	 * render the scene.
	 * 
	 * @param g
	 *            Graphic interface to us to draw all things.
	 */
	public void render(Graphics2D g) {
		// clear screen.
		g.setBackground(Color.BLACK);
		g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		// Draw the Bouncing ball
		int i = 0;
		for (Sprite o : objects) {
			g.drawImage(o.sprite, (int) Math.round(o.x), (int) Math.round(o.y), null);
			i++;
		}
		if (showHelp) {
			RenderHelper.display(g, 10, 10, fpsFont, new String[] { String.format("fps: %d", fps),
					String.format("frame duration: %03.2f ms", (renderingFrameDuration / 1000000.0f)),
					String.format("Objects: %d", objects.size()), "---------------------------------------------------",
					"[+/PUp]       add 1 object, SHIFT/+10, CTRL/+100",
					"[-/PDown]     remove 1 object, SHIFt/-10, CTRL/-100", "[R]           reset to 10 objects",
					"[M]       [" + RenderHelper.showBoolean(isMute, "M", " ") + "] Mute/Unmute sound",
					"[P/Pause] [" + RenderHelper.showBoolean(isPause, "P", " ") + "] pause demo",
					"[X/ESC]       exit demo" });
		}
		if (isPause) {
			g.setFont(pauseFont);
			RenderHelper.drawShadowString(g, "PAUSE", canvas.getWidth() / 2, canvas.getHeight() / 2, Color.WHITE,
					Color.BLACK, TextPosition.CENTER);
		}
	}

	/**
	 * Exit from the BouncySprite.
	 */
	private void exit() {
		sprite = null;
		bounce.stop();
		bounce = null;
		System.exit(0);
	}

	/**
	 * Play the clip sound.
	 * 
	 * @param clip
	 */
	private void playSound(Clip clip) {
		if (!isMute) {
			// play sound
			clip.stop();
			clip.setFramePosition(0);
			clip.start();
		}
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new MultiBouncySprites().run();
	}
}
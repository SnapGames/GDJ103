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

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;

public class BouncySprite {

	private final int WIDTH = 800, HEIGHT = WIDTH / 16 * 9;
	private BufferStrategy strategy;
	private boolean isActive = true;
	private boolean isPause = false;

	private float x = 0, y = 0, dx = 200, dy = 200;

	private int fps = 0;
	private Font fpsFont = null;

	private BufferedImage sprite;
	private Clip bounce;
	private Canvas canvas = null;

	/**
	 * A beautiful class to tests things.
	 */
	public BouncySprite() {

		JFrame frame = new JFrame("Bouncy Sprite");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIgnoreRepaint(true);
		frame.setResizable(true);

		canvas = new Canvas();
		canvas.setIgnoreRepaint(true);
		canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		canvas.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_ESCAPE:
				case KeyEvent.VK_X:
					isActive = false;
					break;
				case KeyEvent.VK_UP:
					dx += 50;
					break;
				case KeyEvent.VK_DOWN:
					dx -= 50;
					break;
				case KeyEvent.VK_LEFT:
					dy += 50;
					break;
				case KeyEvent.VK_RIGHT:
					dy -= 50;
					break;
				case KeyEvent.VK_PAUSE:
				case KeyEvent.VK_P:
					isPause = !isPause;
					break;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
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

			fpsFont = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
			// load PNG
			sprite = ImageIO.read(BouncySprite.class.getResource("/res/data/bouncy-ball.png"));
			// load WAV
			bounce = AudioSystem.getClip();
			bounce.open(AudioSystem.getAudioInputStream(BouncySprite.class.getResource("/res/data/bounce.wav")));
		} catch (Exception exc) {
			exc.printStackTrace();
			exit();
		}
	}

	/**
	 * Run the Bouncy Sprite
	 */
	public void run() {
		init();
		float targetFps = 60.0f;
		float targetFpsTime = (1000.0f / targetFps);
		long lastTime = 0, lastSecond = 0;
		lastTime = lastSecond = System.nanoTime();
		int frames = 0;
		isActive = true;
		while (isActive) {
			long deltaTime = System.nanoTime() - lastTime;
			lastTime += deltaTime;
			if (!isPause) {
				update(deltaTime);
			}
			do {
				do {
					Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
					render(g);
					g.dispose();
				} while (strategy.contentsRestored());
				strategy.show();
			} while (strategy.contentsLost());
			frames++;
			long wait = (long) targetFpsTime - ((System.nanoTime() - lastTime) / 1000000);
			if (wait < 0) {
				wait = 5;
			}
			try {
				Thread.sleep(wait);
				System.out.println("wait:" + wait);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (System.nanoTime() - lastSecond >= 1e9) {
				fps = frames;
				frames = 0;
				lastSecond += 1e9;
			}
		}

		exit();

	}

	/**
	 * Compute the next frame.
	 * 
	 * @param deltaTime
	 */
	public void update(long deltaTime) {
		double delta = deltaTime / 1e9;
		if (x < 0 || x > (canvas.getWidth() - sprite.getWidth())) {
			dx = -dx;
			playSound(bounce);
		}
		x += dx * delta;
		if (y < 0 || y > (canvas.getHeight() - sprite.getHeight())) {

			dy = -dy;
			playSound(bounce);
		}
		y += dy * delta;
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
		g.drawImage(sprite, (int) Math.round(x), (int) Math.round(y), null);
		g.setColor(Color.WHITE);
		g.setFont(fpsFont);
		g.drawString(String.format("fps: %d- p:(%04.2f,%04.2f) - d:(%04.2f,%04.2f)", fps, x, y, dx, dy), 10, 10);
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
		// play sound
		clip.stop();
		clip.setFramePosition(0);
		clip.start();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new BouncySprite().run();
	}
}
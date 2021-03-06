/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * gdj103
 * 
 * @year 2017
 */
package com.snapgames.gdj.gdj103;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;

/**
 * InputHandler
 * 
 * @author Frédéric Delorme
 *
 */
public class InputHandler implements KeyListener {

	boolean[] keys = new boolean[256];
	boolean[] keysPrevious = new boolean[256];

	Stack<KeyEvent> events = new Stack<>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {

		events.push(e);
		keysPrevious[e.getKeyCode()] = keys[e.getKeyCode()];
		keys[e.getKeyCode()] = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		keysPrevious[e.getKeyCode()] = keys[e.getKeyCode()];
		keys[e.getKeyCode()] = false;
	}

	/**
	 * return the status pressed for the key <code>k</code>.
	 * 
	 * @param k
	 *            the KeyEvent.VK_xxx code.
	 * @return true if key is pressed.
	 */
	public boolean getKeyPressed(int k) {
		assert (k >= 0 && k < 256);
		return keys[k];
	}

	/**
	 * return the status released for the key <code>k</code>.
	 * 
	 * @param k
	 *            the KeyEvent.VK_xxx code.
	 * @return true if key is released.
	 */
	public boolean getKeyReleased(int k) {
		assert (k >= 0 && k < 256);
		return keys[k] && !keysPrevious[k];
	}

	public KeyEvent getEvent() {
		if (!events.isEmpty()) {
			return events.pop();
		} else {
			return null;
		}
	}
}

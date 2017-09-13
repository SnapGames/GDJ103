/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * gdj102
 * 
 * @year 2017
 */
package com.snapgames.gdj.gdj103;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * InputHandler
 * 
 * @author frederic
 *
 */
public class InputHandler implements KeyListener {

	boolean[] keyPressed = new boolean[256];
	boolean[] keyReleased = new boolean[256];

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		System.out.println(String.format("User typed the '%s' key", e.getKeyCode()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		keyPressed[e.getKeyCode()] = true;
		System.out.println(String.format("User pressed the '%s' key", e.getKeyCode()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println(String.format("User released the '%s' key", e.getKeyCode()));
		keyReleased[e.getKeyCode()] = true;
	}

	/**
	 * Clean the internal buffer for key state cache.
	 */
	public void clean() {
		for (int i = 0; i < keyPressed.length; i++) {
			keyPressed[i] = false;
			keyReleased[i] = false;
		}
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
		return keyPressed[k];
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
		return keyReleased[k];
	}
}

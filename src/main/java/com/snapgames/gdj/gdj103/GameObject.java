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

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * GameObject
 * 
 * @author frederic
 *
 */
public class GameObject {
	/**
	 * internal index to generate the default object name.
	 */
	private static int index = 0;
	/**
	 * Name of this object.
	 */
	public String name;
	/**
	 * position of this object onto the display space.
	 */
	public int x = 0, y = 0;
	/**
	 * Velocity of this object.
	 */
	public int dx = 0, dy = 0;
	/**
	 * Size of this object.
	 */
	public int width = 32, height = 32;
	/**
	 * Rendering depth and priority.
	 */
	public int layer = 0, priority = 1;

	public Color color = Color.GREEN;

	/**
	 * Default constructor for this GameObject.
	 */
	public GameObject() {
		super();
		index++;
	}

	/**
	 * Create a GameObject with <code>name</code>, position
	 * (<code>x</code>,<code>y</code>) size
	 * (<code>width</code>,<code>height</code>) on a <code>layer</code> width a
	 * rendering <code>priority</code> and a <code>color</code>.
	 * 
	 * @param name
	 *            the name for this object.
	 * @param x
	 *            x position in the (x,y) for this object
	 * @param y
	 *            y position in the (x,y) for this object
	 * @param width
	 *            width of the object
	 * 
	 * @param height
	 *            height of the object
	 * @param layer
	 *            layer where to render this object
	 * @param priority
	 *            priority to sort the rendering position of this object.
	 */
	public GameObject(String name, int x, int y, int width, int height, int layer, int priority, Color color) {
		this(name, x, y, 0, 0);
		this.width = width;
		this.height = height;
		this.layer = layer;
		this.priority = priority;
		this.color = color;
	}

	/**
	 * Create a GameObject with <code>name</code>, position
	 * (<code>x</code>,<code>y</code>) with a velocity of
	 * (<code>dx</code>,<code>dy</code>).
	 * 
	 * @param name
	 *            the name for this object.
	 * @param x
	 *            x position in the (x,y) for this object
	 * @param y
	 *            y position in the (x,y) for this object
	 * @param dx
	 *            velocity on x direction
	 * @param dy
	 *            velocity on y direction.
	 */
	public GameObject(String name, int x, int y, int dx, int dy) {
		super();
		this.name = (name == null || name.equals("") ? "noname_" + index : name);
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
	}

	public void update(long dt) {
		x += dx * dt;
		y += dy * dt;
		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;
		if (x > Game.WIDTH - width)
			x = Game.WIDTH - width;
		if (y > Game.HEIGHT - height)
			y = Game.HEIGHT - height;
	}

	public void draw(Graphics2D g) {
		if (color != null) {
			g.setColor(color);
		}
		g.drawRect(x, y, width, height);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GameObject [name=").append(name).append(", x=").append(x).append(", y=").append(y)
				.append(", width=").append(width).append(", height=").append(height).append(", layer=").append(layer)
				.append(", priority=").append(priority).append("]");
		return builder.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + layer;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + priority;
		result = prime * result + width;
		result = prime * result + (int) (x ^ (x >>> 32));
		result = prime * result + (int) (y ^ (y >>> 32));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameObject other = (GameObject) obj;
		if (height != other.height)
			return false;
		if (layer != other.layer)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (priority != other.priority)
			return false;
		if (width != other.width)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

}

package com.popolvuh.game;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Shape;

public abstract class GameObject 
{
	public static final float GRAVITY = 0.0025f;
	public static final int SPRITE_SCALE = 2;

	protected float x, y, xvel, yvel;
	protected boolean onground;
	public GameObject(float x, float y) throws SlickException
	{
		this.x = x;
		this.y = y;
	}

	public abstract void update(GameContainer gc, int i, ArrayList<GameObject> objects) throws SlickException;
	public abstract void render(GameContainer gc, Graphics g) throws SlickException;
	
	private static HashMap<String, SpriteSheet> sheets = new HashMap<>();
	protected static SpriteSheet getSpriteSheet(String filepath, int x, int y) throws SlickException
	{
		if(!sheets.containsKey(filepath))
		{
			sheets.put(filepath, new SpriteSheet(new SpriteSheet(filepath, x, y).getScaledCopy(SPRITE_SCALE), x*SPRITE_SCALE, y*SPRITE_SCALE));
		}
		return sheets.get(filepath);
	}

	public abstract Shape getShape() throws SlickException;

	protected static enum CollisionSide
	{
		LEFT, RIGHT, TOP, BOTTOM;
	}

	protected CollisionSide collide(GameObject o) throws SlickException
	{
		if(o == this)
		{
			return null;
		}
		if(o.getShape().intersects(getShape()))
		{
			//collision on bottom
			for(float p = 0; p < getShape().getWidth(); p += o.getShape().getWidth() / 2)
			{
				if(o.getShape().contains(getShape().getMinX() + p, getShape().getMaxY())
				|| o.getShape().contains(getShape().getMaxX() - p, getShape().getMaxY()))
				{
					y = o.getShape().getMinY() - getShape().getHeight();
					return CollisionSide.BOTTOM;
				}
			}
			//collision on left
			for(float p = 0; p < getShape().getHeight(); p += o.getShape().getHeight() / 2)
			{
				if(o.getShape().contains(getShape().getMinX(), getShape().getMinY() + p)
				|| o.getShape().contains(getShape().getMinX(), getShape().getMaxY() - p))
				{
					x = o.getShape().getMaxX() + 1;
					return CollisionSide.LEFT;
				}
			}
			//collision on right
			for(float p = 0; p < getShape().getHeight(); p += o.getShape().getHeight() / 2)
			{
				if(o.getShape().contains(getShape().getMaxX(), getShape().getMinY() + p)
				|| o.getShape().contains(getShape().getMaxX(), getShape().getMaxY() - p))
				{
					x = o.getShape().getMinX() - getShape().getWidth() - 1;
					return CollisionSide.RIGHT;
				}
			}
			//collision on top
			for(float p = 0; p < getShape().getWidth(); p += o.getShape().getWidth() / 2)
			{
				if(o.getShape().contains(getShape().getMinX() + p, getShape().getMinY())
				|| o.getShape().contains(getShape().getMaxX() - p, getShape().getMinY()))
				{
					y = o.getShape().getMaxY();
					return CollisionSide.TOP;
				}
			}
		}
		return null;
	}
}

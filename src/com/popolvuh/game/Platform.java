package com.popolvuh.game;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Platform extends GameObject
{
	public Platform(float x, float y) throws SlickException
	{
		super(x, y);
	}

	private SpriteSheet sprite = null;
	private SpriteSheet getSpriteSheet() throws SlickException
	{
		if(sprite == null)
		{
			sprite = getSpriteSheet("PixelArt/Properly Formatted/DesertPlatform1.png", 39, 8);
		}
		return sprite;
	}

	@Override
	public void update(GameContainer gc, int i, ArrayList<GameObject> objects) throws SlickException
	{
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		getSpriteSheet().draw(x, y);
	}

	@Override
	public Shape getShape() throws SlickException
	{
		return new Rectangle(x, y, getSpriteSheet().getWidth(), getSpriteSheet().getHeight());
	}

}

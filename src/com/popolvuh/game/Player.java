package com.popolvuh.game;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Player extends GameObject
{
	public Player(float x, float y) throws SlickException
	{
		super(x, y);
	}

	private Animation walkanimright = null;
	private Animation walkanimleft = null;
	private Animation getWalkAnimation() throws SlickException
	{
		if(walkanimright == null || walkanimleft == null)
		{
			walkanimright = new Animation(getSpriteSheet("PixelArt/SpriteSheets/Cara.png"     , 17, 40), 300);
			walkanimleft  = new Animation(getSpriteSheet("PixelArt/SpriteSheets/Cara-left.png", 17, 40), 300);
		}
		if(xvel < 0)
		{
			return walkanimleft;
		}
		return walkanimright;
	}
	private Animation idleanimright = null;
	private Animation idleanimleft = null;
	private Animation getIdleAnimation() throws SlickException
	{
		if(idleanimright == null || idleanimleft == null)
		{
			idleanimright = new Animation(getSpriteSheet("PixelArt/SpriteSheets/CaraIdle.png"     , 17, 40), 750);
			idleanimleft  = new Animation(getSpriteSheet("PixelArt/SpriteSheets/CaraIdle-left.png", 17, 40), 750);
		}
		if(xvel < 0)
		{
			return idleanimleft;
		}
		return idleanimright;
	}

	public void jump() throws SlickException
	{
		if(onground)
		{
			yvel = -1.25f;
			onground = false;
		}
	}
	public void moveLeft() throws SlickException
	{
		xvel = -0.5f;
	}
	
	public void moveRight() throws SlickException
	{
		xvel = 0.5f;
	}

	@Override
	public void update(GameContainer gc, int i, ArrayList<GameObject> objects) throws SlickException
	{
		getWalkAnimation().update(i);
		getIdleAnimation().update(i);
		xvel *= 0.9f;
		yvel += GRAVITY*i;
		x += xvel*i;
		y += yvel*i;
		if(y > Main.HEIGHT-getWalkAnimation().getHeight())
		{
			y = Main.HEIGHT-getWalkAnimation().getHeight();
			yvel = 0;
			onground = true;
		}
		if(x > Main.WIDTH)
		{
			x = -getWalkAnimation().getWidth();
		}
		else if(x < -getWalkAnimation().getWidth())
		{
			x = Main.WIDTH;
		}

		for(GameObject o: objects)
		{
			CollisionSide side = collide(o);
			if(side != null)
			{
				switch(side)
				{
				case BOTTOM:
					onground = true;
					yvel = o.yvel;
					break;
				}
			}
		}
	}
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		if(Math.abs(xvel) < 0.001 && Math.abs(yvel) < 0.001)
		{
			getIdleAnimation().draw(x, y);
		}
		else
		{
			getWalkAnimation().draw(x, y);
		}
	}

	@Override
	public Shape getShape() throws SlickException
	{
		return new Rectangle(x, y, walkanimright.getWidth(), walkanimright.getHeight());
	}
}

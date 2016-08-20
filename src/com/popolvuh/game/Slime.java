package com.popolvuh.game;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import com.popolvuh.game.GameObject.CollisionSide;

public class Slime extends GameObject
{
	private static final float JUMP_STRENGTH = -0.75f;
	public Slime(float x, float y) throws SlickException
	{
		super(x, y);
	}

	private Animation idle = null;
	private Animation getIdleAnimation() throws SlickException
	{
		if(idle == null)
		{
			idle = new Animation(getSpriteSheet("PixelArt/SpriteSheets/BlueSlime.png", 25, 11), 500);
		}
		return idle;
	}

	@Override
	public void update(GameContainer gc, int i, ArrayList<GameObject> objects) throws SlickException
	{
		getIdleAnimation().update(i);
		yvel += GRAVITY*i;
		x += xvel*i;
		y += yvel*i;
		if(y > Main.HEIGHT-getIdleAnimation().getHeight())
		{
			y = Main.HEIGHT-getIdleAnimation().getHeight();
			yvel = JUMP_STRENGTH;
			xvel += 0.1;
		}
		if(x > Main.WIDTH)
		{
			x = -getIdleAnimation().getWidth();
		}
		else if(x < -getIdleAnimation().getWidth())
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
					yvel = JUMP_STRENGTH;
					xvel += 0.1;
					break;
				case LEFT:
					if(xvel > 0)
					{
						xvel = o.xvel;
					}
					else
					{
						xvel *= -1;
					}
					break;
				case RIGHT:
					if(xvel < 0)
					{
						xvel = o.xvel;
					}
					else
					{
						xvel *= -1;
					}
					break;
				}
			}
		}
	}
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		getIdleAnimation().draw(x, y);
	}

	@Override
	public Shape getShape() throws SlickException
	{
		return new Rectangle(x, y, getIdleAnimation().getWidth(), getIdleAnimation().getHeight());
	}
}

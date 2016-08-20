package com.popolvuh.game;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

public class Main extends BasicGame
{
	public static final int WIDTH = 854, HEIGHT = 630;
	public static void main(String[] args)
	{
		try
		{
			AppGameContainer appgc;
			appgc = new AppGameContainer(new Main("Popol Vuh"));
			appgc.setDisplayMode(WIDTH, HEIGHT, false);
			appgc.start();
		}
		catch(SlickException ex)
		{
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private Player player;
	private ArrayList<GameObject> objects = new ArrayList<>();

	public Main(String gamename) throws SlickException
	{
		super(gamename);
	}

	boolean jump, left, right, up, down;
	@Override
	public void keyPressed(int key, char c)
	{
		switch(key)
		{
		case Input.KEY_SPACE:
			jump = true;
			break;
			
		case Input.KEY_A:
			left = true;
			break;
			
		case Input.KEY_D:
			right = true;
			break;
			
		case Input.KEY_W:
			up = true;
			break;
			
		case Input.KEY_S:
			down = true;
			break;
		}
	}

	@Override
	public void keyReleased(int key, char c)
	{
		switch(key)
		{
		case Input.KEY_SPACE:
			jump = false;
			break;
			
		case Input.KEY_A:
			left = false;
			break;
			
		case Input.KEY_D:
			right = false;
			break;
			
		case Input.KEY_W:
			up = false;
			break;
			
		case Input.KEY_S:
			down = false;
			break;
		}
	}

	@Override
	public void mouseReleased(int button, int x, int y)
	{
		if(gameover)
		{
			score = 0;
			scorevel = 0;
			lastplatform = HEIGHT;
			objects.clear();
			try
			{
				objects.add(player = new Player(150, 150));
			}
			catch(SlickException e)
			{
			}
			gameover = false;
		}
	}

	@Override
	public void init(GameContainer gc) throws SlickException
	{
		bricktile = new SpriteSheet(new SpriteSheet("PixelArt/SpriteSheets/Bricktile.png", 30, 20).getScaledCopy(3), 30*3, 20*3);
		gc.setAlwaysRender(true);
		new Sound("Cuban Sandwich.ogg").loop();
		objects.add(player = new Player(150, 150));
	}

	private float score = 0;
	private float scorevel = 0, scoreaccel = 0.00001f;
	private boolean gameover = false;
	private float lastplatform = HEIGHT;
	private static final float PLATFORM_SPACING = 192;
	@Override
	public void update(GameContainer gc, int i) throws SlickException
	{
		if(!gameover)
		{
			score += scorevel*i;
			scorevel += scoreaccel*i;

			while(-score < lastplatform - PLATFORM_SPACING)
			{
				lastplatform -= PLATFORM_SPACING;
				Platform p;
				objects.add(p = new Platform((float)Math.random() * WIDTH, lastplatform));
				objects.add(new Platform((float)Math.random() * WIDTH, lastplatform));
				if(Math.random() >= 0.60)
				{
					objects.add(new Slime(p.getShape().getMinX(), p.getShape().getMinY()-50));
				}
			}

			if(jump)
			{
				player.jump();
			}
			if(left)
			{
				player.moveLeft();
			}
			if(right)
			{
				player.moveRight();
			}
			for(GameObject o : objects)
			{
				o.update(gc, i, objects);
			}
		}

		if(player.getShape().getMinY() > -score + HEIGHT)
		{
			gameover = true;
		}
		else
		{
			for(int j = 0; j < objects.size(); )
			{
				GameObject o = objects.get(j);
				if(o.getShape().getMinY() > -score + HEIGHT)
				{
					objects.remove(o);
				}
				else
				{
					++j;
				}
			}
		}
	}

	SpriteSheet bricktile;
	private static final int TOWER_WIDTH = 2;
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		Color sky = new Color(0.34f, 0.73f, 0.98f);
		g.setBackground(sky);
		g.setColor(sky);
		g.drawRect(0, 0, gc.getWidth(), gc.getHeight());
		for(int i = 0; i < TOWER_WIDTH; ++i)
		{
			for(float j = -bricktile.getHeight() + (score % bricktile.getHeight()); j < HEIGHT; j += bricktile.getHeight())
			{
				bricktile.draw(WIDTH/2 + i * bricktile.getWidth(), j);
				bricktile.draw(WIDTH/2 + (-i-1) * bricktile.getWidth(), j);
			}
		}
		g.setColor(Color.black);
		g.drawString("Score: "+(int)score, 10, 50);
		if(gameover)
		{
			g.drawString("GAME OVER\n(click to restart)", WIDTH / 2, HEIGHT / 2);
		}
		g.translate(0, score);
		g.drawString("Climb the tower!\nA = left\nD = right\nSpace = jump!", WIDTH / 2, HEIGHT / 2 - 100);
		g.drawString("You can wrap around to the other side!", WIDTH / 2, -150);

		for(GameObject o : objects)
		{
			o.render(gc, g);
		}
	}
}

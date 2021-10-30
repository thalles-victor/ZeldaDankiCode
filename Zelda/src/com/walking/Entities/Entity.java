package com.walking.Entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.walking.main.Game;
import com.walking.world.Camera;

public class Entity {
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	protected BufferedImage sprite;
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(8*16, 0, 16, 16);
	public static BufferedImage WEAPON_EN	= Game.spritesheet.getSprite(7*16, 0, 16, 16);
	public static BufferedImage ENEMY_EN	= Game.spritesheet.getSprite(7*16, 16, 16, 16);
	public static BufferedImage BULLET_EN	= Game.spritesheet.getSprite(6*16, 16, 16, 16);
	public static BufferedImage GUN_RIGHT 	= Game.spritesheet.getSprite(130, 37, 9, 7);
	public static BufferedImage GUN_LEFT 	= Game.spritesheet.getSprite(149, 37, 9, 7);
	
	private int maskX, maskY, maskWidth, maskHeight;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskX = 0;
		this.maskY = 0;
		this.maskWidth = width;
		this.maskHeight = height;
	}
	
	public void setMask(int maskX, int maskY, int maskWidth, int maskHeight) {
		this.maskX = maskX;
		this.maskY = maskY;
		this.maskWidth = maskWidth;
		this.maskHeight = maskHeight;
	}
	public void setX(int newX) 
	{
		this.x = newX;
	}
	
	
	public void setY(int newY) 
	{
		this.y = newY;
	}
	//Return Variables in methods
	public int getX() {return (int)this.x;}
	public int getY() {return (int)this.y;}
	public int getWidth()  {return this.width;}
	public int getHeight() {return this.height;}
	
	
	public static boolean isColliding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX()+e1.maskX, e1.getY()+ e1.maskY, e1.maskWidth, e1.maskHeight);
		Rectangle e2Mask = new Rectangle(e2.getX()+e2.maskX, e2.getY()+ e2.maskY, e2.maskWidth, e2.maskHeight);
		
		return e1Mask.intersects(e2Mask);
	}
	//Methods for render
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX()-Camera.x, this.getY()-Camera.y, null);
		//g.setColor(Color.red);
		//g.fillRect(this.getX() + maskX -Camera.x,this.getY() + maskY - Camera.y, maskWidth, maskHeight);
	}
	public void tick() {
		
	}
	
}

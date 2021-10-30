package com.walking.Entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.walking.main.Game;
import com.walking.world.Camera;

public class BulletShoot extends Entity{
	private double dx;
	private double dy;
	private double spd = 10;
	private int bulletLife = 50, curLife=0;
	
	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}

	public void tick() 
{

		x+=dx*spd;
		y+=dy*spd;
		curLife++;
		if(curLife == bulletLife) 
		{
			curLife=0;
			Game.bullets.remove(this);
			return;
		}

}
	
	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(	this.getX()-Camera.x, this.getY()-Camera.y, width, height);
	}
}

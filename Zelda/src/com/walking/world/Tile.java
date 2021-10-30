package com.walking.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.walking.main.Game;

public class Tile {
	//Static for initialized only once
	public static BufferedImage TILE_FLOR = Game.spritesheet.getSprite(0, 0, 16, 16);
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(16, 0, 16, 16);
	
	public BufferedImage sprite;
	private int x, y;
	
	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g){
		g.drawImage(sprite, x-Camera.x, y-Camera.y, null);
	}
}

package com.walking.Entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.walking.main.Game;
import com.walking.world.Camera;
import com.walking.world.World;

public class Player extends Entity{
	public boolean right, left, up, down;
	private double speedPlayer=1.6;
	
	private int frames=0, maxFrames=5, index=0, maxIndex=3;
	public boolean moved;
	public 	int right_dir = 0, left_dir=1;
	public 	int dir = right_dir;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	public double life = 10, maxLife = 100, saveLife=0;
	public static int ammo = 0;
	public static boolean shoot = false, mouseShoot = false;
	public static boolean hasGun = false;
	public static int mx, my;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		

	//sprite___________________________________________________________________________	
		rightPlayer = new BufferedImage[5];
		leftPlayer	= new BufferedImage[5];
		rightPlayer[0] = Game.spritesheet.getSprite(32, 0, 16, 16);
		for(int i=0; i<5; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32+(i*16), 0, 16, 16);
		}
		for(int i=0; i<5; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32+(i*16), 16, 16, 16);
		}
	//__________________________________________________________________________________	
	}
	public void tick() 
	{
	//Animation of the Player _____________________
		moved = false;
		if(right && World.isFree((int)(x+speedPlayer), this.getY())) {
			moved = true;
			dir=right_dir;
			x+=speedPlayer;
		}
		if(left && World.isFree((int)(x-speedPlayer), this.getY())) {
			moved = true;
			dir=left_dir;
			x-=speedPlayer;
		}
		if(down && World.isFree(this.getX(), (int)(y+speedPlayer))) {
			moved = true;
			y+=speedPlayer;
		}
		if(up && World.isFree(this.getX(), (int)(y-speedPlayer))) {
			moved = true;
			y-=speedPlayer;
		}
		if(moved) {
			frames++;
				if(frames >= maxFrames) {
					frames = 0;
					index++;
						if(index > maxIndex) {
							index=0;
						}
				}
		}
		checkCollisionLife();
		checkCollisionAmmo();
		checkCollisionWeapon();
		
		
		if(life<=0)
		{
			//Game over
			Game.gameState = "GAME_OVER";
			//Game.newLevel();
		}
		
		Camera.x = Camera.clamp(this.getX()- (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY()- (Game.HEIGHT/2), 0, World.HEIGHT*16- Game.HEIGHT);
		
		if(hasGun && ammo>0) 
		{
			if(shoot) 
			{//Acabou de Atirar
				ammo--;
				shoot = false;
				int dx = 0;
				int px = 0;
				if(dir==right_dir) { dx=1; px=19;}
				else { dx = -1; px=-8;}
				BulletShoot bullet = new BulletShoot(this.getX()+px, this.getY()+8, 3, 3, null, dx, 0);
				Game.bullets.add(bullet);
				
			}
		}

		//_________________________________________________________________________
	}
	public void checkCollisionWeapon() {
		for(int i=0; i<Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Weapon) {
				if(Entity.isColliding(this, e)) {
					Game.entities.remove(i);
					hasGun = true;
					return;
					}
					
				}
			}
		}
	
	public void checkCollisionAmmo() {
		for(int i=0; i<Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Bullet) {
				if(Entity.isColliding(this, e)) {
					Game.entities.remove(i);
					ammo+=100;	
					return;
					}
					
				}
			}
		}
	
	public void checkCollisionLife() {
		for(int i=0; i<Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(life<=90 && saveLife >0) {life+=10; saveLife--;}
			if(e instanceof Life) {
				if(Entity.isColliding(this, e)) {
					if(life == 100) saveLife++;
					Game.entities.remove(i);
					life+=10;
					if(life>=100) {
						life = 100;
						return;
					}
				}
			}
		}
	}
	public void render(Graphics g) {
		if(dir==right_dir){
			g.drawImage(rightPlayer[index], this.getX()-Camera.x, this.getY()-Camera.y, null);
			if(hasGun) 
				g.drawImage(GUN_RIGHT, this.getX()+10-Camera.x, this.getY()+7-Camera.y, null);
		}
		else if(dir==left_dir) {
			g.drawImage(leftPlayer[index], this.getX()-Camera.x, this.getY()-Camera.y, null);
			if(hasGun) 
				g.drawImage(GUN_LEFT, this.getX()-2-Camera.x, this.getY()+7-Camera.y, null);
		}
	}

}

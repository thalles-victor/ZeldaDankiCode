package com.walking.Entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.walking.Graphics.UI;
import com.walking.main.Game;
import com.walking.world.Camera;
import com.walking.world.World;


public class Enemy extends Entity{
	private double speedEnemy = 0.3;
	private int maskx = 8, masky= 8, maskWidth=10, maskHeight=10;
	private int frames=0, maxFrames=20, index=0, maxIndex=1;
	private BufferedImage[] sprites;
	private int lifeEnemy = 10;
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[3];
		sprites[0] = Game.spritesheet.getSprite(7*16, 16, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(8*16, 16, 16, 16);
		sprites[2] = Game.spritesheet.getSprite(9*16, 16, 16, 16);
	}
	public void tick() 
	{
	 
		maskx=8;
		masky=8;
		maskWidth=4;
		maskHeight=4;
		if(!this.ifCollidingWhithPlayer()) {
				if((int)x < Game.player.getX() && World.isFree((int)(x+speedEnemy), this.getY())
						 && !isColliding((int)(x+speedEnemy), this.getY())) {
					x = x +speedEnemy;
				}
				if((int)x > Game.player.getX() && World.isFree((int)(x-speedEnemy), this.getY())
						 && !isColliding((int)(x-speedEnemy), this.getY())) {
					x = x - speedEnemy;
				}
				if((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y+speedEnemy))
						 && !isColliding(this.getX(), (int)(y+speedEnemy))) {
					y = y + speedEnemy;
				}
				if((int)y > Game.player.getY() && World.isFree(this.getX(), (int)(y-speedEnemy))
						&& !isColliding(this.getX(), (int)(y-speedEnemy))) {
					y = y - speedEnemy;
				}
				frames++;
				if(frames >= maxFrames) {
					frames = 0;
					index++;
					if(index > maxIndex) {
					index=0;
					}
				}
		}else {
			index = 2;//Altera a Cor do Inimigo
			//Estamos colidindo
			if(Game.rand.nextInt(100)<5)
				Game.player.life-=Game.rand.nextInt(5);
		}
		collidingBullet();
		if(lifeEnemy<=0) {
			destroySelf();
		}
	}
	public void destroySelf() {
		Game.entities.remove(this);
		Game.enemies.remove(this);
			if(Game.enemies.size()<=0) {
				Game.currentLevel++;
				
				if(Game.currentLevel<=2)Game.newLevel();
				else System.out.println("Você Ganhou"); UI.wins = true;
				
			}
	}
	public void collidingBullet() {
		for(int i=0; i<+Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			if(e instanceof BulletShoot) {
				if(Entity.isColliding(this, e)) {
					Game.bullets.remove(i);
					lifeEnemy-=Game.rand.nextInt(50);
					return;
				}
			}
		}
	}
	public boolean ifCollidingWhithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX(), this.getY(), maskWidth, maskHeight);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		
		
		return enemyCurrent.intersects(player);
	}
	
	public boolean isColliding(int xNext, int yNext) {
		Rectangle enemyCurrent = new Rectangle(xNext + maskx, yNext+ masky, maskWidth, maskHeight);
		for(int i=0; i<Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if(e==this) continue;
			Rectangle targetEnemy = new Rectangle(e.getX() + maskx,e.getY() + masky, maskWidth, maskHeight);
			if(enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		return false;
	}
	
	//Cria Mascara de Colisao
	public void render(Graphics g) {
		g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
}

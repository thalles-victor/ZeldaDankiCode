package com.walking.Graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.walking.Entities.Player;
import com.walking.main.Game;



public class UI {
	public static boolean wins = false;
	public void render(Graphics g) {
		
		g.setColor(Color.red);
		g.fillRect(20, 8, 50, 8);
		g.setColor(Color.green);
		g.fillRect(20, 8,(int) ((Game.player.life/Game.player.maxLife)*50), 8);
		g.setColor(Color.darkGray);
		g.setFont(new Font("arial", Font.BOLD, 10));
		g.drawString((int)Game.player.life+"/"+(int)Game.player.maxLife, 25, 16);
		
		
		g.setFont(new Font("arial", Font.BOLD, 8));
		g.setColor(Color.white);
		g.drawString("FPS: "+Game.fpsRander, 2, 159);
		
		g.setFont(new Font("arial", Font.BOLD, 10));
		g.setColor(Color.white);
		g.drawString("Ammo: "+ Player.ammo, 110, 15);
		
		g.setFont(new Font("arial", Font.BOLD, 10));
		g.setColor(Color.white);
		g.drawString("Enemis: "+ Game.enemies.size(), 170, 15);
		
		g.setFont(new Font("arial", Font.BOLD, 9));
		g.setColor(Color.ORANGE);
		g.drawString("Life: "+ Game.player.saveLife, 70, 15);
		
	}

	
}

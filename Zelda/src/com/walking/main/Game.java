package com.walking.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.walking.Entities.BulletShoot;
import com.walking.Entities.Enemy;
import com.walking.Entities.Entity;
import com.walking.Entities.Player;
import com.walking.Graphics.UI;
import com.walking.Graphics.spriteSheet;
import com.walking.world.World;
public class Game extends Canvas implements Runnable, KeyListener, MouseListener
{	
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH =  240;
	public static final int HEIGHT = 160;
	public static final int SCALE =3;
	public static int currentLevel = 1;
	
	public static int fpsRander=0;
	
	private BufferedImage image;

	public static List<Entity> entities; // O Erro da antiga vers�o do jogo pode ter sido na hora
										// De importar Entity;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullets;
	
	public static Player player;
	
	public static World world;
	public static spriteSheet spritesheet;
	public static Random rand;
	
	public UI ui;
	
	public static String gameState = "GAME_OVER";
	public boolean showMessageGameOver = false;
	public char showMessageGameOverFrames=0;

	public Game() {
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		initFrame();
		
		ui = new UI();

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_BGR);
		
		//Create an new List of the Entities
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<BulletShoot>();
		//Take Sprite of the Player in spriteSheet.png with the size 16x16
		spritesheet = new spriteSheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16)); 
		entities.add(player);
		world = new World("/map"+currentLevel+".png");
	}
	public void initFrame() {
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		
		frame = new JFrame("Walking.engine");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}


	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}

	public void run() 
	{
		double amountOfTicks = 120.0;
		double frames  = 0;
		long lastTime = System.nanoTime();
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning) 
		{
			long now = System.nanoTime();
			delta+=(now - lastTime)/ns;
			lastTime = now;
			if(delta>= 1) 
			{	
				tick();
				render();
				frames++;
				delta--;
			}
			if(System.currentTimeMillis() - timer >= 1000)
			{
				fpsRander = (int)frames;
				frames = 0;
				timer+=1000;
			}
		}stop();
	}


	public void tick(){
		if(gameState == "NORMAL") 
		{
			for(int i=0; i<bullets.size(); i++) {
					bullets.get(i).tick();
				}
				for(int i=0; i<entities.size(); i++) {
					Entity e = entities.get(i);
					e.tick();
				}
		}else if(gameState == "GAME_OVER") {
			System.out.println("Voc� perdeu");
		}
	}
	public void render(){		
		BufferStrategy bs = this.getBufferStrategy();

		
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		world.render(g);
		for(int i=0; i<entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for(int i=0; i<bullets.size(); i++) {
			bullets.get(i).render(g);
		}
	//	Rever as Aulas disso
		ui.render(g);
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);		
		
		
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,200));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial", Font.BOLD, 28));
			g.drawString("Voc� perdeu TAPADO", WIDTH*SCALE/2-130, HEIGHT*SCALE/2-30);
			
			showMessageGameOverFrames++;
			if(showMessageGameOverFrames==60) {
				showMessageGameOverFrames = 0;
				if(showMessageGameOver) {
					showMessageGameOver = false;
					g.drawString("Aperte Enter para tentar novamente", WIDTH*SCALE/2-220,  HEIGHT*SCALE/2-5);
				}
				if(!showMessageGameOver) showMessageGameOver = true;
			}
		}
		
		
		bs.show();	//Sempre tem que ficar no Final do M�todo Rader
	}
	public static void newLevel() {
			player.life = 100;
			Player.ammo =0;
			Player.hasGun = false;
			Game.enemies.clear();
			Game.entities.clear();
			Game.entities = new ArrayList<Entity>();
			Game.enemies = new ArrayList<Enemy>();
			//Take Sprite of the Player in spriteSheet.png with the size 16x16
			Game.entities.add(Game.player);
			Game.world = new World("/" + "map"+currentLevel+".png");
	}
	

	public void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void keyTyped(KeyEvent e) {
		
	}
	//
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) 
		{
			//if the key pressed is Right or D
			//System.out.println("Right");
			player.right = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) 
		{
			//if the key pressed is Left or A
			//System.out.println("Left");
			player.left = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) 
		{
			//if the key pressed is Up or W
			//System.out.println("Up");
			player.up = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) 
		{
			//if the key pressed is Down or S
			//System.out.println("Down");
			player.down = true;
		}
			
		if(e.getKeyCode() == KeyEvent.VK_CONTROL && Player.ammo>0) 
		{
			Player.shoot= true;
		}
		
	}
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			//if the key pressed is Right or D
			player.right = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			//if the key pressed is Left or A
			player.left = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			//if the key pressed is Up or W
			player.up = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			//if the key pressed is Down or S
			player.down = false;
			

		}
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if(Player.hasGun && Player.ammo>0) Player.mouseShoot =true;
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}

	

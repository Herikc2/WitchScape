package com.bakerystudios.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bakerystudios.engine.Renderable;
import com.bakerystudios.engine.Updateble;
import com.bakerystudios.engine.graphics.engine.Spritesheet;
import com.bakerystudios.engine.graphics.engine.Tile;
import com.bakerystudios.engine.graphics.engine.World;
import com.bakerystudios.entities.Boy;
import com.bakerystudios.entities.Diario;
import com.bakerystudios.entities.Entity;
import com.bakerystudios.entities.EventManager;
import com.bakerystudios.entities.Player;
import com.bakerystudios.entities.Witch;
import com.bakerystudios.game.input.DebugInput;
import com.bakerystudios.game.input.Input;
import com.bakerystudios.game.input.MenuInput;
import com.bakerystudios.game.input.PlayerInput;
import com.bakerystudios.game.screen.Screen;
import com.bakerystudios.gui.GraphicUserInterface;
import com.bakerystudios.gui.TextBox;
import com.bakerystudios.inventario.Inventario;
import com.bakerystudios.sound.AudioManager;
import com.bakerystudios.teleport.Teleport;

public class Game implements Runnable, Renderable, Updateble {

	public static final int MAP = 0;
	public static final int SEC_FLOOR = 1;
	public static final int DUNGEON = 2;
	public static int CUR_MAP = MAP;
	
	public static int stateIntro = 0;
	public static boolean enter;
	
	private boolean isRunning;

	private Thread thread;
	private Screen screen;
	private List<Input> inputs = new ArrayList<>();
	
	public static Font boxFont;
	public static Font menuFont;
	public static Font inventFont;
	public static InputStream boxFontStream = ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
	public static InputStream menuFontStream = ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
	public static InputStream inventFontStream = ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");

	public static Random rand;

	private BufferedImage frame;
	private GraphicUserInterface gui;

	private AudioManager audio;

	public static Player player;
	
	public static List<Teleport> teleport;

	public static Spritesheet spritesheet;
	public static Spritesheet characters;
	public static Spritesheet doors;
	public static Spritesheet wall;
	public static EventManager em;
	public static List<World> world;
	public static List<Entity> entities;
	public static Inventario inventario;

	public static boolean gameEvent = false;
	public static boolean uiDoor = false;
	public static boolean uiChest = false;
	public static boolean uiNpc = false;
	public static boolean uiPlaca = false;
	public static boolean uiLivro = false;
	public static boolean uiAlavanca = false;
	
	public static Diario diario;
	
	public static boolean EXIT = false;

	public Game() {
		// Object instantiation
		
		// carregamento dos inputs
		inputs = new ArrayList<>();
		inputs.add(new MenuInput());
		inputs.add(new PlayerInput());
		inputs.add(new DebugInput());
		screen = new Screen(inputs);
		
		// carregamento das fontes
		loadFonts();
		
		// cria��o dos teleports
		createTeleports();
		
		// outros carregamentos
		rand = new Random();
		frame = new BufferedImage(Screen.WIDTH, Screen.HEIGHT, BufferedImage.TYPE_INT_RGB);
		em = new EventManager();
		
		gui = new GraphicUserInterface();
		audio = new AudioManager();
		
		// carregamento das sprites
		spritesheet = new Spritesheet("/sprites/spritesheet.png");
		characters = new Spritesheet("/sprites/characters.png");
		doors = new Spritesheet("/sprites/doors.png");
		wall = new Spritesheet("/sprites/wall.png");
		
		inventario = new Inventario(); // Precisou
		player = new Player(0, 0, Tile.SIZE, Tile.SIZE, null);
		entities = new ArrayList<Entity>();

		// carregamento dos mapas
		world = new ArrayList<>();
		world.add(new World("/levels/map.png", "/levels/map_collision.png"));
		

		// carregamento das entidades
		entities.add(player);
		entities.add(new Witch(272, 272, Tile.SIZE, Tile.SIZE, null));
		entities.add(new Boy(432, 608, 16, 16, characters.getSprite(48, 128, 16, 16)));
		diario = new Diario();
	}
	
	public void createTeleports() {
		teleport = new ArrayList<>();
		// casa > segundo andar
		teleport.add(new Teleport(416, 544, Tile.SIZE, Tile.SIZE, 1344, 752, Player.LEFT_DIR));
		// segundo andar > casa
		teleport.add(new Teleport(1360, 752, Tile.SIZE, Tile.SIZE, 400, 544, Player.LEFT_DIR));
		// baixo > cima
		teleport.add(new Teleport(400, 320, Tile.SIZE, Tile.SIZE, 400, 208, Player.UP_DIR));
		// cima > baixo
		teleport.add(new Teleport(400, 224, Tile.SIZE, Tile.SIZE, 400, 336, Player.DOWN_DIR));
		// casa > calabou�o
		teleport.add(new Teleport(416, 432, Tile.SIZE, Tile.SIZE, 1152, 288, Player.RIGHT_DIR));
		// calabou�o > casa
		teleport.add(new Teleport(1136, 288, Tile.SIZE, Tile.SIZE, 400, 432, Player.LEFT_DIR));
	}
	
	public void loadFonts() {
		try {
			boxFont = Font.createFont(Font.TRUETYPE_FONT, boxFontStream).deriveFont(Font.PLAIN, 20);
			menuFont = Font.createFont(Font.TRUETYPE_FONT, menuFontStream).deriveFont(Font.PLAIN, 40);
			inventFont = Font.createFont(Font.TRUETYPE_FONT, inventFontStream).deriveFont(Font.PLAIN, 15);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}

	public synchronized void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		gui.update();
		em.update();
		audio.update();

		if(enter) {
			stateIntro++;
			enter = false;
		}
		
		if (GameState.state == GameState.PLAYING) {
			diario.update();
			for(Teleport t : teleport) {
				t.update();
			}
			for (Entity e : entities) {
				e.update();
			}
			inventario.update();
		} else if (GameState.state == GameState.INTRO) {
			if(stateIntro >= 3) {
				GameState.state = GameState.PLAYING;
			}
		} else if (GameState.state == GameState.OVER) {
			
		}
		
		if(EXIT)
			System.exit(0);
	}

	private void nonPixelatedRender(Graphics g) {
		gui.render(g);
		inventario.render(g);
		
		if(Witch.end) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 1280, 720);
			g.setColor(Color.WHITE);
			g.setFont(Game.boxFont);
			g.drawString("E o massacre das crian�as continua...", Screen.SCALE_WIDTH / 2 - g.getFontMetrics().stringWidth("E o massacre das crian�as continua...") / 2, 300);
		}
		
		if(GameState.state == GameState.INTRO) {
			if(stateIntro == 0) {
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, 1280, 720);
				g.setColor(Color.WHITE);
				g.setFont(boxFont);
				g.drawString("Em um certo dia eu estava explorando a floresta negra,", Screen.SCALE_WIDTH / 2 - g.getFontMetrics().stringWidth("Em um certo dia eu estava explorando a floresta negra,") / 2, 300);
				g.drawString("At� que eu me deparei um um garoto.", Screen.SCALE_WIDTH / 2 - g.getFontMetrics().stringWidth("At� que eu me deparei um um garoto.") / 2, 350);
			} else if(stateIntro == 1) {
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, 1280, 720);
				g.setColor(Color.WHITE);
				g.setFont(boxFont);
				g.drawString("Ele parecendo muito triste me pediu um favor,", Screen.SCALE_WIDTH / 2 - g.getFontMetrics().stringWidth("Ele parecendo muito triste me pediu um favor,") / 2, 300);
				g.drawString("Para que eu pegasse sua bola dentro da casa de uma senhora.", Screen.SCALE_WIDTH / 2 - g.getFontMetrics().stringWidth("Para que eu pegasse sua bola dentro da casa de uma senhora.") / 2, 350);
			} else if(stateIntro == 2) {
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, 1280, 720);
				g.setColor(Color.WHITE);
				g.setFont(boxFont);
				g.drawString("Eu nem imaginava o que estava por vir...", Screen.SCALE_WIDTH / 2 - g.getFontMetrics().stringWidth("Eu nem imaginava o que estava por vir...") / 2, 325);
			}
		}
		
		for(Entity entity : entities) {
			if(entity instanceof Witch) {
				if(((Witch) entity).isSeeingPlayer())
					TextBox.showDialog(g, boxFont, "Ei! O que voc� est� fazendo aqui?", null, null, false, true);
				break;
			}
		}

		if(player.florestEvent) {
			TextBox.showDialog(g, boxFont, "N�o quero voltar para a floresta,", "j� andei muito hoje, vou procurar um local seguro.", null, false, true);
		}
		
//		g.setColor(Color.RED);
//		g.setFont(new Font("arial", Font.PLAIN, 15));
//		g.drawString("x: " + player.getX() + " y: " + player.getY(), 1100, 23);
	}

	private void pixelatedRender(Graphics g) {
		world.get(CUR_MAP).render(g);
		for (Entity e : entities)
			e.render(g);
	}

	@Override
	public void render(Graphics g) {
		BufferStrategy bs = screen.getBufferStrategy();
		if (bs == null) {
			screen.createBufferStrategy(3);
			return;
		}

		g = frame.getGraphics();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Screen.WIDTH, Screen.HEIGHT);

		pixelatedRender(g);

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(frame, 0, 0, Screen.SCALE_WIDTH, Screen.SCALE_HEIGHT, null);
		
		nonPixelatedRender(g);
		
		bs.show();
	}

	@Override
	public void run() {
		double amountOfTicks = 60.0;
		double ns = 1000000000.0 / amountOfTicks;
		double delta = 0;
		long lastTime = System.nanoTime();

		screen.requestFocus(); 
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			if (delta >= 1) {
				update();
				render(null);
				delta--;
			}
		}

		stop();
	}

	public Spritesheet getSpritesheet() {
		return spritesheet;
	}

	public void setSpritesheet(Spritesheet spritesheet) {
		Game.spritesheet = spritesheet;
	}

	public static Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		Game.player = player;
	}

}

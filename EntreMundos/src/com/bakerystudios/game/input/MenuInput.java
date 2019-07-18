package com.bakerystudios.game.input;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.bakerystudios.entities.Anotacao;
import com.bakerystudios.entities.Chest;
import com.bakerystudios.entities.Door;
import com.bakerystudios.entities.Entity;
import com.bakerystudios.entities.Esqueleto;
import com.bakerystudios.entities.Player;
import com.bakerystudios.entities.Princesa;
import com.bakerystudios.game.Game;
import com.bakerystudios.game.GameState;
import com.bakerystudios.gui.menu.MainMenu;
import com.bakerystudios.gui.menu.engine.Menu;
import com.bakerystudios.gui.menu.engine.MenuState;

public class MenuInput extends Input {

	@Override
	public void keyPressed(KeyEvent e) {
		// BASIC KEYS
		// ---------------------------------------------------------------------
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {

		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			if (GameState.state == GameState.MENU) {
				Menu.up = true;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			if (GameState.state == GameState.MENU) {
				Menu.down = true;
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (GameState.state == GameState.MENU) {
				Menu.enter = true;
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (GameState.state == GameState.PLAYING && !Player.inEvent) {
				for (Entity atual : Game.entities) {
					if (atual instanceof Door) {
						if (((Door) atual).getTryAnimation() && !((Door) atual).getAnimation()) {
							((Door) atual).setAnimation(false);
							((Door) atual).setTryAnimation(false);							
							break;
						}
					}
					if (atual instanceof Chest) {
						if (((Chest) atual).isTryAnimation() && !((Chest) atual).isAnimation()) {
							((Chest) atual).setAnimation(false);
							((Chest) atual).setTryAnimation(false);							
							break;
						}
					}
					if (atual instanceof Anotacao) {
						if(((Anotacao) atual).isStatusEventoAnotacao()) {
							((Anotacao) atual).setStatusEventoAnotacao(false);
							break;
						}
					}
					if(atual instanceof Princesa) {
						if(((Princesa) atual).isTryEventActivePrincesa()) {
							((Princesa) atual).setTryEventActivePrincesa(false);
						}
					}
					if(atual instanceof Esqueleto) {
						if(((Esqueleto) atual).isTryEventActiveEsqueleto()) {
							((Esqueleto) atual).setTryEventActiveEsqueleto(false);
						}
					}
				}
				Game.uiDoor = false;
				Game.uiChest = false;
				Game.uiNpc = false;
				
				GameState.state = GameState.MENU;
				MenuState.state = MenuState.PAUSE;
			}
		}

		// OTHER KEYS
		// ---------------------------------------------------------------------

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// BASIC KEYS
		// ---------------------------------------------------------------------
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {

		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			if (GameState.state == GameState.MENU) {
				Menu.up = false;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			if (GameState.state == GameState.MENU) {
				Menu.down = false;
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (GameState.state == GameState.MENU) {
				Menu.enter = false;
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
//			if(GameState.state == GameState.PLAYING) {
//				GameState.state = GameState.MENU;
//			}
		}

		// OTHER KEYS
		// ---------------------------------------------------------------------

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
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

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}

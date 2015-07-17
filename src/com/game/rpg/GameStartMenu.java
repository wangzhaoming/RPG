package com.game.rpg;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JPanel;

/**
 * 显示游戏开始选项菜单
 * 
 * 选项包括option=0 继续游戏
 * 
 * option=1 开始新游戏
 * 
 * @author Administrator
 * 
 */

public class GameStartMenu extends JPanel implements KeyListener {
	private int option;// 存放选择的选项
	private GameStart game;// 将GameStart传入，用于调用其中的方法

	public GameStartMenu(GameStart game) {
		option = 0;// 缺省选项为继续
		this.game = game;
		setBackground(Color.black);
		setSize(640, 480);
		addKeyListener(this);
	}

	/**
	 * 绘制菜单
	 */
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		g.setColor(Color.white);
		g.fillRoundRect(0, 0, 640, 100, 30, 30);
		g.setColor(Color.black);
		Font font = new Font("宋体", Font.BOLD, 25);
		g.setFont(font);
		g.drawString("Continue", 40, 35);
		g.drawString("Start a new game", 40, 80);
		g.drawString("→", 10, 35 + option * 45);
	}

	/**
	 * 监听器
	 * 
	 * 上下键选择
	 * 
	 * a键确定
	 * 
	 * @param e
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			option = 0;
			repaint();
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			option = 1;
			repaint();
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			if (option == 0) {
				// 继续游戏
			} else if (option == 1) {
				try {
					game.startGame();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}// 开始新游戏
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}

package com.game.rpg;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JPanel;

/**
 * ��ʾ��Ϸ��ʼѡ��˵�
 * 
 * ѡ�����option=0 ������Ϸ
 * 
 * option=1 ��ʼ����Ϸ
 * 
 * @author Administrator
 * 
 */

public class GameStartMenu extends JPanel implements KeyListener {
	private int option;// ���ѡ���ѡ��
	private GameStart game;// ��GameStart���룬���ڵ������еķ���

	public GameStartMenu(GameStart game) {
		option = 0;// ȱʡѡ��Ϊ����
		this.game = game;
		setBackground(Color.black);
		setSize(640, 480);
		addKeyListener(this);
	}

	/**
	 * ���Ʋ˵�
	 */
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		g.setColor(Color.white);
		g.fillRoundRect(0, 0, 640, 100, 30, 30);
		g.setColor(Color.black);
		Font font = new Font("����", Font.BOLD, 25);
		g.setFont(font);
		g.drawString("Continue", 40, 35);
		g.drawString("Start a new game", 40, 80);
		g.drawString("��", 10, 35 + option * 45);
	}

	/**
	 * ������
	 * 
	 * ���¼�ѡ��
	 * 
	 * a��ȷ��
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
				// ������Ϸ
			} else if (option == 1) {
				try {
					game.startGame();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}// ��ʼ����Ϸ
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

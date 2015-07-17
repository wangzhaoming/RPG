package com.game.rpg;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * ���ſ���������panel
 * 
 * @author Administrator
 * 
 */
public class OpeningPanel extends JPanel implements Runnable, KeyListener {
	private String str;// ��������������Ϻ���ʾ���ַ���
	private GameStart game;// ���ڵ������еķ���
	private boolean stopped;// ���������Ƿ�ֹͣ������enter
	private ResourseReader reader;// ��ȡ��Դ����

	public OpeningPanel(GameStart game) throws IOException {
		this.game = game;
		reader = new ResourseReader();
//		stopped = false;
		stopped = true;
		this.setBackground(Color.black);
		setSize(640, 480);
		str = "";
		addKeyListener(this);
	}

	/**
	 * ���ƶ���������ַ���������ǰ�ַ���Ϊ�գ�������
	 */
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Font font = new Font("����", Font.BOLD, 50);
		g.setFont(font);
		g.setColor(Color.white);
		FontMetrics fm = g.getFontMetrics();// ���ڻ�ȡ�ַ����ĳ�����������������м�
		g.drawString(str, 320 - fm.stringWidth(str) / 2,
				275 - fm.getHeight() / 2);
	}

	/**
	 * �̵߳�ʵ��
	 * 
	 * ÿ��25֡���ٶȻ���ͼƬ�γɶ���
	 * 
	 * ����������Ϻ���ʾ�ַ���������str��ֵ
	 */
	@Override
	public void run() {
		Image img = null;
		for (int i = 0; i < 2196; i++) {
			try {
				img = reader.getImage("op/" + i + ".jpg");
			} catch (IOException e1) {
				e1.printStackTrace();
			}// ������Ӧ��ŵ�ͼƬ

			Graphics g = this.getGraphics();
			g.drawImage(img, 0, 60, 640, 360, null);// ����ͼƬ
			try {
				Thread.sleep(40);// �ȴ�40ms����ÿ�����25֡
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (stopped) {// ����enter��stoppedΪtrue��ֹͣ����
				break;
			}
		}
		while (!stopped) {// ���궯����û��enter������ʾ�ַ���
			str = "Press Enter";
			repaint();
			try {
				Thread.sleep(1000);// �ȴ�1s
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			str = "";// �γ���˸Ч��
			repaint();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("op is stopped");
	}

	/**
	 * enter���ļ���
	 * 
	 * @param e
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			stopped = true;// ����enter�󿪳����������뿪ʼ��Ϸѡ��
			game.showStartOption();
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

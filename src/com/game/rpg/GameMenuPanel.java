package com.game.rpg;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

/**
 * ��Ϸ�����˵��࣬�̳�JPanel
 * 
 * ��Ϸ�ڰ�enter������
 * 
 * @author Administrator
 * 
 */
public class GameMenuPanel extends JPanel implements KeyListener {
	private int option;//��¼�˵���ѡ��
	private Map map;//��map�ཻ��

	public GameMenuPanel(Map map) {
		option = 0;
		this.map = map;
		setBounds(540, 0, 100, 190);
		setOpaque(false);//panel����Ϊ͸����û�õ�������
		setVisible(false);
		addKeyListener(this);
	}

	/**
	 * ���Ʋ˵�
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.white);
		g.fillRect(0, 0, 100, 190);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(5));//���û��ʴ�ϸ
		g2.setColor(Color.BLUE);
		g2.drawRect(0, 0, 100, 190);
		Font font = new Font("����", Font.BOLD, 20);
		g.setFont(font);
		g.drawString("����", 25, 40);
		g.drawString("����", 25, 80);
		g.drawString("��Ϣ", 25, 120);
		g.drawString("ѡ��", 25, 160);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.5f));//���û��ʰ�͸����͸����0.5f
		g2.setColor(Color.gray);
		g2.fillRect(5, option * 40 + 15, 90, 35);//Ϊѡ�е�ѡ����Ӹ���
	}

	/**
	 * ���̵��¼�����
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {//enter���رղ˵���map��ȡ����
			map.requestFocus();
			this.setVisible(false);
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {//���¼��ı�˵�ѡ��
			option=Math.max(0, option-1);//ѡ��ֻ��0-3����ֹ���
			repaint();
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			option=Math.min(3, option+1);
			repaint();
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

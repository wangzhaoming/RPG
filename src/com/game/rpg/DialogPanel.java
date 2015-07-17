package com.game.rpg;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * �Ի�����
 * 
 * @author Administrator
 * 
 */
public class DialogPanel extends JPanel {
	private String str;// �Ի�������ʾ���ַ���

	public DialogPanel() {
		str = "";
		setBounds(0, 400, 640, 80);
		setOpaque(false);
		setVisible(false);
	}

	/**
	 * ���ƶԻ�
	 */
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);

		Font font = new Font("����", Font.BOLD, 50);// ���û��Ƶ�����
		g.setFont(font);
		g.setColor(Color.white);
		g.fillRoundRect(0, 0, 640, 80, 30, 30);// ���ƶԻ���
		g.setColor(Color.black);
		g.drawString(str, 50, 50);// ���ƶԻ�
	}

	/**
	 * ��ʾ�Ի��򣬲����ƴ����str
	 * 
	 * @param str
	 */
	public void open(String str) {
		this.str = str;
		this.setVisible(true);
		this.repaint();
	}

	/**
	 * �رնԻ���
	 */
	public void close() {
		this.setVisible(false);
	}
}

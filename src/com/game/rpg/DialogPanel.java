package com.game.rpg;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * 对话框类
 * 
 * @author Administrator
 * 
 */
public class DialogPanel extends JPanel {
	private String str;// 对话框内显示的字符串

	public DialogPanel() {
		str = "";
		setBounds(0, 400, 640, 80);
		setOpaque(false);
		setVisible(false);
	}

	/**
	 * 绘制对话
	 */
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);

		Font font = new Font("宋体", Font.BOLD, 50);// 设置绘制的字体
		g.setFont(font);
		g.setColor(Color.white);
		g.fillRoundRect(0, 0, 640, 80, 30, 30);// 绘制对话框
		g.setColor(Color.black);
		g.drawString(str, 50, 50);// 绘制对话
	}

	/**
	 * 显示对话框，并绘制传入的str
	 * 
	 * @param str
	 */
	public void open(String str) {
		this.str = str;
		this.setVisible(true);
		this.repaint();
	}

	/**
	 * 关闭对话框
	 */
	public void close() {
		this.setVisible(false);
	}
}

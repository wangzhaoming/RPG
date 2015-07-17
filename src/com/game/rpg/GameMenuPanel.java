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
 * 游戏内主菜单类，继承JPanel
 * 
 * 游戏内按enter键唤醒
 * 
 * @author Administrator
 * 
 */
public class GameMenuPanel extends JPanel implements KeyListener {
	private int option;//记录菜单的选项
	private Map map;//与map类交互

	public GameMenuPanel(Map map) {
		option = 0;
		this.map = map;
		setBounds(540, 0, 100, 190);
		setOpaque(false);//panel设置为透明，没用到的属性
		setVisible(false);
		addKeyListener(this);
	}

	/**
	 * 绘制菜单
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.white);
		g.fillRect(0, 0, 100, 190);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(5));//设置画笔粗细
		g2.setColor(Color.BLUE);
		g2.drawRect(0, 0, 100, 190);
		Font font = new Font("宋体", Font.BOLD, 20);
		g.setFont(font);
		g.drawString("背包", 25, 40);
		g.drawString("保存", 25, 80);
		g.drawString("信息", 25, 120);
		g.drawString("选项", 25, 160);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.5f));//设置画笔半透明，透明度0.5f
		g2.setColor(Color.gray);
		g2.fillRect(5, option * 40 + 15, 90, 35);//为选中的选项添加高亮
	}

	/**
	 * 键盘的事件监听
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {//enter键关闭菜单，map获取焦点
			map.requestFocus();
			this.setVisible(false);
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {//上下键改变菜单选项
			option=Math.max(0, option-1);//选项只有0-3，防止溢出
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

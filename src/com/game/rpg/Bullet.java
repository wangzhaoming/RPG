package com.game.rpg;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Bullet {
	private Map map;
	
	public Bullet(Map map) {
		this.map=map;
	}
	
	public void drawBullet(int row,int col,int range){
		Graphics2D graphics2d=(Graphics2D) map.getGraphics();
		graphics2d.setStroke(new BasicStroke(5));
		
		for(;range>0;){
		graphics2d.drawLine(col*32, row*32, col*32+32, row*32+32);}
	}
}

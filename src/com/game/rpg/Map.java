package com.game.rpg;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JPanel;

/**
 * ��Ϸ��Ҫ�����ʵ��
 * 
 * ������ͼ���л�����������ߣ��Ի���
 * 
 * @author Administrator
 * 
 */
public class Map extends JPanel implements KeyListener, Runnable {

	private int offsetX, offsetY;// ��ͼ���Ƶ�ƫ���������������������
	private int dirRow;// ��¼����ǰ���ķ���1�£�-1��,��λ����
	private int dirCol;// ����-1��1��,��
	private boolean dirChanged;// ��¼�����Ƿ�ı䣬����ǰ���µķ�������һ�ε��Ƿ���ͬ
	private final int SIZE = 32;// ��������ͼÿһ�񳤿�Ϊ32px
	private int[][][] map;// ��ŵ�ͼ�����飬���ļ�����
	private Image img;// ���Ƶ�ͼ���ز�
	private int mapWidth, mapHeight;// ��Ϸ��ͼ�Ĵ�С����¼�ڵ�ͼ�ļ���ǰ�����ֽ�
	private boolean isRun;// ���Ƿ����ߣ����Ƿ����˷����
	private boolean keyDown;// ������Ƿ񱻰��£����ڴ����������ߵ���ʱ������ϵͳ�԰�����repeat����
	private Unit unit;// �����������
	private String mapName;// ��¼��ǰ��ͼ������
	private long time;// �洢��ǰϵͳʱ�䣬����������ʱ��
	private ResourseReader reader;// ��ȡ��Դ����
	private GameMenuPanel gameMenu;// ��Ϸ�ڲ˵�
	private Monster monster;// �����������
	private DialogPanel dialogPanel;// ������ʾ�Ի���panel

	public Map() throws IOException {
		offsetX = offsetY = 0;// ȱʡƫ����
		dirRow = 1;// ȱʡ������
		dirCol = 0;
		dirChanged = false;
		mapName = "map00.txt";// ȱʡ����ĵ�ͼ�ļ�
		unit = new Unit();
		isRun = false;
		keyDown = false;
		reader = new ResourseReader();
		gameMenu = new GameMenuPanel(this);
		monster = new Monster(this);

		dialogPanel = new DialogPanel();

		setLayout(null);
		add(gameMenu);// ����Ϸ�ڲ˵������map�ϣ���ʼʱ���ɼ�
		add(dialogPanel);// ��ӶԻ��򣬳�ʼʱ���ɼ�
		img = reader.getImage("res/building.png");// �����ͼ�ز�

		addKeyListener(this);
		setSize(640, 480);

		map = setMapArray(reader.getInputStream("res/" + mapName));// �����ͼ�ļ�

		for (int i = 0; i < mapHeight; i++) {// ѭ�����ҳ�����
			for (int j = 0; j < mapWidth; j++) {
				for (int k = 0; k < 3; k++) {
					if ((map[i][j][k] & 0x20) == 0x20) {
						// �жϸõ��Ƿ�Ϊ����ĳ����㣬�������ʼ���ֵ�����
						// ������������ƫ������ʹ������λ����Ļ�����룬�������غ�
						// ÿ�ŵ�ͼ���ֻ��һ��������
						offsetX = -j * 32 + 320;
						offsetY = -i * 32 + 224;
					}
				}
			}
		}
	}

	/**
	 * ��ȡ��ͼ�ļ��ķ���,���õ�ͼ����
	 * 
	 * @param in
	 *            ��ŵ�ͼ�ļ���������
	 * 
	 * @return ���е�ͼ�ļ�������
	 * 
	 * @throws IOException
	 */
	public int[][][] setMapArray(InputStream in) throws IOException {
		mapHeight = in.read() + 1;
		mapWidth = in.read() + 1;
		int[][][] map = new int[mapHeight][mapWidth][3];

		for (int i = 0; i < mapHeight; i++) {
			for (int j = 0; j < mapWidth; j++) {
				for (int k = 0; k < 3; k++) {
					map[i][j][k] = in.read();
				}
			}
		}
		return map;
	}

	/**
	 * ȡ�õ�ͼ����
	 * 
	 * @return
	 */
	public int[][][] getMapArray() {
		return map;
	}

	/**
	 * �л�����ʱ�����µ�ͼ
	 * 
	 * ��ʱ�ĳ��������������ļ��ж�ȡ����ƫ�����ڶ�ȡ�����ļ�ʱ���ã�getMapName����
	 * 
	 * @throws IOException
	 */
	public void loadNewMap() throws IOException {
		try {
			setMapName();// ��ȡ�����õ�ͼ����
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		map = setMapArray(reader.getInputStream("res/" + mapName));// ��ȡ��ͼ
		monster.removeAllMonsters();// �л���ͼʱ��չ����б�
		for (int i = 0; i < mapHeight * mapWidth / 100; i++) {
			monster.create();// �л���ͼʱ����������һ��Ĺ�
		}
		repaint();
	}

	/**
	 * ��ȡ�����ļ�����ȡ�������л��ĵ�ͼ���ƣ�ͬʱ����ƫ��
	 * 
	 * ���úõĵ�ͼ���ƽ��ڶ�ȡ�µ�ͼʱ�õ�
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void setMapName() throws FileNotFoundException, IOException {
		Properties data = reader.getProperties("res/data"
				+ mapName.substring(3, 5) + ".txt");// ���ݵ�ǰ�ĵ�ͼ��������Ӧ�������ļ�
		String[] str = data.getProperty("jump_" + getPoint()).split(",");// ����������ҵ�Ҫ��ת�ĵ�ͼ��������λ�����ݣ��Զ��ŷָ�����ʽΪ��mapname,x,y��
		mapName = str[0];// �����µ�ͼ��
		int x = Integer.valueOf(str[1]);// ������x����
		int y = Integer.valueOf(str[2]);// ������y����
		offsetX = -y * 32 + 320;// ������������ƫ��
		offsetY = -x * 32 + 224;
	}

	/**
	 * ��ȡ��ͼ��ˢ����Ϣ
	 * 
	 * ���ݵ�ͼ����ȡ����
	 * 
	 * @return true ��ˢ��
	 * @throws IOException
	 */
	public boolean getCreateMonster() throws IOException {
		Properties data = reader.getProperties("res/data"
				+ mapName.substring(3, 5) + ".txt");
		String str = data.getProperty("createMonster");
		return str.equals("1") ? true : false;
	}

	/**
	 * ����ƫ�������ݣ����Ƶ�ͼʱ�õ�
	 * 
	 * ��ƫ��xΪ������ͼԭ�����������ԭ������Ϊ����������ԭ������ڵ�ͼԭ������Ϊ�����ӵ�ͼ��-x���꿪ʼ����
	 * 
	 * xΪ������ͼԭ�����������ԭ������Ϊ��������ͼԭ���������ڣ���Ȼ�ӵ�ͼԭ��0��ʼ����
	 * 
	 * @param x
	 * @return x>=0,����0,x<0,����-x/32(��ͼ���갴���м��㣬ÿ��ÿ��Ϊ32����)
	 */
	public int func1(int x) {
		return x >= 0 ? 0 : -x / 32;
	}

	/**
	 * ��ȡ���������λ�õı�־λ��Ϣ
	 * 
	 * ��Ϣ���Ե�ͼ����
	 * 
	 * @return ��־λ�����ݣ�int�ͣ�����8λ��Ч
	 */
	public int getFlag() {
		return map[7 + dirRow - offsetY / 32][10 + dirCol - offsetX / 32][2];
	}

	/**
	 * ��ȡ���������λ�ã������б�ʾ
	 * 
	 * @return �ַ�������8��5�б�ʾΪ��8_5��
	 */
	public String getPoint() {
		return String.valueOf(7 + dirRow - offsetY / 32) + "_"
				+ String.valueOf(10 + dirCol - offsetX / 32);
	}

	/**
	 * ��ʾ�Ի�����
	 * 
	 * ��ȡ�Ե�ͼ���Ӧ�������ļ�
	 * 
	 * @param g
	 * @return �Ի����ַ�����������ʾ�ڿ���̨�������ã���������;
	 * @throws IOException
	 */
	public String showDialog() throws IOException {
		Properties data = reader.getProperties("res/data"
				+ mapName.substring(3, 5) + ".txt");
		String str = data.getProperty("dialog_" + getPoint());// ���������ȡ�Ի�
		dialogPanel.open(str);// ���Ի����벢��ʾ
		return str;
	}

	/**
	 * �ƶ���ͼ�ķ���
	 */
	public void moveMap() {
		long lag = System.currentTimeMillis() - time;// ���㰴�¼���ʱ���뵱ǰʱ���ʱ������ĳһֵ����Ż��ƶ�������ֻת���ƶ�
		if (isRun == true && (lag > 100 || dirChanged == false)) {
			// �����ƶ��������������������&&(��������µ�ʱ�䳬��100����||�ƶ����򲻱�)
			// �����ػ�����150ms���ҷ���ı�ʱ�ĵ�һ���ػ�lag��ȻС��100ms����ʱֻ��ı䷽�򣬲����ƶ�
			// �ڶ����ػ�lag��Ȼ��С��150���ͻ��ƶ���lag��ֵȡ10~149�����ԣ�û����

			if ((map[7 + dirRow - offsetY / 32][10 + dirCol - offsetX / 32][2] & 0x80) == 0x80) {
				offsetX -= dirCol * 32;
				offsetY -= dirRow * 32;
			}// ���ݷ����ƶ�����ȡ��־λ���÷����ͨ��ʱ���ƶ����ƶ����Ǹı�ƫ����
		}
	}

	/**
	 * ���Ƶ�ͼ���������������ܶ�
	 */
	@Override
	protected void paintComponent(Graphics g) {
//		super.paintComponent(g);

		g.fillRect(0, 0, 640, 480);
		// ���ݵ�ͼ���ݶ�ȡ�زĿ�ʼ����
		for (int i = func1(offsetY); i < func1(offsetY) + 15 && i < mapHeight; i++) {
			for (int j = func1(offsetX); j < func1(offsetX) + 20
					&& j < mapWidth; j++) {
				g.drawImage(img, offsetX + j * SIZE, offsetY + i * SIZE,
						offsetX + j * SIZE + SIZE, offsetY + i * SIZE + SIZE,
						map[i][j][0] * SIZE, map[i][j][1] * SIZE, map[i][j][0]
								* SIZE + SIZE, map[i][j][1] * SIZE + SIZE, null);
			}
		}
		unit.move(g, dirRow, dirCol, isRun);// ���ݷ�������������ߵĶ���������unit�ķ���
		monster.drawMonster(g);// ���»��ƹ���
	}

	/**
	 * ��ȡx����ƫ����
	 * 
	 * @return
	 */
	public int getOffsetX() {
		return offsetX;
	}

	/**
	 * ��ȡy����ƫ����
	 * 
	 * @return
	 */
	public int getOffsetY() {
		return offsetY;
	}

	/**
	 * ���̼���
	 * 
	 * @param e
	 */
	public void keyPressed(KeyEvent e) {
		if (keyDown == false) {// û�м������²�ִ�У���ֹϵͳ�Զ��԰���repeat
			keyDown = true;// ��¼���̱�����
			time = System.currentTimeMillis();// ��¼�����µ�ʱ��
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if (dirCol != -1) {// ԭ����Ϊ1ʱ����ı�
					dirCol = -1;// ���÷���
					dirRow = 0;
					dirChanged = true;// ���÷���ı�
				} else {
					dirChanged = false;
				}
				isRun = true;
				dialogPanel.close();//�رնԻ�
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if (dirCol != 1) {
					dirCol = 1;
					dirRow = 0;
					dirChanged = true;
				} else {
					dirChanged = false;
				}
				isRun = true;
				dialogPanel.close();
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				if (dirRow != -1) {
					dirRow = -1;
					dirCol = 0;
					dirChanged = true;
				} else {
					dirChanged = false;
				}
				isRun = true;
				dialogPanel.close();
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				if (dirRow != 1) {
					dirRow = 1;
					dirCol = 0;
					dirChanged = true;
				} else {
					dirChanged = false;
				}
				isRun = true;
				dialogPanel.close();
			} else if (e.getKeyCode() == KeyEvent.VK_A) {// ����a�������µ��¼�
				System.out.println(getPoint());// ����̨�����Ե�����
				if ((getFlag() & 0x8) == 0x8) {// �ӱ�־λ�ж��Ƿ��жԻ�
					try {
						System.out.println(showDialog());// �жԻ�ʱ���ƶԻ������ӿ���̨���
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else if ((getFlag() & 0x10) == 0x10) {// �ӱ�־λ�жϴ˴��Ƿ��Ǵ��͵�
					try {
						loadNewMap();// �Ǵ��͵�������µ�ͼ
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {// enter��������ʱ���¼������Ѳ˵�
				dialogPanel.close();
				gameMenu.setVisible(true);
				gameMenu.requestFocus();
			}
		}
	}

	/**
	 * �����ͷŵļ���
	 * 
	 * @param e
	 */
	public void keyReleased(KeyEvent e) {
		keyDown = false;
		if (e.getKeyCode() == KeyEvent.VK_LEFT
				|| e.getKeyCode() == KeyEvent.VK_RIGHT
				|| e.getKeyCode() == KeyEvent.VK_UP
				|| e.getKeyCode() == KeyEvent.VK_DOWN) {
			isRun = false;
		}
	}

	public void keyTyped(KeyEvent arg0) {

	}

	/**
	 * ��ȡ��ͼ�ĸ�
	 * 
	 * @return
	 */
	public int getMapHeight() {
		return mapHeight;
	}

	/**
	 * ��ȡ��ͼ�Ŀ�
	 * 
	 * @return
	 */
	public int getMapWidth() {
		return mapWidth;
	}

	/**
	 * �̵߳�ʵ��
	 * 
	 * ѭ���ػ��ͼ
	 */
	public void run() {
		Thread monsterThread = new Thread(monster);// ����ˢ���߳�
		monsterThread.start();

		while (true) {
//			System.out.println(isRun);
			while (isRun == true) {// �����������ʱ���ػ�
				this.moveMap();// �ƶ���ͼ
				this.repaint();
//				System.out.println(offsetX + "," + offsetY);
				try {
					Thread.sleep(150);// �ػ���Ϊ150����
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

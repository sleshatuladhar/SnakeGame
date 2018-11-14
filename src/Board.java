import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final int B_WIDTH = 300;
	private final int B_HEIGHT = 300;// these determine the size of the board
	private final int DOT_SIZE = 10;// size of apple and dot of snake
	private final int ALL_DOTS = 900;// total number of possible dots
	private final int RANDOM_POS = 29;// random position of apple
	private final int DELAY = 140;// speed of game

	private final int x[] = new int[ALL_DOTS];// x coordinates of all joints of the snake
	private final int y[] = new int[ALL_DOTS];// y coordinates of all joints of the snake

	private Image ball;
	private Image apple;
	private Image head;

	private int dots;
	private int apple_x;
	private int apple_y;

	private Timer timer;

	private boolean leftDirection = false;
	private boolean rightDirection = true;
	private boolean upDirection = false;
	private boolean downDirection = false;
	private boolean inGame = false;

	// private boolean newGame = false;//has game started?
	private boolean endGame = false;// has game ended?

	private JPanel topPanel;
	private JButton startGame;

	public Board() {
		// default constructor
		initBoard();
	}

	private void initBoard() {
		addKeyListener(new TAdapter());
		setBackground(Color.BLACK);
		setFocusable(true);
		setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

		topPanel = new JPanel(); //top-most JPanel in layout hierarchy
		topPanel.setBackground(Color.BLACK);
		//allow us to layer the panels
		LayoutManager overlay = new OverlayLayout(topPanel);
		topPanel.setLayout(overlay);
		
		//Start Game JButton
		startGame = new JButton("Start Playing!");
		startGame.setBackground(Color.BLUE);
		startGame.setForeground(Color.WHITE);
		startGame.setFocusable(false); //rather than just setFocusabled(false)
		startGame.setFont(new Font("Calibri", Font.BOLD, 20));
		startGame.setAlignmentX(0.5f); //center horizontally on-screen
		startGame.setAlignmentY(0.5f); //center vertically on-screen
		startGame.addActionListener(this);
		topPanel.add(startGame);
		
		add(topPanel);
		startGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				inGame = true;
				// newGame = true;
				//endGame = false;

				remove(topPanel);
				loadImages();
				initGame();

			}
		});		
		// loadImages();
		// initGame();
	}

	private void loadImages() {
		ImageIcon iid = new ImageIcon("src/resources/dot.png");
		ball = iid.getImage();

		ImageIcon iia = new ImageIcon("src/resources/apple.png");
		apple = iia.getImage();

		ImageIcon iih = new ImageIcon("src/resources/head.png");
		head = iih.getImage();

	}

	private void initGame() {
		dots = 3;

		for (int z = 0; z < dots; z++) {
			x[z] = 50 - z * 10;
			y[z] = 50;
		}

		locateApple();

		Timer timer = new Timer(DELAY, this);
		timer.start();
	}

	private void checkApple() {

		if ((x[0] == apple_x) && (y[0] == apple_y)) {
			dots++;
			locateApple();
		}
	}

	private void locateApple() {
		int r = (int) (Math.random() * RANDOM_POS);
		apple_x = r * DOT_SIZE;

		r = (int) (Math.random() * RANDOM_POS);
		apple_y = r * DOT_SIZE;
	}

	private void move() {
		for (int z = dots; z > 0; z--) {
			x[z] = x[z - 1];
			y[z] = y[z - 1];
		}

		if (leftDirection) {
			x[0] -= DOT_SIZE;
		}

		if (rightDirection) {
			x[0] += DOT_SIZE;
		}

		if (upDirection) {
			y[0] -= DOT_SIZE;
		}

		if (downDirection) {
			y[0] += DOT_SIZE;
		}
	}

	private void checkCollision() {

		for (int z = dots; z > 0; z--) {
			if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
				inGame = false;
				endGame = true;

			}
		}

		if (y[0] >= B_HEIGHT) {
			inGame = false;
			endGame = true;
		}

		if (y[0] < 0) {
			inGame = false;
			endGame = true;
		}

		if (x[0] >= B_WIDTH) {
			inGame = false;
			endGame = true;
		}

		if (x[0] < 0) {
			inGame = false;
			endGame = true;
		}

		if (inGame == false) {
			// timer.stop();

			repaint();
			revalidate();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (inGame == true) {
			checkApple();
			checkCollision();
			move();
		}
		repaint();
	}

	private class TAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();

			if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
				leftDirection = true;
				upDirection = false;
				downDirection = false;
			}

			if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
				rightDirection = true;
				upDirection = false;
				downDirection = false;
			}

			if ((key == KeyEvent.VK_UP) && (!downDirection)) {
				upDirection = true;
				leftDirection = false;
				rightDirection = false;
			}

			if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
				downDirection = true;
				leftDirection = false;
				rightDirection = false;
			}

		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		doDrawing(g);
	}

	private void doDrawing(Graphics g) {
		if(inGame==false && endGame==false) {
			playGameScreen(g);
		}
		else if (inGame == true && endGame==false) {
			g.drawImage(apple, apple_x, apple_y, this);

			for (int z = 0; z < dots; z++) {
				if (z == 0) {
					g.drawImage(head, x[z], y[z], this);
				} else {
					g.drawImage(ball, x[z], y[z], this);
				}
			}

			Toolkit.getDefaultToolkit().sync();
		} else {
			gameOver(g);
		}
	}

	private void playGameScreen(Graphics g) {
		String message = "Snake Game";

		Font font = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics primaryFont = getFontMetrics(font);

		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString(message, (B_WIDTH - primaryFont.stringWidth(message)) / 2, B_HEIGHT / 2);

		
	}
	private void gameOver(Graphics g) {
		String message = "Game Over";
		Font font = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics primaryFont = getFontMetrics(font);

		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString(message, (B_WIDTH - primaryFont.stringWidth(message)) / 2, B_HEIGHT / 2);
	}

}
import java.awt.EventQueue;

import javax.swing.JFrame;

public class Snake extends JFrame {

	private static final long serialVersionUID = 1L;

	public Snake() {
		// default constructor
		initSnake();
	}

	private void initSnake() {
		add(new Board());

		setResizable(false);
		pack();

		setTitle("Snake Game");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				JFrame snake = new Snake();
				snake.setVisible(true);
			}
		});

	}
}

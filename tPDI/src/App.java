import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Core;

public class App extends JFrame{

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				try{
					App frame = new App();
					frame.setVisible(true);
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public App(){
		
		Imagem imagem = new Imagem("Lenna.png");
		ImageIcon _imagem = new ImageIcon(imagem.imagem);
		ImageIcon _imagemModificada = new ImageIcon(imagem.filtroMedia("mask9x9.txt"));
		
		/*int color = -16755216;
		
		int blue = color & 0xff;
		int green = (color & 0xff00) >> 8;
		int red = (color & 0xff0000) >> 16;
		int alpha = (color & 0xff000000) >>> 24;
		
		int color2 = blue | (green << 8) | (red << 16) | (alpha << 24);*/
		
		//System.out.println("Finish!");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(64, 64, 1264, 512);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JLabel img1 = new JLabel(_imagem);
		JLabel img2 = new JLabel(_imagemModificada);
		contentPane.add(img1, BorderLayout.WEST);
		contentPane.add(img2, BorderLayout.EAST);
	}

}

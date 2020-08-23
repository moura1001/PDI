import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Imagem{
	
	private static final String DIRETORIO = "recursos/";
	
	class YIQ{
		public float y, i, q;
		
		public YIQ(float y, float i, float q){
			this.y = y;
			this.i = i;
			this.q = q;
		}
	}
	
	public BufferedImage imagem;
	public int width, height;
	
	public Imagem(String nomeArquivo){
		carregarImagem(nomeArquivo);
		//alterarImagem();
		
	}
	
	private void carregarImagem(String nomeArquivo){       
		
		try{
			imagem = ImageIO.read(new File(DIRETORIO + nomeArquivo));
		} catch(IOException e){
			e.printStackTrace();
			System.err.println("Erro! Não foi possível encontrar a imagem a ser carregada.");
		}
		
		width = imagem.getWidth();
		height = imagem.getHeight();
        		
	}
	
	private int getB(int x, int y){
		return imagem.getRGB(x, y) & 0xff;		
	}
	
	private int getG(int x, int y){
		return (imagem.getRGB(x, y) & 0xff00) >> 8;		
	}
	
	private int getR(int x, int y){
		return (imagem.getRGB(x, y) & 0xff0000) >> 16;		
	}
	
	private int getA(int x, int y){
		return (imagem.getRGB(x, y) & 0xff000000) >>> 24;
	}
	
	/*private int[] getRGB(int x, int y){
		int color = imagem.getRGB(x, y);
		int[] rgb = new int[4];
		rgb[0] = color & 0xff; // Blue
		rgb[1] = (color & 0xff00) >> 8; // Green
		rgb[2] = (color & 0xff0000) >> 16; // Red
		rgb[3] = (color & 0xff000000) >>> 24; // Alpha
		return rgb;
	}*/
	
	private int corRGB(int red, int green, int blue, int alpha){
		return blue | (green << 8) | (red << 16) | (alpha << 24);
	}	
	
	public BufferedImage bandasImagem(int banda){
		//BufferedImage imagemModificada = new BufferedImage(imagem.getColorModel(), imagem.copyData(null), imagem.isAlphaPremultiplied(), null);
		BufferedImage imagemModificada = new BufferedImage(width, height, imagem.getType());
		
		switch(banda){
			case 0:
				for(int x = 0; x < width; x++)
					for(int y = 0; y < height; y++)
						imagemModificada.setRGB(x, y, corRGB(getR(x,y),0,0,255));
				break;
				
			case 1:
				for(int x = 0; x < width; x++)
					for(int y = 0; y < height; y++)
						imagemModificada.setRGB(x, y, corRGB(0,getG(x,y),0,255));
				break;
				
			case 2:
				for(int x = 0; x < width; x++)
					for(int y = 0; y < height; y++)
						imagemModificada.setRGB(x, y, corRGB(0,0,getB(x,y),255));
				break;
				
			case 3:
				for(int x = 0; x < width; x++)
					for(int y = 0; y < height; y++){
						int gray = (int) (0.299*getR(x,y) + 0.587*getG(x,y) + 0.114*getB(x,y));
						imagemModificada.setRGB(x, y, corRGB(gray,gray,gray,255));
						//imagemModificada.setRGB(x, y, RGB(getR(x,y),getR(x,y),getR(x,y),255));
					}
				break;
				
			default:
				imagemModificada = null;
				break;
				
		}
		
		return imagemModificada;
		
	}
	
	private int getQ(int x, int y){
		return (int) (0.211456*getR(x,y) - 0.522591*getG(x,y) + 0.311350*getB(x,y));		
	}
	
	private int getI(int x, int y){
		return (int) (0.595716*getR(x,y) - 0.274453*getG(x,y) - 0.321264*getB(x,y));		
	}
	
	private int getY(int x, int y){
		return (int) (0.299000*getR(x,y) + 0.587000*getG(x,y) + 0.114000*getB(x,y));		
	}
	
	/*private YIQ[][] converterParaYIQ(){
		YIQ[][] imagemModificada = new YIQ[width][height];
		
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++){
				int componenteR = getR(x,y);				
				int componenteG = getG(x,y);
				int componenteB = getB(x,y);
				
				float componenteY = (float) (0.299000*componenteR + 0.587000*componenteG + 0.114000*componenteB);
				float componenteI = (float) (0.595716*componenteR - 0.274453*componenteG - 0.321264*componenteB);
				float componenteQ = (float) (0.211456*componenteR - 0.522591*componenteG + 0.311350*componenteB);	
				
				imagemModificada[x][y] = new YIQ(componenteY,componenteI,componenteQ);
			}
		
		return imagemModificada;
	}*/
	
	public BufferedImage converterRGBParaYIQParaRGB(){
		//BufferedImage imagemModificada = new BufferedImage(imagem.getColorModel(), imagem.copyData(null), imagem.isAlphaPremultiplied(), null);
		BufferedImage imagemModificada = new BufferedImage(width, height, imagem.getType());
		
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++){
				int componenteY = getY(x,y);				
				int componenteI = getI(x,y);
				int componenteQ = getQ(x,y);
				
				int componenteR = (int) (componenteY + 0.956*componenteI + 0.621*componenteQ);
				int componenteG = (int) (componenteY - 0.272*componenteI - 0.647*componenteQ);
				int componenteB = (int) (componenteY - 1.107*componenteI + 1.703*componenteQ);
				imagemModificada.setRGB(x, y, corRGB(componenteR,componenteG,componenteB,255));
			}
		
		return imagemModificada;
	}
	
	public void alterarImagem(){
		
		System.out.println("Image width = " + width + "\nImage height = " + height);
		
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++){
				int rgb = (x < 128) ? 0 : corRGB(0,128,0,255);
				
				imagem.setRGB(x, y, rgb);
			}				
		
		try{
			ImageIO.write(imagem, "png", new File(DIRETORIO + "saved.png"));
		} catch (IOException e){
			e.printStackTrace();
			System.err.println("Erro! Não foi possível alterar a imagem.");
		}
		
		System.out.println("Finish!");
	
	}

}

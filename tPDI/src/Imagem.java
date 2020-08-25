import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

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
	
	private int getB(BufferedImage imagem, int x, int y){
		return imagem.getRGB(x, y) & 0xff;		
	}
	
	private int getG(BufferedImage imagem, int x, int y){
		return (imagem.getRGB(x, y) & 0xff00) >> 8;		
	}
	
	private int getR(BufferedImage imagem, int x, int y){
		return (imagem.getRGB(x, y) & 0xff0000) >> 16;		
	}
	
	private int getA(BufferedImage imagem, int x, int y){
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
		//return blue | (green << 8) | (red << 16);
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
	
	private float getQ(int x, int y){
		return (float) (0.211456*getR(x,y) - 0.522591*getG(x,y) + 0.311350*getB(x,y));		
	}
	
	private float getI(int x, int y){
		return (float) (0.595716*getR(x,y) - 0.274453*getG(x,y) - 0.321264*getB(x,y));		
	}
	
	private float getY(int x, int y){
		return (float) (0.299000*getR(x,y) + 0.587000*getG(x,y) + 0.114000*getB(x,y));		
	}
	
	private BufferedImage imagemOpenCVParaBufferedImage(Mat imagemOpenCV){
		BufferedImage imagemModificada = null;
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(".png", imagemOpenCV, matOfByte);
		
		//Imgcodecs.imwrite(DIRETORIO + "saved2.png", imagemOpenCV);
		
		byte[] byteArray = matOfByte.toArray();
		
		try {
			imagemModificada = ImageIO.read(new ByteArrayInputStream(byteArray));
		} catch (IOException e){
			e.printStackTrace();
			System.err.println("Erro! Não foi possível converter a imagem para o espaço YIQ.");
		}
		
		return imagemModificada;		
	}
	
	public BufferedImage converterParaYIQ(){
		//YIQ[][] imagemModificada = new YIQ[width][height];
	    BufferedImage imagemModificada = null;
		Mat imagemOpenCV = new Mat(width, height, CvType.CV_32FC3);
		
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++){
				//float[] pixel = new float[3];
				
				int componenteR = getR(x,y);				
				int componenteG = getG(x,y);
				int componenteB = getB(x,y);
				
				float componenteY = (float) (0.299000*componenteR + 0.587000*componenteG + 0.114000*componenteB);
				float componenteI = (float) (0.595716*componenteR - 0.274453*componenteG - 0.321264*componenteB);
				float componenteQ = (float) (0.211456*componenteR - 0.522591*componenteG + 0.311350*componenteB);
				
				//if(x == 256 && y == 256)
				//	System.out.println(componenteY + " " + componenteI + " " + componenteQ);
				
				//float[] pixel = {componenteY,componenteI,componenteQ};				
				imagemOpenCV.put(y, x, componenteQ,componenteI,componenteY);				
				//imagemModificada[x][y] = new YIQ(componenteY,componenteI,componenteQ);
				//imagemModificada.getRaster().setPixel(x, y, pixel);
				//imagemOpenCV.get(y, x, pixel);
				//System.out.print("");
			}
		
		//System.out.println("Here");
		//int componenteR = getR(256,256);				
		//int componenteG = getG(256,256);
		//int componenteB = getB(256,256);
		//pixel = this.imagem.getData().getDataElements(256, 256, pixel);
		//int r = ((byte[]) pixel)[0] & 0xff;
		
		imagemModificada = imagemOpenCVParaBufferedImage(imagemOpenCV);
		
		//try{
		//	ImageIO.write(imagemModificada, "png", new File(DIRETORIO + "saved2.png"));
		//} catch (IOException e){
		//	e.printStackTrace();
		//	System.err.println("Erro! Não foi possível alterar a imagem.");
		//}
		
		//float[] pixel = new float[3];
		//float[] pixel2 = new float[3];
		//Object pixel3 = null;
		//float componenteY = getY(256,256);				
		//float componenteI = getI(256,256);
		//float componenteQ = getQ(256,256);
		//pixel3 = imagemModificada.getData().getDataElements(256, 256, null);
		//imagemModificada.getData().getPixel(256, 256, pixel);
		//imagemOpenCV.get(256, 256, pixel2);
		//Class clas = pixel3.getClass();
		//float y = pixel2[0];
		
		return imagemModificada;
	}
	
	private Mat YIQ(){
		//YIQ[][] imagemModificada = new YIQ[width][height];
	    //BufferedImage imagemModificada = null;
		Mat imagemOpenCV = new Mat(width, height, CvType.CV_32FC3);
		
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++){
				//float[] pixel = new float[3];
				
				int componenteR = getR(x,y);				
				int componenteG = getG(x,y);
				int componenteB = getB(x,y);
				
				float componenteY = (float) (0.299000*componenteR + 0.587000*componenteG + 0.114000*componenteB);
				float componenteI = (float) (0.595716*componenteR - 0.274453*componenteG - 0.321264*componenteB);
				float componenteQ = (float) (0.211456*componenteR - 0.522591*componenteG + 0.311350*componenteB);
				
				//if(x == 256 && y == 256)
				//	System.out.println(componenteY + " " + componenteI + " " + componenteQ);
				
				//float[] pixel = {componenteY,componenteI,componenteQ};				
				imagemOpenCV.put(y, x, componenteQ,componenteI,componenteY);				
				//imagemModificada[x][y] = new YIQ(componenteY,componenteI,componenteQ);
				//imagemModificada.getRaster().setPixel(x, y, pixel);
				//imagemOpenCV.get(y, x, pixel);
				//System.out.print("");
			}
		
		//System.out.println("Here");
		//int componenteR = getR(256,256);				
		//int componenteG = getG(256,256);
		//int componenteB = getB(256,256);
		//pixel = this.imagem.getData().getDataElements(256, 256, pixel);
		//int r = ((byte[]) pixel)[0] & 0xff;
		
		//MatOfByte matOfByte = new MatOfByte();
		//Imgcodecs.imencode(".png", imagemOpenCV, matOfByte);
		
		//Imgcodecs.imwrite(DIRETORIO + "saved2.jpg", imagemOpenCV);
		
		//byte[] byteArray = matOfByte.toArray();
		
		//try {
		//	imagemModificada = ImageIO.read(new ByteArrayInputStream(byteArray));
		//} catch (IOException e){
		//	e.printStackTrace();
		//	System.err.println("Erro! Não foi possível converter a imagem para o espaço YIQ.");
		//}
		
		//try{
		//	ImageIO.write(imagemModificada, "png", new File(DIRETORIO + "saved2.png"));
		//} catch (IOException e){
		//	e.printStackTrace();
		//	System.err.println("Erro! Não foi possível alterar a imagem.");
		//}
		
		//float[] pixel = new float[3];
		//float[] pixel2 = new float[3];
		//Object pixel3 = null;
		//float componenteY = getY(256,256);				
		//float componenteI = getI(256,256);
		//float componenteQ = getQ(256,256);
		//pixel3 = imagemModificada.getData().getDataElements(256, 256, null);
		//imagemModificada.getData().getPixel(256, 256, pixel);
		//imagemOpenCV.get(256, 256, pixel2);
		//Class clas = pixel3.getClass();
		//float y = pixel2[0];
		
		return imagemOpenCV;
	}
	
	public BufferedImage converterRGBParaYIQParaRGB(){
		Mat imagemOpenCV = YIQ();
		float[] pixel = new float[3];
		BufferedImage imagemModificada = new BufferedImage(width, height, imagem.getType());
		
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++){
				imagemOpenCV.get(y, x, pixel);
				float componenteY = pixel[2];				
				float componenteI = pixel[1];
				float componenteQ = pixel[0];
				//float componenteY = getY(x,y);				
				//float componenteI = getI(x,y);
				//float componenteQ = getQ(x,y);
				
				int componenteR = (int) (componenteY + 0.956*componenteI + 0.621*componenteQ);
				int componenteG = (int) (componenteY - 0.272*componenteI - 0.647*componenteQ);
				int componenteB = (int) (componenteY - 1.107*componenteI + 1.703*componenteQ);
				imagemModificada.setRGB(x, y, corRGB(componenteR,componenteG,componenteB,255));
			}
		
		return imagemModificada;
	}
	
	private BufferedImage converterYIQParaRGB(Mat imagemOpenCV){
		//Mat imagemOpenCV = YIQ();
		float[] pixel = new float[3];
		BufferedImage imagemModificada = new BufferedImage(width, height, imagem.getType());
		
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++){
				imagemOpenCV.get(y, x, pixel);
				float componenteY = pixel[2];				
				float componenteI = pixel[1];
				float componenteQ = pixel[0];
				//float componenteY = getY(x,y);				
				//float componenteI = getI(x,y);
				//float componenteQ = getQ(x,y);
				
				int componenteR = (int) (componenteY + 0.956*componenteI + 0.621*componenteQ);
				int componenteG = (int) (componenteY - 0.272*componenteI - 0.647*componenteQ);
				int componenteB = (int) (componenteY - 1.107*componenteI + 1.703*componenteQ);
				imagemModificada.setRGB(x, y, corRGB(componenteR,componenteG,componenteB,255));
			}
		
		return imagemModificada;
	}
	
	public BufferedImage filtroNegativo(int modo){
		//BufferedImage imagemModificada = new BufferedImage(imagem.getColorModel(), imagem.copyData(null), imagem.isAlphaPremultiplied(), null);
		BufferedImage imagemModificada = new BufferedImage(width, height, imagem.getType());
		
		switch(modo){
			case 0:
				for(int x = 0; x < width; x++)
					for(int y = 0; y < height; y++){
						int componenteR = 255 - getR(x,y);				
						int componenteG = 255 - getG(x,y);
						int componenteB = 255 - getB(x,y);

						imagemModificada.setRGB(x, y, corRGB(componenteR,componenteG,componenteB,255));
					}
				break;
				
			case 1:
				Mat imagemOpenCV = YIQ();
				float[] pixel = new float[3];
				
				for(int x = 0; x < width; x++)
					for(int y = 0; y < height; y++){
						imagemOpenCV.get(y, x, pixel);
						float componenteY = 255f - pixel[2];				
						float componenteI = pixel[1];
						float componenteQ = pixel[0];
						//float componenteY = getY(x,y);				
						//int componenteI = getI(x,y);
						//int componenteQ = getQ(x,y);
						imagemOpenCV.put(y, x, componenteQ,componenteI,componenteY);
						//imagemModificada.setRGB(x, y, corRGB(componenteY,0,0,255));
					}
				
				imagemModificada = imagemOpenCVParaBufferedImage(imagemOpenCV);
				break;
				
			case 2:
				Mat imagemOpenCV2 = YIQ();
				float[] pixel2 = new float[3];
				
				for(int x = 0; x < width; x++)
					for(int y = 0; y < height; y++){
						imagemOpenCV2.get(y, x, pixel2);
						float componenteY = pixel2[2];				
						float componenteI = pixel2[1];
						float componenteQ = pixel2[0];
						//float componenteY = getY(x,y);				
						//int componenteI = getI(x,y);
						//int componenteQ = getQ(x,y);
						imagemOpenCV2.put(y, x, componenteQ,componenteI,componenteY);
						//imagemModificada.setRGB(x, y, corRGB(componenteY,0,0,255));
					}
				
				imagemModificada = converterYIQParaRGB(imagemOpenCV2);
				break;	
				
			default:
				imagemModificada = null;
				break;
			
		}
		
		return imagemModificada;
	}
	
	public BufferedImage manipularBrilho(int modo, int quantidade){
		//BufferedImage imagemModificada = new BufferedImage(imagem.getColorModel(), imagem.copyData(null), imagem.isAlphaPremultiplied(), null);
		BufferedImage imagemModificada = new BufferedImage(width, height, imagem.getType());
		
		switch(modo){
			case 0:
				for(int x = 0; x < width; x++)
					for(int y = 0; y < height; y++){
						int auxR = getR(x,y) + quantidade;
						int auxG = getG(x,y) + quantidade;
						int auxB = getB(x,y) + quantidade;
						int componenteR = (auxR > 255) ? 255 : auxR;				
						int componenteG = (auxG > 255) ? 255 : auxG;
						int componenteB = (auxB > 255) ? 255 : auxB;						

						imagemModificada.setRGB(x, y, corRGB(componenteR,componenteG,componenteB,255));
					}
				break;
				
			case 1:
				for(int x = 0; x < width; x++)
					for(int y = 0; y < height; y++){
						int auxR = getR(x,y) * quantidade;
						int auxG = getG(x,y) * quantidade;
						int auxB = getB(x,y) * quantidade;
						int componenteR = (auxR > 255) ? 255 : auxR;				
						int componenteG = (auxG > 255) ? 255 : auxG;
						int componenteB = (auxB > 255) ? 255 : auxB;						

						imagemModificada.setRGB(x, y, corRGB(componenteR,componenteG,componenteB,255));
					}
				break;
				
			case 2:
				Mat imagemOpenCV2 = YIQ();
				float[] pixel2 = new float[3];
				
				for(int x = 0; x < width; x++)
					for(int y = 0; y < height; y++){
						imagemOpenCV2.get(y, x, pixel2);
						float componenteY = pixel2[2];				
						float componenteI = pixel2[1];
						float componenteQ = pixel2[0];
						//float componenteY = getY(x,y);				
						//int componenteI = getI(x,y);
						//int componenteQ = getQ(x,y);
						imagemOpenCV2.put(y, x, componenteQ,componenteI,componenteY);
						//imagemModificada.setRGB(x, y, corRGB(componenteY,0,0,255));
					}
				
				imagemModificada = converterYIQParaRGB(imagemOpenCV2);
				break;	
				
			default:
				imagemModificada = null;
				break;
			
		}
		
		return imagemModificada;
	}
	
	public BufferedImage thresholding(int modo, int quantidade){
		//BufferedImage imagemModificada = new BufferedImage(imagem.getColorModel(), imagem.copyData(null), imagem.isAlphaPremultiplied(), null);
		BufferedImage imagemModificada = new BufferedImage(width, height, imagem.getType());
		
		switch(modo){
			case 0:
				//imagemModificada = bandasImagem(3);
				for(int x = 0; x < width; x++)
					for(int y = 0; y < height; y++){
						int pixelvalue = (getR(x,y) + getG(x,y) + getB(x,y))/3;
						int componenteR = (pixelvalue < quantidade) ? 0 : 255;				
						int componenteG = (pixelvalue < quantidade) ? 0 : 255;
						int componenteB = (pixelvalue < quantidade) ? 0 : 255;
						//Color color = (pixelvalue < quantidade) ? Color.BLACK : Color.WHITE;

						imagemModificada.setRGB(x, y, corRGB(componenteR,componenteG,componenteB,255));
						//imagemModificada.setRGB(x, y, color.getRGB());
					}
				break;
				
			case 1:
				for(int x = 0; x < width; x++)
					for(int y = 0; y < height; y++){
						int componenteR = (getR(x,y) < quantidade) ? 0 : 255;				
						int componenteG = (getG(x,y) < quantidade) ? 0 : 255;
						int componenteB = (getB(x,y) < quantidade) ? 0 : 255;

						imagemModificada.setRGB(x, y, corRGB(componenteR,componenteG,componenteB,255));
					}
				break;
				
			default:
				imagemModificada = null;
				break;
			
		}
		
		return imagemModificada;
	}
	
	public BufferedImage thresholdingY(int modo, int quantidade){		
		Mat imagemOpenCV = YIQ();
		float[] pixel = new float[3];
		
		//BufferedImage imagemModificada = new BufferedImage(imagem.getColorModel(), imagem.copyData(null), imagem.isAlphaPremultiplied(), null);
		BufferedImage imagemModificada = new BufferedImage(width, height, imagem.getType());
		
		switch(modo){
			case 0:
				for(int x = 0; x < width; x++)
					for(int y = 0; y < height; y++){
						imagemOpenCV.get(y, x, pixel);
						float componenteY = (pixel[2] < quantidade) ? 0: 255;				
						float componenteI = pixel[1];
						float componenteQ = pixel[0];
						//float componenteY = getY(x,y);				
						//int componenteI = getI(x,y);
						//int componenteQ = getQ(x,y);
						imagemOpenCV.put(y, x, componenteQ,componenteI,componenteY);
						//imagemModificada.setRGB(x, y, corRGB(componenteY,0,0,255));
					}
				
				//imagemModificada = converterYIQParaRGB(imagemOpenCV);
				imagemModificada = imagemOpenCVParaBufferedImage(imagemOpenCV);
				break;
				
			case 1:
				float media = 0;
				for(int x = 0; x < width; x++)
					for(int y = 0; y < height; y++){
						imagemOpenCV.get(y, x, pixel);
						media += pixel[2];
					}
				media /= width*height;
				
				for(int x = 0; x < width; x++)
					for(int y = 0; y < height; y++){
						imagemOpenCV.get(y, x, pixel);
						float componenteY = (pixel[2] < media) ? 0: 255;				
						float componenteI = pixel[1];
						float componenteQ = pixel[0];
						//float componenteY = getY(x,y);				
						//int componenteI = getI(x,y);
						//int componenteQ = getQ(x,y);
						imagemOpenCV.put(y, x, componenteQ,componenteI,componenteY);
						//imagemModificada.setRGB(x, y, corRGB(componenteY,0,0,255));
					}
				//imagemModificada = converterYIQParaRGB(imagemOpenCV);
				imagemModificada = imagemOpenCVParaBufferedImage(imagemOpenCV);
				break;
				
			default:
				imagemModificada = null;
				break;
			
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

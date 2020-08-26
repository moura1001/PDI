import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MaskReader{
	
	private static final String DIRETORIO = "recursos/masks/";
	
	public static float[][] carregarArquivoParaMatriz(String nomeArquivo){

	    FileReader arquivo = null;
        File txtFile = new File(DIRETORIO + nomeArquivo);
        
        try{
        	
            arquivo = new FileReader(txtFile);
        
        } catch(FileNotFoundException e){
            System.err.println("Erro! Não foi possível encontrar o arquivo a ser carregado.");
        }

        BufferedReader reader = new BufferedReader(arquivo);
        
        int pos = 1;
        float[][] mask = null;
        String linha;
        
        try{
        	
        	linha = reader.readLine();
        	String[] primeiraLinha = linha.split(" ");
        	mask = new float[primeiraLinha.length][primeiraLinha.length];
        	for(int y = 0; y < primeiraLinha.length; y++)
        		mask[0][y] = Float.parseFloat(primeiraLinha[y]);        	
        	primeiraLinha = null;
       	
        	while(true){
        		
       			linha = reader.readLine();
       			
       			if(linha == null)
       				break;
       			
       			String[] linhaAtual = linha.split(" ");
        		
       			for(int y = 0; y < linhaAtual.length; y++)
            		mask[pos][y] = Float.parseFloat(linhaAtual[y]);
       			
       			pos++;
    
        	}
        	
       		arquivo.close();
                
        } catch(IOException e){
            System.err.println("Erro ao tentar ler o arquivo");
        }
        
        return mask;
		
	}

}

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Writer {
	private final static String filePath = "outmatrix.txt";
	
	public void write(int[][] matrix){
		File file = new File(filePath);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < matrix.length; i++){
				for(int j = 0; j < matrix.length; j++){
					writer.write(matrix[i][j] + "\t");
				}
				writer.write("\n");
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void writeListCluster(ArrayList<Document> lstDocumet, String filePath){
		try {
			Files.createDirectories(Paths.get(filePath));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		for(Document dc : lstDocumet){
			try {
				FileOutputStream file = new FileOutputStream(filePath + "/" + dc.getDocumentID());
				OutputStreamWriter osr = new OutputStreamWriter(file, "utf-8");
				BufferedWriter writer = new BufferedWriter(osr);
				writer.write(dc.getData());
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void writeListPairDC(ArrayList<ArrayList<PairDocument>> lstPairDC){
		File file = new File("List PairDC.txt");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for (ArrayList<PairDocument> lst : lstPairDC){
				writer.write(lst.toString() + "\n");
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeListUnCluster(ArrayList<ArrayList<Integer>> lstUncluster){
		File file = new File("List Uncluster.txt");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for (ArrayList<Integer> lst : lstUncluster){
				writer.append(lst.toString() + "\n");
			}
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeListCluster_Topic(ArrayList<Cluster> lstCluster){		
		try {
			FileOutputStream file = new FileOutputStream("Output/Output.txt");
			OutputStreamWriter osr = new OutputStreamWriter(file, "utf-8");
			BufferedWriter writer = new BufferedWriter(osr);
			for (Cluster c : lstCluster){
				//writer.append(c.getListId().toString() + "\n");
				writer.append(c.getTopic().toString() + "\n");
			}
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

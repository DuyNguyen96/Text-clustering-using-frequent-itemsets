import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		
		//Main.test();
		
		Reader reader = new Reader();
		ArrayList<Document> lstDC = reader.readFile();
		Process_Document pDC = new Process_Document();
		
		ArrayList<ArrayList<Item>> lstArrItem = pDC.listArrItem(lstDC);
		int[][] sim = pDC.createSimilarity(lstArrItem);
		
		MC_1 mc = new MC_1(sim, lstArrItem.size());
		mc.cluster();
		mc.setTopic(lstArrItem);
		mc.normalize(4);
		mc.writeOutPut(lstDC);
	}
	
	private static void test(){
		int[][] matrix = new int[8][9];
		
		try {
			FileReader file = new FileReader("matrix.txt");
			BufferedReader reader = new BufferedReader(file);
			String  line = null;
			int numOfLine = 0;
			while((line = reader.readLine()) != null){
				String[] arr = line.split(" ");
				for (int i = 0; i <= 8; i++){
					matrix[numOfLine][i] = Integer.parseInt(arr[i].toString());
				}
				numOfLine++;
			}
			file.close();
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		MC_1 mc = new MC_1(matrix, 9);
		mc.cluster();
		System.out.println(mc.getListCluster());
	}
	
}

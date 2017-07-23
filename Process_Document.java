import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Process_Document {
	private final static double MINSUPP = 0.3;
	private ArrayList<String> listStopWord;
	
	public Process_Document(){
		listStopWord = new ArrayList<>();
		readFile();
	}
	
	private void readFile(){
		try {
			FileInputStream file = new FileInputStream("vietnamese-stopwords.txt");
			InputStreamReader isr = new InputStreamReader(file, "utf-8");
			BufferedReader reader = new BufferedReader(isr);
			String  line = null;	
			while((line = reader.readLine()) != null){
				listStopWord.add(line);
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
	}
	
	public ArrayList<String> process_RemoveStopWord(String data){
		String str = new String();
		str = data.toLowerCase().replaceAll("[$&+,:;=?@#|'<>.-^*()%!”“]", "").replaceAll("\\s\\s", " ");
		str = str.replaceAll("\"[^\"]*\"", "").replaceAll("\\s\\s", " ");
		String[] arrStr = str.split(" ");
		ArrayList<String> lstStrData = new ArrayList<>(Arrays.asList(arrStr));
		ArrayList<String> lst = new ArrayList<>();
		for (String stopword : listStopWord) {		
			if (lstStrData.contains(stopword)){
				lst.add(stopword);
				lstStrData.removeIf(stopword::equals);
			}
		}
		return lstStrData;
	}
	
	public int[][] createSimilarity(ArrayList<ArrayList<Item>> lstItem){
		int[][] sim = new int[lstItem.size()][lstItem.size()];
		for(int i = 0; i < lstItem.size() - 1; i++){
			for (int j = i + 1; j < lstItem.size(); j++){
				sim[i][j] = simItem(lstItem.get(i), lstItem.get(j));
			}
		}
		return sim;
	}
	
	private int simItem(ArrayList<Item> lstItem1, ArrayList<Item> lstItem2){
		int count = 0;
		for (Item i : lstItem1){
			for(Item j : lstItem2){
				if (i.getItem().equals(j.getItem()))
					count++;
			}
		}
		return count;
	}
	
	public ArrayList<ArrayList<Item>> listArrItem(ArrayList<Document> lstDocument){
		ArrayList<ArrayList<Item>> lstArrItem = new ArrayList<>();
		for(Document dc : lstDocument){
			dc.process_Data(MINSUPP);
			lstArrItem.add(dc.getLstFreqItems());
		}
		return lstArrItem;
	}
	
}

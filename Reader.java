import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Reader {
	private final static String endDC1 = "( theo";
	private String filePath = "Input";
	
	public ArrayList<Document> readFile(){
		ArrayList<Document> lstDocument = new ArrayList<>();
		Document dc = new Document("");
		File dir = new File(filePath);
		File[] file = dir.listFiles();	
		try {
			for(File f : file)
			{
				InputStream isr = new FileInputStream(f);
				BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "utf-8"));
				String  line = null;
				while((line = reader.readLine()) != null){
					line += " ";
					line = line.toLowerCase().replaceAll("-", "").replaceAll("\\s\\s", " ");	
					dc.addData(line);
					dc.setDocumentID(f.getName());
				}
				lstDocument.add(dc);
				dc = new Document("");
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lstDocument;
	}
	
	public ArrayList<ArrayList<Item>> readTestTopic() throws IOException{
		ArrayList<ArrayList<Item>> lstArrItem = new ArrayList<>();
		FileInputStream file = new FileInputStream("textTopic.txt");
		InputStreamReader isr = new InputStreamReader(file, "utf-8");
		BufferedReader reader = new BufferedReader(isr);
		String  line = null;
		ArrayList<Item> lstItem = new ArrayList<>();
		while((line = reader.readLine()) != null){
			if (line != ""){
				String[] str = line.split(",");
				for (int i = 0; i < str.length; i++){
					lstItem.add(new Item(str[i], 1));
				}
			} else{
				lstArrItem.add(lstItem);
				lstItem = new ArrayList<>();
			}
		}
		return lstArrItem;
	}
}

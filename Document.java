
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class Document {
	private String documentID;
	private ArrayList<String> listSequenceOfWord; 
	private String data;
	private ArrayList<Item> listFreqItems;
	
	public Document(String documentID){
		this.documentID = documentID;
		listSequenceOfWord = new ArrayList<>();
		data = "";
		listFreqItems = new ArrayList<>();
	}

	public String getDocumentID() {
		return documentID;
	}

	public void setDocumentID(String documentID) {
		this.documentID = documentID;
	}

	public ArrayList<String> getListSequenceOfWord() {
		return listSequenceOfWord;
	}

	public void addSequenceOfWord(ArrayList<String> lstSeq){
		for (String word : lstSeq){
			listSequenceOfWord.add(word);
		}
	}
	
	public void addData(String data){
		this.data += data;
	}
	
	public String getData() {
		return data;
	}

	public ArrayList<Item> getLstFreqItems() {
		return listFreqItems;
	}

	public void readFile(String filePath){
		try {
			FileInputStream file = new FileInputStream(filePath);
			InputStreamReader isr = new InputStreamReader(file, "utf-8");
			BufferedReader reader = new BufferedReader(isr);
			String  line = null;	
			while((line = reader.readLine()) != null){
				if(line.equals(""))
					data += "\n";
				else data += line;
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
	
	private void countWord(){
		ArrayList<String> lstWord = new ArrayList<>();
		for(String word : listSequenceOfWord){
			if (lstWord.contains(word)){
				listFreqItems.get(lstWord.indexOf(word)).increase();
			} else{
				lstWord.add(word);
				Item i = new Item(word, 1);
				listFreqItems.add(i);
			}
		}
	}
	
	public void findFreqItem(double minsupp){
		countWord();
		
		ArrayList<Item> listAllItem = new ArrayList<>(listFreqItems);
		int maxFreq = maxFreq();
		listFreqItems = listItem_Freq(listFreqItems, minsupp);
		ArrayList<Item> listItemAdd = new ArrayList<>();
		String dataTemp = new String();
		dataTemp = data.copyValueOf(data.toCharArray());
		ArrayList<String> lstWordItem = new ArrayList<>();
		for(String str : listSequenceOfWord){
			lstWordItem.add(str);
			boolean checkListWord = checkListWord(str1(lstWordItem), str, dataTemp);
			if(lstWordItem.size() >= 2 && checkListWord){
				ArrayList<Item> listItemTemp = new ArrayList<>(listFreqItems);
				ArrayList<Item> listAllItemTemp = new ArrayList<>(listAllItem);
				int freqItem = freq(lstWordItem);
				Item item = new Item(strItem(lstWordItem), freqItem);
				//listItemTemp = removeItem(lstWordItem, listItemTemp);
				//listAllItemTemp = removeItem(lstWordItem, listAllItemTemp);
				
				listItemTemp = listItem_Freq(listAllItemTemp, minsupp);
				listItemTemp.add(item);
				
				if ((double)freqItem/maxFreq >= minsupp){
					
					for(Item i : listItemAdd){
						listItemTemp.add(i);
					}
					dataTemp = dataTemp.replaceAll(item.getItem(), "").replaceAll("\\s\\s", " ");
					listFreqItems = listItemTemp;
					//listAllItem = removeItem(lstWordItem, listAllItem);
					listItemAdd.add(item);
				} else {
					lstWordItem = new ArrayList<>();
					lstWordItem.add(str);
				}
			} else if (lstWordItem.size() == 1) {
				continue;
			}else if (checkListWord == false){
				lstWordItem = new ArrayList<>();
				lstWordItem.add(str);
			}
		}
	}
	
	
	private ArrayList<Item> listItem_Freq(ArrayList<Item> listItem, double minsupp){
		int maxFreq = maxFreq(listItem);
		ArrayList<Item> lstFreq = new ArrayList<>();
		for (Item i : listItem){
			double per = ((double)i.getFreq()/maxFreq);
			if(per >= minsupp && i.getFreq() >= 2){
				lstFreq.add(i);
			}
		}
		return lstFreq;
	}
	
	private ArrayList<Item> removeItem(ArrayList<String> lstString, ArrayList<Item> listItemTemp){
		for(String str : lstString){
			for (Item i : listItemTemp){
				if(i.getItem().contains(str)){
					listItemTemp.remove(i);
					break;
				}
			}
		}
		return listItemTemp;
	}
	
	private String strItem(ArrayList<String> lstString){
		String str = "";
		for (int i = 0; i < lstString.size(); i++){
			if (i == lstString.size() - 1)
				str += lstString.get(i);
			else
				str += lstString.get(i) + " ";
		}
		return str;
	}
	
	private String str1(ArrayList<String> lstString){
		String str = "";
		for (int i = 0; i < lstString.size() - 1; i++){
			if (i == lstString.size() - 2)
				str += lstString.get(i);
			else
				str += lstString.get(i) + " ";
		}
		return str;
	}
	
	private int freq(ArrayList<String> lstString){
		int count = 0;
		for (int i = 0; i < listSequenceOfWord.size(); i++){
			String word1 = listSequenceOfWord.get(i);
			if (lstString.get(0).contains(word1)){
				boolean add = false;
				for(int j = 1; j < lstString.size() && i < (listSequenceOfWord.size() - (lstString.size() - 1)); j++){
					String word2 = listSequenceOfWord.get(++i);
					if(!word2.contains(lstString.get(j))){
						add = false;
						break;
					} else
						add = true;
				}
				if (add)
					count ++;
			}		
		}
		return count;
	}
	
	private boolean checkListWord(String str1, String str2, String data){
		int indexStr1 = data.indexOf(str1) + str1.length() + 1;
		if (indexStr1 + str2.length() > data.length())
			return false;
		String strIndex2 = data.substring(indexStr1, indexStr1 + str2.length());
		if (indexStr1 == -1)
			return false;
		return strIndex2.equals(str2);
	}
	
	private int maxFreq(){
		int max = 0;
		for(Item i : listFreqItems){
			if (i.getFreq() > max)
				max = i.getFreq();
		}
		return max;
	}
	
	private int maxFreq(ArrayList<Item> listItem){
		int max = 0;
		for(Item i : listItem){
			if (i.getFreq() > max)
				max = i.getFreq();
		}
		return max;
	}
	
	public void process_Data(double minsupp){
		Process_Document pDC = new Process_Document();
		listSequenceOfWord = pDC.process_RemoveStopWord(data);
		this.findFreqItem(minsupp);
	}
	
	
}

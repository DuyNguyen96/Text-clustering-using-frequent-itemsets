import java.util.ArrayList;
import java.util.Collections;

public class Topic {
	private final static double minSuppTopic = 0.6;
	private ArrayList<Item> listItemCandidate;
	
	public Topic(ArrayList<Item> listItemCandidate) {
		this.listItemCandidate = listItemCandidate;
		if(listItemCandidate != null)
			SetAll();
	}
	
	public Topic(){
		listItemCandidate = new ArrayList<>();
	}
	
	public Topic(Topic topic){
		this.listItemCandidate = topic.getListItemCandidate();
		Collections.sort(listItemCandidate, new ItemComparator());
	}
	
	public void setListItemCandidate(ArrayList<Item> listItemCandidate) {
		this.listItemCandidate = listItemCandidate;
		Collections.sort(listItemCandidate, new ItemComparator());
	}

	public void addItem(Item i){
		this.listItemCandidate.add(i);
	}
	
	public ArrayList<Item> getListItemCandidate() {
		return listItemCandidate;
	}
	
	private void SetAll(){
		Collections.sort(listItemCandidate, new ItemComparator());
		
		ArrayList<Item> lstItemTemp = new ArrayList<>();
		int maxValue = listItemCandidate.get(0).getFreq();
		for (Item i : listItemCandidate){
			if(((double)i.getFreq())/maxValue >= minSuppTopic){
				lstItemTemp.add(i);
				i.setPerOfFreq((double)i.getFreq()/maxValue);
			}
		}
		listItemCandidate = lstItemTemp;
	}

	@Override
	public String toString() {
		String result = "";
		for (Item i : listItemCandidate){
			result += i.toString() + ", ";
 		}
		return result;
	}
}

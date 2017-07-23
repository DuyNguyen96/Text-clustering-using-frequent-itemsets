import java.util.Comparator;

public class ItemComparator implements Comparator<Item> {

	@Override
	public int compare(Item o1, Item o2) {
		int freq1 = o1.getFreq();
		int freq2 = o2.getFreq();
		if(freq1 > freq2)
			return -1;
		else if(freq2 == freq1)
			return 0;
		else return 1;
	}

}

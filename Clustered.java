
public class Clustered {
	private int indexAdd;
	private int item;
	private int count;
	public Clustered(int indexAdd, int item) {
		super();
		this.indexAdd = indexAdd;
		this.item = item;
		this.count = 1;
	}
	public int getIndexAdd() {
		return indexAdd;
	}
	public int getItem() {
		return item;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public boolean sim(Clustered clustered){
		if(this.item == clustered.item)
			return true;
		else return false;
	}
}

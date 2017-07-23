
public class Item_Cluster {
	private int indexOfCluster;
	private Item item;
	
	
	public Item_Cluster(int indexOfCluster, Item item) {
		super();
		this.indexOfCluster = indexOfCluster;
		this.item = item;
	}


	public int getIndexOfCluster() {
		return indexOfCluster;
	}


	public void setIndexOfCluster(int indexOfCluster) {
		this.indexOfCluster = indexOfCluster;
	}


	public Item getItem() {
		return item;
	}


	public void setItem(Item item) {
		this.item = item;
	}
	
	
}

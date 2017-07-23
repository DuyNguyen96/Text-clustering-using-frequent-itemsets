
public class Item{
	private String item;
	private int freq;
	private double perOfFreq;
	
	public Item(String item, int freq) {
		this.item = item;
		this.freq = freq;
		perOfFreq = 0.0;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}
	
	public void increase(){
		this.freq++;
	}

	public double getPerOfFreq() {
		return perOfFreq;
	}

	public void setPerOfFreq(double perOfFreq) {
		this.perOfFreq = perOfFreq;
	}

	@Override
	public String toString() {
		return item;
	}

}

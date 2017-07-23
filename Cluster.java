import java.util.ArrayList;

public class Cluster {
	private ArrayList<Integer> listId;
	private Topic topic;
	private int learningRate;
	
	public Cluster(){
		listId = new ArrayList<>();
		topic = new Topic();
		learningRate = 0;
	}
	
	public Cluster(ArrayList<Integer> listId, Topic topic, int learningRate) {
		this.listId = listId;
		this.topic = topic;
		this.learningRate = learningRate;
	}

	public void setLearningRate(int learningRate) {
		this.learningRate = learningRate;
	}

	public int getLearningRate() {
		return learningRate;
	}


	public void addId(int id){
		listId.add(id);
	}

	public ArrayList<Integer> getListId() {
		return listId;
	}
	
	public boolean contain(PairDocument pairD){
		if(listId.contains(pairD.getX()) || listId.contains(pairD.getY()))
			return true;
		else return false;
	}
	
	public boolean contain(int x){
		return listId.contains(x);
	}
	
	public Topic getTopic(ArrayList<Item> listItem) {
		topic = new Topic(listItem);
		return topic;
	}
	
	public Topic getTopic() {
		return topic;
	}

	public void setTopic(ArrayList<Item> listItem) {
		this.topic = new Topic(listItem);
	}
	
	public void setTopicEnd(ArrayList<Item> listItem) {
		topic.setListItemCandidate(listItem);
	}

	public int addID(PairDocument pairD){
		if (!listId.contains(pairD.getX())){
			listId.add(pairD.getX());
			return pairD.getX();
		}
		if (!listId.contains(pairD.getY())){
			listId.add(pairD.getY());
			return pairD.getY();
		}
		return 0;
	}
	
	public void addIDNew(PairDocument pairD){
		listId.add(pairD.getX());
		listId.add(pairD.getY());
	}

	@Override
	public String toString() {
		String str = "";
		for (Integer i : listId){
			str += i.toString() + ", ";
		}
		return str;
	}
}

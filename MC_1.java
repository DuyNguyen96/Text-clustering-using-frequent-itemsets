import java.util.ArrayList;


public class MC_1 {
	private int[][] simMatrix;
	private int[][] similarityMatrix;
	private int[][] similsrityCluster;
	private int numOfDocument;
	private ArrayList<Cluster> listCluster;
	private ArrayList<Integer> listClustered;
	
	public MC_1(int[][] similatityMatrix, int numOfDocument){
		this.similarityMatrix = similatityMatrix;
		this.numOfDocument = numOfDocument;
		this.listCluster = new ArrayList<>();
		this.listClustered = new ArrayList<>();
		simMatrix = new int[similarityMatrix.length][similarityMatrix.length];
		for (int i = 0; i < similatityMatrix.length; i++){
			for (int j = 0; j < similatityMatrix.length; j++){
				simMatrix[i][j] = similatityMatrix[i][j];
			}
		}
	}
	
	public MC_1(){
		this.similarityMatrix = new int[100][100];
		this.numOfDocument = 0;;
		this.listCluster = new ArrayList<>();
		this.listClustered = new ArrayList<>();
	}

	public int getNumOfDocument() {
		return numOfDocument;
	}

	public void setNumOfDocument(int numOfDocument) {
		this.numOfDocument = numOfDocument;
	}

	public ArrayList<Cluster> getListCluster() {
		return listCluster;
	}

	public int findMaximumValue(){
		int maxValue = -1;
		for (int i = 0; i < numOfDocument - 1; i++){
			for (int j = i + 1; j < numOfDocument; j ++){
				if(similarityMatrix[i][j] > maxValue){
					maxValue = similarityMatrix[i][j];
				}
			}
		}
		return maxValue;
	}
	
	public int findMnimumValue(){
		int minimumValue = Integer.MAX_VALUE;
		for (int i = 0; i < numOfDocument - 1; i++){
			for (int j = i + 1; j < numOfDocument; j ++){
				if(similarityMatrix[i][j] < minimumValue && similarityMatrix[i][j] != 0){
					minimumValue = similarityMatrix[i][j];
				}
			}
		}
		return minimumValue;
	}
	
	public ArrayList<PairDocument> listPairDocument(int value, ArrayList<Integer> listUnCluster){
		ArrayList<PairDocument> list = new ArrayList<>();
		for (int i = 0; i < numOfDocument - 1; i ++){
			for (int j = i + 1; j < numOfDocument; j ++){
				if (similarityMatrix[i][j] == value){
					if (listUnCluster.contains(i) || listUnCluster.contains(j)){
						list.add(new PairDocument(i, j));
					}
				}
			}
		}
		return list;
	}
	
	public void writeOutPut(ArrayList<Document> listDC){
		Writer wr = new Writer();
		wr.writeListCluster_Topic(listCluster);
		int sttCluster = 0;
		for(Cluster c : listCluster){
			sttCluster++;
			ArrayList<Document> listDCOfCluster = listDCOfCluster(c, listDC);
			wr.writeListCluster(listDCOfCluster, "Output/" + "Cluster " + sttCluster);
		}
	}
	
	private ArrayList<Document> listDCOfCluster(Cluster c, ArrayList<Document> listDC){
		ArrayList<Document> listDCResult = new ArrayList<>();
		for(Integer i : c.getListId()){
			listDCResult.add(listDC.get(i));
		}
		return listDCResult;
	}
	
	public void cluster(){
		ArrayList<Cluster> listCluster = new ArrayList<>();
		ArrayList<Integer> listUnCluster = this.listUnCluster();
		int max = findMaximumValue();
		int min	= findMnimumValue();
		while(max >= min){
			ArrayList<PairDocument> listPairDocument = listPairDocument(max, listUnCluster);
			if (max != min)
				listCluster = this.cluster1(listPairDocument, listCluster, listUnCluster);
			else
				listCluster = this.cluster2(listPairDocument, listCluster, listUnCluster);
			setValueArrSim(max);
			this.listCluster = listCluster;
			max = findMaximumValue();
			listUnCluster = this.updateListUnCluster(listCluster, listUnCluster);
		}
		
		if(listUnCluster.size() != 0){
			for (Integer i: listUnCluster){
				if (i != -1){
					Cluster lastCluster = new Cluster();
					lastCluster.addId(i);
					listCluster.add(lastCluster);
				}				
			}	
		}
		
		this.listCluster = listCluster;
	}
	
	public void setTopic(ArrayList<ArrayList<Item>> listItem_DC){
		for(Cluster c : listCluster){
			ArrayList<Item> listItem = new ArrayList<>();
			for(Integer indexOfItem_Dc : c.getListId()){
				for(Item i : listItem_DC.get(indexOfItem_Dc)){
					if(listItem.isEmpty()){
						Item itemNew = new Item(i.getItem(), 1);
						listItem.add(itemNew);
					} else {
						int count = 0;
						for(Item itemTemp : listItem){
							count++;
							if(itemTemp.getItem().equals(i.getItem())){
								int freq = itemTemp.getFreq() + 1;
								itemTemp.setFreq(freq);
								break;
							} else if(count == listItem.size()){
								Item itemNew = new Item(i.getItem(), 1);
								listItem.add(itemNew);
								break;
							}
						}
					}
				}
			}
			c.setTopic(listItem);
		}
		// Laoi bo nhung item trung nhau trong chu de	
		for(int indexCluster = 0; indexCluster < listCluster.size() - 1; indexCluster++){
			ArrayList<Item> listItem = new ArrayList<>();
			for (Item i : listCluster.get(indexCluster).getTopic().getListItemCandidate()){
				Item_Cluster i_cluster = new Item_Cluster(indexCluster, i);
				for(int indexClusterNext = indexCluster + 1; indexClusterNext < listCluster.size(); indexClusterNext++){
					int sizeOfCluster = listCluster.get(indexClusterNext).getTopic().getListItemCandidate().size();
					int count = 0;
					for (Item iNext : listCluster.get(indexClusterNext).getTopic().getListItemCandidate()){
						count++;
						if((i.getItem().equals(iNext.getItem()) && i.getPerOfFreq() > iNext.getPerOfFreq())){
							i_cluster.setIndexOfCluster(indexCluster);
							listCluster.get(indexClusterNext).getTopic().getListItemCandidate().remove(iNext);
							break;
						} else if (count == sizeOfCluster && indexClusterNext == listCluster.size() - 1){
							i_cluster.setIndexOfCluster(indexCluster);
							listCluster.get(indexClusterNext).getTopic().getListItemCandidate().remove(iNext);
							break;
						} else if ((i.getItem().equals(iNext.getItem()) && i.getPerOfFreq() <= iNext.getPerOfFreq())){
							int sizeOfClusterNext = listCluster.get(indexClusterNext).getTopic().getListItemCandidate().size();
							if(sizeOfCluster < sizeOfClusterNext){
								i_cluster.setIndexOfCluster(indexCluster);
								listCluster.get(indexClusterNext).getTopic().getListItemCandidate().remove(iNext);
								break;
							} else {
								i_cluster.setIndexOfCluster(indexClusterNext);
								break;
							}
						}
						if (i.getItem().contains(iNext.getItem()) && i.getItem().contains(" ")){
							i_cluster.setIndexOfCluster(indexCluster);
							break;
						} else if (iNext.getItem().contains(i.getItem()) && iNext.getItem().contains(" ")){
							i_cluster.setIndexOfCluster(indexClusterNext);
							break;
						}
					}
				}
				if (i_cluster.getIndexOfCluster() == indexCluster){
					listItem.add(i);
				}
			}
			listCluster.get(indexCluster).setTopicEnd(listItem);
		}
		
		
		
	}
	
	public ArrayList<Integer> listUnCluster(){
		ArrayList<Integer> list = new ArrayList<>();
		for(int i = 0; i < numOfDocument; i++)
			list.add(i);
		return list;
	}
	
	private ArrayList<Integer> updateListUnCluster(ArrayList<Cluster> listCluster, ArrayList<Integer> listUnCluster){
		for (Cluster cl : listCluster) {
			for (Integer i : cl.getListId()){
			if(listUnCluster.contains(i))
				listUnCluster.remove(i);
			}
		}
		return listUnCluster;
	}
	
	private ArrayList<Cluster> cluster(ArrayList<PairDocument> listPairD, ArrayList<Cluster> listClusterCurr, ArrayList<Integer> listUnCluster){
		ArrayList<Cluster> lstCluster = new ArrayList<>();
		lstCluster = this.coppy(listClusterCurr);
		ArrayList<Clustered> listClustered = new ArrayList<>();
		for(PairDocument pDC : listPairD){
			Clustered clustered = pro_Cluster(pDC, listClusterCurr);
			int contain = containClustered(clustered, listClustered);
			if(listClustered.isEmpty() && clustered != null){
				listClustered.add(clustered);
			} else if(clustered != null && contain != - 1){
				int count = listClustered.get(contain).getCount() + 1;
				listClustered.get(contain).setCount(count);
			} else if(contain == -1 && clustered != null){
				listClustered.add(clustered);
			}
		}
		
		for(Clustered clustered : listClustered){
			if(clustered != null && containItem(clustered.getItem(), lstCluster) == false){
				lstCluster.get(clustered.getIndexAdd()).addId(clustered.getItem());
			}
		}
			
		for (PairDocument pairDocument : listPairD) {
				if(checkCreateNewCluster(lstCluster, pairDocument)){
					Cluster cl = new Cluster();
					cl.addIDNew(pairDocument);
					lstCluster.add(cl);
				}
		}
		
		return lstCluster;
		
	}
	
	private ArrayList<Cluster> cluster1(ArrayList<PairDocument> listPairD, ArrayList<Cluster> listClusterCurr, ArrayList<Integer> listUnCluster){
		ArrayList<Cluster> lstCluster = new ArrayList<>();
		lstCluster = this.coppy(listClusterCurr);
		for(PairDocument pDC : listPairD){
			if (listUnCluster.contains(pDC.getX()) && listUnCluster.contains(pDC.getY())){
				Cluster c = new Cluster();
				c.addIDNew(pDC);
				listUnCluster.set(listUnCluster.indexOf(pDC.getX()), -1);
				listUnCluster.set(listUnCluster.indexOf(pDC.getY()), -1);
				lstCluster.add(c);
			}
		}
		
		for(PairDocument pDC : listPairD){
			if (listUnCluster.contains(pDC.getX())){
				for (Cluster c : lstCluster){
					if (c.getListId().contains(pDC.getY())){
						c.addId(pDC.getX());
						listUnCluster.set(listUnCluster.indexOf(pDC.getX()), -1);
					}
				}
			} else if (listUnCluster.contains(pDC.getY())){
				for (Cluster c : lstCluster){
					if (c.getListId().contains(pDC.getX())){
						c.addId(pDC.getY());
						listUnCluster.set(listUnCluster.indexOf(pDC.getY()), -1);
					}
				}
			}
		}
		
		return lstCluster;
	}
	
	private ArrayList<Cluster> cluster2(ArrayList<PairDocument> listPairD, ArrayList<Cluster> listClusterCurr, ArrayList<Integer> listUnCluster){
		ArrayList<Cluster> lstCluster = new ArrayList<>();
		lstCluster = this.coppy(listClusterCurr);
		for(PairDocument pDC : listPairD){
			if (listUnCluster.contains(pDC.getX()) && listUnCluster.contains(pDC.getY())){
				Cluster c = new Cluster();
				c.addIDNew(pDC);
				listUnCluster.set(listUnCluster.indexOf(pDC.getX()), -1);
				listUnCluster.set(listUnCluster.indexOf(pDC.getY()), -1);
				lstCluster.add(c);
			}
		}
		return lstCluster;
	}
	
	
	private boolean containItem(int item, ArrayList<Cluster> lstCluster){
		for (Cluster cluster : lstCluster) {
			if(cluster.contain(item))
				return true;
		}
		return false;
	}
	
	
	private int containClustered(Clustered clustered, ArrayList<Clustered> lstClustered){
		for(Clustered c : lstClustered){
			if(c != null && clustered != null && c.getIndexAdd() == clustered.getIndexAdd() && c.getItem() == clustered.getItem()){
				return lstClustered.indexOf(c);
			}
		}
		return -1;
	}
	
	private Clustered pro_Cluster(PairDocument pDC, ArrayList<Cluster> lstCluster){
		for(Cluster c : lstCluster){
			if(c.contain(pDC.getX())){
				return new Clustered(lstCluster.indexOf(c), pDC.getY());
			}
			if(c.contain(pDC.getY())){
				return new Clustered(lstCluster.indexOf(c), pDC.getX());
			}
		}
		return null;
	}
	
	public ArrayList<PairDocument> findPairSim(PairDocument pDC, ArrayList<PairDocument> listPairD){
		ArrayList<PairDocument> lstPairDCSim = new ArrayList<>();
		lstPairDCSim.add(pDC);
		int next = listPairD.indexOf(pDC);
		for (int i = next + 1; i < listPairD.size(); i++){
			if(listPairD.get(next).getX() == listPairD.get(i).getX() || listPairD.get(next).getX() == listPairD.get(i).getY()){
				lstPairDCSim.add(listPairD.get(i));
			} else
				break;
		}
		return lstPairDCSim;
	}

	
	private void setValueArrSim(int value){
		for (int i = 0; i < numOfDocument - 1; i++){
			for (int j = i + 1; j < numOfDocument; j ++){
				if (similarityMatrix[i][j] == value)
					similarityMatrix[i][j] = 0;
			}
		}
	}
	
	private ArrayList<Cluster> coppy(ArrayList<Cluster> listCluster){
		ArrayList<Cluster> lst = new ArrayList<>();
		for (Cluster cluster : listCluster) {
			Cluster cl = new Cluster();
			for (Integer id : cluster.getListId()) {
				cl.addId(id);
			}
			lst.add(cl);
		}
		return lst;
	}
	
	
	private boolean checkCreateNewCluster(ArrayList<Cluster> listCluster, PairDocument pairD){
		for (Cluster cluster : listCluster) {
			if(cluster.getListId().contains(pairD.getX()) || cluster.getListId().contains(pairD.getY()))
				return false;
		}
		return true;
	}
	
	public ArrayList<ArrayList<String>> dataBuildFPTree(ArrayList<Document> listDC, int indexCluster){
		ArrayList<ArrayList<String>> dataSet = new ArrayList<>();
		for(Integer i : listCluster.get(indexCluster).getListId()){
			ArrayList<String> lstWordOfDC = new ArrayList<>();
			lstWordOfDC = listDC.get(i).getListSequenceOfWord();
			dataSet.add(lstWordOfDC);
		}
		return dataSet;
	}
	
	public void normalize(int numOfCluster){
		//ArrayList<Cluster> listClusterNor = new ArrayList<>(listCluster);
		ArrayList<Integer> listChecked = new ArrayList<>();
		ArrayList<Cluster> listClusterWithMinimumLearningRate = new ArrayList<>();
		int minimumLearningRate = findMinimumLearningRate();
		listClusterWithMinimumLearningRate = listClusterWithMinimumLearningRate(minimumLearningRate);
		simCluster(listClusterWithMinimumLearningRate);
		
		int maximunSimC = findMaxOfSimC(listClusterWithMinimumLearningRate);
		boolean flag = true;
		while(maximunSimC > 0 && listCluster.size() > numOfCluster){
			int indexCluster1 = listCluster.indexOf(findClusterMinSize(listClusterWithMinimumLearningRate, listChecked));
			simCluster(listCluster);
			if(findMaxOfSimC(listCluster) > 0){
				int indexCluster2 = indexOfCluster2(indexCluster1);
				if(similsrityCluster[indexCluster1][indexCluster2] > 0 || similsrityCluster[indexCluster2][indexCluster1] > 0){
					Cluster c1 = listCluster.get(indexCluster1);
					Cluster c2 = listCluster.get(indexCluster2);
					listCluster.add(mergeCluster(indexCluster1, indexCluster2));
					listCluster.remove(c1);
					listCluster.remove(c2);
					flag = true;
					listChecked.clear();
				}
				else {
					listChecked.add(indexCluster1);
					flag = false;
				}
			} 
			if (flag){
				minimumLearningRate = findMinimumLearningRate();
				listClusterWithMinimumLearningRate = listClusterWithMinimumLearningRate(minimumLearningRate);
				simCluster(listClusterWithMinimumLearningRate);
				maximunSimC = findMaxOfSimC(listClusterWithMinimumLearningRate);	
			}
		}
	}
	
	private Cluster findClusterMinSize(ArrayList<Cluster> listClusterWithMinimumLearningRate, ArrayList<Integer> lstChecked){
		int size = Integer.MAX_VALUE;
		Cluster cl = new Cluster();
		for (Cluster c : listClusterWithMinimumLearningRate){
			if (c.getListId().size() < size && !lstChecked.contains(listClusterWithMinimumLearningRate.indexOf(c))){
				size = c.getListId().size();
				cl = c;
			}
		}
		return cl;
	}
	
	private int findMinimumLearningRate(){
		int min = Integer.MAX_VALUE;
		for (Cluster c : listCluster){
			if(c.getLearningRate() < min){
				min = c.getLearningRate();
			}
		}
		return min;
	}
	
	private Cluster mergeCluster(int indexCluster1, int indexCluster2){
		ArrayList<Integer> listID = new ArrayList<>(listCluster.get(indexCluster1).getListId());
		for(Integer i : listCluster.get(indexCluster2).getListId()){
			listID.add(i);
		}
		Topic topic = new Topic(listCluster.get(indexCluster1).getTopic());
		for(Item i : listCluster.get(indexCluster2).getTopic().getListItemCandidate()){
			topic.addItem(i);
		}
		int learningRate = listCluster.get(indexCluster1).getLearningRate() + listCluster.get(indexCluster2).getLearningRate() + 1;
		Cluster c = new Cluster(listID, topic, learningRate);
		return c;
	}
	
	private int indexOfCluster2(int indexCluster1){
		int max = -1;
		int index = -1;
		for(int i = indexCluster1 + 1; i < listCluster.size(); i++){
			if (similsrityCluster[indexCluster1][i] > max){
				max = similsrityCluster[indexCluster1][i];
				index = i;
			}
		}
		for (int i = 0; i < indexCluster1 - 1; i++){
			if (similsrityCluster[i][indexCluster1] > max){
				max = similsrityCluster[i][indexCluster1];
				index = i;
			}
		}
		return index;
	}
	
	private int findMaxOfSimC(ArrayList<Cluster> listClusterWithMinimumLearningRate){
		int maxValue = -1;
		for (int i = 0; i <  listClusterWithMinimumLearningRate.size() - 1; i++){
			for (int j = i + 1; j < listClusterWithMinimumLearningRate.size(); j ++){
				if(similsrityCluster[i][j] > maxValue){
					maxValue = similsrityCluster[i][j];
				}
			}
		}
		return maxValue;
	}
	
	
	private ArrayList<Cluster> listClusterWithMinimumLearningRate(int minimumLearningRate){
		ArrayList<Cluster> listClusterWithMinimumLearningRate = new ArrayList<>();
		for (Cluster c : listCluster){
			if(c.getLearningRate() == minimumLearningRate){
				listClusterWithMinimumLearningRate.add(c);
			}
		}
		return listClusterWithMinimumLearningRate;
	}	
	
	public void simCluster(ArrayList<Cluster> listClusterWithMinimumLearningRate){
		similsrityCluster = new int[listClusterWithMinimumLearningRate.size()][listClusterWithMinimumLearningRate.size()];
		for(int i = 0; i < listClusterWithMinimumLearningRate.size() - 1; i++){
			ArrayList<Integer> listID = new ArrayList<>(listClusterWithMinimumLearningRate.get(i).getListId());
			for (int id : listID){
				for (int j = i + 1; j < listClusterWithMinimumLearningRate.size(); j++){
					ArrayList<Integer> listID2 = new ArrayList<>(listClusterWithMinimumLearningRate.get(j).getListId());
					for (int id2 : listID2){
						similsrityCluster[i][j] += simMatrix[id][id2];
					}
				}
			}
		}
	}
	
	
	
}

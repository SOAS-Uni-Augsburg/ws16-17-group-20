package isse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DomainStore {
	private ArrayList<ArrayList<ArrayList<Integer>>> store = new ArrayList<ArrayList<ArrayList<Integer>>>();

	public DomainStore(Sudoku sudoku) {
		for (int i = 0; i < 9; i++) {
			ArrayList<ArrayList<Integer>> lane = new ArrayList<ArrayList<Integer>>();
			for (int j = 0; j < 9; j++) {
				ArrayList<Integer> domainarray = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
				lane.add(domainarray);
			}
			store.add(lane);
		}

		initialize(sudoku);
	}

	public void addDomain(int row, int column, int value) {
		if (!store.get(row).get(column).contains(value)) {
			store.get(row).get(column).add(new Integer(value));
		}
	}

	public void removeDomain(int row, int column, int value) {
		if (!this.isDomainSolved(row, column) && store.get(row).get(column).contains(value)) {
			store.get(row).get(column).remove(new Integer(value));
		}
	}

	public boolean isDomainSolved(int row, int column) {
		boolean result = store.get(row).get(column).size() == 1;
		return result;
	}

	private void initialize(Sudoku sudoku) {
		for (int i = 0; i < 9; ++i) {
			for (int j = 0; j < 9; ++j) {
				if (!sudoku.isUnassigned(i, j)) {
					int value = sudoku.get(i, j);
					store.get(i).get(j).clear();
					store.get(i).get(j).add(value);
					updateDomainStoreWithNewFixValue(i, j, value);
				}
			}
		}
	}

	public void updateDomainStoreWithNewFixValue(int row, int column, int value) {
		removeValueFromRow(row, value);
		removeValueFromColumn(column, value);
		removeValueFromUnit(row, column, value);

	}

	private void removeValueFromRow(int row, int value) {
		for (int i = 0; i < 9; ++i) {
			this.removeDomain(row, i, value);
		}
	}

	private void removeValueFromColumn(int column, int value) {
		for (int i = 0; i < 9; ++i) {
			this.removeDomain(i, column, value);
		}
	}

	private void removeValueFromUnit(int row, int column, int value) {
		int unitRow = row / 3;
		int unitCol = column / 3;

		for (int i = unitRow * 3; i < unitRow * 3 + 3; ++i) {
			for (int j = unitCol * 3; j < unitCol * 3 + 3; ++j) {
				this.removeDomain(i, j, value);
			}
		}
	}

	public int getSolutionForVariable(int row, int column) {
		if (this.isDomainSolved(row, column)) {
			return store.get(row).get(column).get(0);
		} else {
			System.err.println("Invalid Call for Solution for Variable");
			return 0;
		}
	}

//	private void checkSingleOccurenceUnit() {
//		ArrayList<Integer> ocurrence = new ArrayList<Integer>();
//
//		for (int i = 0; i < 9; i = i + 3) {
//			for (int j = 0; j < 9; j = j + 3) {
//				if (!isDomainSolved(i, j)) {
//					for (int f = 0; f < store.get(i).get(j).size(); f++) {
//						ocurrence.add(store.get(i).get(j).get(f));
//					}
//				}
//				if (!isDomainSolved(i + 1, j)) {
//					for (int f = 0; f < store.get(i + 1).get(j).size(); f++) {
//						ocurrence.add(store.get(i + 1).get(j).get(f));
//					}
//				}
//				if (!isDomainSolved(i + 2, j)) {
//					for (int f = 0; f < store.get(i + 2).get(j).size(); f++) {
//						ocurrence.add(store.get(i + 2).get(j).get(f));
//					}
//				}
//				for (int f = 1; f <= 9; f++) {
//					if (Collections.frequency(ocurrence, f) == 1) {
//						if (!isDomainSolved(i, j)) {
//							if (store.get(i).get(j).contains(f)) {
//								store.get(i).get(j).clear();
//								store.get(i).get(j).add(f);
//							}
//						}
//						if (!isDomainSolved(i + 1, j)) {
//							if (store.get(i + 1).get(j).contains(f)) {
//								store.get(i + 1).get(j).clear();
//								store.get(i + 1).get(j).add(f);
//							}
//						}
//						if (!isDomainSolved(i + 2, j)) {
//							if (store.get(i + 2).get(j).contains(f)) {
//								store.get(i + 1).get(j).clear();
//								store.get(i + 2).get(j).add(f);
//							}
//						}
//
//					}
//				}
//
//			}
//			ocurrence.clear();
//		}
//	}
	
	public boolean checkSingleOccurenceUnit() {
		for(int i = 0; i < 3; ++i){
			for(int j = 0; j < 3; ++j){
				int[] singleOccurenceValue = findSingleOccurenceInUnit(i, j);
				if(singleOccurenceValue != null){
					store.get(singleOccurenceValue[0]).get(singleOccurenceValue[1]).clear();
					store.get(singleOccurenceValue[0]).get(singleOccurenceValue[1]).add(singleOccurenceValue[2]);
					updateDomainStoreWithNewFixValue(singleOccurenceValue[0], singleOccurenceValue[1], singleOccurenceValue[2]);
					return true;
				}
			}
		}
		return false;
	}
	
	private int[] findSingleOccurenceInUnit(int unitRow, int unitCol)
	{
		// add all possibilities in one array
		ArrayList<Integer> occurences = new ArrayList<Integer>();
		for(int i = 0; i < 3; ++i)
		{
			for(int j = 0; j < 3; ++j)
			{	
				if(this.isDomainSolved(unitRow * 3 + i, unitCol * 3 + j))
				{
					continue;
				}

				occurences.addAll(store.get(unitRow * 3 + i).get(unitCol * 3 + j));
			}
		}
		
		// check if a value is only possible one time
		int foundValue = 0;
		for(int i = 1; i <= 9; ++i)
		{
			if(Collections.frequency(occurences, i) == 1)
			{
				foundValue = i;
				break;
			}
		}
		
		if(foundValue == 0){
			return null;
		}
		
		// find cell in which the unique value is
		for(int i = 0; i < 3; ++i){
			for(int j = 0; j < 3; ++j){				
				if(store.get(unitRow * 3 + i).get(unitCol * 3 + j).contains(foundValue)){
					return new int[] {unitRow * 3 + i, unitCol * 3 + j, foundValue};
				}
			}
		}
		
		System.err.println("findSingleOccurenceInUnit: shit hits the fan");
		return null;
	}

	public int[] getCellWithSmallestDomain(){
		int minSize = Integer.MAX_VALUE;
		int[] pos = new int[] {-1, -1};
		for(int i = 0; i < 9; ++i){
			for(int j = 0; j < 9; ++j){
				if(store.get(i).get(j).size() > 1 && store.get(i).get(j).size() < minSize){
					minSize = store.get(i).get(j).size();
					pos = new int[] {i, j};
				}				
			}
		}
		return pos;
	}
	
	public ArrayList<Integer> getDomainForCell(int row, int column){
		return store.get(row).get(column);
	}
}

package isse;

import java.util.ArrayList;
import java.util.Random;

/**
 * A constraint solver for sudokus, can use propagation as well as branching
 * heuristics
 * 
 * @author alexander
 *
 */
public class ConstraintSolver implements Solver {
	protected StatisticsObject stats;
	DomainStore store;

	public ConstraintSolver(StatisticsObject stats) {
		super();
		this.stats = stats;
	}

	@Override
	public StatisticsObject getStats() {
		return stats;
	}

	@Override
	public Sudoku solve(Sudoku input) {
		stats.tickRuntime();
		Sudoku assignment = solveRek(input);
		stats.tockRuntime();
		return assignment;
	}

	private Sudoku solveRek(Sudoku input) {
		Sudoku assignment = new Sudoku(input);
		DomainStore store = new DomainStore(input);
		boolean hasFoundSingleOccurence = false;
		boolean hasAssignedValue = false;
		while (!assignment.isSolution(input) && assignment.isValid()) {
			hasFoundSingleOccurence = false;
			hasAssignedValue = false;
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if (assignment.isUnassigned(i, j) && store.isDomainSolved(i, j)) {
						int value = store.getSolutionForVariable(i, j);
						assignment.put(i, j, value);
						store.updateDomainStoreWithNewFixValue(i, j, value);
						hasAssignedValue = true;
					}
				}
			}

			if(!hasAssignedValue){
				hasFoundSingleOccurence = store.checkSingleOccurenceUnit();
			}			

			if (!hasAssignedValue && !hasFoundSingleOccurence) {
				// start heuristik
				int[] pos = store.getCellWithSmallestDomain();
				if (pos[0] == -1)
					return null;

				ArrayList<Integer> possibleSolutions = new ArrayList<Integer>();
				possibleSolutions.addAll(store.getDomainForCell(pos[0], pos[1]));				
				Sudoku result = null;
				while (!possibleSolutions.isEmpty()) {
					// if (possibleSolutions.size() == 1) {
					// break;
					// }
					
					int value = possibleSolutions.get(0);
					Sudoku rekSudoku = new Sudoku(assignment);
					rekSudoku.put(pos[0], pos[1], value);
					
					stats.markRecursiveCall();
					result = solveRek(rekSudoku);
					if (result == null) 
					{
						if (possibleSolutions.size() == 1)
						{
							return null;
						} 
						else 
						{
							possibleSolutions.remove(new Integer(value));
						}

					}
					else
					{
						return result;
					}
				}

			}

		}
		if (assignment.isSolution(input)) {
			return assignment;
		} else {
			return null;
		}
	}
}

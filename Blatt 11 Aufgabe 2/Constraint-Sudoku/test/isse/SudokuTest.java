package isse;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * This test case inspects sudoku implementations
 * regarding correctness and other non-functional requirements
 * such as efficiency
 * @author alexander
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SudokuTest {

	Sudoku easyProblem;
	Sudoku hardProblem;
	Sudoku mediumProblem;
	StatisticsObject stats;
	
	@Before
	public void setUp() throws Exception {
		SudokuReader reader = new SudokuReader();
		easyProblem = reader.readFromFile(new File("data/Easy04.sdk"));
		mediumProblem = reader.readFromFile(new File("data/Medium07.sdk"));
		hardProblem = reader.readFromFile(new File("data/Hard08.sdk"));		
		stats = new StatisticsObject();
	}

	private void printSolution(Sudoku solution) {
		System.out.println("Found solution: ");
		System.out.println(solution.toString());		
	}

	private void evaluateSolver(Solver solver,
			Sudoku problem) {
		Sudoku solution = solver.solve(problem);
		Assert.assertTrue(solution.isSolution(problem));
		printSolution(solution);
		StatisticsObject statsLocal = solver.getStats();
		System.out.println(statsLocal.toString());
	}

	
	@Test
	public void testProblem1Easy() {
		System.out.println("::::::::: EASY PROBLEM ::::::::");
		System.out.println(easyProblem.toString());
				
		Solver btSolver = new BacktrackSolver(stats);
		evaluateSolver(btSolver, easyProblem);
		StatisticsObject newStats = new StatisticsObject();
		
		// your turn 
		Solver constraintSolver = new ConstraintSolver(newStats);
		evaluateSolver(constraintSolver, easyProblem);
		
		Assert.assertTrue(newStats.recursiveCalls < stats.recursiveCalls);
		System.out.println("::::::::: END EASY PROBLEM ::::");
	}


	@Test
	public void testProblem2Medium() {
		System.out.println("::::::::: MEDIUM PROBLEM ::::::");
		System.out.println(mediumProblem.toString());
		
		Solver btSolver = new BacktrackSolver(stats);
		evaluateSolver(btSolver, mediumProblem);
		StatisticsObject newStats = new StatisticsObject();
		
		// your turn 
		Solver constraintSolver = new ConstraintSolver(newStats);
		evaluateSolver(constraintSolver, mediumProblem);

		Assert.assertTrue(newStats.recursiveCalls < stats.recursiveCalls);
		System.out.println("::::::::: END MEDIUM PROBLEM ::");
	}
	
	@Test
	public void testProblem3Hard() {
		System.out.println("::::::::: HARD PROBLEM ::::::::");
		System.out.println(hardProblem.toString());
		Solver btSolver = new BacktrackSolver(stats);
	    evaluateSolver(btSolver, hardProblem);
		StatisticsObject newStats = new StatisticsObject();
		
		// your turn 
		Solver constraintSolver = new ConstraintSolver(newStats);
		evaluateSolver(constraintSolver, hardProblem);

		Assert.assertTrue(newStats.recursiveCalls < stats.recursiveCalls);
		System.out.println("::::::::: END HARD PROBLEM ::::");
	}
	
	@Test
	public void testAllEasySudokus() throws Exception{
		System.out.println(":::: TestStarting ::::");
		System.out.println("Solving all easy sudokus!");
		int sumRecCallsForBackTrackingSolver = 0;
		int sumRecCallsForConstraintSolver = 0;
		
		SudokuReader reader = new SudokuReader();
		String relativePath = "Pack1/Easy";
		String fileEnding = ".sdk";
		for(int i = 1; i <= 10; ++i){
			StatisticsObject backTrackingStats = new StatisticsObject();
			StatisticsObject constraintsStats = new StatisticsObject();
			Solver backTrackingSolver = new BacktrackSolver(backTrackingStats);
			Solver constraintSolver = new ConstraintSolver(constraintsStats);
			
			String concatenated;
			if(i < 10){
				concatenated = relativePath + "0" + i + fileEnding;
			}
			else {
				concatenated = relativePath + i + fileEnding;
			}
			File f = new File(concatenated);
			Sudoku currentSudoku = reader.readFromFile(f);
			System.out.println("Solving " + i + ". Easy Sudoku");
			System.out.println("Using BackTrack Algorithm");
			evaluateSolver(backTrackingSolver, currentSudoku);
			System.out.println("Using Constraint Algorithm");
			evaluateSolver(constraintSolver, currentSudoku);
			sumRecCallsForBackTrackingSolver += backTrackingStats.recursiveCalls;
			sumRecCallsForConstraintSolver += constraintsStats.recursiveCalls;
		}
		
		Assert.assertTrue(sumRecCallsForConstraintSolver < sumRecCallsForBackTrackingSolver);
		System.out.println("Finished Solving all easy sudokus!");
		System.out.println("Backtracking Solver used " + sumRecCallsForBackTrackingSolver + " recursive calls");
		System.out.println("Constraint Solver used " + sumRecCallsForConstraintSolver + " recursive calls");
		System.out.println(":::: TestFinished ::::");
	}
	
	@Test
	public void testAllMediumSudokus() throws Exception{
		System.out.println(":::: TestStarting ::::");
		System.out.println("Solving all medium sudokus!");
		int sumRecCallsForBackTrackingSolver = 0;
		int sumRecCallsForConstraintSolver = 0;
		
		SudokuReader reader = new SudokuReader();
		String relativePath = "Pack1/Medium";
		String fileEnding = ".sdk";
		for(int i = 1; i <= 10; ++i){
			StatisticsObject backTrackingStats = new StatisticsObject();
			StatisticsObject constraintsStats = new StatisticsObject();
			Solver backTrackingSolver = new BacktrackSolver(backTrackingStats);
			Solver constraintSolver = new ConstraintSolver(constraintsStats);
			
			String concatenated;
			if(i < 10){
				concatenated = relativePath + "0" + i + fileEnding;
			}
			else {
				concatenated = relativePath + i + fileEnding;
			}
			File f = new File(concatenated);
			Sudoku currentSudoku = reader.readFromFile(f);
			System.out.println("Solving " + i + ". Medium Sudoku");
			System.out.println("Using BackTrack Algorithm");
			evaluateSolver(backTrackingSolver, currentSudoku);
			System.out.println("Using Constraint Algorithm");
			evaluateSolver(constraintSolver, currentSudoku);
			sumRecCallsForBackTrackingSolver += backTrackingStats.recursiveCalls;
			sumRecCallsForConstraintSolver += constraintsStats.recursiveCalls;
		}
		
		Assert.assertTrue(sumRecCallsForConstraintSolver < sumRecCallsForBackTrackingSolver);
		System.out.println("Finished Solving all medium sudokus!");
		System.out.println("Backtracking Solver used " + sumRecCallsForBackTrackingSolver + " recursive calls");
		System.out.println("Constraint Solver used " + sumRecCallsForConstraintSolver + " recursive calls");
		System.out.println(":::: TestFinished ::::");
	}
	
	@Test
	public void testAllHardSudokus() throws Exception{
		System.out.println(":::: TestStarting ::::");
		System.out.println("Solving all hard sudokus!");
		int sumRecCallsForBackTrackingSolver = 0;
		int sumRecCallsForConstraintSolver = 0;
		
		SudokuReader reader = new SudokuReader();
		String relativePath = "Pack1/Hard";
		String fileEnding = ".sdk";
		for(int i = 1; i <= 10; ++i){
			StatisticsObject backTrackingStats = new StatisticsObject();
			StatisticsObject constraintsStats = new StatisticsObject();
			Solver backTrackingSolver = new BacktrackSolver(backTrackingStats);
			Solver constraintSolver = new ConstraintSolver(constraintsStats);
			
			String concatenated;
			if(i < 10){
				concatenated = relativePath + "0" + i + fileEnding;
			}
			else {
				concatenated = relativePath + i + fileEnding;
			}
			File f = new File(concatenated);
			Sudoku currentSudoku = reader.readFromFile(f);
			System.out.println("Solving " + i + ". Hard Sudoku");
			System.out.println("Using BackTrack Algorithm");
			evaluateSolver(backTrackingSolver, currentSudoku);
			System.out.println("Using Constraint Algorithm");
			evaluateSolver(constraintSolver, currentSudoku);
			sumRecCallsForBackTrackingSolver += backTrackingStats.recursiveCalls;
			sumRecCallsForConstraintSolver += constraintsStats.recursiveCalls;
		}
		
		Assert.assertTrue(sumRecCallsForConstraintSolver < sumRecCallsForBackTrackingSolver);
		System.out.println("Finished Solving all medium sudokus!");
		System.out.println("Backtracking Solver used " + sumRecCallsForBackTrackingSolver + " recursive calls");
		System.out.println("Constraint Solver used " + sumRecCallsForConstraintSolver + " recursive calls");
		System.out.println(":::: TestFinished ::::");
	}
}

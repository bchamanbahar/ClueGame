package experiment;
import java.util.*;
public class IntBoard {
private BoardCell[][] grid;
private Map<BoardCell, Set<BoardCell>> AdjMatrix;
private Set<BoardCell> visitedList;
private Set<BoardCell> targetList;
public void calcAdjacencies() {
	AdjMatrix = new HashMap<BoardCell, Set<BoardCell>>();
	for(int i = 0; i < grid.length; i++) {
		for(int j = 0; j < grid[i].length; j++) {
			Set<BoardCell> temp = new HashSet<BoardCell>();
			if (i<grid.length-1) {
				temp.add(grid[i+1][j]);
			}
			if (i>0) {
				temp.add(grid[i-1][j]);
			}
			if (j<grid[i].length-1) {
				temp.add(grid[i][j+1]);
			}
			if (j>0) {
				temp.add(grid[i][j-1]);
			}
			AdjMatrix.put(grid[i][j], temp);
		}
	}
}


public Set<BoardCell> getAdjList(BoardCell c) {
	return AdjMatrix.get(c);
}
public IntBoard(BoardCell[][] grid) {
	super();
	this.grid = grid;
	initializeCells();
    calcAdjacencies();
}
private void initializeCells() {
	for(int i = 0; i < grid.length; i++) {
		for(int j = 0; j < grid[i].length; j++) {
			BoardCell cell = new BoardCell(i, j);
			grid[i][j] = cell;
		}
	}
}
public void calcTargets(BoardCell startCell, int pathLength) {
	visitedList = new HashSet<BoardCell>();
	targetList = new HashSet<BoardCell>();
	visitedList.add(startCell);
	findAllTargets(startCell, pathLength);
	
}

public void findAllTargets(BoardCell thisCell, int numSteps) {
	for (BoardCell c : getAdjList(thisCell)) {
		if (visitedList.contains(c)) {
			continue;
		}
		else {
			visitedList.add(c);
			if (numSteps==1) {
				targetList.add(c);
			}
			else {
				findAllTargets(c, numSteps-1);
				
			}
			visitedList.remove(c); 
		}
	}
	
}
public Set<BoardCell> getTargets() {
	return targetList;
}


public BoardCell getCell(int i, int j) {
	return grid[i][j];
}

}

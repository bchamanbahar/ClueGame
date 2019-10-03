package experiment;
import java.util.*;
public class IntBoard {
private BoardCell[][] grid;
private Map<BoardCell, Set<BoardCell>> AdjMatrix;
private Set<BoardCell> visitedList;
private Set<BoardCell> targetList;
void calcAdjacencies() {
	AdjMatrix = new HashMap<BoardCell, Set<BoardCell>>();
	for(int i = 0; i < grid.length; i++) {
		for(int j = 0; j < grid[i].length; j++) {
			Set<BoardCell> temp = new HashSet<BoardCell>();

		}
	}
}
//HI

void AdjList() {

}
public IntBoard(BoardCell[][] grid) {
	super();
	this.grid = grid;
    calcAdjacencies();
}
void calcTargets(int startCell, int endCell) {
	
}
Set<BoardCell> getTargets() {
	return null;
}

}

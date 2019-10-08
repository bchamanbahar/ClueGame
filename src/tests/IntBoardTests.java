package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import experiment.BoardCell;
import experiment.IntBoard;

class IntBoardTests {
	private IntBoard board;
	@Before
	public void beforeAll() {
		//creates 4x4 grid
		BoardCell[][] grid = new BoardCell[4][4];
		board = new IntBoard(grid);
		
	}
	@Test
	//tests adjacency of [0][0]
	void testAdjacency0() {
		beforeAll();
		BoardCell cell = board.getCell(0,0);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertEquals(2, testList.size());
	}
	//tests adjacency of [1][1]
	@Test
	void testAdjacency1() {
		beforeAll();
		BoardCell cell = board.getCell(1,1);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertEquals(4, testList.size());
	}
	//tests adjacency of [2][2]
	@Test
	void testAdjacency2() {
		beforeAll();
		BoardCell cell = board.getCell(2,2);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertTrue(testList.contains(board.getCell(3, 2)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertEquals(4, testList.size());
	}
	//tests adjacency of [3][3]
	@Test
	void testAdjacency3() {
		beforeAll();
		BoardCell cell = board.getCell(3,3);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(3, 2)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertEquals(2, testList.size());
	}
	//tests adjacency of [1][3]
	@Test
	void testAdjacency4() {
		beforeAll();
		BoardCell cell = board.getCell(1,3);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(0, 3)));
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertEquals(3, testList.size());
	}
	//tests adjacency of [3][0]
	@Test
	void testAdjacency5() {
		beforeAll();
		BoardCell cell = board.getCell(3,0);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(2, 0)));
		assertTrue(testList.contains(board.getCell(3, 1)));
		assertEquals(2, testList.size());
	}
	
	//Tests targets from [1][1] with a roll of 1
	@Test
	public void testTargets1_1() {
		beforeAll();
		BoardCell cell = board.getCell(1, 1);
		board.calcTargets(cell, 1);
		Set targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
	}
	//Tests targets from [0][0] with a roll of 3
	@Test
	public void testTargets0_3() {
		beforeAll();
		BoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 3);
		Set targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 0)));
		
	}
	//Tests targets from [1][1] with a roll of 2
	@Test
	public void testTargets1_2() {
		beforeAll();
		BoardCell cell = board.getCell(1, 1);
		board.calcTargets(cell, 2);
		Set targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(0, 0)));
		assertTrue(targets.contains(board.getCell(1, 3)));
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(2, 0)));
		assertTrue(targets.contains(board.getCell(2, 2)));
		assertTrue(targets.contains(board.getCell(3, 1)));
	}
	//Tests targets from [3][3] with a roll of 3
	@Test
	public void testTargets3_3() {
		beforeAll();
		BoardCell cell = board.getCell(3, 3);
		board.calcTargets(cell, 3);
		Set targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(3, 2)));
		assertTrue(targets.contains(board.getCell(2, 3)));
	}
	//Tests targets from [1][0] with a roll of 4
	@Test
	public void testTargets1_4() {
		beforeAll();
		BoardCell cell = board.getCell(1, 0);
		board.calcTargets(cell, 4);
		Set targets = board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(2, 3)));
		assertTrue(targets.contains(board.getCell(3, 2)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(0, 1)));
	}
	//Tests targets from [2][3] with a roll of 5
	@Test
	public void testTargets2_5() {
		beforeAll();
		BoardCell cell = board.getCell(2, 3);
		board.calcTargets(cell, 5);
		Set targets = board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCell(3, 1)));
		assertTrue(targets.contains(board.getCell(3, 3)));
		assertTrue(targets.contains(board.getCell(0, 0)));
		assertTrue(targets.contains(board.getCell(2, 2)));
		assertTrue(targets.contains(board.getCell(1, 3)));
		assertTrue(targets.contains(board.getCell(2, 0)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(0, 2)));
	}
	//Tests targets from [2][2] with a roll of 6
	@Test
	public void testTargets2_6() {
		beforeAll();
		BoardCell cell = board.getCell(2, 2);
		board.calcTargets(cell, 2);
		Set targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(2, 0)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(3, 3)));
		assertTrue(targets.contains(board.getCell(3, 1)));
		assertTrue(targets.contains(board.getCell(1, 3)));
	}
	

}

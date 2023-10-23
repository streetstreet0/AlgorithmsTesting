import java.util.Comparator;

/**
 * This class contains all of the unnecessary methods that I created for my own curiosity.
 */
public class CuriosityTestingRig extends TestingRig {
	
	public CuriosityTestingRig() {
		
	}

	/** Shuffles the first few integers of an array of integers.
	 * 
	 * Shuffles items 0 through to subsetSize-1. 
	 * Warning: subsetSize should be less than or equal to the size of the data array.
	 * MARKING: This was added for my own entertainment.
	 * 
	 * @param data Array of integers you wish to suffle a subset of
	 * @param subsetSize The size of the subset you wish to shuffle. This cannot exceed the size of data.
	 */
	public void shuffleSubset(int[] data, int subsetSize) {
		// only iterate through values within this subset
		for (int i=0; i<subsetSize; i++) {
			// choose a random spot to put this value. 
			// Note: it is possible for a value to get shuffled back to its original location.
			int randomIndex = (int)(Math.random() * subsetSize);
			// swap the values
			swap(data, i, randomIndex);
		}
	}
	
	
	/**
	 * Shuffles an array of integers.
	 * 
	 * MARKING: This was added for my own entertainment.
	 * 
	 * @param data The array of integers to be shuffled.
	 */
	public void shuffle(int[] data) {
		for (int i=0; i<data.length; i++) {
			// choose a random spot to put this value. 
			// Note: it is possible for a value to get shuffled back to its original location.
			int randomIndex = (int)(Math.random() * data.length);
			// swap the values
			swap(data, i, randomIndex);
		}
	}
	
	/**
	 * Checks if the first few values of the array are sorted.
	 * 
	 * Checks the first subsetSize values of the array to see if they are sorted.
	 * MARKING: This was added for my own entertainment.
	 * 
	 * @param data The array you wish to sort.
	 * @param subsetSize Size of the current subset that bogobogo sort is working on.
	 * @param comp Comparator for the ordering you wish to use.
	 * @return True if these values are sorted, False if they are not.
	 */
	public boolean isSortedSubset(int[] data, int subsetSize, Comparator<Integer> comp) {
		for (int i=1; i<subsetSize; i++) {
			// if a single element is out of order, the subset is not sorted
			if (comp.compare(data[i], data[i-1]) < 0) {
				return false;
			}
		}
		// if every element is in order it is sorted
		return true;
	}
	
	/** 
	 * BogoSorts an array of integers.
	 * 
	 * BogoSort is based on guess and check. It is extremely inefficient.
	 * MARKING: This was added for my own entertainment.
	 * 
	 * @param data The array you wish to sort.
	 * @param comp Comparator for the ordering you wish to use.
	 */
	public void bogoSort(int[] data, Comparator<Integer> comp) {
		// shuffle the list until it is sorted.
		// Note: this implementation can actually make it one of the fasted algorithms for an already sorted list.
		while (!isSorted(data, comp)) {
			shuffle(data);
		}
	}
	
	/** 
	 * BozoSorts an array of integers.
	 * 
	 * BozoSort is based on BogoSort. Instead of shuffling the whole list, it just swaps two random values in the array.
	 * MARKING: This was added for my own entertainment.
	 * 
	 * @param data The array you wish to sort.
	 * @param comp Comparator for the ordering you wish to use.
	 */
	public void bozoSort(int[] data, Comparator<Integer> comp) {
		// swap random pairs of elements until the array is sorted.
		// Note: this implementation can actually make it one of the fasted algorithms for an already sorted list.
		while (!isSorted(data, comp)) {
			swap(data, (int)(Math.random() * data.length), (int)(Math.random() * data.length));
		}
	}
	
	
	/** 
	 * Recursively run BogobogoSort, the slowest sorting algorithm.
	 * 
	 * BogobogoSort was designed by someone who thought BogoSort was far too efficient.
	 * BogobogoSort will not sort a reasonably sized list before the heat death of the universe.
	 * MARKING: This was added for my own entertainment.
	 * 
	 * @param data The array you wish to sort.
	 * @param comp Comparator for the ordering you wish to use.
	 */
	public void bogobogoSort(int[] data, Comparator<Integer> comp) {
		// start with a subset of size 2.
		// BogobogoSort recursively sorts subsets of the array using bogosort.
		// If at any point a subset is not sorted, it starts the process again.
		boolean sorted = false;
		// this loops until BogobogoSort sorts the array
		// using a loop prevents the stackoverflow error that occured in the recursive variation
		while (!sorted) {
			sorted = bogobogoSort(data, 2, comp);
		}
	}
	
	
	/** 
	 * Recursively run BogobogoSort, the slowest sorting algorithm.
	 * 
	 * BogobogoSort will not sort a reasonably sized list before the heat death of the universe.
	 * MARKING: This was added for my own entertainment.
	 * 
	 * @param data The array you wish to sort.
	 * @param subsetSize Size of the current subset that bogobogo sort is working on.
	 * @param comp Comparator for the ordering you wish to use.
	 */
	private boolean bogobogoSort(int[] data, int subsetSize, Comparator<Integer> comp) {
		// shuffle the current subset
		shuffleSubset(data, subsetSize);
		
		if (isSortedSubset(data, subsetSize, comp)) {
			// if the current subset is the whole array, and it is sorted. Then it (somehow) sorted the array.
			if (data.length == subsetSize) {
				// true causes the loop to end and BogobogoSort to halt
				return true;
			}
			// otherwise, move on to the next subset.
			return bogobogoSort(data, subsetSize+1, comp);
		}
		else {
			// if at any point the subset is not sorted, go back to a subset of size 2.
			// false results in us repeating again.
			return false;
		}
	}
}

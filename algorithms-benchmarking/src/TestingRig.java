import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.HashMap;

public class TestingRig {
	// use the same comparator for all tests
	protected Comparator<Integer> comparator;
	
	public TestingRig() {
		comparator = new LessThanComparator();
	}
	
	/**
	 * Calculates the square of a number.
	 * 
	 * @param x Number to be squared.
	 * @return Square of that number
	 */
	public double square(double x) {
		return x * x;
	}
	
	/**
	 * Prints the sizes, averages, and standard deviations of a test to a text file.
	 * 
	 * @param sizeToAvgTime Hashmap of sizes to their average time.
	 * @param sizeToDeviation Hashmap of sizes to their standard deviation.
	 * @param fileName Name of the text file.
	 */
	public void printOutput(HashMap<Integer, Double> sizeToAvgTime, HashMap<Integer, Double> sizeToDeviation, String fileName) {
		try {
			File file = new File(fileName + "Output");
			file.createNewFile();
			PrintStream print = new PrintStream(file);
			
			// each line holds one type of data
			// The order of each line should be the same.
			// i.e. if size1 is first in its line, then its average should be first in the average line, and its deviation first in that line.
			// this makes it easy to copy/paste into python code for plotting
			String sizeLine = "";
			String avgTimeLine = "";
			String deviationLine = "";
			
			// need to build each line one size at a time
			for (int size : sizeToAvgTime.keySet()) {
				sizeLine += size + ", ";
				avgTimeLine += sizeToAvgTime.get(size) + ", ";
				deviationLine += sizeToDeviation.get(size) + ", ";
			}
			
			// need to remove the ", " at the end
			sizeLine = sizeLine.substring(0, sizeLine.length()-2);
			avgTimeLine = avgTimeLine.substring(0, avgTimeLine.length()-2);
			deviationLine = deviationLine.substring(0, deviationLine.length()-2);
			
			// now print the lines
			print.println(sizeLine);
			print.println(avgTimeLine);
			print.println(deviationLine);
			print.close();
		}
		catch (IOException error) {
			System.out.println("error: " + error);
		}
	}
	
	
	/** Tests the efficiency of a sorting algorithm at solving array of a given size.
	 * 
	 * This tests how quickly the algorithm randomly generated arrays of a given size and calculates the average time for each size.
	 * It also calculates the standard deviation of the times to sort, as an indicator of how accurate the average times are.
	 * These are then exported to a text file.
	 * 
	 * @param sizes An array of the sizes of lists that are going to be tested.
	 * @param maxRange The maximum spread of values within the randomly generated lists. Values within a list will be between 0 and maxRange (inclusive).
	 * @param numTests The number of tests that will be run for each array size.
	 * @param filePath The filepath for the exported test file.
	 * @param fileSuffix A suffix to be added to the end of the name of the export file
	 * @param algorithm Algorithm literal. The algorithm you wish to test.
	 */
	public void testSort(int[] sizes, int maxRange, int numTests, String filePath, String fileSuffix, Algorithm algorithm) {
		// save the averages and standard deviations to hashmaps for easy referencing.
		HashMap<Integer, Double> sizeToAvgTime = new HashMap<Integer, Double>();
		HashMap<Integer, Double> sizeToDeviation = new HashMap<Integer, Double>();
		
		// do all tests for one size, before moving onto the next size.
		// This is done to minimise the number of sums, averages, and standard deviations that need to be kept track of.
		for (int size : sizes) {
			System.out.println(size + ":");
			
			// the sum is used to calculate the average
			double sum = 0;
			// store the individual times in an array so we can use them to calculate the standard deviation
			// the standard deviation cannot be calculated until we have already calculated the average.
			double[] timesTaken = new double[numTests];
			
			for (int test=0; test<numTests; test++) {
				System.out.println("test " + test);
				
				// the array is randomly generated for each test.
				// The impact of any individual array is minimised by running multiple tests.
				int[] dataset = generateData(size, maxRange);
				
				// the time is stored in ms. 
				// This is because the tests are usually done on lists that take longer than 1ms to solve.
				// Often significantly longer.
				long startTime = System.nanoTime();
				long timeTaken = System.nanoTime() - startTime;
				// start time and time  initialised before switch statement, so compiler will believe that it will be initialised
				// real values that are used are calculated inside the switch statement so time taken in the switch statement
				// will not be counted against the time the algorithm takes
				switch (algorithm) {
				case QuickSort:
					startTime = System.nanoTime();
					quickSort(dataset, comparator);
					timeTaken = System.nanoTime() - startTime;
					break;
				case InsertionSort:
					startTime = System.nanoTime();
					insertionSort(dataset, comparator);
					timeTaken = System.nanoTime() - startTime;
					break;
				case BubbleSort:
					startTime = System.nanoTime();
					bubbleSort(dataset, comparator);
					timeTaken = System.nanoTime() - startTime;
					break;
				case HybridQuickSort:
					startTime = System.nanoTime();
					hybridQuickSort(dataset, comparator);
					timeTaken = System.nanoTime() - startTime;
					break;
				}
				
				
				// The algorithms should sort the set.
				// In the case that it doesn't this will warn us.
				if (!isSorted(dataset, comparator)) {
					for (int i=0; i<5; i++) {
						System.out.println("ERROR!");
					}
					return;
				}
				
				// start time needs be cast from long to double, so we don't get integer division.
				double timeTakenMs = ((double)timeTaken)/1000000;
				sum += timeTakenMs;
				timesTaken[test] = timeTakenMs;
			}
			
			// calculate the average and add it to the hashMap.
			// need to cast the sum to double to get normal division.
			double avg = sum/numTests;
			sizeToAvgTime.put(size, avg);
			
			// calculate the standard deviation
			double deviation = 0;
			for (int i=0; i<numTests; i++) {
				deviation += square(timesTaken[i] - avg);
			}
			deviation = deviation/numTests;
			deviation = Math.sqrt(deviation);
			sizeToDeviation.put(size, deviation);
		}
		
		// name of file determined by the algorithm
		String fileName = filePath;
		switch (algorithm) {
		case QuickSort:
			fileName += "QuickSort";
			break;
		case InsertionSort:
			fileName += "InsertionSort";
			break;
		case BubbleSort:
			fileName += "BubbleSort";
			break;
		case HybridQuickSort:
			fileName += "HybridQuickSort";
			break;
		}
		fileName += fileSuffix;
		
		// print the results to a text file
		printOutput(sizeToAvgTime, sizeToDeviation, fileName);
	}
	
	
	
	
	
	/** 
	 * Generates a randomly generated array of integers.
	 * 
	 * Each element in the array is a randomly generated number between 0 and maxRange.
	 * 
	 * 
	 * @param size Size of the array to be generated.
	 * @param maxRange Maximum value of an integer in the array.
	 * @return A randomly generated array of integers.
	 */
	public int[] generateData(int size, int maxRange) {
		int[] dataset = new int[size];
		for (int i=0; i<size; i++) {
			// due to integer casting need to add 1 to max range to have change of entry with that value.
			dataset[i] = (int)(Math.random()*(maxRange+1));
		}
		return dataset;
	}

	
	/**
	 * BubbleSorts an array of integers.
	 * 
	 * BubbleSort compares pairs of items i,i+1 in the array. 
	 * When it finds a pair that are out of order it swaps them.
	 * The lesser of these two elements then gets compared to the element before it, and swaps if they are out of order.
	 * This repeats until that item is in the correct place.
	 * It then continues on the next pair of items in the array
	 * 
	 * @param data The array you wish to sort.
	 * @param comp Comparator for the ordering you wish to use.
	 */
	public void bubbleSort(int[ ] data, Comparator<Integer> comp){
		for (int pos=data.length-1; pos >= 0; pos--) {
			for (int scan = 0; scan <= pos -1; scan++) {
				if (comp.compare(data[scan], data[scan+1]) > 0) {
					swap(data, scan, scan+1);
				}
			}
		}
	}
	
	
	/**
	 * InsertionSorts an array of integers.
	 * 
	 * InsertionSort treats the first item as a sorted list. 
	 * It then expands this list by adding the next element in the array to this sorted list in the correct place.
	 * It repeats this process until the whole list is sorted.
	 * 
	 * @param data The array you wish to sort.
	 * @param comp Comparator for the ordering you wish to use.
	 */
	public  void insertionSort(int[] data, Comparator<Integer> comp){
		// for each item, from 0, insert into place in the sorted region (0..i-1)
		for (int i=1; i<data.length; i++){
			int item = data[i];
			int place = i;
			while (place > 0  &&  comp.compare(item, data[place-1]) < 0){
				data[place] = data[place-1];
				place--;
			}
			data[place]= item;
		}
	}
	
	
	/**
	 * InsertionSorts an array of integers.
	 * 
	 * InsertionSorts a partition of an array.
	 * 
	 * @param data The array you wish to sort.
	 * @param low The smallest index of the array in the partition.
	 * @param high The largest index of the array in the partition.
	 * @param comp Comparator for the ordering you wish to use.
	 */
	public  void insertionSort(int[] data, int low, int high, Comparator<Integer> comp){
		// for each item, from low, insert into place in the sorted region (low..i-1)
		for (int i=low+1; i<=high; i++){
			int item = data[i];
			int place = i;
			while (place > 0  &&  comp.compare(item, data[place-1]) < 0){
				data[place] = data[place-1];
				place--;
			}
			data[place]= item;
		}
	}
	
	
	/**
	 * Hybrid-QuickSorts an array of integers.
	 * 
	 * If the array is small enough it will instead InsertionSort the list
	 * 
	 * @param data The array you wish to sort.
	 * @param comp Comparator for the ordering you wish to use.
	 */
	public void hybridQuickSort(int[] data, Comparator<Integer> comp){
		hybridQuickSort(data, 0, data.length-1, comp);
	}
	
	
	/**
	 * QuickSorts an array or subarray of integers.
	 * 
	 * QuickSort treats the first item as the pivot. 
	 * It places all numbers smaller than the pivot just to the right of the pivot.
	 * All of the numbers larger than the pivot will be to the right of all of these.
	 * The pivot then gets switched with the rightmost element that is less than it.
	 * The sublists to the left and right of the pivot are then solved using quicksort.
	 * 
	 * NOTE FOR MARKING: This has been slightly changed so now high is included instead of excluded.
	 * 
	 * @param data The array you wish to sort.
	 * @param low The smallest index of the array you want to include in the partition.
	 * @param high The largest index of the array you want to include in the partition.
	 * @param comp Comparator for the ordering you wish to use.
	 */
	private void hybridQuickSort(int[] data, int low, int high, Comparator<Integer> comp){
		if (high - low < 1) { 
			// only one item to sort, so it is already sorted. 
			return;
		}
		else if (high - low < 20) {
			// 20 or less items to sort, so use insertion sort.
			insertionSort(data, low, high, comp);
		}
		else {     
			// split into two parts,  mid = index of boundary
			int mid = partition(data, low, high, comp);
			
			// mid is already in the correct position, so do not try to sort it again
			hybridQuickSort(data, low, mid-1, comp);
			hybridQuickSort(data, mid+1, high, comp);
		}
	}
	
	
	/**
	 * QuickSorts an array of integers.
	 * 
	 * QuickSort treats the first item as the pivot. 
	 * It places all numbers smaller than the pivot just to the right of the pivot.
	 * All of the numbers larger than the pivot will be to the right of all of these.
	 * The pivot then gets switched with the rightmost element that is less than it.
	 * The sublists to the left and right of the pivot are then solved using quicksort.
	 * 
	 * @param data The array you wish to sort.
	 * @param comp Comparator for the ordering you wish to use.
	 */
	public void quickSort(int[] data, Comparator<Integer> comp){
		quickSort(data, 0, data.length-1, comp);
	}
	
	/**
	 * QuickSorts an array or subarray of integers.
	 * 
	 * QuickSort treats the first item as the pivot. 
	 * It places all numbers smaller than the pivot just to the right of the pivot.
	 * All of the numbers larger than the pivot will be to the right of all of these.
	 * The pivot then gets switched with the rightmost element that is less than it.
	 * The sublists to the left and right of the pivot are then solved using quicksort.
	 * 
	 * NOTE FOR MARKING: This has been slightly changed so now high is included instead of excluded.
	 * 
	 * @param data The array you wish to sort.
	 * @param low The smallest index of the array you want to include in the partition.
	 * @param high The largest index of the array you want to include in the partition.
	 * @param comp Comparator for the ordering you wish to use.
	 */
	private void quickSort(int[] data, int low, int high, Comparator<Integer> comp){
		if (high - low < 1) { 
			// only one item to sort, so it is already sorted. 
			return;
		}
		else {     
			// split into two parts,  mid = index of boundary
			int mid = partition(data, low, high, comp);
			
			// mid is already in the correct position, so do not try to sort it again
			quickSort(data, low, mid-1, comp);
			quickSort(data, mid+1, high, comp);
		}
	}
	
	
	/**
	 * Partitions the data for quicksort.
	 * 
	 * The first value of the subarray given by min and max is the pivot.
	 * Partitions the data so all of the values that are less than the pivot will be to the left of the pivot 
	 * and those greater than will be to the right.
	 * 
	 * NOTE FOR MARKING: This has been slightly changed so now max is included instead of excluded.
	 * 
	 * @param data The array you wish to sort.
	 * @param min The smallest index of the array you want to include in the partition.
	 * @param max The largest index of the array you want to include in the partition.
	 * @param comp Comparator for the ordering you wish to use.
	 */
	private int partition(int[] data, int min, int max, Comparator<Integer> comp){
		int pivot = data[min];  // Place to improve efficiency later.
		int scan = min+1;
		int mark = scan;
		while(scan <= max){
			if (comp.compare(data[scan], pivot) < 0){
				swap(data, scan, mark);
				mark++; 	      
			}
			scan++;
		}
		mark --;
		swap(data, min, mark);
		return mark;
	}
	
	
	/**
	 * Checks if the array is sorted.
	 * 
	 * Checks the first subsetSize values of the array to see if they are sorted.
	 * 
	 * @param data The array you wish to sort.
	 * @param comp Comparator for the ordering you wish to use.
	 * @return True if the array is sorted, False if it is not.
	 */
	public boolean isSorted(int[] data, Comparator<Integer> comp) {
		for (int i=1; i<data.length; i++) {
			if (comp.compare(data[i], data[i-1]) < 0) {
				return false;
			}
		}
		return true;
	}
	
	
	/** Swap the location of two values in an array.
	 * 
	 * @param data Array of integers.
	 * @param index1 One of the indexes you wish to swap.
	 * @param index2 The other index you wish to swap.
	 */
	public void swap(int[] data, int index1, int index2) {
		// need to store one of the data value in a temporary variable to avoid it being destroyed.
		int temp = data[index1];
		// swap the values
		data[index1] = data[index2];
		data[index2] = temp;
	}
	
	public static void main(String[] args) {
		TestingRig main = new TestingRig();
		// test the time each algorithm takes (small range)
//		int[] sizes = {10000, 20000, 30000, 40000, 50000};
//		main.testSort(sizes, 100000, 10, "tests/general test/", "", Algorithm.BubbleSort);
//		main.testSort(sizes, 100000, 10, "tests/general test/", "", Algorithm.InsertionSort);
//		main.testSort(sizes, 100000, 10, "tests/general test/", "", Algorithm.QuickSort);
		
		
		// test the time each algorithm takes (large range)
//		int[] sizes = {10, 100, 1000, 10000, 100000};
//		main.testSort(sizes, 100000, 10, "tests/large range/", "-large range", Algorithm.BubbleSort);
//		main.testSort(sizes, 100000, 10, "tests/large range/", "-large range", Algorithm.InsertionSort);
//		main.testSort(sizes, 100000, 10, "tests/large range/", "-large range", Algorithm.QuickSort);
		
		// compare the time each algorithm takes (small values)
//		int[] sizes = {100, 200, 300, 400, 500, 600, 700, 800, 900};
//		main.testSort(sizes, 100000, 1000, "tests/small values/", "-small values", Algorithm.InsertionSort);
//		main.testSort(sizes, 100000, 1000, "tests/small values/", "-small values", Algorithm.QuickSort);
		
		
		// compare the time each algorithm takes (smallest values)
//		int[] sizes = {10, 20, 30, 40, 50, 60, 70, 80, 90};
//		System.out.println("insertion");
//		main.testSort(sizes, 100000, 100000, "tests/vs hybridsort/", "-smallest values vs hybrid", Algorithm.InsertionSort);
//		System.out.println("quick");
//		main.testSort(sizes, 100000, 100000, "tests/vs hybridsort/", "-smallest values vs hybrid", Algorithm.QuickSort);
//		System.out.println("hybrid");
//		main.testSort(sizes, 100000, 100000, "tests/vs hybridsort/", "-smallest values", Algorithm.HybridQuickSort);
		
		// compare the time each algorithm takes (large range)
		int[] sizes = {10, 100, 1000, 10000, 100000};
		System.out.println("quick");
		main.testSort(sizes, 100000, 1000, "tests/vs hybridsort spread/", " - large range vs hybrid", Algorithm.QuickSort);
		System.out.println("hybrid");
		main.testSort(sizes, 100000, 1000, "tests/vs hybridsort spread/", " - large range", Algorithm.HybridQuickSort);
		
		
		// compare the time each algorithm takes (smallest values reverse order)
//		int[] sizes = {10, 20, 30, 40, 50, 60, 70, 80, 90};
//		main.testQuickSort(sizes, 100000, 100000, "tests/smallest values/", "-smallest values v2", Algorithm.QuickSort);
//		main.testInsertionSort(sizes, 100000, 100000, "tests/smallest values/", "-smallest values v2", Algorithm.InsertionSort);
		
		
		// test insertion sort actually sorts
//		int[] data = main.generateData(10000, 10000);
//		main.insertionSort(data, data.length, new LessThanComparator());
//		for (int i=0; i<data.length; i++) {
//			System.out.println(data[i]);
//		}
//		System.out.println(main.isSorted(data, new LessThanComparator()));
		
		
		// test bogobogosort
//		CuriosityTestingRig test = new CuriosityTestingRig(new LessThanComparator());
//		int[] data = main.generateData(6, 10);
//		long startTime = System.currentTimeMillis();
//		test.bogobogoSort(data, new LessThanComparator());
//		long timeTaken = System.currentTimeMillis() - startTime;
//		for (int item : data) {
//			System.out.println(item);
//		}
//		System.out.println(timeTaken + "ms");
	}

}

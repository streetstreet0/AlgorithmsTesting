import java.util.HashMap;

public class RedundantCodeStorage extends TestingRig {

	public RedundantCodeStorage() {
		// TODO Auto-generated constructor stub
	}

	/** Tests the efficiency of BubbleSort at solving array of a given size.
	 * 
	 * This tests how quickly BubbleSort randomly generated arrays of a given size and calculates the average time for each size.
	 * It also calculates the standard deviation of the times to sort, as an indicator of how accurate the average times are.
	 * These are then exported to a text file.
	 * 
	 * @param sizes An array of the sizes of lists that are going to be tested.
	 * @param maxRange The maximum spread of values within the randomly generated lists. Values within a list will be between 0 and maxRange (inclusive).
	 * @param numTests The number of tests that will be run for each array size.
	 * @param filePath The filepath for the exported test file.
	 * @param fileSuffix A suffix to be added to the end of the name of the export file
	 */
	public void testBubbleSort(int[] sizes, int maxRange, int numTests, String filePath, String fileSuffix) {
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
				bubbleSort(dataset, comparator);
				long timeTaken = System.nanoTime() - startTime;
				
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
		
		// print the results to a text file
		printOutput(sizeToAvgTime, sizeToDeviation, filePath + "bubbleSort" + fileSuffix);
	}
	
	
	/** Tests the efficiency of InsertionSort at solving array of a given size.
	 * 
	 * This tests how quickly InsertionSort randomly generated arrays of a given size and calculates the average time for each size.
	 * It also calculates the standard deviation of the times to sort, as an indicator of how accurate the average times are.
	 * These are then exported to a text file.
	 * 
	 * @param sizes An array of the sizes of lists that are going to be tested.
	 * @param maxRange The maximum spread of values within the randomly generated lists. Values within a list will be between 0 and maxRange (inclusive).
	 * @param numTests The number of tests that will be run for each array size.
	 * @param filePath The filepath for the exported test file.
	 * @param fileSuffix A suffix to be added to the end of the name of the export file
	 */
	public void testInsertionSort(int[] sizes, int maxRange, int numTests, String filePath, String fileSuffix) {
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
				insertionSort(dataset, comparator);
				long timeTaken = System.nanoTime() - startTime;
				
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
		
		// print the results to a text file
		printOutput(sizeToAvgTime, sizeToDeviation, filePath + "insertionSort" + fileSuffix);
	}
	
	
	/** Tests the efficiency of QuickSort at solving array of a given size.
	 * 
	 * This tests how quickly QuickSort randomly generated arrays of a given size and calculates the average time for each size.
	 * It also calculates the standard deviation of the times to sort, as an indicator of how accurate the average times are.
	 * These are then exported to a text file.
	 * 
	 * @param sizes An array of the sizes of lists that are going to be tested.
	 * @param maxRange The maximum spread of values within the randomly generated lists. Values within a list will be between 0 and maxRange (inclusive).
	 * @param numTests The number of tests that will be run for each array size.
	 * @param filePath The filepath for the exported test file.
	 * @param fileSuffix A suffix to be added to the end of the name of the export file
	 */
	public void testQuickSort(int[] sizes, int maxRange, int numTests, String filePath, String fileSuffix) {
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
				quickSort(dataset, comparator);
				long timeTaken = System.nanoTime() - startTime;
				
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
		
		// print the results to a text file
		printOutput(sizeToAvgTime, sizeToDeviation, filePath + "quickSort" + fileSuffix);
	}
	
	
	/** Tests the efficiency of hybrid QuickSort at solving array of a given size.
	 * 
	 * This tests how quickly hybrid QuickSort randomly generated arrays of a given size and calculates the average time for each size.
	 * It also calculates the standard deviation of the times to sort, as an indicator of how accurate the average times are.
	 * These are then exported to a text file.
	 * 
	 * @param sizes An array of the sizes of lists that are going to be tested.
	 * @param maxRange The maximum spread of values within the randomly generated lists. Values within a list will be between 0 and maxRange (inclusive).
	 * @param numTests The number of tests that will be run for each array size.
	 * @param filePath The filepath for the exported test file.
	 * @param fileSuffix A suffix to be added to the end of the name of the export file
	 */
	public void testHybridQuickSort(int[] sizes, int maxRange, int numTests, String filePath, String fileSuffix) {
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
				hybridQuickSort(dataset, comparator);
				long timeTaken = System.nanoTime() - startTime;
				
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
		
		// print the results to a text file
		printOutput(sizeToAvgTime, sizeToDeviation, filePath + "hybridQuickSort" + fileSuffix);
	}
}

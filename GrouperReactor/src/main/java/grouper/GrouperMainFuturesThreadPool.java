package grouper;

import static grouper.utils.VehicleUtils.makeRandomVehicle;
import grouper.controller.VehicleController;
import grouper.controller.impl.inmemory.VehicleControllerConcurentHashMapImpl;
import grouper.controller.impl.inmemory.VehicleControllerSyncListImpl;
import grouper.task.TaskStatistics;
import grouper.task.VehicleTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.persistence.EntityExistsException;

public class GrouperMainFuturesThreadPool {
	public final static int NUM_THREADS = Runtime.getRuntime().availableProcessors();
	public final static int INITIAL_DB_SIZE = 20000; //initially we start with 500000 Db records
	public final static long SIMULATION_DURATION_MS = 180000; //60 seconds
	static long  recordsAdded = 0, recordsDeleted = 0, recordsUpdated = 0, 
			getOperations = 0, bulkGetOperations = 0; 
	static double operationsPerSecond = 0;
	final static List<TaskStatistics> results = new ArrayList<>();


	public static void main(String[] args) {
		VehicleController controller = 
				new  VehicleControllerConcurentHashMapImpl(2*INITIAL_DB_SIZE);
		System.out.println("Filling DB with " + INITIAL_DB_SIZE + " records ...");
		fillInitialRecords(controller, INITIAL_DB_SIZE);
		ExecutorService exec = Executors.newFixedThreadPool(NUM_THREADS);
		CompletionService<TaskStatistics> ecs = new ExecutorCompletionService<>(exec);
		//submit tasks for concurrent execution
		for (int i = 0; i < NUM_THREADS; i++){
			System.out.println("Submitting task: TASK-" + i);
			ecs.submit(new VehicleTask("TASK-" + i, controller, SIMULATION_DURATION_MS));
		}
		
		// Wait for completion and print individul results
		for (int i = 0; i < NUM_THREADS; ++i) {
			try {
				TaskStatistics nextStat = ecs.take().get();
				results.add(nextStat); //block till next task finishes
				updateCounters(nextStat);
				System.out.println(nextStat); //block till next task finishes
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		//Print overall statistics
		StringBuilder builder = new StringBuilder();
		builder.append("\n\nAVERAGE STATISTICS: [recordsAdded=").append((100 * recordsAdded / NUM_THREADS) / 100D)
				.append(", recordsDeleted=").append((100 * recordsDeleted/ NUM_THREADS) / 100D)
				.append(", recordsUpdated=").append((100 * recordsUpdated/ NUM_THREADS) / 100D)
				.append(", getOperations=").append((100 * getOperations/ NUM_THREADS) / 100D)
				.append(", bulkGetOperations=").append((100 * bulkGetOperations/ NUM_THREADS) / 100D)
				.append(", operationsPerSecond=").append(((int)(100 * operationsPerSecond/ NUM_THREADS)) / 100D)
				.append("]");
		System.out.println(builder.toString());
		
		//Shutdown thread executor service
		exec.shutdownNow();
	}

	private static void fillInitialRecords(VehicleController controller,
			int initialDbSize) {
		for(int i = 0; i < initialDbSize; i++){
			try{
				controller.add(makeRandomVehicle());
			} catch (EntityExistsException ex){
				System.err.println(ex.getMessage());
			}
		}
	}
	
	private static void updateCounters(TaskStatistics stat) {
		recordsAdded += stat.getRecordsAdded();
		recordsDeleted += stat.getRecordsDeleted();
		recordsUpdated += stat.getRecordsUpdated();
		getOperations += stat.getGetOperations();
		bulkGetOperations += stat.getBulkGetOperations();
		operationsPerSecond += stat.getOperationsPerSecond();
	}
}

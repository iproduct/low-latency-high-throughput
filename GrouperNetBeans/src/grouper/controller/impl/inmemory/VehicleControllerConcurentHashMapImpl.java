package grouper.controller.impl.inmemory;

import grouper.GrouperMain;
import grouper.controller.VehicleController;
import grouper.model.Vehicle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class VehicleControllerConcurentHashMapImpl implements VehicleController {
	private final Map<Long, Vehicle> vehicles;
	private static AtomicLong count = new AtomicLong(0);
	final static Logger LOG = (Logger) LoggerFactory
			.getLogger(VehicleControllerConcurentHashMapImpl.class);

	
	protected long getNextId() {
		return count.incrementAndGet();
	}
	
	public VehicleControllerConcurentHashMapImpl(int size) {
		vehicles = new ConcurrentHashMap<>(size, 0.75f, 
				GrouperMain.NUM_THREADS);
	}

	@Override
	public void add(Vehicle vehicle) throws EntityExistsException {
		if (getByPlateNumber(vehicle.getNumber()).isPresent())
			throw new EntityExistsException("The vehicle with plate number " + vehicle.getNumber() + " already exists in DB." );
		vehicle.setId(getNextId());
		vehicles.put(vehicle.getId(), vehicle);
		String number = vehicle.getNumber();
		if(LOG.isEnabledFor(Level.TRACE)){
			Optional<Vehicle> vehicleOrNull = getByPlateNumber(number);
			LOG.trace("Vehicle [{}] has GPS number : {}", number, 
					vehicleOrNull.isPresent() ?  vehicleOrNull.get().getGpsNumber(): "none");
		}
//		LOG.trace("Vehicle [{}] has GPS number : {}", number, 
//			getByPlateNumber(number).isPresent() ? getByPlateNumber(number).get().getGpsNumber(): "none");

	}

	@Override
	public void edit(Vehicle vehicle) throws EntityNotFoundException {
		if (getById(vehicle.getId()) == null)
			throw new EntityNotFoundException("The vehicle with ID " + vehicle.getId() + " does not exist in DB." );
		vehicles.put(vehicle.getId(), vehicle);
	}

	@Override
	public void remove(long vehicleId) throws EntityNotFoundException {
		if (getById(vehicleId) == null)
			throw new EntityNotFoundException("The vehicle with ID " + vehicleId + " does not exist in DB." );
		vehicles.remove(vehicleId);
	}

	@Override
	public Optional<Vehicle> getById(long vehicleId) {
		return Optional.ofNullable(vehicles.get(vehicleId));
	}

	@Override
	public Optional<Vehicle> getByPlateNumber(String plateNumber) {
//		LOG.info("In getByPlateNumber()");
			return vehicles.values().stream()
					.filter(vehicle -> vehicle.getNumber().equalsIgnoreCase(plateNumber))
					.findAny();
		
	}

	@Override
	public List<Vehicle> get(long fromCount, int maxCount) {
		Collection<Vehicle> vals = vehicles.values();
		List<Vehicle> results = new ArrayList<Vehicle>();
//		long count = 0;
		

			results = vehicles.values().parallelStream().sorted().skip(fromCount).limit(maxCount)
					.collect(Collectors.toList());
		
//	      Iterator i = s.iterator(); // Must be in synchronized block
//	      while (i.hasNext())
//	          foo(i.next());
//	  }
//		
//		
//		for(Vehicle val: vals){
//			if(count >= fromCount)
//				results.add(val);
//			if(count >=  fromCount + maxCount) 
//				break;
//			count++;
//		}
		return results;
//		return vehicles.values().parallelStream().sorted().skip(fromCount).limit(maxCount).collect(Collectors.toList());
	}

	@Override
	public long size() {
		return vehicles.size();
	}

}

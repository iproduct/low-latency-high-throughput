package grouper.controller;

import grouper.model.Vehicle;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

public interface VehicleController {
	void add(Vehicle vehicle) throws EntityExistsException;
	void edit(Vehicle vehicle) throws EntityNotFoundException;
	void remove(long vehicleId) throws EntityNotFoundException;
	Optional<Vehicle> getById(long vehicleId);
	Optional<Vehicle> getByPlateNumber(String plateNumber);
	List<Vehicle> get(long from, int maxCount);
	long size();
}

package grouper.controller.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import grouper.controller.VehicleController;
import grouper.model.Vehicle;

public class VehicleControllerImpl implements VehicleController {

	@Override
	public void add(Vehicle vehicle) throws EntityExistsException {
		// TODO Auto-generated method stub

	}

	@Override
	public void edit(Vehicle vehicle) throws EntityNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(long vehicleId) throws EntityNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public Optional<Vehicle> getById(long vehicleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Vehicle> getByPlateNumber(String plateNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Vehicle> get(long from, int maxCount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long size() {
		// TODO Auto-generated method stub
		return 0;
	}

}

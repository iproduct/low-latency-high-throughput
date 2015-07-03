package grouper.model;

import grouper.utils.VehicleType;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the vehicle database table.
 * 
 */
@Entity
@Table(name="vehicle")
@NamedQuery(name="Vehicle.findAll", query="SELECT v FROM Vehicle v")
public class Vehicle implements Serializable, Comparable<Vehicle> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private long id;

	@Column(name="gps_number", nullable=false)
	private long gpsNumber;

	@Column(nullable=false, length=12)
	private String number;

	@Column(nullable=false)
	private int places;

	@Column(nullable=false)
	private double price;

	@Column(nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private VehicleType type;

	//bi-directional many-to-one association to Driver
	@OneToMany(mappedBy="vehicle")
	private List<Driver> drivers;

	public Vehicle() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getGpsNumber() {
		return this.gpsNumber;
	}

	public void setGpsNumber(long gpsNumber) {
		this.gpsNumber = gpsNumber;
	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getPlaces() {
		return this.places;
	}

	public void setPlaces(int places) {
		this.places = places;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public VehicleType getType() {
		return this.type;
	}

	public void setType(VehicleType type) {
		this.type = type;
	}

	public List<Driver> getDrivers() {
		return this.drivers;
	}

	public void setDrivers(List<Driver> drivers) {
		this.drivers = drivers;
	}

	public Driver addDriver(Driver driver) {
		getDrivers().add(driver);
		driver.setVehicle(this);

		return driver;
	}

	public Driver removeDriver(Driver driver) {
		getDrivers().remove(driver);
		driver.setVehicle(null);

		return driver;
	}

	@Override
	public String toString() {
		return "Vehicle [id=" + id + ", gpsNumber=" + gpsNumber + ", number="
				+ number + ", places=" + places + ", price=" + price
				+ ", type=" + type + ", drivers=" + drivers + "]";
	}

	@Override
	public int compareTo(Vehicle o) {
		return Long.signum(getId() - o.getId());
	}
}
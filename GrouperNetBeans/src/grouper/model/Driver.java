package grouper.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the driver database table.
 * 
 */
@Entity
@Table(name="driver")
@NamedQuery(name="Driver.findAll", query="SELECT d FROM Driver d")
public class Driver implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private long id;

	@Column(nullable=false, length=45)
	private String address;

	@Column(nullable=false, length=30)
	private String city;

	@Column(nullable=false, length=45)
	private String name;

	@Column(nullable=false, length=20)
	private String phone;

	@Column(name="vechicle_id", nullable=false)
	private long vechicleId;

	//bi-directional many-to-one association to Vehicle
	@ManyToOne
	@JoinColumn(name="id_number", nullable=false)
	private Vehicle vehicle;

	public Driver() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getVechicleId() {
		return this.vechicleId;
	}

	public void setVechicleId(long vechicleId) {
		this.vechicleId = vechicleId;
	}

	public Vehicle getVehicle() {
		return this.vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

}
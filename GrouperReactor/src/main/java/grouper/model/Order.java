package grouper.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the order database table.
 * 
 */
@Entity
@Table(name="order")
@NamedQuery(name="Order.findAll", query="SELECT o FROM Order o")
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private long id;

	@Column(name="from_address", nullable=false, length=45)
	private String fromAddress;

	@Column(name="from_lat", nullable=false)
	private double fromLat;

	@Column(name="from_lon", nullable=false)
	private double fromLon;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="pickup_time", nullable=false)
	private Date pickupTime;

	@Column(name="to_address", nullable=false, length=45)
	private String toAddress;

	@Column(name="to_lat", nullable=false)
	private double toLat;

	@Column(name="to_lon", nullable=false)
	private double toLon;

	@Column(name="vechicle_type", nullable=false)
	private int vechicleType;

	//bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name="customer_id", nullable=false)
	private Customer customer;

	//bi-directional many-to-one association to Payment
	@OneToMany(mappedBy="order", cascade={CascadeType.ALL})
	private List<Payment> payments;

	public Order() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFromAddress() {
		return this.fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public double getFromLat() {
		return this.fromLat;
	}

	public void setFromLat(double fromLat) {
		this.fromLat = fromLat;
	}

	public double getFromLon() {
		return this.fromLon;
	}

	public void setFromLon(double fromLon) {
		this.fromLon = fromLon;
	}

	public Date getPickupTime() {
		return this.pickupTime;
	}

	public void setPickupTime(Date pickupTime) {
		this.pickupTime = pickupTime;
	}

	public String getToAddress() {
		return this.toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public double getToLat() {
		return this.toLat;
	}

	public void setToLat(double toLat) {
		this.toLat = toLat;
	}

	public double getToLon() {
		return this.toLon;
	}

	public void setToLon(double toLon) {
		this.toLon = toLon;
	}

	public int getVechicleType() {
		return this.vechicleType;
	}

	public void setVechicleType(int vechicleType) {
		this.vechicleType = vechicleType;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Payment> getPayments() {
		return this.payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	public Payment addPayment(Payment payment) {
		getPayments().add(payment);
		payment.setOrder(this);

		return payment;
	}

	public Payment removePayment(Payment payment) {
		getPayments().remove(payment);
		payment.setOrder(null);

		return payment;
	}

}
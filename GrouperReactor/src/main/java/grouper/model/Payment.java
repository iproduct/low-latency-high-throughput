package grouper.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the payment database table.
 * 
 */
@Entity
@Table(name="payment")
@NamedQuery(name="Payment.findAll", query="SELECT p FROM Payment p")
public class Payment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private long id;

	@Column(nullable=false)
	private double amount;

	@Column(name="card_number", length=20)
	private String cardNumber;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="card_valid_date")
	private Date cardValidDate;

	@Column(nullable=false)
	private int status;

	//bi-directional many-to-one association to Order
	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="order_id", nullable=false)
	private Order order;

	public Payment() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getAmount() {
		return this.amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCardNumber() {
		return this.cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Date getCardValidDate() {
		return this.cardValidDate;
	}

	public void setCardValidDate(Date cardValidDate) {
		this.cardValidDate = cardValidDate;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Order getOrder() {
		return this.order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}
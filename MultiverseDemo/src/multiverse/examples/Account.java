package multiverse.examples;

import static org.multiverse.api.StmUtils.atomic;
import static org.multiverse.api.StmUtils.newTxnLong;
import static org.multiverse.api.StmUtils.newTxnRef;

import java.util.Date;

import org.multiverse.api.Txn;
import org.multiverse.api.callables.TxnVoidCallable;
import org.multiverse.api.references.TxnLong;
import org.multiverse.api.references.TxnRef;

public class Account {
	private final TxnRef<Date> lastModified = newTxnRef();
	private final TxnLong amount = newTxnLong();

	public Account(long amount) {
		this.amount.atomicSet(amount);
		this.lastModified.atomicSet(new Date());
	}

	public Date getLastModifiedDate() {
		return lastModified.get();
	}

	public long getAmount() {
		return amount.get();
	}

	public static void transfer(final Account from, final Account to, final long amount){
        atomic(new TxnVoidCallable() {
            @Override
            public void call(Txn tx) throws Exception {
            	Date date = new Date();

                from.lastModified.atomicSet(date);
                from.amount.decrement(amount);

                to.lastModified.atomicSet(date);
                to.amount.increment(amount);
            }
        });
    }

	@Override
	public String toString() {
		return "Account [amount=" + amount.atomicToString() +", lastModified=" 
				+ lastModified.atomicToString()	+ "]";
	}

	public static void main(String[] args){
		Account account1 = new Account(10);
		Account account2 = new Account(20);
		System.out.println("Before transaction:" + account1 + ", " + account2);
		Account.transfer(account1, account2, 5);
		System.out.println("After transaction:" + account1 + ", " + account2);
	
	}
}
package simpledb;

import java.util.*;

/**
 * An {@code Aggregate} operator computes an aggregate value (e.g., sum, avg, max, min) over a single column, grouped by
 * a single column.
 */
public class Aggregate extends AbstractDbIterator {

	/**
	 * The child operator.
	 */
	DbIterator child;

	/**
	 * The {@code TupleDesc} associated with this {@code Aggregate}.
	 */
	TupleDesc td;

	/**
	 * The {@code Aggregator} for this {@code Aggregate} operator.
	 */
	Aggregator aggregator;

	/**
	 * The current DbIterator over aggregate results.
	 */
	DbIterator it = null;
    int afield;
    int gfield;
    Aggregator.Op aop;

	/**
	 * Constructs an {@code Aggregate}.
	 *
	 * Implementation hint: depending on the type of afield, you will want to construct an {@code IntAggregator} or
	 * {@code StringAggregator} to help you with your implementation of {@code readNext()}.
	 * 
	 *
	 * @param child
	 *            the {@code DbIterator} that provides {@code Tuple}s.
	 * @param afield
	 *            the column over which we are computing an aggregate.
	 * @param gfield
	 *            the column over which we are grouping the result, or -1 if there is no grouping
	 * @param aop
	 *            the {@code Aggregator} operator to use
	 */
	public Aggregate(DbIterator child, int afield, int gfield, Aggregator.Op aop) {
		// some code goes here
		this.child = child;
        this.afield = afield;
        this.gfield = gfield;
        this.aop = aop;
        this.it=null;
        
        Type gtype;
        
        if(!(this.gfield == Aggregator.NO_GROUPING))
        {
            gtype = this.child.getTupleDesc().getType(gfield);
        }
        
        else
        {
            gtype = null;
        }
        
        if(!(this.child.getTupleDesc().getType(afield) == Type.INT_TYPE))
        {
            
            this.aggregator = new StringAggregator(gfield,gtype,afield,aop);
            
        }
        
        else
        {
            
            this.aggregator = new IntAggregator(gfield,gtype,afield,aop);
        }
    	
		//throw new UnsupportedOperationException("Implement this");
	}
	public static String aggName(Aggregator.Op aop) {
		switch (aop) {
		case MIN:
			return "min";
		case MAX:
			return "max";
		case AVG:
			return "avg";
		case SUM:
			return "sum";
		case COUNT:
			return "count";
		}
		return "";
	}

	/**
	 * Returns the {@code TupleDesc} of this {@code Aggregate}. If there is no group by field, this will have one field
	 * - the aggregate column. If there is a group by field, the first field will be the group by field, and the second
	 * will be the aggregate value column.
	 * 
	 * The name of an aggregate column should be informative. For example:
	 * {@code aggName(aop) (child_td.getFieldName(afield))} where {@code aop} and {@code afield} are given in the
	 * constructor, and {@code child_td} is the {@code TupleDesc} of the child iterator.
	 */
	public TupleDesc getTupleDesc() {
		return td;
	}

	public void open() throws NoSuchElementException, DbException, TransactionAbortedException {
		child.open();
		aggregator.clear();
		while (child.hasNext())
			aggregator.merge(child.next());
		it = aggregator.iterator();
		it.open();
	}

	public void close() {
		it = null;
	}

	public void rewind() throws DbException, TransactionAbortedException {
		// some code goes here
		throw new UnsupportedOperationException("Implement this");
	}

	/**
	 * Returns the next {@code Tuple}. If there is a group by field, then the first field is the field by which we are
	 * grouping, and the second field is the result of computing the aggregate, If there is no group by field, then the
	 * result tuple should contain one field representing the result of the aggregate. Should return {@code null} if
	 * there are no more {@code Tuple}s.
	 */
	protected Tuple readNext() throws TransactionAbortedException, DbException {
		// some code goes here
		throw new UnsupportedOperationException("Implement this");
	}

}

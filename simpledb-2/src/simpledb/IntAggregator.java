package simpledb;

import java.util.ArrayList;

/**
 * An {@code IntAggregator} computes some aggregate value over a set of {@code IntField}s.
 */
public class IntAggregator implements Aggregator {

	/**
	 * A {@code IntAggregatorImpl} instance.
	 */
	IntAggregatorImpl impl;

	/**
	 * Constructs an {@code IntAggregator}.
	 * 
	 * @param gbfield
	 *            the 0-based index of the group-by field in the tuple, or {@code NO_GROUPING} if there is no grouping
	 * @param gbfieldtype
	 *            the type of the group by field (e.g., {@code Type.INT_TYPE}), or {@code null} if there is no grouping
	 * @param afield
	 *            the 0-based index of the aggregate field in the tuple
	 * @param what
	 *            the aggregation operator
	 */
	
	private int gbfield;
	private Type gbfieldtype;
	private int afield;
	private Op what;
	private TupleDesc td;
	private ArrayList<Tuple>Tup;
	private ArrayList<Integer>Count;
	private ArrayList<Integer>Total;
	
	
	
	public IntAggregator(int gbfield, Type gbfieldtype, int afield, Op what) {
		// some code goes here
		this.gbfield = gbfield;
		this.gbfieldtype = gbfieldtype;
		this.afield = afield;
		this.what = what;
		IntAggregatorImpl impl;
		
		while(!(gbfield == Aggregator.NO_GROUPING))
		{
			Type [] int_arr = {Type.INT_TYPE};
			String[] str_arr = {" "};
			td = new TupleDesc (int_arr,str_arr);
			
		}
		
		Type[] int_arr = {gbfieldtype,Type.INT_TYPE};
		String [] str_arr = {" "};
		td = new TupleDesc(int_arr,str_arr);
		
		//throw new UnsupportedOperationException("Implement this");
	}

	/**
	 * Merges a new {@code Tuple} into the aggregate, grouping as indicated in the constructor.
	 * 
	 * @param tup
	 *            the {@code Tuple} containing an aggregate field and a group-by field
	 */
	public void merge(Tuple tup) {
		// some code goes here
		
		while(gbfield == Aggregator.NO_GROUPING)
		{
			int tValue = ((IntField)tup.getField(afield)).getValue();
			if(Tup.size()!=0)
			{
				Tuple t = Tup.get(0);
				int tupleValue = ((IntField)t.getField(0)).getValue();
				
				switch(what)
				{
					case MAX:
						t.setField(0, new IntField(Math.max(tupleValue,tValue )));
					case MIN:
						t.setField(0, new IntField(Math.max(tupleValue,tValue )));
					case SUM:
						t.setField(0,new IntField(tupleValue+tValue));
					case COUNT:
						t.setField(0,new IntField(tupleValue+1));
					case AVG:
						
						Count.set(0,Count.get(0)+1);
						Total.set(0,Total.get(0)+tValue);
						int a = Total.get(0)/Count.get(0);
						t.setField(0,new IntField(a));
							
				}
			}
		}
		
		
		throw new UnsupportedOperationException("Implement this");
	}

	/**
	 * Creates a {@code DbIterator} over group aggregate results.
	 *
	 * @return a {@code DbIterator} whose tuples are the pair ({@code groupVal}, {@code aggregateVal}) if using group,
	 *         or a single ({@code aggregateVal}) if no grouping. The {@code aggregateVal} is determined by the type of
	 *         aggregate specified in the constructor.
	 */
	public DbIterator iterator() {
		// some code goes here
		
		Iterable<Tuple> iter = Tup;
		return (DbIterator)TupleDesc(td,iter);
		
		//throw new UnsupportedOperationException("Implement this");
	}

	private DbIterator TupleDesc(TupleDesc td2, Iterable<Tuple> iter) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * An {@code IntAggregatorImpl} computes some aggregate value over a set of {@code Field}s.
	 */
	abstract class IntAggregatorImpl {
		{
		switch (what) 
		{
		case COUNT:
			new IntAggregatorImpl() {
			};
			break;
			
		 default:
			throw new IllegalArgumentException(what + " is not supported");
		}
		
	}
	
	// some code goes here

	/**
	 * Clears this {@code IntAggregator}.
	 */
	public void clear() {
	}
}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
}

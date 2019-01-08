package simpledb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@code StringAggregator} computes some aggregate value over a set of {@code StringField}s.
 */
public class StringAggregator implements Aggregator {

	/**
	 * A {@code StringAggregatorImpl} instance.
	 */
	StringAggregatorImpl impl;
	private int gbfield;
	private Type gbfieldtype;
	private int afield;
	private Op what;
	private ArrayList<Tuple> Tup;
	private TupleDesc td;

	/**
	 * Constructs a {@code StringAggregator}.
	 * 
	 * @param gbfield
	 *            the 0-based index of the group-by field in the tuple, or {@code NO_GROUPING} if there is no grouping
	 * @param gbfieldtype
	 *            the type of the group by field (e.g., {@code Type.INT_TYPE}), or {@code null} if there is no grouping
	 * @param afield
	 *            the 0-based index of the aggregate field in the tuple
	 * @param what
	 *            aggregation operator to use -- only supports {@code COUNT}
	 * @throws IllegalArgumentException
	 *             if {@code what != COUNT}
	 */
	public StringAggregator(int gbfield, Type gbfieldtype, int afield, Op what) {
		if (gbfield == NO_GROUPING)
			impl = new StringAggregatorImplWithoutGrouping(afield, what);
		else
			impl = new StringAggregatorImplWithGrouping(gbfield, gbfieldtype, afield, what);
	}

	/**
	 * Merges a new tuple into the aggregate, grouping as indicated in the constructor.
	 * 
	 * @param tup
	 *            the {@code Tuple} containing an aggregate field and a group-by field
	 */
	public void merge(Tuple tup) {
		impl.merge(tup);
	}

	/**
	 * Creates a {@code DbIterator} over group aggregate results.
	 *
	 * @return a {@code DbIterator} whose tuples are the pair ({@code groupVal}, {@code aggregateVal}) if using group,
	 *         or a single ({@code aggregateVal}) if no grouping. The aggregateVal is determined by the type of
	 *         aggregate specified in the constructor.
	 */
	public DbIterator iterator() {
		return impl.iterator();
	}

	/**
	 * A {@code StringAggregatorImpl} computes some aggregate value over a set of {@code StringField}s.
	 */
	abstract class StringAggregatorImpl {

		/**
		 * The 0-based index of the aggregate field
		 */
		int afield;

		/**
		 * An {@code AggregateFunctionFactory} for creating an {@code AggregateFunction} whenever needed.
		 */
		AggregateFunctionFactory aggFtnFactory;

		/**
		 * Constructs a {@code StringAggregatorImpl}.
		 * 
		 * @param afield
		 *            the 0-based index of the aggregate field in the tuple
		 * @param what
		 *            aggregation operator to use -- only supports {@code COUNT}
		 * @throws IllegalArgumentException
		 *             if {@code what != COUNT}
		 */
		StringAggregatorImpl(int afield, Op what) {
			switch (what) {
			case COUNT:
				this.aggFtnFactory = new AggregateFunctionFactory() {

					@Override
					public AggregateFunction createAggregateFunction() {
						return new CountAggregateFunction();
					}
				};
				break;
			default:
				throw new IllegalArgumentException(what + " is not supported");
			}
			this.afield = afield;
		}

		/**
		 * Merges a new tuple into the aggregate, grouping as indicated in the constructor.
		 * 
		 * @param tup
		 *            the {@code Tuple} containing an aggregate field and a group-by field
		 */
		public abstract void merge(Tuple tup);

		/**
		 * Creates a {@code DbIterator} over group aggregate results.
		 *
		 * @return a {@code DbIterator} whose tuples are the pair ({@code groupVal}, {@code aggregateVal}) if using
		 *         group, or a single ({@code aggregateVal}) if no grouping. The aggregateVal is determined by the type
		 *         of aggregate specified in the constructor.
		 */
		public abstract DbIterator iterator();

		/**
		 * Clears this {@code StringAggregatorImpl}.
		 */
		public abstract void clear();
	}

	/**
	 * A {@code StringAggregatorImplWithoutGrouping} computes some aggregate value over a set of {@code StringField}s
	 * without grouping.
	 */
	class StringAggregatorImplWithoutGrouping extends StringAggregatorImpl {

		/**
		 * The {@code TupleDesc} for the output {@code Tuple}s.
		 */
		TupleDesc td;

		/**
		 * The {@code AggregateFunction} to use.
		 */
		AggregateFunction aggFtn;

		/**
		 * Constructs a {@code StringAggregatorImplWithoutGrouping}.
		 * 
		 * @param afield
		 *            the 0-based index of the aggregate field in the tuple
		 * @param what
		 *            aggregation operator to use -- only supports {@code COUNT}
		 * @throws IllegalArgumentException
		 *             if {@code what != COUNT}
		 */
		public StringAggregatorImplWithoutGrouping(int afield, Op what) {
			super(afield, what);
			Type[] type = new Type[] { Type.INT_TYPE };
			td = new TupleDesc(type);
			aggFtn = this.aggFtnFactory.createAggregateFunction();
		}

		/**
		 * Merges a new tuple into the aggregate without grouping.
		 * 
		 * @param tup
		 *            the {@code Tuple} containing an aggregate field
		 */
		@Override
		public void merge(Tuple tup) {
			aggFtn.merge(tup.getField(afield));
		}

		/**
		 * Creates a {@code DbIterator} over the aggregate result.
		 *
		 * @return a {@code DbIterator} over a single tuple having a single aggregate value. The aggregate value is
		 *         determined by the type of aggregate specified in the constructor.
		 */
		@Override
		public DbIterator iterator() {
			// some code goes here
			Iterable <Tuple> t = Tup;
			return new TupleIterator(td,t);
			
			//throw new UnsupportedOperationException("Implement this");
			
		}

		/**
		 * Clears this {@code StringAggregatorImplWithoutGrouping}.
		 */
		@Override
		public void clear() {
			aggFtn = aggFtnFactory.createAggregateFunction();
		}

	}

	/**
	 * A {@code StringAggregatorImplWithoutGrouping} computes some aggregate value over a set of {@code StringField}s
	 * with grouping.
	 */
	class StringAggregatorImplWithGrouping extends StringAggregatorImpl {

		/**
		 * The {@code TupleDesc} for the output {@code Tuple}s.
		 */
		TupleDesc td;

		/**
		 * The 0-based index of the group-by field in the tuple.
		 */
		int gbfield;

		/**
		 * A map that associates each group value with an {@code AggregateFunction}.
		 */
		Map<Field, AggregateFunction> field2aggFtn = new HashMap<Field, AggregateFunction>();

		private int Tup;

		/**
		 * Constructs a {@code StringAggregatorImplWithGrouping}.
		 * 
		 * @param gbfield
		 *            the 0-based index of the group-by field in the tuple, or {@code NO_GROUPING} if there is no
		 *            grouping
		 * @param gbfieldtype
		 *            the type of the group by field (e.g., {@code Type.INT_TYPE}), or {@code null} if there is no
		 *            grouping
		 * @param afield
		 *            the 0-based index of the aggregate field in the tuple
		 * @param what
		 *            aggregation operator to use -- only supports {@code COUNT}
		 * @throws IllegalArgumentException
		 *             if {@code what != COUNT}
		 */
		public StringAggregatorImplWithGrouping(int gbfield, Type gbfieldtype, int afield, Op what) {
			super(afield, what);
			// some code goes here
			throw new UnsupportedOperationException("Implement this");
		}

		/**
		 * Merges a new tuple into the aggregate, grouping as indicated in the constructor.
		 * 
		 * @param tup
		 *            the {@code Tuple} containing an aggregate field and a group-by field
		 */
		@Override
		public void merge(Tuple tup) {
			// some code goes here
			if(!(gbfield == Aggregator.NO_GROUPING))
			{
				int t = (((IntField) tup.getField(0)).getValue());
				if(what==Op.COUNT)
				{
					t = 1;
					
				}
				Tuple tuple = new Tuple(td);
				//t.setField(0,new IntField(Tup));
				
			}
			
			//throw new UnsupportedOperationException("Implement this");
		}

		/**
		 * Creates a {@code DbIterator} over group aggregate results.
		 *
		 * @return a {@code DbIterator} whose tuples are the pair ({@code groupVal}, {@code aggregateVal}). The
		 *         aggregateVal is determined by the type of aggregate specified in the constructor.
		 */
		@Override
		public DbIterator iterator() {
			// some code goes here
			throw new UnsupportedOperationException("Implement this");
		}

		/**
		 * Clears this {@code StringAggregatorImplWithGrouping}.
		 */
		@Override
		public void clear() {
			field2aggFtn.clear();
		}

	}

	/**
	 * Clears this {@code StringAggregator}.
	 */
	@Override
	public void clear() {
		impl.clear();
	}

}

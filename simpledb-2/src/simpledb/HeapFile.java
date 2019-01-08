package simpledb;

import java.io.*;
import java.util.*;

/**
 * HeapFile is an implementation of a DbFile that stores a collection of tuples in no particular order. Tuples are
 * stored on pages, each of which is a fixed size, and the file is simply a collection of those pages. HeapFile works
 * closely with HeapPage. The format of HeapPages is described in the HeapPage constructor.
 * 
 * @see simpledb.HeapPage#HeapPage
 */
public class HeapFile implements DbFile {

	/**
	 * The File associated with this HeapFile.
	 */
	protected File file;

	/**
	 * The TupleDesc associated with this HeapFile.
	 */
	protected TupleDesc td;

	/**
	 * Constructs a heap file backed by the specified file.
	 * 
	 * @param f
	 *            the file that stores the on-disk backing store for this heap file.
	 */
	public HeapFile(File f, TupleDesc td) {
		this.file = f;
		this.td = td;
	}

	/**
	 * Returns the File backing this HeapFile on disk.
	 * 
	 * @return the File backing this HeapFile on disk.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Returns an ID uniquely identifying this HeapFile. Implementation note: you will need to generate this tableid
	 * somewhere ensure that each HeapFile has a "unique id," and that you always return the same value for a particular
	 * HeapFile. We suggest hashing the absolute file name of the file underlying the heapfile, i.e.
	 * f.getAbsoluteFile().hashCode().
	 * 
	 * @return an ID uniquely identifying this HeapFile.
	 */
	public int getId() {
		return file.getAbsoluteFile().hashCode();
	}

	/**
	 * Returns the TupleDesc of the table stored in this DbFile.
	 * 
	 * @return TupleDesc of this DbFile.
	 */
	public TupleDesc getTupleDesc() {
		return td;
	}

	// see DbFile.java for javadocs
	public Page readPage(PageId pid) {
		// some code goes here
		/**
		 * Class RandomAccessFile behaves as large array of bytes,
		 * it also helps in both reading and writing to random access file. 
		 */
		try{
			RandomAccessFile ran = new RandomAccessFile(file, "r");
			int offset = pid.pageno() * BufferPool.PAGE_SIZE;
			byte[] page = new byte[BufferPool.PAGE_SIZE];
			ran.seek(offset);
			ran.read(page, 0, BufferPool.PAGE_SIZE);
			ran.close();
			
			HeapPageId id = (HeapPageId) pid;
			return new HeapPage(id, page);
		   } 
		
		catch (IOException e){
			e.printStackTrace();
		}
		throw new IllegalArgumentException();
	}
		//throw new UnsupportedOperationException("Implement this");

	// see DbFile.java for javadocs
	public void writePage(Page page) throws IOException {
		// some code goes here
		// not necessary for assignment1
	}

	/**
	 * Returns the number of pages in this HeapFile.
	 */
	public int numPages() {
		return (int) (file.length() / BufferPool.PAGE_SIZE);
	}

	// see DbFile.java for javadocs
	public ArrayList<Page> addTuple(TransactionId tid, Tuple t)
			throws DbException, IOException, TransactionAbortedException {
		// some code goes here
		// not necessary for assignment1
		return null;
	}

	// see DbFile.java for javadocs
	public Page deleteTuple(TransactionId tid, Tuple t) throws DbException, TransactionAbortedException {
		// some code goes here
		
		HeapPage hp;
		hp = (HeapPage)Database.getBufferPool().getPage(tid, t.getRecordId().getPageId(), Permissions.READ_WRITE);
		hp.deleteTuple(t);
		return hp;
		
		// not necessary for assignment1
		
	}

	// see DbFile.java for javadocs
	public DbFileIterator iterator(TransactionId tid) {

		return new DbFileIterator() {

			/**
			 * The ID of the page to read next.
			 */
			int nextPageID = 0;

			/**
			 * An iterator over all tuples from the page read most recently.
			 */
			Iterator<Tuple> iterator = null;

			@Override
			public void open() throws DbException, TransactionAbortedException {
				nextPageID = 0;
				// obtains an iterator over all tuples from page 0
				iterator = ((HeapPage) Database.getBufferPool().getPage(tid, new HeapPageId(getId(), nextPageID++),
						Permissions.READ_WRITE)).iterator();
			}

			@Override
			public boolean hasNext() throws DbException, TransactionAbortedException {
				// some code goes here
				
				/* if(this.iterator != null)
				{
					if(this.iterator.hasNext())
					{
						return true;
					}
					else
					{
						if(this.nextPageID<HeapFile.this.getId())
						{
							this.nextPageID++;
							this.iterator = ((HeapPage)this.iterator).iterator();
							return this.hasNext();
						}
						
					} */
					if(this.iterator != null)
					{
						if(this.iterator.hasNext())
						{
							return true;
						}
						else
						{
							if(this.nextPageID < numPages())
							{
								HeapFile.this.getId();
								this.nextPageID++;
								iterator = ((HeapPage) Database.getBufferPool().getPage(tid, new HeapPageId(getId(), nextPageID++),
										Permissions.READ_WRITE)).iterator();
								return this.hasNext();
							}
						
						}
				}
				//throw new UnsupportedOperationException("Implement this");
				return false;
			}

			@Override
			public Tuple next() throws DbException, TransactionAbortedException, NoSuchElementException {
				// some code goes here
				if(this.iterator!=null)
				{
					return this.iterator.next();
				}
				throw new NoSuchElementException();
				//throw new UnsupportedOperationException("Implement this");
			}

			@Override
			public void rewind() throws DbException, TransactionAbortedException {
				close();
				open();
			}

			@Override
			public void close() {
				iterator = null;
			}

		};
	}

}

package simpledb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * The {@code Catalog} keeps track of all available tables in the database and their associated schemas. For now, this
 * is a stub catalog that must be populated with tables by a user program before it can be used -- eventually, this
 * should be converted to a catalog that reads a catalog table from disk.
 */

public class Catalog {

	/**
	 * A map that associates the name of each table with the ID of that table.
	 */
	HashMap<String, Integer> name2tableID = new HashMap<String, Integer>();

	/**
	 * A map that associates the ID of each table with the {@code TupleDesc} of that table.
	 */
	HashMap<Integer, TupleDesc> tableID2desc = new HashMap<Integer, TupleDesc>();

	/**
	 * A map that associates the ID of each table with the {@code DbFile} of that table.
	 */
	HashMap<Integer, DbFile> tableID2dbFile = new HashMap<Integer, DbFile>();

	/**
	 * Creates a new, empty {@code Catalog}.
	 */
	public Catalog() {
	}

	/**
	 * Add a new table to this {@code Catalog}. This table's contents are stored in the specified {@code DbFile}.
	 * 
	 * @param file
	 *            the contents of the table to add
	 * @param name
	 *            the name of the table -- may be an empty string. May not be {@code null}. If a name conflict exists,
	 *            use the last table to be added as the table for a given name.
	 * @param pkeyField
	 *            the name of the primary key field
	 */
	public void addTable(DbFile file, String name, String pkeyField) {
		int tableID = file.getId();
		name2tableID.put(name, tableID);
		tableID2desc.put(tableID, file.getTupleDesc());
		tableID2dbFile.put(tableID, file);
	}

	/**
	 * Add a new table to this {@code Catalog}. This table's contents are stored in the specified {@code DbFile}.
	 * 
	 * @param file
	 *            the contents of the table to add
	 * @param name
	 *            the name of the table -- may be an empty string. May not be {@code null}. If a name conflict exists,
	 *            use the last table to be added as the table for a given name.
	 */
	public void addTable(DbFile file, String name) {
		addTable(file, name, "");
	}

	/**
	 * Return the ID of the table with a specified name.
	 * 
	 * @throws NoSuchElementException
	 *             if the table doesn't exist
	 */
	public int getTableId(String name) throws NoSuchElementException{
		// some code goes here
		/**
		 * This will fetch the ID of table, if it is not null,
		 * else it will throw exception that no such element exists. 
		 */
		Integer value = name2tableID.get(name);
		if (value==null)
		{
			throw new NoSuchElementException();
		}
		else
		{
			return value;
		}
        //throw new UnsupportedOperationException("Implement this");
	}

	/**
	 * Returns the tuple descriptor (schema) of the specified table
	 * 
	 * @param tableid
	 *            the ID of the table, as specified by {@code DbFile.getId()} passed to the {@code addTable(...)} method
	 * @throws NoSuchElementException
	 *             if the table doesn't exist
	 */
	public TupleDesc getTupleDesc(int tableid) throws NoSuchElementException {
		// some code goes here
		/**
		 * This returns the tuple descriptor if value is found,
		 * else if null value is found it throws NoSuchElementException.
		 */
		 TupleDesc value = tableID2desc.get(tableid);
		 if (value==null)
		 {
			 throw new NoSuchElementException();
		 }
		 else
		 {
			 return value;
		 }
		
        //throw new UnsupportedOperationException("Implement this");
	}

	/**
	 * Returns the DbFile that can be used to read the contents of the specified table.
	 * 
	 * @param tableid
	 *            The id of the table, as specified by {@code DbFile.getId()} passed to the {@code addTable(...)} method
	 * @throws NoSuchElementException
	 *             if the table doesn't exist
	 */
	public DbFile getDbFile(int tableid) throws NoSuchElementException {
		// some code goes here
		/**
		 * Here it returns DBFile on successful execution,
		 * and if it finds null value then it throws exception.
		 */
		DbFile value = tableID2dbFile.get(tableid);
		if(value==null)
		{
			throw new NoSuchElementException();
		}
		else
		{
			
			return value;
		}
		
       // throw new UnsupportedOperationException("Implement this");
	}

	/**
	 * Deletes all tables from this {@code Catalog}.
	 */
	public void clear() {
		name2tableID.clear();
		tableID2desc.clear();
		tableID2dbFile.clear();
	}

	/**
	 * Returns the primary key for the specified table.
	 * 
	 * @param tableid
	 *            the ID of a table
	 * @return the primary key for the specified table
	 */
	public String getPrimaryKey(int tableid) {
		// some code goes here
		return null;
	}

	/**
	 * Returns the IDs of all tables registered in this {@code Catalog}.
	 * 
	 * @return the IDs of all tables registered in this {@code Catalog}
	 */
	public Iterator<Integer> tableIdIterator() {
		// some code goes here
		return null;
	}

	/**
	 * Returns the name of the specified table.
	 *
	 * @param tableid
	 *            the ID of a table
	 * @return the primary key for the specified table
	 */
	public String getTableName(int id) {
		// some code goes here
		return null;
	}
	
	/**
	 * Reads the schema from a file and creates the appropriate tables in the database.
	 * 
	 * @param catalogFile
	 *            a file storing a {@code Catalog}
	 */
	public void loadSchema(String catalogFile) {
		String line = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(catalogFile)));
			try {
				while ((line = br.readLine()) != null) {
					// assume line is of the format name (field type, field type, ...)
					String name = line.substring(0, line.indexOf("(")).trim();
					// System.out.println("TABLE NAME: " + name);
					String fields = line.substring(line.indexOf("(") + 1, line.indexOf(")")).trim();
					String[] els = fields.split(",");
					ArrayList<String> names = new ArrayList<String>();
					ArrayList<Type> types = new ArrayList<Type>();
					String primaryKey = "";
					for (String e : els) {
						String[] els2 = e.trim().split(" ");
						names.add(els2[0].trim());
						if (els2[1].trim().toLowerCase().equals("int"))
							types.add(Type.INT_TYPE);
						else if (els2[1].trim().toLowerCase().equals("string"))
							types.add(Type.STRING_TYPE);
						else {
							System.out.println("Unknown type " + els2[1]);
							System.exit(0);
						}
						if (els2.length == 3) {
							if (els2[2].trim().equals("pk"))
								primaryKey = els2[0].trim();
							else {
								System.out.println("Unknown annotation " + els2[2]);
								System.exit(0);
							}
						}
					}
					Type[] typeAr = types.toArray(new Type[0]);
					String[] namesAr = names.toArray(new String[0]);
					TupleDesc t = new TupleDesc(typeAr, namesAr);
					HeapFile tabHf = new HeapFile(new File(name + ".dat"), t);
					addTable(tabHf, name, primaryKey);
					System.out.println("Added table : " + name + " with schema " + t);
				}
			} finally {
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Invalid catalog entry : " + line);
			System.exit(0);
		}
	}
}

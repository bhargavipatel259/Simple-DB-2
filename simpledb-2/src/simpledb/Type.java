package simpledb;

import java.text.ParseException;
import java.io.*;

/**
 * Class representing a type in SimpleDB. Types are static objects defined by this class; hence, the Type constructor is
 * private.
 */
public enum Type {
	INT_TYPE() {

		@Override
		public Field parse(DataInputStream dis) throws ParseException {
			try {
				return new IntField(dis.readInt());
			} catch (IOException e) {
				throw new ParseException("couldn't parse", 0);
			}
		}

	},
	STRING_TYPE() {

		@Override
		public Field parse(DataInputStream dis) throws ParseException {
			try {
				int strLen = dis.readInt();
				byte bs[] = new byte[strLen];
				dis.read(bs);
				return new StringField(new String(bs), strLen);
			} catch (IOException e) {
				throw new ParseException("couldn't parse", 0);
			}
		}
	};

	/**
	 * @return a Field object of the same type as this object that has contents read from the specified DataInputStream.
	 * @param dis
	 *            The input stream to read from
	 * @throws ParseException
	 *             if the data read from the input stream is not of the appropriate type.
	 */
	public abstract Field parse(DataInputStream dis) throws ParseException;

}

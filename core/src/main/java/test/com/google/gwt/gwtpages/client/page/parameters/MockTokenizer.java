package test.com.google.gwt.gwtpages.client.page.parameters;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gwt.gwtpages.client.page.parameters.SimpleTokenizer;

public class MockTokenizer extends SimpleTokenizer {

	private DateFormat dateFormat;
	private DateFormat dateTimeFormat;
	private DateFormat timeOnlyFormat = new SimpleDateFormat("HmsS");

	public static void main(String[] args) throws ParseException {
		MockTokenizer t = new MockTokenizer();
		System.out.println(t.createHistoryToken("foo",
				t.dateFormat.parse("11/4/03")));
		System.out.println(t.getPossiblePageTokens("foo/bar/biz"));
	}

	public MockTokenizer() {
		super(null, null);
		this.dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
		this.dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,
				DateFormat.SHORT);
	}

	public MockTokenizer(DateFormat dateFormat,
			DateFormat dateTimeFormat) {
		this.dateFormat = dateFormat;
		this.dateTimeFormat = dateTimeFormat;
	}

	@Override
	public String encode(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}

	@Override
	public String decode(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}

	@Override
	public Date toDate(Serializable entry) {
		if (null == entry)
			return null;
		else if (entry instanceof Date)
			return (Date) entry;
		else if (entry instanceof Long)
			return new Date((Long) entry);
		else {
			try {
				return dateFormat.parse(entry.toString());
			} catch (ParseException e) {
				throw new IllegalArgumentException("Invalid date format '"
						+ entry.toString() + "'");
			}
		}
	}

	@Override
	public Date toDateTime(Serializable entry) {
		if (null == entry)
			return null;
		else if (entry instanceof Date)
			return (Date) entry;
		else if (entry instanceof Long)
			return new Date((Long) entry);
		else {
			try {
				return dateTimeFormat.parse(entry.toString());
			} catch (ParseException e) {
				throw new IllegalArgumentException("Invalid datetime format '"
						+ entry.toString() + "'");
			}
		}
	}

	public String toString(Date entry, boolean allowNull) {
		if (null == entry)
			return allowNull ? null : "";
		else {
			Date date = (Date) entry;
			Integer timeInt = new Integer(timeOnlyFormat.format(date));
			if (timeInt == 0)
				return dateFormat.format(entry);
			else
				return dateTimeFormat.format(entry);
		}
	}
}
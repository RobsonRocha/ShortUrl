package br.com.shorturl.utils;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.shorturl.connection.DBConnection;
import br.com.shorturl.pojo.ShortURL;
import br.com.shorturl.pojo.Top10;

public class Utils {

	private static String ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUWXYZ";
	private static int BASE = ALPHABET.length();

	private static String encode(long num) {
		StringBuilder sb = new StringBuilder();
		while (num > 0) {
			sb.append(ALPHABET.charAt((int) (num % BASE)));
			num /= BASE;
		}
		return sb.reverse().toString();
	}

	public static long decode(String str) {
		long num = 0;
		for (int i = 0; i < str.length(); i++)
			num = num * BASE + ALPHABET.indexOf(str.charAt(i));
		return num;
	}

	public static String getCode() throws SQLException, PropertyVetoException {

		Connection connection = DBConnection.getConnectionDB();
		PreparedStatement selectPreparedStatement = null;

		String selectQuery = "select COALESCE(max(ID),0)+1 as ID from URL";
		selectPreparedStatement = connection.prepareStatement(selectQuery);
		ResultSet rs = selectPreparedStatement.executeQuery();
		long result = 0;
		try {
			if (rs.next())
				result = rs.getInt("ID");
		} finally {
			selectPreparedStatement.close();
			connection.close();
		}
		return encode(result);
	}

	public static void insertURL(String code, String url) throws SQLException,
			PropertyVetoException {

		Connection connection = DBConnection.getConnectionDB();
		PreparedStatement insertPreparedStatement = null;
		try {
			connection.setAutoCommit(false);
			String InsertQuery = "INSERT INTO URL (URL, ORIGEM, NUMERO_ACESSO) values (?,?,?)";
			insertPreparedStatement = connection.prepareStatement(InsertQuery);
			insertPreparedStatement.setString(1, code);
			insertPreparedStatement.setString(2, url);
			insertPreparedStatement.setInt(3, 0);
			insertPreparedStatement.executeUpdate();
			connection.commit();
		} finally {
			insertPreparedStatement.close();
			connection.close();
		}

	}

	public static void insertURL(String code, String url, String alias)
			throws SQLException, PropertyVetoException {

		Connection connection = DBConnection.getConnectionDB();
		PreparedStatement insertPreparedStatement = null;
		try {
			connection.setAutoCommit(false);
			String InsertQuery = "INSERT INTO URL (URL, ORIGEM, NUMERO_ACESSO, ALIAS) values (?,?,?,?)";
			insertPreparedStatement = connection.prepareStatement(InsertQuery);
			insertPreparedStatement.setString(1, code);
			insertPreparedStatement.setString(2, url);
			insertPreparedStatement.setInt(3, 0);
			insertPreparedStatement.setString(4, alias);
			insertPreparedStatement.executeUpdate();
			connection.commit();
		} finally {
			insertPreparedStatement.close();
			connection.close();
		}

	}

	public static ShortURL getURL(String id) throws SQLException,
			PropertyVetoException {

		Connection connection = DBConnection.getConnectionDB();
		PreparedStatement selectPreparedStatement = null;
		PreparedStatement updatePreparedStatement = null;
		ResultSet rs = null;
		ShortURL result = null;		
		try {
			Long urlID = decode(id);
			connection.setAutoCommit(false);
			String selectQuery = "select URL, ORIGEM, NUMERO_ACESSO from URL where ID=?";
			selectPreparedStatement = connection.prepareStatement(selectQuery);
			selectPreparedStatement.setLong(1, urlID);
			rs = selectPreparedStatement.executeQuery();
			result = new ShortURL();
			if (rs.next()) {
				result.setAlias(rs.getString("URL"));
				result.setUrl(rs.getString("ORIGEM"));
			} else
				result = null;

			selectPreparedStatement.close();
			
			String updateQuery = "update URL set NUMERO_ACESSO = NUMERO_ACESSO + 1 where ID = ?";
			updatePreparedStatement = connection.prepareStatement(updateQuery);
			updatePreparedStatement.setLong(1, urlID);
			updatePreparedStatement.executeUpdate();

			connection.commit();
		} finally {
			rs.close();
			updatePreparedStatement.close();
			connection.close();

		}
		return result;

	}

	public static ShortURL getURLAlias(String alias) throws SQLException,
			PropertyVetoException {

		Connection connection = DBConnection.getConnectionDB();
		PreparedStatement selectPreparedStatement = null;
		PreparedStatement updatePreparedStatement = null;
		ResultSet rs = null;
		ShortURL result = null;
		try {
			connection.setAutoCommit(false);
			String selectQuery = "select ID, URL, ORIGEM, NUMERO_ACESSO from URL where ALIAS=?";
			selectPreparedStatement = connection.prepareStatement(selectQuery);
			selectPreparedStatement.setString(1, alias);
			rs = selectPreparedStatement.executeQuery();
			Long id = 0L;
			result = new ShortURL();
			if (rs.next()) {
				result.setAlias(rs.getString("URL"));
				result.setUrl(rs.getString("ORIGEM"));
				id = rs.getLong("ID");
			} else
				result = null;

			selectPreparedStatement.close();
			
			String updateQuery = "update URL set NUMERO_ACESSO = NUMERO_ACESSO + 1 where ID = ?";
			updatePreparedStatement = connection.prepareStatement(updateQuery);
			updatePreparedStatement.setLong(1, id);
			updatePreparedStatement.executeUpdate();
			connection.commit();

		} finally {
			rs.close();
			updatePreparedStatement.close();
			connection.close();
		}

		return result;

	}

	public static String randonWord() {
		String leters = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUWXYZ";
		int random;
		String word = "";
		// para gerar uma quantidade letras
		for (int i = 1; i <= 10; i++) {
			random = (int) (Math.random() * (leters.length() - 1));
			word += leters.charAt(random);
		}

		return word;
	}

	public static String code() {
		String leters = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUWXYZ";
		int random;
		String code = "";

		for (int i = 1; i <= 6; i++) {
			random = (int) (Math.random() * (leters.length() - 1));

			System.out.print(leters.charAt(random));
			code += leters.charAt(random);
		}

		return code;
	}

	public static void createTable() throws SQLException,
			PropertyVetoException {
		Connection connection = DBConnection.getConnectionDB();
		PreparedStatement createPreparedStatement = null;

		String CreateQuery = "CREATE TABLE URL(ID BIGINT AUTO_INCREMENT PRIMARY KEY, URL VARCHAR, ORIGEM VARCHAR, NUMERO_ACESSO INTEGER, ALIAS VARCHAR UNIQUE); CREATE INDEX alias_index ON URL (ALIAS); ";

		try {
			connection.setAutoCommit(false);

			createPreparedStatement = connection.prepareStatement(CreateQuery);
			createPreparedStatement.executeUpdate();

			connection.commit();
		} finally {
			createPreparedStatement.close();
			connection.close();
		}

	}

	public static void delete() throws SQLException, PropertyVetoException {
		Connection connection = DBConnection.getConnectionDB();
		PreparedStatement deletePreparedStatement = null;

		String deleteQuery = "DELETE FROM URL";

		try {
			connection.setAutoCommit(false);

			deletePreparedStatement = connection.prepareStatement(deleteQuery);
			deletePreparedStatement.executeUpdate();

			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			deletePreparedStatement.close();
			connection.close();
		}

	}

	public static List<Top10> getTop10() throws SQLException, PropertyVetoException {
		Connection connection = DBConnection.getConnectionDB();
		PreparedStatement selectPreparedStatement = null;
		ResultSet rs = null;
		List<Top10> result = new ArrayList<Top10>();
		try {
			String selectQuery = "select * from (select ID, URL, ORIGEM, max(NUMERO_ACESSO) as ACESSOS from URL group by ID, URL, ORIGEM order by max(NUMERO_ACESSO) desc) where rownum <= 10";
			selectPreparedStatement = connection.prepareStatement(selectQuery);
			rs = selectPreparedStatement.executeQuery();						
			while (rs.next()) {
				ShortURL su = new ShortURL();
				su.setAlias(rs.getString("URL"));
				su.setUrl(rs.getString("ORIGEM"));
				Top10 top10 = new Top10(rs.getInt("ACESSOS"), su);
				result.add(top10);			
			}
			
			return result;

		} finally {
			rs.close();
			selectPreparedStatement.close();
			connection.close();
		}
	}

}

import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class PaperReviewDriver {
	private static Connection _dbConnection = null;

	private static void createConnection() throws SQLException {
		_dbConnection = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/research_paper?user=sam&serverTimezone=UTC", "sam", "Password1");
	}

	private static void firstQuery(int input) throws SQLException, IOException {
		if (_dbConnection == null) {
			createConnection();
		}
		String query = "SELECT p.ID as paperid, p.Title as paperTitle, p.Abstract as paperAbstract, a.Email as authorEmail, a.FirstName as authorFirstName, a.LastName as authorLastName\n"
				+ "FROM Author AS a\n" + "JOIN Paper AS p ON p.AuthorId = a.ID\n" + "WHERE a.ID = ?;";
		PreparedStatement _ps = _dbConnection.prepareStatement(query);

		_ps.setInt(1, input);
		ResultSet rs = _ps.executeQuery();
		while (rs.next()) {
			System.out.println("Paper ID: " + rs.getInt("paperId"));
			System.out.println("Paper Title: " + rs.getString("paperTitle"));
			System.out.println("Paper Abstract: " + rs.getString("paperAbstract"));
			System.out.println("Paper Email: " + rs.getString("authorEmail"));
			System.out.println("First Name: " + rs.getString("authorFirstName"));
			System.out.println("Last Name: " + rs.getString("authorLastName"));
		}
	}

	private static void secondQuery(int input) throws SQLException {
		if (_dbConnection == null) {
			createConnection();
		}
		String _query = "SELECT r.ID AS ID, r.Recommendation AS Recommendation, r.MeritScore AS MeritScore, r. ReadabilityScore AS ReadabilityScore, "
				+ "r.ReviewerId AS ReviewerId, r.RelevanceScore AS RelevanceScore, r.OriginalityScore AS OriginalityScore  FROM REVIEW as r\n"
				+ "JOIN Paper as p on p.ID = r.PaperId\n" + "WHERE r.Recommendation = 'Yes'\n" + "AND p.ID = ?;";
		PreparedStatement _ps = _dbConnection.prepareStatement(_query);
		_ps.setInt(1, input);
		ResultSet rs = _ps.executeQuery();
		while (rs.next()) {
			System.out.println("Review ID: " + rs.getInt("ID"));
			System.out.println("Recommendation: " + rs.getString("Recommendation"));
			System.out.println("Merit Score: " + rs.getString("MeritScore"));
			System.out.println("Readability Score: " + rs.getString("ReadabilityScore"));
			System.out.println("Reviewer Id: " + rs.getString("ReviewerId"));
			System.out.println("Relevance Score: " + rs.getString("RelevanceScore"));
			System.out.println("Originality Score: " + rs.getString("OriginalityScore"));
		}
	}

	private static void thirdQuery() throws SQLException {
		if (_dbConnection == null) {
			createConnection();
		}
		String _query = "SELECT COUNT(1) as Count FROM Paper;";
		PreparedStatement _ps = _dbConnection.prepareStatement(_query);
		ResultSet rs = _ps.executeQuery();
		while (rs.next()) {
			System.out.println("Total number of papers in database: " + rs.getInt("Count"));
		}
	}

	private static void fourthQuery() throws SQLException {
		if (_dbConnection == null) {
			createConnection();
		}
		String _query = "INSERT INTO Author (ID, FirstName, LastName, Email)\n"
				+ "VALUES (5, 'Pratik','Gaikwad','p.g@gmail.com');";
		PreparedStatement _ps = _dbConnection.prepareStatement(_query);
		_ps.executeUpdate();
		_query = "INSERT INTO Paper (ID, Title, Abstract, FileName, TopicId, AuthorId)\n"
				+ "VALUES(5,'Why security is needed',' This paper provides necesity of using cybersecurity in software development life cycle', 'Cysec' , 2, 5);";
		_ps = _dbConnection.prepareStatement(_query);
		_ps.executeUpdate();
		System.out.println("Created record for Author and Paper");
	}

	private static void fifthQuery() throws SQLException {
		if (_dbConnection == null) {
			createConnection();
		}
		try {
			String _query = "DELETE FROM Author " + "WHERE ID = 1";
			PreparedStatement _ps = _dbConnection.prepareStatement(_query);
			_ps.executeUpdate();
			System.out.println("Since author has no paper record, removed author");
		} catch (SQLIntegrityConstraintViolationException ex) {
			System.out.println("WARNING: Author has paper record associated with it, please remove it first");
			ex.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();

		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			createConnection();
			System.out.println("Connected to db");
			Scanner _in = new Scanner(System.in);
			System.out.println("Enter author id");
			firstQuery(_in.nextInt());
			System.out.println();
			System.out.println("Enter paper id");
			secondQuery(_in.nextInt());
			System.out.println();
			thirdQuery();
			System.out.println();
			fourthQuery();
			System.out.println();
			fifthQuery();
			System.out.println();
			_in.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

}

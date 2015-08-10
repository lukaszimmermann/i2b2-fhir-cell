import java.sql.*;
import org.h2.jdbcx.JdbcConnectionPool;

public class Test {
	public static void main(String[] args) {
		System.out.println("Running");
		try {
			JdbcConnectionPool cp = JdbcConnectionPool.create("jdbc:h2:~/test",
					"sa", "sa");
				Connection conn = cp.getConnection();
		//		conn.createStatement().execute("CREATE TABLE TEST(ID INT PRIMARY KEY, NAME VARCHAR);"
//+"INSERT INTO TEST VALUES(1, 'Hello World');"
//+"CALL FT_CREATE_INDEX('PUBLIC', 'TEST', NULL);");
				conn.createStatement().execute("INSERT INTO TEST VALUES(2, 'Hello2 World');");
				Statement stmt = conn.createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
				 ResultSet rs = stmt.executeQuery("SELECT * FROM TEST");
				 System.out.println("r:"+rs.toString());
				    ResultSetMetaData rsmd = rs.getMetaData();
				    int columnsNumber = rsmd.getColumnCount();
				    while (rs.next()) {
				        for (int i = 1; i <= columnsNumber; i++) {
				            if (i > 1) System.out.print(",  ");
				            String columnValue = rs.getString(i);
				            System.out.print(columnValue + " " + rsmd.getColumnName(i));
				        }
				        System.out.println("");
				    }
										
				conn.close();
			
			cp.dispose();
		} catch (Exception e) {
			System.out.println("erro");
			e.printStackTrace();
		}
	}
}
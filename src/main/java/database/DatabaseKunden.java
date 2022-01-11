package database;

//alles nˆtige f¸r SQL: 
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement; 


public class DatabaseKunden {
    private static Connection con = null; 
    
    
    public static boolean fuegeKundeHinzu() {
    	boolean erfolg = false; 
    	
 
      	try {
    		con = DatabaseConnection.getConnection(); 
    		PreparedStatement pstmt = con.prepareStatement("INSERT INTO kunden VALUES (?);");
    		pstmt.setInt(1, 122);
    		int zeilen = pstmt.executeUpdate(); 
    		
    		if (zeilen > 0) {
    			erfolg = true; 
    		}
    		
    	} catch (SQLException e) {
    		System.out.println("Fehler"); 
    	} finally {
    		try {
    			con.close(); 
    		} catch (SQLException e){
    			System.out.println("Kann nicht schlieﬂen"); 
    		}
    	}
    	
    	
    	return erfolg; 
    }

}

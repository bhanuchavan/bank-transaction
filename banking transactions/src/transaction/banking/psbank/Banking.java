package transaction.banking.psbank;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Banking {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Connection con = null;
        PreparedStatement ps1 = null, ps2 = null, ps3 = null, ps4 = null;
        ResultSet rs1 = null, rs2 = null;
        String qry1 = "SELECT * FROM pentagon.ps_bank WHERE ACNO=? AND PIN=?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver class loaded and registered");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306?user=root&password=root");
            con.setAutoCommit(false);
            System.out.println("DB connected");
            System.out.println("<---login Module--->");
            System.out.println("Enter the account number:");
            int acc1 = sc.nextInt();
            System.out.println("Enter the PIN");
            int pin1 = sc.nextInt();

            ps1 = con.prepareStatement(qry1);
            ps1.setInt(1, acc1);
            ps1.setInt(2, pin1);
            rs1 = ps1.executeQuery();
            if (rs1.next()) {
                String name1 = rs1.getString(2);
                double bal1 = rs1.getDouble(4);

                System.out.println("Hi " + name1 + ", your account balance is Rs " + bal1);

                System.out.println("<---Transaction details--->");
                System.out.println("Enter the Beneficiary account number");
                int acc2 = sc.nextInt();
                System.out.println("Do you like to continue Transaction");
                String rply = sc.next();
                
                if (rply.equalsIgnoreCase("Y")) {
                    System.out.println("Enter the amount:");
                    double amt = sc.nextDouble();
                    System.out.println("Enter the pin");
                    int pin2 = sc.nextInt();
                    	if (pin2 == pin1) 
                    		{
                    		String qry2 = "UPDATE pentagon.ps_bank SET BALANCE=BALANCE-? WHERE ACNO=?";
                    		ps2 = con.prepareStatement(qry2);
                    		ps2.setDouble(1, amt);
                    		ps2.setInt(2, acc1);
                    		ps2.executeUpdate();
                    		
                    			String qry3 = "UPDATE pentagon.ps_bank SET BALANCE=BALANCE+? WHERE ACNO=?";
                    			ps3 = con.prepareStatement(qry3);
                    			ps3.setDouble(1, amt);
                    			ps3.setInt(2, acc2);
                    			ps3.executeUpdate();

                    			con.commit();
                    			System.out.println("The Amount Has Been Transferred Successfully.");
                    			System.out.println("Your Updated Balance is Rs. " + (bal1 - amt));
                    		} 
                    	else 
                    		{
                    		System.out.println("Invalid PIN. Transaction failed.");
                    		}
                		} 
                	else 
                	{
                    System.out.println("Transaction Cancelled.");
                	}
            		} 
            	else 
            		{
            		System.out.println("Invalid account number or PIN.");
            		}
        		} 
        catch (ClassNotFoundException | SQLException e) 
        	{
            e.printStackTrace();
            try 
            {
                if (con != null)
                    con.rollback();
            } 
            catch (SQLException ex) 
            {
                ex.printStackTrace();
            }
        } 
        finally 
        {
            try 
            {
                if (rs1 != null)
                    rs1.close();
                if (ps1 != null)
                    ps1.close();
                if (ps2 != null)
                    ps2.close();
                if (ps3 != null)
                    ps3.close();
                if (con != null)
		con.close();
	        	}
	        catch (SQLException e)
	        	{
	        	e.printStackTrace();
	        	}
	    	}
       }
}


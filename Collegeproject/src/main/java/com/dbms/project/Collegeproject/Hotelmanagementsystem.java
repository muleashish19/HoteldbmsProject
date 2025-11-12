
package com.dbms.project.Collegeproject;

import java.awt.Taskbar.State;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class Hotelmanagementsystem
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    
    try {
    	Class.forName("com.mysql.cj.jdbc.Driver");
    	String url="jdbc:mysql://localhost:3306/hotel_db";
    	String user="root";
    	String pass="root";
    	
    	Connection conn=DriverManager.getConnection(url,user,pass);
    	
    	System.out.println("dbconnected");
    	
    	while(true) {
    		System.out.println();
    		System.out.println("Hotel Management system");
    		Scanner sc=new Scanner(System.in);
    		System.out.println("1. Reserve a room ");
    		System.out.println("2. view reservation ");
    		System.out.println("3. Get roomnumber ");
    		System.out.println("4. Update Reservation ");
    		System.out.println("5. Delete Reservation ");
    		System.out.println("6. exit ");
    		System.out.println("Choose an Option : ");
    		
    		int choice=sc.nextInt();
    		
    		switch (choice) {
			case 1:
				ReserveRoom(conn,sc);
				break;
			case 2:
				viewReservation(conn,sc);
				break;
			case 3:
				getRoomnumber(conn,sc);
				break;
				
			case 4:
				updateReservation(conn,sc);
				break;
			case 5:
				deleteReservation(conn,sc);
				break;
			case 6:
				exit();
				sc.close();
				return;
			
				
				

			default:
				System.out.println("invalid choice , try again ");
				break;
			}
	
    	}
    }catch(Exception e) {
    	e.printStackTrace();
    }
        
    }
    
    private static void ReserveRoom(Connection conn, Scanner sc) {
    	try {
    		System.out.println("enter guest name: ");
    		String guestname =sc.next();
    		
    		System.out.println("Enter roomnumber : ");
    		int roomnumber =sc.nextInt();
    		
    		System.out.println("Enter contact number: ");
    		String contactnumber=sc.next();
    		
    		String sql="insert into reservation(guest_name,Room_no,contact_number)value(?,?,?)";
    		
    		PreparedStatement pst=conn.prepareStatement(sql);
    		pst.setString(1, guestname);
    		pst.setInt(2, roomnumber);
    		pst.setString(3, contactnumber);
    		
    		int rows=pst.executeUpdate();
    		
    		if(rows>0) {
    			System.out.println("Reservation successful");
    		}else {
    			System.out.println("Reservation failed");
    		}
    				
    		
    	    }catch(Exception e) {
    	    	e.printStackTrace();
    	    }
    		
    		
    		
    	
		// TODO Auto-generated method stub
		
	}
    
	private static void viewReservation(Connection conn, Scanner sc) {
		try{
			String sql="select * from reservation ";
			Statement st=conn.createStatement();
			ResultSet resultset=st.executeQuery(sql);
			
			System.out.println("current Reservation : ");
			System.out.println("+---------------+---------+-------------+------------------+---------------");
			System.out.println("|Reservstion ID | Guest   |Room Number | Contact Number   | Reservationdate");
			System.out.println("+---------------+---------+------------+----------------+-------------------");
			
			while(resultset.next()) {
				int reservationId=resultset.getInt("reservation_id");
				String guestname=resultset.getString("guest_name");
				int Roomnumber=resultset.getInt("Room_no");
				String contactnumber=resultset.getString("contact_number");
				String reservationDate =resultset.getTimestamp("reservation_date").toString();
				
				//format and display the reservation data in table-like formate
				System.out.printf("| %-14d | %-15s | %13d | %-20s |%-19s |\n",
						reservationId,guestname,Roomnumber,contactnumber,reservationDate);
			}
			
			System.out.println("+--------------+-----------------+--------------------+------------------+");
		}catch(Exception e) {
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
	}
	
	private static void getRoomnumber(Connection conn, Scanner sc) {
//		
//		try {
//			System.out.println("Enter reservation id : ");
//			int reservationId =sc.nextInt();
//			//System.out.println("enter guestname :");
//			//String GuestName=sc.next();
//			
//			String sql="select from reservation where reservation_id =? ";
//			PreparedStatement pst=conn.prepareStatement(sql);
//			pst.setInt(1, reservationId);
//		    ResultSet row=pst.executeQuery();
//			
//			if(row.next()) {
//				
//				//System.out.println("ID: "+row.getInt("reservationId"));
//				System.out.println("Name: "+row.getString("guest_name"));
//				System.out.println("Roomno "+row.getInt("Room_no"));
//			}else {
//				System.out.println("reservation not found for the given id and guest name .");
//			}
//		
//			
//			
//			
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		// TODO Auto-generated method stub
		
	}
	
	private static void updateReservation(Connection conn, Scanner sc) {
		try {
			System.out.println("Enter reservation ID to update: ");
		    int reservationId =sc.nextInt();
		    sc.nextLine();
		    
		    if(!reservationExists(conn,reservationId)) {
		    	System.out.println("Reservation not found for the given ID");
		    	return;
		    }
		    System.out.println("Enter new guest name :");
		    String newGuestname = sc.nextLine();
		    System.out.println("enter new room number:  ");
		    int newRoomNumber  =sc.nextInt();
		    System.out.println("enter new contact number : ");
		    String newcontact = sc.next();
		    String sql="update reservation set guest_name = '"+newGuestname+"',"+"Room_no = "+newRoomNumber+", "+
		    "contact_number= '"+newcontact+"' "+
		    "where reservation_id= " +reservationId;
		    
		    try(Statement st=conn.createStatement()){ 
		    	int affectedRows =st.executeUpdate(sql);
		    	
		    	if(affectedRows>0) {
		    		System.out.println("reservation updated successfully");
		    	}else {
		    		System.out.println("reservation update failed");
		    	}
		    	
		    }
		}catch(Exception e) {
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
	}

	private static boolean reservationExists(Connection conn, int reservationId) {
		try {
			String sql ="select reservation_id from reservation where reservation_id =  "+reservationId;
			try(Statement st=conn.createStatement();
				ResultSet resultset=st.executeQuery(sql)){
				return resultset.next();
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		// TODO Auto-generated method stub
		
	}
	
	private static void deleteReservation(Connection conn, Scanner sc) {
		try {
			System.out.println("enter reservation ID to deleted: ");
			int reservationId = sc.nextInt();
			
			if(!reservationExists(conn,reservationId)) {
				System.out.println("Reservation not found for the given ID ");
				return;
			}
			
			String sql="Delete from reservation where reservation_id= "+reservationId;
			
			try (Statement st=conn.createStatement()){
				
				int affecedRows = st.executeUpdate(sql);
				if(affecedRows>0) {
					System.out.println("Reservation deleted successfully");
				}else {
					System.out.println("Reservation deletion failed ");
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
	}

	private static void exit() throws InterruptedException {
		System.out.println("Exiting system ");
		int i=5;
		while(i!=0) {
			System.err.print(",");
			Thread.sleep(450);
			i--;
		}
		System.out.println();
		System.out.println("thankyou for using Reservation System!!!");
		// TODO Auto-generated method stub
		
		
	}
 
}

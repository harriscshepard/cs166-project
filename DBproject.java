
// /*
//  * Template JAVA User Interface
//  * =============================
//  *
//  * Database Management Systems
//  * Department of Computer Science &amp; Engineering
//  * University of California - Riverside
//  *
//  * Target DBMS: 'Postgres'
//  *
//  */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

/*
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */

public class DBproject{
	//reference to physical database connection
	private Connection _connection = null;
	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	public DBproject(String dbname, String dbport, String user, String passwd) throws SQLException {
		System.out.print("Connecting to database...");
		try{
			// constructs the connection URL
			String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
			System.out.println ("Connection URL: " + url + "\n");
			
			// obtain a physical connection
	        this._connection = DriverManager.getConnection(url, user, passwd);
	        System.out.println("Done");
		}catch(Exception e){
			System.err.println("Error - Unable to Connect to Database: " + e.getMessage());
	        System.out.println("Make sure you started postgres on this machine");
	        System.exit(-1);
		}
	}
	
	/**
	 * Method to execute an update SQL statement.  Update SQL instructions
	 * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
	 * 
	 * @param sql the input SQL string
	 * @throws java.sql.SQLException when update failed
	 * */
	public void executeUpdate (String sql) throws SQLException { 
		// creates a statement object
		Statement stmt = this._connection.createStatement ();

		// issues the update instruction
		stmt.executeUpdate (sql);

		// close the instruction
	    stmt.close ();
	}//end executeUpdate

	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and outputs the results to
	 * standard out.
	 * 
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQueryAndPrintResult (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		/*
		 *  obtains the metadata object for the returned result set.  The metadata
		 *  contains row and column info.
		 */
		ResultSetMetaData rsmd = rs.getMetaData ();
		int numCol = rsmd.getColumnCount ();
		int rowCount = 0;
		
		//iterates through the result set and output them to standard out.
		boolean outputHeader = true;
		while (rs.next()){
			if(outputHeader){
				for(int i = 1; i <= numCol; i++){
					System.out.print(rsmd.getColumnName(i) + "\t");
			    }
			    System.out.println();
			    outputHeader = false;
			}
			for (int i=1; i<=numCol; ++i)
				System.out.print (rs.getString (i) + "\t");
			System.out.println ();
			++rowCount;
		}//end while
		stmt.close ();
		return rowCount;
	}
	
	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and returns the results as
	 * a list of records. Each record in turn is a list of attribute values
	 * 
	 * @param query the input query string
	 * @return the query result as a list of records
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException { 
		//creates a statement object 
		Statement stmt = this._connection.createStatement (); 
		
		//issues the query instruction 
		ResultSet rs = stmt.executeQuery (query); 
	 
		/*
		 * obtains the metadata object for the returned result set.  The metadata 
		 * contains row and column info. 
		*/ 
		ResultSetMetaData rsmd = rs.getMetaData (); 
		int numCol = rsmd.getColumnCount (); 
		int rowCount = 0; 
	 
		//iterates through the result set and saves the data returned by the query. 
		boolean outputHeader = false;
		List<List<String>> result  = new ArrayList<List<String>>(); 
		while (rs.next()){
			List<String> record = new ArrayList<String>(); 
			for (int i=1; i<=numCol; ++i) 
				record.add(rs.getString (i)); 
			result.add(record); 
		}//end while 
		stmt.close (); 
		return result; 
	}//end executeQueryAndReturnResult
	
	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and returns the number of results
	 * 
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQuery (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		int rowCount = 0;

		//iterates through the result set and count nuber of results.
		if(rs.next()){
			rowCount++;
		}//end while
		stmt.close ();
		return rowCount;
	}

	


	
	/**
	 * Method to fetch the last value from sequence. This
	 * method issues the query to the DBMS and returns the current 
	 * value of sequence used for autogenerated keys
	 * 
	 * @param sequence name of the DB sequence
	 * @return current value of a sequence
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	
	public int getCurrSeqVal(String sequence) throws SQLException {
		Statement stmt = this._connection.createStatement ();
		
		ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
		if (rs.next()) return rs.getInt(1);
		return -1;
	}

	/**
	 * Method to close the physical connection if it is open.
	 */
	public void cleanup(){
		try{
			if (this._connection != null){
				this._connection.close ();
			}//end if
		}catch (SQLException e){
	         // ignored.
		}//end try
	}//end cleanup

	/**
	 * The main execution method
	 * 
	 * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
	 */
	public static void main (String[] args) {
		if (args.length != 3) {
			System.err.println (
				"Usage: " + "java [-classpath <classpath>] " + DBproject.class.getName () +
		            " <dbname> <port> <user>");
			return;
		}//end if
		
		DBproject esql = null;
		
		try{
			System.out.println("(1)");
			
			try {
				Class.forName("org.postgresql.Driver");
			}catch(Exception e){

				System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
				e.printStackTrace();
				return;
			}
			
			System.out.println("(2)");
			String dbname = args[0];
			String dbport = args[1];
			String user = args[2];
			
			esql = new DBproject (dbname, dbport, user, "");
			
			boolean keepon = true;
			while(keepon){
				System.out.println("MAIN MENU");
				System.out.println("---------");
				System.out.println("1. Add Doctor");
				System.out.println("2. Add Patient");
				System.out.println("3. Add Appointment");
				System.out.println("4. Make an Appointment");
				System.out.println("5. List appointments of a given doctor");
				System.out.println("6. List all available appointments of a given department");
				System.out.println("7. List total number of different types of appointments per doctor in descending order");
				System.out.println("8. Find total number of patients per doctor with a given status");
				System.out.println("9. < EXIT");
				
				switch (readChoice()){
					case 1: AddDoctor(esql); break;
					case 2: AddPatient(esql); break;
					case 3: AddAppointment(esql); break;
					case 4: MakeAppointment(esql); break;
					case 5: ListAppointmentsOfDoctor(esql); break;
					case 6: ListAvailableAppointmentsOfDepartment(esql); break;
					case 7: ListStatusNumberOfAppointmentsPerDoctor(esql); break;
					case 8: FindPatientsCountWithStatus(esql); break;
					case 9: keepon = false; break;
				}
			}
		}catch(Exception e){
			System.err.println (e.getMessage ());
		}finally{
			try{
				if(esql != null) {
					System.out.print("Disconnecting from database...");
					esql.cleanup ();
					System.out.println("Done\n\nBye !");
				}//end if				
			}catch(Exception e){
				// ignored.
			}
		}
	}

	public static int readChoice() {
		int input;
		// returns only if a correct value is given.
		do {
			System.out.print("Please make your choice: ");
			try { // read the integer, parse it and break.
				input = Integer.parseInt(in.readLine());
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);
		return input;
	}//end readChoice

	public static void AddDoctor(DBproject esql) {//1
        try{
            String update = "";
            int d_id;
            String d_name;
            String d_department;
            int d_did;

            System.out.println("\tEnter Doctor ID: $");
            d_id = Integer.parseInt(in.readLine());
            System.out.println("\tEnter Name: $");
            d_name = in.readLine();
            System.out.println("\tEnter Department Name: $");
            d_department = in.readLine();
            System.out.println("\tEnter Department id: $");
            d_did = Integer.parseInt(in.readLine());
			update = "INSERT INTO Doctor (doctor_ID, name, specialty, did) SELECT "+ d_id+", \'"+ d_name+"\', \'"+d_department+"\', "+ d_did +" WHERE NOT EXISTS ( SELECT 1 FROM Doctor WHERE  doctor_ID = "+d_id+")";
            //update = "INSERT INTO Doctor VALUES ("+ d_id + ",\'"+ d_name + "\',\'"+ d_department + "\',"+ d_did+ ");";
            System.out.println("UPDATE: " + update);

            esql.executeUpdate(update);
            //System.out.println ("total row(s): " + rowCount);
        }catch(Exception e){
            System.err.println (e.getMessage());
        }
        // CREATE TABLE Doctor
        //     doctor_ID INTEGER NOT NULL,
        //     name VARCHAR(128),
        //     specialty VARCHAR(24),
        //     did INTEGER NOT NULL,
        //     PRIMARY KEY (doctor_ID),
        //     FOREIGN KEY (did) REFERENCES Department(dept_ID)

   //end QueryExample
        
	}

	public static void AddPatient(DBproject esql) {//2
		try{
			String update = "";
            int p_id;
            String p_name;
            String p_gender;
			int p_age;
			String p_address;
            int p_number_appt = 0;

            System.out.println("\tEnter Patient ID: $");
            p_id = Integer.parseInt(in.readLine());
            System.out.println("\tEnter Patient Name: $");
            p_name = in.readLine();
            System.out.println("\tEnter Patient Gender (M/F): $");
            p_gender = in.readLine();
            System.out.println("\tEnter Patient Age: $");
            p_age = Integer.parseInt(in.readLine());
			System.out.println("\tEnter Patient Address: $");
            p_address = (in.readLine());
            //update = "INSERT INTO Patient VALUES ("+ p_id + ",\'"+ p_name + "\',\'"+ p_gender + "\',"+ p_age+ ",\'" + p_address + "\',"+ p_number_appt + ");";
            update = "INSERT INTO Patient (patient_ID, name, gtype, age, address, number_of_appts) SELECT "+ p_id+", \'"+p_name+"\', \'"+p_gender+"\', "+p_age+", \'"+p_address+"\', "+ p_number_appt +" WHERE NOT EXISTS ( SELECT 1 FROM Patient WHERE patient_ID = "+p_id+");";
			System.out.println("UPDATE: " + update);

            esql.executeUpdate(update);
            //System.out.println ("total row(s): " + rowCount);
        }
		catch(Exception e){
            System.err.println (e.getMessage());
        }
	}

	public static void AddAppointment(DBproject esql) {//3
		try{
			String update = "";
            int a_id;
            String a_date;
            String a_time_slot;
			String a_status;
            
			

            System.out.println("\tEnter Appointment id: $");
            a_id = Integer.parseInt(in.readLine());
            System.out.println("\tEnter Appointment date (MM/DD/YYYY): $");
            a_date = in.readLine();
            System.out.println("\tEnter Appointment timeslot (HH:MM-HH:MM): $");
            a_time_slot = in.readLine();
            System.out.println("\tEnter Appointment status (PA,AC,AV,WL): $");
            a_status = (in.readLine());
            //update = "INSERT INTO Appointment VALUES ("+ a_id + ",\'"+ a_date + "\',\'"+ a_time_slot + "\',\'"+ a_status+ "\'"+ ");";
			update = "INSERT INTO Appointment (appnt_ID, adate, time_slot, status) SELECT "+ a_id+", \'"+a_date+"\', \'"+a_time_slot+"\', \'"+ a_status + "\' WHERE NOT EXISTS ( SELECT 1 FROM Appointment WHERE appnt_ID = "+ a_id+");";
            System.out.println("UPDATE: " + update);

            esql.executeUpdate(update);
            //System.out.println ("total row(s): " + rowCount);
        }
		catch(Exception e){
            System.err.println (e.getMessage());
        }

	}


	public static void MakeAppointment(DBproject esql) {//4
		try{
			// Given a patient, a doctor and an appointment of the doctor that s/he wants to take, add an appointment to the DB
			//current function:
			// take appointment id -> find appointment and hospital using has_appointment ->
			String query = "";
			String update = "";
			int p_id = 999; //patient
			int d_id= 128; //doctor
			int a_id = 96;  //appointment
			String p_name_search_term = "";
			String h_id = "0"; //hospital, is string because its derived from result
			List<List<String>> result; //result from executeQueryAndReturnResult
			List<String> first_tuple; //the first tuple from the result table
	
			//Ask for Appointment id
			System.out.println("\tEnter Appointment id: $");
			a_id = Integer.parseInt(in.readLine());

			System.out.println("\tEnter Doctor id: $");
			d_id = Integer.parseInt(in.readLine());
		
			

			

			//find and get appointment status from a_id
			query = "SELECT * FROM Appointment as A CROSS JOIN has_appointment as H WHERE A.appnt_ID = H.appt_id AND A.appnt_ID = "+a_id+";";
			result = esql.executeQueryAndReturnResult(query);
			first_tuple = result.get(0);
			String status = first_tuple.get(3); //a_id,date,time_slot,status
			System.out.println("QUERY:" + query);
			System.out.println("STATUS: " + status);

			if((status.equals("AC"))||(status.equals("AV"))||(status.equals("WL")))
			{
				//verify p_id
				System.out.println("\tEnter Patient id: $(-1 if not known");
				p_id = Integer.parseInt(in.readLine());
				if(p_id == -1)
				{

				}
				else{

				}
				


				//update patients, increment num appointments
				update = "UPDATE Patient SET number_of_appts = number_of_appts + 1 WHERE patient_ID = "+p_id+";";
				esql.executeUpdate(update);

				//add a tuple to 'has_appointment' if not already there
				update = "INSERT INTO has_appointment (appt_id, doctor_id) "+"SELECT "+a_id+", "+d_id + " WHERE NOT EXISTS (" + "SELECT 1 FROM has_appointment WHERE appt_id = "+a_id+" AND doctor_id = "+d_id+");";
				System.out.println("UPDATE: " + update);//
				esql.executeUpdate(update);
				
				//find and get hid from Department->Doctor->has_appointment from a_id
				query = "SELECT MAX(hid) FROM Department as D CROSS JOIN Doctor as O CROSS JOIN has_appointment as H WHERE D.dept_ID = O.did AND O.doctor_ID = H.doctor_id AND appt_id = "+a_id+";";
				result = esql.executeQueryAndReturnResult(query);
				first_tuple = result.get(0);
				h_id = first_tuple.get(0);
				System.out.println("QUERY:" + query);
				System.out.println("HID: " + h_id);

				//add a tuple to 'searches' if not already there
				update = "INSERT INTO searches (hid,pid,aid) SELECT "+h_id+", "+p_id+", "+a_id + " WHERE NOT EXISTS ( SELECT 1 FROM searches WHERE hid ="+h_id+" AND pid = "+p_id+" AND aid =" + a_id + ");";
				System.out.println("UPDATE: " + update);
				esql.executeUpdate(update);
			}
			else if(status.equals("PA"))
			{
				System.out.println("PA: " + status);
				//do nothing?
			}
			else{
				//error? status is not valid
			}

			if(status.equals("AC"))
			{
				
				//update appointment to WL
			
				update = "UPDATE Appointment SET _STATUS = \'WL\' WHERE appnt_ID = " + a_id +";";
				esql.executeUpdate(update);
			}
			else if(status.equals("AV"))
			{
				update = "UPDATE Appointment SET _STATUS = \'AC\' WHERE appnt_ID = " + a_id +";";
				esql.executeUpdate(update);
				//update appointment to AC
				
			}
			else if(status.equals("WL"))
			{
				update = "UPDATE Appointment SET _STATUS = \'WL\' WHERE appnt_ID = " + a_id +";";
				esql.executeUpdate(update);
				//dont change status
				
			}
			
		}



		catch(Exception e){
            System.err.println (e.getMessage());
        }

	}

	public static void ListAppointmentsOfDoctor(DBproject esql) {//5
		// For a doctor ID and a date range, find the list of active and available appointments of the doctor
	}

	public static void ListAvailableAppointmentsOfDepartment(DBproject esql) {//6
		// For a department name and a specific date, find the list of available appointments of the department
	}

	public static void ListStatusNumberOfAppointmentsPerDoctor(DBproject esql) {//7
		// Count number of different types of appointments per doctors and list them in descending order
	}

	
	public static void FindPatientsCountWithStatus(DBproject esql) {//8
		// Find how many patients per doctor there are with a given status (i.e. PA, AC, AV, WL) and list that number per doctor.
	}
}


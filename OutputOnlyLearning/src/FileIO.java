/************************************************************************
* \brief: Class for File-IO, reads in single values and the table       *
*         for the spiral problem                                        *
*																		*
* (c) copyright by Jörn Fischer											*
*                                                                       *																		* 
* @autor: Prof.Dr.Jörn Fischer											*
* @email: j.fischer@hs-mannheim.de										*
*                                                                       *
* @file : MainFrame.java                                                *
*************************************************************************/

import java.io.*;

public class FileIO {
	byte[] inText; // memory for the file
	int fPointer;  // filepointer to Memory
	long length;   // length of File
	File file;	   // File object

	public double value[][];

	public int maxRow;
	public int maxCol; // x

	/**
	 * @brief: opens a file and reads in the data
	 * 
	 * @param filename is the name of the file
	 */
	public FileIO(String filename){
		file = new File(filename);
		fPointer=0;
		try{
			readFile();
		}catch(IOException ex){System.out.println("File: Read Error...");}
		//System.out.println(myString);	
	}
	/**
	 * @brief: fgetc is similar to the c version and reads in a single character	
	 * @return
	 */
	public byte fgetc()
	{
		if (fPointer<length){
			return inText[fPointer++];
		}
		else{
			return 0;
		}
	}
	/**
	 * @brief: This function reads a single value of the file
	 * 
	 * @return The read value is returned 
	 */
	public double readSingleValue(){
		String myString="";
		//System.out.println("ReadSingleValue");

		byte x;
		
		do{
			//System.out.println("t="+t+"length="+length);
			x =fgetc();
		}while((x<'0' || x>'9') && x!='.');
		fPointer--;
		do{
			//System.out.println("t="+t+"length="+length);
			x =fgetc();
			if ((x>='0' && x<='9') || x=='.'){
				myString += (char)(x);
			}
			
		}while((x>='0' && x<='9') || x=='.');
		
		if (myString.length()==0){
			return 0.0;
		}
		else{
			float y = Float.parseFloat(myString);		// first line defines number of input neurons
		//	System.out.println("readSingleValue = "+y);
			return y;
		}
	}

	/**
	 * æbrief: readTable reads in the Whole table 
	 * @param sizeCol 
	 * @param sizeRow
	 */
	public void readTable(int sizeCol, int sizeRow){

		int row=0,col=0;
		maxCol=1;
		maxRow=1;
		value = new double[sizeCol][sizeRow];

		byte x;
		do {
			String myString;
			do {
				x =fgetc();
				if ((int)(x)==10) {
					if (col>maxCol) {
						maxCol=col;
					}
					if (col>0) {
						row++;
					}
					col=0;
					
				}
			} while((x<'0' || x>'9') && x!='-' && x!='.' && x!=0);
			fPointer--;
			myString="";
			do {
				//System.out.println("t="+t+"length="+length);
				x =fgetc();
				if ((x>='0' && x<='9') || x=='.' || x=='-') {
					myString += (char)(x);
				}

			} while((x>='0' && x<='9') || x=='.'|| x=='-');
				
			if (myString.length()==0) {
			} else {
				float y = Float.parseFloat(myString);		// first line defines number of input neurons
				//System.out.println("readSingleValue = "+y);
				if (row<sizeRow && col<sizeCol) {
					value[col][row]= y;
					col++;
					maxRow=row;
				}
			}
		} while(fPointer<inText.length);

		System.out.println(maxRow + ","+maxCol);
		
		for (row = 0; row<maxRow; row++){
			for (col = 0;col<maxCol;col++){
				System.out.print(value[col][row]+",");
			}
			System.out.println();
		}
	}
	/**
	 * reads the file opened in the constructor in a buffer
	 * 
	 * @throws IOException
	 */

	public void readFile() throws IOException {        
    	
    	
        length = file.length();
        
        
        if (length > Integer.MAX_VALUE) {
            // File is too large
            throw new IOException("File is too large!");
        }
        inText = new byte[(int)length];
        // Create the byte array to hold the data
        

        // Read in the bytes
        int offset = 0;
        int numRead = 0;

        InputStream is = new FileInputStream(file);
        try {
            while (offset < inText.length
                   && (numRead=is.read(inText, offset, inText.length-offset)) >= 0) {
                offset += numRead;
            }
        } finally {
            is.close();
        }

        // Ensure all the bytes have been read in
        if (offset < inText.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

    }	
	/**
	 * @brief: writes a file with the specified filename from the buffer buf to harddisk
	 * @param fileName
	 * @param buf
	 */
    public static void writeFile(String fileName, byte[] buf)
    {
		
		FileOutputStream fos = null;
		
		try
		{
		   fos = new FileOutputStream(fileName);
		   fos.write(buf);
		}
		catch(IOException ex)
		{
		   System.out.println(ex);
		}
		finally
		{
		   if(fos!=null)
		      try
		      {
		         fos.close();
		      }
		      catch(Exception ex)
		      {
		      }
		}
    }

}

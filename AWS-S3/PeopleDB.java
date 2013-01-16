import java.io.File;
import java.io.IOException;


public class PeopleDB {
		/**
		 * @param args
		 * 
		 */
		public static void main (String[] args) throws IOException {
			
			PeopleDBManager pdb = new PeopleDBManager();
			
			String localFolderName = "C:\\Users\\Lisa\\workspace\\People\\";
			File localFolder = new File(localFolderName);
			
			pdb.setupStorage();
			pdb.RecursiveUpload (localFolder, "");
			pdb.registerStar("Chris Evans", "Avengers");
			pdb.registerStar("Daniella Alonso", "The Hills Have Eyes II");
			pdb.registerNobel("Carol Greider", "Physiology or Medicine", "2009");
			pdb.registerNobel("Elizabeth Blackburn", "Physiology or Medicine", "2009");
		}	
	}
		



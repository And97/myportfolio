package it.myportfolio.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

public class BlockchainTransactionService {

	public static String registry_transaction(Long UserID, List<Long> ImageID, Date timestamp) {

		try {
			// Percorso dello script Python da eseguire
			String scriptPath = "registry.py";

			// Parametri da passare allo script Python
			String UID = UserID.toString();
			String IID="";
			for (Long id : ImageID) {
				IID=IID+id.toString()+","; 
			}
			IID=IID.substring(0, IID.length()-1);
			String date = timestamp.toString();
			
			// Costruisci il comando da eseguire
			ProcessBuilder pb = new ProcessBuilder("python", scriptPath, UID, IID, date);
			//System.out.println(pb.toString() + pb.command().toString());
			// Avvia il processo
			Process process = pb.start();

			// Ottieni l'output del processo
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = reader.readLine();
			//System.out.println("LINE: " + line);

			// read any errors from the attempted command
			BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String s;
			while ((s = stdError.readLine()) != null) {
				System.out.println("stdError:  "+ s );
			}

			// Attendere il termine dell'esecuzione dello script
			int exitCode = process.waitFor();
			if (exitCode == 0) {
				return line;
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String verify_hash(String Hash) {

		try {
			// Percorso dello script Python da eseguire
			String scriptPath = "hash_verify.py";

			// Costruisci il comando da eseguire
			ProcessBuilder pb = new ProcessBuilder("python", scriptPath, Hash);

			// Avvia il processo
			Process process = pb.start();

			// Ottieni l'output del processo
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = reader.readLine();
			System.out.println(line);

			BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String s;
			System.out.println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}

			// Attendere il termine dell'esecuzione dello script
			int exitCode = process.waitFor();
			if (exitCode == 0) {
				return line;
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
}

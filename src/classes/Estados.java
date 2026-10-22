package classes;

public class Estados {

	private static String[] siglas = {
			"PR", "SC", "RS", "MG", "ES", "RJ", "SP"
	};
	
	public static boolean valido(String sigla) {
		int cont = 0;
		while (cont < quantidade()) {
			if (siglas[cont].equalsIgnoreCase(sigla)) {
				return true;
			}
			cont++;
		}
		return false;
	}
	
	public static int quantidade() {
		return siglas.length;
	}
	
	public static String get(int i) {
		return siglas[i];
	}
}

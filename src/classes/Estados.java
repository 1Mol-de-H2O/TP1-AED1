package classes;

public class Estados {

	private static String[] SIGLAS = {
			"PR", "SC", "RS", "MG", "ES", "RJ", "SP"
	};
	
	public static boolean valido(String sigla) {
		int cont = 0;
		while (cont < quantidade()) {
			if (SIGLAS[cont].equalsIgnoreCase(sigla)) {
				return true;
			}
			cont++;
		}
		return false;
	}
	
	public static int quantidade() {
		return SIGLAS.length;
	}
	
	public static String get(int i) {
		return SIGLAS[i];
	}
}

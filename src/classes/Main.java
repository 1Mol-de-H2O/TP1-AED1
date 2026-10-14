package classes;

import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		String arquivo = args.length > 0 ? args[0] : "candidatos.txt";
		Agencia agencia = new Agencia();

		try {
			agencia.lerArquivo(arquivo);
		} catch (IOException e) {
			System.out.println("Erro ao ler '" + arquivo + "': " + e.getMessage());
			return;
		}
		System.out.println(agencia.getTotal() + " candidatos carregados de " + arquivo + ".");

		Scanner sc = new Scanner(System.in);
		int opcao = -1;

		while (opcao != 0) {
			System.out.println("===== AGÊNCIA DE RELACIONAMENTO =====" +
						       "1 - Inserir/atualizar grau de interesse desejado" +
							   "2 - Potenciais companheiros de um candidato" +	
						       "3 - K candidatos com mais potenciais companheiros" + 
							   "4 - Candidatos sem nenhum companheiro" + 
						       "5 - Candidatos com interesse em uma área" +
							   "6 - Candidatos que residem em um estado" +
						       "7 - Cadastrar novo candidato (grava no arquivo)" +
							   "0 - Sair");

			boolean opcaoLida = false;
			while (!opcaoLida) {
				System.out.print("Opção: ");
				try {
					opcao = Integer.parseInt(sc.nextLine().trim());
					opcaoLida = true;
				} catch (NumberFormatException e) {
					System.out.println("Digite um número inteiro.");
				}
			}
			System.out.println();

			switch (opcao) {
			case 1: {
				int id = 0;
				boolean idLido = false;
				while (!idLido) {
					System.out.print("Id do candidato: ");
					try {
						id = Integer.parseInt(sc.nextLine().trim());
						idLido = true;
					} catch (NumberFormatException e) {
						System.out.println("Digite um número inteiro.");
					}
				}

				int grau = 0;
				boolean grauLido = false;
				while (!grauLido) {
					System.out.print("Grau de interesse desejado (1 a 15): ");
					try {
						grau = Integer.parseInt(sc.nextLine().trim());
						grauLido = true;
					} catch (NumberFormatException e) {
						System.out.println("Digite um número inteiro.");
					}
				}

				System.out.print(agencia.defineGrau(id, grau));
				break;
			}
			case 2: {
				int id = 0;
				boolean idLido = false;
				while (!idLido) {
					System.out.print("Id do candidato: ");
					try {
						id = Integer.parseInt(sc.nextLine().trim());
						idLido = true;
					} catch (NumberFormatException e) {
						System.out.println("Digite um número inteiro.");
					}
				}

				System.out.print(agencia.companheirosDe(id));
				break;
			}
			case 3: {
				int k = 0;
				boolean kLido = false;
				while (!kLido) {
					System.out.print("Valor de K: ");
					try {
						k = Integer.parseInt(sc.nextLine().trim());
						kLido = true;
					} catch (NumberFormatException e) {
						System.out.println("Digite um número inteiro.");
					}
				}

				System.out.print(agencia.topK(k));
				break;
			}
			case 4:
				System.out.print(agencia.semCompanheiros());
				break;
			case 5: {
				int area = 0;
				boolean areaLida = false;
				while (!areaLida) {
					System.out.print("Área (1 Esportes, 2 Artes, 3 Música, 4 Cinema, "
							+ "5 Tecnologia, 6 Animais, 7 Gastronomia, 8 Ciências): ");
					try {
						area = Integer.parseInt(sc.nextLine().trim());
						areaLida = true;
					} catch (NumberFormatException e) {
						System.out.println("Digite um número inteiro.");
					}
				}

				System.out.print(agencia.candidatosPorArea(area));
				break;
			}
			case 6: {
				System.out.print("Sigla do estado: ");
				String sigla = sc.nextLine().trim();
				System.out.print(agencia.candidatosPorEstado(sigla));
				break;
			}
			case 7: {
				System.out.print("Nome: ");
				String nome = sc.nextLine();
				System.out.print("Estado (sigla): ");
				String estado = sc.nextLine();
				System.out.print("Sexo (F/M): ");
				String sexo = sc.nextLine();
				System.out.print("Sexo do potencial companheiro (F/M/I): ");
				String sexoInteresse = sc.nextLine();
				System.out.print("Áreas de interesse (códigos de 1 a 8 separados por espaço, "
							   + "ex.: 1 5 7; deixe vazio para nenhuma): ");
				String areas = sc.nextLine();

//				System.out.print(agencia.cadastraCandidato(nome, estado, sexo, sexoInteresse, areas));
				break;
			}
			case 0:
				System.out.println("Encerrando.");
				break;
			default:
				System.out.println("Opção inválida.");
			}
			System.out.println();
		}
		sc.close();
	}
}
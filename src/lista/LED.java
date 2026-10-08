package lista;

import java.lang.foreign.AddressLayout;

public class LED <T>{
	
	private Celula head;
	private Celula tail;
	private int size;
	
	class Celula {
		T item;
		Celula prox;
		
		public Celula(T item) {
			this.item = item;
		}
	}
	
	public LED(){
		head = new Celula(null);
		tail = head;
	}
	
	public void add(T item) {
		if (item != null) {
			tail.prox = new Celula(item);
			tail = tail.prox;
			size++;
			}
			
		}
		
		
	}
	
}

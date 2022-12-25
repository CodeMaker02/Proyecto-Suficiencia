package Main;

import java.awt.EventQueue;

import Clases.Diagrama;
import Interfaz.Principal;

public class Main {

	
	
	public static void main(String[] args) {

		System.out.println("Este es un nuevo mensaje mejorado");
		System.out.println("Este es un nuevo mensaje mejorado2");
		EventQueue.invokeLater(new Runnable() {		

			public void run() {
				try {
					Principal frame = new Principal(new Diagrama());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}

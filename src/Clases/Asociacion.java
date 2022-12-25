package Clases;

public class Asociacion extends Clase{

	private Concreta clase1;
	private Concreta clase2;

	public Asociacion(String nombre, Concreta clase1, Concreta clase2) throws Exception {
		super(nombre);
        this.setClase1(clase1);
        this.setClase2(clase2);

	}

	public void setClase1(Concreta clase1) {
		if(clase1!=null && clase1 instanceof Concreta)
			this.clase1 = clase1;
		else
			throw new IllegalArgumentException();
	}

	public void setClase2(Concreta clase2) {
		if(clase1!=null && clase1 instanceof Concreta)
			this.clase2 = clase2;
		else
			throw new IllegalArgumentException();
	}

	public Concreta getClase1() {
		return clase1;
	}

	public Concreta getClase2() {
		return clase2;
	}

}

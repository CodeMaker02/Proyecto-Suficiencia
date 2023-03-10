package Clases;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import util.Flecha;
import Interfaz.Lienzo;


public class Diagrama implements Validable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Clase> clases;
	private String nombre;
	@JsonIgnore
	private static Diagrama diagrama;
	private ArrayList<Flecha> flechasHerencia;




	private Diagrama(String nombre) {

		this.clases = new ArrayList<Clase>();
		this.nombre = nombre;

		this.flechasHerencia = new ArrayList<Flecha>();
	}

	private Diagrama(){}

	public static Diagrama getInstance(String nombre){
		if(diagrama == null)
			diagrama = new Diagrama(nombre);

		return diagrama;

	}

	public void addFlechaHerencia(Flecha flecha){
		this.flechasHerencia.add(flecha);
	}

	public static void setInstance(Diagrama d){
		diagrama = d;
	}

	public String getNombre() {
		return nombre;
	}



	public ArrayList<Flecha> getFlechasHerencia() {
		return flechasHerencia;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public static Diagrama getInstance(){
		return diagrama;
	}


	public ArrayList<Clase> getClases() {
		return clases;
	}


	public void addClase(Clase clase){

		if(this.validar(clase))
			this.clases.add(clase);
		else
			throw new IllegalArgumentException();
	}


	@Override
	public boolean validar(Object clase) {
		boolean validator = true;

		for(int i=0;i<this.clases.size() && validator == true;i++){
			if(((Clase) clase).getNombre().equalsIgnoreCase(this.clases.get(i).getNombre())){
				validator = false;

			}
		}
		return validator;
	}

	private boolean validar(Clase clase, int posicionIgnorar){

		boolean validator = true;

		if(clase==null)
			validator= false;
		else{
			for(int i=0;i<this.clases.size()&& validator==true;i++){
				if(clase.getNombre().equals(this.clases.get(i).getNombre()) && i!= posicionIgnorar) 
					validator = false;


			}
		}

		return validator;

	}

	public Clase obtenerPadreClase(String nombreClase){
		return this.buscarClase(nombreClase).getPadre();
	}

	public void addHerencia(String nombreClasePadre, String nombreClaseHija) throws Exception{
		Clase clasePadre = this.buscarClase(nombreClasePadre);
		Clase claseHija = this.buscarClase(nombreClaseHija);
		if((clasePadre!=null && claseHija!=null) && !claseHija.equals(clasePadre) && !claseHija.isDescendiente(clasePadre))
			this.addHerencia(clasePadre, claseHija);
		else
			throw new Exception("Herencia Propia");
	}

	private void addHerencia(Clase clasePadre, Clase claseHija) throws Exception{

		if(claseHija.getPadre()==null){
			claseHija.addPadre(clasePadre);
			this.actualizarHerencia();
		}
		else
			throw new Exception("Herencia Multiple");

	}

	public void actualizarHerencia(){

		for(int i=0;i<this.clases.size();i++){
			for(int j=0;j<this.clases.size();j++){
				if(this.clases.get(j).isMiPadre(this.clases.get(i)) && !this.clases.get(i).isMiHijo(this.clases.get(j))){

					System.out.println("Padre: "+this.clases.get(i).getNombre()+" "+"Hijo: " +this.clases.get(j).getNombre());
					this.clases.get(i).addHijo(this.clases.get(j));
				}
			}
		}
	}
	
	public void cargarHijos(){
		
		for (Clase c : this.clases) {
			c.setHijos(new ArrayList<Clase>());
		}
		this.actualizarHerencia();
	}

	public ArrayList<Clase> buscarHijosdeClase(String nombreClase){
		return this.buscarClase(nombreClase).getHijos();

	}

	public void addAtributo(String nombreClase, Atributo atributo) throws Exception{
		buscarClase(nombreClase).addAtributo(atributo);
	}

	public void addMetodo(String nombreClase, Metodo metodo) throws Exception{
		this.buscarClase(nombreClase).addMetodo(metodo);
	}

	public void modificarAtributo(String nombreClase, String nombreAtributo, Atributo atributo) throws Exception{
		this.buscarClase(nombreClase).modificarAtributo(nombreAtributo, atributo);
	}

	public void modificarMetodo(String nombreClase, String nombreMetodo, ArrayList<String> parametros, Metodo metodo) throws Exception{
		this.buscarClase(nombreClase).modificarMetodo(nombreMetodo, parametros, metodo);
	}

	public void modificarClase(String nombreClase, Clase claseNueva) throws Exception{
		boolean validator = false;

		for(int i=0; i<this.clases.size() && validator == false;i++){
			if(nombreClase.equalsIgnoreCase(this.clases.get(i).getNombre())){
				if(this.validar(claseNueva, i)){
					claseNueva.setAtributos(this.clases.get(i).getAtributos());
					claseNueva.setMetodos(this.clases.get(i).getMetodos());
					claseNueva.setPadre(this.clases.get(i).getPadre());
					claseNueva.setHijos(this.clases.get(i).getHijos());
					claseNueva.setColor(this.clases.get(i).getColor());
					claseNueva.setPosicionX(this.clases.get(i).getPosicionX());
					claseNueva.setPosicionY(this.clases.get(i).getPosicionY());
					this.modificarClaseHerencia(this.clases.get(i), claseNueva);
					this.clases.set(i,claseNueva);
					validator = true;
				}
				else{			
					throw new Exception("Clase mismo nombre");			
				}
			}
		}

	}

	public void modificarClase(String nombreClase) throws Exception{
		boolean validator = false;
		Clase claseAbstracta = new Abstracta(nombreClase,0,0);

		for(int i=0; i<this.clases.size() && validator == false;i++){
			if(nombreClase.equalsIgnoreCase(this.clases.get(i).getNombre())){

				claseAbstracta.setAtributos(this.clases.get(i).getAtributos());
				claseAbstracta.setMetodos(this.clases.get(i).getMetodos());
				claseAbstracta.setPadre(this.clases.get(i).getPadre());
				claseAbstracta.setHijos(this.clases.get(i).getHijos());
				claseAbstracta.setColor(this.clases.get(i).getColor());
				claseAbstracta.setPosicionX(this.clases.get(i).getPosicionX());
				claseAbstracta.setPosicionY(this.clases.get(i).getPosicionY());
				this.modificarClaseHerencia(this.clases.get(i), claseAbstracta);
				this.clases.set(i,claseAbstracta);
				validator = true;

			}
		}

	}

	private void modificarClaseHerencia(Clase claseVieja, Clase claseNueva){
		for (Clase c : this.clases) {
			if(c.isMiPadre(claseVieja))
				c.setPadre(claseNueva);
			else if(c.isMiHijo(claseVieja))
				c.modificarHijo(claseVieja, claseNueva);

		}
	}



	public void CambiarMetodosAbstractosAConcretos(String nombreClase){
		System.out.println(nombreClase);
		this.buscarClase(nombreClase).cambiarMetodosAbstractosAConcretos();
	}

	public Clase buscarClase(String nombre){
		Clase clase = null;
		boolean validator = false;
		for(int i=0; i<this.clases.size() && validator == false;i++){
			if(nombre.equalsIgnoreCase(this.clases.get(i).getNombre())){
				clase = this.clases.get(i);
				validator = true;	
			}
		}

		return clase;
	}



	public void eliminarClase(String nombre){
		Clase claseEliminar = this.buscarClase(nombre);

		if(claseEliminar!=null){
			this.eliminarPadre(claseEliminar);
			this.eliminarHijo(claseEliminar);
			this.clases.remove(claseEliminar);
			this.eliminarRelaciones(nombre);
		}
		else
			throw new IllegalArgumentException();
	}

	public void eliminarPadre(Clase clase){

		for(int i = 0; i < this.clases.size(); i++){
			if(this.clases.get(i).isMiPadre(clase)){
				System.out.println("Se elimino padre");
				this.clases.get(i).eliminarPadre();
			}
		}
	}

	public void eliminarHijo(Clase clase){

		for(int i = 0; i < this.clases.size(); i++){
			if(this.clases.get(i).isMiHijo(clase)){
				System.out.println("Se elimino hijo");
				this.clases.get(i).eliminarHijo(clase);
			}
		}

	}


	public int totalClases(){
		return this.clases.size();
	}

	public int cantClasesConcretas(){
		int count = 0;

		for (Clase a : this.clases) {
			if(a instanceof Concreta)
				count++;		
		}


		return count;

	}

	public int cantClasesAbstractas(){
		int count = 0;

		for (Clase a : this.clases) {
			if(a instanceof Abstracta)
				count++;	

		}

		return count;

	}

	public int totalAtributos(){
		int cant = 0;

		for (Clase a : this.clases) {

			cant+=a.cantAtributos();

		}

		return cant;

	}

	public int totalMetodos(){
		int cant = 0;

		for (Clase a : this.clases) {

			cant+=a.cantMetodos();

		}

		return cant;

	}

	public int cantMetodosConcretos(){
		int cant = 0;

		for (Clase a : this.clases) {

			cant+=a.cantMetodosConcretos();

		}

		return cant;

	}

	public int cantMetodosAbstractos(){
		int cant = 0;

		for (Clase a : this.clases) {

			cant+=a.cantMetodosAbstractos();

		}

		return cant;

	}

	public int totalParametros(){

		int cant = 0;

		for (Clase a : this.clases) {

			cant+=a.totalParametros();

		}

		return cant;

	}

	public ArrayList<Atributo> obtenerAtributosClase(String nombreClase){
		Clase clase = this.buscarClase(nombreClase);
		ArrayList<Atributo> atributos = new ArrayList<Atributo>();
		if(clase!=null)
			atributos = clase.ObtenerAtributos();
		else
			throw new IllegalArgumentException();

		return atributos;

	}

	public ArrayList<Metodo> obtenerMetodoClase(String nombreClase){
		Clase clase = this.buscarClase(nombreClase);
		ArrayList<Metodo> metodos = new ArrayList<Metodo>();
		if(clase!=null)
			metodos= clase.ObtenerMetodos();
		else
			throw new IllegalArgumentException();

		return metodos;

	}

	public ArrayList<Clase> clasesAbstractas(){
		ArrayList<Clase> clasesAbstractas = new ArrayList<Clase>();

		for (Clase a : this.clases) {
			if(a instanceof Abstracta)
				clasesAbstractas.add(a);	

		}

		return clasesAbstractas;

	}

	public ArrayList<Clase> clasesConcretas(){
		ArrayList<Clase> clasesConcretas = new ArrayList<Clase>();

		for (Clase a : this.clases) {
			if(a instanceof Concreta)
				clasesConcretas.add(a);	

		}

		return clasesConcretas;

	}

	public ArrayList<Clase> clasesAsociacion(){
		ArrayList<Clase> clasesAsociacion = new ArrayList<Clase>();

		for (Clase a : this.clases) {
			if(a instanceof Asociacion)
				clasesAsociacion.add(a);	

		}

		return clasesAsociacion;

	}


	//Reporte 1
	public ArrayList<Clase> clasesConMasSuperClases(){
		ArrayList<Clase> clases = new ArrayList<Clase>();
		int mayor = -1;

		for (Clase a : this.clases) {
			if(a.cantProgenitores() > mayor){
				clases.clear();
				clases.add(a);
				mayor = a.cantProgenitores();

			}
			else if (a.cantProgenitores() == mayor){
				clases.add(a);
			}

		}

		return clases;
	}
	//Reporte 2
	public ArrayList<Clase> claseMasRelaciones(){
		ArrayList<Clase> clasesMasRelaciones = new ArrayList<Clase>() ;
		int cantRelaciones = 0;
		int mayor = 0;

		for (Clase a : this.clases) {
			cantRelaciones = a.cantRelaciones();
			if(cantRelaciones > mayor){
				clasesMasRelaciones.clear();
				clasesMasRelaciones.add(a);
				mayor = cantRelaciones;		
			}
			else if (cantRelaciones == mayor){
				clasesMasRelaciones.add(a);
			}
		}


		return clasesMasRelaciones;
	}

	//Reporte 3
	public ArrayList<Clase> clasesSoloMetodosAbstractos(){
		ArrayList<Clase> clases = new ArrayList<Clase>();

		for (Clase a : this.clases) {
			if(a instanceof Abstracta && ((Abstracta) a).soloMetedosAbstractos())
				clases.add(a);
		}

		return clases;

	}

	public void eliminarAtributo(String nombreClase, String nombreAtributo){
		this.buscarClase(nombreClase).eliminarAtributo(nombreAtributo);

	}

	public void eliminarMetodo(String nombreClase, String nombreMetodo, ArrayList<String> parametros){
		this.buscarClase(nombreClase).elminarMetodo(nombreMetodo, parametros);
	}

	public boolean equals(Diagrama d){
		boolean verificador = false;

		if(this.equalsNombre(d) && this.equalsClases(d))
			verificador = true;

		return verificador;
	}

	private boolean equalsNombre (Diagrama d){
		boolean verificador = false;

		if(this.nombre.equals(d.getNombre()))
			verificador = true;

		return verificador;
	}

	private boolean equalsClases (Diagrama d){
		boolean verificador = true;
		ArrayList<Clase> c = d.getClases();
		if(this.clases.size() == c.size()){
			for (int i = 0; i < this.clases.size() && verificador; i++) {
				System.out.println("Entreeeee");
				if(!this.clases.get(i).equals(c.get(i)))
					verificador = false;

			}
		}
		else
			verificador = false;

		return verificador;
	}

	public void eliminarRelaciones(String nombre){
		int i = 0;
		boolean x = false;
		if(this.flechasHerencia.size() != 0){

			while(i<this.flechasHerencia.size()){
				x = false;
				if(this.flechasHerencia.get(i).getHijo().equals(nombre)||this.flechasHerencia.get(i).getPadre().equals(nombre)){
					this.flechasHerencia.remove(i);
					x = true;
				}
				i++;
				if(x){
					i = 0;
				}
			}
		}
	}

}

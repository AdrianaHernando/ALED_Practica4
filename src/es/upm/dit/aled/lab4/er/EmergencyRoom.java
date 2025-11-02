package es.upm.dit.aled.lab4.er;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import es.upm.dit.aled.lab4.er.gui.Position2D;

/**
 * Models a hospital ER. It is characterized by a colletion of patients and the
 * areas that these patients will be treated in. Patients extends the class Thread.
 * 
 * @author rgarciacarmona
 */
public class EmergencyRoom {

	private Map<String, Area> areas;
	private Map<Integer, Patient> patients;

	/**
	 * Builds a new EmergencyRoom
	 */
	public EmergencyRoom() {
		this.areas = new HashMap<String, Area>();
		this.patients = new HashMap<Integer, Patient>();
	}

	/**
	 * Adds a new Area to the EmergencyRoom
	 * 
	 * @param area The new Area.
	 */
	public void addArea(Area area) {
		this.areas.put(area.getName(), area);
	}

	/**
	 * Returns the Area that corresponds to the given name.
	 * 
	 * @param name The name of the Area.
	 * @return     The Area.
	 */
	public Area getArea(String name) {
		return areas.get(name);
	}

	/**
	 * Returns a List with all the Areas of the EmergencyRoom.
	 * 
	 * @return The List.
	 */
	public List<Area> getAreas() { //FIJARME PORQUE ES EL GET DE UNA LISTA
		Collection<Area> areas = this.areas.values();
		return new ArrayList<Area>(areas);
	}

	/**
	 * Adds a new Patient to the EmergencyRoom
	 * 
	 * @param patient The new Patient.
	 */
	public void addPatient(Patient patient) {
		this.patients.put(patient.getNumber(), patient);
	}

	/**
	 * Returns a List with all the Patients of the EmergencyRoom.
	 * 
	 * @return The List.
	 */
	public List<Patient> getPatients() {
		Collection<Patient> patients = this.patients.values();
		return new ArrayList<Patient>(patients);
	}

	/**
	 * Populates the EmergencyRoom with the data contained in a text file.
	 * 
	 * @param in The path of the file.
	 */
	public void readFile(Scanner in) {
		Random rand = new Random(13); // For assigning random colors

		while (in.hasNext()) {
			String tipo = in.next(); //leemos el primer valor de la línea del file
			if (tipo.equals("AREA")) { //Si es AREA
				int x = in.nextInt(); // segundo valor - posicion x
				int y = in.nextInt(); // tercer valor - posicion y
				int time = in.nextInt(); // cuarto valor - tiempo de espera del paciente
				String name = in.next().trim(); // quinto valor - nombre del area
				Area area = new Area(name, time, new Position2D(x, y)); // Construimos nuevo area rellenando sus atributos
				// Assign a random-ish color for visualization
				area.setColor(Color.getHSBColor(rand.nextFloat(), 0.3f, 0.9f));
				areas.put(name, area); //añado el nuevo area al mapa de areas
			} else if (tipo.equals("PATIENT")) {
				int id = in.nextInt();
				Area initial = areas.get(in.next()); //conseguir el objeto area correspondiente al nombre del area que aparece en el file
				Patient patient = new Patient(id, initial); //construye nuevo paciente
				// Assign a random-ish color for visualization
				patient.setColor(Color.getHSBColor(rand.nextFloat(), 1.0f, 0.8f));
				patients.put(id, patient); //añado el nuevo paciente al mapa de pacientes
			} else if (tipo.equals("TRANSFER")) {
				int patientId = in.nextInt();
				String areaName = in.next();
				int time = in.nextInt();
				Transfer transfer = new Transfer(areas.get(areaName), time); 
				patients.get(patientId).addToProtocol(transfer); //añado a la lista de protocolos "protocol" del paciente correspondiente el tranfer creado
			} else {
				System.err.println("Error in input file. Unknown type: " + tipo);
			}
		}
		in.close();
	}

	/**
	 * Starts the protocol of a Patient.
	 * 
	 * @param patient The Patient.
	 */
	public void admit(Patient patient) {
		// HECHO
		patient.start(); //Ejecuta el metodo run() del patient: realiza su protocolo completo.
	}

	/**
	 * Waits until the Patient has finished their protocol.
	 * 
	 * @param patient The Patient.
	 */
	public void waitForDischarge(Patient patient) {
		// HECHO
		try {
			// Espero a que termine el thread del paciente: cuando acabe el run().
			patient.join(); 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
package yatospace.user.session.test;

import java.util.Scanner;

import yatospace.user.session.util.SessionsGarbageCollector;

/**
 * Тестови који се односе на скупљач сесија без регистрованог корисника. 
 * @author MV
 * @version 1.0
 */
public class SessionGarbagadgeCollectorTest {
	private static SessionsGarbageCollector collector = new SessionsGarbageCollector(); 
	private static Scanner scanner = new Scanner(System.in); 
	private static int counter = 0; 
	
	public static void menu() {
		System.out.println("0. Izlaz");
		System.out.println("1. Dodavanje i izvrsavanje");
		System.out.println();
	}
	
	public static void add() {
		counter++; collector.push(()->System.out.println("Izvrsavanje ispisa : "+counter));
		System.out.println("Dodana funkcija ispisa broja "+counter+"."); 
	}
	
	
	public static void main(String ... args) throws Exception{
		System.out.println("Dobrodosli."); 
		collector.start(); 
		while(true) {
			int izbor = -1; 
			menu();
			System.out.print("Izbor : "); 
			try{izbor = Integer.parseInt(scanner.nextLine()); }catch(Exception ex) {}
			if(izbor==0) break;
			switch(izbor) {
				case 1:
					System.out.println("DODAVANJE");
					add(); 
					break;
				default: 
					System.out.println("POGRESAN IZBOR.");
					break; 
			}
		}
		collector.close();
		System.out.println("Dovidjenja."); 
	}
}

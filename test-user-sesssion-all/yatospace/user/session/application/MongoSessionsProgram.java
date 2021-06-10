package yatospace.user.session.application;

import java.util.List;
import java.util.Scanner;

import yatospace.user.session.controller.MongoRegisteredSessionController;
import yatospace.user.session.model.Session;

/**
 * Тетирање сесеија. 
 * @author MV
 * @version 1.0
 */
public class MongoSessionsProgram {
	private static MongoRegisteredSessionController sessionController = GeneralUserSessionCenter.userSessionEngine.getSessionController();
	private static Scanner scanner = new Scanner(System.in); 
	
	public static void menu() {
		System.out.println("0. Izlaz"); 
		System.out.println("1. Listanje sesija");
		System.out.println("2. Dodavanje sesije"); 
		System.out.println("3. Zatvaranje sesije");
		System.out.println("4. Zatvaranje sesija");
		System.out.println("5. Pregled sesije");
		System.out.println("6. Prebrojavanje"); 
		System.out.println("7. Prebrojavanje korisnika");
		System.out.println("8. Listanje korisnika");
		System.out.println("9. Prebrajanje sesija korisnika");
		System.out.println("10. Listanje sesija korisnika"); 
	}
	
	public static void main(String ... args) {
		System.out.println("Dorodosli.");
		while(true) {
			int izbor = -1; menu(); 
			System.out.print("Izbor : ");
			try{izbor = Integer.parseInt(scanner.nextLine()); }catch(Exception ex) {}
			if(izbor==0) break;
			switch(izbor) {
				case 1:
					System.out.println("LISTANJE SESIJA");
					System.out.println(); 
					list(); 
					break;
				case 2:
					System.out.println("DODAVANJE SESIJE");
					System.out.println(); 
					login(); 
					break;
				case 3:
					System.out.println("ZATVARANJE SESIJE");
					System.out.println(); 
					logout(); 
					break;
				case 4:
					System.out.println("ZATVARANJE SESIJA");
					System.out.println();
					logoutAll(); 
					break;
				case 5:
					System.out.println("PREGLED SESIJE");
					System.out.println(); 
					preview();
					break;
				case 6:
					System.out.println("PREBROJAVANJE SESIJA");
					System.out.println(); 
					count();
					break;
				case 7: 
					System.out.println("PREBROJAVANJE PRIJAVLJENIH KORISNIKA");
					System.out.println(); 
					countUsers();
					break;
				case 8: 
					System.out.println("LISTANJE PRIJAVLJENIH KORISNIKA"); 
					System.out.println();
					listUsers();
					break;
				case 9:	
					System.out.println("PREBRAJANJE KORISNICKIH SESIJA");
					System.out.println(); 
					countUsersSessions();
					break;
				case 10: 
					System.out.println("LISTANJE KORISNICKIH SESIJA"); 
					System.out.println(); 
					listUsersSessions();
					break;
				default: 
					System.out.println("POGRESAN IZBOR"); 
					System.out.println();
					break;
			}
			System.out.println(); 
		}
		try {sessionController.getRegisterEngine().getSessionGC().close();}catch(Exception ex) {}
		System.out.println("Dovidjenja."); 
	}
	
	public static void list() {
		try {
			System.out.print("Velicina stranice : ");
			int pageSize = Integer.parseInt(scanner.nextLine());
			System.out.print("Broj stranice : ");
			int pageNo   = Integer.parseInt(scanner.nextLine());
			System.out.print("Filter pocetka identifikacije sesije : "); 
			String sessionFilter = scanner.nextLine(); 
			System.out.print("Filter pocetka identifikacije korisnika : "); 
			String userFilter = scanner.nextLine(); 
			List<Session> sessions = sessionController.getMemoryForSession().list(pageNo, pageSize, sessionFilter, userFilter);
			System.out.println(); 
			for(Session s: sessions)
				System.out.println(s.getSessionId()+"\t"+s.getUserId());
		}catch(Exception ex) {
			System.out.println("Greska pri listanju korisnika.");
		}
	}
	
	public static void login() {
		System.out.print("Korisnik : ");
		String username = scanner.nextLine(); 
		System.out.print("Lozinka : ");
		String password = scanner.nextLine(); 
		Session exist = sessionController.getMemoryForSession().get(username);
		if(exist!=null) System.out.println("Sesija vec postoji."); 
		else {
			if(sessionController.login(username, password))
				System.out.println("Dodavanje sesije, to jest logovanje je uspjelo."); 
			else
				System.out.println("Dodavanje sesije, nije uspjelo."); 
		}
	}
	
	public static void logout() {
		System.out.print("Sesija : ");
		String session = scanner.nextLine();
		Session exist = sessionController.getMemoryForSession().get(session);
		if(exist==null) System.out.println("Sesija ne postoji."); 
		else {
			sessionController.getMemoryForSession().logout(session);
			System.out.println("Brisanje sesije je izvrseno."); 
		}
	}
	
	public static void logoutAll() {
		try {
			System.out.print("Korisnik : ");
			String username = scanner.nextLine();
			for(Session session: sessionController.getMemoryForSession().getFor(username)) {
				sessionController.getMemoryForSession().logout(session.getSessionId());
				System.out.println("Odjavljen je korisnik na sesiji "+session.getSessionId()); 
			}
		}catch(Exception ex) {
			System.out.println("Greska pri odjavljivanju korisnika"); 
		}
	}
	
	public static void preview() {
		System.out.print("Sesija : ");
		String session = scanner.nextLine();
		Session s = sessionController.getMemoryForSession().get(session);
		if(s == null) {System.out.println("Sesija ne postoji."); return;}
		System.out.println("Korisnik : "+ s.getUserId());
		System.out.println("Sesija : "+s.getSessionId());
	}
	
	public static void count() {
		System.out.println("Trenutni broj sesija: "+sessionController.getMemoryForSession().count()); 
	}
	
	public static void countUsers() {
		System.out.println("Trenutni broj korisnika : "+sessionController.getMemoryForSession().countUsers()); 
	}
	
	public static void listUsers() {
		try {
			System.out.print("Velicina stranice : ");
			int pageSize = Integer.parseInt(scanner.nextLine());
			System.out.print("Broj stranice : ");
			int pageNo   = Integer.parseInt(scanner.nextLine());
			System.out.print("Startni filter : ");
			String startFilter = scanner.nextLine();
			List<String> users = sessionController.getMemoryForSession().listUsers(pageNo, pageSize, startFilter); 
			System.out.println();
			for(String u: users)
				System.out.println(u);
		}catch(Exception ex) {
			System.out.println("Greska pri listanju korisnika.");
		}
	}
	
	public static void countUsersSessions() {
		System.out.print("Unesi korisnicko ime : ");
		String username = scanner.nextLine(); 
		System.out.println("Trenutni broj sesija za korisnika je : "+sessionController.getMemoryForSession().countFor(username)); 
	}
	
	public static void listUsersSessions() {
		try {
			System.out.print("Korisnik : ");
			String username = scanner.nextLine();
			System.out.print("Velicina stranice : ");
			int pageSize = Integer.parseInt(scanner.nextLine()); 
			System.out.print("Broj stranice : ");
			int pageNo   = Integer.parseInt(scanner.nextLine());
			System.out.println("Startni filter : ");
			String startFilter = scanner.nextLine();
			List<Session> sessions = sessionController.getMemoryForSession().getFor(username, pageNo, pageSize, startFilter); 
			for(Session s: sessions)
				System.out.println(s.getSessionId()+"\t"+s.getUserId());
		}catch(Exception ex) {
			System.out.println("Greska pri listanju sesija."); 
		}
	}
}

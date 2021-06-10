package yatospace.user.session.application;

/**
 * Општи центар за управљање корисничким сесијама.
 * @author MV
 * @version 1.0
 */
public final class GeneralUserSessionCenter {
	private GeneralUserSessionCenter() {}
	public static final GeneralUserSessionControlPoint userSessionEngine = new GeneralUserSessionControlPoint(); 
}

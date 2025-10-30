package co.edu.uco.ucochallenge.application;

public final class Void extends Response <Object>{

	protected Void() {
        super(false, sanitizeDto(null, Object::new));
}

	public static Void returnVoid() {
        return new Void();
}
}

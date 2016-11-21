package anime.controller;

public class InvalidInputException extends Exception{
	
	private static final long serialVersionVID = -5633915762703837868L;

	public InvalidInputException(String errorMessage){
		super(errorMessage);
	}
}

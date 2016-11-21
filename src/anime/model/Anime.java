package anime.model;

import java.net.*;
import java.util.*;

public class Anime {
	private String name;
	private int id;
	private double score;
	private int numEp;
	private boolean finishedAiring;
	private URL image;
	private String[] keyWords;
	private String synopsis;
	
	public Anime(String name, int id, double score, int numEp, boolean finishedAiring, URL image, String[] keyWords, String synopsis){
		this.name = name;
		this.id = id;
		this.score = score;
		this.numEp = numEp;
		this.finishedAiring = finishedAiring;
		this.image = image;
		this.keyWords = keyWords;
		this.synopsis = synopsis;
	}
	
	
	public String getName(){
		return this.name;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String[] getKeyWords(){
		return this.keyWords;
	}
	
	public double getRating(){
		return this.score;
	}
	
	public URL getImage(){
		return this.image;
	}
	
	public String getSynopsis(){
		return this.synopsis;
	}
	
	public void setSynopsis(String s){
		this.synopsis = s;
	}
}



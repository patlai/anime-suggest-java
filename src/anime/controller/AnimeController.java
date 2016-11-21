package anime.controller;

import java.net.*;
import java.io.*;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import anime.model.Anime;
import anime.model.AnimeListManager;
import anime.persistence.PersistenceXStream;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.util.InvalidFormatException;

public class AnimeController {

	public AnimeController(){}

	public void createAnime(String name, int id, double score, int numEp, boolean finishedAiring, 
			URL image, String[] keyWords, String synopsis) throws InvalidInputException{
		
		if(name == null || name.trim().length() == 0 || name == "" || name == " ")
			throw new InvalidInputException("Anime name cannot be empty...");

		AnimeListManager aml = AnimeListManager.getInstance();
		if(doesExist(id)){
			throw new InvalidInputException("This anime is already added");
		}else{
			Anime a = new Anime(name, id, score, numEp, finishedAiring, image, keyWords, synopsis);
			aml.addAnime(a);
			PersistenceXStream.saveToXMLwithXStream(aml);
		}

	}
	public boolean doesExist(int id){
		AnimeListManager aml = AnimeListManager.getInstance();
		int size = aml.animeListSize();
		for(int i = 0;i<size;i++){
			if(aml.getAnime(i).getId() == id){
				return true;
			}else
				continue;

		}
		return false;
	}
}

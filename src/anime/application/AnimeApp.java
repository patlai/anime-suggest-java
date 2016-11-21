package anime.application;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

//import java.io.IOException;

import anime.persistence.PersistenceAnimeData;
import anime.view.AnimeView;

public class AnimeApp {
	
	public static void main(String[] args){
		//load model
		PersistenceAnimeData.loadAnime();
		
		//Start UI
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run(){
				
					new AnimeView().setVisible(true);
				
			}			
		});
	}
	
}

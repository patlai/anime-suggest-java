package anime.persistence;

import java.util.Iterator;

import anime.model.Anime;
import anime.model.AnimeListManager;

public class PersistenceAnimeData {
	
	private static void initializeXStream(){
		PersistenceXStream.setFilename("Anime_List.xml");
		PersistenceXStream.setAlias("Anime",Anime.class);
		PersistenceXStream.setAlias("List_Manager",AnimeListManager.class);
	}
	
	public static void loadAnime(){
		AnimeListManager aml = AnimeListManager.getInstance();	
		PersistenceAnimeData.initializeXStream();
		AnimeListManager aml2 = (AnimeListManager) PersistenceXStream.loadFromXMLwithXStream();
		if(aml2 != null){
			Iterator<Anime> aIt = aml2.getList().iterator();
			while(aIt.hasNext())
				aml.addAnime(aIt.next());	
		}
	}
}

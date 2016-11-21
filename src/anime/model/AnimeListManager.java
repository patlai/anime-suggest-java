package anime.model;
import java.util.*;


public class AnimeListManager{

  private static AnimeListManager theInstance = null;
  private List<Anime> animeList;
  private List<Anime> suggestionList;

  //constructor
  private AnimeListManager(){

    animeList = new ArrayList<Anime>();
    suggestionList = new ArrayList<Anime>();
    
  }

  //get Instace
  public static AnimeListManager getInstance()
  {
    if(theInstance == null)
    {
      theInstance = new AnimeListManager();
    }
    return theInstance;
  }

  //Get Anime
  public Anime getAnime(int index){
    Anime a1 = animeList.get(index);
    return a1;
  }

  //Get anime List
  public List<Anime> getList(){
    List<Anime> myList = Collections.unmodifiableList(animeList);
    return myList;
  }
  
  public List<Anime> getSuggestionList(){
	    List<Anime> myList = Collections.unmodifiableList(suggestionList);
	    return myList;
	  }
  
  

  //Get the size of the list
  public int animeListSize(){
    int number = animeList.size();
    return number;
  }

  //Check is empty
  public boolean isEmpty(){
    boolean empty = animeList.size() == 0;
    return empty;
  }

  //Get index of anime
  public int indexOfAnime(Anime a2){
    int index = animeList.indexOf(a2);
    return index;
  }

  public int indexOfName(String a){
	  for(int i =0;i< animeList.size();i++){
		  if(a==animeList.get(i).getName()){
			  return i;
		  }
	  }
	  return -1;
  }
  
  public int indexOfSuggestedAnime(Anime a2){
	    int index = suggestionList.indexOf(a2);
	    return index;
	  }
  
  public int indexOfSuggestedName(String a){
	  for(int i =0;i< suggestionList.size();i++){
		  if(a==suggestionList.get(i).getName()){
			  return i;
		  }
	  }
	  return -1;
  }

  public boolean addAnime(Anime a1){
    boolean added = false;
    if (animeList.contains(a1)) { return false; }
    animeList.add(a1);
    added = true;
    return added;
  }
  
  public boolean addSuggestion(Anime a1){
	    boolean added = false;
	    if (suggestionList.contains(a1)) { return false; }
	    suggestionList.add(a1);
	    added = true;
	    return added;
	  }

  public boolean removeAnime(Anime a1){
    boolean removed = false;
    if (animeList.contains(a1)){
      animeList.remove(a1);
      removed = true;
    }
    return removed;
  }

  public boolean addAnimeAt(Anime a1, int index){  
    boolean added = false;
    if(addAnime(a1)){
      if(index < 0 ) { index = 0; }
      if(index > animeListSize()) { index = animeListSize() - 1; }
      animeList.remove(a1);
      animeList.add(index, a1);
      added = true;
    }
    return added;
  }

  
//  public boolean addOrMoveAnimeAt(Anime a2, int index)
//  {
//    boolean added = false;
//    if(animeList.contains(a2))
//    {
//      if(index < 0 ) { index = 0; }
//      if(index > animeListSize()) { index = animeListSize() - 1; }
//      animeList.remove(a2);
//      animeList.add(index, a2);
//      added = true;
//    } 
//    else 
//    {
//      added = addAnimeAt(a2, index);
//    }
//    return added;
//  }


//  public void delete() {
//    animeList.clear();
//  }

}
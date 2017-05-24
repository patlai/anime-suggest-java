package anime.view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;
import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ch.lambdaj.*;
import static ch.lambdaj.Lambda.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.util.InvalidFormatException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import anime.controller.AnimeController;
import anime.controller.InvalidInputException;
import anime.model.Anime;
import anime.model.AnimeListManager;

import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;
import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ch.lambdaj.*;
import static ch.lambdaj.Lambda.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.util.InvalidFormatException;

public class AnimeView extends JFrame {

	private static final long serialVersionUID = -8062635784771606869L;
	
	//UI elements
	private JLabel errorMessage;
	private JButton refresh;

	private Image backgroundImage;
	private JLabel Animepic;

	private JButton AddAnimeButton;
	private JTextField AnimeNameTextField;
	private JLabel AnimeNameLabel;
	private JButton SuggestButton;
	private JList<String> list;
	private DefaultListModel<String> temp;
	private JLabel SelectedA;
	private JLabel SelectedS;
	private JList<String> suggestionList;
	private JScrollPane scrollList;
	private DefaultListModel<String> temp2;
	private JScrollPane scrollList2;
	private JLabel AddAnimeTitle;
	private JLabel SuggestedTitle;
	private JLabel score;
	private JLabel synopsis;
	
	
	public  DocumentBuilder builder;
	public  Parser parser;
	List<Anime> suggestedAnimes = new ArrayList<Anime>();
	
	
	
	//data elements
	private String error = null;
	private HashMap<Integer, Anime> animeHash;
	private HashMap<Integer, Anime> animeHash2;
	//private BufferedImage image;
	private BufferedImage myPicture=null;
	
	/** Creates new form AnimeView */
	public AnimeView(){
		initComponents();
		refreshData();
	}
	
	public  void initParser() throws InvalidFormatException, IOException, ParserConfigurationException{
		//NATURAL LANGUAGE PART
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
		InputStream iss = new FileInputStream("en-parser-chunking.bin");
		ParserModel model = new ParserModel(iss);
		parser = ParserFactory.create(model);
		iss.close();
	}
	
	/** This method is called from within the constructor to initialize the form. */
	private void initComponents(){
		try {
			initParser();
		} catch (IOException | ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//elements for error message
		errorMessage = new JLabel();
		errorMessage.setForeground(Color.RED);		
		refresh = new JButton();
		refresh.setText("Refresh");
		refresh.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt){
				error = null;
				refreshData();
			}
		});
				
		Authenticator.setDefault (new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication ("plai", "xxxcodeslayer420xxx".toCharArray());
			}
		});
		
		//selected anime is the jlabel for the thing currently selected
		SelectedA = new JLabel();
		
		//init jlabels for fields of anime
		score = new JLabel();
		synopsis = new JLabel();
		//button used to add new animes
		AddAnimeButton = new JButton();
		//textbox used to enter anime name to add
		AnimeNameTextField = new JTextField();
		AnimeNameLabel = new JLabel("Enter an Anime:", SwingConstants.RIGHT);
		AddAnimeTitle = new JLabel("Added Animes", SwingConstants.CENTER);
		SuggestedTitle = new JLabel("Suggested Animes", SwingConstants.CENTER);
		AddAnimeButton.setText("add Anime");

		AddAnimeButton.setSize(500, 500);
		AddAnimeButton.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt){
				try {
					addAnimeButtonActionPerformed(evt);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		//Get current directory path
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		s = s.replace("\\", "\\\\"); 
//		System.out.println(s);
		try {
			myPicture = ImageIO.read(new File(s+File.separator+"dbz.jpg")); //append image to current directory path
		} catch (IOException e) {
			e.printStackTrace();
		}
		Animepic = new JLabel(new ImageIcon(myPicture));

		
		//ANIME MANAGER
		AnimeListManager aml = AnimeListManager.getInstance();
		
		temp2 = convertList(aml.getSuggestionList());	
		suggestionList = new JList<String>(temp2);	
		suggestionList.setSelectedIndex(1);	
		scrollList2 = new JScrollPane(suggestionList);
		
		SuggestButton = new JButton();
		SuggestButton.setText("Suggest Animes");
		SuggestButton.setHorizontalAlignment(SwingConstants.RIGHT);
		SuggestButton.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt){
					suggestButtonActionPerformed(evt);
			}
		});

		
		temp = convertList(aml.getList());
		list = new JList<String>(temp);
		list.setSelectedIndex(1);		
		scrollList = new JScrollPane(list);
		//ListSelectionListener triggers an event when the user selects a different item in a list. In this case
		//it displays the images and synopsis of whatever anime is selected in the window on the left
		ListSelectionListener listSelectionListener = new ListSelectionListener() {
			 public void valueChanged(ListSelectionEvent listSelectionEvent) {
			        boolean adjust = listSelectionEvent.getValueIsAdjusting();
			        if (!adjust) {
			            JList selectedList = (JList) listSelectionEvent.getSource();
			            int selections[] = selectedList.getSelectedIndices();
			            List selectionValues = selectedList.getSelectedValuesList();
			            for (int i = 0, n = selections.length; i < n; i++) {
			             
			              SelectedA.setText((String) selectionValues.get(i));
			              if((String) selectionValues.get(i)!= null){
			            	  try {
				            	  	int index = aml.indexOfName((String) selectionValues.get(i));
				            	  	Anime a = aml.getList().get(index);
			            		  	String path = a.getImage().toString(); 
				                    URL url = new URL(path);
				                    myPicture = ImageIO.read(url);	
				                    Image resizedImage = 
				                    	    myPicture.getScaledInstance(Animepic.getWidth(), Animepic.getHeight(), Image.SCALE_SMOOTH);
				                    Animepic.setIcon(new ImageIcon(resizedImage));
				                    score.setText("Rating: "+Double.toString(a.getRating()));
				                    synopsis.setText("<html>Synopsis: <br>" + a.getSynopsis() + "</html>");
				              } catch(Exception e) {
				                    e.printStackTrace();
				              }
			              } 
			            }
			        }
			 }
	    };
	    list.addListSelectionListener(listSelectionListener);
	    
	    //listener for suggestionList
	    ListSelectionListener listSelectionListenerSuggested = new ListSelectionListener() {
			 public void valueChanged(ListSelectionEvent listSelectionEvent) {
			        boolean adjust = listSelectionEvent.getValueIsAdjusting();
			        if (!adjust) {
			            JList selectedList = (JList) listSelectionEvent.getSource();
			            int selections[] = selectedList.getSelectedIndices();
			            List selectionValues = selectedList.getSelectedValuesList();
			            for (int i = 0, n = selections.length; i < n; i++) {
			              
			              SelectedA.setText((String) selectionValues.get(i));
			              if((String) selectionValues.get(i)!= null){
			            	  try {
				            	  	int index = aml.indexOfSuggestedName((String) selectionValues.get(i));
				            	  	Anime a = aml.getSuggestionList().get(index);
			            		  	String path = a.getImage().toString(); 
				                    URL url = new URL(path);
				                    myPicture = ImageIO.read(url);	
				                    Image resizedImage = 
				                    	    myPicture.getScaledInstance(Animepic.getWidth(), Animepic.getHeight(), Image.SCALE_SMOOTH);
				                    Animepic.setIcon(new ImageIcon(resizedImage));
				                    score.setText("Rating: "+Double.toString(a.getRating()));
				                    synopsis.setText("<html>Synopsis: <br>" + a.getSynopsis() + "</html>");
				              } catch(Exception e) {
				                    e.printStackTrace();
				              }
			              } 
			            }
//			            System.out.println();
			        }
			 }
	    };
	    suggestionList.addListSelectionListener(listSelectionListenerSuggested);
	    


	    
	    //Clear Layout
	    getContentPane().removeAll();
	    
	    //Add Background Color to contentPane
	    JPanel contentPane = new JPanel();
	    contentPane.setBackground(new Color(40, 255, 108));
	    setContentPane(contentPane);
	    
		// layout
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup( //Same Vertical line
				layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup()
							.addComponent(AnimeNameLabel)
							.addComponent(AddAnimeButton)
							.addComponent(Animepic)
							.addComponent(SelectedA)
							.addComponent(AnimeNameTextField))
						.addGroup(layout.createParallelGroup()
								.addComponent(errorMessage)
								.addComponent(score)
								.addComponent(synopsis))
						.addGroup(layout.createParallelGroup()
								.addComponent(AddAnimeTitle)
								.addComponent(scrollList))
						.addGroup(layout.createParallelGroup()
								.addComponent(SuggestedTitle)
								.addComponent(scrollList2)
								.addComponent(SuggestButton)
								.addComponent(refresh))
						.addGroup(layout.createParallelGroup()))
						);
		
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[]{AddAnimeButton, AnimeNameLabel});

		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[]{AnimeNameTextField, scrollList, SelectedA, scrollList2, score});

		
		layout.setVerticalGroup( //Same Horizontal line
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(AddAnimeTitle)
						.addComponent(SuggestedTitle)
						.addComponent(SelectedA)
						.addComponent(score))
				.addGroup(layout.createParallelGroup()
						.addComponent(Animepic)
						.addComponent(scrollList)
						.addComponent(scrollList2)
						.addComponent(synopsis))
						.addComponent(AnimeNameLabel)
				.addGroup(layout.createParallelGroup()
						.addComponent(AnimeNameTextField, 20,20,20)
						.addComponent(SuggestButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(AddAnimeButton)
						.addComponent(errorMessage)
						.addComponent(refresh))
				.addGroup(layout.createParallelGroup())
				.addGroup(layout.createParallelGroup())
				.addGroup(layout.createParallelGroup())
				);
		
		pack();
	}
	
	private void refreshData(){
		AnimeListManager aml = AnimeListManager.getInstance();
		//error
		errorMessage.setText(error);
		if(error == null || error.length() == 0){
			//Anime List
			AnimeNameTextField.setText("");
			animeHash = new HashMap<Integer, Anime>();
			animeHash2 = new HashMap<Integer, Anime>();
			list.removeAll();
			suggestionList.removeAll();
			temp.removeAllElements();
			temp2.removeAllElements();
			Iterator<Anime> aIt = aml.getList().iterator();
			Integer i = 0;
			while(aIt.hasNext()){
				Anime a1 = aIt.next();
				animeHash.put(i, a1);
				temp.addElement(a1.getName());
				i++;
			}
			Iterator<Anime> aIs = aml.getSuggestionList().iterator();
			i=0;
			while(aIs.hasNext()){
				Anime a2 = aIs.next();
				animeHash2.put(i, a2);
				temp2.addElement(a2.getName());
				i++;
			}
			System.out.println("-----" + temp2.size());
			
			suggestionList = new JList<String>(temp2); 	//Update suggestion animes
			suggestionList.setSelectedIndex(1);
			
			list = new JList<String>(temp);				//Update added animes
			list.setSelectedIndex(1); 

		}
		//For when error messages appear
		pack();
	}
	
	
	/**
	 * @param evt
	 * @throws IOException
	 * Algorithm for adding an anime:
	 * Search MAL API for string that user entered and return the first result in the XML file (buggy parsing or bad xml file might lead
	 * to the first result being omitted in some cases).
	 * Use the XML parser and get fields for an anime object from the nodes and children it returns.
	 * use the openNLP library to parse the synopsis for nouns and add them to the list of keyWords for the anime
	 * Using all this data, init a new Anime object and add it to list.
	 */
	private void addAnimeButtonActionPerformed(java.awt.event.ActionEvent evt) throws IOException{
		if(AnimeNameTextField.getText() == null || AnimeNameTextField.getText().trim().length() == 0)
			try {
				throw new InvalidInputException("Anime name cannot be empty...");
			} catch (InvalidInputException e1) {
				error = e1.getMessage();
				refreshData();
				return;
			}
		
		Authenticator.setDefault (new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication ("plai", "xxxcodeslayer420xxx".toCharArray());
			}
		});


		//initialize an empty string for the search query of an anime
		String animeSearchString = "";
		//animeSearch is the string that the user will enter
		

		//initialize a variable for the entire url of the xml file
		URL animeUrl = null;

		//split the user's string into words by spaces
		String[] splittedAnimeSearch = AnimeNameTextField.getText().split("\\s");

		//build the search query, ex: q=word1+word2+word3
		if(splittedAnimeSearch.length > 1){
			for(int i = 0; i < splittedAnimeSearch.length; i++){
				if(i < splittedAnimeSearch.length - 1)
					animeSearchString += splittedAnimeSearch[i] + "+";
				else
					animeSearchString += splittedAnimeSearch[i];
			}
			animeUrl = new URL("https://myanimelist.net/api/anime/search.xml?q=" + animeSearchString);
		} else {
			animeUrl = new URL("https://myanimelist.net/api/anime/search.xml?q=" + AnimeNameTextField.getText());
		}


		BufferedReader in = new BufferedReader(
				new InputStreamReader(animeUrl.openStream()));
		
		//create the document builder factory method
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();


		try {
			// use the factory to create a documentbuilder
			DocumentBuilder builder = factory.newDocumentBuilder();

			// create a new document from input source
			InputSource is = new InputSource(in);
			Document doc = builder.parse(is);

			// get the first element
			Element element = doc.getDocumentElement();

			// get all child nodes
			NodeList nodes = element.getChildNodes();
			//xml item indices: 1 = id	 5=name		7=alt name		11 = rating		13 = type (movie/tv/ova)	15=current of finished
			//19 = end date		21 = synopsis		23=imageURL



			// print the text content of each child
			for (int i = 0; i < nodes.getLength(); i++) {
				NodeList children = nodes.item(i).getChildNodes();
				try{
					String animeName = children.item(5).getTextContent();
					int animeID = Integer.parseInt(children.item(1).getTextContent());
					int numberOfEpisodes = Integer.parseInt(children.item(9).getTextContent());
					double rating = Double.parseDouble(children.item(11).getTextContent());
					boolean finishedAiring=false;
					String imageURL = children.item(23).getTextContent();
					String synopsis = children.item(21).getTextContent();
					if(children.item(15).getTextContent().charAt(0) == 'F')
						finishedAiring=true;
					if(!animeName.isEmpty()){
						//call anime controller
						AnimeController aC = new AnimeController();
						
						
						String[] synopsisSplit = synopsis.split("\\s");
						ArrayList<String> keyWords = new ArrayList<String>();
						for(int j=0; j<synopsisSplit.length; j++){
							Parse topParses[] = null;
							try{
								topParses = ParserTool.parseLine(synopsisSplit[j], parser, 1);
							}
							catch(StringIndexOutOfBoundsException ee){
								continue;
							}
							for (Parse p : topParses)
							{	
								//if the word is a noun, add it to the list of keywords
								if(p.getChildren()[0].getType().equals("NN")){
									//replace any weird characters left by xml parsing
									String word  = p.getChildren()[0].getText()
											.replace("&quot;","").replace(".<br","").replace(",","");

									keyWords.add(word);

								}
							}
						}
						
						
						//public void createAnime(String name, int id, double score, int numEp, boolean finishedAiring, URL image) 

						aC.createAnime(animeName, animeID, rating, numberOfEpisodes, finishedAiring, new URL(imageURL), keyWords.toArray(
								new String[keyWords.size()]), synopsis);
						
						System.out.println("Title: " + animeName);
						System.out.println("ID: " + animeID);
						System.out.println("Rating: " + rating);
						System.out.println("Finished Airing: " + finishedAiring);
						System.out.println(children.item(23).getTextContent());
						refreshData();
						break;
					}
				} catch(InvalidInputException e){
					error = e.getMessage();	
				} catch(NullPointerException e){

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		in.close();

		
		//update visuals
		refreshData();	
	}
	
	/**
	 * @param keyWord
	 * @return
	 * @throws Exception
	 * suggest an anime based on a keyWord (comes from the keyWords of animes in the user's input list)
	 * algorithm is the same as adding an anime except now the MAL API is searched for using a word rather
	 * than a specific name. If the first result from that keyWord doesn't exist in the list of suggested animes
	 * or the input list, add it to the suggestion list.
	 */
	private Anime suggest(String keyWord) throws Exception {
		Authenticator.setDefault (new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication ("plai", "xxxcodeslayer420xxx".toCharArray());
			}
		});

		//System.out.println(keyWord);	
		URL animeUrl = new URL("https://myanimelist.net/api/anime/search.xml?q=" + keyWord);
		BufferedReader in = new BufferedReader(
				new InputStreamReader(animeUrl.openStream()));
		if(!in.ready()){
			System.out.print(keyWord + ": no stream");
			return null;
		}
		System.out.println(keyWord);	
		String inputLine;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			// use the factory to create a documentbuilder
			DocumentBuilder builder = factory.newDocumentBuilder();

			// create a new document from input source
			InputSource is = new InputSource(in);

			
			Document doc = builder.parse(is);

			// get the first element
			Element element = doc.getDocumentElement();

			// get all child nodes
			NodeList nodes = element.getChildNodes();
			//xml item indices: 1 = id	 5=name		7=alt name	 9=numEps	11 = rating		13 = type (movie/tv/ova)	15=current of finished
			//19 = end date		21 = synopsis		23=imageURL


			// print the text content of each child
			outerloop:
				for (int i = 0; i < nodes.getLength(); i++) {
					NodeList children = nodes.item(i).getChildNodes();
					try{
						String animeName = children.item(5).getTextContent();
						int animeID = Integer.parseInt(children.item(1).getTextContent());
						int numEps = Integer.parseInt(children.item(9).getTextContent());
						double rating = Double.parseDouble(children.item(11).getTextContent());
						boolean finishedAiring=false;
						String imageURL = children.item(23).getTextContent();
						String synopsis = children.item(21).getTextContent();
//						System.out.println("----" + animeName);
						if(children.item(15).getTextContent().charAt(0) == 'F')
							finishedAiring=true;
						if(!animeName.isEmpty()){
							//							
							Anime a = new Anime(animeName, animeID, rating, numEps, finishedAiring, new URL(imageURL), 
									null, synopsis);
//							System.out.println("!!!!!");
							return a;
						}
					}
					catch(NullPointerException e){
						//do nothing
					}
				}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		in.close();
		return null;
	}
	
	
	
	/**
	 * @param suggestedAnimes2
	 * Main part of software: compute all suggestions based on the input list 
	 */
	private void computeSuggestions(List<Anime> suggestedAnimes2){
		suggestedAnimes = new ArrayList<Anime>();
		//insert the keywords into a hashmap with the value being the frequency of the keywords across the input list.
		//keywords with higher frequency have their associated suggested animes put towards the top of the suggested list.
		//but since we didn't have time to implement this, the suggested animes are simply sorted by score and not relevance to the input.
		HashMap<String, Integer> allKeyWords = new HashMap<String, Integer>();
		for(int i=0; i<suggestedAnimes2.size(); i++){
			for(String keyWord: suggestedAnimes2.get(i).getKeyWords()){
				if(!allKeyWords.containsKey(keyWord)){
					allKeyWords.put(keyWord, 1);
				} else{
					allKeyWords.put(keyWord, allKeyWords.get(keyWord) + 1);
					//System.out.println(allKeyWords.get(keyWord) + 1);
				}
			}
			//sort the hashmap by value
			Object[] a = allKeyWords.entrySet().toArray();
			Arrays.sort(a, new Comparator() {
				public int compare(Object o1, Object o2) {
					return ((Map.Entry<String, Integer>) o2).getValue()
							.compareTo(((Map.Entry<String, Integer>) o1).getValue());
				}
			});
			
			//add it back to an arrayList
			ArrayList aList = new ArrayList<Object>();
			for(int g=0; g<a.length; g++){
				aList.add(a[g]);
			}
			
			//reverse the list so that the highest frequencies are at the beginning
			Collections.reverse(aList);
			a=aList.toArray(a);
			
			for (Object e : a) {
				Anime s = null;
				try{
					String word = ((Map.Entry<String, Integer>) e).getKey();
					s = suggest(word);

				} catch (Throwable q){
					//do nothing
				}
				AnimeListManager aml = AnimeListManager.getInstance();
				if(s != null && !checkIfAnimeExists(suggestedAnimes, s) && !checkIfAnimeExists(aml.getList(), s)){
					suggestedAnimes.add(s);

				}
			}
		}
		
		//display from highest to lowest rating
		//on() is using lambdaj to sort by a specified field
		suggestedAnimes = sort(suggestedAnimes, on(Anime.class).getRating());
		Collections.reverse(suggestedAnimes);
		List<Anime> temp = new ArrayList<Anime>();
		for(int i = 0;i<=10;i++){
			temp.add(suggestedAnimes.get(i));
		}
		suggestedAnimes=temp;		
	}
	
	//used to check if an anime of the same id exists in a list
	private boolean checkIfAnimeExists(List<Anime> aList, Anime a){
		for(int i=0; i<aList.size(); i++){
			if(a.getId() == aList.get(i).getId())
				return true;
		}
		return false;
	}
	
	private void suggestButtonActionPerformed(java.awt.event.ActionEvent evt){
		AnimeListManager aml = AnimeListManager.getInstance();
		computeSuggestions(aml.getList());
		System.out.println("===" + suggestedAnimes.size());
		temp2 = convertList(suggestedAnimes);
		System.out.println("===" + temp2.size());

		suggestionList = new JList<String>(temp2);
		
		scrollList2 = new JScrollPane(suggestionList);
		for(int i=0; i<suggestedAnimes.size(); i++){
			aml.addSuggestion(suggestedAnimes.get(i));
		}
		
		//update visuals
		refreshData();
		initComponents();
		refreshData();
	}
	
	private DefaultListModel<String> convertList(List<Anime> list){
		int size = list.size();
		DefaultListModel<String> model = new DefaultListModel<String>();
		for(int i = 0; i<size;i++){
			model.addElement(list.get(i).getName());
		}
		return model;
	}
}

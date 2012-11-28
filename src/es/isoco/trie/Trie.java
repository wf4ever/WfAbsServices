package es.isoco.trie;

import org.w3c.dom.*;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

import javax.xml.parsers.*;

import es.isoco.trie.io.Constants;
import es.isoco.trie.io.GenericMethods;

/* The different types for trie traversals can be found at 
 * http://en.wikipedia.org/wiki/Tree_traversal
 */

public class Trie {

	public static float[][] freqByActLevel = new float[Constants.numActions][Constants.numLevels]; 
	public static float[][] percByActLevel = new float[Constants.numActions][Constants.numLevels]; 
	public static float[]   freqByLevel = new float[Constants.numLevels];
	
	//protected static ArrayList freqByLevel;
	private TNode root;
	private int depth;
	private static Hashtable<String, Integer> table;
   
	 public Trie(Hashtable<String, Integer> t){
	  root = new TNode(" ");
	  root.freq = 0;
	  root.URIs = new LinkedList<String>();
	  depth = 0;	  
	  table = t;
	 }
	 	 
	 public void insert(LinkedList<String> sList, String URI){
	  
	  String s = new String("");
	  String uri = new String("");
	  int sSize = sList.size();	  
	  TNode current = root;
	  int level = 0;
	  	  
	  if(sSize==0){ //For an empty list make it root		  
		  current.marker = true;
	  	  root.URIs = new LinkedList<String>();
	  }
	  if(sSize > depth)
		  depth = sSize;
		
	  for(int i=0;i<sSize;i++){
	   level++;
	   if (level == 1) {
		   root.freq++; root.updateProb();
	   }
	   
	   s = sList.get(i);
	   uri += s;

	   TNode child = current.subNode(s);
	   if(child != null){  //There is a child with that content
		   child.freq++;
		   current.updateProb();
		   child.sequence.concat(uri); 
		   child.URIs.add(URI);
		   current = child;
	   }
	   else{
		TNode newNode = new TNode(s);
		newNode.level = level;
		newNode.freq++;
		newNode.prob = 1;      //There is only 1 child
		newNode.leaf = true;   //Change if the search has to be over the complete 
							   // set of actions

	    current.child.add(newNode);
	    newNode.sequence = uri;
	    newNode.URIs = new LinkedList<String>();
	    newNode.URIs.add(URI);
	    current.updateProb();
	    current = current.subNode(s);	   
	   }
	   	   
	   // Set marker to indicate end of the word
	   if(i==s.length()-1)
	    current.marker = true;
	  }	  
	 }
	 
	 
	 public TNode search(LinkedList<String> sList){
	  TNode current = root;
	  
	  while(current != null){
	   for(int i=0;i<sList.size();i++){		
	    if(current.subNode(sList.get(i)) == null)
	     return null;
	    else
	     current = current.subNode(sList.get(i));
	   }
	   /*
	    * This means that a string exists, but make sure its
	    * a word by checking its 'marker' flag
	    */
	   
	   if (current.leaf == true)
	    return current;
	   else
	    return null;
	  }
	  return null;
	 }	 

	 public void updateEventProb(TNode n)
	 {
		 Collection<TNode> childs = n.getChilds();	
		 float[] totalLevel = GenericMethods.sumFreqLevels();
		 
		 if (n.level > 0)
		 {
		     n.prob = n.getFreq() / totalLevel[n.level-1];
		 }	     
		 
		 Iterator<TNode> iteNode = childs.iterator();
		 while (iteNode.hasNext())
		 {
			 updateEventProb(iteNode.next());
		 }
}
	 	 
	 public TNode nexTNode (TNode target)
	 {
		 Collection<TNode> childs = target.getChilds();
		 int maxFreq = 0;
		 		 
		 TNode current = null, nextN = null;
		 
		 Iterator<TNode> iteNode = childs.iterator();
		 
		 while (iteNode.hasNext())
		 {
			 current = iteNode.next();
			 if (current.getFreq() > maxFreq)
			 {
				 maxFreq = current.getFreq();
				 nextN = current;
			 }
		 }
		 return nextN;
	 }
	 
	 //ByLevel representation in a DOM document 
	 public Document getDOM(LinkedList<Integer> fLevel) 
		{
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = null;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Document document = docBuilder.newDocument();
	    
		    Element root = (Element)document.createElement("TRIE"); 
		    document.appendChild(root);
		    root.setAttribute("DEPTH", "0");
		        
			 LinkedList<TNode> queue = new LinkedList<TNode>();
			 queue.add(this.root);
			 while ( !queue.isEmpty()) {
				 TNode n = queue.removeFirst();	
				 float prob =(float)n.getFreq()/(float)(fLevel.get(n.getLevel()));
			
				 Element node = (Element)document.createElement("NODE");
				 root.appendChild(node);
				 node.setAttribute("PROCESS", n.getContent());
				 node.setAttribute("LEVEL", Integer.toString(n.getLevel()));
				 node.setAttribute("PROB", Float.toString(prob));
				 node.setAttribute("FREQ", Float.toString(n.getFreq()));
				    
	
				 Collection<TNode> childs = n.getChilds();
				 Iterator<TNode> iteNode = childs.iterator();
				 
				 while (iteNode.hasNext())
				 {
					 queue.add(iteNode.next());
				 }
				 
			 }		 
		    return document;
	    }
	    	  
	   protected void getDOMPreorder(TNode n, Document document, Node nRoot, LinkedList<Integer> fLevel)
	   {		   
		   if (n.hasChilds() && n != this.root){
			    Element root = (Element)document.createElement("TRIE"); 
			    nRoot.appendChild(root);
			    root.setAttribute("DEPTH", Integer.toString(fLevel.size()-n.getLevel())); 
			    nRoot = root;
		   }
		   
		   Element node = (Element)document.createElement("NODE");
		   nRoot.appendChild(node);
		   float prob =(float)n.getFreq()/(float)(fLevel.get(n.getLevel()));
		   	
		   node.setAttribute("ID", n.getContent());
		   node.setAttribute("SEQUENCE", n.getSequence());		   
		   node.setAttribute("LEVEL", Integer.toString(n.getLevel()));
		   
		   DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
		   otherSymbols.setDecimalSeparator('.');

		   DecimalFormat df = new DecimalFormat("0.########", otherSymbols);
		   
		   node.setAttribute("PROB", df.format(prob));
		   node.setAttribute("FREQ", Float.toString(n.getFreq()));
		   		
		   LinkedList<String> URIs = n.getURI();
		   Iterator<String> ite = URIs.iterator();
		   while (ite.hasNext())
		   {			   
			String URI = ite.next();
			Element nodeURI = (Element)document.createElement("URI");
			nodeURI.setTextContent(URI);
			node.appendChild(nodeURI);
			
		   }
		   
		   Collection<TNode> childs = n.getChilds();
			  
   		   Iterator<TNode> iteNode = childs.iterator();
		   while (iteNode.hasNext())
		   {
				 getDOMPreorder(iteNode.next(), document, node, fLevel);				 
		   } 
		   			 
		 }
	   
	   
	   public TNode getRoot(){
		   return this.root;		   
	   }
	   	
	   public LinkedList<String> getURIS (TNode n){
		   return n.URIs;		   
	   }
	    
}

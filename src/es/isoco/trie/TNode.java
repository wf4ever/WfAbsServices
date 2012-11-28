package es.isoco.trie;

import java.util.Collection;
import java.util.LinkedList;

public class TNode {

	
	String content;
	String sequence;
	boolean marker;
	boolean leaf;
	Collection<TNode> child;
	float prob, pValue;
	int freq, level;
	LinkedList<String> URIs;
	
	
	public TNode (String sValue){
		  child = new LinkedList<TNode>();
		  marker = false;  //Denotes the end of a set of elements
		  leaf = true;
		  content = sValue;	
		  level = 0;
		  prob = 1;
		  freq = 0;
		  pValue = -1;
		  URIs = null;
		  sequence = null;
		 }
		  
	public void updateProb()
	{
		if(child!=null){
			for(TNode eachChild:child){
				eachChild.prob = (float)((float)eachChild.freq / (float)this.freq);
			}
		}			
	}
	
	public TNode subNode(String sValue){
	  if(child!=null){
	   for(TNode eachChild:child){
	   if(eachChild.content.equalsIgnoreCase(sValue)){
	     return eachChild;
	   }
	  }
	 }
	 return null;
	}
	
	public Collection<TNode> getChilds()
	{
		return child;
	}
	
	public boolean hasChilds(){
		if (child != null) {return true;};
		return false;
	}
	
	public int getFreq()
	{
		return freq;
	}
	
	public float getProb()
	{
		return prob;
	}
	
	public String getContent(){
		return content;
	}
	
	public void setLevel(int nLevel){
		level = nLevel;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public LinkedList<String> getURI()
	{
		return URIs;
	}
	
	public String getSequence()
	{
		return sequence;		
	}
}

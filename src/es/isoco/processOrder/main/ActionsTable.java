package es.isoco.processOrder.main;

import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import es.isoco.processOrder.rdf.VarExtractor;

public class ActionsTable {
	
	private VarExtractor extractor;
	private Hashtable<String, Integer> table;
	private int pos;
	
	public ActionsTable(){
		pos=0;
		table=new Hashtable<String, Integer>();
		extractor=new VarExtractor();
	}
	
	public Hashtable<String, Integer> getActionsTable(String xml){
		Scanner scanner = new Scanner(xml);
		String line=null;
		String name=null;
		while (scanner.hasNextLine()) {
		  line = scanner.nextLine();
		  if (extractor.checkSimpleAppereance(line, "process")){
			  name=extractor.extractParam(line,"process");
			  addAction(name);
		  }
		}
		return table;
	}
	
	public Hashtable<String, Integer> getActionsTableRSS(String xml){
		Scanner scanner = new Scanner(xml);
		String line=null;
		String name=null;
	    XPath xPath = XPathFactory.newInstance().newXPath();	   

		while (scanner.hasNextLine()) {
		  line = scanner.nextLine();
		  //System.out.println(line);
		  DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	      DocumentBuilder docBuilder;
	      Document doc = null;
		  try {
		
			docBuilder = docBuilderFactory.newDocumentBuilder();
			StringBuilder sbLine = new StringBuilder();
			
			
			sbLine.insert(0, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" );
			sbLine.append(line);
												
			Reader reader=new CharArrayReader(sbLine.toString().toCharArray());
			//doc = docBuilder.parse(new  ByteArrayInputStream(sbLine.toString().getBytes()));
			doc = docBuilder.parse(new ByteArrayInputStream(sbLine.toString().getBytes("UTF-8")));
			//System.out.println(stream);
			String expr = ("//*[@lemma]");
			NodeList nl = (NodeList) xPath.evaluate(expr, doc, XPathConstants.NODESET);
			for (int j = 0; j < nl.getLength(); j++) {
				// System.out.println(nl.item(j).getNodeName()); 
				 NamedNodeMap nodAtt = nl.item(j).getAttributes();
				 Node nLemmas = nodAtt.getNamedItem("lemma");
				 addAction(nLemmas.getNodeValue());
			}
		  }
		  
		  catch (SAXException e) {
			e.printStackTrace();			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return table;
	}
	
	public Hashtable<String, Integer> getActionsTableRSS(String xml, LinkedList<String> sL){
		Scanner scanner = new Scanner(xml);
		String line=null;
		String name=null;
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    

		while (scanner.hasNextLine()) {
		  line = scanner.nextLine();
		  //System.out.println(line);
		  DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	      DocumentBuilder docBuilder;
	      Document doc = null;
		  try {
		
			docBuilder = docBuilderFactory.newDocumentBuilder();
			StringBuilder sbLine = new StringBuilder();
			
			
			sbLine.insert(0, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" );
			sbLine.append(line);
												
			Reader reader=new CharArrayReader(sbLine.toString().toCharArray());
			//doc = docBuilder.parse(new  ByteArrayInputStream(sbLine.toString().getBytes()));
			doc = docBuilder.parse(new ByteArrayInputStream(sbLine.toString().getBytes("UTF-8")));
			//System.out.println(stream);
			String expr = ("//*[@lemma]");
			NodeList nl = (NodeList) xPath.evaluate(expr, doc, XPathConstants.NODESET);
			for (int j = 0; j < nl.getLength(); j++) {
				
				 NamedNodeMap nodAtt = nl.item(j).getAttributes();
				 Node nLemmas = nodAtt.getNamedItem("lemma");
				 addAction(nLemmas.getNodeValue());
				 sL.add(nLemmas.getNodeValue());
			}
		  }
		  
		  catch (SAXException e) {
			e.printStackTrace();			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}		
		return table;
	}
	
	private void addAction(String name) {
	     if (!table.containsKey(name)) {
	    	 table.put(name, new Integer(pos));
	 		 pos++;
	     }
	     //System.out.println(name+" "+(pos-1)+" "+table.get(name));
	}

}

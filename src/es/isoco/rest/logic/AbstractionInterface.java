package es.isoco.rest.logic;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.isoco.processOrder.main.RunAll;
import es.isoco.trie.TNode;
import es.isoco.trie.Trie;
import es.isoco.trie.io.Constants;
import es.isoco.trie.io.XMLTrieReader;
import es.isoco.trie.test.TrieTest;

public class AbstractionInterface {
	
	private double freq;
	private double prob;
	private ArrayList<String> arrayURIs;
	private String processor;

	// Node root = new Node("Inicio");
	public static Trie trieDSA;

	public void fillListUris() {	 
		freq=0;
		prob=0;
		arrayURIs=new ArrayList<String>();
		processor="";
		this.load();
	}
	
	public ArrayList<String> getSearchURIs(List<String> processes){
		search(processes);
		return arrayURIs;
	}
	
	public double getFrequency(){
		return freq;
	}
	
	public double getProbability(){
		return prob;
	}
	
	public String getProcessor(){
		return processor;
	}

	private void load() {
		boolean subChains = true;
		final String inputType = "file"; // Input stream : file or sparql for
											// SPARQL endpoint
		Document doc = null;

		XMLTrieReader tReader = new XMLTrieReader();

		if (inputType.compareToIgnoreCase("file") == 0) {
			///home/stabilizer 
			doc = tReader.read("wfprov2.xml", "file");

			String sXML = DOM2String(doc);
			Hashtable<String, Integer> table = Constants.aT
					.getActionsTable(sXML);
			trieDSA = new Trie(table);
		}

		if (inputType.compareToIgnoreCase("sparql") == 0) {
			RunAll r = new RunAll();
			doc = tReader.read(r.run(), "sparql");
		}

		doc.getDocumentElement().normalize();
		NodeList nListWFs = doc.getElementsByTagName("workflow");

		for (int temp = 0; temp < nListWFs.getLength(); temp++) {
			Node nNode = nListWFs.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eNode = (Element) nNode;
				NodeList pNodes = eNode.getElementsByTagName("process");
				LinkedList<String> sList = new LinkedList<String>();

				if (subChains == false) {
					// Inserting WFs as they are
					int maxLength = Math.min(pNodes.getLength(),
							Constants.numLevels);
					for (int i = 0; i < maxLength; i++) {
						sList.add(pNodes.item(i).getChildNodes().item(0)
								.getNodeValue());
					}
					trieDSA.insert(sList,
							nNode.getAttributes().getNamedItem("uri")
									.getNodeValue());
				} else {
					// Inserting WFs and their subchains
					for (int i = 0; i < pNodes.getLength(); i++) {
						int maxInd = Math.min(pNodes.getLength(), i
								+ Constants.numLevels);
						for (int j = i; j < maxInd; j++) {
							sList.add(pNodes.item(j).getChildNodes().item(0)
									.getNodeValue());
							trieDSA.insert(sList, nNode.getAttributes()
									.getNamedItem("uri").getNodeValue());
						}
						sList.clear();
					}
				}
			}

		}

	}

	private void search(List<String> processes){
		// Search

				LinkedList<String> sTarget = new LinkedList<String>(processes);

				//sTarget.add("Processor regex_value");
				// sTarget.add("Processor substructureSearch");
				TNode nTarget = trieDSA.search(sTarget);
				LinkedList<String> URIs = nTarget.getURI();
				arrayURIs = new ArrayList<String>(URIs);
				
				freq=nTarget.getFreq();
				processor=nTarget.getContent();
				
				// Recommendation
				//while (ite.hasNext()) {
				//	System.out.println("URI: " + ite.next());
				//}

	}
	
	public void recommend(List<String> processes){
		LinkedList<String> sTarget = new LinkedList<String>(processes);

		TNode nTarget = trieDSA.search(sTarget);
		
		freq=trieDSA.nexTNode(nTarget).getFreq();
		processor=trieDSA.nexTNode(nTarget).getContent();
		prob=trieDSA.nexTNode(nTarget).getProb();
	}
	
	/*private static void process(Document doc) {

		NodeList listOfWFs = doc.getElementsByTagName("process");
		int totalWFs = listOfWFs.getLength();
		System.out.println("Total no of WFs : " + totalWFs);

		for (int s = 0; s < listOfWFs.getLength(); s++) {

			Node WFNode = listOfWFs.item(s);
			if (WFNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) WFNode;
				System.out.println(getTagValue("workflow", eElement));
			}
		}
	}*/

	/*private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();
		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}*/

	private String DOM2String(Document doc) {
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (javax.xml.transform.TransformerConfigurationException error) {

			return null;
		}

		Source source = new DOMSource(doc);

		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		try {
			transformer.transform(source, result);
		} catch (javax.xml.transform.TransformerException error) {

			return null;
		}

		String s = writer.toString();
		return s;
	}

}

package lookup;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

/**
 * Lookup table for grounding entities that are mammal.
 * @author sloates
 *
 */
public class TabLookup extends Lookup{
	
	
	
	private static TabLookup instance=null;
	
	public static TabLookup getInstance(){
		if(instance == null) {
			instance = new TabLookup();
		}
		return instance;
	}
	
	/**
	 * Creates the human lookup
	 *  EnglishNames are standardized, by eliminating all seperators and putting everything to uppercase
	 */
	private TabLookup(){
		uniprot = new HashMap<String,String>();
		swisprot = new HashMap<String,String>();
		genename = new HashMap<String,String>();
		english = new HashMap<String,LinkedList<String>>();
		uniToGene = new HashMap<String, Set<String>>();
		File proteins = new File("uniprot-taxonomy%3A-Mammalia+%5B40674%5D-.tab");
		if (!proteins.exists()){
			proteins = new File("uniprot-taxonomy:-Mammalia+%5B40674%5D-.tab");
		}
		Scanner s;
		String uni ="";
		try {
			s = new Scanner(proteins);
			s.nextLine();
			String curr =  s.nextLine();
			while(s.hasNext()){
				String[]line = curr.split("\t");
				uni= line[0];
				String swis = line[1];
				String description = line[3];
				for(String genes: line[4].split("\\s")){
					if(swis.contains("HUMAN")){
						genename.put(genes.toUpperCase(),uni);
					}
					if(uniToGene.get(uni)== null){
						HashSet<String> g = new HashSet<String>();
						g.add(genes.toUpperCase());
						uniToGene.put(uni, g);
					}else{
						uniToGene.get(uni).add(genes.toUpperCase());
					}
				}
				uniprot.put(uni, uni);
				swisprot.put(swis, uni);
				
				//TODO decide on whether
				for(String eng: description.split("\\s\\(")){
					eng = eng.replaceAll("\\)", "");
					eng = eng.replaceAll("\\W+", " ");
					if(english.containsKey(eng.toUpperCase())){
						english.get(eng.toUpperCase()).add(uni);
					}else{
						LinkedList<String> newEng = new LinkedList<String>();
						newEng.add(uni);
						english.put(eng.toUpperCase(), newEng);
					}
				}
				curr = s.nextLine();
			}
			s.close();
		} catch (FileNotFoundException e) {
			System.err.println("cant find Mammalia file");
		}
	}
}
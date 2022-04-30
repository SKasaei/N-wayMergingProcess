/**
 * 
 */
/**
 * @author Admin
 *
 */
package nmlMatchTrace;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.net.URI;



import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.ecl.EclModule;
import org.eclipse.epsilon.ecl.IEclModule;
import org.eclipse.epsilon.ecl.dom.MatchRule;
import org.eclipse.epsilon.ecl.execute.context.IEclContext;
import org.eclipse.epsilon.ecl.launch.EclRunConfiguration;
import org.eclipse.epsilon.ecl.trace.Match;
import org.eclipse.epsilon.ecl.trace.MatchTrace;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.emc.plainxml.PlainXmlModel;
import enml.EmlModule;
import enml.IEmlModule;
import org.eclipse.epsilon.emc.uml.UmlModel;
import org.eclipse.epsilon.eml.dom.MergeRule;
import org.eclipse.epsilon.eml.execute.context.IEmlContext;
import org.eclipse.epsilon.eol.dom.Parameter;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.types.EolMap;
import org.eclipse.epsilon.etl.dom.TransformationRule;
import org.eclipse.epsilon.etl.execute.context.IEtlContext;



public class nmlMatchTrace {
	
	protected List<Object> rightObjects = new ArrayList<>();
	protected List<MatchRule> rule = new ArrayList<>();
	protected List<String> ModelsInMatch = new ArrayList<>();
	
	protected List<String> ModelsInMatchWithOutBase = new ArrayList<>();
	

	protected Object left;
	
	protected boolean matching;
	
	int ModelsMatch = 1;
	
	protected boolean userSpecified = false;
	
	protected EolMap<?, ?> info = new EolMap<>();
	
	public nmlMatchTrace() { }
	

	/*
	public nmlMatchTrace nmlMatchTrace2(IEclModule eclModule , List<nmlMatchTrace> OctopusMatchTrace) throws Exception{
		if (OctopusMatchTrace.isEmpty()) {
			for (Match match : eclModule.getContext().getMatchTrace()) {
			OctopusMatchTrace.add(this.nml_createMatchTrace(match));
			}
		}else {
			nml_createMatchTrace2(eclModule.getContext().getMatchTrace() , OctopusMatchTrace);
		}
				return this.nml_createMatchTrace2(eclModule.getContext().getMatchTrace() , OctopusMatchTrace);
			
	}
	*/
	public MatchTrace getTrueMatch( MatchTrace matches) { 
		MatchTrace TrueMatch = new MatchTrace();
		for (Match match : matches) {
			if (match.isMatching()) {
				TrueMatch.add(match);
			}
		}
		return TrueMatch;
	}
	
	public MatchTrace getFalseMatch(MatchTrace matches) { 
		MatchTrace FalseMatch = new MatchTrace();
		for (Match match : matches) {
			if (!match.isMatching()) {
				FalseMatch.add(match);
			}
		}
		return FalseMatch;
	}
	
	public nmlMatchTrace nml_createMatchTrace(Match match , String ModelName) { 
		nmlMatchTrace a = new nmlMatchTrace();
					
					
						a.left = match.getLeft();
		
						a.rightObjects.add(match.getRight());

						a.matching = match.isMatching();
						a.rule.add(match.getRule());
						a.ModelsInMatch.add(ModelName);
						
					return a;	
	}
	
	public int OctopusMatchTraceSize(List<nmlMatchTrace> NMLmatch , int i) { 
		
					return NMLmatch.get(i).rightObjects.size();	
	}
	
	public nmlMatchTrace nml_createMatchTrace2(Match match , List<nmlMatchTrace> OctopusMatchTrace , IEclContext context , String ModelName) { 
		try {
		if (match.isMatching()) {
		boolean ofTypeOnly = false;
		int count_1 = OctopusMatchTrace.size();
		
		for (int count_2 = 0 ; count_2 < count_1 ; count_2++) {
		//for (Object left : OctopusMatchTrace.get(count_2).rule.get(count_2).getLeftInstances(context, ofTypeOnly)) {
			//for (Object right : match.getRule().getRightInstances(context, ofTypeOnly)) {
				
				boolean appliesToTypes = OctopusMatchTrace.get(count_2).left.equals(match.getLeft());
				//boolean appliesToTypes4 = OctopusMatchTrace.get(0).rightObjects.get(0).equals(right);
				//System.out.println(OctopusMatchTrace.get(0).left);
				
				if (OctopusMatchTrace.get(count_2).left == match.getLeft() && appliesToTypes == true) {	
				OctopusMatchTrace.get(count_2).rightObjects.add(match.getRight());
				OctopusMatchTrace.get(count_2).rule.add(match.getRule());
				OctopusMatchTrace.get(count_2).ModelsInMatch.add(ModelName);
				OctopusMatchTrace.get(count_2).ModelsMatch++;
		return null;	

					}
			}
		}
						nmlMatchTrace a = new nmlMatchTrace();
						a.left = match.getLeft();
						a.rightObjects.add(match.getRight());
						a.matching = match.isMatching();
						a.rule.add(match.getRule());
						a.ModelsInMatch.add(ModelName);
					//	a.ModelsMatch++;
					return a;		
		}
		catch (Exception evt) {
			System.err.println("nmlMatchTrace Error");
        }
		return null;
}
	
	public Object getLeft() {
		return left;
	}
	
	public List<Object>  getRight() {
		return rightObjects;
	}
	
	public List<String>  getModelsInMatch() {
		return ModelsInMatch;
	}
	
	public List<String>  getModelsInMatchWB() {
		return ModelsInMatchWithOutBase;
	}
	
	public int  getModelsMatch() {
		return ModelsMatch;
	}
	
	public boolean isMatching() {
		return matching;
	}
	
	public MatchRule getRule() {
		return  rule.get(0);
	}
	
	public boolean contains(Object object) {
		return this.left == object || this.rightObjects == object;
	}
	
	/**
	 * Returns all the matches for a given object
	 * @param object
	 * @return
	 */
	public List<nmlMatchTrace> getNMLMatches(Object object , List<nmlMatchTrace> matches) {
		
		//new
		List<nmlMatchTrace> matcheCorrespond = null;
		//
		int count_1 = matches.size();
		for (int count_2 = 0 ; count_2 < count_1 ; count_2++) {
			int count_3 = matches.get(count_2).rightObjects.size();
			boolean appliesToTypes = matches.get(count_2).left.equals(object);
			
				for (int count_4 = 0 ; count_4 < count_3 ; count_4++) {
						if (matches.get(count_2).rightObjects.get(count_4).equals(object) || appliesToTypes == true) {	
							//new
							matcheCorrespond = Arrays.asList(matches.get(count_2));
							//
				//return matches;

								}
							}
				
				}
		//return null;
		return matcheCorrespond;
		
	}
	
	public List<nmlMatchTrace> nml_createMatchTraceWithoutBase(MatchTrace matches , List<String> modelNameInMatch ,  List<String> modelNameInMatchWB) { 
		try {
			
			List<nmlMatchTrace> nmlMatchTraceList = new ArrayList<>();
			int CheckEquals = 0;
			int matchNum=0;
			
			for(Match match :  matches ) {
				matchNum++;
				CheckEquals = 0;
				if(nmlMatchTraceList.isEmpty()) {
					nmlMatchTrace a = new nmlMatchTrace();
					a.left = match.getLeft();
					a.rightObjects.add(match.getRight());
					a.matching = match.isMatching();
					a.rule.add(match.getRule());
					a.ModelsInMatch.add(modelNameInMatch.get((matchNum*2)-2));
					a.ModelsInMatch.add(modelNameInMatch.get((matchNum*2)-1));
					
					a.ModelsInMatchWithOutBase.add(modelNameInMatchWB.get((matchNum*2)-2));
					a.ModelsInMatchWithOutBase.add(modelNameInMatchWB.get((matchNum*2)-1));
					
					a.ModelsMatch++;
					
					nmlMatchTraceList.add(a);
				}else {
					for(int i = 0; i<nmlMatchTraceList.size(); i++) {
						if(match.getLeft().equals(nmlMatchTraceList.get(i).left)) {
							
							nmlMatchTraceList.get(i).rightObjects.add(match.getRight());
							nmlMatchTraceList.get(i).ModelsMatch++;
							nmlMatchTraceList.get(i).ModelsInMatch.add(modelNameInMatch.get((matchNum*2)-2));
							nmlMatchTraceList.get(i).ModelsInMatchWithOutBase.add(modelNameInMatchWB.get((matchNum*2)-2));
							CheckEquals = 1;
							
						}else if(match.getRight().equals(nmlMatchTraceList.get(i).left)) {
							
							nmlMatchTraceList.get(i).rightObjects.add(match.getLeft());
							nmlMatchTraceList.get(i).ModelsMatch++;
							nmlMatchTraceList.get(i).ModelsInMatch.add(modelNameInMatch.get((matchNum*2)-1));
							nmlMatchTraceList.get(i).ModelsInMatchWithOutBase.add(modelNameInMatchWB.get((matchNum*2)-1));
							CheckEquals = 1;
							
						}else {
							int jj = nmlMatchTraceList.get(i).rightObjects.size();
							for(int j = 0; j<jj; j++) {
								if(match.getLeft().equals(nmlMatchTraceList.get(i).rightObjects.get(j)) ) {
									
									nmlMatchTraceList.get(i).rightObjects.add(match.getRight());
									nmlMatchTraceList.get(i).ModelsMatch++;
									nmlMatchTraceList.get(i).ModelsInMatch.add(modelNameInMatch.get((matchNum*2)-2));
									nmlMatchTraceList.get(i).ModelsInMatchWithOutBase.add(modelNameInMatchWB.get((matchNum*2)-2));
									CheckEquals = 1;
									
								}else if( match.getRight().equals(nmlMatchTraceList.get(i).rightObjects.get(j)) ) {
									
									nmlMatchTraceList.get(i).rightObjects.add(match.getLeft());
									nmlMatchTraceList.get(i).ModelsMatch++;
									nmlMatchTraceList.get(i).ModelsInMatch.add(modelNameInMatch.get((matchNum*2)-1));
									nmlMatchTraceList.get(i).ModelsInMatchWithOutBase.add(modelNameInMatchWB.get((matchNum*2)-1));
									CheckEquals = 1;
									
								}
							}
						}
					}
					
					switch (CheckEquals) {
						case 0 : {
							nmlMatchTrace a = new nmlMatchTrace();
							a.left = match.getLeft();
							a.rightObjects.add(match.getRight());
							a.matching = match.isMatching();
							a.rule.add(match.getRule());
							a.ModelsInMatch.add(modelNameInMatch.get((matchNum*2)-2));
							a.ModelsInMatch.add(modelNameInMatch.get((matchNum*2)-1));
							a.ModelsInMatchWithOutBase.add(modelNameInMatchWB.get((matchNum*2)-2));
							a.ModelsInMatchWithOutBase.add(modelNameInMatchWB.get((matchNum*2)-1));
							a.ModelsMatch++;
							
							nmlMatchTraceList.add(a);
							}
						break;

					}
					
				}
			}
			return nmlMatchTraceList;
		}
		catch (Exception evt) {
			System.err.println("nmlMatchTrace Error");
        }
		return null;
	}
	
}
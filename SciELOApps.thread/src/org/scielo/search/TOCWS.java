package org.scielo.search;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class TOCWS {
	private static final String TAG = "TOCWS";

	public String getURL(String URL, String issueId) {
		String u = "";
		
		u =	URL.replace("&amp;", "&" );
		if (issueId.length()>0){
			u = u.replace("PIDz", '"' + "S" + issueId + "z" + '"');
			u = u.replace("PID",  '"' + "S" + issueId + '"');
		}
		return u ;							
	}
	public void loadData(String text, Journal journal, ArrayList<Document> searchResultList) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				

				try {
					JSONObject rawResult = new JSONObject(text);
					JSONArray documentRoot = rawResult.getJSONArray("rows");
					
					loadResultList(documentRoot, journal, searchResultList);
					
				} catch(JSONException e){
					Log.d(TAG, "JSONException", e);				
				}
			}

	private void loadResultList(JSONArray documentRoot, Journal journal, ArrayList<Document> searchResultList){
		JSONObject resultItem ;
		Document r;
		String result;
		//String collectionCode;
		
		String _pid;
		String last = "";
	    String id = "";
	    
		//message = "docs.length: " +  new Integer(this.docs.length()).toString() + "\n" + "rows: " + this.itemsPerPage;
		for (int i=0; i<documentRoot.length(); i++){
			r = new Document();
			last = "";
			try {				
				last = last + "\n" +"item " ;
				resultItem = documentRoot.getJSONObject(i);
				id = resultItem.getString("key");
				
				try {					
					resultItem = resultItem.getJSONObject("doc");	
				} catch (JSONException e) {
					last = last + "\n" +"doc";
				}
				
				// resultItem.getJSONArray("v65").getJSONObject(0).getString("_").substring(0,3)
				// "v12":[{"l":"pt","_":"Risco do uso do eletrocaut\u00e9rio em pacientes portadores de adornos met\u00e1licos"},{"l":"en","_":"Risk of the use of electrocautery in patient with metallic ornaments"}],"v158":[{"_":"nd"}],"v35":[{"_":"0102-6720"}],"v114":[{"_":"20100723"}],"v117":[{"_":"vancouv"}],"v111":[{"_":"28/06/2010"}],"v113":[{"_":"23/07/2010"}],"v40":[{"_":"pt"}],"v85":[{"i":"1","d":"decs"},{"i":"1","k":"Eletrocirurgia","t":"m","l":"pt"},{"i":"1","k":"Eletrocoagula\u00e7\u00e3o","t":"m","l":"pt"},{"i":"1","k":"\u00c9tica m\u00e9dica","t":"m","l":"pt"},{"i":"2","d":"decs"},{"i":"2","k":"Electrosurgery","t":"m","l":"en"},{"i":"2","k":"Electrocoagulation","t":"m","l":"en"},{"i":"2","k":"Ethics, medical","t":"m","l":"en"}],"v42":[{"_":"1"}],"v83":[{"a":"INTRODU\u00c7\u00c3O: A eletrocirurgia \u00e9 tecnologia conhecida h\u00e1 longo tempo que, atualmente, tem adquirido cada vez mais destaque. Apesar disso, ainda apresenta v\u00e1rios riscos quanto \u00e0 sua utiliza\u00e7\u00e3o. V\u00e1rias les\u00f5es podem ser causadas por eletrocaut\u00e9rios, sendo as queimaduras a complica\u00e7\u00e3o mais frequente. Nem sempre existe a cooper\u00e7\u00e3o do paciente frente a medidas preventivas. M\u00c9TODO: Revis\u00e3o da literatura pertinente em fun\u00e7\u00e3o de questionamento jur\u00eddico de paciente que se negou a retirar seus ornamentos no in\u00edcio de procedimento cir\u00fargico, j\u00e1 estando ela na sala de opera\u00e7\u00f5es. CONCLUS\u00c3O: \u00c9 essencial o conhecimento dos fundamentos da eletrocirurgia, seu uso correto, equipamento seguro, monitoramento constante e investiga\u00e7\u00e3o imediata diante de quaisquer suspeitas, para minimizar o risco de acidentes em paciente com adornos met\u00e1licos, e a coopera\u00e7\u00e3o do paciente na obedi\u00eancia das medidas preventivas de acidentes deve ser obrigat\u00f3ria.","l":"pt"},{"a":"INTRODUCTION: Electrosurgery technology is known in a long time ago, and has gained increasing prominence. Nevertheless, it still presents many risks as to its use. Several lesions can be caused by electrocautery, and burns are the most frequent complications. There is not always patient's cooperation regarding preventive measures. METHOD: Review of relevant literature on the basis of legal questioning of a patient who refused to remove their ornaments at the beginning of surgery, being already in the operating room. CONCLUSION: It is essential to have the knowledge of the fundamentals of electrosurgery, its correct use, safety equipment, constant monitoring and immediate investigation to minimize the risk of accidents in patients with metal ornaments, and patient cooperation in obeying the preventive measures of accidents should be mandatory.","l":"en"}],"v709":[{"_":"article"}],"v980":[{"_":"scl"}],"v880":[{"_":"S0102-67202010000300010"}],"v2":[{"_":"S0102-6720(10)02300310"}],"v91":[{"_":"20101025"}],"v4":[{"_":"v23n3"}],"v14":[{"l":"186","f":"183"}],"v700":[{"_":"2"}],"v702":[{"_":"V:\\SciELO\\serial\\abcd\\v23n3\\markup\\v23n3a10.htm"}],"v705":[{"_":"S"}],"v706":[{"_":"h"}],"v65":[{"_":"20100900"}],"v708":[{"_":"1"}],"v512":[{"_":"S0102-67202010000300010"}],"v49":[{"_":"ABCD070"}],"v1":[{"_":"br1.1"}],"v882":[{"n":"3","v":"23"}],"v120":[{"_":"4.0"}]}},

				try {
					r.setDocumentTitle( resultItem.getJSONArray("v12").getJSONObject(0).getString("_"));	
				} catch (JSONException e) {
					last = last + "\n" +"ti";
				}
				try {
					result = "";
					for (int j=0; j<resultItem.getJSONArray("v10").length(); j++) {
						result = result + resultItem.getJSONArray("v10").getJSONObject(j).getString("s") + ", " +  resultItem.getJSONArray("v10").getJSONObject(j).getString("n") + "; ";
					}
					r.setDocumentAuthors(result);	
				} catch (JSONException e) {
					last = last + "\n" +"au" ;
				}

					r.setCol(SciELOAppsActivity.myConfig.getJcn().getItem(journal.getCollectionId()));
				
					_pid = id;	
					_pid = _pid.replace("art-", "");
					_pid = _pid.replace("^c" + journal.getCollectionId(), "");
					r.setDocumentId(_pid);
				
				
				
				try {
					r.setIssueLabel(resultItem.getJSONArray("v702").getJSONObject(0).getString("_"));					
				} catch (JSONException e) {
					last = last + "\n" +"fo";
				}
				
				
				
				searchResultList.add(r);
			} catch (JSONException e) {
				Log.d(TAG, "JSONException loadResultList " + new Integer(i).toString() + " " + last, e);	
	        } 
		}
	}
}

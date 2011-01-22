package taberystwyth.prelim;

import java.util.HashMap;

public class DrawTypeRepository {
    private static DrawTypeRepository instance = new DrawTypeRepository();
    
    public static DrawTypeRepository getInstance(){
        return instance;
    }
    
    public HashMap<String, String> getTeamDrawTypeMap() {
		return teamDrawTypeMap;
	}

	public HashMap<String, String> getJudgeDrawTypeMap() {
		return judgeDrawTypeMap;
	}

	public HashMap<String, String> getLocationDrawTypeMap() {
		return locationDrawTypeMap;
	}

	final HashMap<String, String> teamDrawTypeMap = new HashMap<String, String>();
    final HashMap<String, String> judgeDrawTypeMap = new HashMap<String, String>();
    final HashMap<String, String> locationDrawTypeMap = new HashMap<String, String>();
    
    private DrawTypeRepository(){
        /*
         * Team draw types
         */
       teamDrawTypeMap.put("WUDC", 
               "The best teams will be grouped together in the same room " +
               "and teams will be pulled up to fill higher rooms.");
       /*teamDrawTypeMap.put("Folded",
               "");*/
        
       /*
        * Judge draw types
        */
       judgeDrawTypeMap.put("Balanced",
               "The best judges in the pool are allocated as Chairing" +
               " Judges.  The remaining wings are assigned to different " + 
               "quality rooms based on their quality.");
       
       /*
        * Location draw types
        */
       locationDrawTypeMap.put("Random",
               "Matches will be assigned to rooms randomly.");
       locationDrawTypeMap.put("Best to Best",
               "The highest matches will be put into the best quality rooms");
       
    }
}

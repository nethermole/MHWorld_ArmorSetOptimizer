import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JewelRegistry {
    private final static boolean ONLY_MY_JEWELS = true;

    //skill, jewelList
    Map<String, List<Jewel>> jewelMap;
    Map<String, Integer> ownedJewels;

    public JewelRegistry(List<Jewel> jewelList, Map<String, Integer> ownedJewels){
        this.ownedJewels = ownedJewels;
        jewelMap = new HashMap<>();
        for(Jewel jewel : jewelList) {
            if(ONLY_MY_JEWELS) {
                if(ownedJewels.containsKey(jewel.getName())){
                    for (String skill : jewel.getSkills().keySet()) {
                        List<Jewel> jewelsBySkill = jewelMap.getOrDefault(skill, new ArrayList<>());
                        jewelsBySkill.add(jewel);
                        jewelMap.put(skill, jewelsBySkill);
                    }
                }
            } else{
                for (String skill : jewel.getSkills().keySet()) {
                    List<Jewel> jewelsBySkill = jewelMap.getOrDefault(skill, new ArrayList<>());
                    jewelsBySkill.add(jewel);
                    jewelMap.put(skill, jewelsBySkill);
                }
            }
        }
    }

    public List<Jewel> getJewelsBySkill(String skill){
        /*if(ONLY_MY_JEWELS){
            List<Jewel> ownedJewelsOfSkill = new ArrayList<>();
            for(Jewel jewel : jewelMap.get(skill)){
                if(ownedJewels.containsKey(jewel.getName())){
                    ownedJewelsOfSkill.add(jewel);
                }
            }
            return ownedJewelsOfSkill;
        } else {*/
            return jewelMap.get(skill);
        //}
    }
}

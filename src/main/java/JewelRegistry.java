import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JewelRegistry {
    Map<String, List<Jewel>> jewelMap;

    public JewelRegistry(List<Jewel> jewelList){
        jewelMap = new HashMap<>();
        for(Jewel jewel : jewelList) {
            for (String skill : jewel.getSkills().keySet()) {
                List<Jewel> jewelsBySkill = jewelMap.getOrDefault(skill, new ArrayList<>());
                jewelsBySkill.add(jewel);
                jewelMap.put(skill, jewelsBySkill);
            }
        }
    }

    public List<Jewel> getJewelsBySkill(String skill){
        return jewelMap.get(skill);
    }
}

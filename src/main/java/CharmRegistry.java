import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharmRegistry {

    private final static boolean ONLY_USE_MAX_CHARM = true;

    private List<Charm> charmList;

    public CharmRegistry(List<Charm> charmList)throws Exception{
        this.charmList = charmList;
        if(ONLY_USE_MAX_CHARM){
            filterCharmListToMax();
        }
    }

    private void filterCharmListToMax() throws Exception{
        Map<String, Charm> charmsByName = new HashMap<>();
        for(Charm charm : charmList){
            charmsByName.put(charm.getName(), charm);
        }
        Parser parser = new Parser();
        List<String> nonfinalCharmNames = parser.getNonfinalCharmNames();
        for(String charmName : nonfinalCharmNames) {
            charmList.remove(charmsByName.get(charmName));
        }
    }

    public List<Charm> getCharmsBySkillName(String skill){
        List<Charm> selectedCharms = new ArrayList<>();
        for(Charm charm : charmList){
            if(charm.getSkills().containsKey(skill)){
                selectedCharms.add(charm);
            }
        }
        return selectedCharms;
    }

    public List<Charm> getCharmList() {
        return charmList;
    }

}

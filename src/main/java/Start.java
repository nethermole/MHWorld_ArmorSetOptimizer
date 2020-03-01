import java.util.HashMap;
import java.util.Map;

public class Start {

    public static void main(String[] args) throws Exception{
        Map<String, Integer> desiredSkills = new HashMap<>();
        desiredSkills.put("Critical Eye", 7);
        desiredSkills.put("Critical Boost", 3);
        desiredSkills.put("Maximum Might", 3);
        desiredSkills.put("Piercing Shots", 2);
        desiredSkills.put("Focus", 3);

        Parser parser = new Parser();
        SkillRegistry skillRegistry = new SkillRegistry(parser.loadSkills());
        CharmRegistry charmRegistry = new CharmRegistry(parser.loadCharms());
        EquipmentRegistry equipmentRegistry = new EquipmentRegistry(parser.loadEquipment());
        JewelRegistry jewelRegistry = new JewelRegistry(parser.loadJewels());

        skillRegistry.validateSkills(desiredSkills.keySet());

        SetFinder setFinder = new SetFinder(equipmentRegistry, charmRegistry, jewelRegistry);
        setFinder.findSet(desiredSkills);

    }

}

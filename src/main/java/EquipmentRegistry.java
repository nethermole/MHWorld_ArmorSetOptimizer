import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EquipmentRegistry {

    public static boolean ONLY_MR_ARMOR = true;

    private List<Equipment> heads;
    private List<Equipment> chests;
    private List<Equipment> arms;
    private List<Equipment> waists;
    private List<Equipment> legs;

    private Map<String, List<Equipment>> equipmentBySkill;

    public EquipmentRegistry(List<Equipment> equipmentList){
        heads = new ArrayList<>();
        chests = new ArrayList<>();
        arms = new ArrayList<>();
        waists = new ArrayList<>();
        legs = new ArrayList<>();
        equipmentBySkill = new HashMap<>();
        loadEquipment(equipmentList);
    }

    private void loadEquipment(List<Equipment> equipmentList){
        for(Equipment equipment : equipmentList) {
            if(ONLY_MR_ARMOR && (equipment.getRarity() < 9)){
                continue;
            }


            switch (equipment.getPart()) {
                case head:
                    heads.add(equipment);
                    break;
                case chest:
                    chests.add(equipment);
                    break;
                case arms:
                    arms.add(equipment);
                    break;
                case waist:
                    waists.add(equipment);
                    break;
                case legs:
                    legs.add(equipment);
                    break;
            }
            for(String skill : equipment.getArmorSkills().keySet()){
                List<Equipment> bySkill = equipmentBySkill.getOrDefault(skill, new ArrayList<>());
                bySkill.add(equipment);
                equipmentBySkill.put(skill, bySkill);
            }
        }
    }

    public List<Equipment> getHeads() {
        return heads;
    }

    public List<Equipment> getChests() {
        return chests;
    }

    public List<Equipment> getArms() {
        return arms;
    }

    public List<Equipment> getWaists() {
        return waists;
    }

    public List<Equipment> getLegs() {
        return legs;
    }

    public Map<String, List<Equipment>> getEquipmentBySkill() {
        return equipmentBySkill;
    }

    public List<Equipment> getEquipmentByPartAndSkill(Part part, String skill) {
        List<Equipment> equipment = equipmentBySkill.get(skill);
        equipment = equipment.stream().filter(eq -> eq.getPart() == part).collect(Collectors.toList());
        return equipment;
    }
}

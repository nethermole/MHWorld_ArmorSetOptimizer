import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SetFinder {

    private final static boolean SEARCH_ALL_ARMOR = false;
    private final static boolean ONLY_MY_JEWELS = false;

    EquipmentRegistry equipmentRegistry;
    CharmRegistry charmRegistry;
    JewelRegistry jewelRegistry;

    public SetFinder(EquipmentRegistry equipmentRegistry, CharmRegistry charmRegistry, JewelRegistry jewelRegistry) {
        this.equipmentRegistry = equipmentRegistry;
        this.charmRegistry = charmRegistry;
        this.jewelRegistry = jewelRegistry;
    }

    public List<ArmorSet> findSet(Map<String, Integer> desiredSkills) {
        List<Equipment> heads = new ArrayList<>();
        List<Equipment> chests = new ArrayList<>();
        List<Equipment> arms = new ArrayList<>();
        List<Equipment> waists = new ArrayList<>();
        List<Equipment> legs = new ArrayList<>();
        List<Charm> charms = new ArrayList<>();

        loadParts(desiredSkills, heads, chests, arms, waists, legs, charms);

        for (Charm charm : charms) {
            for (Equipment head : heads) {
                for (Equipment chest : chests) {
                    for (Equipment arm : arms) {
                        for (Equipment waist : waists) {
                            for (Equipment leg : legs) {
                                List<Equipment> armorSet = new ArrayList<>();
                                armorSet.add(head);
                                armorSet.add(chest);
                                armorSet.add(arm);
                                armorSet.add(waist);
                                armorSet.add(leg);

                                //load skills
                                Map<String, Integer> armorSetSkills = new HashMap<>();
                                for (Equipment equipment : armorSet) {
                                    for (String skill : equipment.getArmorSkills().keySet()) {
                                        int previous = armorSetSkills.getOrDefault(skill, 0);
                                        armorSetSkills.put(skill, previous + equipment.getArmorSkills().get(skill));
                                    }
                                }
                                for (String skill : charm.getSkills().keySet()) {
                                    int previous = armorSetSkills.getOrDefault(skill, 0);
                                    armorSetSkills.put(skill, previous + charm.getSkills().get(skill));
                                }

                                //calculate remaining skill gap
                                Map<String, Integer> remainingSkills = new HashMap<>();
                                for (String desiredSkill : desiredSkills.keySet()) {
                                    Integer amount = desiredSkills.get(desiredSkill);
                                    if (armorSetSkills.containsKey(desiredSkill)) {
                                        amount -= armorSetSkills.get(desiredSkill);
                                        if (amount > 0) {
                                            remainingSkills.put(desiredSkill, amount);
                                        }
                                    } else {
                                        remainingSkills.put(desiredSkill, amount);
                                    }
                                }

                                //load slotcounts
                                int[] slotCounts = new int[5];
                                for (Equipment equipment : armorSet) {
                                    slotCounts[1] += equipment.getSlot1();
                                    slotCounts[2] += equipment.getSlot2();
                                    slotCounts[3] += equipment.getSlot3();
                                    slotCounts[4] += equipment.getSlot4();
                                }

                                Set<HashMap<String, Integer>> waysToFill = fillSlots(remainingSkills, slotCounts);
                                if(!waysToFill.isEmpty()) {
                                    System.out.println(head.getName() + ", " +chest.getName() + ", " +arm.getName() + ", " +waist.getName() + ", " +leg.getName() + ", " +charm.getName());
                                    for(HashMap<String, Integer> jewelList : waysToFill) {
                                        for(Map.Entry<String, Integer> entry : jewelList.entrySet()){
                                            //System.out.println(entry.getKey() + ": " + entry.getValue());
                                        }
                                        //System.out.println("\n");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private Set<HashMap<String, Integer>> fillSlots(Map<String, Integer> desiredSkills, int[] slotCounts) {
        return fillSlotRecurse(desiredSkills, slotCounts[1], slotCounts[2], slotCounts[3], slotCounts[4], new ArrayList<>(), new HashSet<>());
    }

    /**
     * recursion baybeeeeee
     */
    private Set<HashMap<String, Integer>> fillSlotRecurse(Map<String, Integer> desiredSkills, int slot1s, int slot2s, int slot3s, int slot4s, List<Jewel> jewelsSoFar, Set<HashMap<String, Integer>> successes) {
        //case: no remaining desired skills
        if (desiredSkills.isEmpty()) {
            HashMap<String, Integer> jewelCounts = new HashMap<>();
            for(Jewel jewel : jewelsSoFar){
                int currentCount = jewelCounts.getOrDefault(jewel.getName(), 0);
                currentCount++;
                jewelCounts.put(jewel.getName(), currentCount);
            }
            successes.add(jewelCounts);
            return successes;
        } else {

            //case: fill a jewel, trigger recursion
            for (String desiredSkill : desiredSkills.keySet()) {
                for (Jewel jewel : jewelRegistry.getJewelsBySkill(desiredSkill)) {
                    HashMap<String, Integer> recurseOnDesiredSkills = new HashMap<>(desiredSkills);
                    ArrayList<Jewel> recurseOnJewelsSoFar = new ArrayList<>(jewelsSoFar);
                    int recurseOnSlot1s = slot1s;
                    int recurseOnSlot2s = slot2s;
                    int recurseOnSlot3s = slot3s;
                    int recurseOnSlot4s = slot4s;

                    //determine slot, and fit it in TODO: fit slot3 jewels into slot4s, etc
                    int jewelSlot = jewel.getSlot();
                    switch (jewelSlot) {
                        case 1:
                            if (recurseOnSlot1s > 0) {
                                recurseOnSlot1s--;
                                recurseOnJewelsSoFar.add(jewel);
                            } else {
                                continue;
                            }
                            break;
                        case 2:
                            if (recurseOnSlot2s > 0) {
                                recurseOnSlot2s--;
                                recurseOnJewelsSoFar.add(jewel);
                            } else {
                                continue;
                            }
                            break;
                        case 3:
                            if (recurseOnSlot3s > 0) {
                                recurseOnSlot3s--;
                                recurseOnJewelsSoFar.add(jewel);
                            } else {
                                continue;
                            }
                            break;
                        case 4:
                            if (recurseOnSlot4s > 0) {
                                recurseOnSlot4s--;
                                recurseOnJewelsSoFar.add(jewel);
                            } else {
                                continue;
                            }
                            break;
                        default:
                            throw new RuntimeException("invalid jewel slot");
                    }
                    //modify remaining skills
                    for (String skill : jewel.getSkills().keySet()) {
                        if (recurseOnDesiredSkills.containsKey(skill)) {
                            int newAmount = recurseOnDesiredSkills.get(skill) - jewel.getSkills().get(skill);
                            if(newAmount > 0) {
                                recurseOnDesiredSkills.put(skill, newAmount);
                            } else{
                                recurseOnDesiredSkills.remove(skill);
                            }
                        }
                    }

                    fillSlotRecurse(recurseOnDesiredSkills, recurseOnSlot1s, recurseOnSlot2s, recurseOnSlot3s, recurseOnSlot4s, recurseOnJewelsSoFar, successes);
                }
            }
        }
        return successes;
    }

    private void loadParts(Map<String, Integer> desiredSkills, List<Equipment> heads, List<Equipment> chests, List<Equipment> arms, List<Equipment> waists, List<Equipment> legs, List<Charm> charms) {
        if (SEARCH_ALL_ARMOR) {
            heads = equipmentRegistry.getHeads();
            chests = equipmentRegistry.getChests();
            arms = equipmentRegistry.getArms();
            waists = equipmentRegistry.getWaists();
            legs = equipmentRegistry.getLegs();
        } else {
            for (String skill : desiredSkills.keySet()) {
                heads.addAll(equipmentRegistry.getEquipmentByPartAndSkill(Part.head, skill));
                chests.addAll(equipmentRegistry.getEquipmentByPartAndSkill(Part.chest, skill));
                arms.addAll(equipmentRegistry.getEquipmentByPartAndSkill(Part.arms, skill));
                waists.addAll(equipmentRegistry.getEquipmentByPartAndSkill(Part.waist, skill));
                legs.addAll(equipmentRegistry.getEquipmentByPartAndSkill(Part.legs, skill));
            }
        }
        for (String skill : desiredSkills.keySet()) {
            charms.addAll(charmRegistry.getCharmsBySkillName(skill));
        }
    }

}

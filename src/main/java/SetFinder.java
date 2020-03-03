import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class SetFinder {

    private final static boolean SEARCH_ALL_ARMOR = false;

    EquipmentRegistry equipmentRegistry;
    CharmRegistry charmRegistry;
    JewelRegistry jewelRegistry;
    FinderCache finderCache;

    public SetFinder(EquipmentRegistry equipmentRegistry, CharmRegistry charmRegistry, JewelRegistry jewelRegistry) {
        this.equipmentRegistry = equipmentRegistry;
        this.charmRegistry = charmRegistry;
        this.jewelRegistry = jewelRegistry;
        finderCache = new FinderCache();
    }

    public List<ArmorSet> findSet(Map<String, Integer> desiredSkills) {
        List<Charm> charms = new ArrayList<>();
        for(String skill : desiredSkills.keySet()){
            charms.addAll(charmRegistry.getCharmsBySkillName(skill));
        }
        charms = new ArrayList<>(new HashSet<>(charms));

        for (Charm charm : charms) {
            HashMap<String, Integer> desiredSkillsAfterCharm = new HashMap<>(desiredSkills);
            for(String skill : charm.getSkills().keySet()){
                int amount = desiredSkillsAfterCharm.getOrDefault(skill, 0);
                amount -= charm.getSkills().get(skill);
                if(amount > 0) {
                    desiredSkillsAfterCharm.put(skill, amount);
                } else{
                    desiredSkillsAfterCharm.remove(skill);
                }
            }
            List<Equipment> heads = new ArrayList<>();
            for(String skill : desiredSkillsAfterCharm.keySet()){
                heads.addAll(equipmentRegistry.getEquipmentByPartAndSkill(Part.head, skill));
            }
            heads = new ArrayList<>(new HashSet<>(heads));



            for (Equipment head : heads) {
                HashMap<String, Integer> desiredSkillsAfterHead = new HashMap<>(desiredSkillsAfterCharm);
                for(String skill : head.getArmorSkills().keySet()){
                    int amount = desiredSkillsAfterHead.getOrDefault(skill, 0);
                    amount -= head.getArmorSkills().getOrDefault(skill, 0);
                    if(amount > 0) {
                        desiredSkillsAfterHead.put(skill, amount);
                    } else{
                        desiredSkillsAfterHead.remove(skill);
                    }
                }
                List<Equipment> chests = new ArrayList<>();
                for(String skill : desiredSkillsAfterHead.keySet()){
                    chests.addAll(equipmentRegistry.getEquipmentByPartAndSkill(Part.chest, skill));
                }
                chests = new ArrayList<>(new HashSet<>(chests));



                for (Equipment chest : chests) {
                    HashMap<String, Integer> desiredSkillsAfterChest = new HashMap<>(desiredSkillsAfterHead);
                    for(String skill : chest.getArmorSkills().keySet()){
                        int amount = desiredSkillsAfterChest.getOrDefault(skill, 0);
                        amount -= chest.getArmorSkills().getOrDefault(skill,0 );
                        if(amount > 0) {
                            desiredSkillsAfterChest.put(skill, amount);
                        } else{
                            desiredSkillsAfterChest.remove(skill);
                        }
                    }
                    List<Equipment> equipmentList = new ArrayList<>();
                    equipmentList.add(head);
                    equipmentList.add(chest);
                    if(finderCache.cacheHit(Part.chest, equipmentList, desiredSkillsAfterChest) != null){
                        System.out.println("cache hit chest");
                        continue;
                    }

                    List<Equipment> arms = new ArrayList<>();
                    for(String skill : desiredSkillsAfterChest.keySet()){
                        arms.addAll(equipmentRegistry.getEquipmentByPartAndSkill(Part.arms, skill));
                    }
                    arms = new ArrayList<>(new HashSet<>(arms));



                    for (Equipment arm : arms) {
                        HashMap<String, Integer> desiredSkillsAfterArm = new HashMap<>(desiredSkillsAfterChest);
                        for(String skill : arm.getArmorSkills().keySet()){
                            int amount = desiredSkillsAfterArm.getOrDefault(skill, 0);
                            amount -= arm.getArmorSkills().getOrDefault(skill, 0);
                            if(amount > 0) {
                                desiredSkillsAfterArm.put(skill, amount);
                            } else{
                                desiredSkillsAfterArm.remove(skill);
                            }
                        }
                        equipmentList = new ArrayList<>();
                        equipmentList.add(head);
                        equipmentList.add(chest);
                        equipmentList.add(arm);
                        if(finderCache.cacheHit(Part.arms, equipmentList, desiredSkillsAfterArm) != null){
                            System.out.println("cache hit arms");
                            continue;
                        }

                        List<Equipment> waists = new ArrayList<>();
                        for(String skill : desiredSkillsAfterArm.keySet()){
                            waists.addAll(equipmentRegistry.getEquipmentByPartAndSkill(Part.waist, skill));
                        }
                        waists = new ArrayList<>(new HashSet<>(waists));



                        for (Equipment waist : waists) {
                            HashMap<String, Integer> desiredSkillsAfterWaist = new HashMap<>(desiredSkillsAfterArm);
                            for(String skill : waist.getArmorSkills().keySet()){
                                int amount = desiredSkillsAfterWaist.getOrDefault(skill, 0);
                                amount -= waist.getArmorSkills().getOrDefault(skill, 0);
                                if(amount > 0) {
                                    desiredSkillsAfterWaist.put(skill, amount);
                                } else{
                                    desiredSkillsAfterWaist.remove(skill);
                                }
                            }
                            equipmentList = new ArrayList<>();
                            equipmentList.add(head);
                            equipmentList.add(chest);
                            equipmentList.add(arm);
                            equipmentList.add(waist);
                            if(finderCache.cacheHit(Part.waist, equipmentList, desiredSkillsAfterWaist) != null){
                                System.out.println("cache hit waist");
                                continue;
                            }

                            List<Equipment> legs = new ArrayList<>();
                            for(String skill : desiredSkillsAfterWaist.keySet()){
                                legs.addAll(equipmentRegistry.getEquipmentByPartAndSkill(Part.legs, skill));
                            }
                            legs = new ArrayList<>(new HashSet<>(legs));



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
                                    System.out.println(charm.getName() + ", " + head.getName() + ", " +chest.getName() + ", " +arm.getName() + ", " +waist.getName() + ", " +leg.getName());
                                    finderCache.cacheSet(charm, head, chest, arm, waist, leg, desiredSkills);
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
                List<Jewel> jewelsWithDesiredSkill = jewelRegistry.getJewelsBySkill(desiredSkill);
                if(jewelsWithDesiredSkill == null){
                    continue;
                }
                for (Jewel jewel : jewelsWithDesiredSkill) {
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

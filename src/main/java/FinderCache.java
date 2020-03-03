import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FinderCache {

    //equipment stuff, sets that matched
            //most recently chosen part, skills so far
                          //skill remaining, amount
                          //slot, how many so far
    private HashMap<HashMap<Part, HashMap<String, Integer>>, Set<Set<Equipment>>> cachedSetsBySkillsAndSlots;

    public FinderCache(){
        cachedSetsBySkillsAndSlots = new HashMap<>();
    }

    public Set<Set<Equipment>> checkCache(Part chosenPart, List<Equipment> equipmentSoFar, HashMap<String, Integer> remainingSkills){
        HashMap<String, Integer> _remainingSkills = new HashMap<>(remainingSkills);
        HashMap<Part, HashMap<String, Integer>> key = new HashMap<>();
        addSlotsToCacheMap(equipmentSoFar, _remainingSkills);
        key.put(chosenPart, _remainingSkills);
        Set<Set<Equipment>> result = cachedSetsBySkillsAndSlots.get(key);
        if(result != null && result.isEmpty()){
            System.out.print("no set - ");
        }
        return result;
    }

    public void cacheFail(Part chosenPart, List<Equipment> equipmentSoFar, HashMap<String, Integer> remainingSkills){
        HashMap<String, Integer> _remainingSkills = new HashMap<>(remainingSkills);
        HashMap<Part, HashMap<String, Integer>> key = new HashMap<>();
        addSlotsToCacheMap(equipmentSoFar, _remainingSkills);
        key.put(chosenPart, _remainingSkills);
        cachedSetsBySkillsAndSlots.put(key, new HashSet<>());
    }

    public void cacheSet(Equipment head, Equipment chest, Equipment arm, Equipment waist, Equipment leg, Map<String, Integer> desiredSkills){
        HashMap<String, Integer> _desiredSkills = new HashMap<>(desiredSkills);
        List<Equipment> equipmentSoFar = new ArrayList<>();

        //head
        equipmentSoFar.add(head);
        removeSkillsFromMap(head, _desiredSkills);
        HashMap<String, Integer> cacheMapHead = new HashMap<>(_desiredSkills);
        addSlotsToCacheMap(equipmentSoFar, cacheMapHead);
        HashMap<Part, HashMap<String, Integer>> keyHead = new HashMap<>();
        keyHead.put(Part.head, cacheMapHead);

        Set<Set<Equipment>> currentSetsHead = cachedSetsBySkillsAndSlots.getOrDefault(keyHead, new HashSet<>());
        Set<Equipment> newsetHead = new HashSet<>();
        newsetHead.add(chest);
        newsetHead.add(arm);
        newsetHead.add(waist);
        newsetHead.add(leg);
        currentSetsHead.add(newsetHead);
        cachedSetsBySkillsAndSlots.put(keyHead, currentSetsHead);


        //chest
        equipmentSoFar.add(chest);
        removeSkillsFromMap(chest, _desiredSkills);
        HashMap<String, Integer> cacheMapChest = new HashMap<>(_desiredSkills);
        addSlotsToCacheMap(equipmentSoFar, cacheMapChest);
        HashMap<Part, HashMap<String, Integer>> keyChest = new HashMap<>();
        keyChest.put(Part.chest, cacheMapChest);

        Set<Set<Equipment>> currentSetsChest = cachedSetsBySkillsAndSlots.getOrDefault(keyChest, new HashSet<>());
        Set<Equipment> newSetChest = new HashSet<>();
        newSetChest.add(arm);
        newSetChest.add(waist);
        newSetChest.add(leg);
        currentSetsChest.add(newSetChest);
        cachedSetsBySkillsAndSlots.put(keyChest, currentSetsChest);


        //arm
        equipmentSoFar.add(arm);
        removeSkillsFromMap(arm, _desiredSkills);
        HashMap<String, Integer> cacheMapArm = new HashMap<>(_desiredSkills);
        addSlotsToCacheMap(equipmentSoFar, cacheMapArm);
        HashMap<Part, HashMap<String, Integer>> keyArm = new HashMap<>();
        keyArm.put(Part.arms, cacheMapArm);

        Set<Set<Equipment>> currentSetsArm = cachedSetsBySkillsAndSlots.getOrDefault(keyArm, new HashSet<>());
        Set<Equipment> newSetArm = new HashSet<>();
        newSetArm.add(waist);
        newSetArm.add(leg);
        currentSetsArm.add(newSetArm);
        cachedSetsBySkillsAndSlots.put(keyArm, currentSetsArm);

        //waist
        equipmentSoFar.add(waist);
        removeSkillsFromMap(waist, _desiredSkills);
        HashMap<String, Integer> cacheMapWaist = new HashMap<>(_desiredSkills);
        addSlotsToCacheMap(equipmentSoFar, cacheMapWaist);
        HashMap<Part, HashMap<String, Integer>> keyWaist = new HashMap<>();
        keyWaist.put(Part.waist, cacheMapWaist);

        Set<Set<Equipment>> currentSetsWaist = cachedSetsBySkillsAndSlots.getOrDefault(keyWaist, new HashSet<>());
        Set<Equipment> newSetWaist = new HashSet<>();
        newSetWaist.add(leg);
        currentSetsWaist.add(newSetWaist);
        cachedSetsBySkillsAndSlots.put(keyWaist, currentSetsWaist);
    }

    private void addSlotsToCacheMap(List<Equipment> equipmentList, HashMap<String, Integer> cacheMap){
        int[] slots = new int[5];
        for(Equipment equipment : equipmentList){
            slots[1] += equipment.getSlot1();
            slots[2] += equipment.getSlot2();
            slots[3] += equipment.getSlot3();
            slots[4] += equipment.getSlot4();
        }
        cacheMap.put("1slot", slots[1]);
        cacheMap.put("2slot", slots[2]);
        cacheMap.put("3slot", slots[3]);
        cacheMap.put("4slot", slots[4]);
    }

    private void removeSkillsFromMap(Equipment equipment, HashMap<String, Integer> map){
        for(Map.Entry<String, Integer> skill : equipment.getArmorSkills().entrySet()){
            if(map.containsKey(skill.getKey())) {
                int amount = map.get(skill.getKey());
                amount -= skill.getValue();
                if(amount > 0) {
                    map.put(skill.getKey(), amount);
                }
            }
        }
    }

}

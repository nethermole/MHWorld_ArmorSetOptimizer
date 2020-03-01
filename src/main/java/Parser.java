import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Parser {
    final private static String armor_base_path = "source_data/armors/armor_base.csv";
    final private static String armor_skills_path = "source_data/armors/armor_skills_ext.csv";
    final private static String skills_path = "source_data/skills/skill_levels.csv";
    final private static String charms_path = "source_data/charms/charm_ext.json";
    final private static String charms_base_path = "source_data/charms/charm_base.csv";
    final private static String decoration_base_path = "source_data/decorations/decoration_base.csv";

    public List<Jewel> loadJewels() throws Exception{
        Scanner scanner = new Scanner(new File(decoration_base_path));
        scanner.nextLine();
        List<Jewel> jewelList = new ArrayList<>();
        while(scanner.hasNextLine()){
            String jewelLine = scanner.nextLine();
            String[] jewelArray = jewelLine.split(",");
            String name = jewelArray[1];
            int slot =  Integer.parseInt(jewelArray[2]);
            Map<String, Integer> jewelSkills = new HashMap<>();
            if(!jewelArray[4].isEmpty()){
                jewelSkills.put(jewelArray[4], Integer.parseInt(jewelArray[5]));
            }
            if(jewelArray.length > 6 && !jewelArray[6].isEmpty()){
                jewelSkills.put(jewelArray[6], Integer.parseInt(jewelArray[7]));
            }
            jewelList.add(new Jewel(name, slot, jewelSkills));
        }
        return jewelList;
    }

    public List<String> getNonfinalCharmNames() throws Exception{
        Scanner scanner = new Scanner(new File(charms_base_path));
        scanner.nextLine();
        List<String> nonfinalCharmNames = new ArrayList<>();
        while(scanner.hasNextLine()){
            String charmLine = scanner.nextLine();
            String[] charmArray = charmLine.split(",");
            if(!charmArray[2].isEmpty()){
                nonfinalCharmNames.add(charmArray[2]);
            }
        }
        return nonfinalCharmNames;
    }

    public List<Charm> loadCharms() throws Exception{
        String json = "";
        Scanner scanner = new Scanner(new File(charms_path));
        while(scanner.hasNextLine()){
            json += scanner.nextLine();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        Iterator<Map.Entry<String, JsonNode>> iterator = jsonNode.fields();

        List<Charm> charmList = new ArrayList<>();
        while(iterator.hasNext()){
            Map.Entry<String, JsonNode> charmNode = iterator.next();
            String name = charmNode.getKey();
            Map<String, Integer> skills = new HashMap<>();
            Iterator<Map.Entry<String, JsonNode>> skillIter = charmNode.getValue().get("skills").fields();
            while(skillIter.hasNext()){
                Map.Entry<String, JsonNode> charmSkill = skillIter.next();
                skills.put(charmSkill.getKey(), charmSkill.getValue().intValue());
            }
            charmList.add(new Charm(name, skills));
        }
        return charmList;
    }

    public Map<String, Integer> loadSkills() throws Exception{
        Map<String, Integer> skills = new HashMap<>();
        Scanner scanner = new Scanner(new File(skills_path));
        scanner.nextLine();
        while(scanner.hasNextLine()){
            String skillLine = scanner.nextLine();
            String[] skillArray = skillLine.split(",");

            String name = skillArray[0];
            Integer level = Integer.parseInt(skillArray[1]);

            skills.put(name, Math.max(skills.getOrDefault(name, 0), level));
        }
        return skills;
    }

    public List<Equipment> loadEquipment() throws Exception{
        Map<String, Equipment> armors = new HashMap<>();

        Scanner scanner = new Scanner(new File(armor_base_path));
        scanner.nextLine();
        while(scanner.hasNextLine()){
            String armorLine = scanner.nextLine();
            String[] armorArray = armorLine.split(",");

            String name = armorArray[1];
            int rarity = Integer.parseInt(armorArray[2]);
            Part part = Part.valueOf(armorArray[3]);
            int[] gems = new int[5];
            gems[Integer.parseInt(armorArray[5])]++;
            gems[Integer.parseInt(armorArray[6])]++;
            gems[Integer.parseInt(armorArray[7])]++;
            int maxDefense = Integer.parseInt(armorArray[9]);
            int defenseFire = Integer.parseInt(armorArray[11]);
            int defenseWater = Integer.parseInt(armorArray[12]);
            int defenseThunder = Integer.parseInt(armorArray[13]);
            int defenseIce = Integer.parseInt(armorArray[14]);
            int defenseDragon = Integer.parseInt(armorArray[15]);

            Equipment equipment = new Equipment(name, rarity, part, gems[1], gems[2], gems[3], gems[4], maxDefense, defenseFire, defenseWater, defenseThunder, defenseIce, defenseDragon);
            armors.put(name, equipment);
        }

        scanner = new Scanner(new File(armor_skills_path));
        scanner.nextLine();
        while(scanner.hasNextLine()){
            String armorLine = scanner.nextLine();
            String[] armorArray = armorLine.split(",");
            Equipment equipment = armors.get(armorArray[0]);

            Map<String, Integer> armorSkills = new HashMap<>();
            if(armorArray.length > 1){
                armorSkills.put(armorArray[1], Integer.parseInt(armorArray[2]));
            }
            if(armorArray.length > 3){
                armorSkills.put(armorArray[3], Integer.parseInt(armorArray[4]));
            }
            equipment.setArmorSkills(armorSkills);
        }

        return new ArrayList<>(armors.values());
    }

}

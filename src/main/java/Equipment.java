import java.util.HashMap;
import java.util.Map;

public class Equipment {
    private Part part;
    private int rarity;
    private String name;
    private int slot1;
    private int slot2;
    private int slot3;
    private int slot4;
    private int maxDefense;
    private int defenseFire;
    private int defenseWater;
    private int defenseThunder;
    private int defenseIce;
    private int defenseDragon;
    private Map<String, Integer> armorSkills;

    public Equipment(String name, int rarity, Part part, int slot1, int slot2, int slot3, int slot4, int maxDefense, int defenseFire, int defenseWater, int defenseThunder, int defenseIce, int defenseDragon) {
        this.name = name;
        this.part = part;
        this.rarity = rarity;
        this.slot1 = slot1;
        this.slot2 = slot2;
        this.slot3 = slot3;
        this.slot4 = slot4;
        this.maxDefense = maxDefense;
        this.defenseFire = defenseFire;
        this.defenseWater = defenseWater;
        this.defenseThunder = defenseThunder;
        this.defenseIce = defenseIce;
        this.defenseDragon = defenseDragon;
    }

    public Part getPart() {
        return part;
    }

    public String getName() {
        return name;
    }

    public int getSlot1() {
        return slot1;
    }

    public int getSlot2() {
        return slot2;
    }

    public int getSlot3() {
        return slot3;
    }

    public int getSlot4() {
        return slot4;
    }

    public int getMaxDefense() {
        return maxDefense;
    }

    public int getDefenseFire() {
        return defenseFire;
    }

    public int getDefenseWater() {
        return defenseWater;
    }

    public int getDefenseThunder() {
        return defenseThunder;
    }

    public int getDefenseIce() {
        return defenseIce;
    }

    public int getDefenseDragon() {
        return defenseDragon;
    }

    public Map<String, Integer> getArmorSkills() {
        return armorSkills;
    }

    public void setArmorSkills(Map<String, Integer> armorSkills) {
        this.armorSkills = armorSkills;
    }

    public int getRarity() {
        return rarity;
    }
}

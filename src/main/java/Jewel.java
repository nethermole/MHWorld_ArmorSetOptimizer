import java.util.Map;

public class Jewel {
    private String name;
    private int slot;
    private Map<String, Integer> skills;

    public Jewel(String name, int slot, Map<String, Integer> skills){
        this.name = name;
        this.slot = slot;
        this.skills = skills;
    }

    public String getName() {
        return name;
    }

    public int getSlot() {
        return slot;
    }

    public Map<String, Integer> getSkills() {
        return skills;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object o){
        try{
            Jewel other = (Jewel) o;
            return this.name.equals(other.name);
        } catch(Exception e){
            return false;
        }
    }
}

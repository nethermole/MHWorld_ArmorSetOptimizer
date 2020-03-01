import java.util.Map;

public class Charm {
    private String name;
    private Map<String, Integer> skills;
    private int slot;

    public Charm(String name, Map<String, Integer> skills){
        this.name = name;
        this.skills = skills;
    }

    public String getName() {
        return name;
    }

    public Map<String, Integer> getSkills() {
        return skills;
    }
}

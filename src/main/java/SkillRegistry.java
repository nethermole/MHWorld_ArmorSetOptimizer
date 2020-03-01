import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class SkillRegistry {
    private Map<String, Integer> skills;

    public SkillRegistry(Map<String, Integer> skills){
        this.skills = skills;
    }

    public void validateSkills(Set<String> inputSkills){
        for(String skill : new ArrayList<>(inputSkills)){
            if(!skills.containsKey(skill)){
                throw new RuntimeException("Invalid skill: " + skill);
            }
        }
    }
}

package marketplaces.backend.backendrestapi.restapi.src.system.teammembers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sys/team-members")
public class TeamMemberController {

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @GetMapping
    public List<TeamMember> getTeamMembers(){
        return teamMemberRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<TeamMember> getTeamMember(@PathVariable String id){
        return teamMemberRepository.findById(id);
    }

    @PostMapping
    public void insert(@RequestBody TeamMember teamMember){
        teamMemberRepository.insert(teamMember);
    }

    @PutMapping
    public void update(@RequestBody TeamMember teamMember){
        teamMemberRepository.save(teamMember);
    }

    @DeleteMapping
    public void delete(@PathVariable String id){
        teamMemberRepository.deleteById(id);
    }
}
package marketplaces.backend.backendrestapi.restapi.src.system.teammembers;

import marketplaces.backend.backendrestapi.config.global.filtering.Filtering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sys/team-members")
public class TeamMemberController {

    @Autowired
    private TeamMemberRepository teamMemberRepository;
    @Autowired
    private TeamMemberService teamMemberService;

    @GetMapping
    public Page<TeamMember> getTeamMembers(@RequestHeader int size,
                                           @RequestHeader int page,
                                           @RequestHeader String text,
                                           @RequestHeader int status){
        return teamMemberService.find(new Filtering(size, page, text, status));
    }

    @GetMapping("/{id}")
    public Optional<TeamMember> getTeamMember(@PathVariable String id){
        return teamMemberRepository.findById(id);
    }

    @PostMapping
    public void insert(@RequestBody TeamMember teamMember){
        teamMemberService.insert(teamMember);
    }

    @PutMapping
    public void update(@RequestBody TeamMember teamMember){
        teamMemberService.update(teamMember);
    }

    @DeleteMapping
    public void delete(@PathVariable String id){
        teamMemberRepository.deleteById(id);
    }
}
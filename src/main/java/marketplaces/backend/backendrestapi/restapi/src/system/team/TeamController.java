package marketplaces.backend.backendrestapi.restapi.src.system.team;

import marketplaces.backend.backendrestapi.config.global.filtering.Filtering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/sys/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;
    @Autowired
    private TeamRepository teamRepository;

    @GetMapping
    public Page<Team> getTeams(@RequestHeader int size,
                               @RequestHeader int page,
                               @RequestHeader String text,
                               @RequestHeader int status){
        return teamService.find(new Filtering(size, page, text, status));
    }

    @GetMapping("/{id}")
    public Optional<Team> getTeam(@PathVariable String id){
        return teamRepository.findById(id);
    }

    @PostMapping
    public void insert(@RequestBody Team team){
        teamService.insert(team);
    }

    @PutMapping
    public void update(@RequestBody Team team){
        teamService.update(team);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        teamService.delete(id);
    }
}

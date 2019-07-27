package marketplaces.backend.backendrestapi.restapi.src.system.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sys/teams")
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;

    @GetMapping
    public List<Team> getTeams(){
        return teamRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Team> getTeam(@PathVariable String id){
        return teamRepository.findById(id);
    }

    @PostMapping
    public void insert(@RequestBody Team team){
        teamRepository.insert(team);
    }

    @PutMapping
    public void update(@RequestBody Team team){
        teamRepository.save(team);
    }

    @DeleteMapping void delete(@PathVariable String id){
        teamRepository.deleteById(id);
    }
}

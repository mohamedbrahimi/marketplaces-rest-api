package marketplaces.backend.backendrestapi.restapi.src.system.pack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sys/packs")
public class PackController {

    @Autowired
    PackRepository packRepository;

    @GetMapping
    public List<Pack> getPacks(){
        return packRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Pack> getPack(@PathVariable String id){
        return packRepository.findById(id);
    }

    @PostMapping
    public void insert(@RequestBody Pack pack){
        packRepository.insert(pack);
    }

    @PutMapping
    public void update(@RequestBody Pack pack){
        packRepository.save(pack);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        packRepository.deleteById(id);
    }

}

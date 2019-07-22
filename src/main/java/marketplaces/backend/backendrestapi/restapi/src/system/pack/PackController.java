package marketplaces.backend.backendrestapi.restapi.src.system.pack;

import marketplaces.backend.backendrestapi.config.global.filtering.Filtering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sys/packs")
public class PackController {

    @Autowired
    PackService packService;
    @Autowired
    PackRepository packRepository;

    @GetMapping
    public Page<Pack> getPacks(
            @RequestHeader int size,
            @RequestHeader int page,
            @RequestHeader String text,
            @RequestHeader int status){
        return packService.find(new Filtering(size, page, text, status));
    }

    @GetMapping("/{id}")
    public Optional<Pack> getPack(@PathVariable String id){
        return packRepository.findById(id);
    }

    @PostMapping
    public void insert(@RequestBody Pack pack){
        packService.insert(pack);
    }

    @PutMapping
    public void update(@RequestBody Pack pack){
        packService.update(pack);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        packService.delete(id);
    }

}

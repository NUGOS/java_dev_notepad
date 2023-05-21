package go.it.java_notepad.endpoint;

import go.it.java_notepad.entity.AccessType;
import go.it.java_notepad.entity.Note;
import go.it.java_notepad.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/note")
public class NoteController {
    private final NoteService noteService;
    private final ConversionService conversionService;

    @GetMapping("/list")
    public ModelAndView list() {
        ModelAndView result = new ModelAndView("note-list");
        result.addObject("noteList", noteService.listAllByAutor());
        result.addObject("author", noteService.author());
        return result;
    }

    @GetMapping("/list-all")
    public ModelAndView listAll() {
        ModelAndView result = new ModelAndView("note-list-all");
        List<Note> noteListAll = noteService.listAllByPublic();
        String[] authors = new String[noteListAll.size()];
        for (int i = 0; i < noteListAll.size(); i++) {
            Note note = noteListAll.get(i);
            String authorName = noteService.getAuthorNameById(note.getId());
            authors[i] = authorName;
        }
        result.addObject("noteListAll", noteListAll);
        result.addObject("authors", authors);
        result.addObject("author", noteService.author());
        return result;
    }



    @PostMapping("/delete")
    public RedirectView delete(@RequestParam long id) {
        noteService.deleteById(id);
        return new RedirectView("/note/list");
    }

    @GetMapping("/edit")
    public ModelAndView edit(@RequestParam long id) {
        ModelAndView result = new ModelAndView("note-create-edit");
        result.addObject("note", noteService.getById(id));
        return result;
    }

    @PostMapping("/edit")
    public ModelAndView edit(@RequestParam long id,
                             @RequestParam String title,
                             @RequestParam String content,
                             @RequestParam String accessType) {
        final Note note = new Note(id, title, content,
                conversionService.convert(accessType, AccessType.class),
                noteService.getUserId());
        final ModelAndView modelAndView = new ModelAndView("note-create-edit-error-page");

        if (!noteService.isTitleValid(note)) {
            return modelAndView.addObject("cause", "title");
        }
        if (!noteService.isContentValid(note)) {
            return modelAndView.addObject("cause", "content");
        }
        noteService.update(note);
        return new ModelAndView(new RedirectView("/note/list"));
    }

    @GetMapping("/create")
    public ModelAndView add() {
        ModelAndView result = new ModelAndView("note-create-edit");
        result.addObject("note", null);
        return result;
    }

    @PostMapping("/create")
    public ModelAndView add(@RequestParam String title,
                            @RequestParam String content,
                            @RequestParam String accessType) {
        final Note note = new Note(title, content,
                conversionService.convert(accessType, AccessType.class),
                noteService.getUserId());
        final ModelAndView modelAndView = new ModelAndView("note-create-edit-error-page");

        if (!noteService.isTitleValid(note)) {
            return modelAndView.addObject("cause", "title");
        }
        if (!noteService.isContentValid(note)) {
            return modelAndView.addObject("cause", "content");
        }
        noteService.add(note);
        return new ModelAndView(new RedirectView("/note/list"));
    }

    @GetMapping("/note-share")
    public ModelAndView share(@RequestParam long id) {
        ModelAndView result = new ModelAndView("note-share");
        Note note;
        try {
            note = noteService.getById(id);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            result.addObject("note", null);
            return result;
        }
        if (note.getAccess().equals(AccessType.PUBLIC)) {
            result.addObject("note", note);
        } else {
            result.addObject("note", null);
        }
        return result;
    }
}


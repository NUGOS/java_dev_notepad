package go.it.java_notepad.service;

import go.it.java_notepad.entity.AccessType;
import go.it.java_notepad.entity.Note;
import go.it.java_notepad.entity.User;
import go.it.java_notepad.repository.NoteRepository;
import go.it.java_notepad.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final Random random = new Random();
    private final UserRepository userRepository;

    public List<Note> listAllByAutor() {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        User userObject = userRepository.findByUsername(user);

        if (userObject == null) {
            throw new IllegalStateException("User not found");
        }

        return noteRepository.findAll()
                .stream()
                .filter(o -> Objects.equals(o.getUser_id(), userObject.getUser_id()))
                .collect(Collectors.toList());
    }

    public List<Note> listAllByPublic() {
        return noteRepository.findAll()
                .stream()
                .filter(o->Objects.equals(o.getAccess(), AccessType.PUBLIC))
                .collect(Collectors.toList());
    }

    public String getAuthorNameById(long id) {
        Note note = noteRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Long userId = note.getUser_id();
        User author = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        return author.getUsername();
    }



    public String author() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return authentication.getName();
    }

    public Note add(Note note) {
        note.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        noteRepository.save(note);
        return note;
    }

    public void deleteById(long id) {
        noteRepository.deleteById(id);
    }

    public void update(Note note) {
        long id = note.getId();
        if (!noteRepository.existsById(id)) {
            throw new IllegalArgumentException("Note with id=" + id + " does not exist");
        }
        noteRepository.save(note);
    }

    public Note getById(long id) {
        Note note;
        try {
            note = noteRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        } catch (EntityNotFoundException e) {
            throw new IllegalArgumentException("Note with id=" + id + " does not exist");
        }
        return note;
    }

    public Long getUserId() {
        final User principal = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return principal.getUser_id();
    }

    public boolean isTitleValid(Note note) {
        return note.getTitle().length() >= 5 && note.getTitle().length() <= 100;
    }

    public boolean isContentValid(Note note) {
        return note.getContent().length() >= 5 && note.getContent().length() <= 10000;
    }

}

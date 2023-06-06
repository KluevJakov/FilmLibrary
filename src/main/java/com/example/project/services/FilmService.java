package com.example.project.services;

import com.example.project.models.*;
import com.example.project.models.dto.FilmDto;
import com.example.project.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LikedRepository likedRepository;
    @Autowired
    private UnlikedRepository unlikedRepository;
    @Autowired
    private ThrownRepository thrownRepository;
    @Autowired
    private ViewedRepository viewedRepository;

    public List<Film> getFilms(String search) {
        if (search != null && !search.isEmpty()) {
            return filmRepository.findFilmsSearch(search.toLowerCase());
        } else {
            return filmRepository.findFilms();
        }
    }

    public void addFilm(Film film) {
        filmRepository.save(film);
    }

    public Film filmById(Long id) {
        return filmRepository.findById(id).get();
    }

    public void removeFilm(Long id) {
        likedRepository.removeByFilmId(id);
        unlikedRepository.removeByFilmId(id);
        viewedRepository.removeByFilmId(id);
        thrownRepository.removeByFilmId(id);
        filmRepository.deleteById(id);
    }

    public List<Film> recs(User user) {
        if (viewedRepository.countByUserId(user.getId()) == 0 &&
                likedRepository.countByUserId(user.getId()) == 0 &&
                unlikedRepository.countByUserId(user.getId()) == 0 &&
                thrownRepository.countByUserId(user.getId()) == 0) {
            return filmRepository.findStandartFilms();
        }
        return calculateFilms(user);
    }

    private List<Film> calculateFilms(User user) {
        List<User> users = userRepository.findUsers().stream() //список всех пользователей без текущего
                .filter(e -> e.getId() != user.getId())
                .collect(Collectors.toList());

        List<Film> films = filmRepository.findAll(); //список всех фильмов

        List<Liked> likeds = likedRepository.findAllWithoutMe(user.getId());//список чужих лайкнутых
        List<Unliked> unlikeds = unlikedRepository.findAllWithoutMe(user.getId());//список чужих дизлайкнутых
        List<Thrown> throwns = thrownRepository.findAllWithoutMe(user.getId());//список чужих пропущенных
        List<Viewed> vieweds = viewedRepository.findAllWithoutMe(user.getId());//список чужих просмотренных

        //всё тоже самое, только текущего пользователя
        List<Liked> myLikeds = likedRepository.findMy(user.getId());
        List<Unliked> myUnlikeds = unlikedRepository.findMy(user.getId());
        List<Thrown> myThrown = thrownRepository.findMy(user.getId());
        List<Viewed> myViewed = viewedRepository.findMy(user.getId());

        HashMap<User, Integer> rating = new HashMap<>(); //сюда будем записывать рейтинг совместимости

        for (User u : users) {
            rating.put(u, 0);
        }

        for (Liked y : likeds) {
            for (Liked m : myLikeds) {
                if (Objects.equals(y.getFilm_id(), m.getFilm_id())) {
                    rating.merge(loadedUser(y.getUser_id(), users), 3, Integer::sum); //если совпало то, что нравится +3 балла
                }
            }
        }

        for (Unliked y : unlikeds) {
            for (Unliked m : myUnlikeds) {
                if (Objects.equals(y.getFilm_id(), m.getFilm_id())) {
                    rating.merge(loadedUser(y.getUser_id(), users), 2, Integer::sum); //если совпало то, что не нравится +2 балла
                }
            }
        }

        for (Thrown y : throwns) {
            for (Thrown m : myThrown) {
                if (Objects.equals(y.getFilm_id(), m.getFilm_id())) {
                    rating.merge(loadedUser(y.getUser_id(), users), 1, Integer::sum); //если совпало то, что пропустили +1 балл
                }
            }
        }

        for (Viewed y : vieweds) {
            for (Viewed m : myViewed) {
                if (Objects.equals(y.getFilm_id(), m.getFilm_id())) {
                    rating.merge(loadedUser(y.getUser_id(), users), 1, Integer::sum); //если совпало то, что просмотрели +1 балл
                }
            }
        }

        List<User> sortedUsers = rating.entrySet().stream() //формируем рейтинг совместимости по убыванию
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .limit(5) //берем только 5 наиболее подходящих кандидатов
                .collect(Collectors.toList());

        List<Liked> likedList = new ArrayList<>();
        for (User mate : sortedUsers) { //добавляем их любимые фильмы
            likedList.addAll(likedRepository.findMy(mate.getId()));
        }

        List<Film> resultList = new ArrayList<>();
        for (Liked liked : likedList) {
            if (myUnlikeds.stream().noneMatch(e -> Objects.equals(e.getFilm_id(), liked.getFilm_id()))) { //проверяем, что их нет у нас в нелюбимых
                resultList.add(loadedFilm(liked.getFilm_id(), films));
            }
        }

        return resultList.stream().distinct().limit(10).collect(Collectors.toList()); //оставляем только уникальные и берем только 10 фильмов
    }

    private User loadedUser(Long id, List<User> users) {
        return users.stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().get();
    }

    private Film loadedFilm(Long id, List<Film> films) {
        return films.stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().get();
    }

    public FilmDto filmDtoById(Long filmId, Long userId) {
        FilmDto filmDto = new FilmDto();
        Film film = filmRepository.findById(filmId).get();
        filmDto.setTitle(film.getTitle());
        filmDto.setGenre(film.getGenre());
        filmDto.setYear(film.getYear());
        filmDto.setCountry(film.getCountry());
        filmDto.setDirector(film.getDirector());
        filmDto.setScenarist(film.getScenarist());
        filmDto.setProducer(film.getProducer());
        filmDto.setBudget(film.getBudget());
        filmDto.setFee(film.getFee());
        filmDto.setTime(film.getTime());
        filmDto.setAgeRestriction(film.getAgeRestriction());
        filmDto.setImage(film.getImage());

        filmDto.setLike(0);
        filmDto.setView(0);

        if (likedRepository.selectByUserIdAndFilmId(userId, filmId) != 0) {
            filmDto.setLike(1);
        } else if (unlikedRepository.selectByUserIdAndFilmId(userId, filmId) != 0) {
            filmDto.setLike(2);
        }

        if (viewedRepository.selectByUserIdAndFilmId(userId, filmId) != 0) {
            filmDto.setView(1);
        } else if (thrownRepository.selectByUserIdAndFilmId(userId, filmId) != 0) {
            filmDto.setView(2);
        }

        return filmDto;
    }

    public void processAction(Long filmId, Long action, Long userId) {
        if (action == 1) {
            Liked liked = new Liked();
            liked.setFilm_id(filmId);
            liked.setUser_id(userId);
            likedRepository.save(liked);
            unlikedRepository.removeByUserIdAndFilmId(userId, filmId);
        } else if (action == 2) {
            Unliked unliked = new Unliked();
            unliked.setFilm_id(filmId);
            unliked.setUser_id(userId);
            unlikedRepository.save(unliked);
            likedRepository.removeByUserIdAndFilmId(userId, filmId);
        } else if (action == 3) {
            Viewed viewed = new Viewed();
            viewed.setFilm_id(filmId);
            viewed.setUser_id(userId);
            viewedRepository.save(viewed);
            thrownRepository.removeByUserIdAndFilmId(userId, filmId);
        } else if (action == 4) {
            Thrown thrown = new Thrown();
            thrown.setFilm_id(filmId);
            thrown.setUser_id(userId);
            thrownRepository.save(thrown);
            viewedRepository.removeByUserIdAndFilmId(userId, filmId);
        }
    }
}

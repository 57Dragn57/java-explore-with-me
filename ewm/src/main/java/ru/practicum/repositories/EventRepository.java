package ru.practicum.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.stats.State;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByInitiatorId(long userId, Pageable pageable);

    Page<Event> findByInitiatorInAndStateInAndCategoryInAndEventDateBetween(Collection<User> initiator,
                                                                            Collection<State> state,
                                                                            Collection<Category> category,
                                                                            LocalDateTime eventDate,
                                                                            LocalDateTime eventDate2,
                                                                            Pageable pageable);

    @Query(value = "select *" +
            " from events" +
            " where ANNOTATION ilike ?1 or DESCRIPTION ilike ?1" +
            " and CATEGORY in ?2" +
            " and PAID = ?3" +
            " and EVENT_DATE between ?4 and ?5" +
            " and (PARTICIPANTLIMIT = 0 or ?6 = false or PARTICIPANTLIMIT > CONFIRMEDREQUESTS)", nativeQuery = true)
    Page<Event> findEventsByFilters(String text,
                                    List<Long> categories,
                                    Boolean paid,
                                    LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd,
                                    Boolean onlyAvailable,
                                    Pageable pageable);


    List<Event> findByCategoryId(long catId);
}

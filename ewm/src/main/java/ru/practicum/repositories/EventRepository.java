package ru.practicum.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.Event;
import ru.practicum.stats.State;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByInitiatorId(long userId, Pageable pageable);

 /*   @Query(value = "select *" +
            " from events" +
            " where (?1 is null or INITIATOR in ?1)  " +
            " and ((?2) is null or STATE in (?2))" +
            " and ((?3) is null or CATEGORY in (?3))" +
            " and ((?4) is null or EVENT_DATE > (?4))" +
            " and ((?5) is null or EVENT_DATE < (?5)) ", nativeQuery = true)
    Page<Event> searchEvents(Collection<Long> initiator,
                             Collection<String> state,
                             Collection<Long> category,
                             LocalDateTime eventDate,
                             LocalDateTime eventDate2,
                             Pageable pageable); */

    @Query(value = "select e " +
            " from Event e " +
            " where (:ids is null or e.initiator.id in :ids) " +
            " AND (:state is null or e.state in :state)" +
            " and (:cat is null or e.category.id in :cat)" +
            " AND (e.eventDate between coalesce(:start, e.eventDate) and coalesce(:end, e.eventDate))")
    Page<Event> searchEvents(@Param("ids") Collection<Long> initiator,
                             @Param("state") Collection<State> state,
                             @Param("cat") Collection<Long> category,
                             @Param("start") LocalDateTime eventDate,
                             @Param("end") LocalDateTime eventDate2,
                             Pageable pageable);

    @Query(value =
            "select e " +
                    " from Event e " +
                    " where (:text is null" +
                    " or lower(e.annotation) like concat('%', lower(:text), '%')" +
                    " or lower(e.description) like concat('%', lower(:text), '%'))" +
                    " and (:cat is null or e.category.id in :cat)" +
                    " and (:paid is null or e.paid = :paid)" +
                    " and (e.eventDate between coalesce(:start, e.eventDate) and coalesce(:end, e.eventDate)" +
                    " and :available is null or e.participantLimit = 0 or e.confirmedRequests < e.participantLimit)")
    Page<Event> findEventsByFilters(@Param("text") String text,
                                    @Param("cat") List<Long> categories,
                                    @Param("paid") Boolean paid,
                                    @Param("start") LocalDateTime rangeStart,
                                    @Param("end") LocalDateTime rangeEnd,
                                    @Param("available") Boolean onlyAvailable,
                                    Pageable pageable);

  /*  @Query(value =
            "SELECT * FROM events " +
                    "WHERE ((?1) IS NULL OR ANNOTATION ILIKE (?1) OR DESCRIPTION ILIKE (?1)) " +
                    "AND ((?2) IS NULL OR CATEGORY IN (?2)) " +
                    "AND ((?3) IS NULL OR PAID = (?3)) " +
                    "AND ((?4) IS NULL OR EVENT_DATE >= (?4)) " +
                    "AND ((?5) IS NULL OR EVENT_DATE <= (?5)) " +
                    "AND ((?6) IS NULL OR PARTICIPANT_LIMIT = 0 OR CONFIRMED_REQUESTS < PARTICIPANT_LIMIT) ", nativeQuery = true)
    Page<Event> findEventsByFilters(@Param("text") String text,
                                    @Param("cat") List<Long> categories,
                                    @Param("paid") Boolean paid,
                                    @Param("start") LocalDateTime rangeStart,
                                    @Param("end") LocalDateTime rangeEnd,
                                    @Param("available") Boolean onlyAvailable,
                                    Pageable pageable); */


    List<Event> findByCategoryId(long catId);

    Boolean existsByCategoryId(long catId);
}

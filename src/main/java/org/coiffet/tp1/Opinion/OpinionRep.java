package org.coiffet.tp1.Opinion;

import org.springframework.data.repository.CrudRepository;

public interface OpinionRep extends CrudRepository<Opinion, Long> {

    Opinion getOpinionById(long id);
}

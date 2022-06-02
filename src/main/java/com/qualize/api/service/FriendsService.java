package com.qualize.api.service;

import com.qualize.api.domain.Friends;
import com.qualize.api.repository.FriendsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Friends}.
 */
@Service
@Transactional
public class FriendsService {

    private final Logger log = LoggerFactory.getLogger(FriendsService.class);

    private final FriendsRepository friendsRepository;

    public FriendsService(FriendsRepository friendsRepository) {
        this.friendsRepository = friendsRepository;
    }

    /**
     * Save a friends.
     *
     * @param friends the entity to save.
     * @return the persisted entity.
     */
    public Friends save(Friends friends) {
        log.debug("Request to save Friends : {}", friends);
        return friendsRepository.save(friends);
    }

    /**
     * Update a friends.
     *
     * @param friends the entity to save.
     * @return the persisted entity.
     */
    public Friends update(Friends friends) {
        log.debug("Request to save Friends : {}", friends);
        return friendsRepository.save(friends);
    }

    /**
     * Partially update a friends.
     *
     * @param friends the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Friends> partialUpdate(Friends friends) {
        log.debug("Request to partially update Friends : {}", friends);

        return friendsRepository
            .findById(friends.getId())
            .map(existingFriends -> {
                if (friends.getFriendName() != null) {
                    existingFriends.setFriendName(friends.getFriendName());
                }
                if (friends.getEmail() != null) {
                    existingFriends.setEmail(friends.getEmail());
                }
                if (friends.getTelephone() != null) {
                    existingFriends.setTelephone(friends.getTelephone());
                }
                if (friends.getPhoneNumber() != null) {
                    existingFriends.setPhoneNumber(friends.getPhoneNumber());
                }
                if (friends.getWalletId() != null) {
                    existingFriends.setWalletId(friends.getWalletId());
                }
                if (friends.getDefaultCryptoCurrency() != null) {
                    existingFriends.setDefaultCryptoCurrency(friends.getDefaultCryptoCurrency());
                }
                if (friends.getDateAdded() != null) {
                    existingFriends.setDateAdded(friends.getDateAdded());
                }
                if (friends.getDateModified() != null) {
                    existingFriends.setDateModified(friends.getDateModified());
                }

                return existingFriends;
            })
            .map(friendsRepository::save);
    }

    /**
     * Get all the friends.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Friends> findAll(Pageable pageable) {
        log.debug("Request to get all Friends");
        return friendsRepository.findAll(pageable);
    }

    /**
     * Get one friends by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Friends> findOne(Long id) {
        log.debug("Request to get Friends : {}", id);
        return friendsRepository.findById(id);
    }

    /**
     * Delete the friends by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Friends : {}", id);
        friendsRepository.deleteById(id);
    }
}

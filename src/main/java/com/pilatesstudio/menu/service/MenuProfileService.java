package com.pilatesstudio.menu.service;

import com.pilatesstudio.menu.entity.Menu;
import com.pilatesstudio.menu.entity.MenuProfile;
import com.pilatesstudio.menu.repository.MenuProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuProfileService {

    private final MenuProfileRepository menuProfileRepository;

    public List<MenuProfile> findAll() {
        return menuProfileRepository.findAll();
    }

    public List<Long> findProfileIdsByMenuId(Long menuId) {
        return menuProfileRepository.findAllByMenuId(menuId)
                .stream()
                .map(MenuProfile::getProfileId)
                .toList();
    }

    public List<Menu> findActiveMenusByProfileCode(String profileCode) {
        return menuProfileRepository.findActiveMenusByProfileCode(profileCode);
    }

    @Transactional
    public void updateProfiles(Long menuId, List<Long> profileIds) {

        List<Long> requestedProfileIds =
                profileIds == null
                        ? List.of()
                        : profileIds;

        List<MenuProfile> existing =
                menuProfileRepository.findAllByMenuId(menuId);

        Set<Long> existingProfileIds = existing.stream()
                .map(MenuProfile::getProfileId)
                .collect(Collectors.toSet());

        Set<Long> requestedIds =
                new HashSet<>(requestedProfileIds);

        // Artık seçilmeyenleri sil
        existing.stream()
                .filter(menuProfile ->
                        !requestedIds.contains(menuProfile.getProfileId()))
                .forEach(menuProfileRepository::delete
                );

        // Yeni seçilenleri ekle
        List<MenuProfile> newMenuProfiles =
                requestedIds.stream()
                        .filter(profileId ->
                                !existingProfileIds.contains(profileId))
                        .map(profileId -> {
                            MenuProfile menuProfile = new MenuProfile();
                            menuProfile.setMenuId(menuId);
                            menuProfile.setProfileId(profileId);
                            return menuProfile;
                        })
                        .toList();

        if (!newMenuProfiles.isEmpty()) {
            menuProfileRepository.saveAll(newMenuProfiles);
        }
    }
}
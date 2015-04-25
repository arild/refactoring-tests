package com.tidal.playlist.data;

import java.util.*;


/**
 * A very simplified version of TrackPlaylist
 */
public class TrackPlayList {

    private static final long serialVersionUID = -7939232635387466929L;

    private Integer id;
    private String playListName;
    private Set<PlayListTrack> playListTracks = new HashSet<PlayListTrack>();
    private PlayListType playListType;
    private String description;
    private Date registeredDate;
    private Date lastUpdated;
    private int createdByArtistId;
    private String createdByArtistName;
    private String uuid;
    private int nrOfTracks;
    private boolean deleted;
    private SharingLevel sharingLevel;
    private String createdByNickName;
    private Float duration;
    private Integer userId;

    private String imageSources;

    public TrackPlayList() {
        this.uuid = UUID.randomUUID().toString();
        Date d = new Date();
        this.registeredDate = d;
        this.lastUpdated = d;
        this.playListTracks = new HashSet<PlayListTrack>();
    }


    /*TEMP need to transport to Service layer, instead of loading profile.*/
    public String getCreatedByNickName() {
        return createdByNickName;
    }

    public void setCreatedByNickName(String createdByNickName) {
        this.createdByNickName = createdByNickName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PlayListType getPlayListType() {
        return playListType;
    }

    public void setPlayListType(PlayListType playListType) {
        this.playListType = playListType;
    }

    public String getPlayListName() {
        return playListName;
    }

    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }

    public Set<PlayListTrack> getPlayListTracks() {
        return playListTracks;
    }

    public void setPlayListTracks(Set<PlayListTrack> playListTracks) {
        this.playListTracks = playListTracks;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getCreatedByArtistId() {
        return createdByArtistId;
    }

    public void setCreatedByArtistId(int createdByArtistId) {
        this.createdByArtistId = createdByArtistId;
    }

    public SharingLevel getSharingLevel() {
        return sharingLevel;
    }

    public void setSharingLevel(SharingLevel sharingLevel) {
        this.sharingLevel = sharingLevel;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void addPlayListTrack(PlayListTrack pa) {
        if (getPlayListTracks() == null || getPlayListTracks().size() == 0)
            playListTracks = new HashSet<PlayListTrack>();
        playListTracks.add(pa);
        nrOfTracks++;
    }

    public PlayListTrack getPlayListTrack(int trackId) {
        if (getPlayListTracks() == null || getPlayListTracks().size() == 0)
            return null;

        for (PlayListTrack playListTrack : getPlayListTracks()) {
            if (playListTrack.getTrackId() == trackId)
                return playListTrack;
        }
        return null;
    }

    public int getPlayListTracksSize() {
        if (getPlayListTracks() == null)
            return 0;
        else
            return getPlayListTracks().size();
    }

    public int getNrOfTracks() {
        return nrOfTracks;
    }

    public void setNrOfTracks(int nrOfTracks) {
        this.nrOfTracks = nrOfTracks;
    }

    public String getCreatedByArtistName() {
        return createdByArtistName;
    }

    public void setCreatedByArtistName(String createdByArtistName) {
        this.createdByArtistName = createdByArtistName;
    }

    public Float getDuration() {
        return duration;
    }

    /**
     * Duration in seconds
     *
     * @param duration
     */
    public void setDuration(Float duration) {
        this.duration = duration;
    }


    public Integer getUserId() {
        return userId;
    }


    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public String getImageSources() {
        return imageSources;
    }


    public void setImageSources(String imageSources) {
        this.imageSources = imageSources;
    }

    public List<PlayListTrack> getPlayListTracksSorted() {
        if (getPlayListTracks() == null) {
            return null;
        }
        else {
            ArrayList<PlayListTrack> playListTracks = new ArrayList<PlayListTrack>(getPlayListTracks());
            Collections.sort(playListTracks);
            return playListTracks;
        }
    }
}
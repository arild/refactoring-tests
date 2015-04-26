package com.tidal.playlist.dao;

/**
 * @author: eivind.hognestad@wimpmusic.com
 * Date: 15.04.15
 * Time: 13.07
 */

import com.tidal.playlist.data.PlayListTrack;
import com.tidal.playlist.data.SharingLevel;
import com.tidal.playlist.data.Track;
import com.tidal.playlist.data.TrackPlayList;

import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Class faking the data layer, and returning fake playlists
 */
public class PlaylistDaoBean {

    private final int numTracksToGenerate;
    private int playListTrackIdToStartFrom;

    public PlaylistDaoBean(int numTracksToGenerate, int playListTrackIdToStartFrom) {
        this.numTracksToGenerate = numTracksToGenerate;
        this.playListTrackIdToStartFrom = playListTrackIdToStartFrom;
    }

    public TrackPlayList getPlaylistByUUID(String uuid, int userId) {
        TrackPlayList trackPlayList = new TrackPlayList();

        trackPlayList.setDeleted(false);
        trackPlayList.setDescription("The mother of all playlists");
        trackPlayList.setDuration((float) (60 * 60 * 2));
        trackPlayList.setId(49834);
        trackPlayList.setLastUpdated(new Date());
        trackPlayList.setNrOfTracks(numTracksToGenerate);
        trackPlayList.setPlayListName("Collection of great songs");
        trackPlayList.setPlayListTracks(getPlaylistTracks(numTracksToGenerate, playListTrackIdToStartFrom));
        trackPlayList.setUserId(userId);
        trackPlayList.setSharingLevel(SharingLevel.PUBLIC);
        trackPlayList.setUuid(uuid);

        return trackPlayList;
    }

    private static Set<PlayListTrack> getPlaylistTracks(int numTracksToGenerate, int playListTrackIdToStartFrom) {

        Set<PlayListTrack> playListTracks = new HashSet<PlayListTrack>();
        for (int i = 0; i < numTracksToGenerate; i++) {
            PlayListTrack playListTrack = new PlayListTrack();
            playListTrack.setDateAdded(new Date());
            playListTrack.setDescription("A description");
            playListTrack.setId(playListTrackIdToStartFrom + i);
            playListTrack.setIndex(i);
            playListTrack.setSharingLevel(SharingLevel.PUBLIC);
            playListTrack.setTrack(getTrack());
            playListTracks.add(playListTrack);
        }

        return playListTracks;
    }

    public static Track getTrack() {
        Random randomGenerator = new Random();

        Track track = new Track();
        track.setArtistId(randomGenerator.nextInt(10000));
        track.setDuration(60 * 3);

        int trackNumber = randomGenerator.nextInt(15);
        track.setTitle("Track no: " + trackNumber);
        track.setTrackNumberIdx(trackNumber);

        return track;
    }
}
